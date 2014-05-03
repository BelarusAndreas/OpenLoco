package openloco;

import java.util.HashMap;
import java.util.Map;

public class Assets {

    private final Map<String, Vehicle> vehicles;

    public Assets() {
        this.vehicles = new HashMap<>();
    }

    public void add(Vehicle vehicle) {
        vehicles.put(vehicle.getName(), vehicle);
    }

}
