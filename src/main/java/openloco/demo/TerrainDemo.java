package openloco.demo;

import openloco.Assets;
import openloco.graphics.IsoUtil;
import openloco.graphics.SpriteInstance;
import openloco.terrain.Terrain;
import openloco.terrain.TerrainRenderer;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TerrainDemo extends BaseDemo {

    private static final Logger logger = LoggerFactory.getLogger(TerrainDemo.class);

    private final Assets assets;

    private TerrainRenderer renderer;
    private Terrain terrain;

    private int width = 9;
    private int height = 9;
    private int cellWidth = 32;
    private List<SpriteInstance> spriteInstances;

    private float clickX = 0;
    private float clickY = 0;

    public TerrainDemo(Assets assets) {
        this.assets = assets;
    }

    @Override
    protected void init() {
        renderer = new TerrainRenderer(assets);
        terrain = new Terrain(width, height);

        terrain.setTileType(width/2, height/2, 5);
        terrain.setTileType(width/2, height/2+1, 5);
        terrain.setTileType(width/2+1, height/2, 5);
        terrain.setTileType(width/2+2, height/2, 12);
        terrain.setTileType(width/2+3, height/2, 8);
        terrain.setTileHeights(width/2, height/2, 2);

        spriteInstances = renderer.render(terrain);
    }

    @Override
    protected List<SpriteInstance> getSprites() {
        return spriteInstances;
    }

    @Override
    protected float getXOffset() {
        return -IsoUtil.isoX(cellWidth * width / 2, cellWidth * height / 2, 0);
    }

    @Override
    protected float getYOffset() {
        return -IsoUtil.isoY(cellWidth * width / 2, cellWidth * height / 2, 0);
    }

    @Override
    protected void update() {
        while (Mouse.next()) {
            if (Mouse.getEventButton() == 0) {
                float x = Mouse.getEventX() - (0.5f*getScreenWidth() + getXOffset());
                float y = (0.5f*getScreenHeight() - getYOffset()) - (Mouse.getEventY());
                logger.debug("Click: ({}, {})", x, y);

                int tileX = (int)Math.floor(IsoUtil.cartX(x, y)/cellWidth);
                int tileY = (int)Math.floor(IsoUtil.cartY(x, y)/cellWidth);
                logger.debug("Tile pos: ({}, {})", tileX, tileY);

                clickX = cellWidth*tileX;
                clickY = cellWidth*tileY;


                logger.debug("Cart pos: ({}, {})", clickX, clickY);
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
        GL11.glVertex2f(IsoUtil.isoX(clickX+cellWidth, clickY, 0), IsoUtil.isoY(clickX+cellWidth, clickY, 0));
        GL11.glVertex2f(IsoUtil.isoX(clickX+cellWidth, clickY+cellWidth, 0), IsoUtil.isoY(clickX+cellWidth, clickY+cellWidth, 0));
        GL11.glVertex2f(IsoUtil.isoX(clickX, clickY+cellWidth, 0), IsoUtil.isoY(clickX, clickY+cellWidth, 0));
        GL11.glEnd();

        GL11.glPopMatrix();
    }

}
