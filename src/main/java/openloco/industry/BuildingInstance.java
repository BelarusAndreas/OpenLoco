package openloco.industry;

public class BuildingInstance {
    private final int type;
    private final int x;
    private final int y;

    public BuildingInstance(int type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
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
}
