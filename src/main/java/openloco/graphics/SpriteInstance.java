package openloco.graphics;

public class SpriteInstance {

    private final OpenGlSprite sprite;
    private final ScreenCoord screenCoord;
    private final SpriteLayer spriteLayer;
    private final CartCoord cartCoord;

    public SpriteInstance(OpenGlSprite sprite, int screenX, int screenY, SpriteLayer spriteLayer, int cartX, int cartY, int cartZ) {
        this(sprite, new ScreenCoord(screenX, screenY), spriteLayer, new CartCoord(cartX, cartY, cartZ));
    }

    public SpriteInstance(OpenGlSprite sprite, ScreenCoord screenCoord, SpriteLayer spriteLayer, CartCoord cartCoord) {
        this.sprite = sprite;
        this.screenCoord = screenCoord;
        this.spriteLayer = spriteLayer;
        this.cartCoord = cartCoord;
    }

    public OpenGlSprite getSprite() {
        return sprite;
    }

    public ScreenCoord getScreenCoord() {
        return screenCoord;
    }

    public SpriteLayer getSpriteLayer() {
        return spriteLayer;
    }

    public CartCoord getCartCoord() {
        return cartCoord;
    }

}
