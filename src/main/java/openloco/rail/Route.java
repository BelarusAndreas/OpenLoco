package openloco.rail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;

public class Route {

    private List<TrackNode> nodes = new ArrayList<>();

    public Route(Collection<TrackNode> path) {
        nodes = new ArrayList<>(path);
    }

    public void addNode(TrackNode node) {
        nodes.add(node);
    }

}
