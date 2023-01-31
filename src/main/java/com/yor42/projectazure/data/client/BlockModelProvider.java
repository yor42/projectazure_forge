package com.yor42.projectazure.data.client;

import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.setup.register.registerBlocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockModelProvider extends net.minecraftforge.client.model.generators.BlockStateProvider {
    public BlockModelProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Constants.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(registerBlocks.BAUXITE_ORE.get());
        simpleBlock(registerBlocks.COPPER_ORE.get());
        simpleBlock(registerBlocks.TIN_ORE.get());
        simpleBlock(registerBlocks.LEAD_ORE.get());
        simpleBlock(registerBlocks.ZINC_ORE.get());
        simpleBlock(registerBlocks.MACHINE_FRAME.get());
        simpleBlock(registerBlocks.ORIROCK.get());
        simpleBlock(registerBlocks.REENFORCEDCONCRETE.get());
        simpleBlock(registerBlocks.REENFORCED_PLANK.get());
        simpleBlock(registerBlocks.COBBLED_ORIROCK.get());;
        simpleBlock(registerBlocks.MACHINE_COMPONENTBLOCK.get());

        slabBlock((SlabBlock) registerBlocks.MACHINE_FRAME_SLAB.get(), new ResourceLocation(Constants.MODID, "block/machine_frame"), new ResourceLocation(Constants.MODID, "block/machine_frame"));
    }
}
