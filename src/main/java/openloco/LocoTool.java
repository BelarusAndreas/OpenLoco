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

        int objectClass = bytes[0] & 0x7f;
        //int objectSubClass = ByteBuffer.allocate(4).put((byte)0).put(bytes, 1, 3).getInt();
        String name = new String(bytes, 4, 8, Charset.defaultCharset());

        System.out.println("Object class: " + objectClass);
        //System.out.println("Object subclass: " + objectSubClass);
        System.out.println("Object name: '" + name + "'");

        int pointer = 16;

        int chunkCount = 0;

        while (pointer < bytes.length) {
            System.out.println("Pointer is at " + pointer + "/" + bytes.length);
            byte chunkType = bytes[pointer++];
            long length = readUint32LE(bytes, pointer);
            pointer += 4;

            System.out.println("Start of chunk type " + chunkType + " of length " + length);
            byte[] chunk = rleDecode(bytes, pointer, length);
            System.out.println("Read chunk " + (chunkCount++) + " (" + chunk.length + " bytes)");
            pointer += length;
        }
        System.out.println("Done!");
    }

    private static long readUint32LE(byte[] bytes, int pointer) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.put(bytes, pointer, 4);
        buffer.put(new byte[] {0, 0, 0, 0});
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
