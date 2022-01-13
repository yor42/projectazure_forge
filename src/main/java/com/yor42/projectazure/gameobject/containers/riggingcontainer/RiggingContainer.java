package com.yor42.projectazure.gameobject.containers.riggingcontainer;

import com.yor42.projectazure.gameobject.capability.multiinv.IMultiInventory;
import com.yor42.projectazure.gameobject.capability.multiinv.MultiInvStackHandler;
import com.yor42.projectazure.gameobject.capability.multiinv.MultiInvUtil;
import com.yor42.projectazure.gameobject.containers.slots.slotEquipment;
import com.yor42.projectazure.gameobject.containers.slots.slotInventory;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemEquipmentBase;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemEquipmentGun;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemEquipmentTorpedo;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
import com.yor42.projectazure.libs.enums;
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

import javax.annotation.Nonnull;
import java.util.function.Function;

import static com.yor42.projectazure.setup.register.registerManager.RIGGING_INVENTORY;

public class RiggingContainer extends Container {

    public ItemStack riggingStack;

    public RiggingContainer(int id, PlayerInventory playerInv, PacketBuffer data) {
        super(RIGGING_INVENTORY.get(), id);
        this.riggingStack = data.readItem();

        IMultiInventory inventories = MultiInvUtil.getCap(this.riggingStack);

        this.addSlots(inventories.getInventory(enums.SLOTTYPE.MAIN_GUN.ordinal()), i -> 7, i -> 34 + 18 * i, enums.SLOTTYPE.MAIN_GUN);
        this.addSlots(inventories.getInventory(enums.SLOTTYPE.SUB_GUN.ordinal()), i -> 31 + 18 * i, i -> 34, enums.SLOTTYPE.SUB_GUN);
        this.addSlots(inventories.getInventory(enums.SLOTTYPE.AA.ordinal()), i -> 151, i -> 34 + 18 * i, enums.SLOTTYPE.AA);
        this.addSlots(inventories.getInventory(enums.SLOTTYPE.TORPEDO.ordinal()), i -> 30 + 18 * i, i -> 77, enums.SLOTTYPE.TORPEDO);
        this.addSlots(inventories.getInventory(enums.SLOTTYPE.PLANE.ordinal()), i -> 30 + 18 * i, i -> 30, enums.SLOTTYPE.PLANE);

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new slotInventory(playerInv, j + i * 9 + 9, 8 + j * 18, 110 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new slotInventory(playerInv, k, 8 + k * 18, 168));
        }
    }

    public RiggingContainer(int id, PlayerInventory playerInv, ItemStack riggingStack) {
        super(RIGGING_INVENTORY.get(), id);
        this.riggingStack = riggingStack;

        IMultiInventory inventories = MultiInvUtil.getCap(this.riggingStack);

        this.addSlots(inventories.getInventory(enums.SLOTTYPE.MAIN_GUN.ordinal()), i -> 7, i -> 34 + 18 * i, enums.SLOTTYPE.MAIN_GUN);
        this.addSlots(inventories.getInventory(enums.SLOTTYPE.SUB_GUN.ordinal()), i -> 31 + 18 * i, i -> 34, enums.SLOTTYPE.SUB_GUN);
        this.addSlots(inventories.getInventory(enums.SLOTTYPE.AA.ordinal()), i -> 151, i -> 34 + 18 * i, enums.SLOTTYPE.AA);
        this.addSlots(inventories.getInventory(enums.SLOTTYPE.TORPEDO.ordinal()), i -> 30 + 18 * i, i -> 77, enums.SLOTTYPE.TORPEDO);
        this.addSlots(inventories.getInventory(enums.SLOTTYPE.PLANE.ordinal()), i -> 30 + 18 * i, i -> 30, enums.SLOTTYPE.PLANE);

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new slotInventory(playerInv, j + i * 9 + 9, 8 + j * 18, 110 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new slotInventory(playerInv, k, 8 + k * 18, 168));
        }
    }

    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        IMultiInventory inventories = MultiInvUtil.getCap(this.riggingStack);
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            int hangercount = 0;
            MultiInvStackHandler hanger = inventories.getInventory(enums.SLOTTYPE.PLANE.ordinal());
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
    public boolean stillValid(PlayerEntity playerIn) {
        return true;
    }

    private void addSlots(IItemHandler inventory, Function<Integer, Integer> x, Function<Integer, Integer> y, enums.SLOTTYPE slotType) {
        if (inventory.getSlots() > 0) {
            for (int i = 0; i < inventory.getSlots(); i++) {
                this.addSlot(new slotEquipment(inventory, i, x.apply(i), y.apply(i), slotType));
            }
        }
    }

    public static class Provider implements INamedContainerProvider {
        private final ItemStack stack;

        public Provider(ItemStack stack) {
            this.stack = stack;
        }

        @Nonnull
        @Override
        public ITextComponent getDisplayName() {
            return new TranslationTextComponent("gui.rigginginventory");
        }

        @Override
        public Container createMenu(int windowId, @Nonnull PlayerInventory inventory, @Nonnull PlayerEntity player) {
            return new RiggingContainer(windowId, inventory, this.stack);
        }
    }
}
