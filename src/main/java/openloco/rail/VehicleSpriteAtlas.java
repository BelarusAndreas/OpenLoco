package openloco.rail;

import openloco.assets.VehicleSpriteVar;

public class VehicleSpriteAtlas {

    public int getFlatSpriteIndex(VehicleSpriteVar vehicleSpriteVar, int direction) {
        return direction*vehicleSpriteVar.getLevelSpriteCount()/360;
    }

    public int getBogeySpriteIndex(VehicleSpriteVar vehicleSpriteVar, int direction) {
        return getLevelSpriteCount(vehicleSpriteVar) + getUpDownSpriteCount(vehicleSpriteVar) + getHalfSlopeSpriteCount(vehicleSpriteVar);
    }

    private int getLevelSpriteCount(VehicleSpriteVar vehicleSpriteVar) {
        int levelSpriteCount = vehicleSpriteVar.getLevelSpriteCount() * vehicleSpriteVar.getFrames();
        if (vehicleSpriteVar.isSymmetrical()) {
            levelSpriteCount/=2;
        }
        return levelSpriteCount;
    }

    private int getUpDownSpriteCount(VehicleSpriteVar vehicleSpriteVar) {
        int upDownSpriteCount = 2 * vehicleSpriteVar.getUpDownSpriteCount() * vehicleSpriteVar.getFrames();
        if (vehicleSpriteVar.isSymmetrical()) {
            upDownSpriteCount/=2;
        }
        return upDownSpriteCount;
    }

    private int getHalfSlopeSpriteCount(VehicleSpriteVar vehicleSpriteVar) {
        int halfSlopeCount = 8;
        if (vehicleSpriteVar.isSymmetrical()) {
            halfSlopeCount/=2;
        }
        return halfSlopeCount;
    }
}
