package openloco;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class LocoTool {

    public static void main(String[] args) throws IOException {
        decode("/Users/tim/Desktop/loco_dat/ObjData/777.dat");
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

        int pointer = 0x10;

        byte chunkType = bytes[pointer++];
        int length = 0x0000FFFF & ByteBuffer.wrap(bytes, pointer, 4).getInt();
        pointer += 4;

        System.out.println("Start of chunk type " + chunkType + " of length " + Integer.toUnsignedString(length));
        byte[] chunk = rleDecode(bytes, pointer, length);
        System.out.println("Read " + chunk.length + "bytes");
        System.out.println("Done!");
    }

    private static byte[] rleDecode(byte[] input, int offset, int length) {
        //codec.c has some funky management of array sizes - see codec.c:39
        ByteBuffer buffer = ByteBuffer.allocate(length);
        //byte[] chunk = new byte[length*2];
        //int chunkOffset = 0;
        while (length > 0) {
            int rle = 0x000000FF & input[offset++];
            int run = Math.abs(rle)+1;

            if (rle < 0) {
                length--;
                byte value = input[offset++];
                for (int i=0; i<run; i++) {
                    //chunk[chunkOffset++] = value;
                    buffer.put(value);
                }
            }
            else {
                for (int i=0; i<run; i++) {
                    length--;
                    byte value = input[offset++];
                    //chunk[chunkOffset++] = value;
                    buffer.put(value);
                }
            }
        }
        return buffer.array();
    }

}
