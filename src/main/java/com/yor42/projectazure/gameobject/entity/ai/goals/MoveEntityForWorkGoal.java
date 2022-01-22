package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.fluids.IFluidBlock;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;

abstract class MoveEntityForWorkGoal extends Goal {

    protected AbstractEntityCompanion host;
    private final double speed;
    @Nullable
    protected BlockPos BlockPosBelowTarget;
    protected boolean work;
    private int UpdateTimer;
    private final ArrayList<BlockPos> BlockList = new ArrayList<>();

    protected MoveEntityForWorkGoal(AbstractEntityCompanion entity, double speed) {
        this.host = entity;
        this.speed = speed;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
    }

    public boolean canUse()
    {

        if(this.host.isCriticallyInjured()){
            return false;
        }

        if(this.UpdateTimer == 0){
            this.UpdateTimer = 10;
            this.UpdateBlockList();
        }
        else{
            this.UpdateTimer--;
        }
        if(!this.BlockList.isEmpty() || this.BlockPosBelowTarget == null) {
            this.searchForDestination();
        }

        return this.BlockPosBelowTarget != null && this.host.shouldPickupItem();
    }

    @Override
    public boolean canContinueToUse() {

        return this.canUse();
    }

    private void UpdateBlockList(){

        int diameter = 16;
        int height = 16;

        BlockPos pos = new BlockPos(this.host.blockPosition().getX()-(diameter/2), Math.max(this.host.blockPosition().getY()-(height/2), 0), this.host.blockPosition().getZ()-(diameter/2));
        int size = diameter*diameter*height;
        Block block;
        for(int i = 0; i<size; i++) {
            if (this.host.isDeadOrDying()) {
                return;
            }
            BlockPos CurrentPos = pos.offset(i % diameter, i / diameter / diameter, (i / diameter) % diameter);

            BlockState state = this.host.getCommandSenderWorld().getBlockState(CurrentPos);
            if (state.isAir(this.host.getCommandSenderWorld(), CurrentPos) || state.getDestroySpeed(this.host.getCommandSenderWorld(), CurrentPos) < 0) {
                //Skip air and unbreakable blocks
                continue;
            }
            block = state.getBlock();
            if (block instanceof FlowingFluidBlock || block instanceof IFluidBlock) {
                //Skip liquids
                continue;
            }

            if(this.shouldMoveTo(this.host.level, CurrentPos)){
                this.BlockList.add(CurrentPos);
            }
        }
    }

    private boolean searchForDestination()
    {
        if(this.BlockList.size() > 0) {
            for (BlockPos pos : this.BlockList) {
                boolean cansee = this.canSeeBlock(pos.above());
                boolean flag = this.shouldMoveTo(this.host.level, pos);
                if (cansee && flag) {
                    this.BlockPosBelowTarget = pos;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canSeeBlock(BlockPos blockpos1){
        Vector3d hostPositionVec = new Vector3d(this.host.getX(), this.host.getEyeY(), this.host.getZ());
        Vector3d targetVec = new Vector3d(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());
        RayTraceContext rayTraceContext = new RayTraceContext(hostPositionVec, targetVec, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this.host);
        BlockRayTraceResult RayTraceResult = this.host.level.clip(rayTraceContext);
        BlockPos hitpos = RayTraceResult.getBlockPos();

        boolean isXEqual = hitpos.getX() == blockpos1.getX();
        boolean isYEqual = hitpos.getY() == blockpos1.getY();
        boolean isZEqual = hitpos.getZ() == blockpos1.getZ();

        return isXEqual && isYEqual && isZEqual;
    }

    @Override
    public void stop() {
        if(this.BlockPosBelowTarget != null) {
            BlockPos blockpos = this.BlockPosBelowTarget.above();
            this.host.level.destroyBlockProgress(this.host.getId(), blockpos, -1);
            this.BlockPosBelowTarget = null;
        }
        this.host.getNavigation().stop();
        super.stop();
    }

    public void tick()
    {
        if(this.BlockPosBelowTarget != null) {
            BlockPos blockpos = this.BlockPosBelowTarget.above();
            if (blockpos.distSqr(this.host.getX(), this.host.getY(), this.host.getZ(), true) <= 4d) {
                this.work = true;
                this.host.getNavigation().stop();
            } else {
                this.work = !this.host.getNavigation().moveTo((double) ((float) blockpos.getX()) + 0.5D, (double) blockpos.getY(), (double) ((float) blockpos.getZ()) + 0.5D, this.speed);
            }
        }
    }

    abstract boolean shouldMoveTo(IWorldReader worldIn, BlockPos pos);
}
