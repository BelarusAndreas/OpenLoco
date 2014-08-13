package openloco.assets;

public class VehicleHunk {

    private final byte length;
    private final byte vehunk2;
    private final byte vehunk3;
    private final byte vehunk4;
    private final byte spriteIndex;
    private final byte vehunk6;

    public VehicleHunk(byte length, byte vehunk2, byte vehunk3, byte vehunk4, byte spriteIndex, byte vehunk6) {

        this.length = length;
        this.vehunk2 = vehunk2;
        this.vehunk3 = vehunk3;
        this.vehunk4 = vehunk4;
        this.spriteIndex = spriteIndex;
        this.vehunk6 = vehunk6;
    }

    public byte getLength() {
        return length;
    }

    public byte getVehunk2() {
        return vehunk2;
    }

    public byte getVehunk3() {
        return vehunk3;
    }

    public byte getVehunk4() {
        return vehunk4;
    }

    public byte getSpriteIndex() {
        return spriteIndex;
    }

    public byte getVehunk6() {
        return vehunk6;
    }
}
