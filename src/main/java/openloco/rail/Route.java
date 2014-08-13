package openloco.rail;

import java.util.ArrayList;
import java.util.List;

public class Route {

    private List<TrackNode> nodes = new ArrayList<>();

    public void addNode(TrackNode node) {
        nodes.add(node);
    }

}
