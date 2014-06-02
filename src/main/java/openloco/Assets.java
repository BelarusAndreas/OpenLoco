package openloco;

import openloco.entities.*;

import java.util.HashMap;
import java.util.Map;

public class Assets {

    private final Map<String, Vehicle> vehicles = new HashMap<>();
    private final Map<String, Ground> grounds = new HashMap<>();
    private final Map<String, Company> companies = new HashMap<>();
    private final Map<String, CliffFace> cliffFaces = new HashMap<>();
    private final Map<String, Track> tracks = new HashMap<>();

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

    public void add(CliffFace cf) {
        cliffFaces.put(cf.getName(), cf);
    }

    public CliffFace getCliffFace(String name) {
        return cliffFaces.get(name);
    }

    public void add(Track track) {
        tracks.put(track.getName(), track);
    }

    public Track getTrack(String name) {
        return tracks.get(name);
    }
}
