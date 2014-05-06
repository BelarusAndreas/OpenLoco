package openloco.datfiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class VehicleVars {

    public enum VehicleFlag {
        unknown1, unknown2, unknown3, unknown4, unknown5, unknown6,
        RACKRAIL,
        unknown7, unknown8,
        ANYTRACK,
        unknown9,
        CANCOUPLE,
        DUALHEAD,
        unknown10,
        REFITTABLE,
        NOANNOUNCE
    }

    private byte vehicleClass;
    private byte vehicleType;
    private byte numMods;
    private byte costInd;
    private short costFact;
    private byte reliability;
    private byte runCostInd;
    private short runCostFact;
    private byte colourType;
    private byte numCompat;
    private List<VehicleSpriteVar> vehSprites;
    private int power;
    private int speed;
    private int rackSpeed;
    private int weight;
    private EnumSet<VehicleFlag> vehicleFlags;
    private byte visFxHeight;
    private byte visFxType;
    private byte wakeFxType;
    private int designed;
    private int obsolete;
    private byte startsndtype;
    private byte numSnd;

    public VehicleVars(DatFileInputStream in) {
        try {
            in.skipBytes(2);
            vehicleClass = in.readByte();
            vehicleType = in.readByte();
            in.skipBytes(2);
            numMods = in.readByte();
            costInd = in.readByte();
            costFact = in.readSShort();
            reliability = in.readByte();
            runCostInd = in.readByte();
            runCostFact = in.readSShort();
            colourType = in.readByte();
            numCompat = in.readByte();
            in.skipBytes(20);
            in.skipBytes(24); //vehunk
            vehSprites = new ArrayList<>();
            for (int i=0; i<4; i++) {
                vehSprites.add(new VehicleSpriteVar(in));
            }
            in.skipBytes(36);
            power = in.readUShort();
            speed = in.readUShort();
            rackSpeed = in.readUShort();
            weight = in.readUShort();
            vehicleFlags = in.readBitField(2, VehicleFlag.class);
            in.skipBytes(44);
            visFxHeight = in.readByte();
            visFxType = in.readByte();
            in.skipBytes(2);
            wakeFxType = in.readByte();
            in.skipBytes(1);
            designed = in.readUShort();
            obsolete = in.readUShort();
            in.skipBytes(1);
            startsndtype = in.readByte();
            in.skipBytes(64);
            numSnd = in.readByte();
            in.skipBytes(3);
        }
        catch (IOException e) {
            throw new RuntimeException("Unable to parse vehicle vars", e);
        }
    }

    public byte getVehicleClass() {
        return vehicleClass;
    }

    public byte getVehicleType() {
        return vehicleType;
    }

    public byte getNumMods() {
        return numMods;
    }

    public byte getCostInd() {
        return costInd;
    }

    public short getCostFact() {
        return costFact;
    }

    public byte getReliability() {
        return reliability;
    }

    public byte getRunCostInd() {
        return runCostInd;
    }

    public short getRunCostFact() {
        return runCostFact;
    }

    public byte getColourType() {
        return colourType;
    }

    public byte getNumCompat() {
        return numCompat;
    }

    public List<VehicleSpriteVar> getVehSprites() {
        return vehSprites;
    }

    public int getPower() {
        return power;
    }

    public int getSpeed() {
        return speed;
    }

    public int getRackSpeed() {
        return rackSpeed;
    }

    public int getWeight() {
        return weight;
    }

    public EnumSet<VehicleFlag> getVehicleFlags() {
        return vehicleFlags;
    }

    public byte getVisFxHeight() {
        return visFxHeight;
    }

    public byte getVisFxType() {
        return visFxType;
    }

    public byte getWakeFxType() {
        return wakeFxType;
    }

    public int getDesigned() {
        return designed;
    }

    public int getObsolete() {
        return obsolete;
    }

    public byte getStartsndtype() {
        return startsndtype;
    }

    public byte getNumSnd() {
        return numSnd;
    }
}
