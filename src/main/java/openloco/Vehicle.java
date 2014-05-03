package openloco;

import openloco.datfiles.DatFileInputStream;
import openloco.datfiles.VehicleVars;

public class Vehicle {

    private final String name;
    private final VehicleVars vars;

    public Vehicle(String name, VehicleVars vars) {
        this.name = name;
        this.vars = vars;
    }

    public String getName() {
        return name;
    }

    public static Vehicle load(String name, DatFileInputStream inputStream) {
        VehicleVars vars = new VehicleVars(inputStream);
        return new Vehicle(name, vars);
    }

}
