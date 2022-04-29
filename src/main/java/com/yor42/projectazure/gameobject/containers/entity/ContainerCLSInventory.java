package com.yor42.projectazure.gameobject.containers.entity;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.items.ItemMagazine;
import net.minecraft.network.FriendlyByteBuf ;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.yor42.projectazure.Main.CLS_INVENTORY_TYPE;

public class ContainerCLSInventory extends AbstractContainerMenu {

    public final AbstractEntityCompanion companion;
    public ContainerCLSInventory(int id, Inventory inventory, FriendlyByteBuf data) {
        this(id, inventory, new ItemStackHandler(16), new ItemStackHandler(6), new ItemStackHandler(8), (AbstractEntityCompanion) inventory.player.level.getEntity(data.readInt()));
    }
    public ContainerCLSInventory(int id, Inventory inventory, IItemHandler entityInventory, IItemHandler EntityEquipment, IItemHandler EntityAmmo, AbstractEntityCompanion companion) {
        super(CLS_INVENTORY_TYPE, id);

        this.companion = companion;
        //ZA HANDO
        for (int k = 0; k < 2; k++) {
            this.addSlot(new SlotItemHandler(EntityEquipment, k, 52, 58 + (19 * k)));
        }

        for (int l = 0; l < 4; l++) {
            this.addSlot(new SlotItemHandler(EntityEquipment, 2 + l, 131, 20 + l * 18) {
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
                this.addSlot(new SlotItemHandler(entityInventory, n + 3 * m, 154 + n * 18, 28 + m * 18));
            }
        }


        for (int m = 0; m < 4; m++) {
            for (int n = 0; n < 2; n++) {
                this.addSlot(new SlotItemHandler(EntityAmmo, n + (2 * m), 222 + n * 18, 21 + m * 18) {
                    @Override
                    public boolean mayPlace(@Nonnull ItemStack stack) {
                        return stack.getItem() instanceof ItemMagazine;
                    }
                });
            }
        }

        for (int l = 0; l < companion.getSkillItemCount(); l++) {
            this.addSlot(new SlotItemHandler(entityInventory, 12 + l, 7, 58 + l * 18) {
                @Override
                public boolean mayPlace(@Nonnull ItemStack stack) {
                    return ContainerCLSInventory.this.companion.isSkillItem(stack);
                }
            });
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 12 + j * 18, 114 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 12 + k * 18, 172));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        if(this.companion != null) {
            return player.distanceTo(this.companion)<=8;
        }
        return false;
    }

    public static class Supplier implements MenuProvider {

        AbstractEntityCompanion companion;

        public Supplier(AbstractEntityCompanion companion) {
            this.companion = companion;
        }

        @Override
        public Component getDisplayName() {
            return new TranslatableComponent("gui.companioninventory");
        }

        @Nullable
        @Override
        public AbstractContainerMenu createMenu(int openContainerId, Inventory inventory, Player player) {
            return new ContainerCLSInventory(openContainerId, inventory, this.companion.getInventory(), this.companion.getEquipment(), this.companion.getAmmoStorage(), this.companion);
        }
    }

    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            int i = 6;
            int entityinv = i+12;
            int ammoinv = entityinv+8;
            int playerMainInv = ammoinv + 27;
            int PlayerHotbar = playerMainInv + 9;
            if (index < i) {
                if (!this.moveItemStackTo(itemstack1, i, this.slots.size(), true)) {
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
            if(itemstack1.getItem() instanceof ItemMagazine){
                if (!this.moveItemStackTo(itemstack1, ammoinv, ammoinv+8, false)) {
                    return ItemStack.EMPTY;
                }
            }else if (index < entityinv) {
                if (!this.moveItemStackTo(itemstack1, ammoinv+1, playerMainInv, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.moveItemStackTo(itemstack1, 6, entityinv, false)) {
                if (index >= playerMainInv && index < PlayerHotbar) {
                    if (!this.moveItemStackTo(itemstack1, i, playerMainInv, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < playerMainInv) {
                    if (!this.moveItemStackTo(itemstack1, playerMainInv, PlayerHotbar, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(itemstack1, playerMainInv, playerMainInv, false)) {
                    return ItemStack.EMPTY;
                }

                return ItemStack.EMPTY;
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
