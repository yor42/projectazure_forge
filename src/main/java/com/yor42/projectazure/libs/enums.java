package com.yor42.projectazure.libs;

public class enums {

    public enum SLOTTYPE{
        GUN,
        TORPEDO,
        UTILITY,
        AA
    }

    public enum shipClass {
        Destroyer("destroyer"),
        LightCruiser("light_cruiser"),
        HeavyCruiser("heavy_cruiser"),
        LargeCruiser("large_cruiser"),
        Battleship("battleship"),
        AircraftCarrier("aircraft_carrier"),
        LightAircraftCarrier("light_aircraft_carrier"),
        Submarine("submarine"),
        SubmarineCarrier("submarine_carrier"),
        MonitorShip("monitor"),
        Repair("repair");

        private final String name;
        shipClass(String name) {
            this.name = name;
        }

        public String getName(){
            return this.name;
        }
    }

}
