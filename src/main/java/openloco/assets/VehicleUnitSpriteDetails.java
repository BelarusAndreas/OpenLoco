package openloco.assets;

import java.util.EnumSet;

public class VehicleUnitSpriteDetails {

    private final byte levelSpriteCount;
    private final byte upDownSpriteCount;
    private final byte frames;
    private final byte cargoLoadingStates; // number of frames between full and empty
    private final byte cargoLoadingFrames; // total number of cargo frames (empty to full for each cargo)
    private final byte tiltCount;
    private final byte bogeyPos;
    private final EnumSet<VehicleSpriteFlag> flags;
    private final byte spriteNum;

    public VehicleUnitSpriteDetails(byte levelSpriteCount, byte upDownSpriteCount, byte frames, byte cargoLoadingStates, byte cargoLoadingFrames,
                                    byte tiltCount, byte bogeyPos, EnumSet<VehicleSpriteFlag> flags, byte spriteNum) {
        this.levelSpriteCount = levelSpriteCount;
        this.upDownSpriteCount = upDownSpriteCount;
        this.frames = frames;
        this.cargoLoadingStates = cargoLoadingStates;
        this.cargoLoadingFrames = cargoLoadingFrames;
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

    public byte getCargoLoadingStates() {
        return cargoLoadingStates;
    }

    public byte getCargoLoadingFrames() {
        return cargoLoadingFrames;
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

    public boolean hasSprites() {
        return flags.contains(VehicleSpriteFlag.HAS_SPRITES);
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
