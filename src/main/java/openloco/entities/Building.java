package openloco.entities;

public class Building {

    private final long[] spriteOffsets;

    public Building(long[] spriteOffsets) {
        this.spriteOffsets = spriteOffsets;
    }

    public long[] getSpriteOffsets() {
        return spriteOffsets;
    }
}
