package openloco.rail;

import openloco.assets.Vehicle;
import openloco.assets.VehicleUnit;
import openloco.assets.VehicleUnitSpriteDetails;
import openloco.graphics.CartCoord;

public class RailVehicle {

    private static final int COUPLING_PADDING = 1;

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

    public int getHalfLength() {
        int halfLength;
        Vehicle vehicleAsset = getVehicleAsset();
        VehicleUnit vehicleUnit = vehicleAsset.getVars().getVehicleUnits().get(getUnitIndex());
        VehicleUnitSpriteDetails spriteDetails = vehicleAsset.getVars().getVehicleUnitSpriteDetails().get(vehicleUnit.getSpriteDetailsIndex());
        if (vehicleUnit.isSpacingOnly()) {
            halfLength = 0;
        }
        else {
            halfLength = (int) Math.floor((float)(spriteDetails.getBogeyPos()-(vehicleUnit.getLength()/2-vehicleUnit.getRearBogeyPosition()/2))/ 4.0f) + COUPLING_PADDING;
        }
        return halfLength;
    }
}
