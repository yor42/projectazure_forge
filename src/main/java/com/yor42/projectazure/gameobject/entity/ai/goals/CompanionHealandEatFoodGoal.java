package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SplashPotionItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static net.minecraft.world.InteractionHand.MAIN_HAND;
import static net.minecraft.world.InteractionHand.OFF_HAND;

public class CompanionHealandEatFoodGoal extends Goal {

    private final AbstractEntityCompanion companion;
    @Nullable
    private InteractionHand FoodHand = null;
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
                    boolean value = (this.companion.isEating() || (!this.companion.isAggressive() && this.companion.getTarget() == null) || (this.companion.getHealth()<=10)) && this.companion.getFoodStats().getFoodLevel()<20;
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
        this.companion.releaseUsingItem();
    }

    private void ChangeItem(int index){
        ItemStack Buffer = this.companion.getOffhandItem();
        this.companion.setItemInHand(OFF_HAND, this.companion.getInventory().getStackInSlot(index));
        this.companion.getInventory().setStackInSlot(index, Buffer);
        this.companion.setItemSwapIndexOffHand(index);
    }

    private Optional<InteractionHand> getFoodHand(boolean includeHealingPotion){
        if(this.shouldEat(this.companion.getMainHandItem(), includeHealingPotion)){
            return Optional.of(MAIN_HAND);
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
        if(stack.getUseAnimation() == UseAnim.EAT && stack.getCount() > 0 && stack.isEdible()){
            if(!stack.getItem().getFoodProperties().getEffects().isEmpty()){
                for(int i =0; i<stack.getItem().getFoodProperties().getEffects().size(); i++){
                    MobEffectInstance instance = stack.getItem().getFoodProperties().getEffects().get(i).getFirst();
                    if(instance.getEffect().getCategory() == MobEffectCategory.HARMFUL){
                        return false;
                    }
                }
            }
            return this.companion.canEat(stack.getItem().getFoodProperties().canAlwaysEat());
        }
        if(IncludeHealingPotion) {
            if (stack.getUseAnimation() == UseAnim.DRINK && !(stack.getItem() instanceof SplashPotionItem) && stack.getCount() > 0) {
                for (MobEffectInstance MobEffectInstance : PotionUtils.getMobEffects(stack)) {
                    if (doesPotionHeal(MobEffectInstance.getEffect())) {
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }

    private boolean doesPotionHeal(MobEffect effect){
        return effect == MobEffects.HEAL || effect == MobEffects.REGENERATION;
    }

    @Override
    public void start() {
        if(this.FoodHand != null){
            this.companion.startUsingItem(this.FoodHand);
        }
    }

    @Override
    public boolean canContinueToUse() {
        List<LivingEntity> list = this.companion.getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class, this.companion.getBoundingBox().expandTowards(5.0D, 3.0D, 5.0D));

        if (!list.isEmpty() && this.companion.getFoodStats().getFoodLevel()>5) {
            for (LivingEntity mob : list) {
                if (mob != null) {
                    if (mob instanceof Monster) {
                        return false;
                    }
                }
            }
        }

        return this.companion.isUsingItem() || this.companion.getItemInHand(this.companion.getUsedItemHand()).isEdible();
    }
}
