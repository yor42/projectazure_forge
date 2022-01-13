package com.yor42.projectazure.gameobject.entity;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;

public class CompanionDefaultMovementController extends MovementController {
    protected final AbstractEntityCompanion companion;
    public CompanionDefaultMovementController(AbstractEntityCompanion entity) {
        super(entity);
        this.companion = entity;
    }

    @Override
    public void tick() {
        float lvt_9_2_;
        if (this.operation == MovementController.Action.STRAFE) {
            float lvt_1_1_ = (float)this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
            float lvt_2_1_ = (float)this.speedModifier * lvt_1_1_;
            float lvt_3_1_ = this.strafeForwards;
            float lvt_4_1_ = this.strafeRight;
            float lvt_5_1_ = MathHelper.sqrt(lvt_3_1_ * lvt_3_1_ + lvt_4_1_ * lvt_4_1_);
            if (lvt_5_1_ < 1.0F) {
                lvt_5_1_ = 1.0F;
            }

            lvt_5_1_ = lvt_2_1_ / lvt_5_1_;
            lvt_3_1_ *= lvt_5_1_;
            lvt_4_1_ *= lvt_5_1_;
            float lvt_6_1_ = MathHelper.sin(this.mob.yRot * 0.017453292F);
            float lvt_7_1_ = MathHelper.cos(this.mob.yRot * 0.017453292F);
            float lvt_8_1_ = lvt_3_1_ * lvt_7_1_ - lvt_4_1_ * lvt_6_1_;
            lvt_9_2_ = lvt_4_1_ * lvt_7_1_ + lvt_3_1_ * lvt_6_1_;
            if (!this.isWalkable(lvt_8_1_, lvt_9_2_)) {
                this.strafeForwards = 1.0F;
                this.strafeRight = 0.0F;
            }

            this.mob.setSpeed(lvt_2_1_);
            this.mob.setZza(this.strafeForwards);
            this.mob.setXxa(this.strafeRight);
            this.operation = MovementController.Action.WAIT;
        } else if (this.operation == MovementController.Action.MOVE_TO) {
            this.operation = MovementController.Action.WAIT;
            double lvt_1_2_ = this.wantedX - this.mob.getX();
            double lvt_3_2_ = this.wantedZ - this.mob.getZ();
            double lvt_5_2_ = this.wantedY - this.mob.getY();
            double lvt_7_2_ = lvt_1_2_ * lvt_1_2_ + lvt_5_2_ * lvt_5_2_ + lvt_3_2_ * lvt_3_2_;
            if (lvt_7_2_ < 2.500000277905201E-7D) {
                this.mob.setZza(0.0F);
                return;
            }

            lvt_9_2_ = (float)(MathHelper.atan2(lvt_3_2_, lvt_1_2_) * 57.2957763671875D) - 90.0F;
            this.mob.yRot = this.rotlerp(this.mob.yRot, lvt_9_2_, 90.0F);
            this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
            BlockPos lvt_10_1_ = this.mob.blockPosition();
            BlockState lvt_11_1_ = this.mob.level.getBlockState(lvt_10_1_);
            Block lvt_12_1_ = lvt_11_1_.getBlock();
            VoxelShape lvt_13_1_ = lvt_11_1_.getCollisionShape(this.mob.level, lvt_10_1_);
            if (lvt_5_2_ > (double)this.mob.maxUpStep && lvt_1_2_ * lvt_1_2_ + lvt_3_2_ * lvt_3_2_ < (double)Math.max(1.0F, this.mob.getBbWidth()) || !lvt_13_1_.isEmpty() && this.mob.getY() < lvt_13_1_.max(Direction.Axis.Y) + (double)lvt_10_1_.getY() && !this.companion.isSailing() && !lvt_12_1_.is(BlockTags.DOORS) && !lvt_12_1_.is(BlockTags.FENCES)) {
                this.mob.getJumpControl().jump();
                this.operation = MovementController.Action.JUMPING;
            }
        } else if (this.operation == MovementController.Action.JUMPING) {
            this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
            if (this.mob.isOnGround()) {
                this.operation = MovementController.Action.WAIT;
            }
        } else {
            this.mob.setZza(0.0F);
        }
    }

    private boolean isWalkable(float p_234024_1_, float p_234024_2_) {
        PathNavigator navigator = this.mob.getNavigation();
        NodeProcessor nodeProcessor = navigator.getNodeEvaluator();
        return nodeProcessor.getBlockPathType(this.mob.level, MathHelper.floor(this.mob.getX() + (double) p_234024_1_), MathHelper.floor(this.mob.getY()), MathHelper.floor(this.mob.getZ() + (double) p_234024_2_)) == PathNodeType.WALKABLE;
    }
}
