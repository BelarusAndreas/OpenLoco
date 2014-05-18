package openloco.terrain;

import openloco.Assets;
import openloco.entities.Ground;
import openloco.entities.Sprites;
import openloco.graphics.IsoUtil;
import openloco.graphics.OpenGlSprite;
import openloco.graphics.SpriteInstance;

import java.util.ArrayList;
import java.util.List;

public class TerrainRenderer {

    private final List<OpenGlSprite> tiles = new ArrayList<>();

    public TerrainRenderer(Assets assets) {
        Ground ground = assets.getGround("GRASS1  ");
        for (int i=385; i<400; i++) {
            Sprites.RawSprite sprite = ground.getSprites().get(i);
            tiles.add(OpenGlSprite.createFromRawSprite(sprite));
        }
    }

    public List<SpriteInstance> render(Terrain terrain) {
        List<SpriteInstance> spriteInstances = new ArrayList<>();
        for (int i=0; i<terrain.getWidth(); i++) {
            for (int j = 0; j < terrain.getHeight(); j++) {
                OpenGlSprite sprite = tiles.get(terrain.getTileType(i, j));
                int cartX = i * terrain.getCellWidth();
                int cartY = j * terrain.getCellWidth();
                int cartZ = terrain.getHeightStep()*terrain.getTileHeight(i, j);
                int isoX = Math.round(IsoUtil.isoX(cartX, cartY, cartZ));
                int isoY = Math.round(IsoUtil.isoY(cartX, cartY, cartZ));
                spriteInstances.add(new SpriteInstance(sprite, isoX, isoY));
            }
        }
        return spriteInstances;
    }

}
