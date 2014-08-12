package openloco.assets;

public class Fence {

    private final String name;
    private final Void vars;
    private final MultiLangString description;
    private final Sprites sprites;

    public Fence(String name, Void vars, MultiLangString description, Sprites sprites) {
        this.name = name;
        this.vars = vars;
        this.description = description;
        this.sprites = sprites;
    }

    public String getName() {
        return name;
    }

    public Void getVars() {
        return vars;
    }

    public MultiLangString getDescription() {
        return description;
    }

    public Sprites getSprites() {
        return sprites;
    }
}
