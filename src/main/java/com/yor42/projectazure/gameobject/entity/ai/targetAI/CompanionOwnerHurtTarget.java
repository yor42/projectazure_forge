package com.yor42.projectazure.gameobject.entity.ai.targetAI;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;

public class CompanionOwnerHurtTarget extends OwnerHurtTargetGoal {

    AbstractEntityCompanion host;

    public CompanionOwnerHurtTarget(AbstractEntityCompanion entity) {
        super(entity);
        this.host = entity;
    }

    @Override
    public boolean canUse() {

        LivingEntity owner = this.host.getOwner();
        if(owner  != null) {
            LivingEntity targetEntity = owner.getLastHurtMob();
            if(targetEntity == null){
                return false;
            }
            if(targetEntity instanceof AbstractEntityCompanion){
                if((((AbstractEntityCompanion) targetEntity).getOwner() == owner) || !(this.host.isPVPenabled())){
                    return false;
                }
            }
        }

        return super.canUse();
    }
}
