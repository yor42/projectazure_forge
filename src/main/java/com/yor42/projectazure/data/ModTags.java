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
import net.minecraftforge.common.Tags;

public class ModTags {

    public static final class Fluids {

        public static final ITag.INamedTag<Fluid> CRUDEOIL = forge("crude_oil");
        public static final ITag.INamedTag<Fluid> DIESEL = forge("diesel");
        public static final ITag.INamedTag<Fluid> FUELOIL = forge("heavy_fuel_oil");
        public static final ITag.INamedTag<Fluid> GASOLINE = forge("gasoline");

        private static ITag.INamedTag<Fluid> forge(String path) {
            return FluidTags.bind(new ResourceLocation("forge", path).toString());
        }

        private static ITag.INamedTag<Fluid> mc(String path) {
            return FluidTags.bind(new ResourceLocation("minecraft", path).toString());
        }
    }

    public static final class Blocks {
        public static final ITag.INamedTag<Block> ORES_ALUMINIUM = forge("ores/aluminium");
        public static final ITag.INamedTag<Block> ORES_COPPER = forge("ores/copper");
        public static final ITag.INamedTag<Block> ORES_TIN = forge("ores/tin");
        public static final ITag.INamedTag<Block> ORES_LEAD = forge("ores/lead");
        public static final ITag.INamedTag<Block> ORES_ZINC = forge("ores/zinc");
        public static final ITag.INamedTag<Block> ORES_ORIROCK = forge("ores/originium");
        public static final ITag.INamedTag<Block> ORES_RMA7012 = forge("ores/rma70-12");

        public static final ITag.INamedTag<Block> BLOCK_ALUMINIUM = forge("storage_blocks/aluminium");
        public static final ITag.INamedTag<Block> BLOCK_COPPER = forge("storage_blocks/copper");
        public static final ITag.INamedTag<Block> BLOCK_TIN = forge("storage_blocks/tin");
        public static final ITag.INamedTag<Block> BLOCK_LEAD = forge("storage_blocks/lead");
        public static final ITag.INamedTag<Block> BLOCK_ZINC = forge("storage_blocks/zinc");
        public static final ITag.INamedTag<Block> BLOCK_BRASS = forge("storage_blocks/brass");
        public static final ITag.INamedTag<Block> BLOCK_BRONZE = forge("storage_blocks/bronze");
        public static final ITag.INamedTag<Block> BLOCK_RMA7012 = forge("storage_blocks/rma70-12");
        public static final ITag.INamedTag<Block> BLOCK_RMA7024 = forge("storage_blocks/rma70-24");
        public static final ITag.INamedTag<Block> BLOCK_D32 = forge("storage_blocks/d32");
        public static final ITag.INamedTag<Block> BLOCK_STEEL = forge("storage_blocks/steel");

        private static ITag.INamedTag<Block> forge(String path) {
            return BlockTags.bind(new ResourceLocation("forge", path).toString());
        }

