package com.yor42.projectazure.gameobject.containers.machine;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import static com.yor42.projectazure.setup.register.registerManager.GROWTH_CHAMBER_CONTAINER_TYPE;

public class ContainerCrystalGrowthChamber extends Container {

    private final IIntArray field;
    private final FluidStack waterTank, SolutionTank;

    public ContainerCrystalGrowthChamber(int id, PlayerInventory inventory, PacketBuffer buffer){
        this(id, inventory, new ItemStackHandler(7), new IIntArray() {

            final int[] values = buffer.readVarIntArray();

            @Override
            public int get(int index) {
                return values[index];
            }

            @Override
            public void set(int index, int value) {
                values[index] = value;
            }

            @Override
            public int size() {
                return values.length;
            }
        }, buffer.readFluidStack(), buffer.readFluidStack());
    }

    public ContainerCrystalGrowthChamber(int id, PlayerInventory inventory, ItemStackHandler itemStackHandler, IIntArray field, FluidStack waterTank, FluidStack solutionTank) {
        super(GROWTH_CHAMBER_CONTAINER_TYPE, id);
        this.field = field;
        this.waterTank = waterTank;
        this.SolutionTank = solutionTank;

        this.addSlot(new SlotItemHandler(itemStackHandler, 0, 57,5));

        for(int i=0;i<3;i++){
            this.addSlot(new SlotItemHandler(itemStackHandler, i+1, 30,16+18*i));
        }

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        this.addSlot(new SlotItemHandler(itemStackHandler, 4, 11,5));
        this.addSlot(new SlotItemHandler(itemStackHandler, 5, 11,63));

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
        }

        this.trackIntArray(this.field);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }
}
