package openloco.entities;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class Sprites {

    private List<RawSprite> sprites = new ArrayList<>();

    public Sprites(List<RawSprite> sprites) {
        this.sprites = sprites;
    }

    public RawSprite get(int index) {
        return sprites.get(index);
    }

    public List<RawSprite> getList() {
        return sprites;
    }

    public static class SpriteHeader {
        private long offset;
        private int width;
        private int height;
        private EnumSet<SpriteFlag> flags;
        private int xOffset;
        private int yOffset;

        public SpriteHeader(long offset, int width, int height, int xOffset, int yOffset, EnumSet<SpriteFlag> flags) {
            this.offset = offset;
            this.width = width;
            this.height = height;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.flags = flags;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public int getXOffset() {
            return xOffset;
        }

        public int getYOffset() {
            return yOffset;
        }

        public EnumSet<SpriteFlag> getFlags() {
            return flags;
        }
    }

    public static class RawSprite {
        private final SpriteHeader header;
        private final int[] pixels;

        public RawSprite(SpriteHeader header, int[] pixels) {
            this.header = header;
            this.pixels = pixels;
        }

        public SpriteHeader getHeader() {
            return header;
        }

        public int[] getPixels() {
            return pixels;
        }
    }

    public enum SpriteFlag {
        HASDATA,
        unknown1,
        CHUNKED,
        unknown2,
        unknown3,
        unknown4,
        COPY,
        unknown5
    }

}
