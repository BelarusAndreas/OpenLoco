package openloco.rail;

import openloco.graphics.CartCoord;
import openloco.graphics.Tile;
import openloco.routing.Route;

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
    }

    public void setHeadLocationToNode(TrackNode trackNode) {
        CartCoord cc = new CartCoord(Tile.WIDTH*trackNode.getX(), Tile.WIDTH*trackNode.getY(), Tile.WIDTH*trackNode.getZ());
        setHeadLocation(cc);
    }

    public void setHeadLocation(CartCoord cartCoord) {
        LOGGER.debug("Setting head location to {}", cartCoord);
        for (int i=0; i<consist.size(); i++) {
            RailVehicle vehicle = consist.get(i);
            int halfLength = vehicle.getHalfLength();

            LOGGER.debug("Setting location of vehicle {} ({}) to {} (halfLength {})", i, vehicle, cartCoord, halfLength);
            cartCoord = cartCoord.plus(0, halfLength, 0);
            vehicle.setLocation(cartCoord);
            cartCoord = cartCoord.plus(0, halfLength, 0);
        }
    }

    public List<RailVehicle> getRailVehicles() {
        return consist;
    }
}
