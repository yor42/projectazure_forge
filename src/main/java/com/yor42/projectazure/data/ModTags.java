package com.yor42.projectazure.data;

import com.yor42.projectazure.libs.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
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
        public static final TagKey<Fluid> KETON = forge("keton");

        private static TagKey<Fluid> forge(String path) {
            return FluidTags.create(new ResourceLocation("forge", path));
        }

        private static TagKey<Fluid> mc(String path) {
            return FluidTags.create(new ResourceLocation("minecraft", path));
        }
    }

    public static final class Blocks {
        public static final TagKey<Block> ORES_ALUMINIUM = forge("ores/aluminium");
        public static final TagKey<Block> ORES_TIN = forge("ores/tin");
        public static final TagKey<Block> ORES_LEAD = forge("ores/lead");
        public static final TagKey<Block> ORES_ZINC = forge("ores/zinc");
        public static final TagKey<Block> ORES_ORIROCK = forge("ores/originium");
        public static final TagKey<Block> ORES_RMA7012 = forge("ores/rma70-12");
        public static final TagKey<Block> ORES_MANGANESE = forge("ores/manganese");
        public static final TagKey<Block> ORES_PYROXENE = forge("ores/pyroxene");

        public static final TagKey<Block> BLOCK_ALUMINIUM = forge("storage_blocks/aluminium");
        public static final TagKey<Block> BLOCK_COPPER = forge("storage_blocks/copper");
        public static final TagKey<Block> BLOCK_TIN = forge("storage_blocks/tin");
        public static final TagKey<Block> BLOCK_LEAD = forge("storage_blocks/lead");
        public static final TagKey<Block> BLOCK_ZINC = forge("storage_blocks/zinc");
        public static final TagKey<Block> BLOCK_BRASS = forge("storage_blocks/brass");
        public static final TagKey<Block> BLOCK_BRONZE = forge("storage_blocks/bronze");
        public static final TagKey<Block> BLOCK_RMA7012 = forge("storage_blocks/rma70-12");
        public static final TagKey<Block> BLOCK_RMA7024 = forge("storage_blocks/rma70-24");
        public static final TagKey<Block> BLOCK_D32 = forge("storage_blocks/d32");
        public static final TagKey<Block> BLOCK_STEEL = forge("storage_blocks/steel");
        public static final TagKey<Block> BLOCK_INCANDESCENT_ALLOY = forge("storage_blocks/incandescent_alloy");
        public static final TagKey<Block> BLOCK_MANGANESE = forge("storage_blocks/manganese");
        public static final TagKey<Block> BLOCK_RAW_ALUMINIUM = forge("storage_blocks/raw_aluminium");
        public static final TagKey<Block> BLOCK_RAW_TIN = forge("storage_blocks/raw_tin");
        public static final TagKey<Block> BLOCK_RAW_LEAD = forge("storage_blocks/raw_lead");
        public static final TagKey<Block> BLOCK_RAW_ZINC = forge("storage_blocks/raw_zinc");
        public static final TagKey<Block> BLOCK_RAW_RMA7012 = forge("storage_blocks/raw_rma70-12");
        public static final TagKey<Block> BLOCK_RAW_MANGANESE = forge("storage_blocks/raw_manganese");
        private static TagKey<Block> forge(String path) {
            return BlockTags.create(new ResourceLocation("forge", path));
        }

        private static TagKey<Block> mc(String path) {
            return BlockTags.create(new ResourceLocation("minecraft", path));
        }
    }

    public static final class Items {

        public static final TagKey<Item> INGOT_ALUMINIUM = forge("ingots/aluminium");
        public static final TagKey<Item> INGOT_TIN = forge("ingots/tin");
        public static final TagKey<Item> INGOT_BRONZE = forge("ingots/bronze");
        public static final TagKey<Item> INGOT_LEAD = forge("ingots/lead");
        public static final TagKey<Item> INGOT_ZINC = forge("ingots/zinc");
        public static final TagKey<Item> INGOT_STEEL = forge("ingots/steel");
        public static final TagKey<Item> INGOT_BRASS = forge("ingots/brass");
        public static final TagKey<Item> INGOT_SILICON = forge("ingots/silicon");
        public static final TagKey<Item> INGOT_RMA7012 = forge("ingots/rma70-12");
        public static final TagKey<Item> INGOT_MANGANESE = forge("ingots/manganese");
        public static final TagKey<Item> INGOT_INCANDESCENT_ALLOY = forge("ingots/incandescent_alloy");
        public static final TagKey<Item> INGOT_RMA7024 = forge("ingots/rma70-24");
        public static final TagKey<Item> INGOT_D32 = forge("ingots/d32");

        public static final TagKey<Item> NUGGET_COPPER = forge("nuggets/copper");
        public static final TagKey<Item> NUGGET_TIN = forge("nuggets/tin");
        public static final TagKey<Item> NUGGET_BRONZE = forge("nuggets/bronze");
        public static final TagKey<Item> NUGGET_LEAD = forge("nuggets/lead");
        public static final TagKey<Item> NUGGET_ZINC = forge("nuggets/zinc");
        public static final TagKey<Item> NUGGET_STEEL = forge("nuggets/steel");
        public static final TagKey<Item> NUGGET_BRASS = forge("nuggets/brass");
        public static final TagKey<Item> NUGGET_RMA7012 = forge("nuggets/rma70-12");
        public static final TagKey<Item> NUGGET_RMA7024 = forge("nuggets/rma70-24");

        public static final TagKey<Item> NUGGET_D32 = forge("nuggets/d32");
        public static final TagKey<Item> NUGGET_MANGANESE = forge("nuggets/manganese");
        public static final TagKey<Item> NUGGET_INCANDESCENT_ALLOY = forge("nuggets/incandescent_alloy");
        public static final TagKey<Item> GEM_ORIGINITE = forge("gems/originite");
        public static final TagKey<Item> GEM_ORIGINIUM_PRIME = forge("gems/originium_prime");
        public static final TagKey<Item> GEM_PYROXENE = forge("gems/pyroxene");

        public static final TagKey<Item> GEAR_BRONZE = forge("gears/bronze");
        public static final TagKey<Item> GEAR_IRON = forge("gears/iron");
        public static final TagKey<Item> GEAR_STEEL = forge("gears/steel");

        public static final TagKey<Item> PLATE_ALUMINIUM = forge("plates/aluminium");
        public static final TagKey<Item> PLATE_PLASTIC = forge("plates/plastic");
        public static final TagKey<Item> PLATE_COPPER = forge("plates/copper");
        public static final TagKey<Item> PLATE_TIN = forge("plates/tin");
        public static final TagKey<Item> PLATE_BRONZE = forge("plates/bronze");
        public static final TagKey<Item> PLATE_LEAD = forge("plates/lead");
        public static final TagKey<Item> PLATE_IRON = forge("plates/iron");
        public static final TagKey<Item> PLATE_ZINC = forge("plates/zinc");
        public static final TagKey<Item> PLATE_STEEL = forge("plates/steel");
        public static final TagKey<Item> PLATE_BRASS = forge("plates/brass");
        public static final TagKey<Item> PLATE_GOLD = forge("plates/gold");
        public static final TagKey<Item> PLATE_RMA7012 = forge("plates/rma70-12");
        public static final TagKey<Item> PLATE_RMA7024 = forge("plates/rma70-24");
        public static final TagKey<Item> PLATE_D32 = forge("plates/d32");
        public static final TagKey<Item> PLATE_MANGANESE = forge("plates/manganese");
        public static final TagKey<Item> PLATE_INCANDESCENT_ALLOY = forge("plates/incandescent_alloy");

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
        public static final TagKey<Item> DUST_ORIGINIUM = forge("dusts/originium");
        public static final TagKey<Item> DUST_ORIROCK = forge("dusts/orirock");
        public static final TagKey<Item> DUST_QUARTZ = forge("dusts/quartz");
        public static final TagKey<Item> DUST_RMA7012 = forge("dusts/rma70-12");
        public static final TagKey<Item> DUST_RMA7024 = forge("dusts/rma70-24");
        public static final TagKey<Item> DUST_MANGANESE = forge("dusts/manganese");
        public static final TagKey<Item> DUST_INCANDESCENT_ALLOY = forge("dusts/incandescent_alloy");

        public static final TagKey<Item> BLOCK_ALUMINIUM = forge("storage_blocks/aluminium");
        public static final TagKey<Item> BLOCK_COPPER = forge("storage_blocks/copper");
        public static final TagKey<Item> BLOCK_TIN = forge("storage_blocks/tin");
        public static final TagKey<Item> BLOCK_LEAD = forge("storage_blocks/lead");
        public static final TagKey<Item> BLOCK_ZINC = forge("storage_blocks/zinc");
        public static final TagKey<Item> BLOCK_BRASS = forge("storage_blocks/brass");
        public static final TagKey<Item> BLOCK_BRONZE = forge("storage_blocks/bronze");
        public static final TagKey<Item> BLOCK_RMA7012 = forge("storage_blocks/rma70-12");
        public static final TagKey<Item> BLOCK_RMA7024 = forge("storage_blocks/rma70-24");
        public static final TagKey<Item> BLOCK_D32 = forge("storage_blocks/d32");
        public static final TagKey<Item> BLOCK_STEEL = forge("storage_blocks/steel");
        public static final TagKey<Item> BLOCK_MANGANESE = forge("storage_blocks/manganese");
        public static final TagKey<Item> BLOCK_INCANDESCENT_ALLOY = forge("storage_blocks/incandescent_alloy");
        public static final TagKey<Item> BLOCK_RAW_TIN = forge("storage_blocks/raw_tin");
        public static final TagKey<Item> BLOCK_RAW_LEAD = forge("storage_blocks/raw_lead");
        public static final TagKey<Item> BLOCK_RAW_ZINC = forge("storage_blocks/raw_zinc");
        public static final TagKey<Item> BLOCK_RAW_RMA7012 = forge("storage_blocks/raw_rma70-12");
        public static final TagKey<Item> BLOCK_RAW_MANGANESE = forge("storage_blocks/raw_manganese");
        public static final TagKey<Item> BLOCK_RAW_ALUMINIUM = forge("storage_blocks/aluminium");

        public static final TagKey<Item> MATERIAL_ALUMINIUM = forge("aluminium");
        public static final TagKey<Item> MATERIAL_COPPER = forge("copper");
        public static final TagKey<Item> MATERIAL_TIN = forge("tin");
        public static final TagKey<Item> MATERIAL_LEAD = forge("lead");
        public static final TagKey<Item> MATERIAL_ZINC = forge("zinc");
        public static final TagKey<Item> MATERIAL_BRASS = forge("brass");
        public static final TagKey<Item> MATERIAL_BRONZE = forge("bronze");
        public static final TagKey<Item> MATERIAL_RMA7012 = forge("rma70-12");
        public static final TagKey<Item> MATERIAL_RMA7024 = forge("rma70-24");
        public static final TagKey<Item> MATERIAL_STEEL = forge("steel");
        public static final TagKey<Item> MATERIAL_INCANDESCENT_ALLOY = forge("incandescent_alloy");
        public static final TagKey<Item> MATERIAL_MANGANESE = forge("manganese");
        public static final TagKey<Item> MATERIAL_D32 = forge("d32");
        public static final TagKey<Item> MATERIAL_ORIROCK = forge("orirock");

        public static final TagKey<Item> MATERIAL_SILICON= forge("silicon");

        public static final TagKey<Item> DUST_D32 = forge("dusts/d32");

        public static final TagKey<Item> EXPLOSIVE_COMPOUND = forge("explosive");

        public static final TagKey<Item> ORES_ALUMINIUM = forge("ores/aluminium");
        public static final TagKey<Item> ORES_TIN = forge("ores/tin");
        public static final TagKey<Item> ORES_LEAD = forge("ores/lead");
        public static final TagKey<Item> ORES_ZINC = forge("ores/zinc");
        public static final TagKey<Item> ORES_ORIROCK = forge("ores/originium");
        public static final TagKey<Item> ORES_RMA7012 = forge("ores/rma70-12");
        public static final TagKey<Item> ORES_MANGANESE = forge("ores/manganese");
        public static final TagKey<Item> ORES_PYROXENE = forge("ores/pyroxene");

        public static final TagKey<Item> RAW_ALUMINIUM = forge("raw_materials/aluminium");
        public static final TagKey<Item> RAW_TIN = forge("raw_materials/tin");
        public static final TagKey<Item> RAW_LEAD = forge("raw_materials/lead");
        public static final TagKey<Item> RAW_ZINC = forge("raw_materials/zinc");
        public static final TagKey<Item> RAW_RMA7012 = forge("raw_materials/rma70-12");
        public static final TagKey<Item> RAW_MANGANESE = forge("raw_materials/manganese");

        public static final TagKey<Item> WIRE_COPPER = forge("wires/copper");

        public static final TagKey<Item> ORIGINITE = forge("gems/originite");
        public static final TagKey<Item> ORIGINIUM_PRIME = forge("gems/originium");

        public static final TagKey<Item> MORTAR = forge("tools/mortar");
        public static final TagKey<Item> CUTTER = forge("tools/cutter");
        public static final TagKey<Item> KNIFE = forge("tools/knife");
        public static final TagKey<Item> KNIVES = forge("tools/knives");
        public static final TagKey<Item> HAMMER = forge("tools/crafting_hammer");
        public static final TagKey<Item> SAW = forge("tools/saw");

        public static final TagKey<Item> SLEDGEHAMMER = forge("sledgehammer");

        public static final TagKey<Item> TREE_SAP = forge("tree_sap");

        public static final TagKey<Item> EXTRUSION_MOLD = mod("mold");

        //Mekanism Compat Layer
        public static final TagKey<Item> CIRCUITS = forge("circuits");
        public static final TagKey<Item> CIRCUITS_BASIC = forge("circuits/basic");
        public static final TagKey<Item> CIRCUITS_ADVANCED = forge("circuits/advanced");

        public static final TagKey<Item> CIRCUITS_CRYSTALLINE = forge("circuits/crystalline");
        public static final TagKey<Item> CURIOS_BACK = curios("back");
        public static final TagKey<Item> CURIOS_HEAD = curios("head");
        public static final TagKey<Item> CURIOS_RING = curios("ring");

        private static TagKey<Item> forge(String path) {
            return ItemTags.create(new ResourceLocation("forge", path));
        }

        private static TagKey<Item> curios(String path) {
            return ItemTags.create(new ResourceLocation("curios", path));
        }

        private static TagKey<Item> mod(String path) {
            return ItemTags.create(new ResourceLocation(Constants.MODID, path));
        }
    }
}
