package com.yor42.projectazure.gameobject.containers.riggingcontainer;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.capability.RiggingInventoryCapability;
import com.yor42.projectazure.gameobject.containers.slots.slotEquipment;
import com.yor42.projectazure.gameobject.containers.slots.slotInventory;
import com.yor42.projectazure.gameobject.items.equipment.ItemEquipmentBase;
import com.yor42.projectazure.gameobject.items.equipment.ItemEquipmentGun;
import com.yor42.projectazure.gameobject.items.equipment.ItemEquipmentTorpedo;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
import com.yor42.projectazure.libs.enums;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import static com.yor42.projectazure.setup.register.registerManager.RIGGING_INVENTORY;

public class RiggingContainer extends Container {

    private ItemStack riggingStack;

    public RiggingContainer(int id, PlayerInventory playerinv) {
        super(RIGGING_INVENTORY.get(), id);
        ItemStack riggingStack = Main.PROXY.getSharedStack();
        ItemStackHandler dummystack = new ItemStackHandler(30);
        ItemStackHandler dummyHanger = new ItemStackHandler(45);

        if(riggingStack.getItem() instanceof ItemRiggingBase){


            if (((ItemRiggingBase) riggingStack.getItem()).getMainGunSlotCount() > 0) {
                for (int i = 0; i < ((ItemRiggingBase) riggingStack.getItem()).getMainGunSlotCount(); i++) {
                    this.addSlot(new slotEquipment(dummystack, i, 7, 34 + 18 * i, enums.SLOTTYPE.MAIN_GUN));
                }
            }

            if (((ItemRiggingBase) riggingStack.getItem()).getSubGunSlotCount() > 0) {
                for (int columbs = 0; columbs < (((ItemRiggingBase) riggingStack.getItem()).getSubGunSlotCount()); columbs++) {
                    this.addSlot(new slotEquipment(dummystack, columbs + ((ItemRiggingBase) riggingStack.getItem()).getMainGunSlotCount(), 31 + 18 * columbs, 34, enums.SLOTTYPE.SUB_GUN));
                }
            }

            for(int j = 0; j<((ItemRiggingBase) riggingStack.getItem()).getAASlotCount(); j++){
                this.addSlot(new slotEquipment(dummystack, j + ((ItemRiggingBase) riggingStack.getItem()).getSubGunSlotCount() +((ItemRiggingBase) riggingStack.getItem()).getMainGunSlotCount(), 151, 34+18*j, enums.SLOTTYPE.AA));
            }
            for(int k = 0; k<((ItemRiggingBase) riggingStack.getItem()).getTorpedoSlotCount(); k++){
                this.addSlot(new slotEquipment(dummystack, k+ ((ItemRiggingBase) riggingStack.getItem()).getSubGunSlotCount() +((ItemRiggingBase) riggingStack.getItem()).getMainGunSlotCount()+((ItemRiggingBase) riggingStack.getItem()).getAASlotCount(), 30 + k * 18, 77, enums.SLOTTYPE.TORPEDO));
            }

            int hangercount = Math.min(((ItemRiggingBase) riggingStack.getItem()).getHangerSlots(), 3);
            for(int k = 0; k<hangercount; k++){
                this.addSlot(new slotEquipment(dummyHanger, k, 30 + k * 18, 30, enums.SLOTTYPE.PLANE));
            }

            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 9; ++j) {
                    this.addSlot(new slotInventory(playerinv, j + i * 9 + 9, 8 + j * 18, 110 + i * 18));
                }
            }

