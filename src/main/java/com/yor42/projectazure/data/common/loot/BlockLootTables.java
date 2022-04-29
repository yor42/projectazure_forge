package com.yor42.projectazure.data.common.loot;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

import static net.minecraft.world.level.storage.loot.providers.number.NumberProviders.CONSTANT;

public class BlockLootTables extends net.minecraft.data.loot.BlockLoot {
    private static final LootItemCondition.Builder WITH_SLEDGEHAMMER = MatchTool.toolMatches(ItemPredicate.Builder.item().of(Main.SLEDGEHAMMER.get()));

    @Override
    protected void addTables() {
        this.dropSelf(Main.BAUXITE_ORE.get());
        this.dropSelf(Main.COPPER_ORE.get());
        this.dropSelf(Main.TIN_ORE.get());
        this.dropSelf(Main.LEAD_ORE.get());
        this.dropSelf(Main.ZINC_ORE.get());
        this.dropSelf(Main.REENFORCED_PLANK.get());
        this.dropSelf(Main.MACHINE_FRAME.get());
        this.dropSelf(Main.REENFORCEDCONCRETE.get());
        this.dropSelf(Main.DRYDOCKCONTROLLER.get());
        this.add(Main.METAL_PRESS.get(), net.minecraft.data.loot.BlockLoot::createNameableBlockEntityTable);
        this.add(Main.BASIC_REFINERY.get(), net.minecraft.data.loot.BlockLoot::createNameableBlockEntityTable);
        this.add(Main.ALLOY_FURNACE.get(), net.minecraft.data.loot.BlockLoot::createNameableBlockEntityTable);
        this.add(Main.RECRUIT_BEACON.get(), net.minecraft.data.loot.BlockLoot::createNameableBlockEntityTable);
        this.add(Main.CRYSTAL_GROWTH_CHAMBER.get(), net.minecraft.data.loot.BlockLoot::createNameableBlockEntityTable);
        this.add(Main.BOUNDING_BOX.get(), net.minecraft.data.loot.BlockLoot.noDrop());
        this.add(Main.ORIROCK.get(), (orirock) -> createSilkTouchDispatchTable(orirock, applyExplosionCondition(orirock, LootItem.lootTableItem(Main.ORIGINITE.get()).when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.2F, 0.35F, 0.5F, 1.0F)).otherwise(LootItem.lootTableItem(orirock)))));
    }
    //.acceptFunction(SetCount.builder(RandomValueRange.of(4.0F, 5.0F)))
    protected static LootTable.Builder onlyWithHammerDoubling(ItemLike item) {
        return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(new NumberProvider() {
            @Override
            public float getFloat(LootContext p_165730_) {
                return 1.0F;
            }

            @Override
            public LootNumberProviderType getType() {
                return CONSTANT;
            }
        }).when(WITH_SLEDGEHAMMER).apply(SetItemCountFunction.setCount(new NumberProvider() {
            @Override
            public float getFloat(LootContext p_165730_) {
                return 2.0F;
            }

            @Override
            public LootNumberProviderType getType() {
                return CONSTANT;
            }
        })).add(LootItem.lootTableItem(item)));
    }

    @Nonnull
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ForgeRegistries.BLOCKS.getValues().stream()
                .filter(block -> Constants.MODID.equals(block.getRegistryName().getNamespace()))
                .collect(Collectors.toSet());
    }
}
