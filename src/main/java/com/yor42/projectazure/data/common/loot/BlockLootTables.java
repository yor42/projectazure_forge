package com.yor42.projectazure.data.common.loot;

import com.yor42.projectazure.libs.defined;
import com.yor42.projectazure.setup.register.registerBlocks;
import net.minecraft.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.stream.Collectors;

public class BlockLootTables extends net.minecraft.data.loot.BlockLootTables {

    @Override
    protected void addTables() {
        registerDropSelfLootTable(registerBlocks.BAUXITE_ORE.get());
        registerDropSelfLootTable(registerBlocks.COPPER_ORE.get());
        registerDropSelfLootTable(registerBlocks.TIN_ORE.get());
        registerDropSelfLootTable(registerBlocks.LEAD_ORE.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ForgeRegistries.BLOCKS.getValues().stream()
                .filter(block -> defined.MODID.equals(block.getRegistryName().getNamespace()))
                .collect(Collectors.toSet());
    }
}
