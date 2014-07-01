package openloco.datfiles;

import openloco.entities.MultiLangString;
import openloco.entities.ObjectClass;
import openloco.entities.UseObject;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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

    public UseObject readUseObject(EnumSet<ObjectClass> objectClasses) throws IOException {
        byte objectClassId = readByte();
        ObjectClass objectClass = ObjectClass.values()[objectClassId];
        if (!objectClasses.contains(objectClass)) {
            throw new RuntimeException("Invalid object reference");
        }
        skipBytes(3);
        byte[] ref = new byte[8];
        readFully(ref);
        String objectReference = new String(ref);
        skipBytes(4);
        return new UseObject(objectClass, objectReference);
    }

    public List<UseObject> readUseObjectList(int count, ObjectClass... validObjects) throws IOException {
        EnumSet<ObjectClass> validObjectClasses = EnumSet.copyOf(Arrays.asList(validObjects));
        List<UseObject> references = new ArrayList<>();
        for (int i=0; i<count; i++) {
            references.add(readUseObject(validObjectClasses));
        }
        return references;
    }

    public MultiLangString readMultiLangString() throws IOException {
        Map<Integer, String> strings = new HashMap<>();
        byte language;
        while ((language = readByte()) != (byte)0xFF){
            StringBuffer sb = new StringBuffer();
            char ch;
            while ((ch = (char)readByte()) != (byte)0x00) {
                sb.append(ch);
            }
            strings.put((int)language, sb.toString());
        }
        return new MultiLangString(strings);
    }


    public long[] loadAux(int count, int size) throws IOException {
        long[] result = new long[count];
        for (int i=0; i<count; i++) {
            if (size == 1) {
                result[i] = readByte();
            }
            else if (size == 2) {
                result[i] = readSShort();
            }
        }
        return result;
    }
}
