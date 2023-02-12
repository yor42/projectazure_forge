package com.yor42.projectazure.data.common;

import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.setup.register.registerItems;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
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
        copy(ModTags.Blocks.ORES_RMA7012, ModTags.Items.ORES_RMA7012);
        copy(ModTags.Blocks.ORES_MANGANESE, ModTags.Items.ORES_MANGANESE);

        copy(ModTags.Blocks.BLOCK_D32, ModTags.Items.BLOCK_D32);
        copy(ModTags.Blocks.BLOCK_ALUMINIUM, ModTags.Items.BLOCK_ALUMINIUM);
        copy(ModTags.Blocks.BLOCK_BRASS, ModTags.Items.BLOCK_BRASS);
        copy(ModTags.Blocks.BLOCK_BRONZE, ModTags.Items.BLOCK_BRONZE);
        copy(ModTags.Blocks.BLOCK_COPPER, ModTags.Items.BLOCK_COPPER);
        copy(ModTags.Blocks.BLOCK_LEAD, ModTags.Items.BLOCK_LEAD);
        copy(ModTags.Blocks.BLOCK_RMA7012, ModTags.Items.BLOCK_RMA7012);
        copy(ModTags.Blocks.BLOCK_RMA7024, ModTags.Items.BLOCK_RMA7024);
        copy(ModTags.Blocks.BLOCK_TIN, ModTags.Items.BLOCK_TIN);
        copy(ModTags.Blocks.BLOCK_ZINC, ModTags.Items.BLOCK_ZINC);
        copy(ModTags.Blocks.BLOCK_STEEL, ModTags.Items.BLOCK_STEEL);
        copy(ModTags.Blocks.BLOCK_MANGANESE, ModTags.Items.BLOCK_MANGANESE);
        copy(ModTags.Blocks.BLOCK_INCANDESCENT_ALLOY, ModTags.Items.BLOCK_INCANDESCENT_ALLOY);

        copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);
        copy(Tags.Blocks.ORES, Tags.Items.ORES);

        this.tag(ModTags.Items.INGOT_ALUMINIUM).add(registerItems.INGOT_ALUMINIUM.get());
        this.tag(ModTags.Items.INGOT_COPPER).add(registerItems.INGOT_COPPER.get());
        this.tag(ModTags.Items.INGOT_TIN).add(registerItems.INGOT_TIN.get());
        this.tag(ModTags.Items.INGOT_BRONZE).add(registerItems.INGOT_BRONZE.get());
        this.tag(ModTags.Items.INGOT_LEAD).add(registerItems.INGOT_LEAD.get());
        this.tag(ModTags.Items.INGOT_ZINC).add(registerItems.INGOT_ZINC.get());
        this.tag(ModTags.Items.INGOT_STEEL).add(registerItems.INGOT_STEEL.get());
        this.tag(ModTags.Items.INGOT_BRASS).add(registerItems.INGOT_BRASS.get());
        this.tag(ModTags.Items.INGOT_RMA7012).add(registerItems.INGOT_RMA7012.get());
        this.tag(ModTags.Items.INGOT_RMA7024).add(registerItems.INGOT_RMA7024.get());
        this.tag(ModTags.Items.INGOT_D32).add(registerItems.INGOT_D32.get());
        this.tag(ModTags.Items.INGOT_MANGANESE).add(registerItems.INGOT_MANGANESE.get());
        this.tag(ModTags.Items.INGOT_INCANDESCENT_ALLOY).add(registerItems.INGOT_INCANDESCENT_ALLOY.get());


        this.tag(ModTags.Items.NUGGET_COPPER).add(registerItems.NUGGET_COPPER.get());
        this.tag(ModTags.Items.NUGGET_TIN).add(registerItems.NUGGET_TIN.get());
        this.tag(ModTags.Items.NUGGET_BRONZE).add(registerItems.NUGGET_BRONZE.get());
        this.tag(ModTags.Items.NUGGET_LEAD).add(registerItems.NUGGET_LEAD.get());
        this.tag(ModTags.Items.NUGGET_ZINC).add(registerItems.NUGGET_ZINC.get());
        this.tag(ModTags.Items.NUGGET_STEEL).add(registerItems.NUGGET_STEEL.get());
        this.tag(ModTags.Items.NUGGET_BRASS).add(registerItems.NUGGET_BRASS.get());
        this.tag(ModTags.Items.NUGGET_RMA7012).add(registerItems.NUGGET_RMA7012.get());
        this.tag(ModTags.Items.NUGGET_RMA7024).add(registerItems.NUGGET_RMA7024.get());
        this.tag(ModTags.Items.NUGGET_D32).add(registerItems.NUGGET_D32.get());
        this.tag(ModTags.Items.NUGGET_MANGANESE).add(registerItems.NUGGET_MANGANESE.get());
        this.tag(ModTags.Items.NUGGET_INCANDESCENT_ALLOY).add(registerItems.NUGGET_INCANDESCENT_ALLOY.get());

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
        this.tag(ModTags.Items.PLATE_RMA7012).add(registerItems.PLATE_RMA7012.get());
        this.tag(ModTags.Items.PLATE_RMA7024).add(registerItems.PLATE_RMA7024.get());
        this.tag(ModTags.Items.PLATE_D32).add(registerItems.PLATE_D32.get());
        this.tag(ModTags.Items.PLATE_MANGANESE).add(registerItems.PLATE_MANGANESE.get());
        this.tag(ModTags.Items.PLATE_INCANDESCENT_ALLOY).add(registerItems.PLATE_INCANDESCENT_ALLOY.get());

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
        this.tag(ModTags.Items.DUST_ORIGINIUM).add(registerItems.DUST_ORIGINIUM.get());
        this.tag(ModTags.Items.DUST_ORIROCK).add(registerItems.DUST_ORIROCK.get());
        this.tag(ModTags.Items.DUST_QUARTZ).add(registerItems.DUST_NETHER_QUARTZ.get());
        this.tag(ModTags.Items.DUST_STEEL).add(registerItems.DUST_STEEL.get());
        this.tag(ModTags.Items.DUST_BRASS).add(registerItems.DUST_BRASS.get());
        this.tag(ModTags.Items.DUST_RMA7012).add(registerItems.DUST_RMA7012.get());
        this.tag(ModTags.Items.DUST_RMA7024).add(registerItems.DUST_RMA7024.get());
        this.tag(ModTags.Items.DUST_D32).add(registerItems.DUST_D32.get());
        this.tag(ModTags.Items.DUST_MANGANESE).add(registerItems.DUST_MANGANESE.get());
        this.tag(ModTags.Items.DUST_INCANDESCENT_ALLOY).add(registerItems.DUST_INCANDESCENT_ALLOY.get());

        this.tag(ModTags.Items.EXPLOSIVE_COMPOUND).add(registerItems.GUNPOWDER_COMPOUND.get());

        this.tag(ModTags.Items.SLEDGEHAMMER).add(registerItems.SLEDGEHAMMER.get());

        this.tag(ModTags.Items.WIRE_COPPER).add(registerItems.COPPER_WIRE.get());

        this.tag(ModTags.Items.CIRCUITS).add(registerItems.PRIMITIVE_CIRCUIT.get());
        this.tag(ModTags.Items.CIRCUITS_BASIC).add(registerItems.PRIMITIVE_CIRCUIT.get());
        this.tag(ModTags.Items.CIRCUITS_ADVANCED).add(registerItems.ADVANCED_CIRCUIT.get());
        this.tag(ModTags.Items.CIRCUITS_CRYSTALLINE).add(registerItems.CRYSTALLINE_CIRCUIT.get());

        this.tag(ModTags.Items.ORIGINITE).add(registerItems.ORIGINITE.get());
        this.tag(ModTags.Items.ORIGINIUM_PRIME).add(registerItems.ORIGINIUM_PRIME.get());

        this.tag(ModTags.Items.TREE_SAP).add(registerItems.TREE_SAP.get());

        this.tag(ModTags.Items.EXTRUSION_MOLD).add(registerItems.MOLD_PLATE.get()).add(registerItems.MOLD_WIRE.get()).add(registerItems.MOLD_EXTRACTION.get());

        this.tag(ModTags.Items.MORTAR).add(registerItems.MORTAR_STONE.get()).add(registerItems.MORTAR_COPPER.get()).add(registerItems.MORTAR_TIN.get()).add(registerItems.MORTAR_BRONZE.get()).add(registerItems.MORTAR_IRON.get()).add(registerItems.MORTAR_STEEL.get()).add(registerItems.MORTAR_GOLD.get()).add(registerItems.MORTAR_DIAMOND.get()).add(registerItems.MORTAR_NETHERITE.get()).add(registerItems.MORTAR_RMA7012.get()).add(registerItems.MORTAR_RMA7024.get()).add(registerItems.MORTAR_D32.get());
        this.tag(ModTags.Items.CUTTER).add(registerItems.CUTTER_STONE.get()).add(registerItems.CUTTER_COPPER.get()).add(registerItems.CUTTER_TIN.get()).add(registerItems.CUTTER_BRONZE.get()).add(registerItems.CUTTER_IRON.get()).add(registerItems.CUTTER_STEEL.get()).add(registerItems.CUTTER_GOLD.get()).add(registerItems.CUTTER_DIAMOND.get()).add(registerItems.CUTTER_NETHERITE.get()).add(registerItems.CUTTER_RMA7012.get()).add(registerItems.CUTTER_RMA7024.get()).add(registerItems.CUTTER_D32.get());
        this.tag(ModTags.Items.HAMMER).add(registerItems.HAMMER_STONE.get()).add(registerItems.HAMMER_COPPER.get()).add(registerItems.HAMMER_TIN.get()).add(registerItems.HAMMER_BRONZE.get()).add(registerItems.HAMMER_IRON.get()).add(registerItems.HAMMER_GOLD.get()).add(registerItems.HAMMER_STEEL.get()).add(registerItems.HAMMER_DIAMOND.get()).add(registerItems.HAMMER_RMA7012.get()).add(registerItems.HAMMER_RMA7024.get()).add(registerItems.HAMMER_D32.get()).add(registerItems.HAMMER_NETHERITE.get()).add(registerItems.SLEDGEHAMMER.get());
        this.tag(ModTags.Items.SAW).add(registerItems.SAW_STONE.get()).add(registerItems.SAW_COPPER.get()).add(registerItems.SAW_TIN.get()).add(registerItems.SAW_BRONZE.get()).add(registerItems.SAW_IRON.get()).add(registerItems.SAW_GOLD.get()).add(registerItems.SAW_STEEL.get()).add(registerItems.SAW_DIAMOND.get()).add(registerItems.SAW_NETHERITE.get()).add(registerItems.SAW_RMA7012.get()).add(registerItems.SAW_RMA7024.get()).add(registerItems.SAW_D32.get());
        this.tag(ModTags.Items.KNIFE).add(registerItems.KITCHEN_KNIFE.get(), registerItems.TACTICAL_KNIFE.get());

    }
}
