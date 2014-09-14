package openloco.routing;

import openloco.rail.Route;
import openloco.rail.TrackNetwork;
import openloco.rail.TrackNode;

public interface RouteFinder {

    Route findRoute(TrackNode from, TrackNode to, TrackNetwork network);

}
