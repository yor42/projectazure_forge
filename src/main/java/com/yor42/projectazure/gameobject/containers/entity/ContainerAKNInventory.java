package com.yor42.projectazure.gameobject.containers.entity;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.companioninterface.IArknightOperator;
import com.yor42.projectazure.gameobject.entity.companion.gunusers.EntityGunUserBase;
import com.yor42.projectazure.gameobject.items.ItemCannonshell;
import com.yor42.projectazure.gameobject.items.ItemMagazine;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.setup.register.registerManager;
import net.minecraft.client.gui.screen.inventory.FurnaceScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
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

public class ContainerAKNInventory extends Container {

    //C l i e n t
    public ContainerAKNInventory(int id, PlayerInventory inventory) {
        this(id, inventory, new ItemStackHandler(12), new ItemStackHandler(6), new ItemStackHandler(8));
    }

    public ContainerAKNInventory(int id, PlayerInventory inventory, IItemHandler entityInventory, IItemHandler EntityEquipment, IItemHandler EntityAmmo) {
        super(registerManager.AKN_CONTAINER.get(), id);
        final AbstractEntityCompanion entity = Main.PROXY.getSharedMob();
        //mainhand
        this.addSlot(new SlotItemHandler(EntityEquipment, 0, 75, 80));

        //Offhands
        this.addSlot(new SlotItemHandler(EntityEquipment, 1, 93, 80));

        for (int l = 0; l < 4; l++) {
            int finalL = l;
            this.addSlot(new SlotItemHandler(EntityEquipment, 2 + finalL, 93, 4 + finalL * 18) {
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
                this.addSlot(new SlotItemHandler(entityInventory, n + 3 * m, 116 + n * 18, 20 + m * 18));
            }
        }

        //TODO Change this check to Class check
        if(entity instanceof EntityGunUserBase) {
            for (int m = 0; m < 4; m++) {
                for (int n = 0; n < 2; n++) {
                    this.addSlot(new SlotItemHandler(EntityAmmo, n + 2 * m, 180 + n * 18, 13 + m * 18) {
                        @Override
                        public boolean isItemValid(@Nonnull ItemStack stack) {
                            return stack.getItem() instanceof ItemMagazine;
                        }
                    });
                }
            }
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 5 + j * 18, 107 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 5 + k * 18, 164));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        Main.PROXY.setSharedMob(null);
        super.onContainerClosed(playerIn);
    }

    public static class Supplier implements INamedContainerProvider {

        AbstractEntityCompanion companion;

        public Supplier(AbstractEntityCompanion companion) {
            this.companion = companion;
        }

        @Override
        public ITextComponent getDisplayName() {
            return new TranslationTextComponent("gui.companioninventory");
        }

        @Nullable
        @Override
        public Container createMenu(int openContainerId, PlayerInventory inventory, PlayerEntity player) {
            return new ContainerAKNInventory(openContainerId, inventory, this.companion.getInventory(), this.companion.getEquipment(), this.companion.getAmmoStorage());
        }
    }

}
