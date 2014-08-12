package openloco.assets;

public class Tree {
    private final String name;
    private final TreeVars treeVars;
    private final MultiLangString desc;
    private final Sprites sprites;

    public Tree(String name, TreeVars treeVars, MultiLangString desc, Sprites sprites) {
        this.name = name;
        this.treeVars = treeVars;
        this.desc = desc;
        this.sprites = sprites;
    }

    public String getName() {
        return name;
    }

    public TreeVars getTreeVars() {
        return treeVars;
    }

    public MultiLangString getDesc() {
        return desc;
    }

    public Sprites getSprites() {
        return sprites;
    }
}
