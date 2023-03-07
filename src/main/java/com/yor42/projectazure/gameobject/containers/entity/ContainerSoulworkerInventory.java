package com.yor42.projectazure.gameobject.containers.entity;

import com.yor42.projectazure.gameobject.containers.slots.AmmoSlot;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.setup.register.RegisterContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ContainerSoulworkerInventory extends AbstractContainerInventory {

    public ContainerSoulworkerInventory(int id, PlayerInventory inventory, PacketBuffer data) {
        this(id, inventory, new ItemStackHandler(16), new ItemStackHandler(6), new ItemStackHandler(8), (AbstractEntityCompanion) inventory.player.level.getEntity(data.readInt()));
    }

    public ContainerSoulworkerInventory(int id, PlayerInventory inventory, IItemHandler entityInventory, IItemHandler EntityEquipment, IItemHandler EntityAmmo, AbstractEntityCompanion companion) {
        super(RegisterContainer.CLS_CONTAINER.get(), id, companion);
        //ZA HANDO
        for (int k = 0; k < 2; k++) {
            this.addSlot(new SlotItemHandler(EntityEquipment, k, 125 + (20 * k), 105));
        }

        for (int l = 0; l < 4; l++) {
            this.addSlot(new SlotItemHandler(EntityEquipment, 2 + l, 102, 29 + l * 19) {
                public int getMaxStackSize() {
                    return 1;
                }

                @Override
                public boolean mayPlace(@Nonnull ItemStack stack) {
                    return stack.getItem() instanceof ArmorItem;
                }
            });
        }

        for (int m = 0; m < 4; m++) {
            for (int n = 0; n < 3; n++) {
                this.addSlot(new SlotItemHandler(entityInventory, n + 3 * m, 125 + n * 19, 29 + m * 19));
            }
        }


        for (int m = 0; m < 4; m++) {
            for (int n = 0; n < 2; n++) {
                this.addSlot(new AmmoSlot(EntityAmmo, n + (2 * m), 186 + n * 19, 29 + m * 19));
            }
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 35 + j * 18, 161 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 35 + k * 18, 219));
        }
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
            return new ContainerSoulworkerInventory(openContainerId, inventory, this.companion.getInventory(), this.companion.getEquipment(), this.companion.getAmmoStorage(), this.companion);
        }
    }

}
