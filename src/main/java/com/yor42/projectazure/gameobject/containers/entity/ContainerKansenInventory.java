package com.yor42.projectazure.gameobject.containers.entity;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.containers.slots.SlotRigging;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenBase;
import com.yor42.projectazure.gameobject.items.ItemCannonshell;
import com.yor42.projectazure.gameobject.items.ItemMagazine;
import com.yor42.projectazure.setup.register.registerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ContainerKansenInventory extends Container {

    private static final EquipmentSlotType[] EQUIPMENT = new EquipmentSlotType[]{EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET};
    private final IItemHandler AmmoStack;
    private final IItemHandler equipment;
    @Nullable
    private final EntityKansenBase entity;

    //client
    public ContainerKansenInventory(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, new ItemStackHandler(12),  new ItemStackHandler(1), new ItemStackHandler(6), new ItemStackHandler(8), null);
    }

    //constructor for actual use
    public ContainerKansenInventory(int id, PlayerInventory playerInventory, IItemHandler Inventory, IItemHandler Rigging, IItemHandler Equipments, IItemHandler AmmoStorage, @Nullable EntityKansenBase entity) {
        super(registerManager.SHIP_CONTAINER.get(), id);
        this.equipment = Equipments;
        this.AmmoStack = AmmoStorage;
        this.entity = entity;
//rigging
        this.addSlot(new SlotRigging(Rigging, 0, 152, 35, this.entity));

        //mainhand
        this.addSlot(new SlotItemHandler(this.equipment, 0, 152, 89));
        //Offhand
        this.addSlot(new SlotItemHandler(this.equipment, 1, 134, 89));

        //armor(head/chest/legging/boots)
        for (int l = 0; l < 4; l++) {
            int finalL = l;
            this.addSlot(new SlotItemHandler(this.equipment, 2 + finalL, 75, 35 + finalL * 18) {
                public int getMaxStackSize() {
                    return 1;
                }

                @Override
                public boolean mayPlace(@Nonnull ItemStack stack) {
                    return stack.getItem() instanceof ArmorItem && stack.canEquip(EQUIPMENT[finalL],  ContainerKansenInventory.this.entity);
                }
            });
        }

        for (int m = 0; m < 4; m++) {
            for (int n = 0; n < 3; n++) {
                this.addSlot(new SlotItemHandler(Inventory, n + 3 * m, 11 + n * 18, 19 + m * 18));
            }
        }

        for (int m = 0; m < 4; m++) {
            for (int n = 0; n < 2; n++) {
                this.addSlot(new SlotItemHandler(this.AmmoStack, n + 2 * m, 180 + n * 18, 15 + m * 18){
                    @Override
                    public boolean mayPlace(@Nonnull ItemStack stack) {
                        return stack.getItem() instanceof ItemCannonshell;
                    }
                });
            }
        }


        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 110 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 168));
        }
    }

    @Override
    public void removed(PlayerEntity playerIn) {
        Main.PROXY.setSharedMob(null);
        super.removed(playerIn);
    }

    @Override
    public boolean stillValid(PlayerEntity playerIn) {
        return true;
    }

    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
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

    public static class Supplier implements INamedContainerProvider {
        EntityKansenBase kansenEntity;

        public Supplier(EntityKansenBase kansenEntity) {
            this.kansenEntity = kansenEntity;
        }

        @Override
        public ITextComponent getDisplayName() {
            return new TranslationTextComponent("gui.shipinventory");
        }

        @Nullable
        @Override
        public Container createMenu(int openContainerId, PlayerInventory inventory, PlayerEntity player) {
            return new ContainerKansenInventory(openContainerId, inventory, this.kansenEntity.getInventory(), this.kansenEntity.getShipRiggingStorage(), this.kansenEntity.getEquipment(), this.kansenEntity.getAmmoStorage(), this.kansenEntity);
        }
    }
}
