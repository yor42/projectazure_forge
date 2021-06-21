package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.client.renderer.items.ItemRecruitBeaconRenderer;
import com.yor42.projectazure.gameobject.blocks.AlloyFurnaceBlock;
import com.yor42.projectazure.gameobject.blocks.MetalPressBlock;
import com.yor42.projectazure.gameobject.blocks.PAOreBlock;
import com.yor42.projectazure.gameobject.blocks.RecruitBeaconBlock;
import com.yor42.projectazure.gameobject.items.AnimateableMachineBlockItems;
import com.yor42.projectazure.gameobject.items.PAOreBlockItem;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class registerBlocks {

    public static final RegistryObject<Block> BAUXITE_ORE = registerOre("aluminium");
    public static final RegistryObject<Block> COPPER_ORE = registerOre("copper");
    public static final RegistryObject<Block> TIN_ORE = registerOre("tin");
    public static final RegistryObject<Block> LEAD_ORE = registerOre("lead");
    public static final RegistryObject<Block> ZINC_ORE = registerOre("zinc");

    public static final RegistryObject<Block> MACHINE_FRAME = register("machine_frame", () ->
            new Block((AbstractBlock.Properties.create(Material.IRON).hardnessAndResistance(3, 10).harvestLevel(2).sound(SoundType.METAL).notSolid())), Main.PA_RESOURCES);


    public static final RegistryObject<Block> METAL_PRESS = register("metal_press", MetalPressBlock::new, Main.PA_MACHINES);
    public static final RegistryObject<Block> ALLOY_FURNACE = register("alloy_furnace", AlloyFurnaceBlock::new, Main.PA_MACHINES);

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

    private static RegistryObject<Block> registerOre(String materialName){
        return registerOre("ore_stone_"+materialName, materialName);
    }

    private static RegistryObject<Block> registerOre(String registryName, String materialName){
        RegistryObject<Block> ret = register_noblock(registryName, () -> new PAOreBlock(materialName));
        registerManager.ITEMS.register(registryName, () -> new PAOreBlockItem(ret.get(), materialName));
        return ret;
    }

    public static void register(){}

}
