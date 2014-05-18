package openloco.graphics;

public class IsoUtil {
    public static float isoX(float cartX, float cartY, float cartZ) {
        return cartX - cartY;
    }

    public static float isoY(float cartX, float cartY, float cartZ) {
        return (cartX + cartY) / 2 - cartZ;
    }

    public static float cartX(float isoX, float isoY) {
        return (2 * isoY + isoX) / 2;
    }

    public static float cartY(float isoX, float isoY) {
        return (2 * isoY - isoX) / 2;
    }
}
