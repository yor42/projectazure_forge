package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.player.PlayerEntity;

public class CompanionLookplayerGoal extends LookAtGoal {
    private final AbstractEntityCompanion entity;
    public CompanionLookplayerGoal(AbstractEntityCompanion p_i1631_1_) {
        super(p_i1631_1_, PlayerEntity.class, 8.0F);
        this.entity = p_i1631_1_;
    }

    @Override
    public boolean canUse() {

        if(this.entity.getOwner() != null && this.entity.getVehicle() == this.entity.getOwner() || this.entity.isCriticallyInjured()){
            return false;
        }

        return super.canUse();
    }
}
