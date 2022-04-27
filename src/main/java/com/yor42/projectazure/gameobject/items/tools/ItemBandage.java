package com.yor42.projectazure.gameobject.items.tools;

import com.yor42.projectazure.gameobject.items.ItemBaseTooltip;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;

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
     * @return
     */
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        if (player.getHealth() < player.getMaxHealth() && count % 20 == 0 && !player.getCommandSenderWorld().isClientSide) {
            player.heal(1.0f);
            stack.hurtAndBreak(1, player, (entity) -> entity.broadcastBreakEvent(player.getUsedItemHand()));
            player.getCommandSenderWorld().playSound(null, player.blockPosition(), SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 1.0f, 1.0f);
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if(context.getPlayer() != null && context.getPlayer().getHealth()<context.getPlayer().getMaxHealth()) {
            context.getPlayer().startUsingItem(context.getHand());
            return InteractionResult.CONSUME;
        }
        return InteractionResult.FAIL;
    }
}
