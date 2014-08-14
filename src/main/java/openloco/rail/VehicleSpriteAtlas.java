package openloco.rail;

import openloco.assets.VehicleUnitSpriteDetails;

public class VehicleSpriteAtlas {

    public int getFlatSpriteIndex(VehicleUnitSpriteDetails vehicleUnitSpriteDetails, int direction) {
        return direction* vehicleUnitSpriteDetails.getLevelSpriteCount()/360;
    }

    public int getBogeySpriteIndex(VehicleUnitSpriteDetails vehicleUnitSpriteDetails, int direction) {
        return getLevelSpriteCount(vehicleUnitSpriteDetails) + getUpDownSpriteCount(vehicleUnitSpriteDetails) + getHalfSlopeSpriteCount(vehicleUnitSpriteDetails);
    }

    private int getLevelSpriteCount(VehicleUnitSpriteDetails vehicleUnitSpriteDetails) {
        int levelSpriteCount = vehicleUnitSpriteDetails.getLevelSpriteCount() * vehicleUnitSpriteDetails.getFrames();
        if (vehicleUnitSpriteDetails.isSymmetrical()) {
            levelSpriteCount/=2;
        }
        return levelSpriteCount;
    }

    private int getUpDownSpriteCount(VehicleUnitSpriteDetails vehicleUnitSpriteDetails) {
        int upDownSpriteCount = 2 * vehicleUnitSpriteDetails.getUpDownSpriteCount() * vehicleUnitSpriteDetails.getFrames();
        if (vehicleUnitSpriteDetails.isSymmetrical()) {
            upDownSpriteCount/=2;
        }
        return upDownSpriteCount;
    }

    private int getHalfSlopeSpriteCount(VehicleUnitSpriteDetails vehicleUnitSpriteDetails) {
        int halfSlopeCount = 8;
        if (vehicleUnitSpriteDetails.isSymmetrical()) {
            halfSlopeCount/=2;
        }
        return halfSlopeCount;
    }
}
