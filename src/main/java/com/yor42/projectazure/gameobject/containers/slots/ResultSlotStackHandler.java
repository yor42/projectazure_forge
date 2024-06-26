package com.yor42.projectazure.gameobject.containers.slots;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ResultSlotStackHandler extends SlotItemHandler {

    private final Player player;
    private final boolean isSmelting;
    private int removeCount;

    public ResultSlotStackHandler(Player player , ItemStackHandler itemHandler, int index, int xPosition, int yPosition, boolean isSmelting) {
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
    public void onTake(Player player, ItemStack itemstack) {
        this.checkTakeAchievements(itemstack);
        super.onTake(player, itemstack);
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
        if (!this.player.level.isClientSide && this.container instanceof AbstractFurnaceBlockEntity) {
            ((AbstractFurnaceBlockEntity)this.container).awardUsedRecipesAndPopExperience((ServerPlayer)this.player);
        }

        this.removeCount = 0;
        if(this.isSmelting) {
            MinecraftForge.EVENT_BUS.post(new PlayerEvent.ItemSmeltedEvent(this.player, stack));
        }
    }

}
