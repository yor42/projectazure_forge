package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.gameobject.blocks.tileentity.*;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.MultiBlockReenforcedConcrete;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.MultiblockDrydockTE;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.MultiblockSteelFrame;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class registerTE {

    public static final RegistryObject<TileEntityType<TileEntityMetalPress>> METAL_PRESS = register("metal_press_te", TileEntityMetalPress::new, registerBlocks.METAL_PRESS);
    public static final RegistryObject<TileEntityType<TileEntityAlloyFurnace>> ALLOY_FURNACE = register("alloy_furnace", TileEntityAlloyFurnace::new, registerBlocks.ALLOY_FURNACE);
    public static final RegistryObject<TileEntityType<TileEntityBasicRefinery>> BASIC_REFINERY = register("basic_refinery_te", TileEntityBasicRefinery::new, registerBlocks.BASIC_REFINERY);
    public static final RegistryObject<TileEntityType<TileEntityRecruitBeacon>> RECRUIT_BEACON = register("recruit_beacon_te", TileEntityRecruitBeacon::new, registerBlocks.RECRUIT_BEACON);
    public static final RegistryObject<TileEntityType<TileEntityCrystalGrowthChamber>> CRYSTAL_GROWTH_CHAMBER = register("crystal_growth_chamber_te", TileEntityCrystalGrowthChamber::new, registerBlocks.CRYSTAL_GROWTH_CHAMBER);
    public static final RegistryObject<TileEntityType<MultiblockDrydockTE>> DRYDOCK = register("drydock", MultiblockDrydockTE::new, registerBlocks.DRYDOCKCONTROLLER);
    public static final RegistryObject<TileEntityType<MultiBlockReenforcedConcrete>> MULTIBLOCKSTRUCTURE_CONCRETE = register("multiblock_structure_concrete_te", MultiBlockReenforcedConcrete::new, registerBlocks.REENFORCEDCONCRETE);
    public static final RegistryObject<TileEntityType<MultiblockSteelFrame>> MULTIBLOCKSTRUCTURE_STEELFRAME = register("multiblock_structure_steel_te", MultiblockSteelFrame::new, registerBlocks.MACHINE_FRAME);

    private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String name, Supplier<T> factory, RegistryObject<? extends Block> block){

        //About Mojang's Data Fixer. Afaik Mod can't even use it. and its annotated to non null. KEKW
        //noinspection ConstantConditions
        return registerManager.TILE_ENTITY.register(name, () -> TileEntityType.Builder.create(factory, block.get()).build(null));
    }

    static void register(){}

}
