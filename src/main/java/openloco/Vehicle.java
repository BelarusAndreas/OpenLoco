package openloco;

import openloco.datfiles.*;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class Vehicle {

    private final String name;
    private final MultiLangString description;
    private final VehicleVars vars;
    private final UseObject trackType;
    private final List<CargoCapacity> cargoCapacities;
    private final UseObject visualFx;
    private final UseObject wakeFx;
    private final UseObject rackRail;
    private final UseObject startSound;
    private final List<UseObject> sounds;
    private final Sprites sprites;

    public Vehicle(String name, MultiLangString description, VehicleVars vars, UseObject trackType,
                   List<CargoCapacity> cargoCapacities, UseObject visualFx, UseObject wakeFx, UseObject rackRail,
                   UseObject startSound, List<UseObject> sounds, Sprites sprites) {

        this.name = name;
        this.description = description;
        this.vars = vars;
        this.trackType = trackType;
        this.cargoCapacities = cargoCapacities;
        this.visualFx = visualFx;
        this.wakeFx = wakeFx;
        this.rackRail = rackRail;
        this.startSound = startSound;
        this.sounds = sounds;
        this.sprites = sprites;
    }

    public String getName() {
        return name;
    }

    public VehicleVars getVars() {
        return vars;
    }

    public Sprites getSprites() {
        return sprites;
    }

    public static Vehicle load(String name, DatFileInputStream inputStream) {
        VehicleVars vars = new VehicleVars(inputStream);
        MultiLangString description = new MultiLangString(inputStream);
        UseObject trackType = null;
        if (vars.getVehicleClass() < 2 && !vars.getVehicleFlags().contains(VehicleVars.VehicleFlag.ANYTRACK)) {
            trackType = new UseObject(inputStream, EnumSet.of(ObjectClass.TRACKS, ObjectClass.ROADS));
        }
        //optional reference to track/road modification?
        List<UseObject> trackModifications = new ArrayList<>();
        for (int i=0; i<vars.getNumMods(); i++) {
            trackModifications.add(new UseObject(inputStream, EnumSet.of(ObjectClass.TRACK_MODIFICATIONS, ObjectClass.ROAD_MODIFICATIONS)));
        }

        List<CargoCapacity> cargoCapacities = new ArrayList<>();
        for (int i=0; i<2; i++) {
            cargoCapacities.add(new CargoCapacity(inputStream));
        }

        UseObject visualFx = null;
        if (vars.getVisFxType() != 0) {
            visualFx = new UseObject(inputStream, EnumSet.of(ObjectClass.EXHAUST_EFFECTS));
        }

        UseObject wakeFx = null;
        if (vars.getWakeFxType() != 0) {
            wakeFx = new UseObject(inputStream, EnumSet.of(ObjectClass.EXHAUST_EFFECTS));
        }

        UseObject rackRail = null;
        if (vars.getVehicleClass() < 2 && vars.getVehicleFlags().contains(VehicleVars.VehicleFlag.RACKRAIL)) {
            rackRail = new UseObject(inputStream, EnumSet.of(ObjectClass.TRACK_MODIFICATIONS));
        }

        int numCompat = vars.getNumCompat();
        List<UseObject> numCompats = new ArrayList<>();
        for (int i=0; i<numCompat; i++) {
            numCompats.add(new UseObject(inputStream, EnumSet.of(ObjectClass.VEHICLES)));
        }

        UseObject startSnd = null;
        if (vars.getStartsndtype() != 0) {
            startSnd = new UseObject(inputStream, EnumSet.of(ObjectClass.SOUND_EFFECTS));
        }

        List<UseObject> sounds = new ArrayList<>();
        int soundCount = vars.getNumSnd() & 0x7f;
        for (int i=0; i<soundCount; i++) {
            sounds.add(new UseObject(inputStream, EnumSet.of(ObjectClass.SOUND_EFFECTS)));
        }

        Sprites s = new Sprites(inputStream);

        return new Vehicle(name, description, vars, trackType, cargoCapacities, visualFx, wakeFx, rackRail, startSnd, sounds, s);
    }

}
