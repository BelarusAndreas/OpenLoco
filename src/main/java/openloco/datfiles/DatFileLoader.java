package openloco.datfiles;

import openloco.*;
import openloco.entities.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class DatFileLoader {

    private static final Logger logger = LoggerFactory.getLogger(DatFileLoader.class);

    private final Assets assets;
    private final String DATA_DIR;

    public DatFileLoader(Assets assets, String dataDir) {
        this.assets = assets;
        this.DATA_DIR = dataDir;
    }

    public void loadFiles() throws IOException {
        logger.info("Loading assets from {}...", DATA_DIR);
        Files.list(new File(DATA_DIR).toPath())
                .filter((p) -> p.toString().toLowerCase().endsWith(".dat"))
                .forEach(this::load);
        logger.info("Finished loading assets.");
    }

    private void load(Path path) {
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(path);
        }
        catch (IOException ioe) {
            logger.error("Failed to load file: {}", path);
            return;
        }

        assert bytes[3] == 0x11;

        ObjectClass objectClass = ObjectClass.values()[(bytes[0] & 0x7f)];
        long objectSubClass = DatFileUtil.readUintLE(bytes, 1, 3);
        String name = new String(bytes, 4, 8, Charset.defaultCharset());

        int pointer = 16;

        while (pointer < bytes.length) {
            byte chunkType = bytes[pointer++];
            long length = DatFileUtil.readUint32LE(bytes, pointer);
            pointer += 4;

            byte[] chunk;
            if (chunkType == 0) {
                //no encoding
                chunk = bytes;
            }
            else if (chunkType == 1) {
                chunk = DatFileUtil.rleDecode(bytes, pointer, length);
            }
            else if (chunkType == 2) {
                //decompress doesn't seem to work...
                //chunk = rleDecode(bytes, pointer, length);
                //chunk = decompress(chunk);
                break;
            }
            else if (chunkType == 3) {
                chunk = DatFileUtil.descramble(bytes, pointer, length);
            }
            else {
                logger.error("Unsupported chunk type for {} ({}): {} ", path, objectClass, chunkType);
                break;
            }

            decodeObject(name, chunk, objectClass);
            pointer += length;
        }
    }

    private void decodeObject(String name, byte[] chunk, ObjectClass objectClass) {
        chunk = ByteBuffer.wrap(chunk).order(ByteOrder.LITTLE_ENDIAN).array();
        DatFileInputStream dataInputStream = new DatFileInputStream(new ByteArrayInputStream(chunk));
        try {
            switch (objectClass) {

                case INTERFACES:
                    InterfaceStyle interfaceStyle = loadInterfaceStyle(name, dataInputStream);
                    assets.add(interfaceStyle);
                    break;

                case COMPANIES:
                    Company company = loadCompany(name, dataInputStream);
                    assets.add(company);
                    break;

                case GROUND:
                    Ground ground = loadGround(name, dataInputStream);
                    assets.add(ground);
                    break;

                case VEHICLES:
                    Vehicle v = loadVehicle(name, dataInputStream);
                    assets.add(v);
                    break;

                case CLIFF_FACES:
                    CliffFace cf = loadCliffFace(name, dataInputStream);
                    assets.add(cf);
                    break;

                case TRACKS:
                    Track track = loadTrack(name, dataInputStream);
                    assets.add(track);
                    break;

                case TREES:
                    Tree tree = loadTree(name, dataInputStream);
                    assets.add(tree);
                    break;

                case BRIDGES:
                    Bridge bridge = loadBridge(name, dataInputStream);
                    assets.add(bridge);
                    break;

                case INDUSTRIES:
                    Industry industry = loadIndustry(name, dataInputStream);
                    assets.add(industry);
                    break;

                default:
                    break;
            }
        }
        catch (IOException ioe) {
            logger.error("IOException whilst loading object of class {}: ", objectClass, ioe);
        }
    }

    private static Vehicle loadVehicle(String name, DatFileInputStream inputStream) throws IOException {
        VehicleVars vars = loadVehicleVars(inputStream);
        MultiLangString description = inputStream.readMultiLangString();
        UseObject trackType = null;
        if (vars.getVehicleClass() < 2 && !vars.getVehicleFlags().contains(VehicleVars.VehicleFlag.ANYTRACK)) {
            trackType = inputStream.readUseObject(EnumSet.of(ObjectClass.TRACKS, ObjectClass.ROADS));
        }
        //optional reference to track/road modification?
        List<UseObject> trackModifications = inputStream.readUseObjectList(vars.getNumMods(), ObjectClass.TRACK_MODIFICATIONS, ObjectClass.ROAD_MODIFICATIONS);

        List<CargoCapacity> cargoCapacities = new ArrayList<>();
        for (int i=0; i<2; i++) {
            cargoCapacities.add(loadCargoCapacity(inputStream));
        }

        UseObject visualFx = null;
        if (vars.getVisFxType() != 0) {
            visualFx = inputStream.readUseObject(EnumSet.of(ObjectClass.EXHAUST_EFFECTS));
        }

        UseObject wakeFx = null;
        if (vars.getWakeFxType() != 0) {
            wakeFx = inputStream.readUseObject(EnumSet.of(ObjectClass.EXHAUST_EFFECTS));
        }

        UseObject rackRail = null;
        if (vars.getVehicleClass() < 2 && vars.getVehicleFlags().contains(VehicleVars.VehicleFlag.RACKRAIL)) {
            rackRail = inputStream.readUseObject(EnumSet.of(ObjectClass.TRACK_MODIFICATIONS));
        }

        List<UseObject> compatibleVehicles = inputStream.readUseObjectList(vars.getNumCompat(), ObjectClass.VEHICLES);

        UseObject startSnd = null;
        if (vars.getStartsndtype() != 0) {
            startSnd = inputStream.readUseObject(EnumSet.of(ObjectClass.SOUND_EFFECTS));
        }

        int soundCount = vars.getNumSnd() & 0x7f;
        List<UseObject> sounds = inputStream.readUseObjectList(soundCount, ObjectClass.SOUND_EFFECTS);

        Sprites sprites = loadSprites(inputStream);

        return new Vehicle(name, description, vars, trackType, trackModifications, cargoCapacities, visualFx, wakeFx, rackRail, startSnd, compatibleVehicles, sounds, sprites);
    }

    public static CargoCapacity loadCargoCapacity(DatFileInputStream in) throws IOException {
        byte capacity = in.readByte();
        List<CargoCapacity.CargoRefitCapacity> refitCapacities = new ArrayList<>();
        if (capacity != 0) {
            int term = in.readUShort();
            while (term != 0xFFFF) {
                byte cargoType = (byte)(term & 0xFF);
                int refCap = ((term >> 8) & 0xFF) | (in.readByte() << 8);
                refitCapacities.add(new CargoCapacity.CargoRefitCapacity(cargoType, refCap));
                term = in.readUShort();
            }
        }
        return new CargoCapacity(capacity, refitCapacities);
    }

    public static GroundVars loadGroundVars(DatFileInputStream in) throws IOException {
        in.skipBytes(2);
        byte costInd = in.readByte();
        in.skipBytes(5);
        short costFactor = in.readSShort();
        in.skipBytes(20);
        return new GroundVars(costInd, costFactor);
    }

    public static Ground loadGround(String name, DatFileInputStream dataInputStream) throws IOException {
        GroundVars groundVars = loadGroundVars(dataInputStream);
        MultiLangString desc = dataInputStream.readMultiLangString();
        UseObject cliff = dataInputStream.readUseObject(EnumSet.of(ObjectClass.CLIFF_FACES));
        Sprites sprites = loadSprites(dataInputStream);
        return new Ground(name, groundVars, desc, cliff, sprites);
    }

    public static CliffFace loadCliffFace(String name, DatFileInputStream dataInputStream) throws IOException {
        dataInputStream.skipBytes(6);
        MultiLangString description = dataInputStream.readMultiLangString();
        Sprites sprites = loadSprites(dataInputStream);
        return new CliffFace(name, description, sprites);
    }

    public static Sprites loadSprites(DatFileInputStream in) throws IOException {
        List<Sprites.RawSprite> sprites = new ArrayList<>();

        long num = in.readUnsignedInt();
        in.skipBytes(4); // size

        List<Sprites.SpriteHeader> spriteHeaders = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            spriteHeaders.add(loadSpriteHeader(in));
        }

        for (Sprites.SpriteHeader header : spriteHeaders) {
            boolean isChunked = header.getFlags().contains(Sprites.SpriteFlag.CHUNKED);

            if (isChunked) {
                in.skipBytes(2*header.getHeight());
            }

            int[] pixels = new int[header.getWidth() * header.getHeight()];
            for (int row=0; row<header.getHeight(); row++) {
                int rowOffset = row * header.getWidth();

                if (isChunked) {
                    for (int col=0; col<header.getWidth(); col++) {
                        pixels[rowOffset + col] = Palette.BACKGROUND;
                    }

                    boolean last;
                    do {
                        int lenLast = 0xFF & in.readByte();
                        last = (0xFF & (lenLast & 0x80)) != 0;
                        int len = 0x7f & lenLast;
                        int offset = 0xFF & in.readByte();
                        for (int col = offset; col < offset + len; col++) {
                            pixels[rowOffset + col] = Palette.COLOUR[0xFF & in.readByte()];
                        }
                    }
                    while (!last);
                }
                else {
                    for (int col=0; col<header.getWidth(); col++) {
                        pixels[rowOffset + col] = Palette.COLOUR[0xFF & in.readByte()];
                    }
                }
            }

            sprites.add(new Sprites.RawSprite(header, pixels));
        }

        return new Sprites(sprites);
    }

    public static Sprites.SpriteHeader loadSpriteHeader(DatFileInputStream in) throws IOException {
        long offset = in.readUnsignedInt();
        int width = in.readUShort();
        int height = in.readUShort();
        int xOffset = in.readSShort();
        int yOffset = in.readSShort();
        EnumSet<Sprites.SpriteFlag> flags = in.readBitField(4, Sprites.SpriteFlag.class);
        return new Sprites.SpriteHeader(offset, width, height, xOffset, yOffset, flags);
    }

    public static InterfaceStyle loadInterfaceStyle(String name, DatFileInputStream dataInputStream) throws IOException {
        MultiLangString styleName = dataInputStream.readMultiLangString();
        Sprites sprites = loadSprites(dataInputStream);
        return new InterfaceStyle(name, styleName, sprites);
    }

    public static Company loadCompany(String name, DatFileInputStream dataInputStream) throws IOException {
        Company.CompanyVars companyVars = loadCompanyVars(dataInputStream);
        MultiLangString ceoName = dataInputStream.readMultiLangString();
        MultiLangString companyName = dataInputStream.readMultiLangString();
        Sprites sprites = loadSprites(dataInputStream);
        return new Company(name, companyVars, ceoName, companyName, sprites);
    }

    public static VehicleSpriteVar loadVehicleSpriteVar(DatFileInputStream in) throws IOException {
        byte levelSpriteCount = in.readByte();
        byte upDownSpriteCount = in.readByte();
        byte frames = in.readByte();
        byte vehType = in.readByte();
        byte numUnits = in.readByte();
        byte tiltCount = in.readByte();
        byte bogeyPos = in.readByte();
        EnumSet<VehicleSpriteVar.VehicleSpriteFlag> flags = in.readBitField(1, VehicleSpriteVar.VehicleSpriteFlag.class);
        in.skipBytes(6);
        byte spriteNum = in.readByte();
        in.skipBytes(15);
        return new VehicleSpriteVar(levelSpriteCount, upDownSpriteCount, frames, vehType, numUnits, tiltCount, bogeyPos, flags, spriteNum);
    }

    public static VehicleVars loadVehicleVars(DatFileInputStream in) throws IOException {
        in.skipBytes(2);
        byte vehicleClass = in.readByte();
        byte vehicleType = in.readByte();
        in.skipBytes(2);
        byte numMods = in.readByte();
        byte costInd = in.readByte();
        short costFact = in.readSShort();
        byte reliability = in.readByte();
        byte runCostInd = in.readByte();
        short runCostFact = in.readSShort();
        byte colourType = in.readByte();
        byte numCompat = in.readByte();
        in.skipBytes(20);
        in.skipBytes(24); //vehunk
        List<VehicleSpriteVar> vehSprites = new ArrayList<>();
        for (int i=0; i<4; i++) {
            vehSprites.add(loadVehicleSpriteVar(in));
        }
        in.skipBytes(36);
        int power = in.readUShort();
        int speed = in.readUShort();
        int rackSpeed = in.readUShort();
        int weight = in.readUShort();
        EnumSet<VehicleVars.VehicleFlag> vehicleFlags = in.readBitField(2, VehicleVars.VehicleFlag.class);
        in.skipBytes(44);
        byte visFxHeight = in.readByte();
        byte visFxType = in.readByte();
        in.skipBytes(2);
        byte wakeFxType = in.readByte();
        in.skipBytes(1);
        int designed = in.readUShort();
        int obsolete = in.readUShort();
        in.skipBytes(1);
        byte startsndtype = in.readByte();
        in.skipBytes(64);
        byte numSnd = in.readByte();
        in.skipBytes(3);

        return new VehicleVars(vehicleClass, vehicleType, numMods, costInd, costFact, reliability, runCostInd,
                runCostFact, colourType, numCompat, vehSprites, power, speed, rackSpeed, weight, vehicleFlags,
                visFxHeight, visFxType, wakeFxType, designed, obsolete, startsndtype, numSnd);
    }

    public static Company.CompanyVars loadCompanyVars(DatFileInputStream in) throws IOException {
        in.skipBytes(12);
        in.skipBytes(2); //spritesets
        in.skipBytes(38);
        byte intelligence = in.readByte();
        byte aggressiveness = in.readByte();
        byte competitiveness = in.readByte();

        return new Company.CompanyVars(intelligence, aggressiveness, competitiveness);
    }

    public static Track loadTrack(String name, DatFileInputStream in) throws IOException {
        Track.TrackVars trackVars = loadTrackVars(in);
        MultiLangString description = in.readMultiLangString();
        List<UseObject> compatibleTracks = in.readUseObjectList(trackVars.getNumCompat(), ObjectClass.TRACKS, ObjectClass.ROADS);
        List<UseObject> modifications = in.readUseObjectList(trackVars.getNumMods(), ObjectClass.TRACK_MODIFICATIONS);
        List<UseObject> signals = in.readUseObjectList(trackVars.getNumSignals(), ObjectClass.SIGNALS);
        UseObject tunnel = in.readUseObject(EnumSet.of(ObjectClass.TUNNELS));
        List<UseObject> bridges = in.readUseObjectList(trackVars.getNumBridges(), ObjectClass.BRIDGES);
        List<UseObject> stations = in.readUseObjectList(trackVars.getNumStations(), ObjectClass.TRAIN_STATIONS);
        Sprites sprites = loadSprites(in);

        return new Track(name, trackVars, description, compatibleTracks, modifications, signals, tunnel, bridges, stations, sprites);
    }

    private static Track.TrackVars loadTrackVars(DatFileInputStream in) throws IOException {
        in.skipBytes(2);
        EnumSet<Track.TrackPiece> trackPieces = in.readBitField(2, Track.TrackPiece.class);
        EnumSet<Track.TrackPiece> stationTrackPieces = in.readBitField(2, Track.TrackPiece.class);
        in.skipBytes(1);
        int numCompat = in.readUnsignedByte();
        int numMods = in.readUnsignedByte();
        int numSignals = in.readUnsignedByte();
        in.skipBytes(10);
        short buildCostFact = in.readSShort();
        short sellCostFact = in.readSShort();
        short tunnelCostFact = in.readSShort();
        int costInd = in.readUnsignedByte();
        in.skipBytes(1);
        int curveSpeed = in.readUShort();
        in.skipBytes(6);
        int numBridges = in.readUnsignedByte();
        in.skipBytes(7);
        int numStations = in.readUnsignedByte();
        in.skipBytes(7);
        int displayOffset = in.readUnsignedByte();
        in.skipBytes(1);

        return new Track.TrackVars(trackPieces, stationTrackPieces, numCompat, numMods, numSignals, buildCostFact,
                                  sellCostFact, tunnelCostFact, costInd, curveSpeed, numBridges, numStations,
                                  displayOffset);
    }

    private static Tree loadTree(String name, DatFileInputStream in) throws IOException {
        return loadSimpleObject(name, in, Tree::new, DatFileLoader::loadTreeVars);
    }

    private static TreeVars loadTreeVars(DatFileInputStream in) throws IOException {
        in.skipBytes(3);
        int height = in.readUnsignedByte();
        in.skipBytes(59);
        int costInd = in.readUnsignedByte();
        int buildCostFact = in.readSShort();
        int clearCostFact = in.readSShort();
        in.skipBytes(8);

        return new TreeVars(height, costInd, buildCostFact, clearCostFact);
    }

    private static Bridge loadBridge(String name, DatFileInputStream in) throws IOException {
        return loadSimpleObject(name, in, Bridge::new, DatFileLoader::loadBridgeVars);
    }

    private static BridgeVars loadBridgeVars(DatFileInputStream in) throws IOException {
        in.skipBytes(2);
        int noRoof = in.readUnsignedByte();
        in.skipBytes(5);
        int spanLength = in.readUnsignedByte();
        int pillarSpacing = in.readUnsignedByte();
        int maxSpeed = in.readSShort();
        int maxHeight = in.readUnsignedByte();
        int costInd = in.readUnsignedByte();
        int baseCostFactor = in.readUShort();
        int heightCostFactor = in.readUShort();
        int sellCostFactor = in.readUShort();
        int disabledTrackCfg = in.readSShort();
        in.skipBytes(22);
        return new BridgeVars(noRoof, spanLength, pillarSpacing, maxSpeed, maxHeight, costInd, baseCostFactor, heightCostFactor, sellCostFactor, disabledTrackCfg);
    }

    private static Industry loadIndustry(String name, DatFileInputStream in) throws IOException {
        IndustryVars industryVars = loadIndustryVars(in);
        MultiLangString description = in.readMultiLangString();
        MultiLangString templatedName = in.readMultiLangString();
        MultiLangString prefixDescription = in.readMultiLangString();
        MultiLangString closingDownMessage = in.readMultiLangString();
        MultiLangString productionUpMessage = in.readMultiLangString();
        MultiLangString productionDownMessage = in.readMultiLangString();
        MultiLangString singular = in.readMultiLangString();
        MultiLangString plural = in.readMultiLangString();

        long[] aux0 = in.loadAux(industryVars.getNumAux01(), 1);
        long[] aux1 = in.loadAux(industryVars.getNumAux01(), 2);

        long[][] aux2 = new long[4][];
        for (int i=0; i<4; i++) {
            int size = Math.abs(in.readByte());
            long[] value = new long[size];
            for (int j=0; j<size; j++) {
                value[j] = in.readByte();
            }
            aux2[i] = value;
        }

        long[] aux3 = in.loadAuxVarCount(1, 2);
        long[][] aux4 = in.loadAuxArrayVarCount(2, industryVars.getNumAux4Ent(), 1);
        long[] aux5 = in.loadAux(industryVars.getNumAux5(), 1);

        List<UseObject> produces = Collections.emptyList();
        List<UseObject> accepts = Collections.emptyList();
        List<UseObject> fences = Collections.emptyList();

        //List<UseObject> produces = in.readUseObjectList(2, ObjectClass.CARGOES);
        //List<UseObject> accepts = in.readUseObjectList(3, ObjectClass.CARGOES);
        //List<UseObject> fences = in.readUseObjectList(6, ObjectClass.FENCES);

        Sprites sprites = new Sprites(Collections.emptyList());

        //Sprites sprites = loadSprites(in);

        return new Industry(name, industryVars, description, templatedName, prefixDescription, closingDownMessage, productionUpMessage, productionDownMessage, singular, plural, aux0, aux1, aux2, aux3, aux4, aux5, produces, accepts, fences, sprites);
    }

    private static IndustryVars loadIndustryVars(DatFileInputStream in) throws IOException {
        in.skipBytes(30);
        byte numAux01 = in.readByte();
        byte numAux4Ent = in.readByte();
        in.skipBytes(157);
        byte numAux5 = in.readByte();
        in.skipBytes(12);
        int firstYear = in.readUShort();
        int lastYear = in.readUShort();
        in.skipBytes(1);
        byte costInd = in.readByte();
        int costFactor = in.readSShort();
        in.skipBytes(18);
        EnumSet<IndustryVars.IndustryFlag> industryFlags = in.readBitField(4, IndustryVars.IndustryFlag.class);
        in.skipBytes(12);
        return new IndustryVars(numAux01, numAux4Ent, numAux5, firstYear, lastYear, costInd, costFactor, industryFlags);
    }

    private static void dumpSprites(String dataDir, String name, Sprites sprites) throws IOException {
        int index = 0;
        for (Sprites.RawSprite sprite: sprites.getList()) {
            File f = new File(dataDir + "/../sprites/" + name.trim() + "_" + index + ".png");
            FileOutputStream fos = new FileOutputStream(f);
            BufferedImage image = new BufferedImage(sprite.getHeader().getWidth(), sprite.getHeader().getHeight(), BufferedImage.TYPE_INT_ARGB);
            image.setRGB(0, 0, sprite.getHeader().getWidth(), sprite.getHeader().getHeight(), sprite.getPixels(), 0, sprite.getHeader().getWidth());
            ImageIO.write(image, "png", fos);
            index++;
        }
    }

    private static <T, V> T loadSimpleObject(String name, DatFileInputStream in,
                                             SimpleObjectFactory<T, V> simpleObjectFactory,
                                             VarExtractor<V> varExtractor) throws IOException {
        V vars = varExtractor.extract(in);
        MultiLangString desc = in.readMultiLangString();
        Sprites sprites = loadSprites(in);
        return simpleObjectFactory.create(name, vars, desc, sprites);
    }

    public static interface SimpleObjectFactory<T, V> {
        T create(String name, V vars, MultiLangString description, Sprites sprites);
    }

    public static interface VarExtractor<V> {
        V extract(DatFileInputStream in) throws IOException;
    }

}
