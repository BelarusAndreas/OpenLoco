package openloco.assets;

import java.util.EnumSet;

public class VehicleSpriteVar {

    private final byte levelSpriteCount;
    private final byte upDownSpriteCount;
    private final byte frames;
    private final byte vehType;
    private final byte numUnits;
    private final byte tiltCount;
    private final byte bogeyPos;
    private final EnumSet<VehicleSpriteFlag> flags;
    private final byte spriteNum;

    public VehicleSpriteVar(byte levelSpriteCount, byte upDownSpriteCount, byte frames, byte vehType, byte numUnits,
                            byte tiltCount, byte bogeyPos, EnumSet<VehicleSpriteFlag> flags, byte spriteNum) {
        this.levelSpriteCount = levelSpriteCount;
        this.upDownSpriteCount = upDownSpriteCount;
        this.frames = frames;
        this.vehType = vehType;
        this.numUnits = numUnits;
        this.tiltCount = tiltCount;
        this.bogeyPos = bogeyPos;
        this.flags = flags;
        this.spriteNum = spriteNum;
    }

    public byte getLevelSpriteCount() {
        return levelSpriteCount;
    }

    public byte getUpDownSpriteCount() {
        return upDownSpriteCount;
    }

    public byte getFrames() {
        return frames;
    }

    public byte getVehType() {
        return vehType;
    }

    public byte getNumUnits() {
        return numUnits;
    }

    public byte getTiltCount() {
        return tiltCount;
    }

    public byte getBogeyPos() {
        return bogeyPos;
    }

    public EnumSet<VehicleSpriteFlag> getFlags() {
        return flags;
    }

    public byte getSpriteNum() {
        return spriteNum;
    }

    public boolean isSymmetrical() {
        return flags.contains(VehicleSpriteFlag.IS_SYMMETRICAL);
    }

    public static enum VehicleSpriteFlag {
        HAS_SPRITES,
        IS_SYMMETRICAL,
        unknown2,
        unknown3,
        unknown4,
        REVERSED
    }
}
