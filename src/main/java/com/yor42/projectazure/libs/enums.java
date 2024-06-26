package com.yor42.projectazure.libs;

import com.mojang.datafixers.util.Function4;
import com.tac.guns.common.Gun;
import com.tac.guns.item.AmmoItem;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.containers.entity.*;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenBase;
import com.yor42.projectazure.gameobject.items.ItemCannonshell;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemEquipmentBase;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SoundType;

import javax.annotation.Nullable;
import java.util.function.Predicate;

import static com.yor42.projectazure.libs.utils.MathUtil.rand;

public class enums {

    public enum SLOTTYPE{
        MAIN_GUN("maingun"),
        SUB_GUN("subgun"),
        AA("antiair"),
        TORPEDO("torpedo"),
        PLANE("plane"),
        UTILITY("utility"),
        STORAGE("storage",(stack)->true),
        AMMO("ammo", (stack)-> stack.getItem() instanceof AmmoItem || stack.getItem() instanceof ItemCannonshell);
        ;

        private final String name;
        @Nullable
        private final Predicate<ItemStack> slotPredicate;

        SLOTTYPE(String name, @Nullable Predicate<ItemStack> slotPredicate) {
            this.name = name;
            this.slotPredicate = slotPredicate;
        }

        SLOTTYPE(String name) {
            this.name = name;
            this.slotPredicate = null;
        }

        public String getName(){
            return this.name;
        }

        public boolean testPredicate(ItemStack stack){
            if(slotPredicate != null){
                return slotPredicate.test(stack);
            }
            return stack.getItem() instanceof ItemEquipmentBase && ((ItemEquipmentBase)stack.getItem()).getSlot() == this;
        }

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

    public enum shipClass implements Predicate<AbstractEntityCompanion> {
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
        Repair("repair"),
        ALLSHIP("allship"),
        ANYCOMPANION("any");

        private final String name;
        shipClass(String name) {
            this.name = name;
        }

        public String getName(){
            return this.name;
        }

        @Override
        public boolean test(AbstractEntityCompanion livingEntity) {

            if(!(livingEntity instanceof EntityKansenBase)){
                return this == ANYCOMPANION;
            }
            else if(this == ALLSHIP){
                return true;
            }

            return ((EntityKansenBase) livingEntity).getShipClass() == this;
        }
    }

    public enum CompanionRarity {
        STAR_1("rarity.c", 0xadadad, PAConfig.CONFIG.Star_1_Chance.get()),
        STAR_2("rarity.uc", 0x007eff, PAConfig.CONFIG.Star_2_Chance.get()),
        STAR_3("rarity.r", 0xb4ff00, PAConfig.CONFIG.Star_3_Chance.get()),
        STAR_4("rarity.sr", 0x7200ff, PAConfig.CONFIG.Star_4_Chance.get()),
        STAR_5("rarity.ur", 0xffcc00, PAConfig.CONFIG.Star_5_Chance.get()),
        STAR_6("rarity.leg", 0xff6db5, PAConfig.CONFIG.Star_6_Chance.get()),
        SPECIAL("rarity.sp", 0x00fcff, 0.1F);

        private final String translationkey;
        private final float weight;
        private final int rarityColor;
        CompanionRarity(String name, int rarityColor, double weight) {
            this.translationkey = name;
            this.rarityColor = rarityColor;
            this.weight = (float) (weight/100F);
        }

        public String getTranslationkey(){
            return this.translationkey;
        }

        public float getWeight() {
            return weight;
        }

        public int getColor(){
            return this.rarityColor;
        }
    }

    public enum SERVANT_CLASSES{
        ASSASSIN("servantclass.assassin", 227, 227),
        SHIELDER("servantclass.shielder", 227, 227),
        SABER("servantclass.saber", 227, 227),
        LANCER("servantclass.lancer", 227, 197);
        private final String translationkey;
        private final int iconx, icony;
        SERVANT_CLASSES(String translationkey, int iconx, int icony){
            this.translationkey = translationkey;
            this.iconx = iconx;
            this.icony = icony;
        }

        public int getIconx() {
            return iconx;
        }

        public int getIcony() {
            return icony;
        }

        public String getTranslationkey() {
            return translationkey;
        }
    }

