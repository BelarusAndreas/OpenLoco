package openloco.datfiles;

import java.io.IOException;
import java.util.EnumSet;

public class UseObject {

    private String objectReference;

    public UseObject(DatFileInputStream inputStream, EnumSet<ObjectClass> objectClasses) {
        try {
            byte objectClassId = inputStream.readByte();
            ObjectClass objectClass = ObjectClass.values()[objectClassId];
            if (!objectClasses.contains(objectClass)) {
                throw new RuntimeException("Invalid object reference");
            }
            inputStream.skipBytes(3);
            byte[] ref = new byte[8];
            inputStream.readFully(ref);
            objectReference = new String(ref);
            inputStream.skipBytes(4);
        }
        catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}
