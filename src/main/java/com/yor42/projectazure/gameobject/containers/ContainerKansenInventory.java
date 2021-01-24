package com.yor42.projectazure.gameobject.containers;

import com.yor42.projectazure.gameobject.entity.EntityKansenBase;
import com.yor42.projectazure.setup.register.registerContainer;
import com.yor42.projectazure.setup.register.registerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ContainerKansenInventory extends Container implements INamedContainerProvider {

    private static final EquipmentSlotType[] EQUIPMENT = new EquipmentSlotType[]{EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET};

    //client
    public ContainerKansenInventory(int id, PlayerInventory playerInventory){
        super(registerManager.SHIP_CONTAINER.get(), id);

        ItemStackHandler dummyStackHandler = new ItemStackHandler(19);

        //rigging
        this.addSlot(new SlotItemHandler(dummyStackHandler, 0,152,35));

        //Offhands
        this.addSlot(new SlotItemHandler(dummyStackHandler, 1,134,89));
        //mainhand
        this.addSlot(new SlotItemHandler(dummyStackHandler, 2,152,89));

        //armor(head/chest/legging/boots)
        for(int l = 0; l<4; l++){
            int finalL = l;
            this.addSlot(new SlotItemHandler(dummyStackHandler, 3+ finalL,75,35+ finalL * 18){
                public int getSlotStackLimit()
                {
                    return 1;
                }
            });
        }

        for(int m=0;m<4;m++){
            for(int n=0; n<3;n++){
                this.addSlot(new SlotItemHandler(dummyStackHandler, 7 + n + 3*m,11 + n * 18, 19 + m * 18));
            }
        }

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 110 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 168));
        }
    }

    //constructor for actual use
    public ContainerKansenInventory(int id, PlayerInventory playerInventory, EntityKansenBase entity) {
        super(registerManager.SHIP_CONTAINER.get(), id);

        ItemStackHandler inventory = entity.ShipStorage;

        //rigging
        this.addSlot(new SlotItemHandler(inventory, 0,152,35));

        //Offhand
        this.addSlot(new SlotItemHandler(inventory, 1,134,89));
        //mainhand
        this.addSlot(new SlotItemHandler(inventory, 2,152,89));

        //armor(head/chest/legging/boots)
        for(int l = 0; l<4; l++){
            int finalL = l;
            this.addSlot(new SlotItemHandler(inventory, 3+ finalL,75,35+ finalL * 18){
                public int getSlotStackLimit()
                {
                    return 1;
                }

                @Override
                public boolean isItemValid(@Nonnull ItemStack stack) {
                    return stack.getItem() instanceof ArmorItem && stack.canEquip(EQUIPMENT[finalL], entity);
                }
            });
        }

        for(int m=0;m<4;m++){
            for(int n=0; n<3;n++){
                this.addSlot(new SlotItemHandler(inventory, 7 + n + 3*m,11 + n * 18, 19 + m * 18));
            }
        }




        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 110 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 168));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("gui.shipinventory");
    }

    @Nullable
    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        return this;
    }
}
