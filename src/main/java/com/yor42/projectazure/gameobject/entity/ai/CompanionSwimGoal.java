package com.yor42.projectazure.gameobject.entity.ai;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityKansenBase;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.tags.FluidTags;

public class CompanionSwimGoal extends SwimGoal {

    private final AbstractEntityCompanion entity;
    private final boolean hasRigging;

    public CompanionSwimGoal(AbstractEntityCompanion entityIn) {
        super(entityIn);
        this.entity = entityIn;
        this.hasRigging = entityIn instanceof EntityKansenBase && ((EntityKansenBase) entityIn).hasRigging();
    }
    public boolean shouldExecute() {
        return !this.hasRigging && this.entity.isInWater() && this.entity.getOwner()!= null && this.entity.getOwner().getPosY()-this.entity.getPosY() >=0.5 && !this.entity.getOwner().isInWater()|| this.entity.isInLava();
    }

}
