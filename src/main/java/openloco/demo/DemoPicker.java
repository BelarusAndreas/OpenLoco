package openloco.demo;

import openloco.Assets;
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
        return Arrays.asList(LoadSprite.class, TerrainDemo.class, TrackDemo.class, NashornScriptDemo.class)
                .stream()
                .collect(Collectors.toMap(Class::getSimpleName, Function.identity()));
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        final String DATA_DIR = System.getProperty("openloco.dataDir");
        Assets assets = new Assets();
        new DatFileLoader(assets, DATA_DIR).loadFiles();

        if (args.length > 0) {
            Class<? extends BaseDemo> klass = demoClasses.get(args[0]);
            BaseDemo demo = klass.getConstructor(Assets.class).newInstance(assets);
            demo.run();
        }
    }

}
