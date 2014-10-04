package openloco.routing;

import openloco.rail.TrackNode;

public class RouteNode {

    private final TrackNode node;
    private RouteNode next;

    public RouteNode(TrackNode node) {
        this.node = node;
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
}
