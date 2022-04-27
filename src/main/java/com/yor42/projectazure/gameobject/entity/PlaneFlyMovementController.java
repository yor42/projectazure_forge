package com.yor42.projectazure.gameobject.entity;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.MoveControl;

public class PlaneFlyMovementController extends FlyingMoveControl {
    private final int maxTurn;
    public PlaneFlyMovementController(Mob p_i225710_1_, int p_i225710_2_, boolean p_i225710_3_) {
        super(p_i225710_1_, p_i225710_2_, p_i225710_3_);
        this.maxTurn = p_i225710_2_;
    }

    public void tick() {
        this.mob.setNoGravity(this.mob.isAlive());
        if (this.operation == MoveControl.Operation.MOVE_TO) {
            this.operation = MoveControl.Operation.WAIT;
            double d0 = this.wantedX - this.mob.getX();
            double d1 = this.wantedY - this.mob.getY();
            double d2 = this.wantedZ - this.mob.getZ();

            float f = (float)(Math.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), f, 90.0F));
            float f1 = (float)(this.speedModifier * this.mob.getAttributeValue(Attributes.FLYING_SPEED));

            this.mob.setSpeed(f1);
            double d4 = (double) Mth.sqrt((float) (d0 * d0 + d2 * d2));
            float f2 = (float)(-(Mth.atan2(d1, d4) * (double)(180F / (float)Math.PI)));
            this.mob.setXRot(this.rotlerp(this.mob.getXRot(), f2, (float)this.maxTurn));
            this.mob.setYya(d1 > 0.0D ? f1 : -f1);
        } else {

            this.mob.setYya(0.0F);
            this.mob.setZza(0.0F);
        }

    }

}
