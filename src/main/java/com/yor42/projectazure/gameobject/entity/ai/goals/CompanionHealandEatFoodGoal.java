package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.items.ItemBandage;
import javafx.util.Pair;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.item.UseAction;
import net.minecraft.potion.*;
import net.minecraft.util.Hand;
import net.minecraftforge.items.ItemStackHandler;
import org.lwjgl.system.CallbackI;

import javax.annotation.Nullable;
import javax.swing.text.html.Option;
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
    public boolean shouldExecute() {
        if(this.FoodCooldown > 0){
            this.FoodCooldown--;
        }
        Pair<ItemStack, Integer> InventoryFood = this.getFood(true);
        if(this.companion.isEating()){
            return true;
        }
        if(this.companion.getHealth()<this.companion.getMaxHealth()) {
            if(this.FoodCooldown == 0) {

                if (this.getFoodHand(true).isPresent()) {
                    this.FoodHand = this.getFoodHand(true).get();
                    boolean val = this.companion.isEating() || !this.companion.isAggressive() && this.companion.getAttackTarget() == null;
                    return val;
                } else if (InventoryFood.getKey() != ItemStack.EMPTY && this.companion.getItemSwapIndexOffHand() == -1) {
                    this.ChangeItem(InventoryFood.getKey(), InventoryFood.getValue());
                    this.FoodCooldown = 15;
                }
            }
        }
        else if(this.companion.getFoodStats().getFoodLevel()<10){
            if(this.FoodCooldown == 0) {
                if (this.companion.ticksExisted % 10 == 0) {
                    InventoryFood = this.getFood(false);
                }
                if (this.getFoodHand(false).isPresent()) {
                    this.FoodHand = this.getFoodHand(false).get();
                    return this.companion.isEating() || !this.companion.isAggressive() && this.companion.getAttackTarget() == null || this.companion.getFoodStats().getFoodLevel()<7;
                } else if (InventoryFood.getKey() != ItemStack.EMPTY && this.companion.getItemSwapIndexOffHand() == -1) {
                    this.ChangeItem(InventoryFood.getKey(), InventoryFood.getValue());
                    this.FoodCooldown = 15;
                }
            }
        }
        return false;
    }

    @Override
    public void resetTask() {
        if(this.companion.getItemSwapIndexOffHand() != -1){
            ItemStack Buffer = this.companion.getInventory().getStackInSlot(this.companion.getItemSwapIndexOffHand());
            this.companion.getInventory().setStackInSlot(this.companion.getItemSwapIndexOffHand(), this.companion.getHeldItemOffhand());
            this.companion.setHeldItem(OFF_HAND, Buffer);
            this.companion.setItemSwapIndexOffHand(-1);
        }
        this.FoodHand = null;
        this.companion.setEating(false);
        this.companion.stopActiveHand();
    }

    private void ChangeItem(ItemStack food, int index){
        ItemStack Buffer = this.companion.getHeldItemOffhand();
        this.companion.setHeldItem(OFF_HAND, food);
        this.companion.getInventory().setStackInSlot(index, Buffer);
        this.companion.setItemSwapIndexOffHand(index);
    }

    private Optional<Hand> getFoodHand(boolean includeHealingPotion){
        if(this.shouldEat(this.companion.getHeldItemMainhand(), includeHealingPotion)){
            return Optional.of(Hand.MAIN_HAND);
        }
        else if(this.shouldEat(this.companion.getHeldItemOffhand(), includeHealingPotion)) {
            return Optional.of(OFF_HAND);
        }
        else{
            return Optional.empty();
        }
    }

    private Pair<ItemStack, Integer> getFood(boolean includeHealingPotion){
        ItemStackHandler Inventory = this.companion.getInventory();
        for(int i = 0; i<Inventory.getSlots(); i++){
            ItemStack stack = Inventory.getStackInSlot(i);
            if(shouldEat(stack, includeHealingPotion)){
                return new Pair<>(stack, i);
            }
        }
        return new Pair<>(ItemStack.EMPTY, -1);
    }

    private boolean shouldEat(ItemStack stack, boolean IncludeHealingPotion){
        //Food
        if(stack.getUseAction() == UseAction.EAT && stack.getCount() > 0 && stack.isFood()){
            if(!stack.getItem().getFood().getEffects().isEmpty()){
                for(int i =0; i<stack.getItem().getFood().getEffects().size(); i++){
                    EffectInstance instance = stack.getItem().getFood().getEffects().get(i).getFirst();
                    if(instance.getPotion().getEffectType() == EffectType.HARMFUL){
                        return false;
                    }
                }
            }
            return this.companion.canEat(stack.getItem().getFood().canEatWhenFull());
        }
        else if(IncludeHealingPotion) {
            if (stack.getUseAction() == UseAction.DRINK && !(stack.getItem() instanceof SplashPotionItem) && stack.getCount() > 0) {
                for (EffectInstance effectinstance : PotionUtils.getEffectsFromStack(stack)) {
                    if (doesPotionHeal(effectinstance.getPotion().getEffect())) {
                        return true;
                    }
                }
                return false;
            }
            else return stack.getItem() instanceof ItemBandage;
        }
        return false;
    }

    private boolean doesPotionHeal(Effect effect){
        return effect == Effects.INSTANT_HEALTH || effect == Effects.REGENERATION;
    }

    @Override
    public void startExecuting() {
        if(this.FoodHand != null){
            this.companion.setActiveHand(this.FoodHand);
            this.companion.setEating(true);
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        List<LivingEntity> list = this.companion.getEntityWorld().getEntitiesWithinAABB(LivingEntity.class, this.companion.getBoundingBox().expand(5.0D, 3.0D, 5.0D));

        if (!list.isEmpty() && this.companion.getFoodStats().getFoodLevel()>5) {
            for (LivingEntity mob : list) {
                if (mob != null) {
                    if (mob instanceof MobEntity && (((MobEntity) mob).getAttackTarget() instanceof AbstractEntityCompanion || ((MobEntity) mob).getAttackTarget() == this.companion.getOwner())) {
                        return false;
                    }
                }
            }
        }

        return this.companion.isHandActive() && this.companion.getAttackTarget() == null && this.companion.getHealth() < this.companion.getMaxHealth() || this.companion.getAttackTarget() != null && this.companion.getHealth() < this.companion.getMaxHealth() / 2 + 2 && this.companion.isEating() || this.companion.getFoodStats().getFoodLevel()<7;
    }
}
