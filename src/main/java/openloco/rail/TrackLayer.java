package openloco.rail;

import openloco.entities.Track;

import java.util.ArrayList;
import java.util.List;

public class TrackLayer {

    private int currentX;
    private int currentY;
    private int currentZ;
    private Orientation orientation;

    private List<TrackNode> nodes = new ArrayList<>();

    public TrackLayer(int startX, int startY, int startZ, Orientation orientation) {
        this.currentX = startX;
        this.currentY = startY;
        this.currentZ = startZ;
        this.orientation = orientation;
    }

    public void addTrackPiece(Track.TrackPiece pieceType) {
        if (pieceType == Track.TrackPiece.STRAIGHT) {
            int rotation;
            if (orientation == Orientation.N || orientation == Orientation.S) {
                rotation = 0;
            }
            else if (orientation == Orientation.E || orientation == Orientation.W) {
                rotation = 1;
            }
            else {
                throw new IllegalArgumentException("Invalid orientation " + orientation + " for piece type " + pieceType);
            }
            nodes.add(new TrackNode(currentX, currentY, currentZ, pieceType, rotation));
            switch (orientation) {
                case N:
                    currentY--;
                    break;
                case E:
                    currentX++;
                    break;
                case S:
                    currentY++;
                    break;
                case W:
                    currentX--;
                    break;
            }
        }
    }

    public List<TrackNode> getNodes() {
        return nodes;
    }

}
