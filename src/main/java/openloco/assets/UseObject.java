package openloco.assets;

public class UseObject {

    private final ObjectClass objectClass;
    private final String objectReference;

    public UseObject(ObjectClass objectClass, String objectReference) {
        this.objectClass = objectClass;
        this.objectReference = objectReference;
    }

    public ObjectClass getObjectClass() {
        return objectClass;
    }

    public String getObjectReference() {
        return objectReference;
    }
}
