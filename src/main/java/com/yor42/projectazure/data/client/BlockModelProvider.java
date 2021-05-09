package com.yor42.projectazure.data.client;

import com.yor42.projectazure.libs.defined;
import com.yor42.projectazure.setup.register.registerBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockModelProvider extends net.minecraftforge.client.model.generators.BlockStateProvider {
    public BlockModelProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, defined.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(registerBlocks.BAUXITE_ORE.get());
        simpleBlock(registerBlocks.COPPER_ORE.get());
        simpleBlock(registerBlocks.TIN_ORE.get());
        simpleBlock(registerBlocks.LEAD_ORE.get());
        simpleBlock(registerBlocks.MACHINE_FRAME.get());

    }
}
