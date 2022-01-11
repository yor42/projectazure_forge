package com.yor42.projectazure.libs;

import com.yor42.projectazure.gameobject.items.shipEquipment.ItemEquipmentBase;
import net.minecraft.item.ItemStack;

import static com.yor42.projectazure.libs.utils.MathUtil.rand;

public class enums {

    public enum SLOTTYPE{
        MAIN_GUN("equiment.main_gun"),
        SUB_GUN("equiment.sub_gun"),
        AA("equiment.antiair"),
        TORPEDO("equiment.torpedo"),
        PLANE("equiment.plane"),
        UTILITY("equipment.utility");

        private final String name;
        SLOTTYPE(String name) {
            this.name = name;
        }

        public String getName(){
            return this.name;
        }

        public boolean testPredicate(ItemStack stack){
            return stack.getItem() instanceof ItemEquipmentBase && ((ItemEquipmentBase)stack.getItem()).getSlot() == this;
        }

    }

    public enum MELEE_ATTACK_TYPE{
        KNOCKBACK,
        DAMAGE
    }

    public enum PLANE_TYPE{
        FIGHTER("plane.fighter"),
        TORPEDO_BOMBER("plane.torpedo_bomber"),
        DIVE_BOMBER("plane.dive_bomber");

        private final String name;
        PLANE_TYPE(String name) {
            this.name = name;
        }

        public String getName(){
            return this.name;
        }
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

    public enum CompanionRarity {
        STAR_1("rarity.c", 0xadadad),
        STAR_2("rarity.uc", 0x007eff),
        STAR_3("rarity.r", 0xb4ff00),
        STAR_4("rarity.sr", 0x7200ff),
        STAR_5("rarity.ur", 0xffcc00),
        STAR_6("rarity.leg", 0xff6db5),
        SPECIAL("rarity_sp", 0x00fcff);

        private final String translationkey;
        private final int rarityColor;
        CompanionRarity(String name, int rarityColor) {
            this.translationkey = name;
            this.rarityColor = rarityColor;
        }

        public String getTranslationkey(){
            return this.translationkey;
        }

        public int getColor(){
            return this.rarityColor;
        }
    }

    public enum AmmoCategory {
        //.... and fallback values
        //AP: Higher damage on kansen, lower chance to damage rigging
        //HE: Lower Damage on Kansen, higher chance to damage rigging
        //INC: *pyro noise*
        //APHE:Bigger Damage on Kansen

        //Big Lead boi
        GENERIC(0.75F, 5, 1, 1, 0.8F, false, false),
        //What are you doing, step-shell? ~~I'm sorry~~
        AP(0.9F,7, 2,4, 0.3F,false, false),
        //haha Shell goes AW MAN
        HE(0.65F,2, 1, 6, 0.8F, false, true),
        //Ah thats hot
        INCENDIARY(0.73F, 6, 3F,2, 0.8F, true, false),
        //digs deep, and goes boom. does not do splash damage cuz it blows up inside of ship
        SAP(0.78F, 9, 4, 2,0.8F, false, false),
        API(0.8F, 6, 3, 3,0.6F, false, true),
        //HEI(0.68F, ),
        HEIAP(0.78F, 8, 4F, 4F, 0.6F, false,true);

        private final float damage_rigging, damage_entity, damage_component, hitChance, minimum_damage_modifier;
        private final boolean shouldDamageMultipleComponant;
        private final boolean isIncendiary;
        AmmoCategory(float HitChance, float riggingDamage, float entityDamage, float componentDamage, float MinimumDamageModifier, boolean isFiery, boolean splashDamage) {
            this.hitChance = HitChance;
            this.damage_rigging = riggingDamage;
            this.damage_entity = entityDamage;
            this.damage_component = componentDamage;
            this.minimum_damage_modifier = MinimumDamageModifier;
            this.isIncendiary = isFiery;
            this.shouldDamageMultipleComponant = splashDamage;
        }

