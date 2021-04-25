package com.yor42.projectazure.data;

import com.yor42.projectazure.libs.defined;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
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

        public static final ITag.INamedTag<Item> ORES_ALUMINIUM = forge("ores/aluminium");
        public static final ITag.INamedTag<Item> ORES_COPPER = forge("ores/copper");
        public static final ITag.INamedTag<Item> ORES_TIN = forge("ores/tin");
        public static final ITag.INamedTag<Item> ORES_LEAD = forge("ores/lead");

        private static ITag.INamedTag<Item> forge(String path) {
            return ItemTags.makeWrapperTag(new ResourceLocation("forge", path).toString());
        }

        private static ITag.INamedTag<Item> mod(String path) {
            return ItemTags.makeWrapperTag(new ResourceLocation(defined.MODID, path).toString());
        }
    }
}
