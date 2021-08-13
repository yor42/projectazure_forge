package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.RavagerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.util.Hand;

public class CompanionUseShieldGoal extends Goal {

    private final AbstractEntityCompanion companion;

    public CompanionUseShieldGoal(AbstractEntityCompanion companion){
        this.companion = companion;
    }

    @Override
    public boolean shouldExecute() {
        return companion.getHeldItemOffhand().getItem().isShield(companion.getHeldItemOffhand(), companion) && raiseShield() && companion.getShieldCoolDown() == 0;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.shouldExecute();
    }

    @Override
    public void startExecuting() {
        if (companion.getHeldItemOffhand().getItem().isShield(companion.getHeldItemOffhand(), companion))
            companion.setActiveHand(Hand.OFF_HAND);
    }

    @Override
    public void resetTask() {
        companion.stopActiveHand();
    }

    protected boolean raiseShield() {
        LivingEntity target = companion.getAttackTarget();
        if (target != null && companion.getShieldCoolDown() == 0) {
            boolean ranged = companion.getHeldItemMainhand().getItem() instanceof CrossbowItem || companion.getHeldItemMainhand().getItem() instanceof BowItem || companion.getHeldItemMainhand().getItem() instanceof ItemGunBase;
            return companion.getDistance(target) <= 4.0D || target instanceof CreeperEntity || target instanceof IRangedAttackMob && target.getDistance(companion) >= 5.0D && !ranged || target instanceof RavagerEntity;
        }
        return false;
    }
}
