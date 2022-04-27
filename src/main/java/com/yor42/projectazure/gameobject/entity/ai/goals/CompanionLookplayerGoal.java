package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;

public class CompanionLookplayerGoal extends LookAtPlayerGoal {
    private final AbstractEntityCompanion entity;
    public CompanionLookplayerGoal(AbstractEntityCompanion p_i1631_1_) {
        super(p_i1631_1_, Player.class, 8.0F);
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
