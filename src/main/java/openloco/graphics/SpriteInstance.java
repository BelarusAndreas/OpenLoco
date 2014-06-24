package openloco.graphics;

public class SpriteInstance {

    private final OpenGlSprite sprite;
    private final int screenX;
    private final int screenY;
    private final SpriteLayer spriteLayer;
    private final int zIndex;

    public SpriteInstance(OpenGlSprite sprite, int screenX, int screenY, SpriteLayer spriteLayer, int zIndex) {
        this.sprite = sprite;
        this.screenX = screenX;
        this.screenY = screenY;
        this.spriteLayer = spriteLayer;
        this.zIndex = zIndex;
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

    public int getZIndex() {
        return zIndex;
    }
}
