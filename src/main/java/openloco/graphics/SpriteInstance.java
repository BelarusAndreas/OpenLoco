package openloco.graphics;

public class SpriteInstance {

    private final OpenGlSprite sprite;
    private final int screenX;
    private final int screenY;
    private final SpriteLayer spriteLayer;
    private final int cartX;
    private final int cartY;
    private final int cartZ;

    public SpriteInstance(OpenGlSprite sprite, int screenX, int screenY, SpriteLayer spriteLayer, int cartX, int cartY, int cartZ) {
        this.sprite = sprite;
        this.screenX = screenX;
        this.screenY = screenY;
        this.spriteLayer = spriteLayer;
        this.cartX = cartX;
        this.cartY = cartY;
        this.cartZ = cartZ;
    }

    public OpenGlSprite getSprite() {
        return sprite;
    }

    public int getScreenX() {
        return screenX;
    }

    public int getScreenY() {
        return screenY;
    }

    public SpriteLayer getSpriteLayer() {
        return spriteLayer;
    }

    public int getCartX() {
        return cartX;
    }

    public int getCartY() {
        return cartY;
    }

    public int getCartZ() {
        return cartZ;
    }

}