    public enum AmmoCategory {
        //.... and fallback values
        //AP: Higher damage on kansen, lower chance to damage rigging
        //HE: Lower Damage on Kansen, higher chance to damage rigging
        //INC: *pyro noise*
        //APHE:Bigger Damage on Kansen

        //Big Lead boi
        GENERIC(0.80F, 5, 1, 1, 0.8F, false, false),
        //What are you doing, step-shell? ~~I'm sorry~~
        AP(0.96F,7, 2,4, 0.3F,false, false),
        //haha Shell goes AW MAN
        HE(0.78F,2, 1, 6, 0.8F, false, true),
        //Ah thats hot
        INCENDIARY(0.84F, 6, 3F,2, 0.8F, true, false),
        //Penetrates deep, and causes A LOTTA DAMAGE
        SAP(0.85F, 10, 9, 2,0.8F, false, false),
        API(0.80F, 6, 3, 3,0.6F, true, false),
        //HEI(0.68F, ),
        HEIAP(0.82F, 8, 4F, 4F, 0.6F, true,true);

        private final float damage_rigging, damage_entity, damage_component, hitChance, minimum_damage_modifier;
        private final boolean shouldDamageMultipleComponant;
        private final boolean isIncendiary;
        AmmoCategory(float HitChance, float riggingDamage, float entityDamage, float componentDamage, float MinimumDamageModifier, boolean isIncendiary, boolean splashDamage) {
            this.hitChance = HitChance;
            this.damage_rigging = riggingDamage;
            this.damage_entity = entityDamage;
            this.damage_component = componentDamage;
            this.minimum_damage_modifier = MinimumDamageModifier;
            this.isIncendiary = isIncendiary;
            this.shouldDamageMultipleComponant = splashDamage;
        }

        public float getDamage(DamageType type) {
            return switch (type) {
                default -> this.damage_rigging * getDamageVariation();
                case ENTITY -> this.damage_entity * getDamageVariation();
                case COMPONENT -> this.damage_component * getDamageVariation();
            };
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
        NONE("guntype.none", -1),
        AR("guntype.assault_rifle", 0),
        MG("guntype.machinegun", 1),
        SG("guntype.shotgun", 3),
        SMG("guntype.submachinegun", 4),
        HG("guntype.handgun", 2),
        SR("guntype.sniper", 5);

        private final String name;
        private final int category;
        GunClass(String name, int category) {
            this.name = name;
            this.category = category;
        }

        public boolean isGunSameCategory(Gun gun){
            return gun.getDisplay().getWeaponType() == this.getCategory();
        }

        public String getName(){
            return this.name;
        }

        public int getCategory(){
            return this.category;
        }
    }

    public enum DamageType{
        RIGGING,
        ENTITY,
        COMPONENT
    }

    public enum EntityType{
        KANSEN("al_kansen", (openContainerId, inventory, player, companion)->new ContainerALInventory(openContainerId, inventory, companion.getInventory(), ((EntityKansenBase)companion).getShipRiggingStorage(), companion.getEquipment(), companion.getAmmoStorage(), (EntityKansenBase)companion)),
        KANMUSU("kc_kanmusu", (openContainerId, inventory, player, companion)->new ContainerALInventory(openContainerId, inventory, companion.getInventory(), ((EntityKansenBase)companion).getShipRiggingStorage(), companion.getEquipment(), companion.getAmmoStorage(), (EntityKansenBase)companion)),
        TDOLL("gfl_tdoll", (openContainerId, inventory, player, companion)->new ContainerGFLInventory(openContainerId, inventory, companion.getInventory(), companion.getEquipment(), companion.getAmmoStorage(), companion)),
        BLUEARCHIVE("bluearchive", (openContainerId, inventory, player, companion)->new ContainerBAInventory(openContainerId, inventory, companion.getInventory(), companion.getEquipment(), companion.getAmmoStorage(), companion)),
        OPERATOR("akn_operator",(openContainerId, inventory, player, companion)->new ContainerAKNInventory(openContainerId, inventory, companion.getInventory(), companion.getEquipment(), companion.getAmmoStorage(), companion)),
        PRICONNE("pc_characters", (openContainerId, inventory, player, companion)->new ContainerPCRInventory(openContainerId, inventory, companion.getInventory(), companion.getEquipment(), companion.getAmmoStorage(), companion)),
        REUNION("akn_reunion", (openContainerId, inventory, player, companion)->new ContainerAKNInventory(openContainerId, inventory, companion.getInventory(), companion.getEquipment(), companion.getAmmoStorage(), companion)),
        SHININGRESONANCE("srr", (openContainerId, inventory, player, companion)->new ContainerSRInventory(openContainerId, inventory, companion.getInventory(), companion.getEquipment(), companion.getAmmoStorage(), companion)),
        SERVANT("fgo_servant", (openContainerId, inventory, player, companion)->new ContainerFGOInventory(openContainerId, inventory, companion.getInventory(), companion.getEquipment(), companion.getAmmoStorage(), companion)),
        SOULWORKER("sw_soulworker", (openContainerId, inventory, player, companion)->new ContainerSWInventory(openContainerId, inventory, companion.getInventory(), companion.getEquipment(), companion.getAmmoStorage(), companion)),
        CLOSER("cls_closer", (openContainerId, inventory, player, companion)->new ContainerCLSInventory(openContainerId, inventory, companion.getInventory(), companion.getEquipment(), companion.getAmmoStorage(), companion));

