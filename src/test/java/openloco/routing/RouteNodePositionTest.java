package openloco.routing;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import openloco.rail.Orientation;
import openloco.rail.TrackLayer;
import openloco.rail.TrackNode;

public class RouteNodePositionTest {

    private List<TrackNode> trackNodes;
    private Route route;

    @Before
    public void setup() {
        TrackLayer trackLayer = new TrackLayer(20, 20, 0, Orientation.N);
        for (int i=0; i<10; i++) {
            trackLayer.addStraight();
        }
        trackNodes = trackLayer.getNodes();

        route = new Route(trackNodes.stream().map(RouteNode::new).collect(Collectors.toList()));
    }

    @Test(expected=IllegalArgumentException.class)
    public void moveBackwardsShouldThrowException() {
        RouteNodePosition position = new RouteNodePosition(route.getNodes().get(0), 10);
        position.moveAheadBy(-1);
    }

    @Test
    public void moveWithinSameRouteNode() {
        RouteNode routeNode = route.getNodes().get(0);
        RouteNodePosition position = new RouteNodePosition(routeNode, 10);
        RouteNodePosition updatedPosition = position.moveAheadBy(2);

        assertEquals("RouteNode is incorrect", routeNode, updatedPosition.getRouteNode());
        assertEquals("Position is incorrect", 12, updatedPosition.getPosition());
    }

    @Test
    public void moveToBoundary() {
        RouteNode routeNode = route.getNodes().get(0);
        RouteNodePosition position = new RouteNodePosition(routeNode, 10);
        RouteNodePosition updatedPosition = position.moveAheadBy(22);

        RouteNode node1 = route.getNodes().get(1);

        assertEquals("RouteNode is incorrect", node1, updatedPosition.getRouteNode());
        assertEquals("Position is incorrect", 0, updatedPosition.getPosition());
    }

    @Test
    public void skipSeveral() {
        RouteNode routeNode = route.getNodes().get(0);
        RouteNodePosition position = new RouteNodePosition(routeNode, 10);
        RouteNodePosition updatedPosition = position.moveAheadBy(167);

        RouteNode node5 = route.getNodes().get(5);

        assertEquals("RouteNode is incorrect", node5, updatedPosition.getRouteNode());
        assertEquals("Position is incorrect", 17, updatedPosition.getPosition());
    }

}
