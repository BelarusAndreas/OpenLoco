package openloco.entities;

public class Bridge {
    private final String name;
    private final BridgeVars vars;
    private final MultiLangString multiLangString;
    private final Sprites sprites;

    public Bridge(String name, BridgeVars vars, MultiLangString multiLangString, Sprites sprites) {
        this.name = name;
        this.vars = vars;
        this.multiLangString = multiLangString;
        this.sprites = sprites;
    }

    public String getName() {
        return name;
    }

    public BridgeVars getVars() {
        return vars;
    }

    public MultiLangString getMultiLangString() {
        return multiLangString;
    }

    public Sprites getSprites() {
        return sprites;
    }
}
