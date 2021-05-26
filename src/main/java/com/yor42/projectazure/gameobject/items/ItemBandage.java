package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.RayTraceContext;

public class ItemBandage extends ItemBaseTooltip {

    public ItemBandage(Properties properties) {
        super(properties);
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        if(player instanceof PlayerEntity) {

            PlayerEntity playerEntity = (PlayerEntity) player;
            RayTraceContext lookEyetrace = new RayTraceContext(playerEntity.getEyePosition(1.0f), playerEntity.getLook(1.0F), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, playerEntity);

            if (player.isSneaking()&&playerEntity.getHealth() < playerEntity.getMaxHealth() && count % 20 == 0 && !player.getEntityWorld().isRemote) {
                playerEntity.heal(1.0f);
                if(stack.attemptDamageItem(1, MathUtil.getRand(), (ServerPlayerEntity) playerEntity)){
                    stack.shrink(1);
                }
                player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.0f, 1.0f);
            }
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
