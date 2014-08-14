package openloco.rail;

import openloco.assets.Assets;
import openloco.assets.Sprites;
import openloco.assets.Vehicle;
import openloco.assets.VehicleUnitSpriteDetails;
import openloco.graphics.*;

import java.util.ArrayList;
import java.util.List;

public class TrainRenderer implements Renderer<Train> {

    private static final int BOGEY_FUDGE_FACTOR = -5;

    private final Assets assets;
    private final VehicleSpriteAtlas vehicleSpriteAtlas;

    public TrainRenderer(Assets assets) {
        this.assets = assets;
        vehicleSpriteAtlas = new VehicleSpriteAtlas();
    }

    @Override
    public List<SpriteInstance> render(Train train) {
        List<SpriteInstance> sprites = new ArrayList<>();

        for (int i=0; i<train.getRailVehicles().size(); i++) {
            RailVehicle railVehicle = train.getRailVehicles().get(i);
            Vehicle vehicleAsset = railVehicle.getVehicleAsset();
            VehicleUnitSpriteDetails vehSpriteVar = vehicleAsset.getVars().getVehicleUnitSpriteDetails().get(0);

            sprites.add(drawBogeyAt(railVehicle, vehSpriteVar, railVehicle.getLocation().plus(0, vehSpriteVar.getBogeyPos()/8, 0)));
            sprites.add(drawBogeyAt(railVehicle, vehSpriteVar, railVehicle.getLocation().plus(0, BOGEY_FUDGE_FACTOR -vehSpriteVar.getBogeyPos()/8, 0)));

            int spriteIndex = vehicleSpriteAtlas.getFlatSpriteIndex(vehSpriteVar, railVehicle.getDirection());
            Sprites.RawSprite rawSprite = vehicleAsset.getSprites().get(spriteIndex);
            sprites.add(new SpriteInstance(OpenGlSprite.createFromRawSprite(rawSprite), SpriteLayer.VEHICLES, railVehicle.getLocation().plus(0, 0, 2)));
        }

        return sprites;
    }

    private SpriteInstance drawBogeyAt(RailVehicle railVehicle, VehicleUnitSpriteDetails vehSpriteVar, CartCoord bogey1Loc) {
        int bogeySpriteIndex = vehicleSpriteAtlas.getBogeySpriteIndex(vehSpriteVar, railVehicle.getDirection());
        Sprites.RawSprite bogeyRawSprite = railVehicle.getVehicleAsset().getSprites().get(bogeySpriteIndex);
        return new SpriteInstance(OpenGlSprite.createFromRawSprite(bogeyRawSprite), SpriteLayer.VEHICLES, bogey1Loc);
    }

}
