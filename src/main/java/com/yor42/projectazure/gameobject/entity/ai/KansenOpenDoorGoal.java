package com.yor42.projectazure.gameobject.entity.ai;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityKansenBase;
import net.minecraft.entity.ai.goal.OpenDoorGoal;

public class KansenOpenDoorGoal extends OpenDoorGoal {
    private AbstractEntityCompanion entityin;
    private int animatingtimer;
    public KansenOpenDoorGoal(AbstractEntityCompanion entitylivingIn, boolean shouldClose) {
        super(entitylivingIn, shouldClose);
        this.entityin = entitylivingIn;
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
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
