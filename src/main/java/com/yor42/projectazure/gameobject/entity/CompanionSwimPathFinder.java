package com.yor42.projectazure.gameobject.entity;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.ForgeMod;

public class CompanionSwimPathFinder extends MovementController {

    AbstractEntityCompanion companion;

    public CompanionSwimPathFinder(AbstractEntityCompanion mob) {
        super(mob);
        this.companion = mob;
    }

    public void tick() {
        if(this.companion.isInWater() || this.companion.isInLava())
        {

            BlockPos blockpos = new BlockPos(this.companion.getPosX(), this.companion.getPosY(), this.companion.getPosZ());
            BlockState blockstate = this.companion.world.getBlockState(blockpos.up());
            LivingEntity livingentity = this.companion.getAttackTarget();

            if((this.companion.isInWater() || this.companion.isInLava()) && ((livingentity != null && livingentity.getPosY() > this.companion.getPosY()) || this.companion.isSwimmingUp() || (this.companion.getOwner() != null && this.companion.getOwner().getPosY()+1 > this.companion.getPosY())) && !this.companion.canUseRigging()){
                double f = this.companion.getEyeHeight()-2;
                double waterheight = this.companion.func_233571_b_(FluidTags.WATER);
                 if(waterheight > f) {
                     Vector3d vec3d = this.companion.getMotion();
                    this.companion.setMotion(vec3d.x, vec3d.y + (double)(0.0075F), vec3d.z);
                 }
            }
        }

        boolean isMoving = this.action == MovementController.Action.MOVE_TO;
        boolean haspath = !this.companion.getNavigator().noPath();

        if(isMoving && haspath)
        {
            double d0 = this.posX - this.companion.getPosX();
            double d1 = this.posY - this.companion.getPosY();
            double d2 = this.posZ - this.companion.getPosZ();
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;
            if(d3 < (double)2.5000003E-7F)
            {
                this.mob.setMoveForward(0.0F);
            }
            else
            {
                float f = (float)(MathHelper.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
                this.companion.rotationYaw = this.limitAngle(this.companion.rotationYaw, f, 10.0F);
                this.companion.renderYawOffset = this.companion.rotationYaw;
                this.companion.rotationYawHead = this.companion.rotationYaw;
                float f1 = (float)(this.speed * this.companion.getAttributeValue(ForgeMod.SWIM_SPEED.get()));
                if(this.companion.isInWater() || this.companion.isInLava())
                {
                    this.companion.setAIMoveSpeed(f1);
                    float f2 = -((float)(MathHelper.atan2(d1, (double)MathHelper.sqrt(d0 * d0 + d2 * d2)) * (double)(180F / (float)Math.PI)));
                    f2 = MathHelper.clamp(MathHelper.wrapDegrees(f2), -85.0F, 85.0F);
                    this.companion.rotationPitch = this.limitAngle(this.companion.rotationPitch, f2, 5.0F);
                    float f3 = MathHelper.cos(this.companion.rotationPitch * ((float)Math.PI / 180F));
                    float f4 = MathHelper.sin(this.companion.rotationPitch * ((float)Math.PI / 180F));
                    this.companion.moveForward = f3 * f1;
                    //this.companion.moveVertical = -f4 * f1;
                }
                else
                {
                    this.companion.setAIMoveSpeed(f1);
                }
            }
        }
        else
        {
            this.companion.setAIMoveSpeed(0.0F);
            this.companion.setMoveStrafing(0.0F);
            this.companion.setMoveVertical(0.0F);
            this.companion.setMoveForward(0.0F);
        }
    }
}
