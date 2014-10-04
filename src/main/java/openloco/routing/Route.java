package openloco.routing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;

import openloco.rail.TrackNode;

public class Route {

    private List<RouteNode> nodes = new ArrayList<>();

    public Route(Collection<RouteNode> path) {
        nodes = new ArrayList<>(path);
        RouteNode prev = null;
        for (RouteNode node: nodes) {
            if (prev != null) {
                prev.setNext(node);
            }
            prev = node;
        }
    }

    public void addNode(RouteNode node) {
        nodes.add(node);
    }

    List<RouteNode> getNodes() {
        return nodes;
    }
}
