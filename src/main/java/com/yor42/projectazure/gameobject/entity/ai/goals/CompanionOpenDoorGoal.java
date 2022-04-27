package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;

public class CompanionOpenDoorGoal extends OpenDoorGoal {
    private AbstractEntityCompanion entityin;
    private int animatingtimer;
    public CompanionOpenDoorGoal(AbstractEntityCompanion entitylivingIn, boolean shouldClose) {
        super(entitylivingIn, shouldClose);
        this.entityin = entitylivingIn;
    }

    @Override
    public boolean canUse() {
        if(this.entityin.isCriticallyInjured()){
            return false;
        }
        return super.canUse();
    }

    @Override
    public void start() {
        super.start();
        this.entityin.setOpeningdoor(true);
        this.animatingtimer = 12;
    }

    @Override
    public void tick() {
        this.animatingtimer--;
        if(this.animatingtimer == 0){
            this.entityin.setOpeningdoor(false);
        }
        super.tick();
    }
}
