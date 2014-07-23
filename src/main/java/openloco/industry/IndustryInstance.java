package openloco.industry;

import java.util.List;

public class IndustryInstance {

    private final String type;
    private final List<BuildingInstance> buildings;

    public IndustryInstance(String type, List<BuildingInstance> buildings) {
        this.type = type;
        this.buildings = buildings;
    }

    public String getType() {
        return type;
    }

    public List<BuildingInstance> getBuildings() {
        return buildings;
    }
}
