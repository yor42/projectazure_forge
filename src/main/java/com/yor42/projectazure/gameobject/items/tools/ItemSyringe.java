package com.yor42.projectazure.gameobject.items.tools;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemSyringe extends Item {
    public ItemSyringe(Properties p_i48487_1_) {
        super(p_i48487_1_.stacksTo(16));
    }

    @Override
    public ActionResult<ItemStack> use(@Nonnull World world, PlayerEntity player, @Nonnull Hand hand) {

        ItemStack stack = player.getItemInHand(hand);
        if (player instanceof ServerPlayerEntity) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayerEntity)player, stack);
        }

        if(!world.isClientSide()){
            List<EffectInstance> effects =  PotionUtils.getMobEffects(stack);
            if(effects.isEmpty()){
                return ActionResult.pass(stack);
            }

            for(EffectInstance effectinstance : effects) {
                if (effectinstance.getEffect().isInstantenous()) {
                    effectinstance.getEffect().applyInstantenousEffect(player, player, player, effectinstance.getAmplifier(), 1.0D);
                } else {
                    player.addEffect(new EffectInstance(effectinstance));
                }
            }
        }

        if(!player.abilities.instabuild){
            player.awardStat(Stats.ITEM_USED.get(this));
            if (!player.abilities.instabuild) {
                stack.shrink(1);
            }

            if(stack.isEmpty()){
                player.setItemInHand(hand, new ItemStack(this));
            }
            else{
                player.addItem(new ItemStack(this));
            }
        }

        return ActionResult.success(stack);
    }

    @Override
    public UseAction getUseAnimation(ItemStack p_77661_1_) {
        return super.getUseAnimation(p_77661_1_);
    }

    public String getDescriptionId(ItemStack p_77667_1_) {
        return PotionUtils.getPotion(p_77667_1_).getName(this.getDescriptionId() + ".effect.");
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack p_77624_1_, @Nullable World p_77624_2_, List<ITextComponent> p_77624_3_, ITooltipFlag p_77624_4_) {
        PotionUtils.addPotionTooltip(p_77624_1_, p_77624_3_, 1.0F);
    }

    public void fillItemCategory(ItemGroup p_150895_1_, NonNullList<ItemStack> p_150895_2_) {
        if (this.allowdedIn(p_150895_1_)) {
            p_150895_2_.add(new ItemStack(this));
            for(Potion potion : ForgeRegistries.POTION_TYPES) {
                if (potion != Potions.EMPTY) {
                    p_150895_2_.add(PotionUtils.setPotion(new ItemStack(this), potion));
                }
            }
        }

    }
}
