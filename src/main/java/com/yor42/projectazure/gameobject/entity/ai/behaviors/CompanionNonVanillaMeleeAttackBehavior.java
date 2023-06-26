package com.yor42.projectazure.gameobject.entity.ai.behaviors;

import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.gameobject.entity.ai.behaviors.base.MultiTriggeringDelayedBehavior;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.interfaces.IMeleeAttacker;
import com.yor42.projectazure.setup.register.RegisterAI;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;

import static com.yor42.projectazure.setup.register.RegisterAI.Animations.MELEE_ATTACK;

public class CompanionNonVanillaMeleeAttackBehavior extends MultiTriggeringDelayedBehavior<AbstractEntityCompanion> {

    public CompanionNonVanillaMeleeAttackBehavior(AbstractEntityCompanion entity){
        runFor((e)-> entity instanceof IMeleeAttacker? ((IMeleeAttacker) entity).MeleeAttackAnimationLength():-1);
        if(entity instanceof IMeleeAttacker){
            this.addDelaysWithEntity((IMeleeAttacker) entity);
        }
    }

    private void addDelaysWithEntity(IMeleeAttacker entity){
        this.addDelays(entity.getAttackDamageDelay());
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, AbstractEntityCompanion entity) {

        if(!(entity instanceof IMeleeAttacker)){
            return false;
        }

        LivingEntity target = BrainUtils.getTargetOfEntity(entity);
        if(target == null){
            return false;
        }
        else if(entity.isSwimming() && entity.isOrderedToSit() && entity.isPassenger()){
            return false;
        }
        else if(!BehaviorUtils.isWithinAttackRange(entity, target, 0)){
            return false;
        }
        else return entity.wantsToAttack(target, entity);
    }

    @Override
    protected void start(ServerLevel level, AbstractEntityCompanion entity, long gameTime) {
        super.start(level, entity, gameTime);
        BrainUtils.setForgettableMemory(entity,RegisterAI.ANIMATION.get(), MELEE_ATTACK, ((IMeleeAttacker)entity).MeleeAttackAnimationLength());
    }

    @Override
    protected void doDelayedActions(AbstractEntityCompanion entity, int counter) {
        LivingEntity target = BrainUtils.getTargetOfEntity(entity);
        ((IMeleeAttacker) entity).PerformMeleeAttack(target, (float) entity.getAttribute(Attributes.ATTACK_DAMAGE).getValue(), counter);
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of(Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED), Pair.of(RegisterAI.ANIMATION.get(), MemoryStatus.VALUE_ABSENT));
    }
}
