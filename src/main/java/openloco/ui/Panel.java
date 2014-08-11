package openloco.ui;

import org.lwjgl.opengl.GL11;

import java.awt.*;

public class Panel implements UiComponent {

    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final boolean isInset;

    public Panel(int x, int y, int width, int height, boolean isInset) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isInset = isInset;
    }

    @Override
    public void render() {
        Color background = new Color(0x83/255.0f, 0x97/255.0f, 0x97/255.0f);
        Color highlight = new Color(0xB1/255.0f, 0xBE/255.0f, 0xBE/255.0f);
        Color shadow = new Color(0x69/255.0f, 0x77/255.0f, 0x77/255.0f);

        Color top = isInset? shadow : highlight;
        Color bottom = isInset? highlight : shadow;

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0);

        setColor(background);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(0, 0);
        GL11.glVertex2f(width, 0);
        GL11.glVertex2f(width, height);
        GL11.glVertex2f(0, height);
        GL11.glEnd();

        setColor(bottom);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        GL11.glVertex2f(width, 0);
        GL11.glVertex2f(width, height);
        GL11.glVertex2f(0, height);
        GL11.glEnd();

        setColor(top);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        GL11.glVertex2f(0, height-1);
        GL11.glVertex2f(0, 0);
        GL11.glVertex2f(width-1, 0);
        GL11.glEnd();

        GL11.glPopMatrix();
    }

    private void setColor(Color color) {
        GL11.glColor3f(color.getRed()/255.0f, color.getGreen()/255.0f, color.getBlue()/255.0f);
    }
}
