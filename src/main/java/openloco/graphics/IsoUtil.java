package openloco.graphics;

public class IsoUtil {
    public static float isoX(float cartX, float cartY, float cartZ) {
        return cartX - cartY;
    }

    public static float isoX(CartCoord cartCoord) {
        return isoX(cartCoord.getX(), cartCoord.getY(), cartCoord.getZ());
    }

    public static float isoY(float cartX, float cartY, float cartZ) {
        return (cartX + cartY) / 2 - cartZ;
    }

    public static float isoY(CartCoord cartCoord) {
        return isoY(cartCoord.getX(), cartCoord.getY(), cartCoord.getZ());
    }

    public static float cartX(float isoX, float isoY) {
        return (2 * isoY + isoX) / 2;
    }

    public static float cartY(float isoX, float isoY) {
        return (2 * isoY - isoX) / 2;
    }

    public static ScreenCoord calculateScreenCoord(CartCoord cartCoord) {
        int screenX = Math.round(IsoUtil.isoX(cartCoord));
        int screenY = Math.round(IsoUtil.isoY(cartCoord));
        return new ScreenCoord(screenX, screenY);
    }
}
