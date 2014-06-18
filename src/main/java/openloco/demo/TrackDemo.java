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

        /*
        network.add(new TrackNode(18, 18, 0, Track.TrackPiece.SMALLCURVE, 0));
        network.add(new TrackNode(20, 17, 0, Track.TrackPiece.SMALLCURVE, 1));
        network.add(new TrackNode(21, 19, 0, Track.TrackPiece.SMALLCURVE, 2));
        network.add(new TrackNode(19, 20, 0, Track.TrackPiece.SMALLCURVE, 3));

        network.add(new TrackNode(18, 18, 0, Track.TrackPiece.MEDIUMCURVE, 0));
        network.add(new TrackNode(21, 16, 0, Track.TrackPiece.MEDIUMCURVE, 1));
        network.add(new TrackNode(23, 19, 0, Track.TrackPiece.MEDIUMCURVE, 2));
        network.add(new TrackNode(20, 21, 0, Track.TrackPiece.MEDIUMCURVE, 3));
        */

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
