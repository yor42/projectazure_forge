package com.yor42.projectazure.data.common;

import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.setup.register.registerBlocks;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class BlockTagProvider extends BlockTagsProvider {

    public BlockTagProvider(DataGenerator generatorIn, @Nullable ExistingFileHelper existingFileHelper) {
        super(generatorIn, Constants.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(ModTags.Blocks.ORES_ALUMINIUM).add(registerBlocks.BAUXITE_ORE.get());
        this.tag(ModTags.Blocks.ORES_COPPER).add(registerBlocks.COPPER_ORE.get());
        this.tag(ModTags.Blocks.ORES_LEAD).add(registerBlocks.LEAD_ORE.get());
        this.tag(ModTags.Blocks.ORES_TIN).add(registerBlocks.TIN_ORE.get());
        this.tag(ModTags.Blocks.ORES_ZINC).add(registerBlocks.ZINC_ORE.get());
        this.tag(ModTags.Blocks.ORES_ORIROCK).add(registerBlocks.ORIROCK.get());
        this.tag(ModTags.Blocks.ORES_RMA7012).add(registerBlocks.RMA_7012_ORE.get());
        this.tag(ModTags.Blocks.ORES_MANGANESE).add(registerBlocks.MANGANESE_ORE.get());

        this.tag(ModTags.Blocks.BLOCK_ZINC).add(registerBlocks.ZINC_BLOCK.get());
        this.tag(ModTags.Blocks.BLOCK_ALUMINIUM).add(registerBlocks.ALUMINIUM_BLOCK.get());
        this.tag(ModTags.Blocks.BLOCK_COPPER).add(registerBlocks.COPPER_BLOCK.get());
        this.tag(ModTags.Blocks.BLOCK_TIN).add(registerBlocks.TIN_BLOCK.get());
        this.tag(ModTags.Blocks.BLOCK_LEAD).add(registerBlocks.LEAD_BLOCK.get());
        this.tag(ModTags.Blocks.BLOCK_BRASS).add(registerBlocks.BRASS_BLOCK.get());
        this.tag(ModTags.Blocks.BLOCK_BRONZE).add(registerBlocks.BRONZE_BLOCK.get());
        this.tag(ModTags.Blocks.BLOCK_RMA7012).add(registerBlocks.RMA_7012_BLOCK.get());
        this.tag(ModTags.Blocks.BLOCK_RMA7024).add(registerBlocks.RMA_7024_BLOCK.get());
        this.tag(ModTags.Blocks.BLOCK_D32).add(registerBlocks.D32_BLOCK.get());
        this.tag(ModTags.Blocks.BLOCK_STEEL).add(registerBlocks.STEEL_BLOCK.get());
        this.tag(ModTags.Blocks.BLOCK_INCANDESCENT_ALLOY).add(registerBlocks.INCANDESCENT_ALLOY_BLOCK.get());
        this.tag(ModTags.Blocks.BLOCK_MANGANESE).add(registerBlocks.MANGANESE_BLOCK.get());

        this.tag(Tags.Blocks.ORES).add(registerBlocks.BAUXITE_ORE.get()).add(registerBlocks.COPPER_ORE.get()).add(registerBlocks.LEAD_ORE.get()).add(registerBlocks.TIN_ORE.get()).add(registerBlocks.ZINC_ORE.get()).add(registerBlocks.ORIROCK.get()).add(registerBlocks.RMA_7012_ORE.get()).add(registerBlocks.MANGANESE_ORE.get());
        this.tag(Tags.Blocks.STORAGE_BLOCKS).add(registerBlocks.ZINC_BLOCK.get()).add(registerBlocks.ALUMINIUM_BLOCK.get()).add(registerBlocks.COPPER_BLOCK.get()).add(registerBlocks.TIN_BLOCK.get()).add(registerBlocks.LEAD_BLOCK.get()).add(registerBlocks.BRONZE_BLOCK.get()).add(registerBlocks.RMA_7012_BLOCK.get()).add(registerBlocks.RMA_7024_BLOCK.get()).add(registerBlocks.D32_BLOCK.get()).add(registerBlocks.STEEL_BLOCK.get())
                .add(registerBlocks.MANGANESE_BLOCK.get()).add(registerBlocks.INCANDESCENT_ALLOY_BLOCK.get());
    }
}
