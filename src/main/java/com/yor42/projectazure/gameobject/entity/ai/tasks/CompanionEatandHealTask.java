package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.item.UseAction;
import net.minecraft.potion.*;
import net.minecraft.util.Hand;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static net.minecraft.util.Hand.OFF_HAND;

public class CompanionEatandHealTask extends Task<AbstractEntityCompanion> {

    int FoodCooldown=0;
    @Nullable
    private Hand FoodHand = null;

    public CompanionEatandHealTask() {
        super(ImmutableMap.of());
    }

    @Override
    protected boolean checkExtraStartConditions(ServerWorld world, AbstractEntityCompanion entity) {
        if(this.FoodCooldown > 0){
            this.FoodCooldown--;
            return false;
        }
        else if(entity.isCriticallyInjured()){
            return false;
        }
        else if(entity.getFoodStats().getFoodLevel()<20){
            int InventoryFood = this.getFoodIndex(false, entity);
            if(this.FoodCooldown == 0) {
                if (entity.tickCount % 10 == 0) {
                    InventoryFood = this.getFoodIndex(false, entity);
                }
                if (this.getFoodHand(false, entity).isPresent()) {
                    this.FoodHand = this.getFoodHand(false, entity).get();
                    boolean value = (entity.isEating() || (!entity.isAggressive() && entity.getTarget() == null) || (entity.getHealth()<=10)) && entity.getFoodStats().getFoodLevel()<20;
                    return value;
                } else if (InventoryFood >-1&& entity.getItemSwapIndexOffHand() == -1) {
                    this.ChangeItem(InventoryFood, entity);
                    this.FoodCooldown = 15;
                }
            }
        }
        else if(entity.getHealth()<entity.getMaxHealth()) {
            if(this.FoodCooldown == 0) {
                int InventoryFood = this.getFoodIndex(true, entity);
                if (this.getFoodHand(true, entity).isPresent()) {
                    this.FoodHand = this.getFoodHand(true, entity).get();
                    boolean val = entity.isEating() || !entity.isAggressive() && entity.getTarget() == null;
                    return val;
                } else if (InventoryFood >-1 && entity.getItemSwapIndexOffHand() == -1) {
                    this.ChangeItem(InventoryFood, entity);
                    this.FoodCooldown = 15;
                }
            }
        }
        return false;
    }

    private void ChangeItem(int index, AbstractEntityCompanion entity){
        ItemStack Buffer = entity.getOffhandItem();
        entity.setItemInHand(OFF_HAND, entity.getInventory().getStackInSlot(index));
        entity.getInventory().setStackInSlot(index, Buffer);
        entity.setItemSwapIndexOffHand(index);
    }

    private Optional<Hand> getFoodHand(boolean includeHealingPotion, AbstractEntityCompanion entity){
        if(this.shouldEat(entity, entity.getMainHandItem(), includeHealingPotion)){
            return Optional.of(Hand.MAIN_HAND);
        }
        else if(this.shouldEat(entity, entity.getOffhandItem(), includeHealingPotion)) {
            return Optional.of(OFF_HAND);
        }
        else{
            return Optional.empty();
        }
    }

    private int getFoodIndex(boolean includeHealingPotion, AbstractEntityCompanion entity){
        ItemStackHandler Inventory = entity.getInventory();
        for(int i = 0; i<Inventory.getSlots(); i++){
            ItemStack stack = Inventory.getStackInSlot(i);
            if(shouldEat(entity, stack, includeHealingPotion)){
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void start(ServerWorld p_212831_1_, AbstractEntityCompanion p_212831_2_, long p_212831_3_) {
        if(this.FoodHand != null){
            p_212831_2_.startUsingItem(this.FoodHand);
        }
    }

    @Override
    protected boolean canStillUse(ServerWorld p_212834_1_, AbstractEntityCompanion entity, long p_212834_3_) {
        List<LivingEntity> list = p_212834_1_.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().expandTowards(5.0D, 3.0D, 5.0D));

        if (!list.isEmpty() && entity.getFoodStats().getFoodLevel()>5) {
            for (LivingEntity mob : list) {
                if (mob != null) {
                    if (mob instanceof MonsterEntity) {
                        return false;
                    }
                }
            }
        }

        return entity.isUsingItem() || entity.getItemInHand(entity.getUsedItemHand()).isEdible();
    }

    private boolean shouldEat(AbstractEntityCompanion entity, ItemStack stack, boolean IncludeHealingPotion){
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
            return entity.canEat(stack.getItem().getFoodProperties().canAlwaysEat());
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

}
