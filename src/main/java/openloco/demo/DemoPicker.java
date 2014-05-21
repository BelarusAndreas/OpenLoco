package openloco.demo;

import openloco.Assets;
import openloco.datfiles.DatFileLoader;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class DemoPicker {

    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        final String DATA_DIR = System.getProperty("openloco.dataDir");
        Assets assets = new Assets();
        new DatFileLoader(assets, DATA_DIR).loadFiles();

        Class<? extends BaseDemo> klass = (Class<? extends BaseDemo>) Class.forName("openloco.demo.LoadSprite");

        BaseDemo demo = klass.getConstructor(Assets.class).newInstance(assets);
        demo.run();
    }

}
