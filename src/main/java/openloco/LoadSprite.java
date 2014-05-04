package openloco;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import openloco.graphics.Sprite;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;

public class LoadSprite {

    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;

    private Sprite sprite;

    private void initLwjglDisplay() {
        try {
            Display.setDisplayMode(new DisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT));
            Display.create();
            Display.setVSyncEnabled(true);
        } catch (LWJGLException e) {
            throw new RuntimeException(e);
        }
    }

    private void resetOpenGl() {
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DITHER);
        GL11.glDisable(GL11.GL_FOG);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_1D);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_FLAT);
    }

    private void initOpenGL() {
        resetOpenGl();

        GL11.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);

        GL11.glViewport(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, SCREEN_WIDTH, SCREEN_HEIGHT, 0, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }

    private void initTexture() throws IOException {
        BufferedImage image = ImageIO.read(new File("/Users/tim/Desktop/loco_dat/extracted/A4/028.png"));

        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        this.sprite = Sprite.createFromPixels(pixels, image.getWidth(), image.getHeight(), -30, -29);
    }

    /**
     * Draw a quad with the texture on it.
     */
    private void render() {
        // clear the screen
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        GL11.glLineWidth(1f);
        GL11.glColor4f(0.8f, 0.8f, 0.8f, 1.0f);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2f(SCREEN_WIDTH / 2.0f, 0f);
        GL11.glVertex2f(SCREEN_WIDTH/2.0f, SCREEN_HEIGHT);
        GL11.glVertex2f(0, SCREEN_HEIGHT/2.0f);
        GL11.glVertex2f(SCREEN_WIDTH, SCREEN_HEIGHT/2.0f);
        GL11.glEnd();

        drawSprites();
    }

    private void drawSprites() {
        // configure the texture engine
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_REPLACE);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);

        drawSprite(sprite, SCREEN_WIDTH / 2.0f, SCREEN_HEIGHT / 2.0f);

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    private void drawSprite(Sprite sprite, float x, float y) {
        // bind the texture
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, sprite.getTextureId());

        // draw a quad with our texture on it
        float left = x + sprite.getXOffset();
        float top = y + sprite.getYOffset();

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0); GL11.glVertex2f(left, top);
        GL11.glTexCoord2f(1, 0); GL11.glVertex2f(left + sprite.getWidth(), top);
        GL11.glTexCoord2f(1, 1); GL11.glVertex2f(left + sprite.getWidth(), top + sprite.getHeight());
        GL11.glTexCoord2f(0, 1); GL11.glVertex2f(left, top + sprite.getHeight());
        GL11.glEnd();
    }

    private void run() throws IOException {
        initLwjglDisplay();
        initOpenGL();
        initTexture();

        while (true) {
            render();

            Display.update();
            Display.sync(20);

            if (Display.isCloseRequested()) {
                Display.destroy();
                break;
            }
        }
    }

    public static void main(String[] argv) throws IOException {
        new LoadSprite().run();
    }
}