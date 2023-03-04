package com.yor42.projectazure.gameobject.items.tools;

import com.yor42.projectazure.interfaces.IItemDestroyable;
import com.yor42.projectazure.libs.utils.ItemStackUtils;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.setup.register.RegisterItems;
import com.yor42.projectazure.setup.register.registerSounds;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
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
    public ActionResult<ItemStack> use(@Nonnull World world, PlayerEntity player, @Nonnull Hand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if(PotionUtils.getPotion(stack)==Potions.EMPTY){
            return ActionResult.fail(stack);
        }

        if (player instanceof ServerPlayerEntity) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayerEntity)player, stack);
        }

        if(!player.isCrouching()){
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
            player.playSound(registerSounds.SYRINGE_INJECT, 0.8F+(0.4F* MathUtil.rand.nextFloat()), 0.8F+(0.4F* MathUtil.rand.nextFloat()));
        }
        else{
            player.playSound(SoundEvents.BREWING_STAND_BREW, 0.8F+(0.4F* MathUtil.rand.nextFloat()), 1.3F+(0.4F* MathUtil.rand.nextFloat()));
        }

        if(!player.abilities.instabuild || player.isCrouching() && PotionUtils.getPotion(stack)!=Potions.EMPTY){
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

        return ActionResult.success(stack);
    }

    @Override
    public ITextComponent getName(ItemStack p_200295_1_) {
        Potion potion = PotionUtils.getPotion(p_200295_1_);
        List<EffectInstance> effects = potion.getEffects();
        if(effects.isEmpty()){
            return new TranslationTextComponent(this.getDescriptionId(p_200295_1_)+potion.getName("."));
        }

        return new TranslationTextComponent(this.getDescriptionId()+".withpotion", new TranslationTextComponent(effects.get(0).getDescriptionId()));
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

    //Rule of thumb: Do not reuse syringe
    @Override
    public int getMaxHP() {
        return 1;
    }

    @Override
    public int getRepairAmount(ItemStack candidateItem) {
        return  candidateItem.getItem().is(Tags.Items.INGOTS_IRON)? 1:0;
    }
}
