package openloco.assets;

import java.util.EnumSet;

public class VehicleBogeySpriteDetails {
    private final byte animFrames;
    private final EnumSet<VehicleUnitSpriteDetails.VehicleSpriteFlag> flags;

    public VehicleBogeySpriteDetails(byte animFrames, EnumSet<VehicleUnitSpriteDetails.VehicleSpriteFlag> flags) {
        this.animFrames = animFrames;
        this.flags = flags;
    }

    public byte getAnimFrames() {
        return animFrames;
    }

    public EnumSet<VehicleUnitSpriteDetails.VehicleSpriteFlag> getFlags() {
        return flags;
    }
}
