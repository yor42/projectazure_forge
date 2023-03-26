package com.yor42.projectazure.gameobject.entity.ai.targetAI;

import com.yor42.projectazure.gameobject.entity.misc.AbstractEntityFollowingDrone;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;

import java.util.EnumSet;

import net.minecraft.world.entity.ai.goal.Goal.Flag;

public class DroneOwnerAttackedTargetGoal extends TargetGoal {
    private final AbstractEntityFollowingDrone Drone;
    private LivingEntity attacker;
    private int timestamp;

    public DroneOwnerAttackedTargetGoal(AbstractEntityFollowingDrone p_i1668_1_) {
        super(p_i1668_1_, false);
        this.Drone = p_i1668_1_;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    public boolean canUse() {
        if (this.Drone.getOwner().isPresent()) {
            LivingEntity lvt_1_1_ = (LivingEntity) this.Drone.getOwner().get();
            this.attacker = lvt_1_1_.getLastHurtMob();
            int lvt_2_1_ = lvt_1_1_.getLastHurtMobTimestamp();
            return lvt_2_1_ != this.timestamp && this.canAttack(this.attacker, TargetingConditions.DEFAULT) && this.Drone.shouldAttackEntity(this.attacker, lvt_1_1_);
        } else {
            return false;
        }
    }

    public void start() {
        this.Drone.setTarget(this.attacker);
        if (this.Drone.getOwner().isPresent()) {
            Entity owner = this.Drone.getOwner().get();
            if(owner instanceof LivingEntity) {
                this.timestamp = ((LivingEntity)this.Drone.getOwner().get()).getLastHurtMobTimestamp();
            }
        }

        super.start();
    }

}
