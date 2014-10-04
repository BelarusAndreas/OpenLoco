package openloco.rail;

import openloco.routing.Route;
import openloco.routing.RouteNodePosition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Train {

    private static final Logger LOGGER = LoggerFactory.getLogger(Train.class);

    private List<RailVehicle> consist = new ArrayList<>();
    private Route route;

    public void addVehicle(RailVehicle vehicle) {
        if (hasDualHeadLoco()) {
            consist.add(consist.size()-1, vehicle);
        }
        else {
            consist.add(vehicle);
            if (vehicle.getVehicleAsset().isDualHead()) {
                RailVehicle tail = new RailVehicle(vehicle.getVehicleAsset(), 0);
                tail.setDirection(180);
                consist.add(tail);
            }
        }
    }

    public boolean hasDualHeadLoco() {
        return !consist.isEmpty() && consist.get(0).getVehicleAsset().isDualHead();
    }

    public void setRoute(Route route) {
        this.route = route;
        RouteNodePosition position = route.getStart();

        for (int i=consist.size()-1; i>=0; i--) {
            RailVehicle vehicle = consist.get(i);
            int halfLength = vehicle.getHalfLength();
            position = position.moveAheadBy(halfLength);
            vehicle.setLocation(position.getCartCoord());
            position = position.moveAheadBy(halfLength);
        }
    }

    public List<RailVehicle> getRailVehicles() {
        return consist;
    }
}
