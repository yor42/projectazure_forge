package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.client.renderer.items.ItemRecruitBeaconRenderer;
import com.yor42.projectazure.gameobject.blocks.*;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.MultiblockSteelFrame;
import com.yor42.projectazure.gameobject.items.AnimateableMachineBlockItems;
import com.yor42.projectazure.gameobject.items.PAOreBlockItem;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class registerBlocks {

    public static final RegistryObject<Block> BAUXITE_ORE = registerMetalOre_Stone("aluminium");
    public static final RegistryObject<Block> COPPER_ORE = registerMetalOre_Stone("copper");
    public static final RegistryObject<Block> TIN_ORE = registerMetalOre_Stone("tin");
    public static final RegistryObject<Block> LEAD_ORE = registerMetalOre_Stone("lead");
    public static final RegistryObject<Block> ZINC_ORE = registerMetalOre_Stone("zinc");
    public static final RegistryObject<Block> ORIROCK = register("orirock",() -> new OreBlock(AbstractBlock.Properties.create(Material.ROCK).harvestLevel(0).hardnessAndResistance(1.5F, 3F)), Main.PA_RESOURCES);

    public static final RegistryObject<Block> MACHINE_FRAME = register("machine_frame", () ->
            new MultiblockStructureBlocks((AbstractBlock.Properties.create(Material.IRON).hardnessAndResistance(3, 10).harvestLevel(2).sound(SoundType.METAL))){
                @Override
                public TileEntity createTileEntity(BlockState state, IBlockReader world) {
                    return new MultiblockSteelFrame();
                }
            }, Main.PA_RESOURCES);


    public static final RegistryObject<Block> METAL_PRESS = register("metal_press", MetalPressBlock::new, Main.PA_MACHINES);
    public static final RegistryObject<Block> ALLOY_FURNACE = register("alloy_furnace", AlloyFurnaceBlock::new, Main.PA_MACHINES);

    public static final RegistryObject<Block> DRYDOCKCONTROLLER = register("multiblock_controller",()-> new blockMultiblockDryDockController(AbstractBlock.Properties.create(Material.IRON).hardnessAndResistance(3, 10).harvestLevel(2).sound(SoundType.METAL).notSolid()), Main.PA_MACHINES);
    public static final RegistryObject<Block> REENFORCEDCONCRETE = register("reenforced_concrete",()-> new MultiblockStructureBlocks(AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(3, 10).harvestLevel(2).sound(SoundType.STONE).notSolid()), Main.PA_MACHINES);

    public static final RegistryObject<Block> RECRUIT_BEACON = registerAnimatedMachines("recruit_beacon", RecruitBeaconBlock::new, Main.PA_MACHINES, new Item.Properties().setISTER(()-> ItemRecruitBeaconRenderer::new));

    private static <T extends Block> RegistryObject<T> register_noblock(String name, Supplier<T> block){
        return registerManager.BLOCKS.register(name, block);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block, ItemGroup group){
        return register(name, block, group, new Item.Properties().group(group));
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block, ItemGroup group, Item.Properties properties){
        RegistryObject<T> ret = register_noblock(name, block);
        registerManager.ITEMS.register(name, () -> new BlockItem(ret.get(), properties.group(group)));
        return ret;
    }

    private static <T extends Block> RegistryObject<T> registerAnimatedMachines(String name, Supplier<T> block, ItemGroup group, Item.Properties properties){
        RegistryObject<T> ret = register_noblock(name, block);
        registerManager.ITEMS.register(name, () -> new AnimateableMachineBlockItems(ret.get(), properties.group(group)));
        return ret;
    }

    private static RegistryObject<Block> registerMetalOre_Stone(String materialName){
        return registerMetalOre("ore_stone_"+materialName, materialName);
    }

    private static RegistryObject<Block> registerMetalOre(String registryName, String materialName){
        RegistryObject<Block> ret = register_noblock(registryName, () -> new PAOreBlock(materialName));
        registerManager.ITEMS.register(registryName, () -> new PAOreBlockItem(ret.get(), materialName));
        return ret;
    }

    public static void register(){}

}
