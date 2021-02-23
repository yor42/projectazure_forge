package com.yor42.projectazure.gameobject.capability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class RiggingDefaultCVEquipmentCapability extends RiggingEquipmentCapability{
    public RiggingDefaultCVEquipmentCapability(ItemStack stack) {
        super(stack);
    }

    public RiggingDefaultCVEquipmentCapability(ItemStack stack, LivingEntity entity) {
        super(stack, entity);
    }

    public RiggingDefaultCVEquipmentCapability(ItemStack stack, LivingEntity entity, boolean isEquippedonShip) {
        super(stack, entity, isEquippedonShip);
    }

    @Override
    public int getAAslotCount() {
        return 4;
    }

    @Override
    public int getGunSlotCount() {
        return 2;
    }

    @Override
    public int getTorpedoSlotCount() {
        return 0;
    }

    @Override
    public ItemStackHandler getEquipments() {
        return null;
    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        return null;
    }
}
