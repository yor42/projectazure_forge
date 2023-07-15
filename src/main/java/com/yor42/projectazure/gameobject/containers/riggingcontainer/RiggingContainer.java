package com.yor42.projectazure.gameobject.containers.riggingcontainer;

import com.yor42.projectazure.gameobject.capability.multiinv.IMultiInventory;
import com.yor42.projectazure.gameobject.capability.multiinv.MultiInvStackHandler;
import com.yor42.projectazure.gameobject.capability.multiinv.MultiInvUtil;
import com.yor42.projectazure.gameobject.containers.slots.slotEquipment;
import com.yor42.projectazure.gameobject.containers.slots.slotInventory;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemEquipmentBase;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemEquipmentGun;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemEquipmentTorpedo;
import com.yor42.projectazure.libs.enums;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;
import java.util.function.Function;

import static com.yor42.projectazure.setup.register.RegisterContainer.RIGGING_INVENTORY;

public class RiggingContainer extends AbstractContainerMenu {

    public ItemStack riggingStack;
    public UUID previousEntityUUID;


    public RiggingContainer(int id, Inventory playerInv, ItemStack riggingStack, @Nullable UUID ownerID) {
        super(RIGGING_INVENTORY.get(), id);
        this.riggingStack = riggingStack;
        this.previousEntityUUID = ownerID;

        IMultiInventory inventories = MultiInvUtil.getCap(this.riggingStack);
        for(enums.SLOTTYPE slottype:inventories.getAllKeys()){
            Function<Integer, Integer> x;
            Function<Integer, Integer> y;

            switch (slottype) {
                default -> {
                    x = i -> 7;
                    y = i -> 34 + 18 * i;
                }
                case SUB_GUN -> {
                    x = i -> 31 + 18 * i;
                    y = i -> 34;
                }
                case AA -> {
                    x = i -> 151;
                    y = i -> 34 + 18 * i;
                }
                case TORPEDO -> {
                    x = i -> 30 + 18 * i;
                    y = i -> 77;
                }
                case PLANE -> {
                    x = i -> 30 + 18 * i;
                    y = i -> 30;
                }
            }

            this.addSlots(inventories.getInventory(slottype), x,y,slottype);

        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new slotInventory(playerInv, j + i * 9 + 9, 8 + j * 18, 110 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new slotInventory(playerInv, k, 8 + k * 18, 168));
        }
    }

    public RiggingContainer(int id, Inventory inventory, FriendlyByteBuf data) {
        this(id, inventory, data.readItem(), data.readUUID());
    }

    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        IMultiInventory inventories = MultiInvUtil.getCap(this.riggingStack);
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            int hangercount = 0;
            MultiInvStackHandler hanger = inventories.getInventory(enums.SLOTTYPE.PLANE);
            if (hanger != null) {
                hangercount = Math.min(hanger.getSlots(), 6);
            }

            int mainguncount = ((ItemRiggingBase) this.riggingStack.getItem()).getMainGunSlotCount();
            int subgunCount = mainguncount + ((ItemRiggingBase) this.riggingStack.getItem()).getSubGunSlotCount();
            int AACount = subgunCount + mainguncount + ((ItemRiggingBase) this.riggingStack.getItem()).getAASlotCount();
            int torpedocount = AACount + ((ItemRiggingBase) this.riggingStack.getItem()).getTorpedoSlotCount();

            int maxEquipmenttSlots = ((ItemRiggingBase) this.riggingStack.getItem()).getMainGunSlotCount() + ((ItemRiggingBase) this.riggingStack.getItem()).getSubGunSlotCount() + ((ItemRiggingBase) this.riggingStack.getItem()).getAASlotCount() + hangercount;
            int playerMainInv = maxEquipmenttSlots + 27;
            int PlayerHotbar = playerMainInv + 9;
            if (index < ((ItemRiggingBase) this.riggingStack.getItem()).getMainGunSlotCount() + ((ItemRiggingBase) this.riggingStack.getItem()).getSubGunSlotCount() + ((ItemRiggingBase) this.riggingStack.getItem()).getAASlotCount()) {
                if (!this.moveItemStackTo(itemstack1, maxEquipmenttSlots, PlayerHotbar, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (itemstack1.getItem() instanceof ItemEquipmentBase) {
                    if (itemstack1.getItem() instanceof ItemEquipmentGun) {
                        ItemEquipmentGun gun = (ItemEquipmentGun) itemstack1.getItem();
                        if (gun.getSize() == enums.CanonSize.LARGE) {
                            if (!this.moveItemStackTo(itemstack1, 0, mainguncount, false)) {
                                return ItemStack.EMPTY;
                            }
                        } else {
                            if (!this.moveItemStackTo(itemstack1, 0, subgunCount, false)) {
                                return ItemStack.EMPTY;
                            }
                        }
                    }
                    /*
                    else if (itemstack1.getItem() instanceof ItemEquipmentAA) {
                        if (!this.mergeItemStack(itemstack1, AACount+1, torpedocount, false)) {
                            return ItemStack.EMPTY;
                        }
                    }

                     */
                    else if (itemstack1.getItem() instanceof ItemEquipmentTorpedo) {
                        if (!this.moveItemStackTo(itemstack1, AACount , torpedocount, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
                if (index < playerMainInv) {
                    if (!this.moveItemStackTo(itemstack1, playerMainInv, PlayerHotbar, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.moveItemStackTo(itemstack1, maxEquipmenttSlots, playerMainInv, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }
        }
        return itemstack;
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return true;
    }

    private void addSlots(IItemHandler inventory, Function<Integer, Integer> x, Function<Integer, Integer> y, enums.SLOTTYPE slotType) {
        if (inventory.getSlots() > 0) {
            for (int i = 0; i < inventory.getSlots(); i++) {
                this.addSlot(new slotEquipment(inventory, i, x.apply(i), y.apply(i), slotType));
            }
        }
    }

    public static class Provider implements MenuProvider {
        private final ItemStack stack;
        private final UUID ownerID;

        public Provider(ItemStack stack, UUID ownerID) {
            this.stack = stack;
            this.ownerID = ownerID;
        }

        @Nonnull
        @Override
        public Component getDisplayName() {
            return new TranslatableComponent("gui.rigginginventory");
        }

        @Override
        public AbstractContainerMenu createMenu(int windowId, @Nonnull Inventory inventory, @Nonnull Player player) {
            return new RiggingContainer(windowId, inventory, this.stack, this.ownerID);
        }
    }
}
