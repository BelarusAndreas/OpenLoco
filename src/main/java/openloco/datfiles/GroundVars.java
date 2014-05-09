package openloco.datfiles;

import java.io.IOException;

public class GroundVars {

    private byte costInd;
    private short costFactor;

    public GroundVars(DatFileInputStream in) {
        try {
            in.skipBytes(2);
            costInd = in.readByte();
            in.skipBytes(5);
            costFactor = in.readSShort();
            in.skipBytes(20);
        }
        catch (IOException ioe) {
            throw new RuntimeException("Unable to parse ground vars", ioe);
        }
    }

    public byte getCostInd() {
        return costInd;
    }

    public short getCostFactor() {
        return costFactor;
    }
}
