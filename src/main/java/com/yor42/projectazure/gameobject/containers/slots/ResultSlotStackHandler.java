package com.yor42.projectazure.gameobject.containers.slots;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ResultSlotStackHandler extends SlotItemHandler {

    private final PlayerEntity player;
    private final boolean isSmelting;
    private int removeCount;

    public ResultSlotStackHandler(PlayerEntity player , ItemStackHandler itemHandler, int index, int xPosition, int yPosition, boolean isSmelting) {
        super(itemHandler, index, xPosition, yPosition);
        this.player = player;
        this.isSmelting = isSmelting;
    }

    public ItemStack remove(int amount) {
        if (this.hasItem()) {
            this.removeCount += Math.min(amount, this.getItem().getCount());
        }

        return super.remove(amount);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
        this.checkTakeAchievements(stack);
        return super.onTake(thePlayer, stack);
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood. Typically increases an
     * internal count then calls onCrafting(item).
     */
    protected void onQuickCraft(ItemStack stack, int amount) {
        this.removeCount += amount;
        this.checkTakeAchievements(stack);
    }

    /**
     * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood.
     */
    protected void checkTakeAchievements(ItemStack stack) {
        stack.onCraftedBy(this.player.level, this.player, this.removeCount);
        if (!this.player.level.isClientSide && this.container instanceof AbstractFurnaceTileEntity) {
            ((AbstractFurnaceTileEntity)this.container).awardUsedRecipesAndPopExperience(this.player);
        }

        this.removeCount = 0;
        if(this.isSmelting) {
            net.minecraftforge.fml.hooks.BasicEventHooks.firePlayerSmeltedEvent(this.player, stack);
        }
    }

}
