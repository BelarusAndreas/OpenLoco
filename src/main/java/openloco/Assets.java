package openloco;

import openloco.entities.Company;
import openloco.entities.Ground;
import openloco.entities.Vehicle;

import java.util.HashMap;
import java.util.Map;

public class Assets {

    private final Map<String, Vehicle> vehicles = new HashMap<>();
    private final Map<String, Ground> grounds = new HashMap<>();
    private final Map<String, Company> companies = new HashMap<>();

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

    public void add(Company company) {
        companies.put(company.getName(), company);
    }

}
