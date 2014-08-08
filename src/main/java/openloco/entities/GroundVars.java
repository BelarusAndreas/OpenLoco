package openloco.entities;

public class GroundVars {

    private final byte costInd;
    private final byte numberOfVariations;
    private final short costFactor;

    public GroundVars(byte costInd, byte numberOfVariations, short costFactor) {
        this.costInd = costInd;
        this.numberOfVariations = numberOfVariations;
        this.costFactor = costFactor;
    }

    public byte getCostInd() {
        return costInd;
    }

    public byte getNumberOfVariations() {
        return numberOfVariations;
    }

    public short getCostFactor() {
        return costFactor;
    }
}
