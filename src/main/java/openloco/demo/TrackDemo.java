package openloco.demo;

import openloco.Assets;
import openloco.entities.Track;
import openloco.graphics.IsoUtil;
import openloco.graphics.OpenGlSprite;
import openloco.graphics.SpriteInstance;
import openloco.graphics.SpriteLayer;
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
        for (int i=0; i<36; i++) {
            int trackX = 18;
            int trackY = i;
            network.add(new TrackNode(trackX*cellWidth, trackY*cellWidth, 0, Track.TrackPiece.STRAIGHT, 0));
        }

        network.add(new TrackNode(18*cellWidth, 18*cellWidth, 0, Track.TrackPiece.SMALLCURVE, 0));
        network.add(new TrackNode(20*cellWidth, 17*cellWidth, 0, Track.TrackPiece.SMALLCURVE, 1));
        network.add(new TrackNode(21*cellWidth, 19*cellWidth, 0, Track.TrackPiece.SMALLCURVE, 2));
        network.add(new TrackNode(19*cellWidth, 20*cellWidth, 0, Track.TrackPiece.SMALLCURVE, 3));

        network.add(new TrackNode(18*cellWidth, 18*cellWidth, 0, Track.TrackPiece.MEDIUMCURVE, 0));
        network.add(new TrackNode(21*cellWidth, 16*cellWidth, 0, Track.TrackPiece.MEDIUMCURVE, 1));
        network.add(new TrackNode(23*cellWidth, 19*cellWidth, 0, Track.TrackPiece.MEDIUMCURVE, 2));
        network.add(new TrackNode(20*cellWidth, 21*cellWidth, 0, Track.TrackPiece.MEDIUMCURVE, 3));

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
