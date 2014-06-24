package openloco.demo;

import openloco.Assets;
import openloco.entities.Track;
import openloco.graphics.IsoUtil;
import openloco.graphics.SpriteInstance;
import openloco.rail.*;
import openloco.terrain.Terrain;
import openloco.terrain.TerrainRenderer;

import java.util.List;

public class TrackDemo extends BaseDemo {

    private final Assets assets;

    private int width = 36;
    private int height = 36;

    public static final int cellWidth = 32;

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

        TrackLayer trackLayer = new TrackLayer(18, 19, 0, Orientation.N);
        trackLayer.addNormalSlopeUp();
        trackLayer.addMediumCurve(CurveDirection.LEFT);
        trackLayer.addNormalSlopeUp();
        trackLayer.addMediumCurve(CurveDirection.LEFT);
        trackLayer.addNormalSlopeUp();
        trackLayer.addMediumCurve(CurveDirection.LEFT);
        trackLayer.addNormalSlopeUp();
        network.addAll(trackLayer.getNodes());

        trackLayer = new TrackLayer(18, 20, 0, Orientation.S);
        trackLayer.addMediumCurve(CurveDirection.RIGHT);
        trackLayer.addMediumCurve(CurveDirection.RIGHT);
        trackLayer.addStraight();
        trackLayer.addStraight();
        trackLayer.addStraight();
        trackLayer.addStraight();
        trackLayer.addStraight();
        trackLayer.addStraight();
        trackLayer.addStraight();
        trackLayer.addStraight();
        trackLayer.addSmallCurve(CurveDirection.RIGHT);
        trackLayer.addSmallCurve(CurveDirection.RIGHT);
        trackLayer.addStraight();
        trackLayer.addStraight();
        trackLayer.addStraight();
        trackLayer.addStraight();
        trackLayer.addStraight();
        trackLayer.addStraight();
        trackLayer.addStraight();
        trackLayer.addStraight();
        trackLayer.addStraight();
        trackLayer.addStraight();
        trackLayer.addStraight();
        trackLayer.addStraight();
        trackLayer.addStraight();
        trackLayer.addStraight();
        trackLayer.addStraight();
        trackLayer.addStraight();
        network.addAll(trackLayer.getNodes());

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
