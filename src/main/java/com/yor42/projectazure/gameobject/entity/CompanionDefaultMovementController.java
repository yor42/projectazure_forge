package com.yor42.projectazure.gameobject.entity;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.MobEntity;
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
        if (this.action == MovementController.Action.STRAFE) {
            float lvt_1_1_ = (float)this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
            float lvt_2_1_ = (float)this.speed * lvt_1_1_;
            float lvt_3_1_ = this.moveForward;
            float lvt_4_1_ = this.moveStrafe;
            float lvt_5_1_ = MathHelper.sqrt(lvt_3_1_ * lvt_3_1_ + lvt_4_1_ * lvt_4_1_);
            if (lvt_5_1_ < 1.0F) {
                lvt_5_1_ = 1.0F;
            }

            lvt_5_1_ = lvt_2_1_ / lvt_5_1_;
            lvt_3_1_ *= lvt_5_1_;
            lvt_4_1_ *= lvt_5_1_;
            float lvt_6_1_ = MathHelper.sin(this.mob.rotationYaw * 0.017453292F);
            float lvt_7_1_ = MathHelper.cos(this.mob.rotationYaw * 0.017453292F);
            float lvt_8_1_ = lvt_3_1_ * lvt_7_1_ - lvt_4_1_ * lvt_6_1_;
            lvt_9_2_ = lvt_4_1_ * lvt_7_1_ + lvt_3_1_ * lvt_6_1_;
            if (!this.func_234024_b_(lvt_8_1_, lvt_9_2_)) {
                this.moveForward = 1.0F;
                this.moveStrafe = 0.0F;
            }

            this.mob.setAIMoveSpeed(lvt_2_1_);
            this.mob.setMoveForward(this.moveForward);
            this.mob.setMoveStrafing(this.moveStrafe);
            this.action = MovementController.Action.WAIT;
        } else if (this.action == MovementController.Action.MOVE_TO) {
            this.action = MovementController.Action.WAIT;
            double lvt_1_2_ = this.posX - this.mob.getPosX();
            double lvt_3_2_ = this.posZ - this.mob.getPosZ();
            double lvt_5_2_ = this.posY - this.mob.getPosY();
            double lvt_7_2_ = lvt_1_2_ * lvt_1_2_ + lvt_5_2_ * lvt_5_2_ + lvt_3_2_ * lvt_3_2_;
            if (lvt_7_2_ < 2.500000277905201E-7D) {
                this.mob.setMoveForward(0.0F);
                return;
            }

            lvt_9_2_ = (float)(MathHelper.atan2(lvt_3_2_, lvt_1_2_) * 57.2957763671875D) - 90.0F;
            this.mob.rotationYaw = this.limitAngle(this.mob.rotationYaw, lvt_9_2_, 90.0F);
            this.mob.setAIMoveSpeed((float)(this.speed * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
            BlockPos lvt_10_1_ = this.mob.getPosition();
            BlockState lvt_11_1_ = this.mob.world.getBlockState(lvt_10_1_);
            Block lvt_12_1_ = lvt_11_1_.getBlock();
            VoxelShape lvt_13_1_ = lvt_11_1_.getCollisionShape(this.mob.world, lvt_10_1_);
            if (lvt_5_2_ > (double)this.mob.stepHeight && lvt_1_2_ * lvt_1_2_ + lvt_3_2_ * lvt_3_2_ < (double)Math.max(1.0F, this.mob.getWidth()) || !lvt_13_1_.isEmpty() && this.mob.getPosY() < lvt_13_1_.getEnd(Direction.Axis.Y) + (double)lvt_10_1_.getY() && !this.companion.isSailing() && !lvt_12_1_.isIn(BlockTags.DOORS) && !lvt_12_1_.isIn(BlockTags.FENCES)) {
                this.mob.getJumpController().setJumping();
                this.action = MovementController.Action.JUMPING;
            }
        } else if (this.action == MovementController.Action.JUMPING) {
            this.mob.setAIMoveSpeed((float)(this.speed * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
            if (this.mob.isOnGround()) {
                this.action = MovementController.Action.WAIT;
            }
        } else {
            this.mob.setMoveForward(0.0F);
        }
    }

    private boolean func_234024_b_(float p_234024_1_, float p_234024_2_) {
        PathNavigator navigator = this.mob.getNavigator();
        NodeProcessor nodeProcessor = navigator.getNodeProcessor();
        return nodeProcessor.getPathNodeType(this.mob.world, MathHelper.floor(this.mob.getPosX() + (double) p_234024_1_), MathHelper.floor(this.mob.getPosY()), MathHelper.floor(this.mob.getPosZ() + (double) p_234024_2_)) == PathNodeType.WALKABLE;
    }
}
