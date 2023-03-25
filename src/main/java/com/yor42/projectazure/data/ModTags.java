package com.yor42.projectazure.data;

import com.yor42.projectazure.libs.Constants;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.Tags;

public class ModTags {

    public static final class Fluids {

        public static final Tag.Named<Fluid> CRUDEOIL = forge("crude_oil");
        public static final Tag.Named<Fluid> DIESEL = forge("diesel");
        public static final Tag.Named<Fluid> FUELOIL = forge("heavy_fuel_oil");
        public static final Tag.Named<Fluid> GASOLINE = forge("gasoline");
        public static final Tag.Named<Fluid> KETON = forge("keton");

        private static Tag.Named<Fluid> forge(String path) {
            return FluidTags.bind(new ResourceLocation("forge", path).toString());
        }

        private static Tag.Named<Fluid> mc(String path) {
            return FluidTags.bind(new ResourceLocation("minecraft", path).toString());
        }
    }

    public static final class Blocks {
        public static final Tag.Named<Block> ORES_ALUMINIUM = forge("ores/aluminium");
        public static final Tag.Named<Block> ORES_COPPER = forge("ores/copper");
        public static final Tag.Named<Block> ORES_TIN = forge("ores/tin");
        public static final Tag.Named<Block> ORES_LEAD = forge("ores/lead");
        public static final Tag.Named<Block> ORES_ZINC = forge("ores/zinc");
        public static final Tag.Named<Block> ORES_ORIROCK = forge("ores/originium");
        public static final Tag.Named<Block> ORES_RMA7012 = forge("ores/rma70-12");
        public static final Tag.Named<Block> ORES_MANGANESE = forge("ores/manganese");
        public static final Tag.Named<Block> ORES_PYROXENE = forge("ores/pyroxene");

        public static final Tag.Named<Block> BLOCK_ALUMINIUM = forge("storage_blocks/aluminium");
        public static final Tag.Named<Block> BLOCK_COPPER = forge("storage_blocks/copper");
        public static final Tag.Named<Block> BLOCK_TIN = forge("storage_blocks/tin");
        public static final Tag.Named<Block> BLOCK_LEAD = forge("storage_blocks/lead");
        public static final Tag.Named<Block> BLOCK_ZINC = forge("storage_blocks/zinc");
        public static final Tag.Named<Block> BLOCK_BRASS = forge("storage_blocks/brass");
        public static final Tag.Named<Block> BLOCK_BRONZE = forge("storage_blocks/bronze");
        public static final Tag.Named<Block> BLOCK_RMA7012 = forge("storage_blocks/rma70-12");
        public static final Tag.Named<Block> BLOCK_RMA7024 = forge("storage_blocks/rma70-24");
        public static final Tag.Named<Block> BLOCK_D32 = forge("storage_blocks/d32");
        public static final Tag.Named<Block> BLOCK_STEEL = forge("storage_blocks/steel");
        public static final Tag.Named<Block> BLOCK_INCANDESCENT_ALLOY = forge("storage_blocks/incandescent_alloy");
        public static final Tag.Named<Block> BLOCK_MANGANESE = forge("storage_blocks/manganese");

        private static Tag.Named<Block> forge(String path) {
            return BlockTags.bind(new ResourceLocation("forge", path).toString());
        }

        private static Tag.Named<Block> mc(String path) {
            return BlockTags.bind(new ResourceLocation("minecraft", path).toString());
        }
    }

    public static final class Items {

        public static final Tag.Named<Item> INGOT_ALUMINIUM = forge("ingots/aluminium");
        public static final Tag.Named<Item> INGOT_COPPER = forge("ingots/copper");
        public static final Tag.Named<Item> INGOT_TIN = forge("ingots/tin");
        public static final Tag.Named<Item> INGOT_BRONZE = forge("ingots/bronze");
        public static final Tag.Named<Item> INGOT_LEAD = forge("ingots/lead");
        public static final Tag.Named<Item> INGOT_ZINC = forge("ingots/zinc");
        public static final Tag.Named<Item> INGOT_STEEL = forge("ingots/steel");
        public static final Tag.Named<Item> INGOT_BRASS = forge("ingots/brass");
        public static final Tag.Named<Item> INGOT_SILICON = forge("ingots/silicon");
        public static final Tag.Named<Item> INGOT_RMA7012 = forge("ingots/rma70-12");
        public static final Tag.Named<Item> INGOT_MANGANESE = forge("ingots/manganese");
        public static final Tag.Named<Item> INGOT_INCANDESCENT_ALLOY = forge("ingots/incandescent_alloy");
        public static final Tag.Named<Item> INGOT_RMA7024 = forge("ingots/rma70-24");
        public static final Tag.Named<Item> INGOT_D32 = forge("ingots/d32");

