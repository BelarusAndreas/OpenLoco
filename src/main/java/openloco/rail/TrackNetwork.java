package openloco.rail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrackNetwork {

    private List<TrackNode> allNodes = new ArrayList<>();

    public void add(TrackNode trackNode) {
        allNodes.add(trackNode);
    }

    public void addAll(List<TrackNode> nodes) {
        allNodes.addAll(nodes);
    }

    public List<TrackNode> getAllNodes() {
        return Collections.unmodifiableList(allNodes);
    }
}
