package openloco.industry;

public class BuildingInstance {
    private final int type;
    private final int x;
    private final int y;
    private final int rotation;

    public BuildingInstance(int type, int x, int y, int rotation) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.rotation = rotation % 4;
    }

    public int getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRotation() {
        return rotation;
    }
}
