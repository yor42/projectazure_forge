package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.items.tools.ItemBandage;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.item.UseAction;
import net.minecraft.potion.*;
import net.minecraft.util.Hand;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static net.minecraft.util.Hand.OFF_HAND;

public class CompanionHealandEatFoodGoal extends Goal {

    private final AbstractEntityCompanion companion;
    @Nullable
    private Hand FoodHand = null;
    int FoodCooldown=0;

    public CompanionHealandEatFoodGoal(AbstractEntityCompanion companion){
        this.companion = companion;
    }

    @Override
    public boolean canUse() {
        if(this.FoodCooldown > 0){
            this.FoodCooldown--;
        }
        else if(this.companion.isCriticallyInjured()){
            return false;
        }
        else if(this.companion.getFoodStats().getFoodLevel()<20){
            int InventoryFood = this.getFoodIndex(false);
            if(this.FoodCooldown == 0) {
                if (this.companion.tickCount % 10 == 0) {
                    InventoryFood = this.getFoodIndex(false);
                }
                if (this.getFoodHand(false).isPresent()) {
                    this.FoodHand = this.getFoodHand(false).get();
                    boolean value = this.companion.isEating() || (!this.companion.isAggressive() && this.companion.getTarget() == null) || this.companion.getFoodStats().getFoodLevel()<20;
                    return value;
                } else if (InventoryFood >-1&& this.companion.getItemSwapIndexOffHand() == -1) {
                    this.ChangeItem(InventoryFood);
                    this.FoodCooldown = 15;
                }
            }
        }
        else if(this.companion.getHealth()<this.companion.getMaxHealth()) {
            if(this.FoodCooldown == 0) {
                int InventoryFood = this.getFoodIndex(true);
                if (this.getFoodHand(true).isPresent()) {
                    this.FoodHand = this.getFoodHand(true).get();
                    boolean val = this.companion.isEating() || !this.companion.isAggressive() && this.companion.getTarget() == null;
                    return val;
                } else if (InventoryFood >-1 && this.companion.getItemSwapIndexOffHand() == -1) {
                    this.ChangeItem(InventoryFood);
                    this.FoodCooldown = 15;
                }
            }
        }
        return false;
    }

    @Override
    public void stop() {
        if(this.companion.getItemSwapIndexOffHand() != -1){
            ItemStack Buffer = this.companion.getInventory().getStackInSlot(this.companion.getItemSwapIndexOffHand());
            this.companion.getInventory().setStackInSlot(this.companion.getItemSwapIndexOffHand(), this.companion.getOffhandItem());
            this.companion.setItemInHand(OFF_HAND, Buffer);
            this.companion.setItemSwapIndexOffHand(-1);
        }
        this.FoodHand = null;
        this.companion.setEating(false);
        this.companion.releaseUsingItem();
    }

    private void ChangeItem(int index){
        ItemStack Buffer = this.companion.getOffhandItem();
        this.companion.setItemInHand(OFF_HAND, this.companion.getInventory().getStackInSlot(index));
        this.companion.getInventory().setStackInSlot(index, Buffer);
        this.companion.setItemSwapIndexOffHand(index);
    }

    private Optional<Hand> getFoodHand(boolean includeHealingPotion){
        if(this.shouldEat(this.companion.getMainHandItem(), includeHealingPotion)){
            return Optional.of(Hand.MAIN_HAND);
        }
        else if(this.shouldEat(this.companion.getOffhandItem(), includeHealingPotion)) {
            return Optional.of(OFF_HAND);
        }
        else{
            return Optional.empty();
        }
    }

    private int getFoodIndex(boolean includeHealingPotion){
        ItemStackHandler Inventory = this.companion.getInventory();
        for(int i = 0; i<Inventory.getSlots(); i++){
            ItemStack stack = Inventory.getStackInSlot(i);
            if(shouldEat(stack, includeHealingPotion)){
                return i;
            }
        }
        return -1;
    }

    private boolean shouldEat(ItemStack stack, boolean IncludeHealingPotion){
        //Food
        if(stack.getUseAnimation() == UseAction.EAT && stack.getCount() > 0 && stack.isEdible()){
            if(!stack.getItem().getFoodProperties().getEffects().isEmpty()){
                for(int i =0; i<stack.getItem().getFoodProperties().getEffects().size(); i++){
                    EffectInstance instance = stack.getItem().getFoodProperties().getEffects().get(i).getFirst();
                    if(instance.getEffect().getCategory() == EffectType.HARMFUL){
                        return false;
                    }
                }
            }
            return this.companion.canEat(stack.getItem().getFoodProperties().canAlwaysEat());
        }
        if(IncludeHealingPotion) {
            if (stack.getUseAnimation() == UseAction.DRINK && !(stack.getItem() instanceof SplashPotionItem) && stack.getCount() > 0) {
                for (EffectInstance effectinstance : PotionUtils.getMobEffects(stack)) {
                    if (doesPotionHeal(effectinstance.getEffect().getEffect())) {
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }

    private boolean doesPotionHeal(Effect effect){
        return effect == Effects.HEAL || effect == Effects.REGENERATION;
    }

    @Override
    public void start() {
        if(this.FoodHand != null){
            this.companion.startUsingItem(this.FoodHand);
            this.companion.setEating(true);
        }
    }

    @Override
    public boolean canContinueToUse() {
        List<LivingEntity> list = this.companion.getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class, this.companion.getBoundingBox().expandTowards(5.0D, 3.0D, 5.0D));

        if (!list.isEmpty() && this.companion.getFoodStats().getFoodLevel()>5) {
            for (LivingEntity mob : list) {
                if (mob != null) {
                    if (mob instanceof MonsterEntity) {
                        return false;
                    }
                }
            }
        }

        return this.companion.isUsingItem() || this.companion.getItemInHand(this.companion.getUsedItemHand()).isEdible();
    }
}
