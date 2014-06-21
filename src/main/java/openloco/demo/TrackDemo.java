package openloco.demo;

import openloco.Assets;
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

        TrackLayer trackLayer;

        TrackNetwork network = new TrackNetwork();

        trackLayer = new TrackLayer(18, 35, 0, Orientation.N);
        for (int i=0; i<36; i++) {
            trackLayer.addStraight();
        }
        network.addAll(trackLayer.getNodes());

        trackLayer = new TrackLayer(18, 24, 0, Orientation.N);
        trackLayer.addSBend(CurveDirection.LEFT);
        trackLayer.addSBend(CurveDirection.RIGHT);
        network.addAll(trackLayer.getNodes());

        trackLayer = new TrackLayer(18, 17, 0, Orientation.S);
        trackLayer.addSBend(CurveDirection.LEFT);
        trackLayer.addSBend(CurveDirection.RIGHT);
        network.addAll(trackLayer.getNodes());

        trackLayer = new TrackLayer(19, 18, 0, Orientation.E);
        trackLayer.addSBend(CurveDirection.LEFT);
        trackLayer.addSBend(CurveDirection.RIGHT);
        network.addAll(trackLayer.getNodes());

        trackLayer = new TrackLayer(26, 18, 0, Orientation.W);
        trackLayer.addSBend(CurveDirection.LEFT);
        trackLayer.addSBend(CurveDirection.RIGHT);
        network.addAll(trackLayer.getNodes());

        trackLayer = new TrackLayer(18, 12, 0, Orientation.N);
        trackLayer.addSmallCurve(CurveDirection.RIGHT);
        trackLayer.addSmallCurve(CurveDirection.RIGHT);
        trackLayer.addSmallCurve(CurveDirection.RIGHT);
        trackLayer.addSmallCurve(CurveDirection.RIGHT);

        trackLayer.addSmallCurve(CurveDirection.LEFT);
        trackLayer.addSmallCurve(CurveDirection.LEFT);
        trackLayer.addSmallCurve(CurveDirection.LEFT);
        trackLayer.addSmallCurve(CurveDirection.LEFT);

        trackLayer.addMediumCurve(CurveDirection.RIGHT);
        trackLayer.addMediumCurve(CurveDirection.RIGHT);
        trackLayer.addMediumCurve(CurveDirection.RIGHT);
        trackLayer.addMediumCurve(CurveDirection.RIGHT);

        trackLayer.addMediumCurve(CurveDirection.LEFT);
        trackLayer.addMediumCurve(CurveDirection.LEFT);
        trackLayer.addMediumCurve(CurveDirection.LEFT);
        trackLayer.addMediumCurve(CurveDirection.LEFT);
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
