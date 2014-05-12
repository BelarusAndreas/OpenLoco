package openloco.entities;

import java.util.ArrayList;
import java.util.List;

public class CargoCapacity {

    private byte capacity;
    private List<CargoRefitCapacity> refitCapacities = new ArrayList<>();

    public CargoCapacity(byte capacity, List<CargoRefitCapacity> refitCapacities) {
        this.capacity = capacity;
        this.refitCapacities = refitCapacities;
    }

    public static class CargoRefitCapacity {

        private final byte cargoType;
        private final int refitCapacity;

        public CargoRefitCapacity(byte cargoType, int refitCapacity) {
            this.cargoType = cargoType;
            this.refitCapacity = refitCapacity;
        }

    }
}
