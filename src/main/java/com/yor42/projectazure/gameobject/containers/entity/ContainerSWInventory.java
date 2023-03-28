package com.yor42.projectazure.gameobject.containers.entity;

import com.tac.guns.item.AmmoItem;
import com.yor42.projectazure.gameobject.containers.slots.AmmoSlot;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.setup.register.RegisterContainer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ContainerSWInventory extends AbstractContainerInventory {

    public ContainerSWInventory(int id, Inventory inventory) {
        super(RegisterContainer.SW_CONTAINER.get(), id, null);
    }

    public ContainerSWInventory(int id, Inventory inventory, IItemHandler entityInventory, IItemHandler EntityEquipment, IItemHandler EntityAmmo, AbstractEntityCompanion companion) {
        super(RegisterContainer.SW_CONTAINER.get(), id, companion);
        //ZA HANDO
        for (int k = 0; k < 2; k++) {
            this.addSlot(new SlotItemHandler(EntityEquipment, k, 122 + (20 * k), 100));
        }

        for (int l = 0; l < 4; l++) {
            this.addSlot(new SlotItemHandler(EntityEquipment, 2 + l, 99, 24 + l * 19) {
                public int getMaxStackSize() {
                    return 1;
                }

                @Override
                public boolean mayPlace(@Nonnull ItemStack stack) {
                    return stack.getItem() instanceof ArmorItem;
                }
            });
        }

        for (int m = 0; m < 4; m++) {
            for (int n = 0; n < 3; n++) {
                this.addSlot(new SlotItemHandler(entityInventory, n + 3 * m, 122 + n * 19, 24 + m * 19));
            }
        }


        for (int m = 0; m < 4; m++) {
            for (int n = 0; n < 2; n++) {
                this.addSlot(new AmmoSlot(EntityAmmo, n + (2 * m), 183 + n * 19, 24 + m * 19));
            }
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 32 + j * 18, 158 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 32 + k * 18, 216));
        }
    }

    public ContainerSWInventory(int id, Inventory inventory, FriendlyByteBuf data) {
        this(id, inventory, new ItemStackHandler(16), new ItemStackHandler(6), new ItemStackHandler(8), (AbstractEntityCompanion) inventory.player.level.getEntity(data.readInt()));

    }

    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            int equipments = 6;
            int entityinv = equipments+12;
            int ammoinv = entityinv+8;
            int playerMainInv = ammoinv + 27;
            int PlayerHotbar = playerMainInv + 9;
            if (index < equipments) {
                if (!this.moveItemStackTo(itemstack1, equipments, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(2).mayPlace(itemstack1) && !this.getSlot(2).hasItem()) {
                if (!this.moveItemStackTo(itemstack1, 2, 3, false)) {
                    return ItemStack.EMPTY;
                }
            }else if (this.getSlot(3).mayPlace(itemstack1) && !this.getSlot(3).hasItem()) {
                if (!this.moveItemStackTo(itemstack1, 3, 4, false)) {
                    return ItemStack.EMPTY;
                }
            }else if (this.getSlot(4).mayPlace(itemstack1) && !this.getSlot(4).hasItem()) {
                if (!this.moveItemStackTo(itemstack1, 4, 5, false)) {
                    return ItemStack.EMPTY;
                }
            }else if (this.getSlot(5).mayPlace(itemstack1) && !this.getSlot(5).hasItem()) {
                if (!this.moveItemStackTo(itemstack1, 5, 6, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if(itemstack1.getItem() instanceof AmmoItem){
                if (!this.moveItemStackTo(itemstack1, ammoinv, ammoinv+8, false)) {
                    return ItemStack.EMPTY;
                }
            }else if (index < entityinv) {
                if (!this.moveItemStackTo(itemstack1, ammoinv+1, playerMainInv, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.moveItemStackTo(itemstack1, equipments, entityinv, false)) {
                return ItemStack.EMPTY;
            }
            else if (index >= playerMainInv && index < PlayerHotbar) {
                if (!this.moveItemStackTo(itemstack1, equipments, playerMainInv, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < playerMainInv) {
                if (!this.moveItemStackTo(itemstack1, playerMainInv, PlayerHotbar, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }
}
