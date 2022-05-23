package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.gameobject.entity.ai.CompanionTasks.setAttackTargetIfCloserThanCurrent;

public class CompanionProtectOwnerTask extends Task<AbstractEntityCompanion> {

    private int owner_hurt_by_entity_timestamp;
    private LivingEntity tgt;
    private final boolean onlyWhenVisible;
    private int owner_last_hurt_timestamp;

    public CompanionProtectOwnerTask(boolean onlyWhenVisible) {
        super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryModuleStatus.VALUE_ABSENT));
        this.onlyWhenVisible = onlyWhenVisible;

    }

    @Override
    protected boolean checkExtraStartConditions(@Nonnull ServerWorld world, @Nonnull AbstractEntityCompanion entity) {
        LivingEntity owner = entity.getOwner();
        if(owner!=null){

            if(this.onlyWhenVisible && !entity.getSensing().canSee(owner)){
                return false;
            }

            LivingEntity ownerLastHurtBy = owner.getLastHurtByMob();
            int i = owner.getLastHurtByMobTimestamp();

            LivingEntity ownerLastHurt = owner.getLastHurtMob();
            int j = owner.getLastHurtMobTimestamp();

            if(i != this.owner_hurt_by_entity_timestamp && entity.wantsToAttack(ownerLastHurtBy, owner)){
                this.tgt = ownerLastHurtBy;
                return true;
            }
            else if(j != this.owner_last_hurt_timestamp && entity.wantsToAttack(ownerLastHurt, owner)&& !this.onlyWhenVisible){
                this.tgt = ownerLastHurt;
                return true;
            }
        }
        return false;
    }

    @Override
    protected void start(ServerWorld p_212831_1_, AbstractEntityCompanion entity, long p_212831_3_) {
        if(this.tgt != null) {
            setAttackTargetIfCloserThanCurrent(entity, this.tgt);
            LivingEntity owner = entity.getOwner();
            if (owner != null) {
                this.owner_last_hurt_timestamp = owner.getLastHurtMobTimestamp();
                this.owner_hurt_by_entity_timestamp = owner.getLastHurtByMobTimestamp();
            }
        }

    }
}
