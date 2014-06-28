package openloco.rail;

import openloco.Assets;

import openloco.entities.Bridge;
import openloco.entities.Track;
import openloco.graphics.*;

import java.util.ArrayList;
import java.util.List;

import static openloco.rail.BridgeTileType.*;

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

        BridgeTileType[][] bridgeTileTypes = {
                { FULL_WALL_EW },
                { FULL_WALL_NS }
        };

        drawBridgeTiles(sprites, node, deltas, bridgeTileTypes);
    }

    private void drawSmallCurve(Track track, List<SpriteInstance> spriteInstances, TrackNode node) {
        int[][][] deltas = {
                { {0, 0}, {1, 0}, {0,-1}, {1,-1} },
                { {0, 0}, {0, 1}, {1, 0}, {1, 1} },
                { {0, 0}, {-1,0}, {0, 1}, {-1,1} },
                { {0, 0}, {0,-1}, {-1,0}, {-1,-1} }
        };

        BridgeTileType[][] bridgeTileTypes = {
            { FULL_WALL_W, HALF_NW, HALF_SE, FULL_WALL_N },
            { FULL_WALL_N, HALF_NE, HALF_SW, FULL_WALL_E },
            { FULL_WALL_E, HALF_SE, HALF_NW, FULL_WALL_S },
            { FULL_WALL_S, HALF_SW, HALF_NE, FULL_WALL_W }
        };

        drawTrackPiece(track, spriteInstances, node, 4, 4, deltas, 24);
        drawBridgeTiles(spriteInstances, node, deltas, bridgeTileTypes);
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

        BridgeTileType[][] bridgeTileTypes = {
            { FULL_WALL_EW, HALF_SW, HALF_NE, FULL_WALL_EW },
            { FULL_WALL_NS, HALF_NW, HALF_SE, FULL_WALL_NS },
            { FULL_WALL_EW, HALF_SE, HALF_NW, FULL_WALL_EW },
            { FULL_WALL_NS, HALF_SW, HALF_NE, FULL_WALL_NS }
        };

        drawTrackPiece(track, spriteInstances, node, 4, 4, deltas, 352);
        drawBridgeTiles(spriteInstances, node, deltas, bridgeTileTypes);
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

        Bridge bridge = assets.getBridge(node.getBridgeType());

        for (int i=0; i < 2; i++) {
            int zIndex = Tile.HEIGHT_STEP *node.getZ();
            OpenGlSprite sprite = OpenGlSprite.createFromRawSprite(track.getSprites().get(startIndex + i + (2*rotation)));
            int cartX = Tile.WIDTH * (node.getX() + deltas[rotation][i][0]);
            int cartY = Tile.WIDTH * (node.getY() + deltas[rotation][i][1]);
            int screenX = Math.round(IsoUtil.isoX(cartX, cartY, zIndex));
            int screenY = Math.round(IsoUtil.isoY(cartX, cartY, zIndex));
            spriteInstances.add(new SpriteInstance(sprite, screenX, screenY, SpriteLayer.RAILS, cartX, cartY, zIndex));

            //draw the bridge wedge
            int spriteIndex = 84+(node.getRotation()*6)+(3*i);

            OpenGlSprite wedgeSprite = OpenGlSprite.createFromRawSprite(bridge.getSprites().get(spriteIndex));
            OpenGlSprite wallSpriteW = OpenGlSprite.createFromRawSprite(bridge.getSprites().get(spriteIndex + 1));
            OpenGlSprite wallSpriteE = OpenGlSprite.createFromRawSprite(bridge.getSprites().get(spriteIndex + 2));

            spriteInstances.add(new SpriteInstance(wedgeSprite, screenX, screenY, SpriteLayer.BRIDGE, cartX, cartY, zIndex));
            spriteInstances.add(new SpriteInstance(wallSpriteW, screenX, screenY, SpriteLayer.BRIDGE, cartX, cartY, zIndex+1));
            spriteInstances.add(new SpriteInstance(wallSpriteE, screenX, screenY, SpriteLayer.BRIDGE, cartX, cartY, zIndex+1));
        }

        drawBridgeSupports(spriteInstances, node, deltas);
    }

    private void drawBridgeSupports(List<SpriteInstance> spriteInstances, TrackNode node, int[][][] allDeltas) {
        if (node.getZ() > 0) {
            int rotation = node.getRotation() % allDeltas.length;
            int[][] deltas = allDeltas[rotation];
            Bridge bridge = assets.getBridge(node.getBridgeType());

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
                int cartX = Tile.WIDTH * (node.getX() + deltas[i][0]);
                int cartY = Tile.WIDTH * (node.getY() + deltas[i][1]);

                for (int j=0; j<3; j++) {
                    OpenGlSprite baseSprite = OpenGlSprite.createFromRawSprite(bridge.getSprites().get(topIndex+j));
                    int zIndex = Tile.HEIGHT_STEP * (node.getZ() - 1);
                    int screenX = Math.round(IsoUtil.isoX(cartX, cartY, zIndex));
                    int screenY = Math.round(IsoUtil.isoY(cartX, cartY, zIndex));
                    spriteInstances.add(new SpriteInstance(baseSprite, screenX, screenY, SpriteLayer.BRIDGE, cartX, cartY, zIndex+2));
                }


                for (int z = node.getZ()-1; z>=0; z--) {
                    OpenGlSprite supportSprite1 = OpenGlSprite.createFromRawSprite(bridge.getSprites().get(supportIndex));
                    OpenGlSprite supportSprite2 = OpenGlSprite.createFromRawSprite(bridge.getSprites().get(supportIndex+1));
                    int zIndex = z* Tile.HEIGHT_STEP;
                    int screenX = Math.round(IsoUtil.isoX(cartX, cartY, zIndex));
                    int screenY = Math.round(IsoUtil.isoY(cartX, cartY, zIndex));
                    spriteInstances.add(new SpriteInstance(supportSprite1, screenX, screenY, SpriteLayer.BRIDGE, cartX, cartY, zIndex+1));
                    spriteInstances.add(new SpriteInstance(supportSprite2, screenX, screenY, SpriteLayer.BRIDGE, cartX, cartY, zIndex+1));
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
            int cartX = Tile.WIDTH * (node.getX() + deltas[rotation][i][0]);
            int cartY = Tile.WIDTH * (node.getY() + deltas[rotation][i][1]);
            int screenX = Math.round(IsoUtil.isoX(cartX, cartY, Tile.HEIGHT_STEP *node.getZ()));
            int screenY = Math.round(IsoUtil.isoY(cartX, cartY, Tile.HEIGHT_STEP *node.getZ()));
            spriteInstances.add(new SpriteInstance(ballastSprite, screenX, screenY, SpriteLayer.BALLAST, cartX, cartY, Tile.HEIGHT_STEP * node.getZ()));
            spriteInstances.add(new SpriteInstance(sleeperSprite, screenX, screenY, SpriteLayer.SLEEPERS, cartX, cartY, Tile.HEIGHT_STEP * node.getZ()));
            spriteInstances.add(new SpriteInstance(railSprite, screenX, screenY, SpriteLayer.RAILS, cartX, cartY, Tile.HEIGHT_STEP * node.getZ()));
        }
    }

    private void drawBridgeTiles(List<SpriteInstance> sprites, TrackNode node, int[][][] allOffsets, BridgeTileType[][] allBridgeTileTypes) {
        int[][] offsets = allOffsets[node.getRotation()];
        BridgeTileType[] bridgeTileTypes = allBridgeTileTypes[node.getRotation()];

        Bridge bridge = assets.getBridge(node.getBridgeType());

        for (int i=0; i<offsets.length; i++) {
            drawBridgeTile(sprites, bridge, node, offsets[i], bridgeTileTypes[i]);
        }
    }

    private void drawBridgeTile(List<SpriteInstance> sprites, Bridge bridge, TrackNode trackNode, int[] offset, BridgeTileType bridgeTileType) {
        int[] walls = bridgeTileType.getWallSprites();
        int[] base = bridgeTileType.getBaseSprites();
        int[] supports = bridgeTileType.getSupportSprites();

        int x = (trackNode.getX()+offset[0])*Tile.WIDTH;
        int y = (trackNode.getY()+offset[1])*Tile.WIDTH;
        int z = trackNode.getZ()*Tile.HEIGHT_STEP;

        for (int u=0; u<walls.length; u++) {
            int wallSprite = walls[u];
            outputBridgeSprite(sprites, bridge, wallSprite, x, y, z);
        }

        for (int v=0; v<base.length; v++) {
            int baseSprite = base[v];
            outputBridgeSprite(sprites, bridge, baseSprite, x, y, z-Tile.HEIGHT_STEP);
        }

        for (int h=trackNode.getZ()-2; h>=0; h--) {
            for (int s=0; s<supports.length; s++) {
                int supportSprite = supports[s];
                outputBridgeSprite(sprites, bridge, supportSprite, x, y, h*Tile.HEIGHT_STEP);
            }
        }
    }

    private void outputBridgeSprite(List<SpriteInstance> sprites, Bridge bridge, int spriteIndex, int cartX, int cartY, int cartZ) {
        OpenGlSprite sprite = OpenGlSprite.createFromRawSprite(bridge.getSprites().get(spriteIndex));
        int screenX = Math.round(IsoUtil.isoX(cartX, cartY, cartZ));
        int screenY = Math.round(IsoUtil.isoY(cartX, cartY, cartZ));
        sprites.add(new SpriteInstance(sprite, screenX, screenY, SpriteLayer.BRIDGE, cartX, cartY, cartZ));

    }
}
