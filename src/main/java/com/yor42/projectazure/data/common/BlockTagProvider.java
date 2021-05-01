package com.yor42.projectazure.data.common;

import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.libs.defined;
import com.yor42.projectazure.setup.register.registerBlocks;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class BlockTagProvider extends BlockTagsProvider {

    public BlockTagProvider(DataGenerator generatorIn, @Nullable ExistingFileHelper existingFileHelper) {
        super(generatorIn, defined.MODID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
        this.getOrCreateBuilder(ModTags.Blocks.ORES_ALUMINIUM).add(registerBlocks.BAUXITE_ORE.get());
        this.getOrCreateBuilder(ModTags.Blocks.ORES_COPPER).add(registerBlocks.COPPER_ORE.get());
        this.getOrCreateBuilder(ModTags.Blocks.ORES_LEAD).add(registerBlocks.LEAD_ORE.get());
        this.getOrCreateBuilder(ModTags.Blocks.ORES_TIN).add(registerBlocks.TIN_ORE.get());
    }
}