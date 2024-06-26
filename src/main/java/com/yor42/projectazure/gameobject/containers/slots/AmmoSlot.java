package com.yor42.projectazure.gameobject.containers.slots;

import com.tac.guns.item.AmmoItem;
import com.yor42.projectazure.gameobject.items.ItemCannonshell;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class AmmoSlot extends SlotItemHandler {

    public AmmoSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {

        if(!(stack.getItem() instanceof AmmoItem || stack.getItem() instanceof ArrowItem || stack.getItem() instanceof ItemCannonshell)){
            return false;
        }

        return super.mayPlace(stack);
    }
}
