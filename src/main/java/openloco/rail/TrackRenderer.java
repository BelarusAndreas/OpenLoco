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

                case SMALLCURVE:
                    drawSmallCurve(track, sprites, node);
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

    private void drawSmallCurve(Track track, List<SpriteInstance> spriteInstances, TrackNode node) {
        int[][] xDeltas = {
                {0, 1, 0, 1}, {0, 0, 1, 1}, {0,-1, 0,-1}, {0, 0,-1,-1}
        };
        int[][] yDeltas = {
                {0, 0,-1,-1}, {0, 1, 0, 1}, {0, 0, 1, 1}, {0,-1, 0,-1}
        };

        drawTrackPiece(track, spriteInstances, node, 4, 4, xDeltas, yDeltas, 24, 40, 56);
    }

    private void drawTrackPiece(Track track, List<SpriteInstance> spriteInstances, TrackNode node, int spritesPerTile, int maxRotation, int[][] xDeltas, int[][] yDeltas, int ballastStartIndex, int sleeperStartIndex, int railStartIndex) {
        int rotation = node.getRotation() % maxRotation;
        int ballastIndex = ballastStartIndex + spritesPerTile * rotation;
        int sleeperIndex = sleeperStartIndex + spritesPerTile * rotation;
        int railIndex = railStartIndex + spritesPerTile * rotation;

        for (int i = 0; i < spritesPerTile; i++) {
            OpenGlSprite ballastSprite = OpenGlSprite.createFromRawSprite(track.getSprites().get(ballastIndex + i));
            OpenGlSprite sleeperSprite = OpenGlSprite.createFromRawSprite(track.getSprites().get(sleeperIndex + i));
            OpenGlSprite railSprite = OpenGlSprite.createFromRawSprite(track.getSprites().get(railIndex + i));
            int screenX = Math.round(IsoUtil.isoX(node.getX() + cellWidth * xDeltas[rotation][i], node.getY() + cellWidth * yDeltas[rotation][i], node.getZ()));
            int screenY = Math.round(IsoUtil.isoY(node.getX() + cellWidth*xDeltas[rotation][i], node.getY() + cellWidth*yDeltas[rotation][i], node.getZ()));
            spriteInstances.add(new SpriteInstance(ballastSprite, screenX, screenY, SpriteLayer.BALLAST));
            spriteInstances.add(new SpriteInstance(sleeperSprite, screenX, screenY, SpriteLayer.SLEEPERS));
            spriteInstances.add(new SpriteInstance(railSprite, screenX, screenY, SpriteLayer.RAILS));
        }
    }
}
