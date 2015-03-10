package openloco.assets;

import openloco.graphics.Tile;

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
        DIAGONAL(1, Tile.WIDTH),        // XXX
        WIDECURVE(2, CurveType.WIDE.getSegmentLength()),
        MEDIUMCURVE(4, CurveType.MEDIUM.getSegmentLength()),
        SMALLCURVE(8, CurveType.SMALL.getSegmentLength()),
        TIGHTCURVE(10, CurveType.TIGHT.getSegmentLength()),
        NORMALSLOPE(20, Tile.WIDTH),    // XXX
        STEEPSLOPE(40, Tile.WIDTH),    // XXX
        UNKNOWN(80, Tile.WIDTH),    // XXX
        SLOPEDCURVE(100, Tile.WIDTH),    // XXX
        SBEND(200, Tile.WIDTH),    // XXX
        STRAIGHT(0, Tile.WIDTH);

        private final int id;
        private final double length;

        private TrackPiece(int id, double length) {
            this.id = id;
            this.length = length;
        }

        public double getLength() {
            return length;
        }
    }

    public static enum CurveType {
        WIDE(3.5, 8),
        MEDIUM(1.5, 4),
        SMALL(1, 4),
        TIGHT(1, 4);

        private final double radius;
        private final int segments;
        private final double segmentLength;

        CurveType(double radius, int segments) {
            this.radius = radius;
            this.segments = segments;
            this.segmentLength = Math.round(Tile.WIDTH * 2.0 * Math.PI * radius / (double)segments);
        }

        public double getRadius() {
            return radius;
        }

        public int getSegments() {
            return segments;
        }

        public double getSegmentLength() {
            return segmentLength;
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
