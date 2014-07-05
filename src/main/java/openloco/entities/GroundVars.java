package openloco.entities;

public class GroundVars {

    private final byte costInd;
    private final short costFactor;

    public GroundVars(byte costInd, short costFactor) {
        this.costInd = costInd;
        this.costFactor = costFactor;
    }

    public byte getCostInd() {
        return costInd;
    }

    public short getCostFactor() {
        return costFactor;
    }
}
