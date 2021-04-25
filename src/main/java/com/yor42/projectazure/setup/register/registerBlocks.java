package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.Main;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class registerBlocks {

    public static final RegistryObject<Block> BAUXITE_ORE = register("ore_stone_bauxite", () ->
            new OreBlock((AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(3, 10).harvestLevel(2).sound(SoundType.STONE))), Main.PA_RESOURCES);

    public static final RegistryObject<Block> COPPER_ORE = register("ore_stone_copper", () ->
            new OreBlock((AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(3, 10).harvestLevel(2).sound(SoundType.STONE))), Main.PA_RESOURCES);

    public static final RegistryObject<Block> TIN_ORE = register("ore_stone_tin", () ->
            new OreBlock((AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(3, 10).harvestLevel(2).sound(SoundType.STONE))), Main.PA_RESOURCES);

    public static final RegistryObject<Block> LEAD_ORE = register("ore_stone_lead", () ->
            new OreBlock((AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(3, 10).harvestLevel(2).sound(SoundType.STONE))), Main.PA_RESOURCES);

    private static <T extends Block> RegistryObject<T> register_noblock(String name, Supplier<T> block){
        return registerManager.BLOCKS.register(name, block);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block, ItemGroup group){
        RegistryObject<T> ret = register_noblock(name, block);
        registerManager.ITEMS.register(name, () -> new BlockItem(ret.get(), new Item.Properties().group(group)));
        return ret;
    }

    public static void register(){}

}
