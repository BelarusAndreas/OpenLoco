package openloco.graphics;

public class SpriteInstance {

    private final OpenGlSprite sprite;
    private final int screenX;
    private final int screenY;

    public SpriteInstance(OpenGlSprite sprite, int screenX, int screenY) {
        this.sprite = sprite;
        this.screenX = screenX;
        this.screenY = screenY;
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
}
