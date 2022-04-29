package com.yor42.projectazure.data.common;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.libs.Constants;
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

        this.tag(ModTags.Items.INGOT_ALUMINIUM).add(Main.INGOT_ALUMINIUM.get());
        this.tag(ModTags.Items.INGOT_COPPER).add(Main.INGOT_COPPER.get());
        this.tag(ModTags.Items.INGOT_TIN).add(Main.INGOT_TIN.get());
        this.tag(ModTags.Items.INGOT_BRONZE).add(Main.INGOT_BRONZE.get());
        this.tag(ModTags.Items.INGOT_LEAD).add(Main.INGOT_LEAD.get());
        this.tag(ModTags.Items.INGOT_ZINC).add(Main.INGOT_ZINC.get());
        this.tag(ModTags.Items.INGOT_STEEL).add(Main.INGOT_STEEL.get());
        this.tag(ModTags.Items.INGOT_BRASS).add(Main.INGOT_BRASS.get());

        this.tag(ModTags.Items.NUGGET_COPPER).add(Main.NUGGET_COPPER.get());
        this.tag(ModTags.Items.NUGGET_TIN).add(Main.NUGGET_TIN.get());
        this.tag(ModTags.Items.NUGGET_BRONZE).add(Main.NUGGET_BRONZE.get());
        this.tag(ModTags.Items.NUGGET_LEAD).add(Main.NUGGET_LEAD.get());
        this.tag(ModTags.Items.NUGGET_ZINC).add(Main.NUGGET_ZINC.get());
        this.tag(ModTags.Items.NUGGET_STEEL).add(Main.NUGGET_STEEL.get());
        this.tag(ModTags.Items.NUGGET_BRASS).add(Main.NUGGET_BRASS.get());

        this.tag(ModTags.Items.PLATE_ALUMINIUM).add(Main.PLATE_ALUMINIUM.get());
        this.tag(ModTags.Items.PLATE_COPPER).add(Main.PLATE_COPPER.get());
        this.tag(ModTags.Items.PLATE_TIN).add(Main.PLATE_TIN.get());
        this.tag(ModTags.Items.PLATE_BRONZE).add(Main.PLATE_BRONZE.get());
        this.tag(ModTags.Items.PLATE_LEAD).add(Main.PLATE_LEAD.get());
        this.tag(ModTags.Items.PLATE_ZINC).add(Main.PLATE_ZINC.get());
        this.tag(ModTags.Items.PLATE_IRON).add(Main.PLATE_IRON.get());
        this.tag(ModTags.Items.PLATE_GOLD).add(Main.PLATE_GOLD.get());
        this.tag(ModTags.Items.PLATE_STEEL).add(Main.PLATE_STEEL.get());
        this.tag(ModTags.Items.PLATE_BRASS).add(Main.PLATE_BRASS.get());

        this.tag(ModTags.Items.GEAR_STEEL).add(Main.GEAR_STEEL.get());
        this.tag(ModTags.Items.GEAR_BRONZE).add(Main.GEAR_BRONZE.get());
        this.tag(ModTags.Items.GEAR_TIN).add(Main.GEAR_TIN.get());
        this.tag(ModTags.Items.GEAR_IRON).add(Main.GEAR_IRON.get());
        this.tag(ModTags.Items.GEAR_GOLD).add(Main.GEAR_GOLD.get());
        this.tag(ModTags.Items.GEAR_COPPER).add(Main.GEAR_COPPER.get());

        this.tag(ModTags.Items.DUST_ALUMINIUM).add(Main.DUST_ALUMINIUM.get());
        this.tag(ModTags.Items.DUST_COPPER).add(Main.DUST_COPPER.get());
        this.tag(ModTags.Items.DUST_TIN).add(Main.DUST_TIN.get());
        this.tag(ModTags.Items.DUST_BRONZE).add(Main.DUST_BRONZE.get());
        this.tag(ModTags.Items.DUST_LEAD).add(Main.DUST_LEAD.get());
        this.tag(ModTags.Items.DUST_IRON).add(Main.DUST_IRON.get());
        this.tag(ModTags.Items.DUST_ZINC).add(Main.DUST_ZINC.get());
        this.tag(ModTags.Items.DUST_COAL).add(Main.DUST_COAL.get());
        this.tag(ModTags.Items.DUST_GOLD).add(Main.DUST_GOLD.get());
        this.tag(ModTags.Items.DUST_STEEL).add(Main.DUST_STEEL.get());
        this.tag(ModTags.Items.DUST_BRASS).add(Main.DUST_BRASS.get());

        this.tag(ModTags.Items.SLEDGEHAMMER).add(Main.SLEDGEHAMMER.get());

        this.tag(ModTags.Items.WIRE_COPPER).add(Main.COPPER_WIRE.get());

        this.tag(ModTags.Items.CIRCUITS).add(Main.PRIMITIVE_CIRCUIT.get());
        this.tag(ModTags.Items.CIRCUITS_BASIC).add(Main.PRIMITIVE_CIRCUIT.get());
        this.tag(ModTags.Items.CIRCUITS_ADVANCED).add(Main.ADVANCED_CIRCUIT.get());

        this.tag(ModTags.Items.ORIGINITE).add(Main.ORIGINITE.get());
        this.tag(ModTags.Items.ORIGINIUM_PRIME).add(Main.ORIGINIUM_PRIME.get());

        this.tag(ModTags.Items.TREE_SAP).add(Main.TREE_SAP.get());

        this.tag(ModTags.Items.EXTRUSION_MOLD).add(Main.MOLD_PLATE.get()).add(Main.MOLD_WIRE.get()).add(Main.MOLD_EXTRACTION.get());

        this.tag(ModTags.Items.MORTAR).add(Main.MORTAR_IRON.get());
        this.tag(ModTags.Items.CUTTER).add(Main.STEEL_CUTTER.get());
        this.tag(ModTags.Items.HAMMER).add(Main.HAMMER_IRON.get());

    }
}
