package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.gameobject.blocks.MetalPressBlock;
import com.yor42.projectazure.gameobject.blocks.tileentity.TileEntityAlloyFurnace;
import com.yor42.projectazure.gameobject.blocks.tileentity.TileEntityMetalPress;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class registerTE {

    public static final RegistryObject<TileEntityType<TileEntityMetalPress>> METAL_PRESS = register("metal_press", TileEntityMetalPress::new, registerBlocks.METAL_PRESS);
    public static final RegistryObject<TileEntityType<TileEntityAlloyFurnace>> ALLOY_FURNACE = register("alloy_furnace", TileEntityAlloyFurnace::new, registerBlocks.ALLOY_FURNACE);

    private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String name, Supplier<T> factory, RegistryObject<? extends Block> block){

        //About Mojang's Data Fixer. Afaik Mod can't even use it. and its annotated to non null. KEKW
        //noinspection ConstantConditions
        return registerManager.TILE_ENTITY.register(name, () -> TileEntityType.Builder.create(factory, block.get()).build(null));
    }

    static void register(){}

}