        public static final Tag.Named<Item> NUGGET_COPPER = forge("nuggets/copper");
        public static final Tag.Named<Item> NUGGET_TIN = forge("nuggets/tin");
        public static final Tag.Named<Item> NUGGET_BRONZE = forge("nuggets/bronze");
        public static final Tag.Named<Item> NUGGET_LEAD = forge("nuggets/lead");
        public static final Tag.Named<Item> NUGGET_ZINC = forge("nuggets/zinc");
        public static final Tag.Named<Item> NUGGET_STEEL = forge("nuggets/steel");
        public static final Tag.Named<Item> NUGGET_BRASS = forge("nuggets/brass");
        public static final Tag.Named<Item> NUGGET_RMA7012 = forge("nuggets/rma70-12");
        public static final Tag.Named<Item> NUGGET_RMA7024 = forge("nuggets/rma70-24");

        public static final Tag.Named<Item> NUGGET_D32 = forge("nuggets/d32");
        public static final Tag.Named<Item> NUGGET_MANGANESE = forge("nuggets/manganese");
        public static final Tag.Named<Item> NUGGET_INCANDESCENT_ALLOY = forge("nuggets/incandescent_alloy");
        public static final Tag.Named<Item> GEM_ORIGINITE = forge("gems/originite");
        public static final Tag.Named<Item> GEM_ORIGINIUM_PRIME = forge("gems/originium_prime");
        public static final Tag.Named<Item> GEM_PYROXENE = forge("gems/pyroxene");

        public static final Tag.Named<Item> GEAR_BRONZE = forge("gears/bronze");
        public static final Tag.Named<Item> GEAR_IRON = forge("gears/iron");
        public static final Tag.Named<Item> GEAR_STEEL = forge("gears/steel");

        public static final Tag.Named<Item> PLATE_ALUMINIUM = forge("plates/aluminium");
        public static final Tag.Named<Item> PLATE_PLASTIC = forge("plates/plastic");
        public static final Tag.Named<Item> PLATE_COPPER = forge("plates/copper");
        public static final Tag.Named<Item> PLATE_TIN = forge("plates/tin");
        public static final Tag.Named<Item> PLATE_BRONZE = forge("plates/bronze");
        public static final Tag.Named<Item> PLATE_LEAD = forge("plates/lead");
        public static final Tag.Named<Item> PLATE_IRON = forge("plates/iron");
        public static final Tag.Named<Item> PLATE_ZINC = forge("plates/zinc");
        public static final Tag.Named<Item> PLATE_STEEL = forge("plates/steel");
        public static final Tag.Named<Item> PLATE_BRASS = forge("plates/brass");
        public static final Tag.Named<Item> PLATE_GOLD = forge("plates/gold");
        public static final Tag.Named<Item> PLATE_RMA7012 = forge("plates/rma70-12");
        public static final Tag.Named<Item> PLATE_RMA7024 = forge("plates/rma70-24");
        public static final Tag.Named<Item> PLATE_D32 = forge("plates/d32");
        public static final Tag.Named<Item> PLATE_MANGANESE = forge("plates/manganese");
        public static final Tag.Named<Item> PLATE_INCANDESCENT_ALLOY = forge("plates/incandescent_alloy");

        public static final Tag.Named<Item> DUST_ALUMINIUM = forge("dusts/aluminium");
        public static final Tag.Named<Item> DUST_COPPER = forge("dusts/copper");
        public static final Tag.Named<Item> DUST_TIN = forge("dusts/tin");
        public static final Tag.Named<Item> DUST_BRONZE = forge("dusts/bronze");
        public static final Tag.Named<Item> DUST_LEAD = forge("dusts/lead");
        public static final Tag.Named<Item> DUST_ZINC = forge("dusts/zinc");
        public static final Tag.Named<Item> DUST_IRON = forge("dusts/iron");
        public static final Tag.Named<Item> DUST_COAL = forge("dusts/coal");
        public static final Tag.Named<Item> DUST_STEEL = forge("dusts/steel");
        public static final Tag.Named<Item> DUST_BRASS = forge("dusts/brass");
        public static final Tag.Named<Item> DUST_GOLD = forge("dusts/gold");
        public static final Tag.Named<Item> DUST_ORIGINIUM = forge("dusts/originium");
        public static final Tag.Named<Item> DUST_ORIROCK = forge("dusts/orirock");
        public static final Tag.Named<Item> DUST_QUARTZ = forge("dusts/quartz");
        public static final Tag.Named<Item> DUST_RMA7012 = forge("dusts/rma70-12");
        public static final Tag.Named<Item> DUST_RMA7024 = forge("dusts/rma70-24");
        public static final Tag.Named<Item> DUST_MANGANESE = forge("dusts/manganese");
        public static final Tag.Named<Item> DUST_INCANDESCENT_ALLOY = forge("dusts/incandescent_alloy");

