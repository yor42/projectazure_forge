package com.yor42.projectazure.gameobject.items.tools;

import net.minecraft.block.Block;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ToolItem;

import java.util.Set;

public class ItemSledgeHammer extends ToolItem {
    public ItemSledgeHammer(float attackDamageIn, float attackSpeedIn, IItemTier tier, Set<Block> effectiveBlocksIn, Properties builderIn) {
        super(attackDamageIn, attackSpeedIn, tier, effectiveBlocksIn, builderIn);
    }
}
