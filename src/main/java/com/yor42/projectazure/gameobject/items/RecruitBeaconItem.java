package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.client.renderer.block.MachineRecruitBeaconRenderer;
import com.yor42.projectazure.client.renderer.items.ItemRecruitBeaconRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RecruitBeaconItem extends AnimateableMachineBlockItems{
    public RecruitBeaconItem(Block blockIn, Properties builder, boolean shouldAddShiftToolTip) {
        super(blockIn, builder, shouldAddShiftToolTip);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected BlockEntityWithoutLevelRenderer getRenderer() {
        return new ItemRecruitBeaconRenderer();
    }
}
