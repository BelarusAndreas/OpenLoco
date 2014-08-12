package openloco.assets;

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

    public VehicleVars(byte vehicleClass, byte vehicleType, byte numMods, byte costInd, short costFact,
                       byte reliability, byte runCostInd, short runCostFact, byte colourType, byte numCompat,
                       List<VehicleSpriteVar> vehSprites, int power, int speed, int rackSpeed, int weight,
                       EnumSet<VehicleFlag> vehicleFlags, byte visFxHeight, byte visFxType, byte wakeFxType,
                       int designed, int obsolete, byte startsndtype, byte numSnd) {

        this.vehicleClass = vehicleClass;
        this.vehicleType = vehicleType;
        this.numMods = numMods;
        this.costInd = costInd;
        this.costFact = costFact;
        this.reliability = reliability;
        this.runCostInd = runCostInd;
        this.runCostFact = runCostFact;
        this.colourType = colourType;
        this.numCompat = numCompat;
        this.vehSprites = vehSprites;
        this.power = power;
        this.speed = speed;
        this.rackSpeed = rackSpeed;
        this.weight = weight;
        this.vehicleFlags = vehicleFlags;
        this.visFxHeight = visFxHeight;
        this.visFxType = visFxType;
        this.wakeFxType = wakeFxType;
        this.designed = designed;
        this.obsolete = obsolete;
        this.startsndtype = startsndtype;
        this.numSnd = numSnd;
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
