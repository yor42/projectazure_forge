package com.yor42.projectazure.gameobject.items;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.level.block.Block;

public class RecruitBeaconItem extends AnimateableMachineBlockItems{
    public RecruitBeaconItem(Block blockIn, Properties builder, boolean shouldAddShiftToolTip) {
        super(blockIn, builder, shouldAddShiftToolTip);
    }

    @Override
    protected BlockEntityWithoutLevelRenderer getRenderer() {
        return null;
    }
}
