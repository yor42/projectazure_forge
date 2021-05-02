package com.yor42.projectazure.data.common.loot;

import com.yor42.projectazure.libs.defined;
import com.yor42.projectazure.setup.register.registerBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.stream.Collectors;

public class BlockLootTables extends net.minecraft.data.loot.BlockLootTables {

    @Override
    protected void addTables() {
        this.registerDropSelfLootTable(registerBlocks.BAUXITE_ORE.get());
        this.registerDropSelfLootTable(registerBlocks.COPPER_ORE.get());
        this.registerDropSelfLootTable(registerBlocks.TIN_ORE.get());
        this.registerDropSelfLootTable(registerBlocks.LEAD_ORE.get());
        this.registerLootTable(registerBlocks.METAL_PRESS.get(), net.minecraft.data.loot.BlockLootTables::droppingWithName);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ForgeRegistries.BLOCKS.getValues().stream()
                .filter(block -> defined.MODID.equals(block.getRegistryName().getNamespace()))
                .collect(Collectors.toSet());
    }
}
