package com.yor42.projectazure.gameobject.items.tools;

import com.yor42.projectazure.interfaces.IItemDestroyable;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.setup.register.RegisterItems;
import com.yor42.projectazure.setup.register.registerSounds;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemSyringe extends Item implements IItemDestroyable {
    public ItemSyringe(Properties p_i48487_1_) {
        super(p_i48487_1_.stacksTo(4));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level world, Player player, @Nonnull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if(PotionUtils.getPotion(stack)==Potions.EMPTY){
            return InteractionResultHolder.fail(stack);
        }

        if (player instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer)player, stack);
        }

        if(!player.isCrouching()){
            List<MobEffectInstance> effects =  PotionUtils.getMobEffects(stack);
            if(effects.isEmpty()){
                return InteractionResultHolder.pass(stack);
            }

            for(MobEffectInstance effectinstance : effects) {
                if (effectinstance.getEffect().isInstantenous()) {
                    effectinstance.getEffect().applyInstantenousEffect(player, player, player, effectinstance.getAmplifier(), 1.0D);
                } else {
                    player.addEffect(new MobEffectInstance(effectinstance));
                }
            }
            player.playSound(registerSounds.SYRINGE_INJECT, 0.8F+(0.4F* MathUtil.rand.nextFloat()), 0.8F+(0.4F* MathUtil.rand.nextFloat()));
        }
        else{
            player.playSound(SoundEvents.BREWING_STAND_BREW, 0.8F+(0.4F* MathUtil.rand.nextFloat()), 1.3F+(0.4F* MathUtil.rand.nextFloat()));
        }

        if(!player.getAbilities().instabuild || player.isCrouching() && PotionUtils.getPotion(stack)!=Potions.EMPTY){
            player.awardStat(Stats.ITEM_USED.get(this));
            stack.shrink(1);
            ItemStack returnstack = new ItemStack(RegisterItems.SYRINGE.get());
            if(stack.isEmpty()){
                player.setItemInHand(hand, returnstack.copy());
            }
            else{
                player.addItem(returnstack.copy());
            }
            player.getCooldowns().addCooldown(RegisterItems.SYRINGE.get(), 1200);
        }

        return InteractionResultHolder.success(stack);
    }

    @Override
    public Component getName(ItemStack p_200295_1_) {
        Potion potion = PotionUtils.getPotion(p_200295_1_);
        List<MobEffectInstance> effects = potion.getEffects();
        if(effects.isEmpty()){
            return new TranslatableComponent(this.getDescriptionId(p_200295_1_)+potion.getName("."));
        }

        return new TranslatableComponent(this.getDescriptionId()+".withpotion", new TranslatableComponent(effects.get(0).getDescriptionId()));
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack p_77624_1_, @Nullable Level p_77624_2_, List<Component> p_77624_3_, TooltipFlag p_77624_4_) {
        PotionUtils.addPotionTooltip(p_77624_1_, p_77624_3_, 1.0F);
    }

    public void fillItemCategory(CreativeModeTab p_150895_1_, NonNullList<ItemStack> p_150895_2_) {
        if (this.allowdedIn(p_150895_1_)) {
            p_150895_2_.add(new ItemStack(this));
            for(Potion potion : ForgeRegistries.POTIONS) {
                if (potion != Potions.EMPTY) {
                    p_150895_2_.add(PotionUtils.setPotion(new ItemStack(this), potion));
                }
            }
        }

    }

    //Rule of thumb: Do not reuse syringe
    @Override
    public int getMaxHP() {
        return 1;
    }

    @Override
    public int getRepairAmount(ItemStack candidateItem) {
        return  candidateItem.is(Tags.Items.INGOTS_IRON)? 1:0;
    }
}
