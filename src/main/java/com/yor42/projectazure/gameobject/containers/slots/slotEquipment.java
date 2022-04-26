package com.yor42.projectazure.gameobject.containers.slots;

import com.yor42.projectazure.gameobject.items.shipEquipment.ItemEquipmentBase;
import com.yor42.projectazure.libs.enums;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class slotEquipment extends SlotItemHandler {

    private final enums.SLOTTYPE slottype;
    private final IItemHandler handler;
    private ItemStack stack;
    private LivingEntity host;

    //clientDummy
    public slotEquipment(IItemHandler itemHandler, int index, int xPosition, int yPosition, enums.SLOTTYPE type) {
        super(itemHandler, index, xPosition, yPosition);
        this.slottype = type;
        this.handler = itemHandler;
    }


    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {

        if(this.slottype == enums.SLOTTYPE.MAIN_GUN){
            return stack.getItem() instanceof ItemEquipmentBase && (((ItemEquipmentBase) stack.getItem()).getSlot() == enums.SLOTTYPE.MAIN_GUN || ((ItemEquipmentBase) stack.getItem()).getSlot() == enums.SLOTTYPE.SUB_GUN);
        }

        return stack.getItem() instanceof ItemEquipmentBase && ((ItemEquipmentBase) stack.getItem()).getSlot() == this.slottype;
    }

}
