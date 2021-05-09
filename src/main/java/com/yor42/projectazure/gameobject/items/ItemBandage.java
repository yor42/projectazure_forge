package com.yor42.projectazure.gameobject.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundEvents;

public class ItemBandage extends ItemBaseTooltip {

    public ItemBandage(Properties properties) {
        super(properties);
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        if(player.getHealth()<player.getMaxHealth() && count %20 == 0){
            player.heal(1.0f);
            stack.damageItem(1, player, (playerEntity) -> playerEntity.sendBreakAnimation(player.getActiveHand()));
            player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.0f, 1.0f);
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
