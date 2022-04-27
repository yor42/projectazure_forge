package com.yor42.projectazure.data;

import com.yor42.projectazure.libs.Constants;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class ModTags {

    public static final class Fluids {

        public static final TagKey<Fluid> CRUDEOIL = forge("crude_oil");
        public static final TagKey<Fluid> DIESEL = forge("diesel");
        public static final TagKey<Fluid> FUELOIL = forge("heavy_fuel_oil");
        public static final TagKey<Fluid> GASOLINE = forge("gasoline");

        private static TagKey<Fluid> forge(String path) {
            return TagKey.create(Registry.FLUID_REGISTRY, new ResourceLocation("forge", path));
        }

        private static TagKey<Fluid> mc(String path) {
            return TagKey.create(Registry.FLUID_REGISTRY, new ResourceLocation("minecraft", path));
        }
    }

    public static final class Blocks {
        public static final TagKey<Block> ORES_ALUMINIUM = forge("ores/aluminium");
        public static final TagKey<Block> ORES_COPPER = forge("ores/copper");
        public static final TagKey<Block> ORES_TIN = forge("ores/tin");
        public static final TagKey<Block> ORES_LEAD = forge("ores/lead");
        public static final TagKey<Block> ORES_ZINC = forge("ores/zinc");
        public static final TagKey<Block> ORES_ORIROCK = forge("ores/originium");

        private static TagKey<Block> forge(String path) {
            return TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation("forge", path));
        }

        private static TagKey<Block> mc(String path) {
            return TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation("minecraft", path));
        }
    }

    public static final class Items {

        public static final TagKey<Item> INGOT_ALUMINIUM = forge("ingots/aluminium");
        public static final TagKey<Item> INGOT_COPPER = forge("ingots/copper");
        public static final TagKey<Item> INGOT_TIN = forge("ingots/tin");
        public static final TagKey<Item> INGOT_BRONZE = forge("ingots/bronze");
        public static final TagKey<Item> INGOT_LEAD = forge("ingots/lead");
        public static final TagKey<Item> INGOT_ZINC = forge("ingots/zinc");
        public static final TagKey<Item> INGOT_STEEL = forge("ingots/steel");
        public static final TagKey<Item> INGOT_BRASS = forge("ingots/brass");

        public static final TagKey<Item> NUGGET_COPPER = forge("nuggets/copper");
        public static final TagKey<Item> NUGGET_TIN = forge("nuggets/tin");
        public static final TagKey<Item> NUGGET_BRONZE = forge("nuggets/bronze");
        public static final TagKey<Item> NUGGET_LEAD = forge("nuggets/lead");
        public static final TagKey<Item> NUGGET_ZINC = forge("nuggets/zinc");
        public static final TagKey<Item> NUGGET_STEEL = forge("nuggets/steel");
        public static final TagKey<Item> NUGGET_BRASS = forge("nuggets/brass");

        public static final TagKey<Item> GEAR_COPPER = forge("gears/copper");
        public static final TagKey<Item> GEAR_TIN = forge("gears/tin");
        public static final TagKey<Item> GEAR_BRONZE = forge("gears/bronze");
        public static final TagKey<Item> GEAR_IRON = forge("gears/iron");
        public static final TagKey<Item> GEAR_STEEL = forge("gears/steel");
        public static final TagKey<Item> GEAR_GOLD = forge("gears/gold");

        public static final TagKey<Item> PLATE_ALUMINIUM = forge("plates/aluminium");
        public static final TagKey<Item> PLATE_COPPER = forge("plates/copper");
        public static final TagKey<Item> PLATE_TIN = forge("plates/tin");
        public static final TagKey<Item> PLATE_BRONZE = forge("plates/bronze");
        public static final TagKey<Item> PLATE_LEAD = forge("plates/lead");
        public static final TagKey<Item> PLATE_IRON = forge("plates/iron");
        public static final TagKey<Item> PLATE_ZINC = forge("plates/zinc");
        public static final TagKey<Item> PLATE_STEEL = forge("plates/steel");
        public static final TagKey<Item> PLATE_BRASS = forge("plates/brass");
        public static final TagKey<Item> PLATE_GOLD = forge("plates/gold");

        public static final TagKey<Item> DUST_ALUMINIUM = forge("dusts/aluminium");
        public static final TagKey<Item> DUST_COPPER = forge("dusts/copper");
        public static final TagKey<Item> DUST_TIN = forge("dusts/tin");
        public static final TagKey<Item> DUST_BRONZE = forge("dusts/bronze");
        public static final TagKey<Item> DUST_LEAD = forge("dusts/lead");
        public static final TagKey<Item> DUST_ZINC = forge("dusts/zinc");
        public static final TagKey<Item> DUST_IRON = forge("dusts/iron");
        public static final TagKey<Item> DUST_COAL = forge("dusts/coal");
        public static final TagKey<Item> DUST_STEEL = forge("dusts/steel");
        public static final TagKey<Item> DUST_BRASS = forge("dusts/brass");
        public static final TagKey<Item> DUST_GOLD = forge("dusts/gold");

        public static final TagKey<Item> ORES_ALUMINIUM = forge("ores/aluminium");
        public static final TagKey<Item> ORES_COPPER = forge("ores/copper");
        public static final TagKey<Item> ORES_TIN = forge("ores/tin");
        public static final TagKey<Item> ORES_LEAD = forge("ores/lead");
        public static final TagKey<Item> ORES_ZINC = forge("ores/zinc");
        public static final TagKey<Item> ORES_ORIROCK = forge("ores/originium");

        public static final TagKey<Item> WIRE_COPPER = forge("wires/copper");

        public static final TagKey<Item> ORIGINITE = forge("gems/originite");
        public static final TagKey<Item> ORIGINIUM_PRIME = forge("gems/originium");
        //public static final TagKey<Item> ORES_ZINC = forge("ores/zinc");

        public static final TagKey<Item> MORTAR = forge("mortar");
        public static final TagKey<Item> CUTTER = forge("cutter");
        public static final TagKey<Item> HAMMER = forge("crafting_hammer");

        public static final TagKey<Item> SLEDGEHAMMER = forge("sledgehammer");

        public static final TagKey<Item> TREE_SAP = forge("tree_sap");

        public static final TagKey<Item> EXTRUSION_MOLD = mod("mold");

        //Mekanism Compat Layer
        public static final TagKey<Item> CIRCUITS = forge("circuits");
        public static final TagKey<Item> CIRCUITS_BASIC = forge("circuits/basic");
        public static final TagKey<Item> CIRCUITS_ADVANCED = forge("circuits/advanced");

        private static TagKey<Item> forge(String path) {
            return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("forge", path));
        }

        private static TagKey<Item> mc(String path) {
            return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("minecraft", path));
        }

        private static TagKey<Item> mod(String path) {
            return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Constants.MODID, path));
        }
    }
}
