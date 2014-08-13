package openloco.rail;

import openloco.assets.Vehicle;
import openloco.graphics.CartCoord;

public class RailVehicle {

    private final Vehicle vehicleAsset;
    private CartCoord location;
    private int direction = 0;

    public RailVehicle(Vehicle vehicleAsset) {
        this.vehicleAsset = vehicleAsset;
    }

    public Vehicle getVehicleAsset() {
        return vehicleAsset;
    }

    public void setLocation(CartCoord location) {
        this.location = location;
    }

    public CartCoord getLocation() {
        return location;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }
}
