package openloco.entities;

public class Industry {
    private String name;
    private final IndustryVars industryVars;
    private final MultiLangString templatedName;
    private final MultiLangString description;
    private final MultiLangString prefixDescription;
    private final MultiLangString closingDownMessage;
    private final MultiLangString productionUpMessage;
    private final MultiLangString productionDownMessage;
    private final MultiLangString singular;
    private final MultiLangString plural;
    private final long[] aux0;
    private final long[] aux1;

    public Industry(String name, IndustryVars industryVars, MultiLangString description, MultiLangString templatedName, MultiLangString prefixDescription, MultiLangString closingDownMessage, MultiLangString productionUpMessage, MultiLangString productionDownMessage, MultiLangString singular, MultiLangString plural, long[] aux0, long[] aux1) {
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
        this.aux0 = aux0;
        this.aux1 = aux1;
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
}
