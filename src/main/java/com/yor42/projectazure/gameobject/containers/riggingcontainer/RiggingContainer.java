package com.yor42.projectazure.gameobject.containers.riggingcontainer;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.containers.slots.slotEquipment;
import com.yor42.projectazure.gameobject.containers.slots.slotInventory;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
import com.yor42.projectazure.libs.enums;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

import static com.yor42.projectazure.setup.register.registerManager.RIGGING_INVENTORY;

public class RiggingContainer extends Container {

    private ItemStack riggingStack;

    public RiggingContainer(int id, PlayerInventory playerinv) {
        super(RIGGING_INVENTORY.get(), id);
        ItemStack riggingStack = Main.PROXY.getSharedStack();
        int slotcount = 1;
        if(riggingStack.getItem() instanceof ItemRiggingBase){
            slotcount = ((ItemRiggingBase) riggingStack.getItem()).getTotalSlotCount();
        }
        ItemStackHandler dummystack = new ItemStackHandler(30);

        if(riggingStack.getItem() instanceof ItemRiggingBase){
            for(int i = 0; i<((ItemRiggingBase) riggingStack.getItem()).getGunSlotCount(); i++){
                this.addSlot(new slotEquipment(dummystack, i, 7, 34+18*i, enums.SLOTTYPE.GUN));
            }
            for(int j = 0; j<((ItemRiggingBase) riggingStack.getItem()).getAASlotCount(); j++){
                this.addSlot(new slotEquipment(dummystack, j+((ItemRiggingBase) riggingStack.getItem()).getGunSlotCount(), 151, 34+18*j, enums.SLOTTYPE.AA));
            }
            for(int k = 0; k<((ItemRiggingBase) riggingStack.getItem()).getTorpedoSlotCount(); k++){
                this.addSlot(new slotEquipment(dummystack, k+((ItemRiggingBase) riggingStack.getItem()).getGunSlotCount()+((ItemRiggingBase) riggingStack.getItem()).getAASlotCount(), 30 + k * 18, 77, enums.SLOTTYPE.TORPEDO));
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

    public RiggingContainer(int id, PlayerInventory playerinv, IRiggingContainerSupplier supplier) {
        super(RIGGING_INVENTORY.get(), id);
        this.riggingStack = supplier.getRigging();
        ItemStackHandler equipmentstack = supplier.getEquipments();

        if(this.riggingStack.getItem() instanceof ItemRiggingBase) {

            if(((ItemRiggingBase) this.riggingStack.getItem()).getTorpedoSlotCount()<0|| ((ItemRiggingBase) riggingStack.getItem()).getAASlotCount()<0|| ((ItemRiggingBase) riggingStack.getItem()).getGunSlotCount()<0){
                throw new IllegalArgumentException("All Rigging Slot count must NOT be negative! or it will cause infinite loop!");
            }

            if (((ItemRiggingBase) this.riggingStack.getItem()).getGunSlotCount() > 0) {
                for (int i = 0; i < ((ItemRiggingBase) this.riggingStack.getItem()).getGunSlotCount(); i++) {
                    this.addSlot(new slotEquipment(equipmentstack, i, 7, 34+18*i, enums.SLOTTYPE.GUN));
                }
            }
            if (((ItemRiggingBase) this.riggingStack.getItem()).getAASlotCount() > 0) {
                for (int j = 0; j < ((ItemRiggingBase) this.riggingStack.getItem()).getAASlotCount(); j++) {
                    this.addSlot(new slotEquipment(equipmentstack, j + ((ItemRiggingBase) this.riggingStack.getItem()).getGunSlotCount(), 151, 34+18*j, enums.SLOTTYPE.AA));
                }
            }
            if (((ItemRiggingBase) this.riggingStack.getItem()).getTorpedoSlotCount() > 0){
                for (int k = 0; k < ((ItemRiggingBase) this.riggingStack.getItem()).getTorpedoSlotCount(); k++) {
                    this.addSlot(new slotEquipment(equipmentstack, k + ((ItemRiggingBase) this.riggingStack.getItem()).getGunSlotCount() + ((ItemRiggingBase) this.riggingStack.getItem()).getAASlotCount(), 30 + k * 18, 77, enums.SLOTTYPE.TORPEDO));
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

    public ItemStack getRiggingStack() {
        return this.riggingStack;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }
}
