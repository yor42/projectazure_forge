package com.yor42.projectazure.data.common;

import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.setup.register.RegisterBlocks;
import com.yor42.projectazure.setup.register.RegisterItems;
import mekanism.common.registries.MekanismItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemTagProvider extends ItemTagsProvider {
    public ItemTagProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagProvider, Constants.MODID, existingFileHelper);
    }

    @Nonnull
    @Override
    public String getName() {
        return "ProjectAzure:Item Tags";
    }

    @Override
    protected void addTags() {

        copy(ModTags.Blocks.ORES_TIN, ModTags.Items.ORES_TIN);
        copy(ModTags.Blocks.ORES_LEAD, ModTags.Items.ORES_LEAD);
        copy(ModTags.Blocks.ORES_ALUMINIUM, ModTags.Items.ORES_ALUMINIUM);
        copy(ModTags.Blocks.ORES_ZINC, ModTags.Items.ORES_ZINC);
        copy(ModTags.Blocks.ORES_ORIROCK, ModTags.Items.ORES_ORIROCK);
        copy(ModTags.Blocks.ORES_RMA7012, ModTags.Items.ORES_RMA7012);
        copy(ModTags.Blocks.ORES_MANGANESE, ModTags.Items.ORES_MANGANESE);
        copy(ModTags.Blocks.ORES_PYROXENE, ModTags.Items.ORES_PYROXENE);

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

        this.tag(ModTags.Items.INGOT_ALUMINIUM).add(RegisterItems.INGOT_ALUMINIUM.get());
        this.tag(ModTags.Items.INGOT_TIN).add(RegisterItems.INGOT_TIN.get());
        this.tag(ModTags.Items.INGOT_BRONZE).add(RegisterItems.INGOT_BRONZE.get());
        this.tag(ModTags.Items.INGOT_LEAD).add(RegisterItems.INGOT_LEAD.get());
        this.tag(ModTags.Items.INGOT_ZINC).add(RegisterItems.INGOT_ZINC.get());
        this.tag(ModTags.Items.INGOT_STEEL).add(RegisterItems.INGOT_STEEL.get());
        this.tag(ModTags.Items.INGOT_BRASS).add(RegisterItems.INGOT_BRASS.get());
        this.tag(ModTags.Items.INGOT_RMA7012).add(RegisterItems.INGOT_RMA7012.get());
        this.tag(ModTags.Items.INGOT_RMA7024).add(RegisterItems.INGOT_RMA7024.get());
        this.tag(ModTags.Items.INGOT_D32).add(RegisterItems.INGOT_D32.get());
        this.tag(ModTags.Items.INGOT_SILICON).add(RegisterItems.SILICONE.get());
        this.tag(ModTags.Items.INGOT_MANGANESE).add(RegisterItems.INGOT_MANGANESE.get());
        this.tag(ModTags.Items.INGOT_INCANDESCENT_ALLOY).add(RegisterItems.INGOT_INCANDESCENT_ALLOY.get());


        this.tag(ModTags.Items.NUGGET_TIN).add(RegisterItems.NUGGET_TIN.get());
        this.tag(ModTags.Items.NUGGET_BRONZE).add(RegisterItems.NUGGET_BRONZE.get());
        this.tag(ModTags.Items.NUGGET_LEAD).add(RegisterItems.NUGGET_LEAD.get());
        this.tag(ModTags.Items.NUGGET_ZINC).add(RegisterItems.NUGGET_ZINC.get());
        this.tag(ModTags.Items.NUGGET_STEEL).add(RegisterItems.NUGGET_STEEL.get());
        this.tag(ModTags.Items.NUGGET_BRASS).add(RegisterItems.NUGGET_BRASS.get());
        this.tag(ModTags.Items.NUGGET_RMA7012).add(RegisterItems.NUGGET_RMA7012.get());
        this.tag(ModTags.Items.NUGGET_RMA7024).add(RegisterItems.NUGGET_RMA7024.get());
        this.tag(ModTags.Items.NUGGET_D32).add(RegisterItems.NUGGET_D32.get());
        this.tag(ModTags.Items.NUGGET_MANGANESE).add(RegisterItems.NUGGET_MANGANESE.get());
        this.tag(ModTags.Items.NUGGET_INCANDESCENT_ALLOY).add(RegisterItems.NUGGET_INCANDESCENT_ALLOY.get());

        this.tag(ModTags.Items.PLATE_ALUMINIUM).add(RegisterItems.PLATE_ALUMINIUM.get());
        this.tag(ModTags.Items.PLATE_PLASTIC).add(RegisterItems.PLATE_POLYMER.get()).addOptional(MekanismItems.HDPE_SHEET.getRegistryName());
        this.tag(ModTags.Items.PLATE_COPPER).add(RegisterItems.PLATE_COPPER.get());
        this.tag(ModTags.Items.PLATE_TIN).add(RegisterItems.PLATE_TIN.get());
        this.tag(ModTags.Items.PLATE_BRONZE).add(RegisterItems.PLATE_BRONZE.get());
        this.tag(ModTags.Items.PLATE_LEAD).add(RegisterItems.PLATE_LEAD.get());
        this.tag(ModTags.Items.PLATE_ZINC).add(RegisterItems.PLATE_ZINC.get());
        this.tag(ModTags.Items.PLATE_IRON).add(RegisterItems.PLATE_IRON.get());
        this.tag(ModTags.Items.PLATE_GOLD).add(RegisterItems.PLATE_GOLD.get());
        this.tag(ModTags.Items.PLATE_STEEL).add(RegisterItems.PLATE_STEEL.get());
        this.tag(ModTags.Items.PLATE_BRASS).add(RegisterItems.PLATE_BRASS.get());
        this.tag(ModTags.Items.PLATE_RMA7012).add(RegisterItems.PLATE_RMA7012.get());
        this.tag(ModTags.Items.PLATE_RMA7024).add(RegisterItems.PLATE_RMA7024.get());
        this.tag(ModTags.Items.PLATE_D32).add(RegisterItems.PLATE_D32.get());
        this.tag(ModTags.Items.PLATE_MANGANESE).add(RegisterItems.PLATE_MANGANESE.get());
        this.tag(ModTags.Items.PLATE_INCANDESCENT_ALLOY).add(RegisterItems.PLATE_INCANDESCENT_ALLOY.get());

        this.tag(ModTags.Items.GEAR_STEEL).add(RegisterItems.GEAR_STEEL.get());
        this.tag(ModTags.Items.GEAR_BRONZE).add(RegisterItems.GEAR_BRONZE.get());
        this.tag(ModTags.Items.GEAR_IRON).add(RegisterItems.GEAR_IRON.get());

        this.tag(ModTags.Items.DUST_ALUMINIUM).add(RegisterItems.DUST_ALUMINIUM.get());
        this.tag(ModTags.Items.DUST_COPPER).add(RegisterItems.DUST_COPPER.get());
        this.tag(ModTags.Items.DUST_TIN).add(RegisterItems.DUST_TIN.get());
        this.tag(ModTags.Items.DUST_BRONZE).add(RegisterItems.DUST_BRONZE.get());
        this.tag(ModTags.Items.DUST_LEAD).add(RegisterItems.DUST_LEAD.get());
        this.tag(ModTags.Items.DUST_IRON).add(RegisterItems.DUST_IRON.get());
        this.tag(ModTags.Items.DUST_ZINC).add(RegisterItems.DUST_ZINC.get());
        this.tag(ModTags.Items.DUST_COAL).add(RegisterItems.DUST_COAL.get());
        this.tag(ModTags.Items.DUST_GOLD).add(RegisterItems.DUST_GOLD.get());
        this.tag(ModTags.Items.DUST_ORIGINIUM).add(RegisterItems.DUST_ORIGINIUM.get());
        this.tag(ModTags.Items.DUST_ORIROCK).add(RegisterItems.DUST_ORIROCK.get());
        this.tag(ModTags.Items.DUST_QUARTZ).add(RegisterItems.DUST_NETHER_QUARTZ.get());
        this.tag(ModTags.Items.DUST_STEEL).add(RegisterItems.DUST_STEEL.get());
        this.tag(ModTags.Items.DUST_BRASS).add(RegisterItems.DUST_BRASS.get());
        this.tag(ModTags.Items.DUST_RMA7012).add(RegisterItems.DUST_RMA7012.get());
        this.tag(ModTags.Items.DUST_RMA7024).add(RegisterItems.DUST_RMA7024.get());
        this.tag(ModTags.Items.DUST_D32).add(RegisterItems.DUST_D32.get());
        this.tag(ModTags.Items.DUST_MANGANESE).add(RegisterItems.DUST_MANGANESE.get());
        this.tag(ModTags.Items.DUST_INCANDESCENT_ALLOY).add(RegisterItems.DUST_INCANDESCENT_ALLOY.get());

        this.tag(ModTags.Items.EXPLOSIVE_COMPOUND).add(RegisterItems.GUNPOWDER_COMPOUND.get());

        this.tag(ModTags.Items.SLEDGEHAMMER).add(RegisterItems.SLEDGEHAMMER.get());

        this.tag(ModTags.Items.WIRE_COPPER).add(RegisterItems.COPPER_WIRE.get());

        this.tag(ModTags.Items.CIRCUITS).add(RegisterItems.PRIMITIVE_CIRCUIT.get(), RegisterItems.ADVANCED_CIRCUIT.get(), RegisterItems.CRYSTALLINE_CIRCUIT.get());
        this.tag(ModTags.Items.CIRCUITS_BASIC).add(RegisterItems.PRIMITIVE_CIRCUIT.get());
        this.tag(ModTags.Items.CIRCUITS_ADVANCED).add(RegisterItems.ADVANCED_CIRCUIT.get());
        this.tag(ModTags.Items.CIRCUITS_CRYSTALLINE).add(RegisterItems.CRYSTALLINE_CIRCUIT.get());

        this.tag(ModTags.Items.ORIGINITE).add(RegisterItems.ORIGINITE.get());
        this.tag(ModTags.Items.ORIGINIUM_PRIME).add(RegisterItems.ORIGINIUM_PRIME.get());

        this.tag(ModTags.Items.TREE_SAP).add(RegisterItems.TREE_SAP.get());

        this.tag(ModTags.Items.MATERIAL_D32).add(RegisterItems.INGOT_D32.get(), RegisterItems.PLATE_D32.get(), RegisterItems.NUGGET_D32.get(), RegisterBlocks.D32_BLOCK.get().asItem());
        this.tag(ModTags.Items.MATERIAL_COPPER).add(RegisterItems.PLATE_COPPER.get(), RegisterBlocks.COPPER_BLOCK.get().asItem());
        this.tag(ModTags.Items.MATERIAL_TIN).add(RegisterItems.INGOT_TIN.get(), RegisterItems.PLATE_TIN.get(), RegisterItems.NUGGET_TIN.get(), RegisterBlocks.TIN_BLOCK.get().asItem());
        this.tag(ModTags.Items.MATERIAL_STEEL).add(RegisterItems.INGOT_STEEL.get(), RegisterItems.PLATE_STEEL.get(), RegisterItems.NUGGET_STEEL.get(), RegisterBlocks.STEEL_BLOCK.get().asItem());
        this.tag(ModTags.Items.MATERIAL_MANGANESE).add(RegisterItems.INGOT_MANGANESE.get(), RegisterItems.PLATE_MANGANESE.get(), RegisterItems.NUGGET_MANGANESE.get(), RegisterBlocks.MANGANESE_BLOCK.get().asItem());
        this.tag(ModTags.Items.MATERIAL_BRONZE).add(RegisterItems.INGOT_BRONZE.get(), RegisterItems.PLATE_BRONZE.get(), RegisterItems.NUGGET_BRONZE.get(), RegisterBlocks.BRONZE_BLOCK.get().asItem());
        this.tag(ModTags.Items.MATERIAL_BRASS).add(RegisterItems.INGOT_BRASS.get(), RegisterItems.PLATE_BRASS.get(), RegisterItems.NUGGET_BRASS.get(), RegisterBlocks.BRASS_BLOCK.get().asItem());
        this.tag(ModTags.Items.MATERIAL_ZINC).add(RegisterItems.INGOT_ZINC.get(), RegisterItems.PLATE_ZINC.get(), RegisterItems.NUGGET_ZINC.get(), RegisterBlocks.ZINC_BLOCK.get().asItem());
        this.tag(ModTags.Items.MATERIAL_LEAD).add(RegisterItems.INGOT_LEAD.get(), RegisterItems.PLATE_LEAD.get(), RegisterItems.NUGGET_LEAD.get(), RegisterBlocks.LEAD_BLOCK.get().asItem());
        this.tag(ModTags.Items.MATERIAL_ALUMINIUM).add(RegisterItems.INGOT_ALUMINIUM.get(), RegisterItems.PLATE_ALUMINIUM.get(), RegisterBlocks.ALUMINIUM_BLOCK.get().asItem());
        this.tag(ModTags.Items.MATERIAL_RMA7012).add(RegisterItems.INGOT_RMA7012.get(), RegisterItems.PLATE_RMA7012.get(), RegisterItems.NUGGET_RMA7012.get(), RegisterBlocks.RMA_7012_BLOCK.get().asItem());
        this.tag(ModTags.Items.MATERIAL_RMA7024).add(RegisterItems.INGOT_RMA7024.get(), RegisterItems.PLATE_RMA7024.get(), RegisterItems.NUGGET_RMA7024.get(), RegisterBlocks.RMA_7024_BLOCK.get().asItem());
        this.tag(ModTags.Items.MATERIAL_INCANDESCENT_ALLOY).add(RegisterItems.INGOT_INCANDESCENT_ALLOY.get(), RegisterItems.PLATE_INCANDESCENT_ALLOY.get(), RegisterItems.NUGGET_INCANDESCENT_ALLOY.get(), RegisterBlocks.INCANDESCENT_ALLOY_BLOCK.get().asItem());
        this.tag(ModTags.Items.MATERIAL_SILICON).add(RegisterItems.SILICONE.get());
        this.tag(ModTags.Items.MATERIAL_ORIROCK).add(RegisterBlocks.ORIROCK.get().asItem(), RegisterBlocks.COBBLED_ORIROCK.get().asItem());

        this.tag(ModTags.Items.GEM_ORIGINITE).add(RegisterItems.ORIGINITE.get());
        this.tag(ModTags.Items.GEM_ORIGINIUM_PRIME).add(RegisterItems.ORIGINIUM_PRIME.get());
        this.tag(ModTags.Items.GEM_PYROXENE).add(RegisterItems.PYROXENE.get());

        this.tag(ModTags.Items.EXTRUSION_MOLD).add(RegisterItems.MOLD_PLATE.get()).add(RegisterItems.MOLD_WIRE.get()).add(RegisterItems.MOLD_EXTRACTION.get());

        this.tag(ModTags.Items.MORTAR).add(RegisterItems.MORTAR_STONE.get()).add(RegisterItems.MORTAR_COPPER.get()).add(RegisterItems.MORTAR_TIN.get()).add(RegisterItems.MORTAR_BRONZE.get()).add(RegisterItems.MORTAR_IRON.get()).add(RegisterItems.MORTAR_STEEL.get()).add(RegisterItems.MORTAR_GOLD.get()).add(RegisterItems.MORTAR_DIAMOND.get()).add(RegisterItems.MORTAR_NETHERITE.get()).add(RegisterItems.MORTAR_RMA7012.get()).add(RegisterItems.MORTAR_RMA7024.get()).add(RegisterItems.MORTAR_D32.get());
        this.tag(ModTags.Items.CUTTER).add(RegisterItems.CUTTER_STONE.get()).add(RegisterItems.CUTTER_COPPER.get()).add(RegisterItems.CUTTER_TIN.get()).add(RegisterItems.CUTTER_BRONZE.get()).add(RegisterItems.CUTTER_IRON.get()).add(RegisterItems.CUTTER_STEEL.get()).add(RegisterItems.CUTTER_GOLD.get()).add(RegisterItems.CUTTER_DIAMOND.get()).add(RegisterItems.CUTTER_NETHERITE.get()).add(RegisterItems.CUTTER_RMA7012.get()).add(RegisterItems.CUTTER_RMA7024.get()).add(RegisterItems.CUTTER_D32.get());
        this.tag(ModTags.Items.HAMMER).add(RegisterItems.HAMMER_STONE.get()).add(RegisterItems.HAMMER_COPPER.get()).add(RegisterItems.HAMMER_TIN.get()).add(RegisterItems.HAMMER_BRONZE.get()).add(RegisterItems.HAMMER_IRON.get()).add(RegisterItems.HAMMER_GOLD.get()).add(RegisterItems.HAMMER_STEEL.get()).add(RegisterItems.HAMMER_DIAMOND.get()).add(RegisterItems.HAMMER_RMA7012.get()).add(RegisterItems.HAMMER_RMA7024.get()).add(RegisterItems.HAMMER_D32.get()).add(RegisterItems.HAMMER_NETHERITE.get()).add(RegisterItems.SLEDGEHAMMER.get());
        this.tag(ModTags.Items.SAW).add(RegisterItems.SAW_STONE.get()).add(RegisterItems.SAW_COPPER.get()).add(RegisterItems.SAW_TIN.get()).add(RegisterItems.SAW_BRONZE.get()).add(RegisterItems.SAW_IRON.get()).add(RegisterItems.SAW_GOLD.get()).add(RegisterItems.SAW_STEEL.get()).add(RegisterItems.SAW_DIAMOND.get()).add(RegisterItems.SAW_NETHERITE.get()).add(RegisterItems.SAW_RMA7012.get()).add(RegisterItems.SAW_RMA7024.get()).add(RegisterItems.SAW_D32.get());
        this.tag(ModTags.Items.KNIFE).add(RegisterItems.KITCHEN_KNIFE.get(), RegisterItems.TACTICAL_KNIFE.get());
        this.copyItems(ModTags.Items.KNIFE, ModTags.Items.KNIVES);
        this.tag(ItemTags.BEACON_PAYMENT_ITEMS).add(RegisterItems.INGOT_D32.get(), RegisterItems.INGOT_TIN.get(), RegisterItems.INGOT_STEEL.get(), RegisterItems.INGOT_MANGANESE.get(),
                RegisterItems.INGOT_BRONZE.get(), RegisterItems.INGOT_BRASS.get(), RegisterItems.INGOT_ZINC.get(), RegisterItems.INGOT_LEAD.get(), RegisterItems.INGOT_ALUMINIUM.get(),
                RegisterItems.INGOT_RMA7012.get(),RegisterItems.INGOT_RMA7024.get(), RegisterItems.INGOT_INCANDESCENT_ALLOY.get(), RegisterItems.PYROXENE.get());

        this.tag(ItemTags.MUSIC_DISCS).add(RegisterItems.DISC_CC5.get(), RegisterItems.DISC_DRAGONHEARTED.get(), RegisterItems.DISC_FRIDAYNIGHT.get(), RegisterItems.DISC_REVENGE.get(), RegisterItems.DISC_FALLEN_KINGDOM.get(), RegisterItems.DISC_TAKE_BACK_THE_NIGHT.get(), RegisterItems.DISC_FIND_THE_PIECES.get());

        this.tag(ModTags.Items.CURIOS_BACK).add(RegisterItems.DEFIB_CHARGER.get(), RegisterItems.EXCALIBUR_SHEATH.get());
        this.tag(ModTags.Items.CURIOS_HEAD).add(RegisterItems.GASMASK.get());
        this.tag(ModTags.Items.CURIOS_RING).add(RegisterItems.OATHRING.get());
    }

    protected void copyItems(TagKey<Item> from, TagKey<Item> to) {
        Tag.Builder tgt = this.getOrCreateRawBuilder(to);
        Tag.Builder original = this.getOrCreateRawBuilder(from);
        original.getEntries().forEach(tgt::add);
    }

}
