package com.yor42.projectazure.gameobject.containers.entity;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractContainerInventory extends Container {

    public final AbstractEntityCompanion companion;
    protected AbstractContainerInventory(@Nullable ContainerType<?> p_i50105_1_, int p_i50105_2_, AbstractEntityCompanion companion) {
        super(p_i50105_1_, p_i50105_2_);
        this.companion = companion;
    }

    @Override
    public boolean stillValid(@Nonnull PlayerEntity p_75145_1_) {
        if (this.companion.isDeadOrDying()) {
            return false;
        } else {
            return p_75145_1_.distanceToSqr(this.companion.getX() + 0.5D, this.companion.getY() + 0.5D, this.companion.getZ() + 0.5D) <= 64.0D;
        }
    }
}
