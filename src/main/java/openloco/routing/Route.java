package openloco.routing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;

import openloco.rail.TrackNode;

public class Route {

    private List<TrackNode> nodes = new ArrayList<>();

    public Route(Collection<TrackNode> path) {
        nodes = new ArrayList<>(path);
    }

    public void addNode(TrackNode node) {
        nodes.add(node);
    }

}
