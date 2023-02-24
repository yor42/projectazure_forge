package com.yor42.projectazure.gameobject.containers.slots;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenBase;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SlotRigging extends SlotItemHandler {
    @Nullable
    private final AbstractEntityCompanion hostEntity;

    public SlotRigging(IItemHandler itemHandler, int index, int xPosition, int yPosition, @Nullable AbstractEntityCompanion hostEntity) {
        super(itemHandler, index, xPosition, yPosition);
        this.hostEntity = hostEntity;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {

        if (!(stack.getItem() instanceof ItemRiggingBase)) {
            return false;
        }

        if (this.hostEntity == null) {
            return stack.getItem() instanceof ItemRiggingBase;
        }
        return ((ItemRiggingBase) stack.getItem()).getValidclass().test(this.hostEntity);
    }
}
