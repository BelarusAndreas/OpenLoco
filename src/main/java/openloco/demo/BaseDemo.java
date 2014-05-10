package openloco.demo;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public abstract class BaseDemo {

    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;

    private void initLwjglDisplay() {
        try {
            Display.setTitle("OpenLoco demo :: " + getClass().getSimpleName());
            Display.setDisplayMode(new DisplayMode(getScreenWidth(), getScreenHeight()));
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

    public int getScreenWidth() {
        return SCREEN_WIDTH;
    }

    public int getScreenHeight() {
        return SCREEN_HEIGHT;
    }

    protected int isoX(int cartX, int cartY, int cartZ) {
        return cartX - cartY;
    }

    protected int isoY(int cartX, int cartY, int cartZ) {
        return (cartX + cartY) / 2 - cartZ;
    }

    protected void initDisplay() {
        initLwjglDisplay();
        initOpenGL();
    }

    public void run() throws IOException {
        initDisplay();
        init();

        while (true) {
            update();
            renderInternal();

            Display.update();
            Display.sync(20);

            if (Display.isCloseRequested()) {
                Display.destroy();
                break;
            }
        }
    }

    private void renderInternal() {
        // clear the screen
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        render();
        GL11.glFlush();
    }

    protected abstract void init();

    protected void update() { }

    protected abstract void render();
}
