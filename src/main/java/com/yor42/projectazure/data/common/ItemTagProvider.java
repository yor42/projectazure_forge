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

        copy(ModTags.Blocks.LOG, ModTags.Items.LOG);

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
        this.getOrCreateBuilder(ModTags.Items.INGOT_ZINC).add(registerItems.INGOT_ZINC.get());
        this.getOrCreateBuilder(ModTags.Items.INGOT_STEEL).add(registerItems.INGOT_STEEL.get());

        this.getOrCreateBuilder(ModTags.Items.PLATE_ALUMINIUM).add(registerItems.PLATE_ALUMINIUM.get());
        this.getOrCreateBuilder(ModTags.Items.PLATE_COPPER).add(registerItems.PLATE_COPPER.get());
        this.getOrCreateBuilder(ModTags.Items.PLATE_TIN).add(registerItems.PLATE_TIN.get());
        this.getOrCreateBuilder(ModTags.Items.PLATE_BRONZE).add(registerItems.PLATE_BRONZE.get());
        this.getOrCreateBuilder(ModTags.Items.PLATE_LEAD).add(registerItems.PLATE_LEAD.get());
        this.getOrCreateBuilder(ModTags.Items.PLATE_ZINC).add(registerItems.PLATE_ZINC.get());
        this.getOrCreateBuilder(ModTags.Items.PLATE_IRON).add(registerItems.PLATE_IRON.get());
        this.getOrCreateBuilder(ModTags.Items.PLATE_STEEL).add(registerItems.PLATE_STEEL.get());

        this.getOrCreateBuilder(ModTags.Items.DUST_ALUMINIUM).add(registerItems.DUST_ALUMINIUM.get());
        this.getOrCreateBuilder(ModTags.Items.DUST_COPPER).add(registerItems.DUST_COPPER.get());
        this.getOrCreateBuilder(ModTags.Items.DUST_TIN).add(registerItems.DUST_TIN.get());
        this.getOrCreateBuilder(ModTags.Items.DUST_BRONZE).add(registerItems.DUST_BRONZE.get());
        this.getOrCreateBuilder(ModTags.Items.DUST_LEAD).add(registerItems.DUST_LEAD.get());
        this.getOrCreateBuilder(ModTags.Items.DUST_IRON).add(registerItems.DUST_IRON.get());
        this.getOrCreateBuilder(ModTags.Items.DUST_ZINC).add(registerItems.DUST_ZINC.get());
        this.getOrCreateBuilder(ModTags.Items.DUST_COAL).add(registerItems.DUST_COAL.get());
        this.getOrCreateBuilder(ModTags.Items.DUST_STEEL).add(registerItems.DUST_STEEL.get());

        this.getOrCreateBuilder(ModTags.Items.EXTRUSION_MOLD).add(registerItems.MOLD_PLATE.get()).add(registerItems.MOLD_WIRE.get()).add(registerItems.MOLD_EXTRACTION.get());

        this.getOrCreateBuilder(ModTags.Items.MORTAR).add(registerItems.MORTAR_IRON.get());
        this.getOrCreateBuilder(ModTags.Items.CUTTER).add(registerItems.STEEL_CUTTER.get());
        this.getOrCreateBuilder(ModTags.Items.HAMMER).add(registerItems.HAMMER_IRON.get());

    }
}
