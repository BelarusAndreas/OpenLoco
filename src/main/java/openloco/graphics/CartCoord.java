package openloco.graphics;

public class CartCoord {

    private final int x;
    private final int y;
    private final int z;

    public CartCoord(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CartCoord cartCoord = (CartCoord) o;

        if (x != cartCoord.x) return false;
        if (y != cartCoord.y) return false;
        if (z != cartCoord.z) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        result = 31 * result + z;
        return result;
    }
}
