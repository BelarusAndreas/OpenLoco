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
    private final List<OpenGlSprite> cliffSpritesSwLhTop = new ArrayList<>();
    private final List<OpenGlSprite> cliffSpritesSwHlTop = new ArrayList<>();
    private final List<OpenGlSprite> cliffSpritesSwLhBottom = new ArrayList<>();
    private final List<OpenGlSprite> cliffSpritesSwHlBottom = new ArrayList<>();
    private final List<OpenGlSprite> cliffSpritesSe = new ArrayList<>();
    private final List<OpenGlSprite> cliffSpritesSeLhTop = new ArrayList<>();
    private final List<OpenGlSprite> cliffSpritesSeHlTop = new ArrayList<>();
    private final List<OpenGlSprite> cliffSpritesSeLhBottom = new ArrayList<>();
    private final List<OpenGlSprite> cliffSpritesSeHlBottom = new ArrayList<>();

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
                cliffSpritesSwHlTop.add(OpenGlSprite.createFromRawSprite(maskDiagonalNwSeTop(sprite)));
                cliffSpritesSwLhTop.add(OpenGlSprite.createFromRawSprite(maskTop(sprite)));
                cliffSpritesSwHlBottom.add(OpenGlSprite.createFromRawSprite(maskDiagonalNwSeBottom(sprite)));
                cliffSpritesSwLhBottom.add(OpenGlSprite.createFromRawSprite(maskBottom(sprite)));
            }
            else {
                cliffSpritesSe.add(openGlSprite);
                cliffSpritesSeLhTop.add(OpenGlSprite.createFromRawSprite(maskDiagonalSwNeTop(sprite)));
                cliffSpritesSeHlTop.add(OpenGlSprite.createFromRawSprite(maskTop(sprite)));
                cliffSpritesSeLhBottom.add(OpenGlSprite.createFromRawSprite(maskDiagonalSwNeBottom(sprite)));
                cliffSpritesSeHlBottom.add(OpenGlSprite.createFromRawSprite(maskBottom(sprite)));
            }
        }
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

                int aS = terrain.getCornerHeight(i, j, Terrain.S);

                if (j < terrain.getYMax()-1) {
                    int xIndex = i;
                    int yIndex = j + 1;
                    int aW = terrain.getCornerHeight(xIndex, yIndex-1, Terrain.W);
                    int bN = terrain.getCornerHeight(xIndex, yIndex, Terrain.N);
                    int bE = terrain.getCornerHeight(xIndex, yIndex, Terrain.E);
                    if (aW > bN || aS > bE) {
                        int fromZ = h * Math.max(bN, bE);
                        int toZ = h * Math.min(aW, aS);
                        int offset = +2;
                        fillInCliffs(spriteInstances, fromZ, toZ, i, yIndex, cliffSpritesSw, offset);

                        if (aW < aS) {
                            int z = h * aW;
                            int x = Math.round(IsoUtil.isoX(xIndex * w, yIndex * w, z)) + offset;
                            int y = Math.round(IsoUtil.isoY(xIndex * w, yIndex * w, z)) - 1;
                            spriteInstances.add(new SpriteInstance(cliffSpritesSwLhTop.get(0), x, y));
                        }
                        else if (aS < aW) {
                            int z = h * aS;
                            int x = Math.round(IsoUtil.isoX(xIndex * w, yIndex * w, z)) + offset;
                            int y = Math.round(IsoUtil.isoY(xIndex * w, yIndex * w, z)) - 1;
                            spriteInstances.add(new SpriteInstance(cliffSpritesSwHlTop.get(0), x, y));
                        }

                        if (bN < bE) {
                            int z = h * bN;
                            int x = Math.round(IsoUtil.isoX(xIndex * w, yIndex * w, z)) + offset;
                            int y = Math.round(IsoUtil.isoY(xIndex * w, yIndex * w, z)) - 1;
                            spriteInstances.add(new SpriteInstance(cliffSpritesSwLhBottom.get(0), x, y));
                        }
                        else if (bE < bN) {
                            int z = h * bE;
                            int x = Math.round(IsoUtil.isoX(xIndex * w, yIndex * w, z)) + offset;
                            int y = Math.round(IsoUtil.isoY(xIndex * w, yIndex * w, z)) - 1;
                            spriteInstances.add(new SpriteInstance(cliffSpritesSwHlBottom.get(0), x, y));
                        }
                    }
                }

                if (i < terrain.getXMax()-1) {
                    int xIndex = i + 1;
                    int yIndex = j;
                    int aE = terrain.getCornerHeight(xIndex-1, yIndex, Terrain.E);
                    int bW = terrain.getCornerHeight(xIndex, yIndex, Terrain.W);
                    int bN = terrain.getCornerHeight(xIndex, yIndex, Terrain.N);
                    if (aS > bW || aE > bN) {
                        int fromZ = h * Math.max(bW, bN);
                        int toZ = h * Math.min(aS, aE);

                        int offset = -2;
                        fillInCliffs(spriteInstances, fromZ, toZ, xIndex, j, cliffSpritesSe, offset);

                        if (aS < aE) {
                            int z = h * aS;
                            int x = Math.round(IsoUtil.isoX(xIndex * w, yIndex * w, z)) + offset;
                            int y = Math.round(IsoUtil.isoY(xIndex * w, yIndex * w, z)) - 1;
                            spriteInstances.add(new SpriteInstance(cliffSpritesSeLhTop.get(0), x, y));
                        }
                        else if (aE < aS) {
                            int z = h * aE;
                            int x = Math.round(IsoUtil.isoX(xIndex * w, yIndex * w, z)) + offset;
                            int y = Math.round(IsoUtil.isoY(xIndex * w, yIndex * w, z)) - 1;
                            spriteInstances.add(new SpriteInstance(cliffSpritesSeHlTop.get(0), x, y));
                        }

                        if (bW < bN) {
                            int z = h * bW;
                            int x = Math.round(IsoUtil.isoX(xIndex * w, yIndex * w, z)) + offset;
                            int y = Math.round(IsoUtil.isoY(xIndex * w, yIndex * w, z)) - 1;
                            spriteInstances.add(new SpriteInstance(cliffSpritesSeLhBottom.get(0), x, y));
                        }
                        else if (bN < bW) {
                            int z = h * bN;
                            int x = Math.round(IsoUtil.isoX(xIndex * w, yIndex * w, z)) + offset;
                            int y = Math.round(IsoUtil.isoY(xIndex * w, yIndex * w, z)) - 1;
                            spriteInstances.add(new SpriteInstance(cliffSpritesSeHlBottom.get(0), x, y));
                        }
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
