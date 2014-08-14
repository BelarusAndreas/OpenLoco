package openloco.assets;

public class VehicleUnit {

    private final byte length;
    private final byte rearBogeyPosition;
    private final byte frontBogeyIndex;
    private final byte rearBogeyIndex;
    private final byte spriteDetailsIndex;
    private final byte effectPosition;

    public VehicleUnit(byte length, byte rearBogeyPosition, byte frontBogeyIndex, byte rearBogeyIndex, byte spriteDetailsIndex, byte effectPosition) {
        this.length = length;
        this.rearBogeyPosition = rearBogeyPosition;
        this.frontBogeyIndex = frontBogeyIndex;
        this.rearBogeyIndex = rearBogeyIndex;
        this.spriteDetailsIndex = spriteDetailsIndex;
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

    public byte getSpriteDetailsIndex() {
        return spriteDetailsIndex;
    }

    public byte getEffectPosition() {
        return effectPosition;
    }

}
