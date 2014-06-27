package openloco.rail;

import openloco.Assets;

import openloco.entities.Bridge;
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

        drawStraightBridgeWalls(sprites, node);
        drawBridgeSupports(sprites, node, deltas);
    }

    private void drawStraightBridgeWalls(List<SpriteInstance> sprites, TrackNode trackNode) {
        if (trackNode.getZ() > 0) {
            int spriteIndex;
            if (trackNode.getRotation() % 2 == 0) {
                spriteIndex = 39;
            } else {
                spriteIndex = 63;
            }

            Bridge bridge = assets.getBridge("BRDGBRCK");

            for (int i = 0; i < 2; i++) {
                OpenGlSprite sprite = OpenGlSprite.createFromRawSprite(bridge.getSprites().get(spriteIndex + i));
                int screenX = Math.round(IsoUtil.isoX(Tile.WIDTH * (trackNode.getX()), Tile.WIDTH * (trackNode.getY()), trackNode.getZ() * Tile.HEIGHT_STEP));
                int screenY = Math.round(IsoUtil.isoY(Tile.WIDTH * (trackNode.getX()), Tile.WIDTH * (trackNode.getY()), trackNode.getZ() * Tile.HEIGHT_STEP));
                sprites.add(new SpriteInstance(sprite, screenX, screenY, SpriteLayer.BRIDGE, trackNode.getZ() * Tile.HEIGHT_STEP + 1));
            }
        }
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

        Bridge bridge = assets.getBridge("BRDGBRCK");

        for (int i=0; i < 2; i++) {
            int zIndex = Tile.HEIGHT_STEP *node.getZ();
            OpenGlSprite sprite = OpenGlSprite.createFromRawSprite(track.getSprites().get(startIndex + i + (2*rotation)));
            int screenX = Math.round(IsoUtil.isoX(Tile.WIDTH * (node.getX() + deltas[rotation][i][0]), Tile.WIDTH * (node.getY() + deltas[rotation][i][1]), zIndex));
            int screenY = Math.round(IsoUtil.isoY(Tile.WIDTH * (node.getX() + deltas[rotation][i][0]), Tile.WIDTH * (node.getY() + deltas[rotation][i][1]), zIndex));
            spriteInstances.add(new SpriteInstance(sprite, screenX, screenY, SpriteLayer.RAILS, zIndex));

            //draw the bridge wedge
            int spriteIndex = 84+(node.getRotation()*6)+(3*i);

            OpenGlSprite wedgeSprite = OpenGlSprite.createFromRawSprite(bridge.getSprites().get(spriteIndex));
            OpenGlSprite wallSpriteW = OpenGlSprite.createFromRawSprite(bridge.getSprites().get(spriteIndex + 1));
            OpenGlSprite wallSpriteE = OpenGlSprite.createFromRawSprite(bridge.getSprites().get(spriteIndex + 2));

            spriteInstances.add(new SpriteInstance(wedgeSprite, screenX, screenY, SpriteLayer.BRIDGE, zIndex));
            spriteInstances.add(new SpriteInstance(wallSpriteW, screenX, screenY, SpriteLayer.BRIDGE, zIndex+1));
            spriteInstances.add(new SpriteInstance(wallSpriteE, screenX, screenY, SpriteLayer.BRIDGE, zIndex+1));
        }

        drawBridgeSupports(spriteInstances, node, deltas);
    }

    private void drawBridgeSupports(List<SpriteInstance> spriteInstances, TrackNode node, int[][][] allDeltas) {
        if (node.getZ() > 0) {
            int rotation = node.getRotation() % allDeltas.length;
            int[][] deltas = allDeltas[rotation];
            Bridge bridge = assets.getBridge("BRDGBRCK");

            int topIndex;
            int supportIndex;

            if (rotation % 2 == 0) {
                topIndex = 36;
                supportIndex = 16;
            }
            else {
                topIndex = 60;
                supportIndex = 24;
            }

            for (int i=0; i<deltas.length; i++) {
                for (int j=0; j<3; j++) {
                    OpenGlSprite baseSprite = OpenGlSprite.createFromRawSprite(bridge.getSprites().get(topIndex+j));
                    int zIndex = Tile.HEIGHT_STEP * (node.getZ() - 1);
                    int screenX = Math.round(IsoUtil.isoX(Tile.WIDTH * (node.getX() + deltas[i][0]), Tile.WIDTH * (node.getY() + deltas[i][1]), zIndex));
                    int screenY = Math.round(IsoUtil.isoY(Tile.WIDTH * (node.getX() + deltas[i][0]), Tile.WIDTH * (node.getY() + deltas[i][1]), zIndex));
                    spriteInstances.add(new SpriteInstance(baseSprite, screenX, screenY, SpriteLayer.BRIDGE, zIndex+2));
                }


                for (int z = node.getZ()-1; z>=0; z--) {
                    OpenGlSprite supportSprite1 = OpenGlSprite.createFromRawSprite(bridge.getSprites().get(supportIndex));
                    OpenGlSprite supportSprite2 = OpenGlSprite.createFromRawSprite(bridge.getSprites().get(supportIndex+1));
                    int zIndex = z* Tile.HEIGHT_STEP;
                    int screenX = Math.round(IsoUtil.isoX(Tile.WIDTH * (node.getX() + deltas[i][0]), Tile.WIDTH * (node.getY() + deltas[i][1]), zIndex));
                    int screenY = Math.round(IsoUtil.isoY(Tile.WIDTH * (node.getX() + deltas[i][0]), Tile.WIDTH * (node.getY() + deltas[i][1]), zIndex));
                    spriteInstances.add(new SpriteInstance(supportSprite1, screenX, screenY, SpriteLayer.BRIDGE, zIndex+1));
                    spriteInstances.add(new SpriteInstance(supportSprite2, screenX, screenY, SpriteLayer.BRIDGE, zIndex+1));
                }
            }
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
            int screenX = Math.round(IsoUtil.isoX(Tile.WIDTH * (node.getX() + deltas[rotation][i][0]), Tile.WIDTH * (node.getY() + deltas[rotation][i][1]), Tile.HEIGHT_STEP *node.getZ()));
            int screenY = Math.round(IsoUtil.isoY(Tile.WIDTH * (node.getX() + deltas[rotation][i][0]), Tile.WIDTH * (node.getY() + deltas[rotation][i][1]), Tile.HEIGHT_STEP *node.getZ()));
            spriteInstances.add(new SpriteInstance(ballastSprite, screenX, screenY, SpriteLayer.BALLAST, Tile.HEIGHT_STEP *node.getZ()));
            spriteInstances.add(new SpriteInstance(sleeperSprite, screenX, screenY, SpriteLayer.SLEEPERS, Tile.HEIGHT_STEP *node.getZ()));
            spriteInstances.add(new SpriteInstance(railSprite, screenX, screenY, SpriteLayer.RAILS, Tile.HEIGHT_STEP *node.getZ()));
        }
    }
}