        public float getDamage(DamageType type) {
            switch (type){
                default:
                    return this.damage_rigging*getDamageVariation();
                case ENTITY:
                    return this.damage_entity*getDamageVariation();
                case COMPONENT:
                    return this.damage_component*getDamageVariation();
            }
        }

        private float getDamageVariation(){
            return (rand.nextFloat()*(1-this.minimum_damage_modifier)+this.minimum_damage_modifier);
        }

        public boolean ShouldDamageMultipleComponent(){
            return this.shouldDamageMultipleComponant;
        }

        public boolean isFiery(){
            return this.isIncendiary;
        }

        public float getRawComponentDamage() {
            return damage_component;
        }

        public float getRawEntityDamage() {
            return damage_entity;
        }

        public float getRawRiggingDamage() {
            return damage_rigging;
        }

        public float getRawhitChance() {
            return hitChance;
        }

        public float getRawDamageModifer() {
            return minimum_damage_modifier;
        }

    }

    public enum CanonSize{
        LARGE,
        MEDIUM,
        SMALL
    }

    public enum OperatorClass{
        VANGUARD("operator.vanguard"),
        SNIPER("operator.sniper"),
        DEFENDER("operator.defender"),
        CASTER("operator.caster"),
        GUARD("operator.guard"),
        MEDIC("operator.medic"),
        SPECIALIST("operator.specialist"),
        SUPPORTER("operator.supporter"),
        REUNION("operator.reunion");

        private final String name;
        OperatorClass(String name) {
            this.name = name;
        }

        public String getName(){
            return this.name;
        }
    }

    public enum GunClass {
        NONE("guntype.none"),
        AR("guntype.assault_rifle"),
        SG("guntype.shotgun"),
        HG("guntype.handgun"),
        SR("guntype.sniper");

        private final String name;
        GunClass(String name) {
            this.name = name;
        }

        public String getName(){
            return this.name;
        }
    }

    public enum DamageType{
        RIGGING,
        ENTITY,
        COMPONENT
    }

    public enum EntityType{
        KANSEN("al_kansen"),
        KANMUSU("kc_kanmusu"),
        TDOLL("gfl_tdoll"),
        BLUEARCHIVE("bluearchive"),
        OPERATOR("akn_operator"),
        ELEMENTS("gi_elements"),
        REUNION("akn_reunion"),
        SERVANT("fgo_servant");

        private final String name;
        EntityType(String name) {
            this.name = name;
        }

        public String getName(){
            return this.name;
        }
    }

    public enum ALAffection {
        DISAPPOINTED("affection.disappointed"),
        STRANGER("affection.stranger"),
        FRIENDLY("affection.friendly"),
        CRUSH("affection.crush"),
        LOVE("affection.love"),
        OATH("affection.oath");

        private final String name;
        ALAffection(String name) {
            this.name = name;
        }

        public String getName(){
            return this.name;
        }

    }

    public enum AmmoCalibur{
        AMMO_5_56,
        AMMO_45,
        AMMO_9MM,
        TORPEDO,
        DRONE_MISSILE
    }

    public enum Morale{
        REALLY_HAPPY("morale.really_happy"),
        HAPPY("morale.happy"),
        NEUTRAL("morale.neutral"),
        TIRED("morale.tired"),
        EXHAUSTED("morale.exhausted");

        private final String name;

        Morale(String nameKey){
            this.name = nameKey;
        }

        public String getName() {
            return this.name;
        }
    }

    public enum Material{
        COPPER("copper"),
        TIN("tin"),
        BRONZE("bronze");

        private final String name;

        Material(String name){
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    public enum ResourceType {
        GEAR("gear"),
        INGOT("ingot"),
        PLATE("plate"),
        DUST("dust"),
        WIRE("wire"),
        ORE("ore"),
        NUGGET("nugget");

        private final String name;

        ResourceType(String name){
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }


}
