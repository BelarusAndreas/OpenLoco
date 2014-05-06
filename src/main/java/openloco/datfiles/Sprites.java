package openloco.datfiles;

import openloco.Palette;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class Sprites {

    private List<RawSprite> sprites = new ArrayList<>();

    public Sprites(DatFileInputStream in) {
        try {
            long num = in.readUnsignedInt();
            long size = in.readUnsignedInt();

            List<SpriteHeader> spriteHeaders = new ArrayList<>();
            for (int i = 0; i < num; i++) {
                spriteHeaders.add(new SpriteHeader(in));
            }

            for (SpriteHeader header : spriteHeaders) {
                in.skipBytes(2*header.getHeight());
                int[] pixels = new int[header.getWidth() * header.getHeight()];
                if (header.flags.contains(SpriteFlag.CHUNKED)) {
                    for (int row=0; row<header.getHeight(); row++) {
                        int rowOffset = row * header.getWidth();
                        for (int col=0; col<header.getWidth(); col++) {
                            pixels[rowOffset + col] = Palette.BACKGROUND;
                        }
                        boolean last = false;
                        do {
                            int lenLast = 0xFF & in.readByte();
                            last = (0xFF & (lenLast & 0x80)) != 0;
                            int len = 0x7f & lenLast;
                            int offset = 0xFF & in.readByte();
                            for (int col = offset; col < offset + len; col++) {
                                pixels[rowOffset + col] = Palette.COLOUR[0xFF & in.readByte()];
                            }
                        }
                        while (!last);
                    }
                }
                else {
                    //just copy it
                }

                sprites.add(new RawSprite(header, pixels));
            }
        } catch (IOException ioe) {
            throw new RuntimeException("Could not parse sprite", ioe);
        }
    }

    public RawSprite get(int index) {
        return sprites.get(index);
    }

    public List<RawSprite> getList() {
        return sprites;
    }

    public class SpriteHeader {
        private long offset;
        private int width;
        private int height;
        private EnumSet<SpriteFlag> flags;
        private int xOffset;
        private int yOffset;

        public SpriteHeader(DatFileInputStream in) throws IOException {
            offset = in.readUnsignedInt();
            width = in.readUShort();
            height = in.readUShort();
            xOffset = in.readSShort();
            yOffset = in.readSShort();
            flags = in.readBitField(4, SpriteFlag.class);
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
    }

    public class RawSprite {
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
        unknown5;
    }

}
