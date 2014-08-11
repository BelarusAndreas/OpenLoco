package openloco.ui.font;

import org.lwjgl.opengl.GL11;

import java.util.Map;

public class BitmapFont {

    private final Map<Character, GlyphDetails> glyphTextureMap;
    private final int textureId;

    public BitmapFont(Map<Character, GlyphDetails> glyphTextureMap, int textureId) {
        this.glyphTextureMap = glyphTextureMap;
        this.textureId = textureId;
    }

    public static class GlyphDetails {

        private final float x1;
        private final float y1;
        private final float x2;
        private final float y2;
        private final float w;
        private final float h;

        public GlyphDetails(float x1, float y1, float x2, float y2, float w, float h) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.w = w;
            this.h = h;
        }

        public float getX1() {
            return x1;
        }

        public float getY1() {
            return y1;
        }

        public float getX2() {
            return x2;
        }

        public float getY2() {
            return y2;
        }

        public float getW() {
            return w;
        }

        public float getH() {
            return h;
        }
    }

    public void drawChar(char c) {
        if (glyphTextureMap.containsKey(c)) {
            GlyphDetails glyphDetails = glyphTextureMap.get(c);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(glyphDetails.getX1(), glyphDetails.getY1()); GL11.glVertex2f(0, 0);
            GL11.glTexCoord2f(glyphDetails.getX2(), glyphDetails.getY1()); GL11.glVertex2f(glyphDetails.getW(), 0);
            GL11.glTexCoord2f(glyphDetails.getX2(), glyphDetails.getY2()); GL11.glVertex2f(glyphDetails.getW(), glyphDetails.getH());
            GL11.glTexCoord2f(glyphDetails.getX1(), glyphDetails.getY2()); GL11.glVertex2f(0, glyphDetails.getH());
            GL11.glEnd();
            GL11.glTranslatef(glyphDetails.getW(), 0.0f, 0.0f);
        }
        else {
            //character not in the map

        }
    }

    public void setAsCurrentFont() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
    }
}
