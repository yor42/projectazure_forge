package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.interfaces.ISpellUser;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.server.level.ServerLevel;

public class CompanionMovetoTargetTask extends Behavior<AbstractEntityCompanion> {
    private final float speedModifier;
    private final AbstractEntityCompanion entity;
    private float closeEnoughDist = 0;
    public CompanionMovetoTargetTask(AbstractEntityCompanion entity, float speed) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryStatus.REGISTERED));
        this.speedModifier = speed;
        this.entity = entity;
    }

    protected void start(ServerLevel p_212831_1_, AbstractEntityCompanion p_212831_2_, long p_212831_3_) {
        LivingEntity livingentity = p_212831_2_.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
        if (BehaviorUtils.canSee(p_212831_2_, livingentity) && (BehaviorUtils.isWithinAttackRange(p_212831_2_, livingentity, 1)||livingentity.closerThan(this.entity, closeEnoughDist))) {
            this.clearWalkTarget(p_212831_2_);
        } else {
            this.setWalkAndLookTarget(p_212831_2_, livingentity);
        }

    }

    @Override
    protected void stop(ServerLevel p_212835_1_, AbstractEntityCompanion p_212835_2_, long p_212835_3_) {
        super.stop(p_212835_1_, p_212835_2_, p_212835_3_);
        this.closeEnoughDist = 0;
    }

    private void setWalkAndLookTarget(LivingEntity p_233968_1_, LivingEntity p_233968_2_) {

        if(this.entity instanceof ISpellUser && ((ISpellUser) this.entity).shouldUseSpell(p_233968_2_)){
            this.closeEnoughDist = this.entity.getSpellRange();
        }
        else if(this.entity.canUseCannonOrTorpedo()){
            this.closeEnoughDist = this.entity.getCannonRange();
        }
        else if(entity.shouldUseGun()){
            this.closeEnoughDist = this.entity.getGunRange();
        }
        this.closeEnoughDist-=2;


        Brain<?> brain = p_233968_1_.getBrain();
        brain.setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(p_233968_2_, true));
        WalkTarget walktarget = new WalkTarget(new EntityTracker(p_233968_2_, false), this.speedModifier, (int) this.closeEnoughDist);
        brain.setMemory(MemoryModuleType.WALK_TARGET, walktarget);
    }

    private void clearWalkTarget(LivingEntity p_233967_1_) {
        p_233967_1_.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
    }
}
