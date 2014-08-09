package openloco.demo;

import com.sun.nio.file.SensitivityWatchEventModifier;
import openloco.Assets;
import openloco.graphics.SpriteInstance;
import org.lwjgl.input.Keyboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.*;
import javax.swing.*;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class NashornScriptDemo extends BaseDemo {

    private Assets assets;
    private List<SpriteInstance> spriteInstances;
    private WatchService watchService;

    private static int WIDTH = 36;
    private static int HEIGHT = 36;

    private static final Logger LOGGER = LoggerFactory.getLogger(NashornScriptDemo.class);

    public NashornScriptDemo(Assets assets) throws IOException {
        super(WIDTH, HEIGHT);
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
            bindings.put("width", WIDTH);
            bindings.put("height", HEIGHT);

            String initScript = System.getProperty("openloco.initScript");
            engine.eval(new FileReader(initScript), bindings);
            engine.eval("init();", bindings);
        }
        catch (Exception e) {
            LOGGER.error("Caught exception whilst running init script", e);
            JOptionPane.showMessageDialog(null, "Exception whilst running script:\n\n" + e.getMessage(), "Script Exception", JOptionPane.ERROR_MESSAGE);
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
    protected void finalize() throws Throwable {
        watchService.close();
        super.finalize();
    }
}
