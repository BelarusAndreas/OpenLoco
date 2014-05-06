package openloco.datfiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CargoCapacity {

    private byte capacity;

    private List<CargoRefitCapacity> refitCapacities = new ArrayList<>();

    public CargoCapacity(DatFileInputStream in) {
        try {
            capacity = in.readByte();
            if (capacity != 0) {
                int term = in.readUShort();
                while (term != 0xFFFF) {
                    byte cargoType = (byte)(term & 0xFF);
                    int refCap = ((term >> 8) & 0xFF) | (in.readByte() << 8);
                    refitCapacities.add(new CargoRefitCapacity(cargoType, refCap));
                    term = in.readUShort();
                }
            }
        }
        catch (IOException ioe) {
            throw new RuntimeException("Could not parse cargo capacity", ioe);
        }
    }


    public class CargoRefitCapacity {

        private final byte cargoType;
        private final int refitCapacity;

        public CargoRefitCapacity(byte cargoType, int refitCapacity) {
            this.cargoType = cargoType;
            this.refitCapacity = refitCapacity;
        }

    }
}
