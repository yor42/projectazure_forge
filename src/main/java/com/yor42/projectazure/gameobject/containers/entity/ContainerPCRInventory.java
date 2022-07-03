package com.yor42.projectazure.gameobject.containers.entity;

import com.tac.guns.item.AmmoItem;
import com.yor42.projectazure.gameobject.containers.slots.AmmoSlot;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.setup.register.RegisterContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ContainerPCRInventory extends Container {

    public final AbstractEntityCompanion companion;

    public ContainerPCRInventory(int id, PlayerInventory inventory, PacketBuffer data) {
        this(id, inventory, new ItemStackHandler(30), new ItemStackHandler(6), new ItemStackHandler(8), (AbstractEntityCompanion) inventory.player.level.getEntity(data.readInt()));
    }

    public ContainerPCRInventory(int id, PlayerInventory inventory, IItemHandler entityInventory, IItemHandler EntityEquipment, IItemHandler EntityAmmo, AbstractEntityCompanion companion) {
        super(RegisterContainer.PCR_CONTAINER.get(), id);
        this.companion = companion;

        //mainhand
        this.addSlot(new SlotItemHandler(EntityEquipment, 0, 20, 66));
        //Offhand
        this.addSlot(new SlotItemHandler(EntityEquipment, 1, 74, 66));

        this.addSlot(new SlotItemHandler(EntityEquipment, 2, 20, 28));
        this.addSlot(new SlotItemHandler(EntityEquipment, 3, 74, 28));
        this.addSlot(new SlotItemHandler(EntityEquipment, 4, 12, 47));
        this.addSlot(new SlotItemHandler(EntityEquipment, 5, 82, 47));

        for (int m = 0; m < 3; m++) {
            for (int n = 0; n < 4; n++) {
                this.addSlot(new SlotItemHandler(entityInventory, n + 3 * m, 110 + n * 18, 71 + m * 18));
            }
        }


        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 23 + j * 18, 152 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 23 + k * 18, 210));
        }

    }

    @Override
    public boolean stillValid(@Nonnull PlayerEntity p_75145_1_) {
        if (this.companion.isDeadOrDying()) {
            return false;
        } else {
            return p_75145_1_.distanceToSqr(this.companion.getX() + 0.5D, this.companion.getY() + 0.5D, this.companion.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Nonnull
    public ItemStack quickMoveStack(@Nonnull PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            int equipments = 6;
            int entityinv = equipments+12;
            int playerMainInv = entityinv + 27;
            int PlayerHotbar = playerMainInv + 9;
            if (index < entityinv) {
                if (!this.moveItemStackTo(itemstack1, entityinv, playerMainInv, true)) {
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

    public static class Supplier implements INamedContainerProvider {

        AbstractEntityCompanion companion;

        public Supplier(AbstractEntityCompanion companion) {
            this.companion = companion;
        }

        @Nonnull
        @Override
        public ITextComponent getDisplayName() {
            return new TranslationTextComponent("gui.companioninventory");
        }

        @Nullable
        @Override
        public Container createMenu(int openContainerId, @Nonnull PlayerInventory inventory, @Nonnull PlayerEntity player) {
            return new ContainerPCRInventory(openContainerId, inventory, this.companion.getInventory(), this.companion.getEquipment(), this.companion.getAmmoStorage(), this.companion);
        }
    }
}
