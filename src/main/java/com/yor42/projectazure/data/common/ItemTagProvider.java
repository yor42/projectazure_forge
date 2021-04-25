package com.yor42.projectazure.data.common;

import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.libs.defined;
import com.yor42.projectazure.setup.register.registerItems;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class ItemTagProvider extends ItemTagsProvider {
    public ItemTagProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagProvider, defined.MODID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "ProjectAzure:Item Tags";
    }

    @Override
    protected void registerTags() {
        copy(ModTags.Blocks.ORES_COPPER, ModTags.Items.ORES_COPPER);
        copy(ModTags.Blocks.ORES_TIN, ModTags.Items.ORES_TIN);
        copy(ModTags.Blocks.ORES_LEAD, ModTags.Items.ORES_LEAD);
        copy(ModTags.Blocks.ORES_ALUMINIUM, ModTags.Items.ORES_ALUMINIUM);
        copy(Tags.Blocks.ORES, Tags.Items.ORES);

        this.getOrCreateBuilder(ModTags.Items.INGOT_ALUMINIUM).add(registerItems.INGOT_ALUMINIUM.get());
        this.getOrCreateBuilder(ModTags.Items.INGOT_COPPER).add(registerItems.INGOT_COPPER.get());
        this.getOrCreateBuilder(ModTags.Items.INGOT_TIN).add(registerItems.INGOT_TIN.get());
        this.getOrCreateBuilder(ModTags.Items.INGOT_BRONZE).add(registerItems.INGOT_BRONZE.get());
        this.getOrCreateBuilder(ModTags.Items.INGOT_LEAD).add(registerItems.INGOT_LEAD.get());
    }
}
