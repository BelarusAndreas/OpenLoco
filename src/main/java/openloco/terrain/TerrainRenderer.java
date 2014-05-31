package openloco.terrain;

import openloco.Assets;
import openloco.Palette;
import openloco.entities.CliffFace;
import openloco.entities.Ground;
import openloco.entities.Sprites;
import openloco.graphics.IsoUtil;
import openloco.graphics.OpenGlSprite;
import openloco.graphics.SpriteInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TerrainRenderer {

    private final List<OpenGlSprite> tiles = new ArrayList<>();
    private final List<OpenGlSprite> cliffSpritesSw = new ArrayList<>();
    private final List<OpenGlSprite> cliffSpritesSe = new ArrayList<>();

    private static final int w = Terrain.CELL_WIDTH;
    private static final int h = Terrain.HEIGHT_STEP;

    private enum CliffSpriteType {
        UNCLIPPED,
        HL_TOP,
        LH_TOP,
        HL_BOTTOM,
        LH_BOTTOM
    };

    public TerrainRenderer(Assets assets) {
        Ground ground = assets.getGround("GRASS1  ");
        for (int i=385; i<400; i++) {
            Sprites.RawSprite sprite = ground.getSprites().get(i);
            tiles.add(OpenGlSprite.createFromRawSprite(sprite));
        }

        CliffFace cliffFace = assets.getCliffFace(ground.getCliff().getObjectReference());
        for (int i=0; i<64; i++) {
            Sprites.RawSprite sprite = cliffFace.getSprites().get(i);
            OpenGlSprite openGlSprite = OpenGlSprite.createFromRawSprite(sprite);
            if ((i%32) < 16) {
                cliffSpritesSw.add(openGlSprite);
                cliffSpritesSw.add(OpenGlSprite.createFromRawSprite(maskDiagonalNwSeTop(sprite)));
                cliffSpritesSw.add(OpenGlSprite.createFromRawSprite(maskTop(sprite)));
                cliffSpritesSw.add(OpenGlSprite.createFromRawSprite(maskDiagonalNwSeBottom(sprite)));
                cliffSpritesSw.add(OpenGlSprite.createFromRawSprite(maskBottom(sprite)));
            }
            else {
                cliffSpritesSe.add(openGlSprite);
                cliffSpritesSe.add(OpenGlSprite.createFromRawSprite(maskTop(sprite)));
                cliffSpritesSe.add(OpenGlSprite.createFromRawSprite(maskDiagonalSwNeTop(sprite)));
                cliffSpritesSe.add(OpenGlSprite.createFromRawSprite(maskBottom(sprite)));
                cliffSpritesSe.add(OpenGlSprite.createFromRawSprite(maskDiagonalSwNeBottom(sprite)));
            }
        }
    }

    private OpenGlSprite getCliffSprite(List<OpenGlSprite> sprites, int index, CliffSpriteType spriteType) {
        return sprites.get(index * CliffSpriteType.values().length + spriteType.ordinal());
    }

    private Sprites.RawSprite maskTop(Sprites.RawSprite sprite) {
        int from = 0;
        int middle = sprite.getHeader().getHeight()/2;
        int to = middle * sprite.getHeader().getWidth();
        return maskRange(sprite, from, to);
    }

    private Sprites.RawSprite maskBottom(Sprites.RawSprite sprite) {
        int middle = sprite.getHeader().getHeight()/2;
        int from = middle * sprite.getHeader().getWidth();
        int to = sprite.getPixels().length;
        return maskRange(sprite, from, to);
    }

    private Sprites.RawSprite maskDiagonalNwSeTop(Sprites.RawSprite sprite) {
        Sprites.SpriteHeader header = sprite.getHeader();
        int[] maskedPixels = Arrays.copyOf(sprite.getPixels(), sprite.getPixels().length);
        for (int i=0; i<header.getHeight(); i++) {
            int offset = i*header.getWidth();
            Arrays.fill(maskedPixels, offset+i+1, offset+header.getWidth(), Palette.BACKGROUND);
        }
        return new Sprites.RawSprite(sprite.getHeader(), maskedPixels);
    }

    private Sprites.RawSprite maskDiagonalNwSeBottom(Sprites.RawSprite sprite) {
        Sprites.SpriteHeader header = sprite.getHeader();
        int[] maskedPixels = Arrays.copyOf(sprite.getPixels(), sprite.getPixels().length);
        for (int i=0; i<header.getHeight(); i++) {
            int offset = i*header.getWidth();
            Arrays.fill(maskedPixels, offset, offset+i, Palette.BACKGROUND);
        }
        return new Sprites.RawSprite(sprite.getHeader(), maskedPixels);
    }

    private Sprites.RawSprite maskDiagonalSwNeTop(Sprites.RawSprite sprite) {
        Sprites.SpriteHeader header = sprite.getHeader();
        int[] maskedPixels = Arrays.copyOf(sprite.getPixels(), sprite.getPixels().length);
        for (int i=0; i<header.getHeight(); i++) {
            int offset = i*header.getWidth();
            Arrays.fill(maskedPixels, offset, offset+header.getWidth()-i-1, Palette.BACKGROUND);
        }
        return new Sprites.RawSprite(sprite.getHeader(), maskedPixels);
    }

    private Sprites.RawSprite maskDiagonalSwNeBottom(Sprites.RawSprite sprite) {
        Sprites.SpriteHeader header = sprite.getHeader();
        int[] maskedPixels = Arrays.copyOf(sprite.getPixels(), sprite.getPixels().length);
        for (int i=0; i<header.getHeight(); i++) {
            int offset = i*header.getWidth();
            Arrays.fill(maskedPixels, offset+header.getWidth()-i, offset+header.getWidth(), Palette.BACKGROUND);
        }
        return new Sprites.RawSprite(sprite.getHeader(), maskedPixels);
    }

    private Sprites.RawSprite maskRange(Sprites.RawSprite sprite, int from, int to) {
        int[] maskedPixels = Arrays.copyOf(sprite.getPixels(), sprite.getPixels().length);
        Arrays.fill(maskedPixels, from, to, Palette.BACKGROUND);
        return new Sprites.RawSprite(sprite.getHeader(), maskedPixels);
    }

    public List<SpriteInstance> render(Terrain terrain) {
        List<SpriteInstance> spriteInstances = new ArrayList<>();

        for (int i=0; i<terrain.getXMax(); i++) {
            for (int j = 0; j < terrain.getYMax(); j++) {
                OpenGlSprite sprite = tiles.get(terrain.getTileType(i, j));
                int cartX = i * w;
                int cartY = j * w;
                int cartZ = h *terrain.getTileHeight(i, j);
                int isoX = Math.round(IsoUtil.isoX(cartX, cartY, cartZ));
                int isoY = Math.round(IsoUtil.isoY(cartX, cartY, cartZ));
                spriteInstances.add(new SpriteInstance(sprite, isoX, isoY));

                if (j < terrain.getYMax()-1) {
                    int aW = terrain.getCornerHeight(i, j, Terrain.W);
                    int aS = terrain.getCornerHeight(i, j, Terrain.S);
                    int bN = terrain.getCornerHeight(i, j + 1, Terrain.N);
                    int bE = terrain.getCornerHeight(i, j + 1, Terrain.E);

                    renderCliffs(spriteInstances, i, j + 1, aW, aS, bN, bE, 2, cliffSpritesSw);
                }

                if (i < terrain.getXMax()-1) {
                    int aS = terrain.getCornerHeight(i, j, Terrain.S);
                    int aE = terrain.getCornerHeight(i, j, Terrain.E);
                    int bW = terrain.getCornerHeight(i + 1, j, Terrain.W);
                    int bN = terrain.getCornerHeight(i + 1, j, Terrain.N);

                    renderCliffs(spriteInstances, i + 1, j, aS, aE, bW, bN, -2, cliffSpritesSe);
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
        if (a < b) {
            int z = h * a;
            int x = Math.round(IsoUtil.isoX(xIndex * w, yIndex * w, z)) + offset;
            int y = Math.round(IsoUtil.isoY(xIndex * w, yIndex * w, z)) - 1;
            spriteInstances.add(new SpriteInstance(getCliffSprite(cliffSprites, 0, lhSpriteType), x, y));
        }
        else if (b < a) {
            int z = h * b;
            int x = Math.round(IsoUtil.isoX(xIndex * w, yIndex * w, z)) + offset;
            int y = Math.round(IsoUtil.isoY(xIndex * w, yIndex * w, z)) - 1;
            spriteInstances.add(new SpriteInstance(getCliffSprite(cliffSprites, 0, hlSpriteType), x, y));
        }
    }

    private void fillInCliffs(List<SpriteInstance> spriteInstances, int fromZ, int toZ, int xIndex, int yIndex, List<OpenGlSprite> cliffSprites, int offset) {
        for (int z=fromZ; z<toZ; z+=h) {
            int x = Math.round(IsoUtil.isoX(xIndex * w, yIndex * w, z)) + offset;
            int y = Math.round(IsoUtil.isoY(xIndex * w, yIndex * w, z)) - 1;
            spriteInstances.add(new SpriteInstance(cliffSprites.get(0), x, y));
        }
    }

}
