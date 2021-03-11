package com.yor42.projectazure.gameobject.containers;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.entity.companion.gunusers.EntityGunUserBase;
import com.yor42.projectazure.gameobject.items.ItemAmmo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
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

import static com.yor42.projectazure.setup.register.registerManager.BA_CONTAINER;

public class ContainerBAInventory extends Container {

    private ItemStackHandler AmmoStack;
    private IItemHandlerModifiable equipment;

    private EntityGunUserBase host;

    public ContainerBAInventory(int ID, PlayerInventory inventory) {
        super(BA_CONTAINER.get(), ID);
        ItemStackHandler dummyStackHandler = new ItemStackHandler(12);
        ItemStackHandler dummyEquipmentHandler = new ItemStackHandler(19);
        ItemStackHandler dummyAmmoHandler = new ItemStackHandler(8);

        //mainhand
        this.addSlot(new SlotItemHandler(dummyEquipmentHandler, 0, 10, 12));

        //Offhands
        this.addSlot(new SlotItemHandler(dummyEquipmentHandler, 1, 10, 30));

        //armor(head/chest/legging/boots)
        for (int l = 0; l < 4; l++) {
            int finalL = l;
            this.addSlot(new SlotItemHandler(dummyEquipmentHandler, 3 + finalL, 85, 16 + finalL * 18) {
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
                this.addSlot(new SlotItemHandler(dummyStackHandler, n + 3 * m, 113 + n * 18, 27 + m * 18));
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
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 110 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 168));
        }
    }

    public ContainerBAInventory(int ID, PlayerInventory inventory, EntityGunUserBase companion) {
        super(BA_CONTAINER.get(), ID);
        ItemStackHandler stack = companion.getInventory();
        this.equipment = companion.getEquipment();
        this.AmmoStack = companion.getAmmoStorage();
        this.host = companion;

        //mainhand
        this.addSlot(new SlotItemHandler(this.equipment, 0, 10, 12));

        //Offhands
        this.addSlot(new SlotItemHandler(this.equipment, 1, 10, 30));

        //armor(head/chest/legging/boots)
        for (int l = 0; l < 4; l++) {
            int finalL = l;
            this.addSlot(new SlotItemHandler(this.equipment, 3 + finalL, 85, 16 + finalL * 18) {
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
                this.addSlot(new SlotItemHandler(stack, n + 3 * m, 113 + n * 18, 27 + m * 18));
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
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 110 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 168));
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

    public static class Supplier implements INamedContainerProvider {

        EntityGunUserBase companion;

        public Supplier(EntityGunUserBase companion) {
            this.companion = companion;
        }

        @Override
        public ITextComponent getDisplayName() {
            return new TranslationTextComponent("gui.companioninventory");
        }

        @Nullable
        @Override
        public Container createMenu(int openContainerId, PlayerInventory inventory, PlayerEntity player) {
            return new ContainerBAInventory(openContainerId, inventory, this.companion);
        }
    }
}
