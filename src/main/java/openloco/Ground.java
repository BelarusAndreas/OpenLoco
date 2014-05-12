package openloco;

import openloco.datfiles.*;

public class Ground {
    private final String name;
    private final GroundVars groundVars;
    private final MultiLangString desc;
    private final UseObject cliff;
    private final Sprites sprites;

    public Ground(String name, GroundVars groundVars, MultiLangString desc, UseObject cliff, Sprites sprites) {
        this.name = name;
        this.groundVars = groundVars;
        this.desc = desc;
        this.cliff = cliff;
        this.sprites = sprites;
    }

    public String getName() {
        return name;
    }

    public MultiLangString getDesc() {
        return desc;
    }

    public GroundVars getGroundVars() {
        return groundVars;
    }

    public UseObject getCliff() {
        return cliff;
    }

    public Sprites getSprites() {
        return sprites;
    }
}
