package openloco.entities;

import java.util.EnumSet;

public class IndustryVars {

    public static enum IndustryFlag {
        unknown1, unknown2, unknown3, unknown4,
        unknown5, unknown6, unknown7, unknown8,
        unknown9, unknown10, unknown11, unknown12,
        unknown13, unknown14, unknown15, unknown16,
        unknown17,
        needall,
        canincreasproduction,
        candecreaseproduction
    }

    private final byte numSprites;
    private final byte numBuildingTypes;
    private final byte numBuildingInstances;
    private final int firstYear;
    private final int lastYear;
    private final byte costInd;
    private final int costFactor;
    private final EnumSet<IndustryFlag> industryFlags;

    public IndustryVars(byte numSprites, byte numBuildingTypes, byte numBuildingInstances, int firstYear, int lastYear, byte costInd, int costFactor, EnumSet<IndustryFlag> industryFlags) {

        this.numSprites = numSprites;
        this.numBuildingTypes = numBuildingTypes;
        this.numBuildingInstances = numBuildingInstances;
        this.firstYear = firstYear;
        this.lastYear = lastYear;
        this.costInd = costInd;
        this.costFactor = costFactor;
        this.industryFlags = industryFlags;
    }

    public byte getNumSprites() {
        return numSprites;
    }

    public byte getNumBuildingTypes() {
        return numBuildingTypes;
    }

    public byte getNumBuildingInstances() {
        return numBuildingInstances;
    }

    public int getFirstYear() {
        return firstYear;
    }

    public int getLastYear() {
        return lastYear;
    }

    public byte getCostInd() {
        return costInd;
    }

    public int getCostFactor() {
        return costFactor;
    }

    public EnumSet<IndustryFlag> getIndustryFlags() {
        return industryFlags;
    }
}
