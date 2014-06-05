package openloco.rail;

import openloco.Assets;
import static openloco.demo.TrackDemo.cellWidth;
import openloco.entities.Track;
import openloco.graphics.*;

import java.util.ArrayList;
import java.util.List;

public class TrackRenderer implements Renderer<TrackNetwork>{

    private final Assets assets;

    public TrackRenderer(Assets assets) {
        this.assets = assets;
    }

    @Override
    public List<SpriteInstance> render(TrackNetwork network) {
        Track track = assets.getTrack("TRACKST ");

        List<SpriteInstance> sprites = new ArrayList<>();

        for (TrackNode node: network.getAllNodes()) {
            switch(node.getPieceType()) {
                case STRAIGHT:
                    drawStraight(track, sprites, node);
                    break;

                case MEDIUMCURVE:
                    drawMediumCurve(track, sprites, node);
                    break;
            }
        }

        return sprites;
    }

    private void drawStraight(Track track, List<SpriteInstance> sprites, TrackNode node) {
        int spritesPerTile = 1;
        int maxRotation = 2;
        int rotation = node.getRotation() % maxRotation;
        int ballastIndex = 18 + spritesPerTile * rotation;
        int sleeperIndex = 20 + spritesPerTile * rotation;
        int railIndex = 22 + spritesPerTile * rotation;

        OpenGlSprite ballastSprite = OpenGlSprite.createFromRawSprite(track.getSprites().get(ballastIndex));
        OpenGlSprite sleeperSprite = OpenGlSprite.createFromRawSprite(track.getSprites().get(sleeperIndex));
        OpenGlSprite railSprite = OpenGlSprite.createFromRawSprite(track.getSprites().get(railIndex));

        int screenX = Math.round(IsoUtil.isoX(node.getX(), node.getY(), node.getZ()));
        int screenY = Math.round(IsoUtil.isoY(node.getX(), node.getY(), node.getZ()));

        sprites.add(new SpriteInstance(ballastSprite, screenX, screenY, SpriteLayer.BALLAST));
        sprites.add(new SpriteInstance(sleeperSprite, screenX, screenY, SpriteLayer.SLEEPERS));
        sprites.add(new SpriteInstance(railSprite, screenX, screenY, SpriteLayer.RAILS));
    }

    private void drawMediumCurve(Track track, List<SpriteInstance> spriteInstances, TrackNode node) {
        int spritesPerTile = 4;
        int maxRotation = 4;
        int rotation = node.getRotation() % maxRotation;
        int ballastIndex = 24 + spritesPerTile * rotation;
        int sleeperIndex = 40 + spritesPerTile * rotation;
        int railIndex = 56 + spritesPerTile * rotation;

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
}
