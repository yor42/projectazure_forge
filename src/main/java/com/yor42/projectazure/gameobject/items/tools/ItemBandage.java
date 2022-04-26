package com.yor42.projectazure.gameobject.items.tools;

import com.yor42.projectazure.gameobject.items.ItemBaseTooltip;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.item.ItemStack;

public class ItemBandage extends ItemBaseTooltip {

    public ItemBandage(Properties properties) {
        super(properties);
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getUseDuration(ItemStack stack) {
        return 80;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public UseAction getUseAnimation(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        if (player.getHealth() < player.getMaxHealth() && count % 20 == 0 && !player.getCommandSenderWorld().isClientSide) {
            player.heal(1.0f);
            stack.hurtAndBreak(1, player, (entity) -> entity.broadcastBreakEvent(player.getUsedItemHand()));
            player.getCommandSenderWorld().playSound(null, player.blockPosition(), SoundEvents.ARMOR_EQUIP_LEATHER, SoundCategory.PLAYERS, 1.0f, 1.0f);
        }
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        if(context.getPlayer() != null && context.getPlayer().getHealth()<context.getPlayer().getMaxHealth()) {
            context.getPlayer().startUsingItem(context.getHand());
            return ActionResultType.CONSUME;
        }
        return ActionResultType.FAIL;
    }
}
