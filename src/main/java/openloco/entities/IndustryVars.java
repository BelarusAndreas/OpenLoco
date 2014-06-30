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

    private final byte numAux01;
    private final byte numAux4Ent;
    private final byte numAux5;
    private final int firstYear;
    private final int lastYear;
    private final byte costInd;
    private final int costFactor;
    private final EnumSet<IndustryFlag> industryFlags;

    public IndustryVars(byte numAux01, byte numAux4Ent, byte numAux5, int firstYear, int lastYear, byte costInd, int costFactor, EnumSet<IndustryFlag> industryFlags) {

        this.numAux01 = numAux01;
        this.numAux4Ent = numAux4Ent;
        this.numAux5 = numAux5;
        this.firstYear = firstYear;
        this.lastYear = lastYear;
        this.costInd = costInd;
        this.costFactor = costFactor;
        this.industryFlags = industryFlags;
    }

    public byte getNumAux01() {
        return numAux01;
    }

    public byte getNumAux4Ent() {
        return numAux4Ent;
    }

    public byte getNumAux5() {
        return numAux5;
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
