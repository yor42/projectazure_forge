package com.yor42.projectazure.data.common.loot;

import com.yor42.projectazure.libs.defined;
import com.yor42.projectazure.setup.register.registerBlocks;
import com.yor42.projectazure.setup.register.registerItems;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.conditions.TableBonus;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

public class BlockLootTables extends net.minecraft.data.loot.BlockLootTables {

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
        this.registerLootTable(registerBlocks.ALLOY_FURNACE.get(), net.minecraft.data.loot.BlockLootTables::droppingWithName);
        this.registerLootTable(registerBlocks.RECRUIT_BEACON.get(), net.minecraft.data.loot.BlockLootTables::droppingWithName);
        this.registerLootTable(registerBlocks.ORIROCK.get(), (orirock) -> droppingWithSilkTouch(orirock, withSurvivesExplosion(orirock, ItemLootEntry.builder(registerItems.ORIGINITE.get()).acceptCondition(TableBonus.builder(Enchantments.FORTUNE, 0.2F, 0.35F, 0.5F, 1.0F)).alternatively(ItemLootEntry.builder(orirock)))));

    }

    @Nonnull
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ForgeRegistries.BLOCKS.getValues().stream()
                .filter(block -> defined.MODID.equals(block.getRegistryName().getNamespace()))
                .collect(Collectors.toSet());
    }
}
