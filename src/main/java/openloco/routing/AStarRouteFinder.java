package openloco.routing;

import openloco.graphics.Tile;
import openloco.rail.TrackNetwork;
import openloco.rail.TrackNode;

import java.util.*;

public class AStarRouteFinder implements RouteFinder {

    private static class DirectedNode {
        private final TrackNode trackNode;
        private final boolean forwards;

        public DirectedNode(TrackNode trackNode, boolean forwards) {
            this.trackNode = trackNode;
            this.forwards = forwards;
        }

        public TrackNode getTrackNode() {
            return trackNode;
        }

        public boolean isForwards() {
            return forwards;
        }
    }

    @Override
    public Route findRoute(TrackNode from, TrackNode to, TrackNetwork network) {
        Set<TrackNode> visitedNodes = new HashSet<>();
        Set<TrackNode> toVisitNodes = new HashSet<>(Arrays.asList(from));
        Map<TrackNode, DirectedNode> cameFrom = new HashMap<>();
        Map<TrackNode, Double> actualCostToNode = new HashMap<>();
        Map<TrackNode, Double> estimatedTotalCost = new HashMap<>();

        actualCostToNode.put(from, 0.0);
        estimatedTotalCost.put(from, actualCostToNode.get(from)+distanceBetween(from, to));

        while (!toVisitNodes.isEmpty()) {
            TrackNode current = toVisitNodes.stream().min((a, b) -> Double.compare(estimatedTotalCost.get(a), estimatedTotalCost.get(b))).get();

            if (current.equals(to)) {
                DirectedNode previous = cameFrom.get(to);
                boolean finalNodeForwards = current.getConnectedFrom().contains(previous.getTrackNode());
                return reconstructPath(cameFrom, new DirectedNode(to, finalNodeForwards));
            }

            toVisitNodes.remove(current);
            visitedNodes.add(current);

            visitNeighbours(to, visitedNodes, toVisitNodes, cameFrom, actualCostToNode, estimatedTotalCost, current, current.getConnectedTo(), true);
            visitNeighbours(to, visitedNodes, toVisitNodes, cameFrom, actualCostToNode, estimatedTotalCost, current,
                    current.getConnectedFrom(), false);
        }

        return null;
    }

    private void visitNeighbours(TrackNode to, Set<TrackNode> visitedNodes, Set<TrackNode> toVisitNodes, Map<TrackNode, DirectedNode> cameFrom, Map<TrackNode, Double> actualCostToNode, Map<TrackNode, Double> estimatedTotalCost, TrackNode current, Set<TrackNode> neighbours, boolean forwards) {
        for (TrackNode neighbour: neighbours) {
            if (visitedNodes.contains(neighbour)) {
                continue;
            }
            else {
                double tentativeActualCost = actualCostToNode.get(current) + current.getLength();

                if (!toVisitNodes.contains(neighbour) || tentativeActualCost < actualCostToNode.get(neighbour)) {
                    cameFrom.put(neighbour, new DirectedNode(current, forwards));
                    actualCostToNode.put(neighbour, tentativeActualCost);
                    estimatedTotalCost.put(neighbour, actualCostToNode.get(neighbour) + distanceBetween(neighbour, to));
                    if (!toVisitNodes.contains(neighbour)) {
                        toVisitNodes.add(neighbour);
                    }
                }
            }
        }
    }

    private Route reconstructPath(Map<TrackNode, DirectedNode> cameFrom, DirectedNode to) {
        Deque<RouteNode> path = new LinkedList<>();
        DirectedNode from = to;

        while (from != null) {
            path.push(new RouteNode(from.getTrackNode(), from.isForwards()));
            from = cameFrom.get(from.getTrackNode());
        }

        return new Route(path);
    }

    private double distanceBetween(TrackNode from, TrackNode to) {
        return Math.sqrt(Math.pow(Tile.WIDTH * (to.getX() - from.getX()), 2) + Math.pow(Tile.WIDTH * (to.getY()-from.getY()), 2) + Math.pow(Tile.WIDTH * (to.getZ()-from.getZ()), 2));
    }

}
