package openloco.routing;

import openloco.graphics.CartCoord;
import openloco.rail.TrackNode;

public class RouteNode {

    private final TrackNode node;
    private final boolean forwards;
    private RouteNode next;

    public RouteNode(TrackNode node, boolean forwards) {
        this.node = node;
        this.forwards = forwards;
    }

    public void setNext(RouteNode node) {
        this.next = node;
    }

    public int getLength() {
        return (int) Math.floor(node.getLength());
    }

    public RouteNode getNext() {
        return next;
    }

    public CartCoord getCartCoordAtPosition(int position) {
        return node.getCartCoordAtPosition(position, forwards);
    }
}
