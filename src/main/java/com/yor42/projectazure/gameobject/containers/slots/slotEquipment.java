package com.yor42.projectazure.gameobject.containers.slots;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.items.ItemEquipmentBase;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.network.packets.syncRiggingInventoryPacket;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class slotEquipment extends SlotItemHandler {

    private final enums.SLOTTYPE slottype;
    private final ItemStackHandler handler;
    private ItemStack stack;
    private LivingEntity host;

    //clientDummy
    public slotEquipment(ItemStackHandler itemHandler, int index, int xPosition, int yPosition, enums.SLOTTYPE type) {
        super(itemHandler, index, xPosition, yPosition);
        this.slottype = type;
        this.handler = itemHandler;
    }


    @Override
    public int getSlotStackLimit() {
        return 1;
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        return stack.getItem() instanceof ItemEquipmentBase && ((ItemEquipmentBase) stack.getItem()).getSlot() == this.slottype;
    }

}
