package com.yor42.projectazure.data;

import com.yor42.projectazure.libs.defined;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class ModTags {
    public static final class Blocks {
        public static final ITag.INamedTag<Block> ORES_ALUMINIUM = forge("ores/aluminium");
        public static final ITag.INamedTag<Block> ORES_COPPER = forge("ores/copper");
        public static final ITag.INamedTag<Block> ORES_TIN = forge("ores/tin");
        public static final ITag.INamedTag<Block> ORES_LEAD = forge("ores/lead");

        private static ITag.INamedTag<Block> forge(String path) {
            return BlockTags.makeWrapperTag(new ResourceLocation("forge", path).toString());
        }

        private static ITag.INamedTag<Block> mod(String path) {
            return BlockTags.makeWrapperTag(new ResourceLocation(defined.MODID, path).toString());
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

        public static final ITag.INamedTag<Item> PLATE_ALUMINIUM = forge("plates/aluminium");
        public static final ITag.INamedTag<Item> PLATE_COPPER = forge("plates/copper");
        public static final ITag.INamedTag<Item> PLATE_TIN = forge("plates/tin");
        public static final ITag.INamedTag<Item> PLATE_BRONZE = forge("plates/bronze");
        public static final ITag.INamedTag<Item> PLATE_LEAD = forge("plates/lead");
        public static final ITag.INamedTag<Item> PLATE_IRON = forge("plates/iron");
        public static final ITag.INamedTag<Item> PLATE_ZINC = forge("plates/zinc");
        public static final ITag.INamedTag<Item> PLATE_STEEL = forge("plates/steel");

        public static final ITag.INamedTag<Item> DUST_ALUMINIUM = forge("dusts/aluminium");
        public static final ITag.INamedTag<Item> DUST_COPPER = forge("dusts/copper");
        public static final ITag.INamedTag<Item> DUST_TIN = forge("dusts/tin");
        public static final ITag.INamedTag<Item> DUST_BRONZE = forge("dusts/bronze");
        public static final ITag.INamedTag<Item> DUST_LEAD = forge("dusts/lead");
        public static final ITag.INamedTag<Item> DUST_ZINC = forge("dusts/zinc");
        public static final ITag.INamedTag<Item> DUST_IRON = forge("dusts/iron");
        public static final ITag.INamedTag<Item> DUST_COAL = forge("dusts/coal");
        public static final ITag.INamedTag<Item> DUST_STEEL = forge("dusts/steel");

        public static final ITag.INamedTag<Item> ORES_ALUMINIUM = forge("ores/aluminium");
        public static final ITag.INamedTag<Item> ORES_COPPER = forge("ores/copper");
        public static final ITag.INamedTag<Item> ORES_TIN = forge("ores/tin");
        public static final ITag.INamedTag<Item> ORES_LEAD = forge("ores/lead");
        //public static final ITag.INamedTag<Item> ORES_ZINC = forge("ores/zinc");

        public static final ITag.INamedTag<Item> MORTAR = forge("mortar");
        public static final ITag.INamedTag<Item> CUTTER = forge("cutter");
        public static final ITag.INamedTag<Item> HAMMER = forge("hammer");

        public static final ITag.INamedTag<Item> EXTRUSION_MOLD = mod("mold");


        private static ITag.INamedTag<Item> forge(String path) {
            return ItemTags.makeWrapperTag(new ResourceLocation("forge", path).toString());
        }

        private static ITag.INamedTag<Item> mod(String path) {
            return ItemTags.makeWrapperTag(new ResourceLocation(defined.MODID, path).toString());
        }
    }
}
