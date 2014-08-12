package openloco.assets;

import java.util.EnumSet;
import java.util.List;

public class Track {

    public static class TrackVars {

        private final EnumSet<TrackPiece> trackPieces;
        private final EnumSet<TrackPiece> stationTrackPieces;
        private final int numCompat;
        private final int numMods;
        private final int numSignals;
        private final short buildCostFact;
        private final short sellCostFact;
        private final short tunnelCostFact;
        private final int costInd;
        private final int curveSpeed;
        private final int numBridges;
        private final int numStations;
        private final int displayOffset;

        public TrackVars(EnumSet<TrackPiece> trackPieces, EnumSet<TrackPiece> stationTrackPieces, int numCompat, int numMods, int numSignals, short buildCostFact, short sellCostFact, short tunnelCostFact, int costInd, int curveSpeed, int numBridges, int numStations, int displayOffset) {
            this.trackPieces = trackPieces;
            this.stationTrackPieces = stationTrackPieces;
            this.numCompat = numCompat;
            this.numMods = numMods;
            this.numSignals = numSignals;
            this.buildCostFact = buildCostFact;
            this.sellCostFact = sellCostFact;
            this.tunnelCostFact = tunnelCostFact;
            this.costInd = costInd;
            this.curveSpeed = curveSpeed;
            this.numBridges = numBridges;
            this.numStations = numStations;
            this.displayOffset = displayOffset;
        }

        public int getNumCompat() {
            return numCompat;
        }

        public int getNumMods() {
            return numMods;
        }

        public int getNumSignals() {
            return numSignals;
        }

        public int getNumBridges() {
            return numBridges;
        }

        public int getNumStations() {
            return numStations;
        }
    }

    public static enum TrackPiece {
        DIAGONAL(1),
        WIDECURVE(2),
        MEDIUMCURVE(4),
        SMALLCURVE(8),
        TIGHTCURVE(10),
        NORMALSLOPE(20),
        STEEPSLOPE(40),
        UNKNOWN(80),
        SLOPEDCURVE(100),
        SBEND(200),
        STRAIGHT(0);

        private final int id;

        private TrackPiece(int id) {
            this.id = id;
        }
    }

    private final String name;
    private final TrackVars trackVars;
    private final MultiLangString description;
    private final List<UseObject> compatibleTracks;
    private final List<UseObject> modifications;
    private final List<UseObject> signals;
    private final UseObject tunnel;
    private final List<UseObject> bridges;
    private final List<UseObject> stations;
    private final Sprites sprites;

    public Track(String name, TrackVars trackVars, MultiLangString description, List<UseObject> compatibleTracks, List<UseObject> modifications, List<UseObject> signals, UseObject tunnel, List<UseObject> bridges, List<UseObject> stations, Sprites sprites) {
        this.name = name;
        this.trackVars = trackVars;
        this.description = description;
        this.compatibleTracks = compatibleTracks;
        this.modifications = modifications;
        this.signals = signals;
        this.tunnel = tunnel;
        this.bridges = bridges;
        this.stations = stations;
        this.sprites = sprites;
    }

    public String getName() {
        return name;
    }

    public Sprites getSprites() {
        return sprites;
    }
}
