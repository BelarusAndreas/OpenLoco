package openloco.rail;

import com.google.common.base.Objects;
import openloco.assets.Track;
import openloco.graphics.CartCoord;
import openloco.graphics.Tile;

import java.util.HashSet;
import java.util.Set;

public class TrackNode {

    private final int x;
    private final int y;
    private final int z;
    private final Track.TrackPiece pieceType;
    private final int rotation;
    private Set<TrackNode> neighbours = new HashSet<>();
    private double length;

    public TrackNode(int x, int y, int z, Track.TrackPiece pieceType, int rotation) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pieceType = pieceType;
        this.rotation = rotation;
        this.length = Tile.WIDTH;
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

    public Set<TrackNode> getNeighbours() {
        return neighbours;
    }

    public double getLength() {
        return length;
    }

    public void connectTo(TrackNode other) {
        mutuallyConnect(other);
        other.mutuallyConnect(this);
    }

    private void mutuallyConnect(TrackNode other) {
        neighbours.add(other);
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

    public CartCoord getCartCoordAtPosition(int position, boolean forwards) {
        if (pieceType == Track.TrackPiece.STRAIGHT) {
            int offset = forwards ? position : Tile.WIDTH - position;
            if (rotation == 0) {
                return new CartCoord(Tile.WIDTH*x+16, Tile.WIDTH*y + offset, Tile.HEIGHT_STEP*z);
            }
            else if (rotation == 1) {
                return new CartCoord(Tile.WIDTH*x + offset, Tile.WIDTH*y, Tile.HEIGHT_STEP*z);
            }
        }
        throw new IllegalArgumentException("Cannot calculate position for orientation");
    }
}
