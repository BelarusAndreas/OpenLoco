package openloco.terrain;

import openloco.Assets;
import openloco.entities.CliffFace;
import openloco.entities.Ground;
import openloco.entities.Sprites;
import openloco.graphics.IsoUtil;
import openloco.graphics.OpenGlSprite;
import openloco.graphics.SpriteInstance;

import java.util.ArrayList;
import java.util.List;

public class TerrainRenderer {

    private final List<OpenGlSprite> tiles = new ArrayList<>();
    private final List<OpenGlSprite> cliffSpritesSw = new ArrayList<>();
    private final List<OpenGlSprite> cliffSpritesSe = new ArrayList<>();

    private static final int w = Terrain.CELL_WIDTH;
    private static final int h = Terrain.HEIGHT_STEP;

    public TerrainRenderer(Assets assets) {
        Ground ground = assets.getGround("GRASS1  ");
        for (int i=385; i<400; i++) {
            Sprites.RawSprite sprite = ground.getSprites().get(i);
            tiles.add(OpenGlSprite.createFromRawSprite(sprite));
        }
        CliffFace cliffFace = assets.getCliffFace(ground.getCliff().getObjectReference());
        for (int i=0; i<32; i++) {
            Sprites.RawSprite sprite = cliffFace.getSprites().get(i);
            OpenGlSprite openGlSprite = OpenGlSprite.createFromRawSprite(sprite);
            if (i < 16) {
                cliffSpritesSw.add(openGlSprite);
            }
            else {
                cliffSpritesSe.add(openGlSprite);
            }
        }
    }

    public List<SpriteInstance> render(Terrain terrain) {
        List<SpriteInstance> spriteInstances = new ArrayList<>();

        for (int i=0; i<terrain.getWidth(); i++) {
            for (int j = 0; j < terrain.getHeight(); j++) {
                OpenGlSprite sprite = tiles.get(terrain.getTileType(i, j));
                int cartX = i * w;
                int cartY = j * w;
                int cartZ = h *terrain.getTileHeight(i, j);
                int isoX = Math.round(IsoUtil.isoX(cartX, cartY, cartZ));
                int isoY = Math.round(IsoUtil.isoY(cartX, cartY, cartZ));
                spriteInstances.add(new SpriteInstance(sprite, isoX, isoY));

                int aS = terrain.getCornerHeight(i, j, Terrain.S);

                if (j < terrain.getHeight()-1) {
                    int aW = terrain.getCornerHeight(i, j, Terrain.W);
                    int bN = terrain.getCornerHeight(i, j + 1, Terrain.N);
                    int bE = terrain.getCornerHeight(i, j + 1, Terrain.E);
                    if (aW > bN || aS > bE) {
                        int fromZ = h * Math.max(bN, bE);
                        int toZ = h * Math.min(aW, aS);

                        fillInCliffs(spriteInstances, fromZ, toZ, i, j+1, cliffSpritesSw, +2);
                    }
                }

                if (i < terrain.getWidth()-1) {
                    int aE = terrain.getCornerHeight(i, j, Terrain.E);
                    int bW = terrain.getCornerHeight(i + 1, j, Terrain.W);
                    int bN = terrain.getCornerHeight(i + 1, j, Terrain.N);
                    if (aS > bW || aE > bN) {
                        int fromZ = h * Math.max(bW, bN);
                        int toZ = h * Math.min(aS, aE);

                        fillInCliffs(spriteInstances, fromZ, toZ, i+1, j, cliffSpritesSe, -2);
                    }
                }
            }
        }
        return spriteInstances;
    }

    private void fillInCliffs(List<SpriteInstance> spriteInstances, int fromZ, int toZ, int xIndex, int yIndex, List<OpenGlSprite> cliffSprites, int offset) {
        for (int z=fromZ; z<toZ; z+=h) {
            int x = Math.round(IsoUtil.isoX(xIndex * w, yIndex * w, z)) + offset;
            int y = Math.round(IsoUtil.isoY(xIndex * w, yIndex * w, z)) - 1;
            spriteInstances.add(new SpriteInstance(cliffSprites.get(0), x, y));
        }
    }

}
