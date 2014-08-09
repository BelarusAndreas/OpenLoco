package openloco.demo;

import openloco.Assets;
import openloco.graphics.IsoUtil;
import openloco.graphics.SpriteInstance;
import openloco.graphics.Tile;
import openloco.rail.*;
import openloco.terrain.Terrain;
import openloco.terrain.TerrainRenderer;

import java.util.List;

public class TrackDemo extends BaseDemo {

    private final Assets assets;

    private static final int WIDTH = 36;
    private static final int HEIGHT = 36;

    private List<SpriteInstance> spriteInstances;

    public TrackDemo(Assets assets) {
        super(WIDTH, HEIGHT);
        this.assets = assets;
    }

    @Override
    protected void init() {
        TerrainRenderer terrainRenderer = new TerrainRenderer(assets);
        Terrain terrain = new Terrain(WIDTH, HEIGHT);

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

}
