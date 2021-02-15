package com.yor42.projectazure.gameobject.containers.riggingcontainer;


import com.yor42.projectazure.gameobject.containers.slots.slotEquipment;
import com.yor42.projectazure.gameobject.containers.slots.slotInventory;
import com.yor42.projectazure.libs.enums;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraftforge.items.ItemStackHandler;

import static com.yor42.projectazure.setup.register.registerManager.DD_DEFAULT_RIGGING_INVENTORY;

public class RiggingContainerDDDefault extends Container {

    private ItemStackHandler equipments;

    public RiggingContainerDDDefault(int id, PlayerInventory playerinv) {
        super(DD_DEFAULT_RIGGING_INVENTORY.get(), id);

        ItemStackHandler clientDummyItemStack = new ItemStackHandler(6);
        this.addSlot(new slotEquipment(clientDummyItemStack, 0, 12, 42, enums.SLOTTYPE.GUN));
        this.addSlot(new slotEquipment(clientDummyItemStack, 1, 146, 42, enums.SLOTTYPE.GUN));

        this.addSlot(new slotEquipment(clientDummyItemStack, 2, 138, 72, enums.SLOTTYPE.AA));

        //why 3 torpedo why waisse :concern:
        for (int i = 0; i < 3; i++) {
            this.addSlot(new slotEquipment(clientDummyItemStack, 3 + i, 54 + i * 18, 72, enums.SLOTTYPE.TORPEDO));
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

    public RiggingContainerDDDefault(int id, PlayerInventory playerinv, IRiggingContainerSupplier containerSupplier) {
        super(DD_DEFAULT_RIGGING_INVENTORY.get(), id);
        this.equipments = containerSupplier.getEquipments();

        this.addSlot(new slotEquipment(this.equipments, 0, 14, 42, enums.SLOTTYPE.GUN));
        this.addSlot(new slotEquipment(this.equipments, 1, 146, 42, enums.SLOTTYPE.GUN));

        this.addSlot(new slotEquipment(this.equipments, 2, 138, 72, enums.SLOTTYPE.AA));

        //why 3 torpedo why waisse :concern:
        for (int i = 0; i < 3; i++) {
            this.addSlot(new slotEquipment(this.equipments, 3 + i, 54 + i * 18, 72, enums.SLOTTYPE.TORPEDO));
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

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }
}