        private static ITag.INamedTag<Block> mc(String path) {
            return BlockTags.bind(new ResourceLocation("minecraft", path).toString());
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
        public static final ITag.INamedTag<Item> INGOT_RMA7012 = forge("ingots/rma70-12");

        public static final ITag.INamedTag<Item> INGOT_RMA7024 = forge("ingots/rma70-24");

        public static final ITag.INamedTag<Item> INGOT_D32 = forge("ingots/d32");

        public static final ITag.INamedTag<Item> NUGGET_COPPER = forge("nuggets/copper");
        public static final ITag.INamedTag<Item> NUGGET_TIN = forge("nuggets/tin");
        public static final ITag.INamedTag<Item> NUGGET_BRONZE = forge("nuggets/bronze");
        public static final ITag.INamedTag<Item> NUGGET_LEAD = forge("nuggets/lead");
        public static final ITag.INamedTag<Item> NUGGET_ZINC = forge("nuggets/zinc");
        public static final ITag.INamedTag<Item> NUGGET_STEEL = forge("nuggets/steel");
        public static final ITag.INamedTag<Item> NUGGET_BRASS = forge("nuggets/brass");
        public static final ITag.INamedTag<Item> NUGGET_RMA7012 = forge("nuggets/rma70-12");

        public static final ITag.INamedTag<Item> NUGGET_RMA7024 = forge("nuggets/rma70-24");

        public static final ITag.INamedTag<Item> NUGGET_D32 = forge("nuggets/d32");

        public static final ITag.INamedTag<Item> GEAR_COPPER = forge("gears/copper");
        public static final ITag.INamedTag<Item> GEAR_TIN = forge("gears/tin");
        public static final ITag.INamedTag<Item> GEAR_BRONZE = forge("gears/bronze");
        public static final ITag.INamedTag<Item> GEAR_IRON = forge("gears/iron");
        public static final ITag.INamedTag<Item> GEAR_STEEL = forge("gears/steel");
        public static final ITag.INamedTag<Item> GEAR_GOLD = forge("gears/gold");

        public static final ITag.INamedTag<Item> PLATE_ALUMINIUM = forge("plates/aluminium");
        public static final ITag.INamedTag<Item> PLATE_COPPER = forge("plates/copper");
        public static final ITag.INamedTag<Item> PLATE_TIN = forge("plates/tin");
        public static final ITag.INamedTag<Item> PLATE_BRONZE = forge("plates/bronze");
        public static final ITag.INamedTag<Item> PLATE_LEAD = forge("plates/lead");
        public static final ITag.INamedTag<Item> PLATE_IRON = forge("plates/iron");
        public static final ITag.INamedTag<Item> PLATE_ZINC = forge("plates/zinc");
        public static final ITag.INamedTag<Item> PLATE_STEEL = forge("plates/steel");
        public static final ITag.INamedTag<Item> PLATE_BRASS = forge("plates/brass");
        public static final ITag.INamedTag<Item> PLATE_GOLD = forge("plates/gold");
        public static final ITag.INamedTag<Item> PLATE_RMA7012 = forge("plates/rma70-12");

        public static final ITag.INamedTag<Item> PLATE_RMA7024 = forge("plates/rma70-24");

        public static final ITag.INamedTag<Item> PLATE_D32 = forge("plates/d32");

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
        public static final ITag.INamedTag<Item> DUST_GOLD = forge("dusts/gold");
        public static final ITag.INamedTag<Item> DUST_ORIGINIUM = forge("dusts/originium");
        public static final ITag.INamedTag<Item> DUST_ORIROCK = forge("dusts/orirock");
        public static final ITag.INamedTag<Item> DUST_QUARTZ = forge("dusts/quartz");
        public static final ITag.INamedTag<Item> DUST_RMA7012 = forge("dusts/rma70-12");
        public static final ITag.INamedTag<Item> DUST_RMA7024 = forge("dusts/rma70-24");

        public static final ITag.INamedTag<Item> BLOCK_ALUMINIUM = forge("storage_blocks/aluminium");
        public static final ITag.INamedTag<Item> BLOCK_COPPER = forge("storage_blocks/copper");
        public static final ITag.INamedTag<Item> BLOCK_TIN = forge("storage_blocks/tin");
        public static final ITag.INamedTag<Item> BLOCK_LEAD = forge("storage_blocks/lead");
        public static final ITag.INamedTag<Item> BLOCK_ZINC = forge("storage_blocks/zinc");
        public static final ITag.INamedTag<Item> BLOCK_BRASS = forge("storage_blocks/brass");
        public static final ITag.INamedTag<Item> BLOCK_BRONZE = forge("storage_blocks/bronze");
        public static final ITag.INamedTag<Item> BLOCK_RMA7012 = forge("storage_blocks/rma70-12");
        public static final ITag.INamedTag<Item> BLOCK_RMA7024 = forge("storage_blocks/rma70-24");
        public static final ITag.INamedTag<Item> BLOCK_D32 = forge("storage_blocks/d32");
        public static final ITag.INamedTag<Item> BLOCK_STEEL = forge("storage_blocks/steel");


        public static final ITag.INamedTag<Item> DUST_D32 = forge("dusts/d32");

        public static final ITag.INamedTag<Item> EXPLOSIVE_COMPOUND = forge("explosive");

        public static final ITag.INamedTag<Item> ORES_ALUMINIUM = forge("ores/aluminium");
        public static final ITag.INamedTag<Item> ORES_COPPER = forge("ores/copper");
        public static final ITag.INamedTag<Item> ORES_TIN = forge("ores/tin");
        public static final ITag.INamedTag<Item> ORES_LEAD = forge("ores/lead");
        public static final ITag.INamedTag<Item> ORES_ZINC = forge("ores/zinc");
        public static final ITag.INamedTag<Item> ORES_ORIROCK = forge("ores/originium");
        public static final ITag.INamedTag<Item> ORES_RMA7012 = forge("ores/rma70-12");

        public static final ITag.INamedTag<Item> WIRE_COPPER = forge("wires/copper");

        public static final ITag.INamedTag<Item> ORIGINITE = forge("gems/originite");
        public static final ITag.INamedTag<Item> ORIGINIUM_PRIME = forge("gems/originium");
        //public static final ITag.INamedTag<Item> ORES_ZINC = forge("ores/zinc");

        public static final ITag.INamedTag<Item> MORTAR = forge("mortar");
        public static final ITag.INamedTag<Item> CUTTER = forge("cutter");
        public static final ITag.INamedTag<Item> HAMMER = forge("crafting_hammer");

        public static final ITag.INamedTag<Item> SLEDGEHAMMER = forge("sledgehammer");

        public static final ITag.INamedTag<Item> TREE_SAP = forge("tree_sap");

        public static final ITag.INamedTag<Item> EXTRUSION_MOLD = mod("mold");

        //Mekanism Compat Layer
        public static final ITag.INamedTag<Item> CIRCUITS = forge("circuits");
        public static final ITag.INamedTag<Item> CIRCUITS_BASIC = forge("circuits/basic");
        public static final ITag.INamedTag<Item> CIRCUITS_ADVANCED = forge("circuits/advanced");

        private static ITag.INamedTag<Item> forge(String path) {
            return ItemTags.bind(new ResourceLocation("forge", path).toString());
        }

        private static ITag.INamedTag<Item> mc(String path) {
            return ItemTags.bind(new ResourceLocation("minecraft", path).toString());
        }

        private static ITag.INamedTag<Item> mod(String path) {
            return ItemTags.bind(new ResourceLocation(Constants.MODID, path).toString());
        }
    }
}
