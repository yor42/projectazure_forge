package com.yor42.projectazure.gameobject.containers;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.containers.slots.SlotRigging;
import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityKansenBase;
import com.yor42.projectazure.gameobject.items.ItemAmmo;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
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
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ContainerKansenInventory extends Container {

    private static final EquipmentSlotType[] EQUIPMENT = new EquipmentSlotType[]{EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET};
    private EntityKansenBase host;
    private ItemStackHandler Stack, AmmoStack;
    private IItemHandlerModifiable equipment;

    //client
    public ContainerKansenInventory(int id, PlayerInventory playerInventory) {
        super(registerManager.SHIP_CONTAINER.get(), id);

        ItemStackHandler dummyStackHandler = new ItemStackHandler(1);
        ItemStackHandler dummyInventory = new ItemStackHandler(12);
        ItemStackHandler dummyEquipmentHandler = new ItemStackHandler(19);
        ItemStackHandler dummyAmmoHandler = new ItemStackHandler(8);

        //rigging
        this.addSlot(new SlotRigging(dummyStackHandler, 0, 152, 35, (EntityKansenBase) Main.PROXY.getSharedMob()));

        //mainhand
        this.addSlot(new SlotItemHandler(dummyEquipmentHandler, 0, 152, 89));

        //Offhands
        this.addSlot(new SlotItemHandler(dummyEquipmentHandler, 1, 134, 89));

        //armor(head/chest/legging/boots)
        for (int l = 0; l < 4; l++) {
            int finalL = l;
            this.addSlot(new SlotItemHandler(dummyEquipmentHandler, 3 + finalL, 75, 35 + finalL * 18) {
                public int getSlotStackLimit() {
                    return 1;
                }

                @Override
                public boolean isItemValid(@Nonnull ItemStack stack) {
                    return stack.getItem() instanceof ArmorItem;
                }
            });
        }

        for (int m = 0; m < 4; m++) {
            for (int n = 0; n < 3; n++) {
                this.addSlot(new SlotItemHandler(dummyInventory, n + 3 * m, 11 + n * 18, 19 + m * 18));
            }
        }

        for (int m = 0; m < 4; m++) {
            for (int n = 0; n < 2; n++) {
                this.addSlot(new SlotItemHandler(dummyAmmoHandler, n + 2 * m, 180 + n * 18, 15 + m * 18){
                    @Override
                    public boolean isItemValid(@Nonnull ItemStack stack) {
                        return stack.getItem() instanceof ItemAmmo;
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

    //constructor for actual use
    public ContainerKansenInventory(int id, PlayerInventory playerInventory, EntityKansenBase entity) {
        super(registerManager.SHIP_CONTAINER.get(), id);
        this.host = entity;
        this.Stack = entity.getShipRiggingStorage();
        this.equipment = this.host.getEquipment();
        this.AmmoStack = this.host.getAmmoStorage();


        //rigging
        this.addSlot(new SlotRigging(this.Stack, 0, 152, 35, (EntityKansenBase) Main.PROXY.getSharedMob()));

        //mainhand
        this.addSlot(new SlotItemHandler(this.equipment, 0, 152, 89));
        //Offhand
        this.addSlot(new SlotItemHandler(this.equipment, 1, 134, 89));

        //armor(head/chest/legging/boots)
        for (int l = 0; l < 4; l++) {
            int finalL = l;
            this.addSlot(new SlotItemHandler(this.equipment, 2 + finalL, 75, 35 + finalL * 18) {
                public int getSlotStackLimit() {
                    return 1;
                }

                @Override
                public boolean isItemValid(@Nonnull ItemStack stack) {
                    return stack.getItem() instanceof ArmorItem && stack.canEquip(EQUIPMENT[finalL], entity);
                }
            });
        }

        for (int m = 0; m < 4; m++) {
            for (int n = 0; n < 3; n++) {
                this.addSlot(new SlotItemHandler(this.host.getInventory(), n + 3 * m, 11 + n * 18, 19 + m * 18));
            }
        }

        for (int m = 0; m < 4; m++) {
            for (int n = 0; n < 2; n++) {
                this.addSlot(new SlotItemHandler(this.AmmoStack, n + 2 * m, 180 + n * 18, 15 + m * 18){
                    @Override
                    public boolean isItemValid(@Nonnull ItemStack stack) {
                        return stack.getItem() instanceof ItemAmmo;
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
    public void onContainerClosed(PlayerEntity playerIn) {
        Main.PROXY.setSharedMob(null);
        super.onContainerClosed(playerIn);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if(slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();


            if(itemstack1.getItem() instanceof ItemRiggingBase){
                if(!this.mergeItemStack(itemstack1, 0, 1, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if(itemstack1.getItem() instanceof ArmorItem){
                if(itemstack1.canEquip(EquipmentSlotType.HEAD, this.host)){
                    if(!this.mergeItemStack(itemstack1, 3, 4, true))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if(itemstack1.canEquip(EquipmentSlotType.CHEST, this.host)){
                    if(!this.mergeItemStack(itemstack1, 4, 5, true))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if(itemstack1.canEquip(EquipmentSlotType.LEGS, this.host)){
                    if(!this.mergeItemStack(itemstack1, 5, 6, true))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if(itemstack1.canEquip(EquipmentSlotType.FEET, this.host)){
                    if(!this.mergeItemStack(itemstack1, 6, 7, true))
                    {
                        return ItemStack.EMPTY;
                    }
                }
            }
            else if(index<19)
            {
                if(!this.mergeItemStack(itemstack1, 19, 53, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else {
                if(!this.mergeItemStack(itemstack1, 7, 19, true))
                {
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
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
            return new ContainerKansenInventory(openContainerId, inventory, this.kansenEntity);
        }
    }
}
