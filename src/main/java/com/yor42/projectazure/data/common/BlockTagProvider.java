package com.yor42.projectazure.data.common;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class BlockTagProvider extends BlockTagsProvider {

    public BlockTagProvider(DataGenerator generatorIn, @Nullable ExistingFileHelper existingFileHelper) {
        super(generatorIn, Constants.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(ModTags.Blocks.ORES_ALUMINIUM).add(Main.BAUXITE_ORE.get());
        this.tag(ModTags.Blocks.ORES_COPPER).add(Main.COPPER_ORE.get());
        this.tag(ModTags.Blocks.ORES_LEAD).add(Main.LEAD_ORE.get());
        this.tag(ModTags.Blocks.ORES_TIN).add(Main.TIN_ORE.get());
        this.tag(ModTags.Blocks.ORES_ZINC).add(Main.ZINC_ORE.get());
        this.tag(ModTags.Blocks.ORES_ORIROCK).add(Main.ORIROCK.get());
    }
}
