package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.magicuser.ISpellUser;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.memory.WalkTarget;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityPosWrapper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;

public class CompanionMovetoTargetTask extends Task<AbstractEntityCompanion> {
    private final float speedModifier;
    private final AbstractEntityCompanion entity;
    private float closeEnoughDist = 0;
    public CompanionMovetoTargetTask(AbstractEntityCompanion entity, float speed) {
        super(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleStatus.REGISTERED, MemoryModuleType.LOOK_TARGET, MemoryModuleStatus.REGISTERED, MemoryModuleType.ATTACK_TARGET, MemoryModuleStatus.VALUE_PRESENT, MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryModuleStatus.REGISTERED));
        this.speedModifier = speed;
        this.entity = entity;
    }

    protected void start(ServerWorld p_212831_1_, MobEntity p_212831_2_, long p_212831_3_) {
        LivingEntity livingentity = p_212831_2_.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get();
        if (BrainUtil.canSee(p_212831_2_, livingentity) && (BrainUtil.isWithinAttackRange(p_212831_2_, livingentity, 1)||livingentity.closerThan(this.entity, closeEnoughDist))) {
            this.clearWalkTarget(p_212831_2_);
        } else {
            this.setWalkAndLookTarget(p_212831_2_, livingentity);
        }

    }

    @Override
    protected void stop(ServerWorld p_212835_1_, AbstractEntityCompanion p_212835_2_, long p_212835_3_) {
        super.stop(p_212835_1_, p_212835_2_, p_212835_3_);
        this.closeEnoughDist = 0;
    }

    private void setWalkAndLookTarget(LivingEntity p_233968_1_, LivingEntity p_233968_2_) {

        if(this.entity instanceof ISpellUser && ((ISpellUser) this.entity).shouldUseSpell()){
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
        brain.setMemory(MemoryModuleType.LOOK_TARGET, new EntityPosWrapper(p_233968_2_, true));
        WalkTarget walktarget = new WalkTarget(new EntityPosWrapper(p_233968_2_, false), this.speedModifier, (int) this.closeEnoughDist);
        brain.setMemory(MemoryModuleType.WALK_TARGET, walktarget);
    }

    private void clearWalkTarget(LivingEntity p_233967_1_) {
        p_233967_1_.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
    }
}
