package openloco.datfiles;

import openloco.Assets;
import openloco.Vehicle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DatFileLoader {

    private static final Logger logger = LoggerFactory.getLogger(DatFileLoader.class);

    private final Assets assets;
    private final String DATA_DIR;

    public DatFileLoader(Assets assets, String dataDir) {
        this.assets = assets;
        this.DATA_DIR = dataDir;
    }

    public void loadFiles() throws IOException {
        Files.list(new File(DATA_DIR).toPath())
                .forEach(this::load);
    }

    private void load(Path path) {
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(path);
        }
        catch (IOException ioe) {
            logger.error("Failed to load file: {}", path);
            return;
        }

        assert bytes[3] == 0x11;

        ObjectClass objectClass = ObjectClass.values()[(bytes[0] & 0x7f)];
        long objectSubClass = readUintLE(bytes, 1, 3);
        String name = new String(bytes, 4, 8, Charset.defaultCharset());

        logger.debug("Object class: {}", objectClass);
        logger.debug("Object subclass: {}", objectSubClass);
        logger.debug("Object name: '{}'", name);

        int pointer = 16;

        int chunkCount = 0;

        while (pointer < bytes.length) {
            logger.debug("Pointer is at " + pointer + "/" + bytes.length);
            byte chunkType = bytes[pointer++];
            long length = readUint32LE(bytes, pointer);
            pointer += 4;

            logger.debug("Start of chunk type " + chunkType + " of length " + length);
            byte[] chunk;
            if (chunkType == 1) {
                chunk = rleDecode(bytes, pointer, length);
                logger.debug("Read chunk " + (chunkCount++) + " (" + chunk.length + " bytes)");
            }
            else {
                logger.error("Unsupported chunk type: " + chunkType);
                break;
            }

            decodeObject(name, chunk, objectClass);

            pointer += length;
        }
        logger.debug("Done!");
    }

    private void decodeObject(String name, byte[] chunk, ObjectClass objectClass) {
        chunk = ByteBuffer.wrap(chunk).order(ByteOrder.LITTLE_ENDIAN).array();
        DatFileInputStream dataInputStream = new DatFileInputStream(new ByteArrayInputStream(chunk));
        switch (objectClass) {
            case VEHICLES:
                Vehicle v = Vehicle.load(name, dataInputStream);
                assets.add(v);
                break;
            default:
                break;
        }
    }

    private long readUint32LE(byte[] bytes, int pointer) {
        return readUintLE(bytes, pointer, 4);
    }

    private long readUintLE(byte[] bytes, int pointer, int size) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.put(bytes, pointer, size);
        buffer.put(new byte[8-size]);
        buffer.flip();
        return buffer.order(ByteOrder.LITTLE_ENDIAN).getLong();
    }

    private byte[] rleDecode(byte[] input, int offset, long length) {
        List<Byte> buffer = new ArrayList<Byte>();
        while (length > 0) {
            byte rle = input[offset++];
            int run = Math.abs(rle)+1;
            length--;

            if (rle < 0) {
                //set this run to a particular value
                byte value = input[offset++];
                for (int i=0; i<run; i++) {
                    buffer.add(value);
                }
                length--;
            }
            else {
                //copy this run from the input
                for (int i=0; i<run; i++) {
                    byte value = input[offset++];
                    buffer.add(value);
                }
                length-=run;
            }
        }
        return toArray(buffer);
    }

    private byte[] toArray(List<Byte> buffer) {
        byte[] result = new byte[buffer.size()];
        for (int i=0; i<buffer.size(); i++) {
            result[i] = buffer.get(i);
        }
        return result;
    }

}
