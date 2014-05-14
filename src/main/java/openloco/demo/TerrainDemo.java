package openloco.demo;

import openloco.Assets;
import openloco.datfiles.DatFileLoader;
import openloco.graphics.IsoUtil;
import openloco.graphics.SpriteInstance;
import openloco.terrain.Terrain;
import openloco.terrain.TerrainRenderer;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.List;

public class TerrainDemo extends BaseDemo {

    private final Assets assets;

    private TerrainRenderer renderer;
    private Terrain terrain;

    private int width = 9;
    private int height = 9;
    private int cellWidth = 32;
    private List<SpriteInstance> spriteInstances;

    public TerrainDemo(Assets assets) {
        this.assets = assets;
    }

    @Override
    protected void init() {
        renderer = new TerrainRenderer(assets);
        terrain = new Terrain();
        spriteInstances = renderer.render(terrain);
    }

    @Override
    protected void render() {
        drawTiles();
    }

    private void drawTiles() {
        GL11.glPushMatrix();
        GL11.glTranslatef(getScreenWidth()/2.0f, getScreenHeight()/2.0f, 0f);

        GL11.glTranslatef(-IsoUtil.isoX(cellWidth * width / 2, cellWidth * height / 2, 0), -IsoUtil.isoY(cellWidth * width / 2, cellWidth * height / 2, 0), 0f);

        GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_REPLACE);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);

        for (SpriteInstance sprite: spriteInstances) {
            sprite.getSprite().draw(sprite.getScreenX(), sprite.getScreenY());
        }

        GL11.glPopMatrix();
    }

    public static void main(String[] args) throws IOException {
        final String DATA_DIR = args[0];
        Assets assets = new Assets();
        new DatFileLoader(assets, DATA_DIR).loadFiles();
        new TerrainDemo(assets).run();
    }

}
