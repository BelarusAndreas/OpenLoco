package openloco.datfiles;

import openloco.*;
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
        logger.info("Loading assets from {}...", DATA_DIR);
        Files.list(new File(DATA_DIR).toPath())
                .forEach(this::load);
        logger.info("Finished loading assets.");
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

        int pointer = 16;

        while (pointer < bytes.length) {
            byte chunkType = bytes[pointer++];
            long length = readUint32LE(bytes, pointer);
            pointer += 4;

            byte[] chunk;
            if (chunkType == 0) {
                //no encoding
                chunk = bytes;
            }
            else if (chunkType == 1) {
                chunk = rleDecode(bytes, pointer, length);
            }
            else if (chunkType == 2) {
                //decompress doesn't seem to work...
                //chunk = rleDecode(bytes, pointer, length);
                //chunk = decompress(chunk);
                break;
            }
            else if (chunkType == 3) {
                chunk = descramble(bytes, pointer, length);
            }
            else {
                logger.error("Unsupported chunk type for {} ({}): {} ", path, objectClass, chunkType);
                break;
            }

            decodeObject(name, chunk, objectClass);
            pointer += length;
        }
    }

    private byte[] decompress(byte[] chunk) {
        List<Byte> buffer = new ArrayList<>();
        int i=0;
        while (i < chunk.length) {
            byte code = chunk[i++];
            if (code == (byte)0xFF) {
                buffer.add(chunk[i++]);
            }
            else {
                int len = (code & 7)+1;
                int ofs = 32 - (code >> 3);

                int start = buffer.size()-ofs;
                int end = start+len;

                for (int j=start; j<end; j++) {
                    buffer.add(buffer.get(start));
                }
            }
        }
        return toArray(buffer);
    }

    private void decodeObject(String name, byte[] chunk, ObjectClass objectClass) {
        chunk = ByteBuffer.wrap(chunk).order(ByteOrder.LITTLE_ENDIAN).array();
        DatFileInputStream dataInputStream = new DatFileInputStream(new ByteArrayInputStream(chunk));
        switch (objectClass) {

            case COMPANIES:
                Company company = Company.load(name, dataInputStream);
                assets.add(company);
                break;

            case GROUND:
                Ground ground = Ground.load(name, dataInputStream);
                assets.add(ground);
                break;

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

    private byte[] descramble(byte[] input, int pointer, long length) {
        byte[] result = new byte[(int)length];
        byte bits = 1;
        for (int ofs=pointer; ofs<pointer+length; ofs++) {
            result[ofs-pointer] = rotr8(input[ofs], bits);
            bits = (byte)((bits+2)&7);
        }
        return result;
    }

    private byte rotr8(byte a, byte n) {
        return (byte)(0xFF & (((a)>>(n))|((a)<<(8-n))));
    }

    private byte[] toArray(List<Byte> buffer) {
        byte[] result = new byte[buffer.size()];
        for (int i=0; i<buffer.size(); i++) {
            result[i] = buffer.get(i);
        }
        return result;
    }

}
