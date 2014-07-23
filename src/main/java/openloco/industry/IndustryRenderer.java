package openloco.industry;

import openloco.Assets;
import openloco.entities.Industry;
import openloco.graphics.Renderer;
import openloco.graphics.SpriteInstance;

import java.util.ArrayList;
import java.util.List;

public class IndustryRenderer implements Renderer<IndustryInstance> {

    private final Assets assets;

    public IndustryRenderer(Assets assets) {
        this.assets = assets;
    }

    @Override
    public List<SpriteInstance> render(IndustryInstance industryInstance) {
        Industry industry = assets.getIndustry(industryInstance.getType());
        List<SpriteInstance> spriteInstances = new ArrayList<>();

        for (BuildingInstance buildingInstance: industryInstance.getBuildings()) {
            spriteInstances.addAll(render(buildingInstance));
        }

        return spriteInstances;
    }

    private List<SpriteInstance> render(BuildingInstance buildingInstance) {
        return new ArrayList<>();
    }
}
