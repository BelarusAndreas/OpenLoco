package openloco.terrain;

import openloco.Assets;
import openloco.entities.Ground;
import openloco.graphics.*;

import java.util.*;

public class TerrainRenderer implements Renderer<Terrain> {

    private static final int w = Tile.WIDTH;
    private static final int h = Tile.HEIGHT_STEP;

    private final Assets assets;
    private final Map<String, TerrainSprites> terrainSprites = new HashMap<>();

    private enum CliffSpriteType {
        UNCLIPPED,
        HL_TOP,
        LH_TOP,
        HL_BOTTOM,
        LH_BOTTOM
    }

    public TerrainRenderer(Assets assets) {
        this.assets = assets;
    }

    private OpenGlSprite getCliffSprite(List<OpenGlSprite> sprites, int index, CliffSpriteType spriteType) {
        return sprites.get(index * CliffSpriteType.values().length + spriteType.ordinal());
    }

    public void addTerrainSprites(String groundType, TerrainSprites terrainSprites) {
        this.terrainSprites.put(groundType, terrainSprites);
    }

    @Override
    public List<SpriteInstance> render(Terrain terrain) {
        List<SpriteInstance> spriteInstances = new ArrayList<>();

        for (int i=0; i<terrain.getXMax(); i++) {
            for (int j = 0; j < terrain.getYMax(); j++) {
                String groundType = terrain.getGroundType(i, j);

                if (!terrainSprites.containsKey(groundType)) {
                    Ground ground = assets.getGround(groundType);
                    addTerrainSprites(groundType, new TerrainSprites(assets, ground));
                }

                TerrainSprites currentTerrainSprites = terrainSprites.get(groundType);

                OpenGlSprite sprite = currentTerrainSprites.getTileType(terrain.getTileType(i, j));
                CartCoord cartCoord = new CartCoord(i * w, j * w, h *terrain.getTileHeight(i, j));
                spriteInstances.add(new SpriteInstance(sprite, SpriteLayer.TERRAIN, cartCoord));

                if (j < terrain.getYMax()-1) {
                    int aW = terrain.getCornerHeight(i, j, Terrain.W);
                    int aS = terrain.getCornerHeight(i, j, Terrain.S);
                    int bN = terrain.getCornerHeight(i, j + 1, Terrain.N);
                    int bE = terrain.getCornerHeight(i, j + 1, Terrain.E);

                    renderCliffs(spriteInstances, i, j + 1, aW, aS, bN, bE, 2, currentTerrainSprites.getCliffSpritesSw());
                }

                if (i < terrain.getXMax()-1) {
                    int aS = terrain.getCornerHeight(i, j, Terrain.S);
                    int aE = terrain.getCornerHeight(i, j, Terrain.E);
                    int bW = terrain.getCornerHeight(i + 1, j, Terrain.W);
                    int bN = terrain.getCornerHeight(i + 1, j, Terrain.N);

                    renderCliffs(spriteInstances, i + 1, j, aS, aE, bW, bN, -2, currentTerrainSprites.getCliffSpritesSe());
                }
            }
        }
        return spriteInstances;
    }

    private void renderCliffs(List<SpriteInstance> spriteInstances, int xIndex, int yIndex, int a, int b, int c, int d, int offset, List<OpenGlSprite> cliffSprites) {
        if (a > c || b > d) {
            int fromZ = h * Math.max(c, d);
            int toZ = h * Math.min(a, b);

            fillInCliffs(spriteInstances, fromZ, toZ, xIndex, yIndex, cliffSprites, offset);

            renderDiagonalCliff(spriteInstances, xIndex, yIndex, a, b, offset, cliffSprites, CliffSpriteType.LH_TOP, CliffSpriteType.HL_TOP);
            renderDiagonalCliff(spriteInstances, xIndex, yIndex, c, d, offset, cliffSprites, CliffSpriteType.LH_BOTTOM, CliffSpriteType.HL_BOTTOM);
        }
    }

    private void renderDiagonalCliff(List<SpriteInstance> spriteInstances, int xIndex, int yIndex, int a, int b, int offset, List<OpenGlSprite> cliffSprites, CliffSpriteType lhSpriteType, CliffSpriteType hlSpriteType) {
        if (a != b) {
            int z = h * Math.min(a, b);
            CliffSpriteType spriteType = (a < b) ? lhSpriteType : hlSpriteType;
            int x = Math.round(IsoUtil.isoX(xIndex * w, yIndex * w, z)) + offset;
            int y = Math.round(IsoUtil.isoY(xIndex * w, yIndex * w, z)) - 1;
            spriteInstances.add(new SpriteInstance(getCliffSprite(cliffSprites, 0, spriteType), x, y, SpriteLayer.TERRAIN, xIndex * w, yIndex * w, z));
        }
    }

    private void fillInCliffs(List<SpriteInstance> spriteInstances, int fromZ, int toZ, int xIndex, int yIndex, List<OpenGlSprite> cliffSprites, int offset) {
        for (int z=fromZ; z<toZ; z+=h) {
            int x = Math.round(IsoUtil.isoX(xIndex * w, yIndex * w, z)) + offset;
            int y = Math.round(IsoUtil.isoY(xIndex * w, yIndex * w, z)) - 1;
            spriteInstances.add(new SpriteInstance(cliffSprites.get(0), x, y, SpriteLayer.TERRAIN, xIndex * w, yIndex * w, z));
        }
    }

}
