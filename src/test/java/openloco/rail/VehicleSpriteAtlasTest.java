package openloco.rail;

import openloco.assets.VehicleUnitSpriteDetails;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VehicleSpriteAtlasTest {

    private VehicleUnitSpriteDetails spriteDetails;
    private VehicleSpriteAtlas atlas;

    @Test
    public void testSymmtericalVehicle() {
        spriteDetails = mock(VehicleUnitSpriteDetails.class);
        when(spriteDetails.getLevelSpriteCount()).thenReturn((byte) 64);
        when(spriteDetails.isSymmetrical()).thenReturn(true);

        atlas = new VehicleSpriteAtlas();

        verifySpriteIndexForDirection(0, 0);
        verifySpriteIndexForDirection(89, 16);
        verifySpriteIndexForDirection(90, 16);
        verifySpriteIndexForDirection(91, 16);
        verifySpriteIndexForDirection(180, 0);
        verifySpriteIndexForDirection(270, 16);
    }

    private void verifySpriteIndexForDirection(int direction, int expectedIndex) {
        assertEquals("Wrong sprite index for " + direction + " degrees orientation", expectedIndex, atlas.getFlatSpriteIndex(spriteDetails, direction));
    }


}
