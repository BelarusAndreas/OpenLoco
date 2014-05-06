package openloco.datfiles;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;

public class DatFileInputStream extends DataInputStream {

    public DatFileInputStream(InputStream in) {
        super(in);
    }

    public <T extends Enum<T>> EnumSet<T> readBitField(int width, Class<T> bitFieldClass) throws IOException {
        EnumSet<T> values = EnumSet.noneOf(bitFieldClass);
        T[] fieldValues = bitFieldClass.getEnumConstants();

        byte[] bytes = new byte[width];
        readFully(bytes);

        for (int i=0; i<8*width && i<fieldValues.length; i++) {
            int byteIndex = i/8;
            int bitIndex = i%8;
            if (((bytes[byteIndex] >> bitIndex) & 1) == 1) {
                values.add(fieldValues[i]);
            }
        }
        return values;
    }

    public short readSShort() throws IOException {
        return (short)(((0xFF & readByte())) | ((0xFF & readByte()) << 8));
    }

    public int readUShort() throws IOException {
        return ((0xFF & readByte())) | ((0xFF & readByte()) << 8);
    }

    public long readUnsignedInt() throws IOException {
        byte[] bytes = new byte[4];
        readFully(bytes);
        return (long) ((0xFF & bytes[0]) | ((0xFF & bytes[1]) << 8) | ((0xFF & bytes[2]) << 16) | ((0xFF & bytes[3]) << 24));
    }

}