            for (int k = 0; k < 9; ++k) {
                this.addSlot(new slotInventory(playerinv, k, 8 + k * 18, 168));
            }
        }
    }

    public RiggingContainer(int id, PlayerInventory playerinv, RiggingInventoryCapability supplier) {
        super(RIGGING_INVENTORY.get(), id);
        this.riggingStack = supplier.getRigging();
        ItemStackHandler equipmentstack = supplier.getEquipments();
        ItemStackHandler hanger = ((ItemRiggingBase) riggingStack.getItem()).getHangers(riggingStack);

        if(this.riggingStack.getItem() instanceof ItemRiggingBase) {

            if(((ItemRiggingBase) this.riggingStack.getItem()).getTorpedoSlotCount()<0|| ((ItemRiggingBase) riggingStack.getItem()).getAASlotCount()<0|| ((ItemRiggingBase) riggingStack.getItem()).getMainGunSlotCount()<0){
                throw new IllegalArgumentException("All Rigging Slot count must NOT be negative! or it will cause infinite loop!");
            }

            if (((ItemRiggingBase) this.riggingStack.getItem()).getMainGunSlotCount() > 0) {

                for (int i = 0; i < ((ItemRiggingBase) this.riggingStack.getItem()).getMainGunSlotCount(); i++) {
                    this.addSlot(new slotEquipment(equipmentstack, i, 7, 34 + 18 * i, enums.SLOTTYPE.MAIN_GUN));
                }
            }

            if (((ItemRiggingBase) this.riggingStack.getItem()).getSubGunSlotCount() > 0) {
                for (int columbs = 0; columbs < (((ItemRiggingBase) riggingStack.getItem()).getSubGunSlotCount()); columbs++) {
                    this.addSlot(new slotEquipment(equipmentstack, columbs + ((ItemRiggingBase) this.riggingStack.getItem()).getMainGunSlotCount(), 31 + 18 * columbs, 34, enums.SLOTTYPE.SUB_GUN));
                }
            }

            if (((ItemRiggingBase) this.riggingStack.getItem()).getAASlotCount() > 0) {
                for (int j = 0; j < ((ItemRiggingBase) this.riggingStack.getItem()).getAASlotCount(); j++) {
                    this.addSlot(new slotEquipment(equipmentstack, j + ((ItemRiggingBase) this.riggingStack.getItem()).getMainGunSlotCount() + ((ItemRiggingBase) this.riggingStack.getItem()).getSubGunSlotCount(), 151, 34+18*j, enums.SLOTTYPE.AA));
                }
            }
            if (((ItemRiggingBase) this.riggingStack.getItem()).getTorpedoSlotCount() > 0){
                for (int k = 0; k < ((ItemRiggingBase) this.riggingStack.getItem()).getTorpedoSlotCount(); k++) {
                    this.addSlot(new slotEquipment(equipmentstack, k + ((ItemRiggingBase) this.riggingStack.getItem()).getMainGunSlotCount() + ((ItemRiggingBase) this.riggingStack.getItem()).getSubGunSlotCount() + ((ItemRiggingBase) this.riggingStack.getItem()).getAASlotCount(), 30 + k * 18, 77, enums.SLOTTYPE.TORPEDO));
                }
            }
            if(hanger != null) {
                int hangercount = Math.min(hanger.getSlots(), 3);
                for (int k = 0; k < hangercount; k++) {
                    this.addSlot(new slotEquipment(hanger, k, 30 + k * 18, 30, enums.SLOTTYPE.PLANE));
                }
            }

            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 9; ++j) {
                    this.addSlot(new slotInventory(playerinv, j + i * 9 + 9, 8 + j * 18, 110 + i * 18));
                }
            }

            for (int k = 0; k < 9; ++k) {
                this.addSlot(new slotInventory(playerinv, k, 8 + k * 18, 168));
            }
        }
    }
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            int hangercount = 0;
            ItemStackHandler hanger = ((ItemRiggingBase) riggingStack.getItem()).getHangers(riggingStack);
            if(hanger != null){
                hangercount =  Math.min(hanger.getSlots(), 3);
            }

            int mainguncount = ((ItemRiggingBase) this.riggingStack.getItem()).getMainGunSlotCount()-1;
            int subgunCount = mainguncount+ ((ItemRiggingBase) this.riggingStack.getItem()).getSubGunSlotCount();
            int AACount = subgunCount+mainguncount+ ((ItemRiggingBase) this.riggingStack.getItem()).getAASlotCount();
            int torpedocount = AACount+ ((ItemRiggingBase) this.riggingStack.getItem()).getTorpedoSlotCount();

            int maxEquipmenttSlots = ((ItemRiggingBase) this.riggingStack.getItem()).getMainGunSlotCount() + ((ItemRiggingBase) this.riggingStack.getItem()).getSubGunSlotCount() + ((ItemRiggingBase) this.riggingStack.getItem()).getAASlotCount()+hangercount;
            int playerMainInv = maxEquipmenttSlots + 27;
            int PlayerHotbar = playerMainInv + 9;
            if(index<((ItemRiggingBase) this.riggingStack.getItem()).getMainGunSlotCount() + ((ItemRiggingBase) this.riggingStack.getItem()).getSubGunSlotCount() + ((ItemRiggingBase) this.riggingStack.getItem()).getAASlotCount()){
                if (!this.mergeItemStack(itemstack1, maxEquipmenttSlots, PlayerHotbar, true)) {
                    return ItemStack.EMPTY;
                }
            }
            else {
                if (itemstack1.getItem() instanceof ItemEquipmentBase) {
                    if (itemstack1.getItem() instanceof ItemEquipmentGun) {
                        ItemEquipmentGun gun = (ItemEquipmentGun) itemstack1.getItem();
                        if(gun.getSize() == enums.CanonSize.LARGE) {
                            if (!this.mergeItemStack(itemstack1, 0, mainguncount, false)) {
                                return ItemStack.EMPTY;
                            }
                        }
                        else{
                            if (!this.mergeItemStack(itemstack1, 0, subgunCount, false)) {
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
                        if (!this.mergeItemStack(itemstack1, AACount+1, torpedocount, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                    if(index<playerMainInv) {
                        if (!this.mergeItemStack(itemstack1, playerMainInv + 1, PlayerHotbar, false)) {
                            return ItemStack.EMPTY;
                        }
                    }else{
                        if (!this.mergeItemStack(itemstack1, maxEquipmenttSlots+1, playerMainInv, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
            }
        }
        return itemstack;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }
}
