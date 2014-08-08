package openloco.terrain;

import openloco.Assets;
import openloco.Palette;
import openloco.entities.CliffFace;
import openloco.entities.Ground;
import openloco.entities.Sprites;
import openloco.graphics.OpenGlSprite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TerrainSprites {

    private static final int LEVELS_OF_DETAIL = 4;

    private final List<OpenGlSprite> tiles = new ArrayList<>();
    private final List<OpenGlSprite> cliffSpritesSw = new ArrayList<>();
    private final List<OpenGlSprite> cliffSpritesSe = new ArrayList<>();

    public TerrainSprites(Assets assets, Ground ground) {
        int variation = ground.getGroundVars().getNumberOfVariations()-1;

        int offset = ground.getGroundVars().getNumberOfVariations() * (LEVELS_OF_DETAIL - 1) * 19 + (25 * variation);

        for (int i = offset; i < offset+15; i++) {
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

    public OpenGlSprite getTileType(int tileType) {
        return tiles.get(tileType);
    }

    public List<OpenGlSprite> getCliffSpritesSe() {
        return cliffSpritesSe;
    }

    public List<OpenGlSprite> getCliffSpritesSw() {
        return cliffSpritesSw;
    }
}
