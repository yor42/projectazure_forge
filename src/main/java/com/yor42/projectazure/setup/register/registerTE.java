package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.gameobject.blocks.tileentity.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.RegistryObject;

import java.util.ArrayList;
import java.util.function.Supplier;

public class registerTE {

    public static final RegistryObject<BlockEntityType<TileEntityMetalPress>> METAL_PRESS = register("metal_press_te", TileEntityMetalPress::new, RegisterBlocks.METAL_PRESS);
    public static final RegistryObject<BlockEntityType<TileEntityAlloyFurnace>> ALLOY_FURNACE = register("alloy_furnace", TileEntityAlloyFurnace::new, RegisterBlocks.ALLOY_FURNACE);
    public static final RegistryObject<BlockEntityType<TileEntityBasicRefinery>> BASIC_REFINERY = register("basic_refinery_te", TileEntityBasicRefinery::new, RegisterBlocks.BASIC_REFINERY);
    public static final RegistryObject<BlockEntityType<TileEntityRecruitBeacon>> RECRUIT_BEACON = register("recruit_beacon_te", TileEntityRecruitBeacon::new, RegisterBlocks.RECRUIT_BEACON);
    public static final RegistryObject<BlockEntityType<TileEntityBasicChemicalReactor>> BASIC_CHEMICAL_REACTOR = register("basic_chemical_reactor_te", TileEntityBasicChemicalReactor::new, RegisterBlocks.BASIC_CHEMICAL_REACTOR);
    public static final RegistryObject<BlockEntityType<TileEntityCrystalGrowthChamber>> CRYSTAL_GROWTH_CHAMBER = register("crystal_growth_chamber_te", TileEntityCrystalGrowthChamber::new, RegisterBlocks.CRYSTAL_GROWTH_CHAMBER);
    public static final RegistryObject<BlockEntityType<TileEntityPantry>> PANTRY = register("pantry_te", TileEntityPantry::new, RegisterBlocks.OAK_PANTRY, RegisterBlocks.CRIMSON_PANTRY, RegisterBlocks.JUNGLE_PANTRY, RegisterBlocks.ACACIA_PANTRY, RegisterBlocks.BIRCH_PANTRY, RegisterBlocks.SPRUCE_PANTRY, RegisterBlocks.WARPED_PANTRY, RegisterBlocks.DARK_OAK_PANTRY);
    @SafeVarargs
    private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String name, Supplier<T> factory, RegistryObject<Block>... block){
        //About Mojang's Data Fixer. Afaik Mod can't even use it. and its annotated to non null. KEKW
        //noinspection ConstantConditions
        return RegisterContainer.TILE_ENTITY.register(name, () -> BlockEntityType.Builder.of(factory, RegistryObject2Block(block)).build(null));
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
