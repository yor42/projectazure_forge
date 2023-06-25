package com.yor42.projectazure.gameobject.entity.ai.behaviors;

import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRetaliateTarget;

import javax.annotation.Nullable;
import java.util.List;

public class CompanionProtectOwnerBehavior extends SetRetaliateTarget<AbstractEntityCompanion> {

    private boolean onlyWhenVisible = false;
    private int owner_hurt_by_entity_timestamp;
    private int owner_last_hurt_timestamp;

    public CompanionProtectOwnerBehavior(){
        alertAlliesWhen((owner, tgt)-> tgt instanceof LivingEntity && owner.isAlly((LivingEntity) tgt));
    }

    public CompanionProtectOwnerBehavior onlyWhenVisible(){
        this.onlyWhenVisible = true;
        return this;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of(Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, AbstractEntityCompanion entity) {
        LivingEntity owner = entity.getOwner();
        if (owner == null) {
            return false;
        }

        if (this.onlyWhenVisible && !entity.getSensing().hasLineOfSight(owner)) {
            return false;
        }

        LivingEntity ownerLastHurtBy = owner.getLastHurtByMob();
        int i = owner.getLastHurtByMobTimestamp();

        LivingEntity ownerLastHurt = owner.getLastHurtMob();
        int j = owner.getLastHurtMobTimestamp();

        if (i != this.owner_hurt_by_entity_timestamp && ownerLastHurtBy != null &&entity.wantsToAttack(ownerLastHurtBy, owner)) {
            this.toTarget = ownerLastHurtBy;
            doalert(entity);
            return true;
        }

        if (j != this.owner_last_hurt_timestamp && ownerLastHurt != null && entity.wantsToAttack(ownerLastHurt, owner) && !this.onlyWhenVisible) {
            this.toTarget = ownerLastHurt;
            doalert(entity);
            return true;
        }
        return false;
    }

    private void doalert(AbstractEntityCompanion entity){
        Level level = entity.getLevel();
        if (this.toTarget.isAlive() && this.toTarget.level == level && this.canAttackPredicate.test(this.toTarget)) {
            if (this.alertAlliesPredicate.test(entity, this.toTarget))
                alertAllies((ServerLevel) level, entity);
        }
    }

    @Override
    protected void start(AbstractEntityCompanion entity) {
        LivingEntity owner = entity.getOwner();
        if (owner != null) {
            this.owner_last_hurt_timestamp = owner.getLastHurtMobTimestamp();
            this.owner_hurt_by_entity_timestamp = owner.getLastHurtByMobTimestamp();
        }
        super.start(entity);
    }
}
