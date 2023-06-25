package com.yor42.projectazure.gameobject.entity.ai.behaviors;

import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.gameobject.entity.ai.behaviors.base.ExtendedItemSwitchingBehavior;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.SplashPotionItem;
import net.tslat.smartbrainlib.util.BrainUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

import static com.yor42.projectazure.setup.register.RegisterAI.*;

public class CompanionEatBehavior extends ExtendedItemSwitchingBehavior<AbstractEntityCompanion> {

    public CompanionEatBehavior(){
        startCondition((entity)->entity.getFoodStats().getFoodLevel()<20);
        cooldownFor((e)->15);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, AbstractEntityCompanion entity) {

        InteractionHand potionHand = getFoodHand(entity);

        if(potionHand == null){
            this.ChangeItemwithMemory(FOOD_INDEX.get(),entity);
            return false;
        }

        return entity.isEating() || BrainUtils.getTargetOfEntity(entity) == null || entity.getHealth() <= 10;
    }

    @Override
    protected InteractionHand SwapHand(AbstractEntityCompanion entity) {
        return InteractionHand.MAIN_HAND;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of(Pair.of(FOOD_INDEX.get(), MemoryStatus.REGISTERED));
    }

    @Override
    protected boolean shouldKeepRunning(AbstractEntityCompanion entity) {
        return this.startCondition.test(entity);
    }

    @Override
    protected void start(AbstractEntityCompanion entity) {
        InteractionHand foodhand = getFoodHand(entity);
        if(foodhand == null){
            return;
        }

        entity.startUsingItem(foodhand);
    }

    @Nullable
    private InteractionHand getFoodHand(AbstractEntityCompanion entity){
        for(InteractionHand h : InteractionHand.values()){

            FoodProperties food =entity.getItemInHand(h).getFoodProperties(entity);

            if(food == null){
                continue;
            }

            boolean isharmful = false;

            for(Pair<MobEffectInstance, Float> pair:food.getEffects()){
                if(pair.getFirst().getEffect().getCategory() == MobEffectCategory.HARMFUL){
                    isharmful = true;
                    break;
                }
            }

            if(isharmful){
                continue;
            }

            return h;
        }
        return null;
    }
}
