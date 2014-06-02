package openloco.demo;

import openloco.Assets;
import openloco.entities.Track;
import openloco.graphics.IsoUtil;
import openloco.graphics.SpriteInstance;
import openloco.rail.TrackNetwork;
import openloco.rail.TrackNode;
import openloco.rail.TrackRenderer;
import openloco.terrain.Terrain;
import openloco.terrain.TerrainRenderer;

import java.util.List;

public class TrackDemo extends BaseDemo {

    private final Assets assets;

    private int width = 36;
    private int height = 36;
    private int cellWidth = 32;
    private List<SpriteInstance> spriteInstances;

    public TrackDemo(Assets assets) {
        this.assets = assets;
    }

    @Override
    protected void init() {
        TerrainRenderer terrainRenderer = new TerrainRenderer(assets);
        Terrain terrain = new Terrain(width, height);
        spriteInstances = terrainRenderer.render(terrain);

        TrackNetwork network = new TrackNetwork();
        for (int i=0; i<36; i++) {
            int trackX = 18;
            int trackY = i;
            network.add(new TrackNode(trackX*cellWidth, trackY*cellWidth, 0));
        }

        TrackRenderer trackRenderer = new TrackRenderer(assets);
        spriteInstances.addAll(trackRenderer.render(network));
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
}
