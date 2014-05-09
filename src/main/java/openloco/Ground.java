package openloco;

import openloco.datfiles.*;

import java.util.EnumSet;

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

    public static Ground load(String name, DatFileInputStream dataInputStream) {
        GroundVars groundVars = new GroundVars(dataInputStream);
        MultiLangString desc = new MultiLangString(dataInputStream);
        UseObject cliff = new UseObject(dataInputStream, EnumSet.of(ObjectClass.CLIFF_FACES));
        Sprites sprites = new Sprites(dataInputStream);
        return new Ground(name, groundVars, desc, cliff, sprites);
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
