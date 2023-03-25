package com.yor42.projectazure.data.client;

import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.setup.register.RegisterBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

import java.util.ArrayList;

public class BlockModelProvider extends net.minecraftforge.client.model.generators.BlockStateProvider {

    public static final ArrayList<RegistryObject<? extends Block>> SIMPLEBLOCKLIST = new ArrayList<>();

    public static final ArrayList<RegistryObject<? extends Block>> OREBLOCKLIST = new ArrayList<>();

    public BlockModelProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Constants.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        for(RegistryObject<? extends Block> block:SIMPLEBLOCKLIST){
            simpleBlock(block.get());
        }



        slabBlock((SlabBlock) RegisterBlocks.MACHINE_FRAME_SLAB.get(), new ResourceLocation(Constants.MODID, "block/machine_frame"), new ResourceLocation(Constants.MODID, "block/machine_frame"));
    }
}
