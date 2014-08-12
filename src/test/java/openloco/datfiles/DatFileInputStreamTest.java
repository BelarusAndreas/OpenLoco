package openloco.datfiles;

import openloco.assets.IndustryVars;
import openloco.assets.ObjectClass;
import openloco.assets.UseObject;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.EnumSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DatFileInputStreamTest {

    @Test
    public void testZeroWidthIsEmptyBitField() throws IOException {
        DatFileInputStream in = streamOf(new byte[] { 0, 0, 0 });
        EnumSet<IndustryVars.IndustryFlag> flags = in.readBitField(0, IndustryVars.IndustryFlag.class);
        assertEquals("Flags should be empty", flags.size(), 0);
        assertEquals("No bytes should have been consumed", 3, in.available());
    }

    @Test
    public void testFlagsSetCorrectly() throws IOException {
        DatFileInputStream in = streamOf(new byte[] { 1, 4, (byte)0b11110010, 0, 0});
        EnumSet<IndustryVars.IndustryFlag> flags = in.readBitField(3, IndustryVars.IndustryFlag.class);
        assertTrue("Flags should have contained 'unknown1'", flags.contains(IndustryVars.IndustryFlag.unknown1));
        assertTrue("Flags should have contained 'unknown12'", flags.contains(IndustryVars.IndustryFlag.unknown11));
        assertTrue("Flags should have contained 'needall'", flags.contains(IndustryVars.IndustryFlag.needall));
        assertEquals("Too many flags set", 3, flags.size());
        assertEquals("2 bytes should remain on the stream", 2, in.available());
    }

    @Test
    public void testBlankUseObjectReferences() throws IOException {
        DatFileInputStream in = streamOf(new byte[] {-1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        UseObject ref = in.readUseObject(EnumSet.allOf(ObjectClass.class));
        assertNull("Expected null UseObject reference when objectClassId is 0xFF", ref);
        assertEquals("Expected no bytes to remain on the stream", 0, in.available());
    }

    @Test(expected=IllegalArgumentException.class)
    public void checkIllegalArgExceptionWhenInvalidObjectClass() throws IOException {
        DatFileInputStream in = streamOf(new byte[] {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        in.readUseObject(EnumSet.noneOf(ObjectClass.class));
    }

    @Test
    public void checkValidObjectReference() throws IOException {
        DatFileInputStream in = streamOf(new byte[] {23, 0, 0, 0, 'H', 'S', 'T', '2', '0', '0', '0', ' ', 0, 0, 0, 0});
        UseObject ref = in.readUseObject(EnumSet.of(ObjectClass.VEHICLES));
        assertEquals("UseObject reference has wrong object class", ObjectClass.VEHICLES, ref.getObjectClass());
        assertEquals("UseObject reference contains wrong reference", "HST2000 ", ref.getObjectReference());
    }

    private DatFileInputStream streamOf(byte[] bytes) {
        return new DatFileInputStream(new ByteArrayInputStream(bytes));
    }
}
