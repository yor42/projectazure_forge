package com.yor42.projectazure.libs;

import org.lwjgl.system.CallbackI;

import static com.yor42.projectazure.libs.utils.MathUtil.rollBooleanRNG;

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

    public enum ShipRarity{
        NORMAL,
        RARE,
        ELITE,
        SUPER_RARE,
        ULTRA_RARE
    }

    public enum AmmoTypes {
        //AP: Higher damage on kansen, lower chance to damage rigging
        //HE: Lower Damage on Kansen, higher chance to damage rigging
        //INC: mmmph mmph!
        //APHE:Bigger Damage on Kansen

        //Big Lead boi
        GENERIC(0.75F, 5, 0.4F, false, false),
        //What are you doing, step-shell?
        AP(0.9F,6, 0.3F, false, false),
        //haha Shell goes AW MAN
        HE(0.65F,10, 0.75F, true, false),
        //Ah thats hot
        INCENDIARY(0.73F, 7, 0.45F, false, true),
        //digs deep, and goes boom. does not do splash damage cuz it blows up inside of ship
        APHE(0.8F, 9, 0.5F, false, false),
        API(0.83F, 6, 0.15F, false, true),
        //HEI(0.68F, ),
        APHEI(0.78F, 8, 0.45F, false, true);

        private final float hitChance, damage, damagerigging;
        private boolean shouldDamageMultipleComponant, isIncendiary;
        AmmoTypes(float HitChance, float damage, float chanceRiggingDamage, boolean splashDamage, boolean incendiary) {
            this.hitChance = HitChance;
            this.damage = damage;
            this.damagerigging = chanceRiggingDamage;
        }

        public float getDamage() {
            return this.damage;
        }

        public float getHitchance(){
            return this.hitChance;
        }

        public boolean isHit(){
            return rollBooleanRNG(this.hitChance);
        }

        public boolean shouldDamageRigging(){
            return rollBooleanRNG(this.damagerigging);
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
