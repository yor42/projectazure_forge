package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenBase;
import net.minecraft.entity.ai.goal.SwimGoal;

public class CompanionSwimGoal extends SwimGoal {

    private final AbstractEntityCompanion entity;
    private final boolean hasRigging;

    public CompanionSwimGoal(AbstractEntityCompanion entityIn) {
        super(entityIn);
        this.entity = entityIn;
        this.hasRigging = entityIn instanceof EntityKansenBase && ((EntityKansenBase) entityIn).hasRigging();
    }
    public boolean canUse() {
        return !this.hasRigging && this.entity.isInWater() && this.entity.getOwner()!= null && this.entity.getOwner().getY()-this.entity.getY() >=0.5 && !this.entity.getOwner().isInWater()|| this.entity.isInLava();
    }

}
