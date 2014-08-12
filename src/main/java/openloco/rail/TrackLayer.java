package openloco.rail;

import openloco.assets.Track;

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
        Track.TrackPiece pieceType;
        switch (orientation) {
            case N:
            case S:
                rotation = 0;
                pieceType = Track.TrackPiece.STRAIGHT;
                break;
            case NE:
            case SW:
                rotation = 0;
                pieceType = Track.TrackPiece.DIAGONAL;
                break;
            case E:
            case W:
                rotation = 1;
                pieceType = Track.TrackPiece.STRAIGHT;
                break;
            case SE:
            case NW:
                rotation = 1;
                pieceType = Track.TrackPiece.DIAGONAL;
                break;

            default:
                throw new IllegalArgumentException("Invalid orientation " + orientation + " for straight");
        }

        final int[][] preTranslations = {{0, 0}, {0, 0}, {0, 0}, {0, 0}, {0, 0}, {-1, 1}, {0, 0}, {-1, -1}};
        final int[] preTranslation = preTranslations[orientation.ordinal()];

        currentX += preTranslation[0];
        currentY += preTranslation[1];
        addNode(pieceType, rotation);
        currentX -= preTranslation[0];
        currentY -= preTranslation[1];

        final int[][] translations = {{0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}};
        final int[] translation = translations[orientation.ordinal()];

        currentX += translation[0];
        currentY += translation[1];
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
        addCurve(curveDirection, Track.TrackPiece.SMALLCURVE);
    }

    public void addMediumCurve(CurveDirection curveDirection) {
        addCurve(curveDirection, Track.TrackPiece.MEDIUMCURVE);
    }

    public void addWideCurve(CurveDirection curveDirection) {
        if (curveDirection == CurveDirection.LEFT) {
            final int[] rotations = {4, 2, 5, 3, 6, 0, 7, 1};
            int rotation = rotations[orientation.ordinal()];

            int[][] preTranslations = {{-1, 2}, {-2, -1}, {1, -2}, {2, 1}, {-1, -2}, {2, -1}, {1, 2}, {-2, 1}};
            int[] preTranslation = preTranslations[rotation];
            currentX += preTranslation[0];
            currentY += preTranslation[1];
            addNode(Track.TrackPiece.WIDECURVE, rotation);
            currentX -= preTranslation[0];
            currentY -= preTranslation[1];

            int[][] postTranslations = {{-1, 3}, {-3, -1}, {1, -3}, {3, 1}, {-1, -2}, {2, -1}, {1, 2}, {-2, 1}};
            int[] postTranslation = postTranslations[rotation];
            currentX += postTranslation[0];
            currentY += postTranslation[1];

            orientation = Orientation.values()[(orientation.ordinal()+7) % 8];
        }
        else {
            final int[] rotations = {0, 7, 1, 4, 2, 5, 3, 6};
            int rotation = rotations[orientation.ordinal()];

            addNode(Track.TrackPiece.WIDECURVE, rotation);

            int[][] afterOffset = {{1, -2}, {3, -1}, {2, 1}, {1, 3}, {-1, 2}, {-3, 1}, {-2, -1}, {-1, -3}};
            int[] offset = afterOffset[orientation.ordinal()];
            currentX += offset[0];
            currentY += offset[1];
            orientation = Orientation.values()[(orientation.ordinal()+1) % 8];
        }

    }

    private void addCurve(CurveDirection curveDirection, Track.TrackPiece curveType) {
        int rotation = orientation.ordinal()/2;

        int curveSize = curveType == Track.TrackPiece.SMALLCURVE? 2 : 3;

        if (curveDirection == CurveDirection.LEFT) {
            int[][] preOffsets = { {-(curveSize-1), -(curveSize-1)}, {(curveSize-1), -(curveSize-1)}, {(curveSize-1), (curveSize-1)}, {-(curveSize-1), (curveSize-1)}};
            int[] preOffset = preOffsets[rotation];
            currentX += preOffset[0];
            currentY += preOffset[1];
            int tileRotations[] = {1, 2, 3, 0};
            int tileRotation = tileRotations[rotation];
            addNode(curveType, tileRotation);
            currentX -= preOffset[0];
            currentY -= preOffset[1];
            int[][] offsets = { {-curveSize, -(curveSize-1)}, {(curveSize-1), -curveSize}, {curveSize, (curveSize-1)}, {-(curveSize-1), curveSize} };
            int[] offset = offsets[rotation];
            currentX += offset[0];
            currentY += offset[1];
        }
        else {
            int[][] offsets = { {curveSize, -(curveSize-1)}, {(curveSize-1), curveSize}, {-curveSize, (curveSize-1)}, {-(curveSize-1),-curveSize} };
            addNode(curveType, rotation);
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

    public void addNormalSlopeUp() {
        nodes.add(new TrackNode(currentX, currentY, currentZ, Track.TrackPiece.NORMALSLOPE, orientation.ordinal()/2));
        switch (orientation) {
            case N:
                currentY -= 2;
                break;
            case E:
                currentX += 2;
                break;
            case S:
                currentY += 2;
                break;
            case W:
                currentX -= 2;
                break;
        }
        currentZ++;
    }

    private void addNode(Track.TrackPiece pieceType, int rotation) {
        nodes.add(new TrackNode(currentX, currentY, currentZ, pieceType, rotation));
    }

    public List<TrackNode> getNodes() {
        return nodes;
    }

    public int getCurrentX() {
        return currentX;
    }

    public int getCurrentY() {
        return currentY;
    }

    public Orientation getOrientation() {
        return orientation;
    }
}
