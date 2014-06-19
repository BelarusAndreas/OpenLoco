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
        int rotation;
        if (orientation == Orientation.N || orientation == Orientation.S) {
            rotation = 0;
        }
        else if (orientation == Orientation.E || orientation == Orientation.W) {
            rotation = 1;
        }
        else {
            throw new IllegalArgumentException("Invalid orientation " + orientation + " for piece type " + Track.TrackPiece.STRAIGHT);
        }
        addNode(Track.TrackPiece.STRAIGHT, rotation);
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

    public void addSmallCurve(CurveDirection curveDirection) {
        int rotation = orientation.ordinal()/2;

        if (curveDirection == CurveDirection.LEFT) {
            int[][] preOffsets = { {-1, -1}, {1, -1}, {1, 1}, {-1, 1}};
            int[] preOffset = preOffsets[rotation];
            currentX += preOffset[0];
            currentY += preOffset[1];
            int tileRotations[] = {1, 2, 3, 0};
            int tileRotation = tileRotations[rotation];
            addNode(Track.TrackPiece.SMALLCURVE, tileRotation);
            currentX -= preOffset[0];
            currentY -= preOffset[1];
            int[][] offsets = { {-2, -1}, {1, -2}, {2, 1}, {-1, 2} };
            int[] offset = offsets[rotation];
            currentX += offset[0];
            currentY += offset[1];
        }
        else {
            int[][] offsets = { {2, -1}, {1, 2}, {-2, 1}, {-1,-2} };
            addNode(Track.TrackPiece.SMALLCURVE, rotation);
            int[] offset = offsets[rotation];
            currentX += offset[0];
            currentY += offset[1];
        }
        updateOrientationForCurveDirection(curveDirection);
    }

    private void updateOrientationForCurveDirection(CurveDirection curveDirection) {
        int rotation = orientation.ordinal() / 2;
        rotation = (rotation + (curveDirection == CurveDirection.LEFT? -1 : 1) + 4) % 4;
        orientation = Orientation.values()[rotation*2];
    }

    private void addNode(Track.TrackPiece pieceType, int rotation) {
        nodes.add(new TrackNode(currentX, currentY, currentZ, pieceType, rotation));
    }

    public List<TrackNode> getNodes() {
        return nodes;
    }
}
