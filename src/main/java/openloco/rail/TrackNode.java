package openloco.rail;

import openloco.entities.Track;

public class TrackNode {

    private final int x;
    private final int y;
    private final int z;
    private final Track.TrackPiece pieceType;
    private final int rotation;

    public TrackNode(int x, int y, int z, Track.TrackPiece pieceType, int rotation) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pieceType = pieceType;
        this.rotation = rotation;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public Track.TrackPiece getPieceType() {
        return pieceType;
    }

    public int getRotation() {
        return rotation;
    }
}
