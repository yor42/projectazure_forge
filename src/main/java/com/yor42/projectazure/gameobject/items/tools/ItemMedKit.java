package com.yor42.projectazure.gameobject.items.tools;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.Main.PA_GROUP;

public class ItemMedKit extends Item {
    public ItemMedKit() {
        super(new Item.Properties().durability(4).tab(PA_GROUP));
    }

    @Nonnull
    @Override
    public UseAction getUseAnimation(@Nonnull ItemStack stack) {
        return UseAction.EAT;
    }



    @Override
    public int getUseDuration(ItemStack p_77626_1_) {
        return 200;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity entity) {
        if(entity instanceof PlayerEntity) {
            entity.heal(5);
            entity.addEffect(new EffectInstance(Effects.REGENERATION, 600, 1));
            entity.addEffect(new EffectInstance(Effects.ABSORPTION, 300));
            ((PlayerEntity) entity).awardStat(Stats.ITEM_USED.get(this));
            stack.hurtAndBreak(1, entity, (entity1) -> entity1.broadcastBreakEvent(entity1.getUsedItemHand()));
        }
        return super.finishUsingItem(stack, world, entity);
    }

    @Override
    public ActionResult<ItemStack> use(World p_77659_1_, PlayerEntity p_77659_2_, Hand p_77659_3_) {
        ItemStack itemstack = p_77659_2_.getItemInHand(p_77659_3_);
        p_77659_2_.startUsingItem(p_77659_3_);
        return ActionResult.consume(itemstack);
    }
}
