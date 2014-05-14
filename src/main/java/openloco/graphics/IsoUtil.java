package openloco.graphics;

public class IsoUtil {
    public static int isoX(int cartX, int cartY, int cartZ) {
        return cartX - cartY;
    }

    public static int isoY(int cartX, int cartY, int cartZ) {
        return (cartX + cartY) / 2 - cartZ;
    }
}
