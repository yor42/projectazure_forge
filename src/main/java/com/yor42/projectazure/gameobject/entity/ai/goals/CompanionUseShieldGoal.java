package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;

import java.util.EnumSet;

public class CompanionUseShieldGoal extends Goal {

    private final AbstractEntityCompanion companion;

    public CompanionUseShieldGoal(AbstractEntityCompanion companion){
        this.companion = companion;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {

        if(this.companion.isCriticallyInjured()){
            return false;
        }

        return companion.getOffhandItem().getItem().canPerformAction(companion.getOffhandItem(), net.minecraftforge.common.ToolActions.SHIELD_BLOCK) && raiseShield() && companion.getShieldCoolDown() == 0;
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
        if (companion.getOffhandItem().getItem().canPerformAction(companion.getOffhandItem(), net.minecraftforge.common.ToolActions.SHIELD_BLOCK))
            companion.startUsingItem(InteractionHand.OFF_HAND);
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
            return companion.distanceTo(target) <= 4.0D || target instanceof Creeper || target instanceof RangedAttackMob && target.distanceTo(companion) >= 5.0D && !ranged || target instanceof Ravager;
        }
        return false;
    }
}
