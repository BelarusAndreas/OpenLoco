package openloco.entities;

public class TreeVars {
    private final int height;
    private final int costInd;
    private final int buildCostFact;
    private final int clearCostFact;

    public TreeVars(int height, int costInd, int buildCostFact, int clearCostFact) {
        this.height = height;
        this.costInd = costInd;
        this.buildCostFact = buildCostFact;
        this.clearCostFact = clearCostFact;
    }

    public int getHeight() {
        return height;
    }

    public int getCostInd() {
        return costInd;
    }

    public int getBuildCostFact() {
        return buildCostFact;
    }

    public int getClearCostFact() {
        return clearCostFact;
    }
}
