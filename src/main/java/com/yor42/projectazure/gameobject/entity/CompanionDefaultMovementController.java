package com.yor42.projectazure.gameobject.entity;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.Vec3;

public class CompanionDefaultMovementController extends MoveControl {
    protected final AbstractEntityCompanion companion;
    public CompanionDefaultMovementController(AbstractEntityCompanion entity) {
        super(entity);
        this.companion = entity;
    }

    @Override
    public void tick() {
        float lvt_9_2_;
        if (this.operation == MoveControl.Operation.STRAFE) {
            float lvt_1_1_ = (float)this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
            float lvt_2_1_ = (float)this.speedModifier * lvt_1_1_;
            float lvt_3_1_ = this.strafeForwards;
            float lvt_4_1_ = this.strafeRight;
            float lvt_5_1_ = Mth.sqrt(lvt_3_1_ * lvt_3_1_ + lvt_4_1_ * lvt_4_1_);
            if (lvt_5_1_ < 1.0F) {
                lvt_5_1_ = 1.0F;
            }

            lvt_5_1_ = lvt_2_1_ / lvt_5_1_;
            lvt_3_1_ *= lvt_5_1_;
            lvt_4_1_ *= lvt_5_1_;
            float lvt_6_1_ = Mth.sin(this.mob.yRot * 0.017453292F);
            float lvt_7_1_ = Mth.cos(this.mob.yRot * 0.017453292F);
            float lvt_8_1_ = lvt_3_1_ * lvt_7_1_ - lvt_4_1_ * lvt_6_1_;
            lvt_9_2_ = lvt_4_1_ * lvt_7_1_ + lvt_3_1_ * lvt_6_1_;
            if (!this.isWalkable(lvt_8_1_, lvt_9_2_)) {
                this.strafeForwards = 1.0F;
                this.strafeRight = 0.0F;
            }
            this.mob.setSpeed(lvt_2_1_);
            this.mob.setZza(this.strafeForwards);
            this.mob.setXxa(this.strafeRight);
            this.operation = MoveControl.Operation.WAIT;
        }
        else if(this.mob.onClimbable() && !this.mob.isOnGround()){
            double xDelta = this.wantedX - this.mob.getX();
            double ZDelta = this.wantedZ - this.mob.getZ();
            double YDelta = this.wantedY - this.mob.getY();
            Vec3 deltaMovement = mob.getDeltaMovement();
            double dx = deltaMovement.x;
            double dy = deltaMovement.y;
            double dz = deltaMovement.z;
            if(YDelta >=0){
                this.mob.getJumpControl().jump();
            }
            else{
                dy = Mth.clamp(0, dy, -0.3);
                this.mob.fallDistance = 0;
            }

            Vec3 newDelta = new Vec3(Math.abs(YDelta)<0.25?dx:Mth.clamp(0.03, xDelta, -0.03), dy, Math.abs(YDelta)<0.25?dz:Mth.clamp(0.03, ZDelta, -0.03));
            this.mob.setDeltaMovement(newDelta);
        }
        else if (this.operation == MoveControl.Operation.MOVE_TO) {
            this.operation = MoveControl.Operation.WAIT;
            double xDelta = this.wantedX - this.mob.getX();
            double ZDelta = this.wantedZ - this.mob.getZ();
            double YDelta = this.wantedY - this.mob.getY();
            double DistanceSquared = xDelta * xDelta + YDelta * YDelta + ZDelta * ZDelta;
            if (DistanceSquared < 2.500000277905201E-7D) {
                this.mob.setZza(0.0F);
                return;
            }

            lvt_9_2_ = (float)(Mth.atan2(ZDelta, xDelta) * 57.2957763671875D) - 90.0F;
            this.mob.yRot = this.rotlerp(this.mob.yRot, lvt_9_2_, 90.0F);
            this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
            BlockPos blockPos = this.mob.blockPosition();
            BlockState blockState = this.mob.level.getBlockState(blockPos);
            Block block = blockState.getBlock();
            VoxelShape voxel = blockState.getCollisionShape(this.mob.level, blockPos);
            if (YDelta > (double)this.mob.maxUpStep && xDelta * xDelta + ZDelta * ZDelta < (double)Math.max(1.0F, this.mob.getBbWidth()) || !voxel.isEmpty() && this.mob.getY() < voxel.max(Direction.Axis.Y) + (double)blockPos.getY() && !this.companion.isSailing() && !block.is(BlockTags.DOORS) && !block.is(BlockTags.FENCES)) {
                this.mob.getJumpControl().jump();
                this.operation = MoveControl.Operation.JUMPING;
            }
        } else if (this.operation == MoveControl.Operation.JUMPING) {
            this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
            if (this.mob.isOnGround()) {
                this.operation = MoveControl.Operation.WAIT;
            }
        } else {
            this.mob.setZza(0.0F);
        }
    }

    private boolean isWalkable(float p_234024_1_, float p_234024_2_) {
        PathNavigation navigator = this.mob.getNavigation();
        NodeEvaluator nodeProcessor = navigator.getNodeEvaluator();
        return nodeProcessor.getBlockPathType(this.mob.level, Mth.floor(this.mob.getX() + (double) p_234024_1_), Mth.floor(this.mob.getY()), Mth.floor(this.mob.getZ() + (double) p_234024_2_)) == BlockPathTypes.WALKABLE;
    }
}
