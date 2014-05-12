package openloco.entities;

public class UseObject {

    private final ObjectClass objectClass;
    private String objectReference;

    public UseObject(ObjectClass objectClass, String objectReference) {
        this.objectClass = objectClass;
        this.objectReference = objectReference;
    }

}
