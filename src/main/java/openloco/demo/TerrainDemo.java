package openloco.demo;

import openloco.assets.Assets;
import openloco.graphics.IsoUtil;
import openloco.graphics.SpriteInstance;
import openloco.graphics.Tile;
import openloco.terrain.Terrain;
import openloco.terrain.TerrainRenderer;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TerrainDemo extends BaseDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerrainDemo.class);

    private final Assets assets;

    private TerrainRenderer renderer;
    private Terrain terrain;

    private static final int WIDTH = 9;
    private static final int HEIGHT = 9;

    private List<SpriteInstance> spriteInstances;

    private float clickX = 0;
    private float clickY = 0;

    public TerrainDemo(Assets assets) {
        super(WIDTH, HEIGHT);
        this.assets = assets;
    }

    @Override
    protected void init() {
        renderer = new TerrainRenderer(assets);
        terrain = new Terrain(WIDTH, HEIGHT);

        terrain.setTileType(WIDTH/2, HEIGHT/2, 5);
        terrain.setTileType(WIDTH/2, HEIGHT/2+1, 5);
        terrain.setTileType(WIDTH/2+1, HEIGHT/2, 5);
        terrain.setTileType(WIDTH/2+2, HEIGHT/2, 12);
        terrain.setTileType(WIDTH/2+3, HEIGHT/2, 8);
        terrain.setTileHeight(WIDTH / 2, HEIGHT / 2, 2);

        spriteInstances = renderer.render(terrain);
    }

    @Override
    protected List<SpriteInstance> getSprites() {
        return spriteInstances;
    }

    @Override
    protected void update() {
        while (Mouse.next()) {
            if (Mouse.getEventButton() == 0) {
                float x = Mouse.getEventX() - (0.5f*getScreenWidth() + getXOffset());
                float y = (0.5f*getScreenHeight() - getYOffset()) - (Mouse.getEventY());
                LOGGER.debug("Click: ({}, {})", x, y);

                int tileX = (int)Math.floor(IsoUtil.cartX(x, y)/ Tile.WIDTH);
                int tileY = (int)Math.floor(IsoUtil.cartY(x, y)/ Tile.WIDTH);
                LOGGER.debug("Tile pos: ({}, {})", tileX, tileY);

                clickX = Tile.WIDTH *tileX;
                clickY = Tile.WIDTH *tileY;

                LOGGER.debug("Cart pos: ({}, {})", clickX, clickY);
            }
        }
    }

    @Override
    protected void render() {
        super.render();
        //drawHighlightedTile();
    }

    private void drawHighlightedTile() {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);

        GL11.glLineWidth(1f);
        GL11.glColor4f(1f, 0f, 0f, 0.7f);

        GL11.glPushMatrix();

        GL11.glTranslatef(getScreenWidth()/2.0f, getScreenHeight()/2.0f, 0f);
        GL11.glTranslatef(getXOffset(), getYOffset(), 0.0f);

        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex2f(IsoUtil.isoX(clickX, clickY, 0), IsoUtil.isoY(clickX, clickY, 0));
        GL11.glVertex2f(IsoUtil.isoX(clickX+ Tile.WIDTH, clickY, 0), IsoUtil.isoY(clickX+ Tile.WIDTH, clickY, 0));
        GL11.glVertex2f(IsoUtil.isoX(clickX+ Tile.WIDTH, clickY+ Tile.WIDTH, 0), IsoUtil.isoY(clickX+ Tile.WIDTH, clickY+ Tile.WIDTH, 0));
        GL11.glVertex2f(IsoUtil.isoX(clickX, clickY+ Tile.WIDTH, 0), IsoUtil.isoY(clickX, clickY+ Tile.WIDTH, 0));
        GL11.glEnd();

        GL11.glPopMatrix();
    }

}
