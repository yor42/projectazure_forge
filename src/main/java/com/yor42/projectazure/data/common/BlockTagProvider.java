package com.yor42.projectazure.data.common;

import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.setup.register.RegisterBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class BlockTagProvider extends BlockTagsProvider {

    public BlockTagProvider(DataGenerator generatorIn, @Nullable ExistingFileHelper existingFileHelper) {
        super(generatorIn, Constants.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(ModTags.Blocks.ORES_ALUMINIUM).add(RegisterBlocks.BAUXITE_ORE.get(), RegisterBlocks.BAUXITE_ORE_DEEPSLATE.get());
        this.tag(ModTags.Blocks.ORES_LEAD).add(RegisterBlocks.LEAD_ORE.get(),RegisterBlocks.LEAD_ORE_DEEPSLATE.get());
        this.tag(ModTags.Blocks.ORES_TIN).add(RegisterBlocks.TIN_ORE.get(), RegisterBlocks.TIN_ORE_DEEPSLATE.get());
        this.tag(ModTags.Blocks.ORES_ZINC).add(RegisterBlocks.ZINC_ORE.get(), RegisterBlocks.ZINC_ORE_DEEPSLATE.get());
        this.tag(ModTags.Blocks.ORES_ORIROCK).add(RegisterBlocks.ORIROCK.get());
        this.tag(ModTags.Blocks.ORES_RMA7012).add(RegisterBlocks.RMA_7012_ORE.get(), RegisterBlocks.RMA_7012_ORE_DEEPSLATE.get());
        this.tag(ModTags.Blocks.ORES_MANGANESE).add(RegisterBlocks.MANGANESE_ORE.get(), RegisterBlocks.MANGANESE_ORE_DEEPSLATE.get());
        this.tag(ModTags.Blocks.ORES_PYROXENE).add(RegisterBlocks.PYROXENE_ORE.get(), RegisterBlocks.PYROXENE_ORE_DEEPSLATE.get());

        this.tag(ModTags.Blocks.BLOCK_ZINC).add(RegisterBlocks.ZINC_BLOCK.get());
        this.tag(ModTags.Blocks.BLOCK_ALUMINIUM).add(RegisterBlocks.ALUMINIUM_BLOCK.get());
        this.tag(ModTags.Blocks.BLOCK_COPPER).add(RegisterBlocks.COPPER_BLOCK.get());
        this.tag(ModTags.Blocks.BLOCK_TIN).add(RegisterBlocks.TIN_BLOCK.get());
        this.tag(ModTags.Blocks.BLOCK_LEAD).add(RegisterBlocks.LEAD_BLOCK.get());
        this.tag(ModTags.Blocks.BLOCK_BRASS).add(RegisterBlocks.BRASS_BLOCK.get());
        this.tag(ModTags.Blocks.BLOCK_BRONZE).add(RegisterBlocks.BRONZE_BLOCK.get());
        this.tag(ModTags.Blocks.BLOCK_RMA7012).add(RegisterBlocks.RMA_7012_BLOCK.get());
        this.tag(ModTags.Blocks.BLOCK_RMA7024).add(RegisterBlocks.RMA_7024_BLOCK.get());
        this.tag(ModTags.Blocks.BLOCK_D32).add(RegisterBlocks.D32_BLOCK.get());
        this.tag(ModTags.Blocks.BLOCK_STEEL).add(RegisterBlocks.STEEL_BLOCK.get());
        this.tag(ModTags.Blocks.BLOCK_INCANDESCENT_ALLOY).add(RegisterBlocks.INCANDESCENT_ALLOY_BLOCK.get());
        this.tag(ModTags.Blocks.BLOCK_MANGANESE).add(RegisterBlocks.MANGANESE_BLOCK.get());

        this.tag(ModTags.Blocks.BLOCK_RAW_ALUMINIUM).add(RegisterBlocks.RAW_BAUXITE_BLOCK. get());
        this.tag(ModTags.Blocks.BLOCK_RAW_LEAD).add(RegisterBlocks.RAW_LEAD_BLOCK. get());
        this.tag(ModTags.Blocks.BLOCK_RAW_TIN).add(RegisterBlocks.RAW_TIN_BLOCK. get());
        this.tag(ModTags.Blocks.BLOCK_RAW_RMA7012).add(RegisterBlocks.RAW_RMA_7012_BLOCK. get());
        this.tag(ModTags.Blocks.BLOCK_RAW_ZINC).add(RegisterBlocks.RAW_ZINC_BLOCK. get());
        this.tag(ModTags.Blocks.BLOCK_RAW_MANGANESE).add(RegisterBlocks.RAW_MANGANESE_BLOCK. get());

        this.tag(Tags.Blocks.ORES).add(RegisterBlocks.BAUXITE_ORE.get()).add(RegisterBlocks.LEAD_ORE.get()).add(RegisterBlocks.TIN_ORE.get()).add(RegisterBlocks.ZINC_ORE.get()).add(RegisterBlocks.ORIROCK.get()).add(RegisterBlocks.RMA_7012_ORE.get()).add(RegisterBlocks.MANGANESE_ORE.get())
                .add(RegisterBlocks.BAUXITE_ORE_DEEPSLATE.get()).add(RegisterBlocks.LEAD_ORE_DEEPSLATE.get()).add(RegisterBlocks.TIN_ORE_DEEPSLATE.get()).add(RegisterBlocks.ZINC_ORE_DEEPSLATE.get()).add(RegisterBlocks.ORIROCK.get()).add(RegisterBlocks.RMA_7012_ORE_DEEPSLATE.get()).add(RegisterBlocks.MANGANESE_ORE_DEEPSLATE.get());
        this.tag(Tags.Blocks.STORAGE_BLOCKS).add(RegisterBlocks.ZINC_BLOCK.get()).add(RegisterBlocks.ALUMINIUM_BLOCK.get()).add(RegisterBlocks.COPPER_BLOCK.get()).add(RegisterBlocks.TIN_BLOCK.get()).add(RegisterBlocks.LEAD_BLOCK.get()).add(RegisterBlocks.BRONZE_BLOCK.get()).add(RegisterBlocks.RMA_7012_BLOCK.get()).add(RegisterBlocks.RMA_7024_BLOCK.get()).add(RegisterBlocks.D32_BLOCK.get()).add(RegisterBlocks.STEEL_BLOCK.get())
                .add(RegisterBlocks.MANGANESE_BLOCK.get()).add(RegisterBlocks.INCANDESCENT_ALLOY_BLOCK.get(), RegisterBlocks.RAW_MANGANESE_BLOCK.get(), RegisterBlocks.RAW_BAUXITE_BLOCK.get(), RegisterBlocks.RAW_ZINC_BLOCK.get(), RegisterBlocks.RAW_LEAD_BLOCK.get(), RegisterBlocks.RAW_TIN_BLOCK.get(), RegisterBlocks.RAW_RMA_7012_BLOCK.get());
    }
}
