package openloco.graphics;

import openloco.util.ChainComparator;

import java.util.Comparator;

public class SpriteInstance {

    private static final Comparator<SpriteInstance> COMPARE_LAYERS = (s, t)  -> s.getSpriteLayer().compareTo(t.getSpriteLayer());
    private static final Comparator<SpriteInstance> COMPARE_Z_INDEX = (s, t) -> s.getCartCoord().getZ() - t.getCartCoord().getZ();
    private static final Comparator<SpriteInstance> COMPARE_CART_X = (s, t) -> s.getCartCoord().getX() - t.getCartCoord().getX();
    private static final Comparator<SpriteInstance> COMPARE_CART_Y = (s, t) -> s.getCartCoord().getY() - t.getCartCoord().getY();

    @SuppressWarnings("unchecked")
    public static final Comparator<SpriteInstance>  SPRITE_DEPTH_COMPARATOR = new ChainComparator<>(COMPARE_CART_X, COMPARE_Z_INDEX, COMPARE_CART_Y, COMPARE_LAYERS);

    private final OpenGlSprite sprite;
    private final ScreenCoord screenCoord;
    private final SpriteLayer spriteLayer;
    private final CartCoord cartCoord;

    public SpriteInstance(OpenGlSprite sprite, int screenX, int screenY, SpriteLayer spriteLayer, int cartX, int cartY, int cartZ) {
        this.sprite = sprite;
        this.screenCoord = new ScreenCoord(screenX, screenY);
        this.spriteLayer = spriteLayer;
        this.cartCoord = new CartCoord(cartX, cartY, cartZ);
    }

    public SpriteInstance(OpenGlSprite sprite, SpriteLayer spriteLayer, CartCoord cartCoord) {
        this.sprite = sprite;
        this.screenCoord = IsoUtil.calculateScreenCoord(cartCoord);
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
