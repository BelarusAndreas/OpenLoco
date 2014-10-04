package openloco.routing;

import openloco.graphics.Tile;
import openloco.rail.TrackNetwork;
import openloco.rail.TrackNode;

import java.util.*;

public class AStarRouteFinder implements RouteFinder {

    @Override
    public Route findRoute(TrackNode from, TrackNode to, TrackNetwork network) {
        Set<TrackNode> visitedNodes = new HashSet<>();
        Set<TrackNode> toVisitNodes = new HashSet<>(Arrays.asList(from));
        Map<TrackNode, TrackNode> cameFrom = new HashMap<>();
        Map<TrackNode, Double> actualCostToNode = new HashMap<>();
        Map<TrackNode, Double> estimatedTotalCost = new HashMap<>();

        actualCostToNode.put(from, 0.0);
        estimatedTotalCost.put(from, actualCostToNode.get(from)+distanceBetween(from, to));

        while (!toVisitNodes.isEmpty()) {
            TrackNode current = toVisitNodes.stream().min((a, b) -> Double.compare(estimatedTotalCost.get(a), estimatedTotalCost.get(b))).get();

            if (current.equals(to)) {
                return reconstructPath(cameFrom, to);
            }

            toVisitNodes.remove(current);
            visitedNodes.add(current);

            for (TrackNode neighbour: current.getNeighbours()) {
                if (visitedNodes.contains(neighbour)) {
                    continue;
                }
                else {
                    double tentativeActualCost = actualCostToNode.get(current) + current.getLength();

                    if (!toVisitNodes.contains(neighbour) || tentativeActualCost < actualCostToNode.get(neighbour)) {
                        cameFrom.put(neighbour, current);
                        actualCostToNode.put(neighbour, tentativeActualCost);
                        estimatedTotalCost.put(neighbour, actualCostToNode.get(neighbour) + distanceBetween(neighbour, to));
                        if (!toVisitNodes.contains(neighbour)) {
                            toVisitNodes.add(neighbour);
                        }
                    }
                }
            }
        }

        return null;
    }

    private Route reconstructPath(Map<TrackNode, TrackNode> cameFrom, TrackNode to) {
        Deque<RouteNode> path = new LinkedList<>();
        TrackNode from = to;

        while (from != null) {
            path.push(new RouteNode(from, true));
            from = cameFrom.get(from);
        }

        return new Route(path);
    }

    private double distanceBetween(TrackNode from, TrackNode to) {
        return Math.sqrt(Math.pow(Tile.WIDTH * (to.getX() - from.getX()), 2) + Math.pow(Tile.WIDTH * (to.getY()-from.getY()), 2) + Math.pow(Tile.WIDTH * (to.getZ()-from.getZ()), 2));
    }

}
