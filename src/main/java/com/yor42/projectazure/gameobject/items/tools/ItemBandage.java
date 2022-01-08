package com.yor42.projectazure.gameobject.items.tools;

import com.yor42.projectazure.gameobject.items.ItemBaseTooltip;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;

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
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        if (player.getHealth() < player.getMaxHealth() && count % 20 == 0 && !player.getEntityWorld().isRemote) {
            player.heal(1.0f);
            stack.damageItem(1, player, (entity) -> entity.sendBreakAnimation(player.getActiveHand()));
            player.getEntityWorld().playSound(null, player.getPosition(), SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.PLAYERS, 1.0f, 1.0f);
        }
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        if(context.getPlayer() != null && context.getPlayer().getHealth()<context.getPlayer().getMaxHealth()) {
            context.getPlayer().setActiveHand(context.getHand());
            return ActionResultType.CONSUME;
        }
        return ActionResultType.FAIL;
    }
}
