package com.yor42.projectazure.gameobject.entity;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.util.math.MathHelper;

public class PlaneFlyMovementController extends FlyingMovementController {
    private final int field_226323_i_;
    public PlaneFlyMovementController(MobEntity p_i225710_1_, int p_i225710_2_, boolean p_i225710_3_) {
        super(p_i225710_1_, p_i225710_2_, p_i225710_3_);
        this.field_226323_i_ = p_i225710_2_;
    }

    public void tick() {
        this.mob.setNoGravity(this.mob.isAlive());
        if (this.action == MovementController.Action.MOVE_TO) {
            this.action = MovementController.Action.WAIT;
            double d0 = this.posX - this.mob.getPosX();
            double d1 = this.posY - this.mob.getPosY();
            double d2 = this.posZ - this.mob.getPosZ();

            float f = (float)(MathHelper.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
            this.mob.rotationYaw = this.limitAngle(this.mob.rotationYaw, f, 90.0F);
            float f1 = (float)(this.speed * this.mob.getAttributeValue(Attributes.FLYING_SPEED));

            this.mob.setAIMoveSpeed(f1);
            double d4 = (double)MathHelper.sqrt(d0 * d0 + d2 * d2);
            float f2 = (float)(-(MathHelper.atan2(d1, d4) * (double)(180F / (float)Math.PI)));
            this.mob.rotationPitch = this.limitAngle(this.mob.rotationPitch, f2, (float)this.field_226323_i_);
            this.mob.setMoveVertical(d1 > 0.0D ? f1 : -f1);
        } else {

            this.mob.setMoveVertical(0.0F);
            this.mob.setMoveForward(0.0F);
        }

    }

}
