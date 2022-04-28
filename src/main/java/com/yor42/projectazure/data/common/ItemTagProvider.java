package com.yor42.projectazure.data.common;

import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.setup.register.registerItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class ItemTagProvider extends ItemTagsProvider {
    public ItemTagProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagProvider, Constants.MODID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "ProjectAzure:Item Tags";
    }

    @Override
    protected void addTags() {
        copy(ModTags.Blocks.ORES_COPPER, ModTags.Items.ORES_COPPER);
        copy(ModTags.Blocks.ORES_TIN, ModTags.Items.ORES_TIN);
        copy(ModTags.Blocks.ORES_LEAD, ModTags.Items.ORES_LEAD);
        copy(ModTags.Blocks.ORES_ALUMINIUM, ModTags.Items.ORES_ALUMINIUM);
        copy(ModTags.Blocks.ORES_ZINC, ModTags.Items.ORES_ZINC);
        copy(ModTags.Blocks.ORES_ORIROCK, ModTags.Items.ORES_ORIROCK);
        copy(Tags.Blocks.ORES, Tags.Items.ORES);

        this.tag(ModTags.Items.INGOT_ALUMINIUM).add(registerItems.INGOT_ALUMINIUM.get());
        this.tag(ModTags.Items.INGOT_COPPER).add(registerItems.INGOT_COPPER.get());
        this.tag(ModTags.Items.INGOT_TIN).add(registerItems.INGOT_TIN.get());
        this.tag(ModTags.Items.INGOT_BRONZE).add(registerItems.INGOT_BRONZE.get());
        this.tag(ModTags.Items.INGOT_LEAD).add(registerItems.INGOT_LEAD.get());
        this.tag(ModTags.Items.INGOT_ZINC).add(registerItems.INGOT_ZINC.get());
        this.tag(ModTags.Items.INGOT_STEEL).add(registerItems.INGOT_STEEL.get());
        this.tag(ModTags.Items.INGOT_BRASS).add(registerItems.INGOT_BRASS.get());

        this.tag(ModTags.Items.NUGGET_COPPER).add(registerItems.NUGGET_COPPER.get());
        this.tag(ModTags.Items.NUGGET_TIN).add(registerItems.NUGGET_TIN.get());
        this.tag(ModTags.Items.NUGGET_BRONZE).add(registerItems.NUGGET_BRONZE.get());
        this.tag(ModTags.Items.NUGGET_LEAD).add(registerItems.NUGGET_LEAD.get());
        this.tag(ModTags.Items.NUGGET_ZINC).add(registerItems.NUGGET_ZINC.get());
        this.tag(ModTags.Items.NUGGET_STEEL).add(registerItems.NUGGET_STEEL.get());
        this.tag(ModTags.Items.NUGGET_BRASS).add(registerItems.NUGGET_BRASS.get());

        this.tag(ModTags.Items.PLATE_ALUMINIUM).add(registerItems.PLATE_ALUMINIUM.get());
        this.tag(ModTags.Items.PLATE_COPPER).add(registerItems.PLATE_COPPER.get());
        this.tag(ModTags.Items.PLATE_TIN).add(registerItems.PLATE_TIN.get());
        this.tag(ModTags.Items.PLATE_BRONZE).add(registerItems.PLATE_BRONZE.get());
        this.tag(ModTags.Items.PLATE_LEAD).add(registerItems.PLATE_LEAD.get());
        this.tag(ModTags.Items.PLATE_ZINC).add(registerItems.PLATE_ZINC.get());
        this.tag(ModTags.Items.PLATE_IRON).add(registerItems.PLATE_IRON.get());
        this.tag(ModTags.Items.PLATE_GOLD).add(registerItems.PLATE_GOLD.get());
        this.tag(ModTags.Items.PLATE_STEEL).add(registerItems.PLATE_STEEL.get());
        this.tag(ModTags.Items.PLATE_BRASS).add(registerItems.PLATE_BRASS.get());

        this.tag(ModTags.Items.GEAR_STEEL).add(registerItems.GEAR_STEEL.get());
        this.tag(ModTags.Items.GEAR_BRONZE).add(registerItems.GEAR_BRONZE.get());
        this.tag(ModTags.Items.GEAR_TIN).add(registerItems.GEAR_TIN.get());
        this.tag(ModTags.Items.GEAR_IRON).add(registerItems.GEAR_IRON.get());
        this.tag(ModTags.Items.GEAR_GOLD).add(registerItems.GEAR_GOLD.get());
        this.tag(ModTags.Items.GEAR_COPPER).add(registerItems.GEAR_COPPER.get());

        this.tag(ModTags.Items.DUST_ALUMINIUM).add(registerItems.DUST_ALUMINIUM.get());
        this.tag(ModTags.Items.DUST_COPPER).add(registerItems.DUST_COPPER.get());
        this.tag(ModTags.Items.DUST_TIN).add(registerItems.DUST_TIN.get());
        this.tag(ModTags.Items.DUST_BRONZE).add(registerItems.DUST_BRONZE.get());
        this.tag(ModTags.Items.DUST_LEAD).add(registerItems.DUST_LEAD.get());
        this.tag(ModTags.Items.DUST_IRON).add(registerItems.DUST_IRON.get());
        this.tag(ModTags.Items.DUST_ZINC).add(registerItems.DUST_ZINC.get());
        this.tag(ModTags.Items.DUST_COAL).add(registerItems.DUST_COAL.get());
        this.tag(ModTags.Items.DUST_GOLD).add(registerItems.DUST_GOLD.get());
        this.tag(ModTags.Items.DUST_STEEL).add(registerItems.DUST_STEEL.get());
        this.tag(ModTags.Items.DUST_BRASS).add(registerItems.DUST_BRASS.get());

        this.tag(ModTags.Items.SLEDGEHAMMER).add(registerItems.SLEDGEHAMMER.get());

        this.tag(ModTags.Items.WIRE_COPPER).add(registerItems.COPPER_WIRE.get());

        this.tag(ModTags.Items.CIRCUITS).add(registerItems.PRIMITIVE_CIRCUIT.get());
        this.tag(ModTags.Items.CIRCUITS_BASIC).add(registerItems.PRIMITIVE_CIRCUIT.get());
        this.tag(ModTags.Items.CIRCUITS_ADVANCED).add(registerItems.ADVANCED_CIRCUIT.get());

        this.tag(ModTags.Items.ORIGINITE).add(registerItems.ORIGINITE.get());
        this.tag(ModTags.Items.ORIGINIUM_PRIME).add(registerItems.ORIGINIUM_PRIME.get());

        this.tag(ModTags.Items.TREE_SAP).add(registerItems.TREE_SAP.get());

        this.tag(ModTags.Items.EXTRUSION_MOLD).add(registerItems.MOLD_PLATE.get()).add(registerItems.MOLD_WIRE.get()).add(registerItems.MOLD_EXTRACTION.get());

        this.tag(ModTags.Items.MORTAR).add(registerItems.MORTAR_IRON.get());
        this.tag(ModTags.Items.CUTTER).add(registerItems.STEEL_CUTTER.get());
        this.tag(ModTags.Items.HAMMER).add(registerItems.HAMMER_IRON.get());

    }
}
