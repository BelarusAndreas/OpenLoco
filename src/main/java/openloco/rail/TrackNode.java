package openloco.rail;

import com.google.common.base.Objects;
import openloco.assets.Track;
import openloco.graphics.CartCoord;
import openloco.graphics.CartCoordRot;
import openloco.graphics.Tile;

import java.util.HashSet;
import java.util.Set;

public class TrackNode {

    private final int x;
    private final int y;
    private final int z;
    private final Track.TrackPiece pieceType;
    private final int rotation;
    private Set<TrackNode> connectedTo = new HashSet<>();
    private Set<TrackNode> connectedFrom = new HashSet<>();

    private final double[] rotOfs = new double[] { -1*Math.PI, -0.5*Math.PI, 0, 0.5*Math.PI, -0.25*Math.PI, 0.25*Math.PI, 0.75*Math.PI, -0.75*Math.PI };

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

    public String getBridgeType() {
        return "BRDGBRCK";
    }

    public Set<TrackNode> getConnectedTo() {
        return connectedTo;
    }

    public Set<TrackNode> getConnectedFrom() {
        return connectedFrom;
    }

    public double getLength() {
        return pieceType.getLength();
    }

    public void connectTo(TrackNode other) {
        connectedTo.add(other);
    }

    public void connectFrom(TrackNode other) {
        connectedFrom.add(other);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("x", x)
                .add("y", y)
                .add("z", z)
                .add("pieceType", pieceType)
                .add("rotation", rotation)
                .toString();
    }

    public CartCoordRot getCartCoordAtPosition(int position, boolean forwards) {
        if (pieceType == Track.TrackPiece.STRAIGHT) {
            int offset = forwards ? position : Tile.WIDTH - position;
            if (rotation == 0) {
                double rot = forwards? 0 : Math.PI;
                return new CartCoordRot(new CartCoord(Tile.WIDTH*x+16, Tile.WIDTH*y + offset, Tile.HEIGHT_STEP*z), rot);
            }
            else if (rotation == 1) {
                double rot = forwards? Math.PI/2 : -Math.PI/2;
                return new CartCoordRot(new CartCoord(Tile.WIDTH*x + offset, Tile.WIDTH*y+16, Tile.HEIGHT_STEP*z), rot);
            }
        }
        else if (pieceType == Track.TrackPiece.WIDECURVE) {
            Track.CurveType curveType = Track.CurveType.WIDE;
            double offset = forwards ? position : curveType.getSegmentLength() - position;
            return calculateCoordForOffset(curveType, offset);
        }
        throw new IllegalArgumentException("Cannot calculate position for orientation");
    }

    private CartCoordRot calculateCoordForOffset(Track.CurveType curveType, double offset) {
        CartCoord centre = calculateCurveCentre(curveType);
        double r = Tile.WIDTH * curveType.getRadius();
        double theta = offset / r + rotOfs[rotation % curveType.getSegments()];
        double x = r * Math.cos(theta);
        double y = r * Math.sin(theta);
        return new CartCoordRot(centre.plus((int)Math.round(x), (int)Math.round(y), 0), theta+Math.PI);
    }

    private CartCoord calculateCurveCentre(Track.CurveType curveType) {
        int xMult = 0;
        int yMult = 0;
        int xOfs = 0;
        int yOfs = 0;

        switch (rotation) {
            case 0:
                xMult = 1;
                xOfs = 1;
                yOfs = 1;
                break;

            case 1:
                yMult = 1;
                yOfs = 1;
                break;

            case 2:
                xMult = -1;
                break;

            case 3:
                xOfs = 1;
                yMult = -1;
                break;

            case 4:
                xMult = -1;
                xOfs = 1;
                yOfs = 3;
                break;

            case 5:
                yMult = -1;
                xOfs = -2;
                yOfs = 1;
                break;

            case 6:
                yOfs = -2;
                xMult = 1;
                break;

            case 7:
                xOfs = 3;
                yMult = 1;
                break;
        }

        return new CartCoord(Tile.WIDTH * (int)(x + xOfs + xMult*(curveType.getRadius()-0.5)),
                                 Tile.WIDTH * (int)(y + yOfs + yMult*(curveType.getRadius()-0.5)),
                                 z);
    }
}
