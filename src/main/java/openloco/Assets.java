package openloco;

import java.util.HashMap;
import java.util.Map;

public class Assets {

    private final Map<String, Vehicle> vehicles;
    private final Map<String, Ground> grounds;

    public Assets() {
        this.vehicles = new HashMap<>();
        this.grounds = new HashMap<>();
    }

    public void add(Vehicle vehicle) {
        vehicles.put(vehicle.getName(), vehicle);
    }

    public Vehicle getVehicle(String name) {
        return vehicles.get(name);
    }

    public void add(Ground ground) {
        grounds.put(ground.getName(), ground);
    }

    public Ground getGround(String name) {
        return grounds.get(name);
    }
}
