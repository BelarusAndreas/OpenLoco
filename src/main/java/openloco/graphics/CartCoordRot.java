package openloco.graphics;

public class CartCoordRot {

    private final CartCoord cartCoord;
    private final double rotZ;

    public CartCoordRot(CartCoord cartCoord, double rotZ) {
        this.cartCoord = cartCoord;
        this.rotZ = rotZ;
    }

    public CartCoord getCartCoord() {
        return cartCoord;
    }

    public double getRotZ() {
        return rotZ;
    }
}
