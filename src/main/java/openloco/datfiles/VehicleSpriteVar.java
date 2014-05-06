package openloco.datfiles;

import java.io.IOException;
import java.util.EnumSet;

public class VehicleSpriteVar {

    private final byte numDir;
    private final byte vehType;
    private final byte numUnits;
    private final byte bogeyPos;
    private final EnumSet<VehicleSpriteFlag> flags;
    private final byte spriteNum;

    public VehicleSpriteVar(DatFileInputStream in) {
        try {
            numDir = in.readByte();
            in.skipBytes(2);
            vehType = in.readByte();
            numUnits = in.readByte();
            in.skipBytes(1);
            bogeyPos = in.readByte();
            flags = in.readBitField(1, VehicleSpriteFlag.class);
            in.skipBytes(6);
            spriteNum = in.readByte();
            in.skipBytes(15);
        }
        catch (IOException ioe) {
            throw new RuntimeException("Unable to parse vehicle sprite vars", ioe);
        }
    }

    public byte getNumDir() {
        return numDir;
    }

    public byte getVehType() {
        return vehType;
    }

    public byte getNumUnits() {
        return numUnits;
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

    public static enum VehicleSpriteFlag {
        HAS_SPRITES,
        unknown1,
        unknown2,
        unknown3,
        unknown4,
        REVERSED
    }
}
