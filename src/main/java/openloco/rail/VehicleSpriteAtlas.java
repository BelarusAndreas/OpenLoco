package openloco.rail;

import openloco.assets.VehicleUnitSpriteDetails;

public class VehicleSpriteAtlas {

    public int getFlatSpriteIndex(VehicleUnitSpriteDetails vehicleUnitSpriteDetails, int direction) {
        if (vehicleUnitSpriteDetails.isSymmetrical()) {
            direction %= 180;
        }
        return vehicleUnitSpriteDetails.getFrames() * getSpriteIndex(direction, vehicleUnitSpriteDetails.getLevelSpriteCount());
    }

    public int getBogeySpriteIndex(VehicleUnitSpriteDetails vehicleUnitSpriteDetails, int direction) {
        direction %= 180;
        return getLevelSpriteCount(vehicleUnitSpriteDetails) + getUpDownSpriteCount(vehicleUnitSpriteDetails) + getHalfSlopeSpriteCount(vehicleUnitSpriteDetails) + getSpriteIndex(direction, 32);
    }

    private int getSpriteIndex(int direction, int spriteCount) {
        return (int)Math.floor(spriteCount * ((double) (direction % 360) + 0.5 * 360.0/ spriteCount) / 360.0);
    }

    private int getLevelSpriteCount(VehicleUnitSpriteDetails vehicleUnitSpriteDetails) {
        int levelSpriteCount = vehicleUnitSpriteDetails.getLevelSpriteCount() * vehicleUnitSpriteDetails.getFrames();
        if (vehicleUnitSpriteDetails.isSymmetrical()) {
            levelSpriteCount/=2;
        }
        return levelSpriteCount * vehicleUnitSpriteDetails.getCargoLoadingFrames();
    }

    private int getUpDownSpriteCount(VehicleUnitSpriteDetails vehicleUnitSpriteDetails) {
        int upDownSpriteCount = 2 * vehicleUnitSpriteDetails.getUpDownSpriteCount() * vehicleUnitSpriteDetails.getFrames();
        if (vehicleUnitSpriteDetails.isSymmetrical()) {
            upDownSpriteCount/=2;
        }
        return upDownSpriteCount * vehicleUnitSpriteDetails.getCargoLoadingFrames();
    }

    private int getHalfSlopeSpriteCount(VehicleUnitSpriteDetails vehicleUnitSpriteDetails) {
        int halfSlopeCount = 8;
        if (vehicleUnitSpriteDetails.isSymmetrical()) {
            halfSlopeCount/=2;
        }
        return halfSlopeCount * vehicleUnitSpriteDetails.getCargoLoadingFrames();
    }
}
