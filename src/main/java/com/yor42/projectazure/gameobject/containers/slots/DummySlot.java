package com.yor42.projectazure.gameobject.containers.slots;

import com.yor42.projectazure.gameobject.containers.machine.ContainerDroneDockingStation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class DummySlot extends Slot {

    private static final IInventory emptyInventory = new Inventory(0);
    private final ItemStackHandler inventory = new ItemStackHandler(1);
    private final ContainerDroneDockingStation screen;
    public DummySlot(ContainerDroneDockingStation screen, int x, int y) {
        super(emptyInventory, 1, x, y);
        this.screen = screen;
    }

    @Override
    public boolean mayPickup(PlayerEntity playerIn) {
        return false;
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        this.screen.SetFilterIDFromItemStack(stack);
        return false;
    }

    @Override
    public void set(ItemStack p_75215_1_) {

    }

    @Override
    protected void onQuickCraft(ItemStack p_75210_1_, int p_75210_2_) {

    }

    @Override
    public int getMaxStackSize()
    {
        return this.inventory.getSlotLimit(this.index);
    }

    @Override
    public boolean isActive() {
        return this.screen.getScreenMode() == ContainerDroneDockingStation.ScreenMode.EDITFILTER;
    }

    @Nonnull
    @Override
    public ItemStack remove(int p_75209_1_) {
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ItemStack getItem() {
        return super.getItem();
    }
}
