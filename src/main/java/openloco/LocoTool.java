package openloco;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class LocoTool {

    public static void main(String[] args) throws IOException {
        final String DATA_DIR = args[0];
        decode(DATA_DIR + "/ObjData/777.dat");
    }

    private static void decode(String datFile) throws IOException {
        File f = new File(datFile);
        byte[] bytes = Files.readAllBytes(f.toPath());

        assert bytes[3] == 0x11;

        ObjectClass objectClass = ObjectClass.values()[(bytes[0] & 0x7f)];
        long objectSubClass = readUintLE(bytes, 1, 3);
        String name = new String(bytes, 4, 8, Charset.defaultCharset());

        System.out.println("Object class: " + objectClass);
        System.out.println("Object subclass: " + objectSubClass);
        System.out.println("Object name: '" + name + "'");

        int pointer = 16;

        int chunkCount = 0;

        while (pointer < bytes.length) {
            System.out.println("Pointer is at " + pointer + "/" + bytes.length);
            byte chunkType = bytes[pointer++];
            long length = readUint32LE(bytes, pointer);
            pointer += 4;

            System.out.println("Start of chunk type " + chunkType + " of length " + length);
            byte[] chunk;
            if (chunkType == 1) {
                chunk = rleDecode(bytes, pointer, length);
                System.out.println("Read chunk " + (chunkCount++) + " (" + chunk.length + " bytes)");
            }
            else {
                System.err.println("Unsupported chunk type: " + chunkType);
                break;
            }

            dumpChunk(chunk, objectClass);

            pointer += length;
        }
        System.out.println("Done!");
    }

    private static void dumpChunk(byte[] chunk, ObjectClass objectClass) {
        switch (objectClass) {
            case VEHICLES:
                dumpVariables(chunk);
                break;
            default:
                break;
        }
    }

    private static void dumpVariables(byte[] chunkData) {
        /*
        //
        // definition of an object structure variable
        //
        struct s_varinf {
            int ofs;		// offset counted from the beginning of the structure
            int size;		// size in bytes, negative for signed variables
            int num;		// number of entries (for arrays)
            const char *name;	// name, if known; if empty (*not* NULL!) will use generic field_## name
            struct s_varinf *structvars;	// if not NULL, sub-structure definition
            const char **flags;		// if not NULL, bit field definition (pointer to list of *char bit names)
        };
        typedef struct s_varinf varinf;
         */
    }

    private static long readUint32LE(byte[] bytes, int pointer) {
        return readUintLE(bytes, pointer, 4);
    }

    private static long readUintLE(byte[] bytes, int pointer, int size) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.put(bytes, pointer, size);
        buffer.put(new byte[8-size]);
        buffer.flip();
        return buffer.order(ByteOrder.LITTLE_ENDIAN).getLong();
    }

    private static byte[] rleDecode(byte[] input, int offset, long length) {
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

    private static byte[] toArray(List<Byte> buffer) {
        byte[] result = new byte[buffer.size()];
        for (int i=0; i<buffer.size(); i++) {
            result[i] = buffer.get(i);
        }
        return result;
    }

}
