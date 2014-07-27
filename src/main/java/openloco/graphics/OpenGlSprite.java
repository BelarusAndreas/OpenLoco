package openloco.graphics;

import openloco.Palette;
import openloco.entities.Sprites;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;

public class OpenGlSprite {

    private final int width;
    private final int height;
    private final int xOffset;
    private final int yOffset;
    private final int textureId;

    public OpenGlSprite(int width, int height, int xOffset, int yOffset, int textureId) {
        this.width = width;
        this.height = height;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.textureId = textureId;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getXOffset() {
        return xOffset;
    }

    public int getYOffset() {
        return yOffset;
    }

    public static OpenGlSprite createFromRawSprite(Sprites.RawSprite rawSprite) {
        Sprites.SpriteHeader header = rawSprite.getHeader();
        return OpenGlSprite.createFromPixels(rawSprite.getPixels(), header.getWidth(), header.getHeight(),
                header.getXOffset(), header.getYOffset());
    }

    public static OpenGlSprite createFromPixels(int[] pixels, int width, int height, int xOffset, int yOffset) {
        ByteBuffer buffer = BufferUtils.createByteBuffer(pixels.length * 4);

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int pixel = pixels[y * width + x];

                if (pixel == Palette.BACKGROUND) {
                    pixel &= 0x00FFFFFF;
                }
                else if (Palette.isCompanyPrimary(pixel)) {
                    pixel = Palette.primaryShade(pixel, 0xFF193B50);
                }
                else if (Palette.isCompanySecondary(pixel)) {
                    pixel = Palette.secondaryShade(pixel, 0xFFDDDDDD);
                }

                byte a = (byte) ((pixel >> 24) & 0xFF);
                byte r = (byte) ((pixel >> 16) & 0xFF);
                byte g = (byte) ((pixel >> 8) & 0xFF);
                byte b = (byte) (pixel & 0xFF);

                buffer.put(r);
                buffer.put(g);
                buffer.put(b);
                buffer.put(a);
            }
        }

        buffer.flip();

        // store it under a textureId
        int textureId = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

        return new OpenGlSprite(width, height, xOffset, yOffset, textureId);
    }


    public void draw(ScreenCoord screenCoord) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

        float left = screenCoord.getX() + getXOffset();
        float top = screenCoord.getY() + getYOffset();

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0); GL11.glVertex2f(left, top);
        GL11.glTexCoord2f(1, 0); GL11.glVertex2f(left + getWidth(), top);
        GL11.glTexCoord2f(1, 1); GL11.glVertex2f(left + getWidth(), top + getHeight());
        GL11.glTexCoord2f(0, 1); GL11.glVertex2f(left, top + getHeight());
        GL11.glEnd();
    }
}
