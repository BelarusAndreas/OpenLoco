package openloco.assets;

public class InterfaceStyle {

    private final String name;
    private final MultiLangString styleName;
    private final Sprites sprites;

    public InterfaceStyle(String name, MultiLangString styleName, Sprites sprites) {
        this.name = name;
        this.styleName = styleName;
        this.sprites = sprites;
    }

    public String getName() {
        return name;
    }

    public MultiLangString getStyleName() {
        return styleName;
    }

    public Sprites getSprites() {
        return sprites;
    }
}