        public static final Tag.Named<Item> BLOCK_ALUMINIUM = forge("storage_blocks/aluminium");
        public static final Tag.Named<Item> BLOCK_COPPER = forge("storage_blocks/copper");
        public static final Tag.Named<Item> BLOCK_TIN = forge("storage_blocks/tin");
        public static final Tag.Named<Item> BLOCK_LEAD = forge("storage_blocks/lead");
        public static final Tag.Named<Item> BLOCK_ZINC = forge("storage_blocks/zinc");
        public static final Tag.Named<Item> BLOCK_BRASS = forge("storage_blocks/brass");
        public static final Tag.Named<Item> BLOCK_BRONZE = forge("storage_blocks/bronze");
        public static final Tag.Named<Item> BLOCK_RMA7012 = forge("storage_blocks/rma70-12");
        public static final Tag.Named<Item> BLOCK_RMA7024 = forge("storage_blocks/rma70-24");
        public static final Tag.Named<Item> BLOCK_D32 = forge("storage_blocks/d32");
        public static final Tag.Named<Item> BLOCK_STEEL = forge("storage_blocks/steel");
        public static final Tag.Named<Item> BLOCK_MANGANESE = forge("storage_blocks/manganese");
        public static final Tag.Named<Item> BLOCK_INCANDESCENT_ALLOY = forge("storage_blocks/incandescent_alloy");

        public static final Tag.Named<Item> MATERIAL_ALUMINIUM = forge("aluminium");
        public static final Tag.Named<Item> MATERIAL_COPPER = forge("copper");
        public static final Tag.Named<Item> MATERIAL_TIN = forge("tin");
        public static final Tag.Named<Item> MATERIAL_LEAD = forge("lead");
        public static final Tag.Named<Item> MATERIAL_ZINC = forge("zinc");
        public static final Tag.Named<Item> MATERIAL_BRASS = forge("brass");
        public static final Tag.Named<Item> MATERIAL_BRONZE = forge("bronze");
        public static final Tag.Named<Item> MATERIAL_RMA7012 = forge("rma70-12");
        public static final Tag.Named<Item> MATERIAL_RMA7024 = forge("rma70-24");
        public static final Tag.Named<Item> MATERIAL_STEEL = forge("steel");
        public static final Tag.Named<Item> MATERIAL_INCANDESCENT_ALLOY = forge("incandescent_alloy");
        public static final Tag.Named<Item> MATERIAL_MANGANESE = forge("manganese");
        public static final Tag.Named<Item> MATERIAL_D32 = forge("d32");
        public static final Tag.Named<Item> MATERIAL_ORIROCK = forge("orirock");

        public static final Tag.Named<Item> MATERIAL_SILICON= forge("silicon");

        public static final Tag.Named<Item> DUST_D32 = forge("dusts/d32");

        public static final Tag.Named<Item> EXPLOSIVE_COMPOUND = forge("explosive");

        public static final Tag.Named<Item> ORES_ALUMINIUM = forge("ores/aluminium");
        public static final Tag.Named<Item> ORES_COPPER = forge("ores/copper");
        public static final Tag.Named<Item> ORES_TIN = forge("ores/tin");
        public static final Tag.Named<Item> ORES_LEAD = forge("ores/lead");
        public static final Tag.Named<Item> ORES_ZINC = forge("ores/zinc");
        public static final Tag.Named<Item> ORES_ORIROCK = forge("ores/originium");
        public static final Tag.Named<Item> ORES_RMA7012 = forge("ores/rma70-12");
        public static final Tag.Named<Item> ORES_MANGANESE = forge("ores/manganese");
        public static final Tag.Named<Item> ORES_PYROXENE = forge("ores/pyroxene");
        public static final Tag.Named<Item> WIRE_COPPER = forge("wires/copper");

        public static final Tag.Named<Item> ORIGINITE = forge("gems/originite");
        public static final Tag.Named<Item> ORIGINIUM_PRIME = forge("gems/originium");
        //public static final ITag.INamedTag<Item> ORES_ZINC = forge("ores/zinc");

        public static final Tag.Named<Item> MORTAR = forge("tools/mortar");
        public static final Tag.Named<Item> CUTTER = forge("tools/cutter");
        public static final Tag.Named<Item> KNIFE = forge("tools/knife");
        public static final Tag.Named<Item> KNIVES = forge("tools/knives");
        public static final Tag.Named<Item> HAMMER = forge("tools/crafting_hammer");
        public static final Tag.Named<Item> SAW = forge("tools/saw");

        public static final Tag.Named<Item> SLEDGEHAMMER = forge("sledgehammer");

        public static final Tag.Named<Item> TREE_SAP = forge("tree_sap");

        public static final Tag.Named<Item> EXTRUSION_MOLD = mod("mold");

        //Mekanism Compat Layer
        public static final Tag.Named<Item> CIRCUITS = forge("circuits");
        public static final Tag.Named<Item> CIRCUITS_BASIC = forge("circuits/basic");
        public static final Tag.Named<Item> CIRCUITS_ADVANCED = forge("circuits/advanced");

        public static final Tag.Named<Item> CIRCUITS_CRYSTALLINE = forge("circuits/crystalline");

        private static Tag.Named<Item> forge(String path) {
            return ItemTags.bind(new ResourceLocation("forge", path).toString());
        }

        private static Tag.Named<Item> mod(String path) {
            return ItemTags.bind(new ResourceLocation(Constants.MODID, path).toString());
        }
    }
}
