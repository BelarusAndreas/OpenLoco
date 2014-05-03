package openloco;

import openloco.datfiles.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class LocoTool {

    private static final Logger logger = LoggerFactory.getLogger(LocoTool.class);

    public static void main(String[] args) throws IOException {
        final String DATA_DIR = args[0];
        Assets assets = new Assets();
        DatFileLoader loader = new DatFileLoader(assets, DATA_DIR);
        loader.loadFiles();
    }

}
