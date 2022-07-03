package com.yor42.projectazure.data.common.loot;

import com.lowdragmc.multiblocked.api.block.BlockComponent;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.OriginiumGeneratorControllerTE;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.setup.register.registerBlocks;
import com.yor42.projectazure.setup.register.registerItems;
import com.yor42.projectazure.setup.register.registerMultiBlocks;
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
    private static final ILootCondition.IBuilder WITH_SLEDGEHAMMER = MatchTool.toolMatches(ItemPredicate.Builder.item().of(registerItems.SLEDGEHAMMER.get()));

    @Override
    protected void addTables() {
        this.dropSelf(registerBlocks.BAUXITE_ORE.get());
        this.dropSelf(registerBlocks.COPPER_ORE.get());
        this.dropSelf(registerBlocks.TIN_ORE.get());
        this.dropSelf(registerBlocks.LEAD_ORE.get());
        this.dropSelf(registerBlocks.ZINC_ORE.get());
        this.dropSelf(registerBlocks.REENFORCED_PLANK.get());
        this.dropSelf(registerBlocks.MACHINE_FRAME.get());
        this.dropSelf(registerBlocks.REENFORCEDCONCRETE.get());

        this.dropSelf(registerBlocks.MACHINE_COMPONENTBLOCK.get());
        this.dropSelf(registerBlocks.MACHINE_DYNAMO.get());
        this.dropSelf(registerBlocks.CRIMSON_PANTRY.get());
        this.dropSelf(registerBlocks.OAK_PANTRY.get());
        this.dropSelf(registerBlocks.SPRUCE_PANTRY.get());
        this.dropSelf(registerBlocks.ACACIA_PANTRY.get());
        this.dropSelf(registerBlocks.BIRCH_PANTRY.get());
        this.dropSelf(registerBlocks.JUNGLE_PANTRY.get());
        this.dropSelf(registerBlocks.DARK_OAK_PANTRY.get());
        this.dropSelf(registerBlocks.WARPED_PANTRY.get());

        this.add(registerBlocks.METAL_PRESS.get(), net.minecraft.data.loot.BlockLootTables::createNameableBlockEntityTable);
        this.add(registerBlocks.BASIC_REFINERY.get(), net.minecraft.data.loot.BlockLootTables::createNameableBlockEntityTable);
        this.add(registerBlocks.ALLOY_FURNACE.get(), net.minecraft.data.loot.BlockLootTables::createNameableBlockEntityTable);
        this.add(registerBlocks.RECRUIT_BEACON.get(), net.minecraft.data.loot.BlockLootTables::createNameableBlockEntityTable);
        this.add(registerBlocks.CRYSTAL_GROWTH_CHAMBER.get(), net.minecraft.data.loot.BlockLootTables::createNameableBlockEntityTable);
        this.add(registerBlocks.ORIROCK.get(), (orirock) -> createSilkTouchDispatchTable(orirock, applyExplosionCondition(orirock, ItemLootEntry.lootTableItem(registerItems.ORIGINITE.get()).when(TableBonus.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.2F, 0.35F, 0.5F, 1.0F)).otherwise(ItemLootEntry.lootTableItem(orirock)))));
    }
    //.acceptFunction(SetCount.builder(RandomValueRange.of(4.0F, 5.0F)))
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
