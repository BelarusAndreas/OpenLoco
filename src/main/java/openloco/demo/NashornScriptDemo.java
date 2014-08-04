package openloco.demo;

import com.sun.nio.file.SensitivityWatchEventModifier;
import openloco.Assets;
import openloco.graphics.IsoUtil;
import openloco.graphics.SpriteInstance;
import openloco.graphics.Tile;
import org.lwjgl.input.Keyboard;

import javax.script.*;
import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class NashornScriptDemo extends BaseDemo {

    private Assets assets;
    private List<SpriteInstance> spriteInstances;
    private WatchService watchService;

    private int width = 36;
    private int height = 36;

    public NashornScriptDemo(Assets assets) throws IOException {
        String initScript = System.getProperty("openloco.initScript");

        if (initScript == null) {
            System.out.println("\nPlease specify path to init script to run as -Dopenloco.initScript=path/to/initScript.js\n");
            System.exit(0);
        }

        this.assets = assets;
        watchService = FileSystems.getDefault().newWatchService();
        Path path = Paths.get(initScript).getParent();
        path.register(watchService, new WatchEvent.Kind[]{StandardWatchEventKinds.ENTRY_MODIFY}, SensitivityWatchEventModifier.HIGH);
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
            JOptionPane.showMessageDialog(null, "ScriptException whilst running script:\n\n" + e.getMessage(), "ScriptException", JOptionPane.ERROR_MESSAGE);
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

        WatchKey watchKey = watchService.poll();
        if (watchKey != null) {
            boolean changed = false;
            for (WatchEvent<?> event: watchKey.pollEvents()) {
                changed = true;
            }
            if (changed) {
                init();
            }
            watchKey.reset();
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

    @Override
    protected void finalize() throws Throwable {
        watchService.close();
        super.finalize();
    }
}
