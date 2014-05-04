package openloco;

import openloco.datfiles.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class LocoTool {

    private static final Logger logger = LoggerFactory.getLogger(LocoTool.class);

    public static void main(String[] args) throws IOException {
        final String DATA_DIR = args[0];
        Assets assets = new Assets();
        new DatFileLoader(assets, DATA_DIR).loadFiles();
    }

}
