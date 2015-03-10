package openloco.routing;

import openloco.graphics.CartCoordRot;

public class RouteNodePosition {

    private final RouteNode node;
    private final int position;

    public RouteNodePosition(RouteNode node, int position) {
        this.node = node;
        this.position = position;
    }

    public RouteNode getRouteNode() {
        return node;
    }

    public int getPosition() {
        return position;
    }

    public CartCoordRot getCartCoord() {
        return node.getCartCoordAtPosition(position);
    }

    public RouteNodePosition moveAheadBy(int delta) {
        if (delta < 0) {
            throw new IllegalArgumentException("Negative delta specified - cannot move backwards along route!");
        }
        int newPosition = position + delta;

        if (newPosition >= node.getLength()) {
            int newDelta = delta - (node.getLength() - position);
            return new RouteNodePosition(node.getNext(), 0).moveAheadBy(newDelta);
        }
        else {
            return new RouteNodePosition(node, newPosition);
        }
    }
}
