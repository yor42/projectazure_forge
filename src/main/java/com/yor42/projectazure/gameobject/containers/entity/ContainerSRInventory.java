package com.yor42.projectazure.gameobject.containers.entity;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.setup.register.RegisterContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ContainerSRInventory extends AbstractContainerInventory{

    public ContainerSRInventory(int id, PlayerInventory inventory, PacketBuffer data) {
        this(id, inventory, new ItemStackHandler(30), new ItemStackHandler(6), new ItemStackHandler(8), (AbstractEntityCompanion) inventory.player.level.getEntity(data.readInt()));
    }

    public ContainerSRInventory(int id, PlayerInventory inventory, IItemHandler entityInventory, IItemHandler EntityEquipment, IItemHandler EntityAmmo, AbstractEntityCompanion companion) {
        super(RegisterContainer.SR_CONTAINER.get(), id, companion);

        //mainhand
        this.addSlot(new SlotItemHandler(EntityEquipment, 0, 97, 83));
        //Offhands
        this.addSlot(new SlotItemHandler(EntityEquipment, 1, 115, 83));

        for (int m = 0; m < 3; m++) {
            for (int n = 0; n < 4; n++) {
                this.addSlot(new SlotItemHandler(entityInventory, n + 3 * m, 97 + n * 18, 25 + m * 18));
            }
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 7 + j * 18, 151 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 7 + k * 18, 209));
        }
    }
    @Nonnull
    public ItemStack quickMoveStack(@Nonnull PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack from = slot.getItem();
            itemstack = from.copy();
            int equipments = 2;
            int entityinv = equipments+12;
            int playerMainInv = entityinv + 27;
            int PlayerHotbar = playerMainInv + 9;
            if (index < equipments) {
                if (!this.moveItemStackTo(from, equipments, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (from.getItem() instanceof ToolItem) {
                if (!this.moveItemStackTo(from, 0, 2, false)) {
                    return ItemStack.EMPTY;
                } else {
                    if (!this.moveItemStackTo(from, 3, entityinv, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }
            else if (!this.moveItemStackTo(from, 2, entityinv, false)) {
                if (index >= playerMainInv && index < PlayerHotbar) {
                    if (!this.moveItemStackTo(from, equipments, playerMainInv, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < playerMainInv) {
                    if (!this.moveItemStackTo(from, playerMainInv, PlayerHotbar, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(from, playerMainInv, playerMainInv, false)) {
                    return ItemStack.EMPTY;
                }

                return ItemStack.EMPTY;
            }

            if (from.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }
}
