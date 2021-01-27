package com.yor42.projectazure.gameobject.containers;

import com.yor42.projectazure.gameobject.entity.EntityKansenBase;
import com.yor42.projectazure.gameobject.items.ItemRiggingBase;
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
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ContainerKansenInventory extends Container {

    private static final EquipmentSlotType[] EQUIPMENT = new EquipmentSlotType[]{EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET};
    private EntityKansenBase host;
    private ItemStackHandler Stack;

    //client
    public ContainerKansenInventory(int id, PlayerInventory playerInventory) {
        super(registerManager.SHIP_CONTAINER.get(), id);

        ItemStackHandler dummyStackHandler = new ItemStackHandler(19);

        //rigging
        this.addSlot(new SlotItemHandler(dummyStackHandler, 0, 152, 35){
            @Override
            public int getSlotStackLimit() {
                return 1;
            }

            @Override
            public boolean isItemValid(@Nonnull ItemStack stack) {
                return stack.getItem() instanceof ItemRiggingBase;
            }
        });

        //Offhands
        this.addSlot(new SlotItemHandler(dummyStackHandler, 1, 134, 89));
        //mainhand
        this.addSlot(new SlotItemHandler(dummyStackHandler, 2, 152, 89));

        //armor(head/chest/legging/boots)
        for (int l = 0; l < 4; l++) {
            int finalL = l;
            this.addSlot(new SlotItemHandler(dummyStackHandler, 3 + finalL, 75, 35 + finalL * 18) {
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
                this.addSlot(new SlotItemHandler(dummyStackHandler, 7 + n + 3 * m, 11 + n * 18, 19 + m * 18));
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
        this.Stack = entity.getShipStorage();

        //rigging
        this.addSlot(new SlotItemHandler(this.Stack, 0, 152, 35));

        //Offhand
        this.addSlot(new SlotItemHandler(this.Stack, 1, 134, 89));
        //mainhand
        this.addSlot(new SlotItemHandler(this.Stack, 2, 152, 89));

        //armor(head/chest/legging/boots)
        for (int l = 0; l < 4; l++) {
            int finalL = l;
            this.addSlot(new SlotItemHandler(this.Stack, 3 + finalL, 75, 35 + finalL * 18) {
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
                this.addSlot(new SlotItemHandler(this.Stack, 7 + n + 3 * m, 11 + n * 18, 19 + m * 18));
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
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if(slot instanceof Slot && slot.getHasStack())
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
