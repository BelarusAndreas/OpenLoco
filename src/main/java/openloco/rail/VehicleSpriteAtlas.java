package openloco.rail;

import openloco.assets.Vehicle;
import openloco.assets.VehicleUnit;
import openloco.assets.VehicleUnitSpriteDetails;

import java.util.function.*;

public class VehicleSpriteAtlas {

    public int getFlatSpriteIndex(Vehicle vehicleAsset, int unitId, int direction) {
        VehicleUnit vehicleUnit = vehicleAsset.getVars().getVehicleUnits().get(unitId);
        VehicleUnitSpriteDetails vehicleUnitSpriteDetails = vehicleAsset.getVars().getVehicleUnitSpriteDetails().get(vehicleUnit.getSpriteDetailsIndex());
        if (vehicleUnit.isSpriteDetailsReversed()) {
            direction += 180;
        }
        if (vehicleUnitSpriteDetails.isSymmetrical()) {
            direction %= 180;
        }

        int unitStart = 0;
        for (int i=vehicleUnit.getSpriteDetailsIndex()-1; i>=0; i--) {
            VehicleUnitSpriteDetails previousUnit = vehicleAsset.getVars().getVehicleUnitSpriteDetails().get(i);
            unitStart += getSpriteCountForUnit(previousUnit);
        }

        return unitStart + vehicleUnitSpriteDetails.getCargoLoadingFrames() * vehicleUnitSpriteDetails.getFrames() * vehicleUnitSpriteDetails.getTiltCount() * getSpriteIndex(direction, vehicleUnitSpriteDetails.getLevelSpriteCount());
    }

    public int getBogeySpriteIndex(Vehicle vehicleAsset, int unitId, int direction) {
        VehicleUnitSpriteDetails vehicleUnitSpriteDetails = vehicleAsset.getVars().getVehicleUnitSpriteDetails().get(unitId);
        direction %= 180;
        return getLevelSpriteCount(vehicleAsset) + getUpDownSpriteCount(vehicleAsset) + getHalfSlopeSpriteCount(vehicleAsset) + getSpriteIndex(direction, 32);
    }

    private int getSpriteIndex(int direction, int spriteCount) {
        return (int)Math.floor(spriteCount * ((double) (direction % 360) + 0.5 * 360.0/ spriteCount) / 360.0);
    }

    private int getLevelSpriteCount(Vehicle vehicleAsset) {
        return getTotalSpriteCount(vehicleAsset, (vu) -> vu.getLevelSpriteCount() * vu.getFrames());
    }

    private int getUpDownSpriteCount(Vehicle vehicleAsset) {
        return getTotalSpriteCount(vehicleAsset, (vu) -> 2 * vu.getUpDownSpriteCount() * vu.getFrames());
    }

    private int getHalfSlopeSpriteCount(Vehicle vehicleAsset) {
        return getTotalSpriteCount(vehicleAsset, (vu) -> 8);
    }

    private int getTotalSpriteCount(Vehicle vehicleAsset, ToIntFunction<VehicleUnitSpriteDetails> countSupplier) {
        int total = 0;

        for (VehicleUnitSpriteDetails spriteDetails: vehicleAsset.getVars().getVehicleUnitSpriteDetails()) {
            if (spriteDetails.hasSprites()) {
                int spriteCount = countSupplier.applyAsInt(spriteDetails);
                if (spriteDetails.isSymmetrical()) {
                    spriteCount /= 2;
                }
                total += spriteCount * spriteDetails.getTiltCount() * spriteDetails.getCargoLoadingFrames();
            }
        }

        return total;
    }

    private int getSpriteCountForUnit(VehicleUnitSpriteDetails spriteDetails) {
        if (spriteDetails.hasSprites()) {
            int spriteCount = spriteDetails.getCargoLoadingFrames()
                    * spriteDetails.getFrames()
                    * spriteDetails.getTiltCount()
                    * (spriteDetails.getLevelSpriteCount() + 2 * spriteDetails.getUpDownSpriteCount() + 8);
            if (spriteDetails.isSymmetrical()) {
                spriteCount/=2;
            }
            return spriteCount;
        }
        else {
            return 0;
        }
    }
}
