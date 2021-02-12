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

    public enum AmmoTypes {
        GENERIC(0),
        AP(1),
        HE(2),
        INCENDIARY(3),
        API(4),
        APHEI(5);

        private final int index;

        AmmoTypes(int index) {
            this.index = index;
        }

        public int getIndex() {
            return this.index;
        }
    }

    public static int getAmmotypeCount(){

        int i = AmmoTypes.values().length;
        return i;
    }

    public enum Affection{
        DISAPPOINTED("disappointed"),
        STRANGER("stranger"),
        FRIENDLY("friendly"),
        CRUSH("crush"),
        LOVE("love"),
        OATH("oath");

        private final String name;
        Affection(String name) {
            this.name = name;
        }

        public String getName(){
            return this.name;
        }

    }

}
