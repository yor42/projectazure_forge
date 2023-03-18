package com.yor42.projectazure.data.common;

import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.setup.register.RegisterBlocks;
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
        this.tag(ModTags.Blocks.ORES_ALUMINIUM).add(RegisterBlocks.BAUXITE_ORE.get());
        this.tag(ModTags.Blocks.ORES_COPPER).add(RegisterBlocks.COPPER_ORE.get());
        this.tag(ModTags.Blocks.ORES_LEAD).add(RegisterBlocks.LEAD_ORE.get());
        this.tag(ModTags.Blocks.ORES_TIN).add(RegisterBlocks.TIN_ORE.get());
        this.tag(ModTags.Blocks.ORES_ZINC).add(RegisterBlocks.ZINC_ORE.get());
        this.tag(ModTags.Blocks.ORES_ORIROCK).add(RegisterBlocks.ORIROCK.get());
        this.tag(ModTags.Blocks.ORES_RMA7012).add(RegisterBlocks.RMA_7012_ORE.get());
        this.tag(ModTags.Blocks.ORES_MANGANESE).add(RegisterBlocks.MANGANESE_ORE.get());
        this.tag(ModTags.Blocks.ORES_PYROXENE).add(RegisterBlocks.PYROXENE_ORE.get());

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

        this.tag(Tags.Blocks.ORES).add(RegisterBlocks.BAUXITE_ORE.get()).add(RegisterBlocks.COPPER_ORE.get()).add(RegisterBlocks.LEAD_ORE.get()).add(RegisterBlocks.TIN_ORE.get()).add(RegisterBlocks.ZINC_ORE.get()).add(RegisterBlocks.ORIROCK.get()).add(RegisterBlocks.RMA_7012_ORE.get()).add(RegisterBlocks.MANGANESE_ORE.get());
        this.tag(Tags.Blocks.STORAGE_BLOCKS).add(RegisterBlocks.ZINC_BLOCK.get()).add(RegisterBlocks.ALUMINIUM_BLOCK.get()).add(RegisterBlocks.COPPER_BLOCK.get()).add(RegisterBlocks.TIN_BLOCK.get()).add(RegisterBlocks.LEAD_BLOCK.get()).add(RegisterBlocks.BRONZE_BLOCK.get()).add(RegisterBlocks.RMA_7012_BLOCK.get()).add(RegisterBlocks.RMA_7024_BLOCK.get()).add(RegisterBlocks.D32_BLOCK.get()).add(RegisterBlocks.STEEL_BLOCK.get())
                .add(RegisterBlocks.MANGANESE_BLOCK.get()).add(RegisterBlocks.INCANDESCENT_ALLOY_BLOCK.get());
    }
}
