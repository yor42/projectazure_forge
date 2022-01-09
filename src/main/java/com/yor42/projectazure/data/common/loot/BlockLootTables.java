package com.yor42.projectazure.data.common.loot;

import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.setup.register.registerBlocks;
import com.yor42.projectazure.setup.register.registerItems;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.MatchTool;
import net.minecraft.loot.conditions.TableBonus;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

public class BlockLootTables extends net.minecraft.data.loot.BlockLootTables {
    private static final ILootCondition.IBuilder WITH_SLEDGEHAMMER = MatchTool.builder(ItemPredicate.Builder.create().item(registerItems.SLEDGEHAMMER.get()));

    @Override
    protected void addTables() {
        this.registerDropSelfLootTable(registerBlocks.BAUXITE_ORE.get());
        this.registerDropSelfLootTable(registerBlocks.COPPER_ORE.get());
        this.registerDropSelfLootTable(registerBlocks.TIN_ORE.get());
        this.registerDropSelfLootTable(registerBlocks.LEAD_ORE.get());
        this.registerDropSelfLootTable(registerBlocks.ZINC_ORE.get());
        this.registerDropSelfLootTable(registerBlocks.REENFORCED_PLANK.get());
        this.registerDropSelfLootTable(registerBlocks.MACHINE_FRAME.get());
        this.registerDropSelfLootTable(registerBlocks.REENFORCEDCONCRETE.get());
        this.registerDropSelfLootTable(registerBlocks.DRYDOCKCONTROLLER.get());
        this.registerLootTable(registerBlocks.METAL_PRESS.get(), net.minecraft.data.loot.BlockLootTables::droppingWithName);
        this.registerLootTable(registerBlocks.BASIC_REFINERY.get(), net.minecraft.data.loot.BlockLootTables::droppingWithName);
        this.registerLootTable(registerBlocks.ALLOY_FURNACE.get(), net.minecraft.data.loot.BlockLootTables::droppingWithName);
        this.registerLootTable(registerBlocks.RECRUIT_BEACON.get(), net.minecraft.data.loot.BlockLootTables::droppingWithName);
        this.registerLootTable(registerBlocks.CRYSTAL_GROWTH_CHAMBER.get(), net.minecraft.data.loot.BlockLootTables::droppingWithName);
        this.registerLootTable(registerBlocks.BOUNDING_BOX.get(), net.minecraft.data.loot.BlockLootTables.blockNoDrop());
        this.registerLootTable(registerBlocks.ORIROCK.get(), (orirock) -> droppingWithSilkTouch(orirock, withSurvivesExplosion(orirock, ItemLootEntry.builder(registerItems.ORIGINITE.get()).acceptCondition(TableBonus.builder(Enchantments.FORTUNE, 0.2F, 0.35F, 0.5F, 1.0F)).alternatively(ItemLootEntry.builder(orirock)))));
    }
    //.acceptFunction(SetCount.builder(RandomValueRange.of(4.0F, 5.0F)))
    protected static LootTable.Builder onlyWithHammerDoubling(IItemProvider item) {
        return LootTable.builder().addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).acceptCondition(WITH_SLEDGEHAMMER).acceptFunction(SetCount.builder(RandomValueRange.of(2.0F, 2.1F))).addEntry(ItemLootEntry.builder(item)));
    }

    @Nonnull
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ForgeRegistries.BLOCKS.getValues().stream()
                .filter(block -> Constants.MODID.equals(block.getRegistryName().getNamespace()))
                .collect(Collectors.toSet());
    }
}
