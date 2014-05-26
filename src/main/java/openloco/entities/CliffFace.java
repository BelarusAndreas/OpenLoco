package openloco.entities;

public class CliffFace {
    private final String name;
    private final MultiLangString description;
    private final Sprites sprites;

    public CliffFace(String name, MultiLangString description, Sprites sprites) {
        this.name = name;
        this.description = description;
        this.sprites = sprites;
    }

    public String getName() {
        return name;
    }

    public MultiLangString getDescription() {
        return description;
    }

    public Sprites getSprites() {
        return sprites;
    }
}
