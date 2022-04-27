package com.yor42.projectazure.gameobject.entity;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeMod;

import static net.minecraft.tags.FluidTags.WATER;

public class CompanionSwimPathFinder extends MoveControl {

    AbstractEntityCompanion companion;

    public CompanionSwimPathFinder(AbstractEntityCompanion mob) {
        super(mob);
        this.companion = mob;
    }

    public void tick() {
        if(this.companion.isInWater() || this.companion.isInLava())
        {
            BlockPos blockpos = this.mob.blockPosition();
            BlockState blockstate = this.mob.level.getBlockState(blockpos);
            LivingEntity livingentity = this.companion.getTarget();

            double d0 = this.wantedX - this.mob.getX();
            double d1 = this.wantedZ - this.mob.getZ();
            double d2 = this.wantedY - this.mob.getY();
            VoxelShape voxelshape = blockstate.getCollisionShape(this.mob.level, blockpos);

            boolean obstructed = d0 * d0 + d1 * d1 < (double)Math.max(1.0F, this.mob.getBbWidth()) || !voxelshape.isEmpty() && this.mob.getY() < voxelshape.max(Direction.Axis.Y) + (double)blockpos.getY();

            boolean isOwnerHigherinWater = this.companion.getOwner() != null && this.companion.getOwner().isEyeInFluid(WATER) && (this.companion.getOwner().getY() > this.companion.getY());
            boolean isTargetHigher = livingentity != null && livingentity.isInWater() && livingentity.getY() > this.companion.getY();
            double f = this.companion.getEyeHeight()-0.2;
            double waterheight = this.companion.getFluidHeight(WATER);
            boolean shouldswim;
            if(this.companion.getOwner()!= null){
                shouldswim= waterheight > f && !this.companion.getOwner().isEyeInFluid(WATER);
            }else {
                shouldswim = waterheight > f;
            }
            boolean isJumping = this.operation == Operation.JUMPING;
            if((shouldswim || this.companion.isInLava()) || isTargetHigher || this.companion.isSwimmingUp() || obstructed || isOwnerHigherinWater || isJumping || this.companion.horizontalCollision && !this.companion.canUseRigging()){
                Vec3 vec3d = this.companion.getDeltaMovement();
                this.companion.setDeltaMovement(vec3d.x, vec3d.y + (double)(0.0175F), vec3d.z);
            }
        }

        boolean isMoving = this.operation == MoveControl.Operation.MOVE_TO;
        boolean haspath = !this.companion.getNavigation().isDone();

        if(isMoving && haspath)
        {
            double d0 = this.wantedX - this.companion.getX();
            double d1 = this.wantedY - this.companion.getY();
            double d2 = this.wantedZ - this.companion.getZ();
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;
            if(d3 < (double)2.5000003E-7F)
            {
                this.mob.setZza(0.0F);
            }
            else
            {
                float f = (float)(Mth.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
                this.companion.setYRot(this.rotlerp(this.companion.getYRot(), f, 10.0F));
                this.companion.yBodyRot = this.companion.getYRot();
                this.companion.yHeadRot = this.companion.getYRot();
                float f1 = (float)(this.speedModifier * this.companion.getAttributeValue(ForgeMod.SWIM_SPEED.get()));
                if(this.companion.isInWater() || this.companion.isInLava())
                {
                    this.companion.setSpeed(f1);
                    float f2 = -((float)(Mth.atan2(d1, (double)Mth.sqrt((float) (d0 * d0 + d2 * d2))) * (double)(180F / (float)Math.PI)));
                    f2 = Mth.clamp(Mth.wrapDegrees(f2), -85.0F, 85.0F);
                    this.companion.setXRot(this.rotlerp(this.companion.getXRot(), f2, 5.0F));
                    float f3 = Mth.cos(this.companion.getXRot() * ((float)Math.PI / 180F));
                    float f4 = Mth.sin(this.companion.getXRot() * ((float)Math.PI / 180F));
                    this.companion.zza = f3 * f1;
                    //this.companion.moveVertical = -f4 * f1;
                }
                else
                {
                    this.companion.setSpeed(f1);
                }
            }
        }
        else
        {
            this.companion.setSpeed(0.0F);
            this.companion.setXxa(0.0F);
            this.companion.setYya(0.0F);
            this.companion.setZza(0.0F);
        }
    }
}
