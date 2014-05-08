package openloco;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import openloco.datfiles.DatFileLoader;
import openloco.datfiles.Sprites;
import openloco.graphics.OpenGlSprite;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class LoadSprite {

    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;

    private List<OpenGlSprite> sprites = new ArrayList<>();

    private Assets assets;
    private int spriteIndex = 0;
    private Vehicle vehicle;
    private boolean rotating = true;

    public LoadSprite(Assets assets) {
        this.assets = assets;
    }

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
        vehicle = assets.getVehicle("A4      ");
        for (Sprites.RawSprite rawSprite: vehicle.getSprites().getList()) {
            Sprites.SpriteHeader header = rawSprite.getHeader();
            this.sprites.add(OpenGlSprite.createFromPixels(rawSprite.getPixels(), header.getWidth(), header.getHeight(),
                    header.getXOffset(), header.getYOffset()));
        }
    }

    private void render() {
        // clear the screen
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        GL11.glLineWidth(1f);
        GL11.glColor4f(0.7f, 0.7f, 0.7f, 1.0f);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2f(SCREEN_WIDTH / 2.0f, 0f);
        GL11.glVertex2f(SCREEN_WIDTH/2.0f, SCREEN_HEIGHT);
        GL11.glVertex2f(0, SCREEN_HEIGHT/2.0f);
        GL11.glVertex2f(SCREEN_WIDTH, SCREEN_HEIGHT/2.0f);
        GL11.glEnd();

        drawSprites();

        GL11.glFlush();
    }

    private void drawSprites() {
        GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_REPLACE);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);

        OpenGlSprite sprite = sprites.get(spriteIndex);
        drawSprite(sprite, SCREEN_WIDTH / 2.0f, SCREEN_HEIGHT / 2.0f);

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    private void drawSprite(OpenGlSprite sprite, float x, float y) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, sprite.getTextureId());

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

            while (Keyboard.next()) {
                if (Keyboard.getEventKey() == Keyboard.KEY_SPACE && Keyboard.getEventKeyState()) {
                    rotating = !rotating;
                }
            }

            if (rotating) {
                spriteIndex = (spriteIndex + 1) % (vehicle.getVars().getVehSprites().get(0).getLevelSpriteCount());
            }

            Display.update();
            Display.sync(20);

            if (Display.isCloseRequested()) {
                Display.destroy();
                break;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        final String DATA_DIR = args[0];
        Assets assets = new Assets();
        new DatFileLoader(assets, DATA_DIR).loadFiles();
        new LoadSprite(assets).run();
    }
}