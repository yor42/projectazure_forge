package com.yor42.projectazure.gameobject.items.tools;

import com.yor42.projectazure.Main;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.state.BlockState;

public class ItemSledgeHammer extends DiggerItem {
    public ItemSledgeHammer(float attackDamageIn, float attackSpeedIn, Tier tier, Properties builderIn) {
        super(attackDamageIn, attackSpeedIn, tier, BlockTags.MINEABLE_WITH_PICKAXE, builderIn);
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState blockIn) {
        if (Main.CRUSHING_REGISTRY.isHammerable(blockIn.getBlock())) {
            return true;
        }
        return super.isCorrectToolForDrops(blockIn);
    }
}
