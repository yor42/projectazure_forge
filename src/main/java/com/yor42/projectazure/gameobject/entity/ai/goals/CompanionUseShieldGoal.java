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

import java.util.EnumSet;

public class CompanionUseShieldGoal extends Goal {

    private final AbstractEntityCompanion companion;

    public CompanionUseShieldGoal(AbstractEntityCompanion companion){
        this.companion = companion;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return companion.getOffhandItem().getItem().isShield(companion.getOffhandItem(), companion) && raiseShield() && companion.getShieldCoolDown() == 0;
    }

    @Override
    public boolean canContinueToUse() {

        boolean bool = this.canUse();

        if(!bool){
            this.stop();
        }

        return bool;
    }

    @Override
    public void start() {
        if (companion.getOffhandItem().getItem().isShield(companion.getOffhandItem(), companion))
            companion.startUsingItem(Hand.OFF_HAND);
    }

    @Override
    public void stop() {
        companion.releaseUsingItem();
    }

    protected boolean raiseShield() {
        LivingEntity target = companion.getTarget();

        if(this.companion.isSailing()){
            return false;
        }

        if (target != null && companion.getShieldCoolDown() == 0) {
            boolean ranged = companion.getMainHandItem().getItem() instanceof CrossbowItem || companion.getMainHandItem().getItem() instanceof BowItem || companion.getMainHandItem().getItem() instanceof ItemGunBase;
            return companion.distanceTo(target) <= 4.0D || target instanceof CreeperEntity || target instanceof IRangedAttackMob && target.distanceTo(companion) >= 5.0D && !ranged || target instanceof RavagerEntity;
        }
        return false;
    }
}
