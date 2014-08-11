package openloco.ui.font;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.image.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class BitmapFontFactory {

    public static final int TEXTURE_WIDTH = 256;
    public static final int TEXTURE_HEIGHT = 256;

    public BitmapFont getBitmapFont(String fontName, int fontSize) {
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@£$%^&*()[[]{}.,;:'\"\\/?<>`~+-=_ ".toCharArray();
        Font font = new Font(fontName, Font.PLAIN, Math.round(fontSize*Display.getPixelScaleFactor()));

        BufferedImage texImage = new BufferedImage(TEXTURE_WIDTH, TEXTURE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Map<Character, BitmapFont.GlyphDetails> glyphTextureMap = renderCharactersToImage(chars, font, texImage);

        ByteBuffer imageBuffer = convertToByteBuffer(texImage);
        int textureId = initTexture(TEXTURE_WIDTH, TEXTURE_HEIGHT, imageBuffer);

        return new BitmapFont(glyphTextureMap, textureId);
    }

    private Map<Character, BitmapFont.GlyphDetails> renderCharactersToImage(char[] chars, Font font, BufferedImage texImage) {
        Map<Character, BitmapFont.GlyphDetails> glyphTextureMap = new HashMap<>();
        int x = 0;
        int y = 0;
        float scaleFactor = Display.getPixelScaleFactor();
        Graphics2D graphics2d = texImage.createGraphics();
        graphics2d.setFont(font);
        graphics2d.setColor(Color.BLACK);
        graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        FontMetrics fontMetrics = graphics2d.getFontMetrics();

        for (char c: chars) {
            int w = fontMetrics.charWidth(c);
            if (x + w > TEXTURE_WIDTH) {
                x = 0;
                y += fontMetrics.getMaxAscent() + fontMetrics.getMaxDescent();
            }
            graphics2d.drawString(new String(new char[] { c }), x, y+fontMetrics.getAscent());
            glyphTextureMap.put(c, new BitmapFont.GlyphDetails((float)x/(float) TEXTURE_WIDTH, (float)y/(float) TEXTURE_HEIGHT, (float)(x+w)/(float) TEXTURE_WIDTH, (float)(y+fontMetrics.getHeight())/(float) TEXTURE_HEIGHT, (float)w / scaleFactor, (float)fontMetrics.getHeight()/ scaleFactor));
            x += fontMetrics.getMaxAdvance();
        }
        return glyphTextureMap;
    }

    private int initTexture(int textureWidth, int textureHeight, ByteBuffer imageBuffer) {
        int textureId = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, textureWidth, textureHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageBuffer);
        return textureId;
    }

    private ByteBuffer convertToByteBuffer(BufferedImage texImage) {
        int textureWidth = texImage.getWidth();
        int textureHeight = texImage.getHeight();

        int[] pixels = new int[textureWidth * textureHeight];
        texImage.getRGB(0, 0, textureWidth, textureHeight, pixels, 0, textureWidth);

        ByteBuffer imageBuffer = BufferUtils.createByteBuffer(textureWidth * textureHeight * 4);
        for (int y = 0; y < texImage.getHeight(); y++){
            for(int x = 0; x < texImage.getWidth(); x++){
                int pixel = pixels[y * texImage.getWidth() + x];
                imageBuffer.put((byte) ((pixel >> 16) & 0xFF));
                imageBuffer.put((byte) ((pixel >> 8) & 0xFF));
                imageBuffer.put((byte) (pixel & 0xFF));
                imageBuffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }
        imageBuffer.flip();

        return imageBuffer;
    }

}
