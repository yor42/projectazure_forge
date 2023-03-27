package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static com.yor42.projectazure.setup.register.RegisterAI.FOOD_INDEX;

public class CompanionEatTask extends Behavior<AbstractEntityCompanion> {

    int FoodCooldown=0;
    @Nullable
    private InteractionHand FoodHand = null;

    public CompanionEatTask() {
        super(ImmutableMap.of(FOOD_INDEX.get(), MemoryStatus.REGISTERED));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel world, AbstractEntityCompanion entity) {
        if (this.FoodCooldown > 0) {
            this.FoodCooldown--;
            return false;
        } else if (entity.getFoodStats().getFoodLevel() < 20) {
            if (this.getFoodHand(entity).isPresent()) {
                this.FoodHand = this.getFoodHand(entity).get();
                boolean value = (entity.isEating() || (!entity.isAggressive() && !entity.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET)) || (entity.getHealth() <= 10)) && entity.getFoodStats().getFoodLevel() < 20;
                return value;
            }
            entity.getBrain().getMemory(FOOD_INDEX.get()).ifPresent((index) -> {
                if (entity.getItemSwapIndexOffHand() == -1) {
                    this.ChangeItem(index, entity);
                    this.FoodCooldown = 15;
                }
            });
        }
        return false;
    }

    private void ChangeItem(int index, AbstractEntityCompanion entity){
        ItemStack Buffer = entity.getOffhandItem();
        entity.setItemInHand(InteractionHand.OFF_HAND, entity.getInventory().getStackInSlot(index));
        entity.getInventory().setStackInSlot(index, Buffer);
        entity.setItemSwapIndexOffHand(index);
    }

    private Optional<InteractionHand> getFoodHand(AbstractEntityCompanion entity){
        for(InteractionHand h : InteractionHand.values()){
            if(this.shouldEat(entity, entity.getItemInHand(h))){
                return Optional.of(h);
            }
        }
        return Optional.empty();
    }

    @Override
    protected void start(ServerLevel p_212831_1_, AbstractEntityCompanion p_212831_2_, long p_212831_3_) {
        if(this.FoodHand != null){
            p_212831_2_.startUsingItem(this.FoodHand);
        }
    }

    @Override
    protected boolean canStillUse(ServerLevel p_212834_1_, AbstractEntityCompanion entity, long p_212834_3_) {
        List<LivingEntity> list = p_212834_1_.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(4.0D, 3.0D, 4.0D));

        if (!list.isEmpty() && entity.getFoodStats().getFoodLevel()>5) {
            for (LivingEntity mob : list) {
                if (mob != null) {
                    if (mob instanceof Monster) {
                        return false;
                    }
                }
            }
        }

        return entity.isUsingItem() || entity.getItemInHand(entity.getUsedItemHand()).isEdible();
    }

    @Override
    protected void stop(ServerLevel p_212835_1_, AbstractEntityCompanion entity, long p_212835_3_) {
        entity.stopUsingItem();
        if(entity.getItemSwapIndexOffHand()>-1) {
            ItemStack buffer = entity.getOffhandItem();
            entity.setItemInHand(InteractionHand.OFF_HAND, entity.getInventory().getStackInSlot(entity.getItemSwapIndexOffHand()));
            entity.getInventory().setStackInSlot(entity.getItemSwapIndexOffHand(), buffer);
        }
        entity.setItemSwapIndexOffHand(-1);
    }

    private boolean shouldEat(AbstractEntityCompanion entity, ItemStack stack){
        //Food
        if(stack.isEdible()){
            return entity.canEat(stack.getItem().getFoodProperties().canAlwaysEat());
        }
        return false;
    }

}