        private final String name;
        private final Function4<Integer, Inventory, Player, AbstractEntityCompanion, AbstractContainerInventory> createmenu;
        EntityType(String name, Function4<Integer, Inventory, Player, AbstractEntityCompanion, AbstractContainerInventory> inventory) {
            this.name = name;
            this.createmenu = inventory;
        }

        public String getName(){
            return this.name;
        }

        @Nullable
        public AbstractContainerInventory createmenu(int openContainerId, Inventory inventory, Player player, AbstractEntityCompanion companion) {
            return createmenu.apply(openContainerId, inventory, player, companion);
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

    public enum TaskBlockType {
        ORE,
        CROP_HARVESTABLE,
        CROP_BONEMEALABLE,
        CROP_PLANTABLE,
        WORLDSKILL
    }

    public enum ResourceBlockType {
        RAW_ORE_BLOCK("raw_block", net.minecraft.world.level.material.Material.STONE, SoundType.STONE),
        ORE("ore", net.minecraft.world.level.material.Material.STONE, SoundType.STONE),
        DEEPSLATE_ORE("deepslate_ore", net.minecraft.world.level.material.Material.STONE, SoundType.DEEPSLATE),
        METAL_BLOCK("block", net.minecraft.world.level.material.Material.METAL, SoundType.METAL);
        private final String name;
        @Nullable
        private final net.minecraft.world.level.material.Material material;
        @Nullable
        private final SoundType soundType;

        ResourceBlockType(String name, net.minecraft.world.level.material.@org.jetbrains.annotations.Nullable Material material, @org.jetbrains.annotations.Nullable SoundType soundType){
            this.name = name;
            this.material = material;
            this.soundType = soundType;
        }

        @Nullable
        public net.minecraft.world.level.material.Material getMaterial() {
            return material;
        }


        @Nullable
        public SoundType getSoundType() {
            return soundType;
        }

        public String getName() {
            return this.name;
        }
    }

    public enum ResourceItemType {
        GEAR("gear"),
        INGOT("ingot"),
        PLATE("plate"),
        RAW_ORE("raw"),
        DUST("dust"),
        WIRE("wire"),
        NUGGET("nugget");

        private final String name;
        @Nullable
        private final net.minecraft.world.level.material.Material material;
        @Nullable
        private final SoundType soundType;

        ResourceItemType(String name){
            this.name = name;
            this.material = null;
            this.soundType = null;
        }

        ResourceItemType(String name, net.minecraft.world.level.material.@org.jetbrains.annotations.Nullable Material material, @org.jetbrains.annotations.Nullable SoundType soundType){
            this.name = name;
            this.material = material;
            this.soundType = soundType;
        }

        @Nullable
        public net.minecraft.world.level.material.Material getMaterial() {
            return material;
        }


        @Nullable
        public SoundType getSoundType() {
            return soundType;
        }

        public String getName() {
            return this.name;
        }
    }


}
