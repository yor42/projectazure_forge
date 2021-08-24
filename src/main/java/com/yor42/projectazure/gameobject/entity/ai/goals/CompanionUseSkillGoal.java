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
    public boolean shouldExecute() {
        return this.companion.canUseSkill() && this.companion.getSkillDelayTick() == 0 && !this.isSkillFinished && this.companion.getAttackTarget() != null && this.companion.getAttackTarget().isAlive();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.companion.canUseSkill() && this.companion.getSkillDelayTick() == 0 && !this.isSkillFinished;
    }

    @Override
    public void startExecuting() {
        if(this.companion.getAttackTarget() != null && !this.isSkillFinished) {
            if(this.companion.performOneTimeSkill(this.companion.getAttackTarget())){
                this.isSkillFinished = true;
            }
        }
    }

    @Override
    public void tick() {
        if(this.companion.getAttackTarget() != null && !this.isSkillFinished) {
            if(this.companion.performSkillTick(this.companion.getAttackTarget(), this.SkillTimer)){
                this.isSkillFinished = true;
            }
            this.SkillTimer++;
        }
    }

    @Override
    public void resetTask() {
        this.companion.resetSkill();
        this.isSkillFinished = false;
        this.companion.setSkillDelay();
        this.SkillTimer = 0;
    }
}
