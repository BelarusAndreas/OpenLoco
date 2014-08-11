package openloco.ui;

import openloco.ui.font.BitmapFont;
import openloco.ui.font.BitmapFontFactory;
import org.lwjgl.opengl.GL11;

public class Text implements UiComponent {

    private final int x;
    private final int y;
    private final String text;
    private final String fontName;
    private final int size;
    private final BitmapFont bitmapFont;

    public Text(int x, int y, String text, String fontName, int size) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.fontName = fontName;
        this.size = size;
        bitmapFont = new BitmapFontFactory().getBitmapFont(fontName, size);
    }

    @Override
    public void render() {
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0);

        GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_REPLACE);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);

        bitmapFont.setAsCurrentFont();

        for (char c: text.toCharArray()) {
            bitmapFont.drawChar(c);
        }

        GL11.glPopMatrix();
    }
}
