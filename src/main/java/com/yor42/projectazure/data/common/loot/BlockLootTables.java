package com.yor42.projectazure.data.common.loot;

import com.lowdragmc.multiblocked.api.block.BlockComponent;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.setup.register.RegisterBlocks;
import com.yor42.projectazure.setup.register.RegisterItems;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.MatchTool;
import net.minecraft.loot.conditions.TableBonus;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

public class BlockLootTables extends net.minecraft.data.loot.BlockLootTables {
    private static final ILootCondition.IBuilder WITH_SLEDGEHAMMER = MatchTool.toolMatches(ItemPredicate.Builder.item().of(RegisterItems.SLEDGEHAMMER.get()));

    @Override
    protected void addTables() {
        this.dropSelf(RegisterBlocks.BAUXITE_ORE.get());
        this.dropSelf(RegisterBlocks.COPPER_ORE.get());
        this.dropSelf(RegisterBlocks.TIN_ORE.get());
        this.dropSelf(RegisterBlocks.LEAD_ORE.get());
        this.dropSelf(RegisterBlocks.ZINC_ORE.get());
        this.dropSelf(RegisterBlocks.RMA_7012_ORE.get());
        this.dropSelf(RegisterBlocks.MANGANESE_ORE.get());
        this.dropSelf(RegisterBlocks.BASIC_CHEMICAL_REACTOR.get());

        this.dropSelf(RegisterBlocks.BRASS_BLOCK.get());
        this.dropSelf(RegisterBlocks.COPPER_BLOCK.get());
        this.dropSelf(RegisterBlocks.TIN_BLOCK.get());
        this.dropSelf(RegisterBlocks.ZINC_BLOCK.get());
        this.dropSelf(RegisterBlocks.LEAD_BLOCK.get());
        this.dropSelf(RegisterBlocks.BRONZE_BLOCK.get());
        this.dropSelf(RegisterBlocks.STEEL_BLOCK.get());
        this.dropSelf(RegisterBlocks.ALUMINIUM_BLOCK.get());
        this.dropSelf(RegisterBlocks.RMA_7012_BLOCK.get());
        this.dropSelf(RegisterBlocks.RMA_7024_BLOCK.get());
        this.dropSelf(RegisterBlocks.D32_BLOCK.get());
        this.dropSelf(RegisterBlocks.INCANDESCENT_ALLOY_BLOCK.get());
        this.dropSelf(RegisterBlocks.MANGANESE_BLOCK.get());

        this.dropSelf(RegisterBlocks.REENFORCED_PLANK.get());
        this.dropSelf(RegisterBlocks.MACHINE_FRAME.get());
        this.dropSelf(RegisterBlocks.MACHINE_FRAME_SLAB.get());
        this.dropSelf(RegisterBlocks.REENFORCEDCONCRETE.get());
        this.dropSelf(RegisterBlocks.ASPHALTCONCRETE.get());
        this.dropSelf(RegisterBlocks.STEEL_FOUNDATION.get());

        this.dropSelf(RegisterBlocks.MACHINE_COMPONENTBLOCK.get());
        this.dropSelf(RegisterBlocks.MACHINE_DYNAMO.get());
        this.dropSelf(RegisterBlocks.CRIMSON_PANTRY.get());
        this.dropSelf(RegisterBlocks.OAK_PANTRY.get());
        this.dropSelf(RegisterBlocks.SPRUCE_PANTRY.get());
        this.dropSelf(RegisterBlocks.ACACIA_PANTRY.get());
        this.dropSelf(RegisterBlocks.BIRCH_PANTRY.get());
        this.dropSelf(RegisterBlocks.JUNGLE_PANTRY.get());
        this.dropSelf(RegisterBlocks.DARK_OAK_PANTRY.get());
        this.dropSelf(RegisterBlocks.WARPED_PANTRY.get());
        this.dropSelf(RegisterBlocks.COBBLED_ORIROCK.get());

        this.add(RegisterBlocks.METAL_PRESS.get(), net.minecraft.data.loot.BlockLootTables::createNameableBlockEntityTable);
        this.add(RegisterBlocks.BASIC_REFINERY.get(), net.minecraft.data.loot.BlockLootTables::createNameableBlockEntityTable);
        this.add(RegisterBlocks.ALLOY_FURNACE.get(), net.minecraft.data.loot.BlockLootTables::createNameableBlockEntityTable);
        this.add(RegisterBlocks.RECRUIT_BEACON.get(), net.minecraft.data.loot.BlockLootTables::createNameableBlockEntityTable);
        this.add(RegisterBlocks.CRYSTAL_GROWTH_CHAMBER.get(), net.minecraft.data.loot.BlockLootTables::createNameableBlockEntityTable);
        this.add(RegisterBlocks.ORIROCK.get(), (orirock) -> createSilkTouchDispatchTable(orirock, applyExplosionCondition(orirock, ItemLootEntry.lootTableItem(RegisterItems.ORIGINITE.get()).when(TableBonus.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.2F, 0.35F, 0.5F, 1.0F)).otherwise(ItemLootEntry.lootTableItem(RegisterBlocks.COBBLED_ORIROCK.get())))));
    }
    protected static LootTable.Builder onlyWithHammerDoubling(IItemProvider item) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantRange.exactly(1)).when(WITH_SLEDGEHAMMER).apply(SetCount.setCount(RandomValueRange.between(2.0F, 2.1F))).add(ItemLootEntry.lootTableItem(item)));
    }

    @Nonnull
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ForgeRegistries.BLOCKS.getValues().stream()
                .filter(block -> Constants.MODID.equals(block.getRegistryName().getNamespace()) && !(block instanceof BlockComponent))
                .collect(Collectors.toSet());
    }
}
