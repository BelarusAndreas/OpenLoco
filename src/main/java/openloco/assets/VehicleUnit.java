package openloco.assets;

public class VehicleUnit {

    private final byte length;
    private final byte rearBogeyPosition;
    private final byte frontBogeyIndex;
    private final byte rearBogeyIndex;
    private final boolean isSpriteDetailsReversed;
    private final boolean isSpacingOnly;
    private final int spriteDetailsIndex;
    private final byte effectPosition;

    public VehicleUnit(byte length, byte rearBogeyPosition, byte frontBogeyIndex, byte rearBogeyIndex, byte spriteDetailsIndex, byte effectPosition) {
        this.length = length;
        this.rearBogeyPosition = rearBogeyPosition;
        this.frontBogeyIndex = frontBogeyIndex;
        this.rearBogeyIndex = rearBogeyIndex;
        if (spriteDetailsIndex < 0) {
            if (spriteDetailsIndex > -5) {
                this.spriteDetailsIndex = -(spriteDetailsIndex+1);
                isSpriteDetailsReversed = false;
                isSpacingOnly = true;
            }
            else {
                this.spriteDetailsIndex = spriteDetailsIndex + 128;
                isSpriteDetailsReversed = true;
                isSpacingOnly = false;
            }
        }
        else {
            this.spriteDetailsIndex = spriteDetailsIndex;
            isSpriteDetailsReversed = false;
            isSpacingOnly = false;
        }
        this.effectPosition = effectPosition;
    }

    public byte getLength() {
        return length;
    }

    public byte getRearBogeyPosition() {
        return rearBogeyPosition;
    }

    public byte getFrontBogeyIndex() {
        return frontBogeyIndex;
    }

    public byte getRearBogeyIndex() {
        return rearBogeyIndex;
    }

    public int getSpriteDetailsIndex() {
        return spriteDetailsIndex;
    }

    public boolean isSpriteDetailsReversed() {
        return isSpriteDetailsReversed;
    }

    public boolean isSpacingOnly() {
        return isSpacingOnly;
    }

    public byte getEffectPosition() {
        return effectPosition;
    }

    public boolean hasFrontBogey() {
        return frontBogeyIndex != -1;
    }

    public boolean hasRearBogey() {
        return rearBogeyIndex != -1;
    }
}
