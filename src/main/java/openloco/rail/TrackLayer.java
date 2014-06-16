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

    public void addStraight() {
        addTrackPiece(Track.TrackPiece.STRAIGHT);
    }

    public void addSBend(CurveDirection curveDirection) {
        int rotation = curveDirection == CurveDirection.LEFT? 0 : 2;

        if (orientation == Orientation.E || orientation == Orientation.W) {
            rotation++;
        }

        switch (orientation) {
            case S:
                currentX += (curveDirection == CurveDirection.LEFT)? 1 : -1;
                currentY += 3;
                break;
            case W:
                currentX -= 3;
                currentY += (curveDirection == CurveDirection.LEFT)? 1 : -1;
                break;
        }

        if (curveDirection == CurveDirection.LEFT) {
            addNode(Track.TrackPiece.SBEND, rotation);
        }
        else {
            addNode(Track.TrackPiece.SBEND, rotation);
        }

        switch (orientation) {
            case N:
                currentX += (curveDirection == CurveDirection.LEFT)? -1 : 1;
                currentY -= 3;
                break;
            case E:
                currentX += 3;
                currentY += (curveDirection == CurveDirection.LEFT)? -1 : 1;
                break;
        }
    }

    private void addTrackPiece(Track.TrackPiece pieceType) {
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
            addNode(pieceType, rotation);
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

    private void addNode(Track.TrackPiece pieceType, int rotation) {
        nodes.add(new TrackNode(currentX, currentY, currentZ, pieceType, rotation));
    }

    public List<TrackNode> getNodes() {
        return nodes;
    }
}
