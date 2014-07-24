package openloco.industry;

import openloco.Assets;
import openloco.entities.Building;
import openloco.entities.Industry;
import openloco.entities.Sprites;
import openloco.graphics.*;

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
            spriteInstances.addAll(render(buildingInstance, industry));
        }

        return spriteInstances;
    }

    private List<SpriteInstance> render(BuildingInstance buildingInstance, Industry industry) {
        ArrayList<SpriteInstance> spriteInstances = new ArrayList<>();
        Building building = industry.getBuilding(buildingInstance.getType());
        int[] p = {0, 0, 0};

        for (int i=0; i<building.getSpriteCount(); i++) {
            int spriteOffset = 4*building.getSpriteOffset(i);
            Sprites.RawSprite sprite = industry.getSprites().get(spriteOffset);
            OpenGlSprite glSprite = OpenGlSprite.createFromRawSprite(sprite);

            p = new int[]{buildingInstance.getX(), buildingInstance.getY(), p[2]};
            int screenX = Math.round(IsoUtil.isoX(p[0], p[1], p[2]));
            int screenY = Math.round(IsoUtil.isoY(p[0], p[1], p[2]));
            p[2] = building.getSpriteHeight(i);

            spriteInstances.add(new SpriteInstance(glSprite, screenX, screenY, SpriteLayer.BUILDING, p[0], p[1], p[2]));
        }

        return spriteInstances;
    }
}
