package com.yor42.projectazure.gameobject.items.tools;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.Main.PA_GROUP;

public class ItemMedKit extends Item {
    public ItemMedKit() {
        super(new Item.Properties().durability(4).tab(PA_GROUP));
    }

    @Nonnull
    @Override
    public UseAnim getUseAnimation(@Nonnull ItemStack stack) {
        return UseAnim.EAT;
    }



    @Override
    public int getUseDuration(ItemStack p_77626_1_) {
        return 200;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity entity) {
        if(entity instanceof Player) {
            entity.heal(5);
            entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 600, 1));
            entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 300));
            ((Player) entity).awardStat(Stats.ITEM_USED.get(this));
            stack.hurtAndBreak(1, entity, (entity1) -> entity1.broadcastBreakEvent(entity1.getUsedItemHand()));
        }
        return super.finishUsingItem(stack, world, entity);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level p_77659_1_, Player p_77659_2_, InteractionHand p_77659_3_) {
        ItemStack itemstack = p_77659_2_.getItemInHand(p_77659_3_);
        p_77659_2_.startUsingItem(p_77659_3_);
        return InteractionResultHolder.consume(itemstack);
    }
}
