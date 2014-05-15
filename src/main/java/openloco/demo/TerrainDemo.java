package openloco.demo;

import openloco.Assets;
import openloco.datfiles.DatFileLoader;
import openloco.graphics.IsoUtil;
import openloco.graphics.SpriteInstance;
import openloco.terrain.Terrain;
import openloco.terrain.TerrainRenderer;

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

    public static void main(String[] args) throws IOException {
        final String DATA_DIR = args[0];
        Assets assets = new Assets();
        new DatFileLoader(assets, DATA_DIR).loadFiles();
        new TerrainDemo(assets).run();
    }

}
