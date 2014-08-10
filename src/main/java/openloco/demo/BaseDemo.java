package openloco.demo;

import openloco.graphics.IsoUtil;
import openloco.graphics.SpriteInstance;
import openloco.graphics.Tile;
import openloco.ui.UiComponent;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class BaseDemo {

    private int frameCount = 0;
    private long renderTime = 0;

    private String title;

    private int focusX;
    private int focusY;
    private int mouseDownFocusX = -1;
    private int mouseDownFocusY = -1;
    private int mouseDownX = -1;
    private int mouseDownY = -1;

    private List<UiComponent> uiComponents = new LinkedList<>();

    public BaseDemo(int xTiles, int yTiles) {
        focusX = Tile.WIDTH * xTiles / 2;
        focusY = Tile.WIDTH * yTiles / 2;
    }

    private void initLwjglDisplay() {
        try {
            title = "OpenLoco demo :: " + getClass().getSimpleName();
            Display.setTitle(title);
            DisplayMode displayMode = new DisplayMode(800, 600);
            Display.setDisplayMode(displayMode);
            Display.setResizable(true);
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

        GL11.glViewport(0, 0, getScreenWidth(), getScreenHeight());

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, getScreenWidth(), getScreenHeight(), 0, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }

    public int getScreenWidth() {
        return Display.getWidth();
    }

    public int getScreenHeight() {
        return Display.getHeight();
    }

    protected void initDisplay() {
        initLwjglDisplay();
        initOpenGL();
    }

    public void run() {
        initDisplay();
        init();

        while (true) {
            updateInternal();
            update();
            renderInternal();

            Display.update();
            Display.sync(50);

            if (Display.isCloseRequested()) {
                Display.destroy();
                break;
            }
        }
    }

    private void updateInternal() {
        if (Display.wasResized()) {
            initOpenGL();
        }

        if (Mouse.isButtonDown(1)) {
            if (!isTerrainDragging()) {
                beginTerrainDragging();
            }
            updateTerrainDragging();
        }
        else if (isTerrainDragging()) {
            endTerrainDragging();
        }
    }

    private void renderInternal() {
        // clear the screen
        long start = System.nanoTime();
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        render();
        GL11.glFlush();
        renderTime += System.nanoTime()-start;

        if (frameCount == 100) {
            Display.setTitle(title + " [" + ((double)renderTime/(frameCount*1000000.0)) + "ms/frame]");
            renderTime = 0;
            frameCount = 0;
        }
        else {
            frameCount++;
        }
    }

    protected abstract void init();

    protected void update() { }

    protected void render() {
        drawSprites();
        drawUi();
    }

    private void drawSprites() {
        GL11.glPushMatrix();
        GL11.glTranslatef(getScreenWidth()/2.0f, getScreenHeight()/2.0f, 0f);

        GL11.glTranslatef(getXOffset(), getYOffset(), 0f);

        GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_REPLACE);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);

        List<SpriteInstance> sprites = getSprites();

        Collections.sort(sprites, SpriteInstance.SPRITE_DEPTH_COMPARATOR);

        for (SpriteInstance sprite: sprites) {
            sprite.getSprite().draw(sprite.getScreenCoord());
        }

        GL11.glPopMatrix();
    }

    private void drawUi() {
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        for (UiComponent uiComponent: uiComponents) {
            uiComponent.render();
        }
    }

    public void addUiComponent(UiComponent uiComponent) {
        uiComponents.add(uiComponent);
    }

    public void clearUiComponents() {
        uiComponents.clear();
    }

    private boolean isTerrainDragging() {
        return mouseDownX != -1;
    }

    private void beginTerrainDragging() {
        mouseDownX = Mouse.getX();
        mouseDownY = Mouse.getY();
        mouseDownFocusX = focusX;
        mouseDownFocusY = focusY;
    }

    private void updateTerrainDragging() {
        int dx = (mouseDownX - Mouse.getX());
        int dy = -(mouseDownY - Mouse.getY());

        float cdx = IsoUtil.cartX(dx, dy);
        float cdy = IsoUtil.cartY(dx, dy);

        focusX = mouseDownFocusX + (int)cdx;
        focusY = mouseDownFocusY + (int)cdy;
    }

    private void endTerrainDragging() {
        mouseDownX = -1;
        mouseDownY = -1;
    }

    protected List<SpriteInstance> getSprites() {
        return Collections.emptyList();
    }

    protected float getXOffset() {
        return -IsoUtil.isoX(focusX, focusY, 0);
    }

    protected float getYOffset() {
        return -IsoUtil.isoY(focusX, focusY, 0);
    }
}
