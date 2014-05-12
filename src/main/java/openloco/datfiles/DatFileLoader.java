package openloco.datfiles;

import openloco.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
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
        MultiLangString description = loadMultiLangString(inputStream);
        UseObject trackType = null;
        if (vars.getVehicleClass() < 2 && !vars.getVehicleFlags().contains(VehicleVars.VehicleFlag.ANYTRACK)) {
            trackType = loadUseObject(inputStream, EnumSet.of(ObjectClass.TRACKS, ObjectClass.ROADS));
        }
        //optional reference to track/road modification?
        List<UseObject> trackModifications = new ArrayList<>();
        for (int i=0; i<vars.getNumMods(); i++) {
            trackModifications.add(loadUseObject(inputStream, EnumSet.of(ObjectClass.TRACK_MODIFICATIONS, ObjectClass.ROAD_MODIFICATIONS)));
        }

        List<CargoCapacity> cargoCapacities = new ArrayList<>();
        for (int i=0; i<2; i++) {
            cargoCapacities.add(loadCargoCapacity(inputStream));
        }

        UseObject visualFx = null;
        if (vars.getVisFxType() != 0) {
            visualFx = loadUseObject(inputStream, EnumSet.of(ObjectClass.EXHAUST_EFFECTS));
        }

        UseObject wakeFx = null;
        if (vars.getWakeFxType() != 0) {
            wakeFx = loadUseObject(inputStream, EnumSet.of(ObjectClass.EXHAUST_EFFECTS));
        }

        UseObject rackRail = null;
        if (vars.getVehicleClass() < 2 && vars.getVehicleFlags().contains(VehicleVars.VehicleFlag.RACKRAIL)) {
            rackRail = loadUseObject(inputStream, EnumSet.of(ObjectClass.TRACK_MODIFICATIONS));
        }

        int numCompat = vars.getNumCompat();
        List<UseObject> numCompats = new ArrayList<>();
        for (int i=0; i<numCompat; i++) {
            numCompats.add(loadUseObject(inputStream, EnumSet.of(ObjectClass.VEHICLES)));
        }

        UseObject startSnd = null;
        if (vars.getStartsndtype() != 0) {
            startSnd = loadUseObject(inputStream, EnumSet.of(ObjectClass.SOUND_EFFECTS));
        }

        List<UseObject> sounds = new ArrayList<>();
        int soundCount = vars.getNumSnd() & 0x7f;
        for (int i=0; i<soundCount; i++) {
            sounds.add(loadUseObject(inputStream, EnumSet.of(ObjectClass.SOUND_EFFECTS)));
        }

        Sprites s = loadSprites(inputStream);

        return new Vehicle(name, description, vars, trackType, cargoCapacities, visualFx, wakeFx, rackRail, startSnd, sounds, s);
    }

    private static UseObject loadUseObject(DatFileInputStream inputStream, EnumSet<ObjectClass> objectClasses) throws IOException {
        byte objectClassId = inputStream.readByte();
        ObjectClass objectClass = ObjectClass.values()[objectClassId];
        if (!objectClasses.contains(objectClass)) {
            throw new RuntimeException("Invalid object reference");
        }
        inputStream.skipBytes(3);
        byte[] ref = new byte[8];
        inputStream.readFully(ref);
        String objectReference = new String(ref);
        inputStream.skipBytes(4);
        return new UseObject(objectClass, objectReference);
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

    public static MultiLangString loadMultiLangString(DataInputStream in) throws IOException {
        Map<Integer, String> strings = new HashMap<>();
        byte language;
        while ((language = in.readByte()) != (byte)0xFF){
            StringBuffer sb = new StringBuffer();
            char ch;
            while ((ch = (char)in.readByte()) != (byte)0x00) {
                sb.append(ch);
            }
            strings.put((int)language, sb.toString());
        }
        return new MultiLangString(strings);
    }

    public static Ground loadGround(String name, DatFileInputStream dataInputStream) throws IOException {
        GroundVars groundVars = loadGroundVars(dataInputStream);
        MultiLangString desc = loadMultiLangString(dataInputStream);
        UseObject cliff = loadUseObject(dataInputStream, EnumSet.of(ObjectClass.CLIFF_FACES));
        Sprites sprites = loadSprites(dataInputStream);
        return new Ground(name, groundVars, desc, cliff, sprites);
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

    public static Company loadCompany(String name, DatFileInputStream dataInputStream) throws IOException {
        Company.CompanyVars companyVars = loadCompanyVars(dataInputStream);
        MultiLangString ceoName = loadMultiLangString(dataInputStream);
        MultiLangString companyName = loadMultiLangString(dataInputStream);
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

}
