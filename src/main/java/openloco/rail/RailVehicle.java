package openloco.rail;

import openloco.assets.Vehicle;
import openloco.graphics.CartCoord;

public class RailVehicle {

    private final Vehicle vehicleAsset;
    private final int unitIndex;

    private CartCoord location;
    private int direction = 0;

    public RailVehicle(Vehicle vehicleAsset, int unitIndex) {
        this.vehicleAsset = vehicleAsset;
        this.unitIndex = unitIndex;
    }

    public Vehicle getVehicleAsset() {
        return vehicleAsset;
    }

    public int getUnitIndex() {
        return unitIndex;
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
