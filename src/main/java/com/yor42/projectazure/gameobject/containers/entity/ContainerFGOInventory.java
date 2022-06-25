package com.yor42.projectazure.gameobject.containers.entity;

import com.yor42.projectazure.gameobject.containers.slots.AmmoSlot;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.items.ItemMagazine;
import com.yor42.projectazure.setup.register.RegisterContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ContainerFGOInventory extends Container {

    public final AbstractEntityCompanion companion;

    public ContainerFGOInventory(int id, PlayerInventory inventory, PacketBuffer data) {
        this(id, inventory, new ItemStackHandler(30), new ItemStackHandler(6), new ItemStackHandler(8), (AbstractEntityCompanion) inventory.player.level.getEntity(data.readInt()));
    }

    public ContainerFGOInventory(int id, PlayerInventory inventory, IItemHandler entityInventory, IItemHandler EntityEquipment, IItemHandler EntityAmmo, AbstractEntityCompanion companion) {
        super(RegisterContainer.FGO_CONTAINER.get(), id);
        this.companion = companion;

        //mainhand
        this.addSlot(new SlotItemHandler(EntityEquipment, 0, 81, 94));
        //Offhands
        this.addSlot(new SlotItemHandler(EntityEquipment, 1, 81, 112));

        for (int m = 0; m < 2; m++) {
            for (int n = 0; n < 6; n++) {
                this.addSlot(new AmmoSlot(entityInventory, n + 6 * m, 101 + n * 18, 94 + m * 18));
            }
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 26 + j * 18, 151 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 26 + k * 18, 209));
        }

    }


    @Override
    public boolean stillValid(PlayerEntity p_75145_1_) {
        if (this.companion.isDeadOrDying()) {
            return false;
        } else {
            return p_75145_1_.distanceToSqr(this.companion.getX() + 0.5D, this.companion.getY() + 0.5D, this.companion.getZ() + 0.5D) <= 64.0D;
        }
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
            return new ContainerFGOInventory(openContainerId, inventory, this.companion.getInventory(), this.companion.getEquipment(), this.companion.getAmmoStorage(), this.companion);
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
