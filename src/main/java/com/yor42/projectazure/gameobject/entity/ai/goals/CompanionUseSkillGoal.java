package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.entity.ai.goal.Goal;

public class CompanionUseSkillGoal extends Goal {

    private final AbstractEntityCompanion companion;
    private int SkillTimer;
    private boolean isSkillFinished;

    public CompanionUseSkillGoal(AbstractEntityCompanion entity){
        this.companion = entity;
        this.SkillTimer = 0;
    }

    @Override
    public boolean canUse() {

        if(this.companion.isCriticallyInjured()){
            return false;
        }

        return this.companion.canUseSkill() && this.companion.getSkillDelayTick() == 0 && !this.isSkillFinished && this.companion.getTarget() != null && this.companion.getTarget().isAlive();
    }

    @Override
    public boolean canContinueToUse() {
        return this.companion.canUseSkill() && this.companion.getSkillDelayTick() == 0 && !this.isSkillFinished;
    }

    @Override
    public void start() {
        if(this.companion.getTarget() != null && !this.isSkillFinished) {
            this.companion.setUsingSkill(false);
            if(this.companion.performOneTimeSkill(this.companion.getTarget())){
                this.isSkillFinished = true;
            }
        }
    }

    @Override
    public void tick() {
        if(this.companion.getTarget() != null && !this.isSkillFinished) {
            if(this.companion.performSkillTick(this.companion.getTarget(), this.SkillTimer)){
                this.isSkillFinished = true;
            }
            this.SkillTimer++;
        }
    }

    @Override
    public void stop() {
        this.companion.resetSkill();
        this.isSkillFinished = false;
        this.companion.setSkillDelay();
        this.SkillTimer = 0;
    }
}
