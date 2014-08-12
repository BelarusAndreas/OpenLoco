package openloco.assets;

public class BuildingAsset {

    private final long[] spriteOffsets;
    private final long[] spriteHeights;

    public BuildingAsset(long[] spriteOffsets, long[] industrySpriteHeights) {
        this.spriteOffsets = spriteOffsets;
        spriteHeights = new long[spriteOffsets.length];
        for (int i=0; i<spriteOffsets.length; i++) {
            spriteHeights[i] = industrySpriteHeights[(int)spriteOffsets[i]];
        }
    }

    public int getSpriteCount() {
        return spriteOffsets.length;
    }

    public long[] getSpriteOffsets() {
        return spriteOffsets;
    }

    public int getSpriteOffset(int i) {
        return (int) spriteOffsets[i];
    }

    public long[] getSpriteHeights() {
        return spriteHeights;
    }

    public int getSpriteHeight(int i) {
        return (int) spriteHeights[i];
    }
}
