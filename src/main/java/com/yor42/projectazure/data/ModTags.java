package com.yor42.projectazure.data;

import com.yor42.projectazure.libs.Constants;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class ModTags {

    public static final class Fluids {

        public static final ITag.INamedTag<Fluid> CRUDEOIL = forge("crude_oil");
        public static final ITag.INamedTag<Fluid> DIESEL = forge("diesel");
        public static final ITag.INamedTag<Fluid> FUELOIL = forge("heavy_fuel_oil");
        public static final ITag.INamedTag<Fluid> GASOLINE = forge("gasoline");

        private static ITag.INamedTag<Fluid> forge(String path) {
            return FluidTags.makeWrapperTag(new ResourceLocation("forge", path).toString());
        }

        private static ITag.INamedTag<Fluid> mc(String path) {
            return FluidTags.makeWrapperTag(new ResourceLocation("minecraft", path).toString());
        }
    }

    public static final class Blocks {

        public static final ITag.INamedTag<Block> LOG = forge("logs");

        public static final ITag.INamedTag<Block> ORES_ALUMINIUM = forge("ores/aluminium");
        public static final ITag.INamedTag<Block> ORES_COPPER = forge("ores/copper");
        public static final ITag.INamedTag<Block> ORES_TIN = forge("ores/tin");
        public static final ITag.INamedTag<Block> ORES_LEAD = forge("ores/lead");

        private static ITag.INamedTag<Block> forge(String path) {
            return BlockTags.makeWrapperTag(new ResourceLocation("forge", path).toString());
        }

        private static ITag.INamedTag<Block> mc(String path) {
            return BlockTags.makeWrapperTag(new ResourceLocation("minecraft", path).toString());
        }
    }

    public static final class Items {
        public static final ITag.INamedTag<Item> INGOT_ALUMINIUM = forge("ingots/aluminium");
        public static final ITag.INamedTag<Item> INGOT_COPPER = forge("ingots/copper");
        public static final ITag.INamedTag<Item> INGOT_TIN = forge("ingots/tin");
        public static final ITag.INamedTag<Item> INGOT_BRONZE = forge("ingots/bronze");
        public static final ITag.INamedTag<Item> INGOT_LEAD = forge("ingots/lead");
        public static final ITag.INamedTag<Item> INGOT_ZINC = forge("ingots/zinc");
        public static final ITag.INamedTag<Item> INGOT_STEEL = forge("ingots/steel");
        public static final ITag.INamedTag<Item> INGOT_BRASS = forge("ingots/brass");

        public static final ITag.INamedTag<Item> NUGGET_COPPER = forge("nuggets/copper");
        public static final ITag.INamedTag<Item> NUGGET_TIN = forge("nuggets/tin");
        public static final ITag.INamedTag<Item> NUGGET_BRONZE = forge("nuggets/bronze");
        public static final ITag.INamedTag<Item> NUGGET_LEAD = forge("nuggets/lead");
        public static final ITag.INamedTag<Item> NUGGET_ZINC = forge("nuggets/zinc");
        public static final ITag.INamedTag<Item> NUGGET_STEEL = forge("nuggets/steel");
        public static final ITag.INamedTag<Item> NUGGET_BRASS = forge("nuggets/brass");

        public static final ITag.INamedTag<Item> GEAR_COPPER = forge("gears/copper");
        public static final ITag.INamedTag<Item> GEAR_TIN = forge("gears/tin");
        public static final ITag.INamedTag<Item> GEAR_BRONZE = forge("gears/bronze");
        public static final ITag.INamedTag<Item> GEAR_IRON = forge("gears/iron");
        public static final ITag.INamedTag<Item> GEAR_STEEL = forge("gears/steel");

        public static final ITag.INamedTag<Item> PLATE_ALUMINIUM = forge("plates/aluminium");
        public static final ITag.INamedTag<Item> PLATE_COPPER = forge("plates/copper");
        public static final ITag.INamedTag<Item> PLATE_TIN = forge("plates/tin");
        public static final ITag.INamedTag<Item> PLATE_BRONZE = forge("plates/bronze");
        public static final ITag.INamedTag<Item> PLATE_LEAD = forge("plates/lead");
        public static final ITag.INamedTag<Item> PLATE_IRON = forge("plates/iron");
        public static final ITag.INamedTag<Item> PLATE_ZINC = forge("plates/zinc");
        public static final ITag.INamedTag<Item> PLATE_STEEL = forge("plates/steel");
        public static final ITag.INamedTag<Item> PLATE_BRASS = forge("plates/brass");

        public static final ITag.INamedTag<Item> DUST_ALUMINIUM = forge("dusts/aluminium");
        public static final ITag.INamedTag<Item> DUST_COPPER = forge("dusts/copper");
        public static final ITag.INamedTag<Item> DUST_TIN = forge("dusts/tin");
        public static final ITag.INamedTag<Item> DUST_BRONZE = forge("dusts/bronze");
        public static final ITag.INamedTag<Item> DUST_LEAD = forge("dusts/lead");
        public static final ITag.INamedTag<Item> DUST_ZINC = forge("dusts/zinc");
        public static final ITag.INamedTag<Item> DUST_IRON = forge("dusts/iron");
        public static final ITag.INamedTag<Item> DUST_COAL = forge("dusts/coal");
        public static final ITag.INamedTag<Item> DUST_STEEL = forge("dusts/steel");
        public static final ITag.INamedTag<Item> DUST_BRASS = forge("dusts/brass");

        public static final ITag.INamedTag<Item> ORES_ALUMINIUM = forge("ores/aluminium");
        public static final ITag.INamedTag<Item> ORES_COPPER = forge("ores/copper");
        public static final ITag.INamedTag<Item> ORES_TIN = forge("ores/tin");
        public static final ITag.INamedTag<Item> ORES_LEAD = forge("ores/lead");

        public static final ITag.INamedTag<Item> WIRE_COPPER = forge("wires/copper");

        public static final ITag.INamedTag<Item> ORIGINITE = forge("gems/originite");
        public static final ITag.INamedTag<Item> ORIGINIUM_PRIME = forge("gems/originium");
        //public static final ITag.INamedTag<Item> ORES_ZINC = forge("ores/zinc");

        public static final ITag.INamedTag<Item> MORTAR = forge("mortar");
        public static final ITag.INamedTag<Item> CUTTER = forge("cutter");
        public static final ITag.INamedTag<Item> HAMMER = forge("hammer");

        public static final ITag.INamedTag<Item> TREE_SAP = forge("tree_sap");

        public static final ITag.INamedTag<Item> EXTRUSION_MOLD = mod("mold");

        //Mekanism Compat Layer
        public static final ITag.INamedTag<Item> CIRCUITS = forge("circuits");
        public static final ITag.INamedTag<Item> CIRCUITS_BASIC = forge("circuits/basic");
        public static final ITag.INamedTag<Item> CIRCUITS_ADVANCED = forge("circuits/advanced");

        private static ITag.INamedTag<Item> forge(String path) {
            return ItemTags.makeWrapperTag(new ResourceLocation("forge", path).toString());
        }

        private static ITag.INamedTag<Item> mc(String path) {
            return ItemTags.makeWrapperTag(new ResourceLocation("minecraft", path).toString());
        }

        private static ITag.INamedTag<Item> mod(String path) {
            return ItemTags.makeWrapperTag(new ResourceLocation(Constants.MODID, path).toString());
        }
    }
}
