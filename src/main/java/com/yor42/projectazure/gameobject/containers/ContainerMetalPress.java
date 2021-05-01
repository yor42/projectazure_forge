package com.yor42.projectazure.gameobject.containers;

import com.yor42.projectazure.data.ModTags;
import com.yor42.projectazure.gameobject.blocks.tileentity.TileEntityMetalPress;
import com.yor42.projectazure.gameobject.crafting.PressingRecipe;
import com.yor42.projectazure.setup.register.registerRecipes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.setup.register.registerManager.METAL_PRESS_CONTAINER_TYPE;

public class ContainerMetalPress extends Container {

    private final TileEntity TE;

    public ContainerMetalPress(int id, PlayerInventory inventory, PacketBuffer buffer) {
        this(id, inventory, new ItemStackHandler(3), buffer.readBlockPos());
    }

    public ContainerMetalPress(int id, PlayerInventory inventory, ItemStackHandler Inventory, BlockPos pos) {
        super(METAL_PRESS_CONTAINER_TYPE, id);
        IRecipeType<? extends PressingRecipe> recipeType = registerRecipes.Types.PRESSING;

        this.addSlot(new SlotItemHandler(Inventory, 0, 41, 35));
        this.addSlot(new SlotMold(Inventory, 1, 75,35));
        this.addSlot(new MachineResultSlot(inventory.player, Inventory, 2, 116, 35));

        this.TE = inventory.player.world.getTileEntity(pos);


        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
        }
    }



    public int getStoredPowerScaled(int pixels){

        if(this.TE instanceof TileEntityMetalPress){
            int currentpower = ((TileEntityMetalPress) this.TE).getEnergyStorage().getEnergyStored();
            int maxpower = ((TileEntityMetalPress) this.TE).getEnergyStorage().getMaxEnergyStored();

            return currentpower != 0 && maxpower != 0? currentpower*pixels/maxpower:0;
        }
        return 0;
    }

    public int getprogressScaled(int pixels){
        if(this.TE instanceof TileEntityMetalPress) {
            int i = ((TileEntityMetalPress) this.TE).getProcessTime();
            int j = ((TileEntityMetalPress) this.TE).getTotalProcessTime();
            double k = (double) i / j;
            return (int)(i != 0 ? k * pixels : 0);
        }
        return 0;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }
    //Why do I have to do this all over again forge :kekw:
    private static class MachineResultSlot extends SlotItemHandler{

        private final PlayerEntity player;
        private int removeCount;

        public MachineResultSlot(PlayerEntity player , ItemStackHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
            this.player = player;
        }

        public ItemStack decrStackSize(int amount) {
            if (this.getHasStack()) {
                this.removeCount += Math.min(amount, this.getStack().getCount());
            }

            return super.decrStackSize(amount);
        }

        @Override
        public boolean isItemValid(@Nonnull ItemStack stack) {
            return false;
        }

        @Override
        public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
            this.onCrafting(stack);
            return super.onTake(thePlayer, stack);
        }

        /**
         * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood. Typically increases an
         * internal count then calls onCrafting(item).
         */
        protected void onCrafting(ItemStack stack, int amount) {
            this.removeCount += amount;
            this.onCrafting(stack);
        }

        /**
         * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood.
         */
        protected void onCrafting(ItemStack stack) {
            stack.onCrafting(this.player.world, this.player, this.removeCount);
            if (!this.player.world.isRemote && this.inventory instanceof AbstractFurnaceTileEntity) {
                ((AbstractFurnaceTileEntity)this.inventory).unlockRecipes(this.player);
            }

            this.removeCount = 0;
            net.minecraftforge.fml.hooks.BasicEventHooks.firePlayerSmeltedEvent(this.player, stack);
        }
    }

    private static class SlotMold extends SlotItemHandler{

        public SlotMold(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(@Nonnull ItemStack stack) {
            return stack.getItem().isIn(ModTags.Items.EXTRUSION_MOLD);
        }

        @Override
        public int getSlotStackLimit() {
            return 1;
        }
    }
}
