package com.yor42.projectazure.gameobject.entity.ai.targetAI;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.OwnerHurtTargetGoal;

public class CompanionOwnerHurtTarget extends OwnerHurtTargetGoal {

    AbstractEntityCompanion host;

    public CompanionOwnerHurtTarget(AbstractEntityCompanion entity) {
        super(entity);
        this.host = entity;
    }

    @Override
    public boolean shouldExecute() {

        LivingEntity owner = this.host.getOwner();
        if(owner  != null) {
            LivingEntity targetEntity = owner.getLastAttackedEntity();
            if(targetEntity == null){
                return false;
            }
            if(targetEntity instanceof AbstractEntityCompanion){
                if((((AbstractEntityCompanion) targetEntity).getOwner() == owner) || !(this.host.isPVPenabled())){
                    return false;
                }
            }
        }

        return super.shouldExecute();
    }
}
