package openloco.entities;

import java.util.List;

public class IndustryAsset {
    private final String name;
    private final IndustryVars industryVars;
    private final MultiLangString templatedName;
    private final MultiLangString description;
    private final MultiLangString prefixDescription;
    private final MultiLangString closingDownMessage;
    private final MultiLangString productionUpMessage;
    private final MultiLangString productionDownMessage;
    private final MultiLangString singular;
    private final MultiLangString plural;
    private final long[] spriteHeights;
    private final long[] animationFlags;
    private final long[][] aux2;
    private final long[] aux3;
    private final long[][] buildingSprites;
    private final long[] buildingInstances;
    private final List<UseObject> produces;
    private final List<UseObject> accepts;
    private final List<UseObject> fences;
    private final Sprites sprites;

    public IndustryAsset(String name, IndustryVars industryVars, MultiLangString description, MultiLangString templatedName, MultiLangString prefixDescription, MultiLangString closingDownMessage, MultiLangString productionUpMessage, MultiLangString productionDownMessage, MultiLangString singular, MultiLangString plural, long[] spriteHeights, long[] animationFlags, long[][] aux2, long[] aux3, long[][] buildingSprites, long[] buildingInstances, List<UseObject> produces, List<UseObject> accepts, List<UseObject> fences, Sprites sprites) {
        this.name = name;
        this.industryVars = industryVars;
        this.description = description;
        this.templatedName = templatedName;
        this.prefixDescription = prefixDescription;
        this.closingDownMessage = closingDownMessage;
        this.productionUpMessage = productionUpMessage;
        this.productionDownMessage = productionDownMessage;
        this.singular = singular;
        this.plural = plural;
        this.spriteHeights = spriteHeights;
        this.animationFlags = animationFlags;
        this.aux2 = aux2;
        this.aux3 = aux3;
        this.buildingSprites = buildingSprites;
        this.buildingInstances = buildingInstances;
        this.produces = produces;
        this.accepts = accepts;
        this.fences = fences;
        this.sprites = sprites;
    }

    public String getName() {
        return name;
    }

    public IndustryVars getIndustryVars() {
        return industryVars;
    }

    public MultiLangString getTemplatedName() {
        return templatedName;
    }

    public MultiLangString getDescription() {
        return description;
    }

    public MultiLangString getPrefixDescription() {
        return prefixDescription;
    }

    public MultiLangString getClosingDownMessage() {
        return closingDownMessage;
    }

    public MultiLangString getProductionUpMessage() {
        return productionUpMessage;
    }

    public MultiLangString getProductionDownMessage() {
        return productionDownMessage;
    }

    public MultiLangString getSingular() {
        return singular;
    }

    public MultiLangString getPlural() {
        return plural;
    }

    public long[] getAnimationFlags() {
        return animationFlags;
    }

    public long[][] getAux2() {
        return aux2;
    }

    public long[] getAux3() {
        return aux3;
    }

    public long[] getBuildingInstances() {
        return buildingInstances;
    }

    public List<UseObject> getProduces() {
        return produces;
    }

    public List<UseObject> getAccepts() {
        return accepts;
    }

    public List<UseObject> getFences() {
        return fences;
    }

    public Sprites getSprites() {
        return sprites;
    }

    public int getBuildingCount() {
        return industryVars.getNumBuildingTypes();
    }

    public BuildingAsset getBuilding(int index) {
        return new BuildingAsset(buildingSprites[index], spriteHeights);
    }

}
