package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.gameobject.blocks.tileentity.*;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;

import java.util.ArrayList;
import java.util.function.Supplier;

public class registerTE {

    public static final RegistryObject<TileEntityType<TileEntityMetalPress>> METAL_PRESS = register("metal_press_te", TileEntityMetalPress::new, registerBlocks.METAL_PRESS);
    public static final RegistryObject<TileEntityType<TileEntityAlloyFurnace>> ALLOY_FURNACE = register("alloy_furnace", TileEntityAlloyFurnace::new, registerBlocks.ALLOY_FURNACE);
    public static final RegistryObject<TileEntityType<TileEntityBasicRefinery>> BASIC_REFINERY = register("basic_refinery_te", TileEntityBasicRefinery::new, registerBlocks.BASIC_REFINERY);
    public static final RegistryObject<TileEntityType<TileEntityRecruitBeacon>> RECRUIT_BEACON = register("recruit_beacon_te", TileEntityRecruitBeacon::new, registerBlocks.RECRUIT_BEACON);
    public static final RegistryObject<TileEntityType<TileEntityCrystalGrowthChamber>> CRYSTAL_GROWTH_CHAMBER = register("crystal_growth_chamber_te", TileEntityCrystalGrowthChamber::new, registerBlocks.CRYSTAL_GROWTH_CHAMBER);
    public static final RegistryObject<TileEntityType<TileEntityPantry>> PANTRY = register("pantry_te", TileEntityPantry::new, registerBlocks.OAK_PANTRY, registerBlocks.CRIMSON_PANTRY, registerBlocks.JUNGLE_PANTRY, registerBlocks.ACACIA_PANTRY, registerBlocks.BIRCH_PANTRY, registerBlocks.SPRUCE_PANTRY, registerBlocks.WARPED_PANTRY, registerBlocks.DARK_OAK_PANTRY);
    @SafeVarargs
    private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String name, Supplier<T> factory, RegistryObject<Block>... block){
        //About Mojang's Data Fixer. Afaik Mod can't even use it. and its annotated to non null. KEKW
        //noinspection ConstantConditions
        return RegisterContainer.TILE_ENTITY.register(name, () -> TileEntityType.Builder.of(factory, RegistryObject2Block(block)).build(null));
    }

    @SafeVarargs
    private static Block[] RegistryObject2Block(RegistryObject<Block>... blocks){
        ArrayList<Block> list = new ArrayList<>();
        for(RegistryObject<Block> block:blocks){
            list.add(block.get());
        }
        return list.toArray(new Block[0]);
    }

    static void register(){}

}
