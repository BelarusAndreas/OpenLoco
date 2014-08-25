package openloco.rail;

import com.google.common.collect.Lists;
import openloco.assets.Vehicle;
import openloco.assets.VehicleUnit;
import openloco.assets.VehicleUnitSpriteDetails;
import openloco.assets.VehicleVars;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VehicleSpriteAtlasTest {

    private VehicleUnitSpriteDetails spriteDetails;
    private VehicleVars vehicleVars;
    private Vehicle vehicle;
    private VehicleSpriteAtlas atlas;
    private VehicleUnit vehicleUnit;

    @Test
    public void testSymmtericalVehicle() {
        spriteDetails = mock(VehicleUnitSpriteDetails.class);
        when(spriteDetails.getLevelSpriteCount()).thenReturn((byte) 64);
        when(spriteDetails.isSymmetrical()).thenReturn(true);
        when(spriteDetails.hasSprites()).thenReturn(true);
        when(spriteDetails.getFrames()).thenReturn((byte) 4);
        when(spriteDetails.getTiltCount()).thenReturn((byte) 1);

        vehicleUnit = mock(VehicleUnit.class);

        vehicleVars = mock(VehicleVars.class);
        when(vehicleVars.getVehicleUnitSpriteDetails()).thenReturn(Lists.newArrayList(spriteDetails));
        when(vehicleVars.getVehicleUnits()).thenReturn(Lists.newArrayList(vehicleUnit));

        vehicle = mock(Vehicle.class);
        when(vehicle.getVars()).thenReturn(vehicleVars);

        atlas = new VehicleSpriteAtlas();

        verifySpriteIndexForDirection(0, 0);
        verifySpriteIndexForDirection(89, 16);
        verifySpriteIndexForDirection(90, 16);
        verifySpriteIndexForDirection(91, 16);
        verifySpriteIndexForDirection(180, 0);
        verifySpriteIndexForDirection(270, 16);
    }

    private void verifySpriteIndexForDirection(int direction, int expectedIndex) {
        assertEquals("Wrong sprite index for " + direction + " degrees orientation", expectedIndex, atlas.getFlatSpriteIndex(vehicle, 0, direction));
    }


}
