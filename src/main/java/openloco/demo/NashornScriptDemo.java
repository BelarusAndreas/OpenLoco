package openloco.demo;

import openloco.Assets;
import openloco.graphics.IsoUtil;
import openloco.graphics.SpriteInstance;
import openloco.graphics.Tile;
import org.lwjgl.input.Keyboard;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class NashornScriptDemo extends BaseDemo {

    private Assets assets;
    private List<SpriteInstance> spriteInstances;

    private int width = 36;
    private int height = 36;

    public NashornScriptDemo(Assets assets) {
        this.assets = assets;
    }

    @Override
    protected void init() {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        try {
            Bindings bindings = engine.createBindings();
            bindings.put("assets", assets);
            this.spriteInstances = new ArrayList<>();
            bindings.put("spriteInstances", spriteInstances);
            bindings.put("width", width);
            bindings.put("height", height);

            String initScript = System.getProperty("openloco.initScript");
            engine.eval(new FileReader(initScript), bindings);
            engine.eval("init();", bindings);
            System.out.println("Called init");
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void update() {
        while (Keyboard.next()) {
            if (Keyboard.getEventKey() == Keyboard.KEY_R
                    && Keyboard.getEventKeyState()) {
                init();
            }
        }
    }

    @Override
    protected List<SpriteInstance> getSprites() {
        return spriteInstances;
    }

    @Override
    protected float getXOffset() {
        return -IsoUtil.isoX(Tile.WIDTH * width / 2, Tile.WIDTH * height / 2, 0);
    }

    @Override
    protected float getYOffset() {
        return -IsoUtil.isoY(Tile.WIDTH * width / 2, Tile.WIDTH * height / 2, 0);
    }
}
