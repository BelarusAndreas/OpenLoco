package openloco.rail;

import openloco.Assets;
import static openloco.demo.TrackDemo.cellWidth;
import openloco.entities.Track;
import openloco.graphics.*;

import java.util.ArrayList;
import java.util.List;

public class TrackRenderer implements Renderer<TrackNetwork>{

    private final Assets assets;
    private static final int TILE_HEIGHT = 16;

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

                case MEDIUMCURVE:
                    drawMediumCurve(track, sprites, node);
                    break;

                case WIDECURVE:
                    drawWideCurve(track, sprites, node);
                    break;

                case SBEND:
                    drawSBend(track, sprites, node);
                    break;

                case DIAGONAL:
                    drawDiagonal(track, sprites, node);
                    break;

                case NORMALSLOPE:
                    drawNormalSlope(track, sprites, node);
                    break;
            }
        }

        return sprites;
    }

    private void drawStraight(Track track, List<SpriteInstance> sprites, TrackNode node) {
        int[][][] deltas = {{{0, 0}}, {{0, 0}}};
        drawTrackPiece(track, sprites, node, 1, 2, deltas, 18);
    }

    private void drawSmallCurve(Track track, List<SpriteInstance> spriteInstances, TrackNode node) {
        int[][][] deltas = {
                { {0, 0}, {1, 0}, {0,-1}, {1,-1} },
                { {0, 0}, {0, 1}, {1, 0}, {1, 1} },
                { {0, 0}, {-1,0}, {0, 1}, {-1,1} },
                { {0, 0}, {0,-1}, {-1,0}, {-1,-1} }
        };

        drawTrackPiece(track, spriteInstances, node, 4, 4, deltas, 24);
    }

    private void drawMediumCurve(Track track, List<SpriteInstance> spriteInstances, TrackNode node) {
        int[][][] deltas = {
                { {0, 0}, {0, -1}, {1,-1}, {1,-2}, {2,-2} },
                { {0, 0}, {1, 0}, {1, 1}, {2, 1}, {2, 2} },
                { {0, 0}, {0, 1}, {-1,1}, {-1, 2}, {-2, 2} },
                { {0, 0}, {-1,0}, {-1,-1}, {-2,-1}, {-2,-2} }
        };

        drawTrackPiece(track, spriteInstances, node, 5, 4, deltas, 136);
    }

    private void drawWideCurve(Track track, List<SpriteInstance> spriteInstances, TrackNode node) {
        int[][][] deltas = {
                {{0, 0}, {0,-1}, {1, -1}, {0, -2}, {1, -2}},
                {{0, 0}, {1, 0}, {1, 1}, {2, 0}, {2, 1}},
                {{0, 0}, {0, 1}, {-1, 1}, {0, 2}, {-1, 2}},
                {{0, 0}, {-1, 0}, {-1, -1}, {-2, 0}, {-2, -1}},
                {{1, 2}, {1, 1}, {0, 1}, {1, 0}, {0, 0}},
                {{-2, 1}, {-1, 1}, {-1, 0}, {0, 1}, {0, 0}},
                {{-1, -2}, {-1, -1}, {0, -1}, {-1, 0}, {0, 0}},
                {{2, -1}, {1, -1}, {1, 0}, {0, -1}, {0, 0}}
        };

        drawTrackPiece(track, spriteInstances, node, 5, 8, deltas, 208);
    }

    private void drawSBend(Track track, List<SpriteInstance> spriteInstances, TrackNode node) {
        int[][][] deltas = {
                { {0, 0}, {0, -1}, {-1, -1}, {-1, -2} },
                { {0, 0}, {1, 0}, {1, -1}, {2, -1} },
                { {0, 0}, {0, -1}, {1, -1}, {1, -2} },
                { {0, 0}, {1, 0}, {1, 1}, {2, 1} }
        };

        drawTrackPiece(track, spriteInstances, node, 4, 4, deltas, 352);
    }

    private void drawDiagonal(Track track, List<SpriteInstance> spriteInstances, TrackNode node) {
        int[][][] deltas = {
                { {0, 0}, {0, -1}, {1, 0}, {1, -1} },
                { {0, 0}, {1, 0}, {0, 1}, {1, 1} }
        };

        drawTrackPiece(track, spriteInstances, node, 4, 2, deltas, 328);
    }

    private void drawNormalSlope(Track track, List<SpriteInstance> spriteInstances, TrackNode node) {
        int[][][] deltas = {
                { {0, 0}, {0, -1} },
                { {0, 0}, {1, 0} },
                { {0, 0}, {0, 1} },
                { {0, 0}, {-1, 0} }
        };

        int rotation = node.getRotation() % 4;
        int startIndex = 196;

        for (int i=0; i < 2; i++) {
            OpenGlSprite sprite = OpenGlSprite.createFromRawSprite(track.getSprites().get(startIndex + i + (2*rotation)));
            int screenX = Math.round(IsoUtil.isoX(cellWidth * (node.getX() + deltas[rotation][i][0]), cellWidth * (node.getY() + deltas[rotation][i][1]), TILE_HEIGHT*node.getZ()));
            int screenY = Math.round(IsoUtil.isoY(cellWidth * (node.getX() + deltas[rotation][i][0]), cellWidth * (node.getY() + deltas[rotation][i][1]), TILE_HEIGHT*node.getZ()));
            spriteInstances.add(new SpriteInstance(sprite, screenX, screenY, SpriteLayer.RAILS));
        }
    }

    private void drawTrackPiece(Track track, List<SpriteInstance> spriteInstances, TrackNode node, int spritesPerTile, int maxRotation, int[][][] deltas, int spriteStartIndex) {
        int rotation = node.getRotation() % maxRotation;
        int ballastIndex = spriteStartIndex + spritesPerTile * rotation;
        int sleeperIndex = spriteStartIndex + ((maxRotation + rotation) * spritesPerTile);
        int railIndex = spriteStartIndex + (2 * maxRotation + rotation) * spritesPerTile;

        for (int i = 0; i < spritesPerTile; i++) {
            OpenGlSprite ballastSprite = OpenGlSprite.createFromRawSprite(track.getSprites().get(ballastIndex + i));
            OpenGlSprite sleeperSprite = OpenGlSprite.createFromRawSprite(track.getSprites().get(sleeperIndex + i));
            OpenGlSprite railSprite = OpenGlSprite.createFromRawSprite(track.getSprites().get(railIndex + i));
            int screenX = Math.round(IsoUtil.isoX(cellWidth * (node.getX() + deltas[rotation][i][0]), cellWidth * (node.getY() + deltas[rotation][i][1]), TILE_HEIGHT*node.getZ()));
            int screenY = Math.round(IsoUtil.isoY(cellWidth * (node.getX() + deltas[rotation][i][0]), cellWidth * (node.getY() + deltas[rotation][i][1]), TILE_HEIGHT*node.getZ()));
            spriteInstances.add(new SpriteInstance(ballastSprite, screenX, screenY, SpriteLayer.BALLAST));
            spriteInstances.add(new SpriteInstance(sleeperSprite, screenX, screenY, SpriteLayer.SLEEPERS));
            spriteInstances.add(new SpriteInstance(railSprite, screenX, screenY, SpriteLayer.RAILS));
        }
    }
}
