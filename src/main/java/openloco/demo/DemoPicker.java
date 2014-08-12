package openloco.demo;

import openloco.assets.Assets;
import openloco.app.LwjglNativeLoader;
import openloco.datfiles.DatFileLoader;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DemoPicker {

    private static final Map<String, Class<? extends BaseDemo>> demoClasses = mapifyClasses();

    private static Map<String, Class<? extends BaseDemo>> mapifyClasses() {
        return Arrays.asList(LoadSpriteDemo.class, TerrainDemo.class, TrackDemo.class, NashornScriptDemo.class)
                .stream()
                .collect(Collectors.toMap(Class::getSimpleName, Function.identity()));
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        final String DATA_DIR = System.getProperty("openloco.dataDir");

        if (DATA_DIR == null) {
            printUsage();
            System.out.println("\nPlease specify openloco.dataDir.\n");
            System.exit(0);
        }


        new LwjglNativeLoader().loadLibraries();

        Assets assets = new Assets();
        new DatFileLoader(assets, DATA_DIR).loadFiles();

        if (args.length > 0) {
            Class<? extends BaseDemo> klass = demoClasses.get(args[0]);
            BaseDemo demo = klass.getConstructor(Assets.class).newInstance(assets);
            demo.run();
        }
    }

    private static void printUsage() {
        System.out.println();
        System.out.println("Usage: java -Dopenloco.dataDir=path/to/locomotion/ObjData -jar openloco.jar demoToRun");
    }

}
