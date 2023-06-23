package com.yor42.projectazure.gameobject.entity.ai.behaviors;

import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.gameobject.entity.ai.behaviors.base.ExtendedItemSwitchingBehavior;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.setup.register.RegisterAI;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class CompanionUseTotemBehavior extends ExtendedItemSwitchingBehavior<AbstractEntityCompanion> {

    public CompanionUseTotemBehavior(){
        startCondition((entity)-> entity.getHealth()<6);
        cooldownFor((ety)->5);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, AbstractEntityCompanion entity) {
        InteractionHand hand = getItemHand(entity, (stack)->stack.getItem() == Items.TOTEM_OF_UNDYING);

        if(hand == null){
            ChangeItemwithMemory(RegisterAI.TOTEM_INDEX.get(),entity);
            return false;
        }

        return true;
    }

    @Override
    protected boolean canStillUse(ServerLevel level, AbstractEntityCompanion entity, long gameTime) {
        boolean flag1 = (entity.getRemainingFireTicks() > 0 || entity.hasEffect(MobEffects.WITHER) || entity.tickCount - entity.getLastHurtByMobTimestamp()<200);
        boolean value = entity.getHealth()<9;
        boolean canuseTotem = entity.getOffhandItem().getItem() == Items.TOTEM_OF_UNDYING;
        return value && flag1 && canuseTotem;
    }

    @Override
    protected void tick(AbstractEntityCompanion entity) {
        InteractionHand hand = getItemHand(entity, (stack)->stack.getItem() == Items.TOTEM_OF_UNDYING);
        if(hand == null && entity.hasEffect(MobEffects.REGENERATION) && entity.hasEffect(MobEffects.ABSORPTION)){
            doStop((ServerLevel)entity.level, entity, entity.level.getGameTime());
        }
    }

    @Override
    protected InteractionHand SwapHand() {
        return InteractionHand.OFF_HAND;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of(Pair.of(RegisterAI.TOTEM_INDEX.get(), MemoryStatus.REGISTERED));
    }
}
