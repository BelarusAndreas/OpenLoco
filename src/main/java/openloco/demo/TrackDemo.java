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

        draw90Curve(18, 18, 0);
        draw90Curve(20, 17, 2);
        draw90Curve(20, 15, 1);
        draw90Curve(18, 14, 3);

        spriteInstances.addAll(trackRenderer.render(network));
    }

    private void draw90Curve(int x, int y, int rotation) {
        Track track = assets.getTrack("TRACKST ");

        TrackNode node = new TrackNode(x * cellWidth, y * cellWidth, 0);

        int ballastIndex = 24 + 4 * rotation;
        int sleeperIndex = 40 + 4 * rotation;
        int railIndex = 56 + 4 * rotation;

        final int[][] allOffsets = new int[][]{
            {0, 1, 2, 3}, {1, 3, 0, 2}, {3, 2, 1, 0}, {2, 0, 3, 1}
        };

        int[] offsets = allOffsets[rotation];

        int[] xDeltas = {0, cellWidth, 0, cellWidth};
        int[] yDeltas = {0, 0, -cellWidth, -cellWidth};

        for (int i = 0; i<4; i++) {
            OpenGlSprite ballastSprite = OpenGlSprite.createFromRawSprite(track.getSprites().get(ballastIndex + offsets[i]));
            OpenGlSprite sleeperSprite = OpenGlSprite.createFromRawSprite(track.getSprites().get(sleeperIndex + offsets[i]));
            OpenGlSprite railSprite = OpenGlSprite.createFromRawSprite(track.getSprites().get(railIndex + offsets[i]));
            int screenX = Math.round(IsoUtil.isoX(node.getX() + xDeltas[i], node.getY() + yDeltas[i], node.getZ()));
            int screenY = Math.round(IsoUtil.isoY(node.getX() + xDeltas[i], node.getY() + yDeltas[i], node.getZ()));
            spriteInstances.add(new SpriteInstance(ballastSprite, screenX, screenY, SpriteLayer.BALLAST));
            spriteInstances.add(new SpriteInstance(sleeperSprite, screenX, screenY, SpriteLayer.SLEEPERS));
            spriteInstances.add(new SpriteInstance(railSprite, screenX, screenY, SpriteLayer.RAILS));
        }
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
