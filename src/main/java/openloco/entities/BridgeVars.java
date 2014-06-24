package openloco.entities;

public class BridgeVars {
    private final int noRoof;
    private final int spanLength;
    private final int pillarSpacing;
    private final int maxSpeed;
    private final int maxHeight;
    private final int costInd;
    private final int baseCostFactor;
    private final int heightCostFactor;
    private final int sellCostFactor;
    private final int disabledTrackCfg;

    public BridgeVars(int noRoof, int spanLength, int pillarSpacing, int maxSpeed, int maxHeight, int costInd, int baseCostFactor, int heightCostFactor, int sellCostFactor, int disabledTrackCfg) {
        this.noRoof = noRoof;
        this.spanLength = spanLength;
        this.pillarSpacing = pillarSpacing;
        this.maxSpeed = maxSpeed;
        this.maxHeight = maxHeight;
        this.costInd = costInd;
        this.baseCostFactor = baseCostFactor;
        this.heightCostFactor = heightCostFactor;
        this.sellCostFactor = sellCostFactor;
        this.disabledTrackCfg = disabledTrackCfg;
    }

    public int getNoRoof() {
        return noRoof;
    }

    public int getSpanLength() {
        return spanLength;
    }

    public int getPillarSpacing() {
        return pillarSpacing;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public int getCostInd() {
        return costInd;
    }

    public int getBaseCostFactor() {
        return baseCostFactor;
    }

    public int getHeightCostFactor() {
        return heightCostFactor;
    }

    public int getSellCostFactor() {
        return sellCostFactor;
    }

    public int getDisabledTrackCfg() {
        return disabledTrackCfg;
    }
}
