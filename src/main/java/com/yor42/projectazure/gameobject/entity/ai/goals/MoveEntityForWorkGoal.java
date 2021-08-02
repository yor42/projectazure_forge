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
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
    }

    public boolean shouldExecute()
    {

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
    public boolean shouldContinueExecuting() {

        return this.shouldExecute();
    }

    private void UpdateBlockList(){

        int diameter = 16;
        int height = 16;

        BlockPos pos = new BlockPos(this.host.getPosition().getX()-(diameter/2), Math.max(this.host.getPosition().getY()-(height/2), 0), this.host.getPosition().getZ()-(diameter/2));
        int size = diameter*diameter*height;
        Block block;
        for(int i = 0; i<size; i++) {
            if (this.host.getShouldBeDead()) {
                return;
            }
            BlockPos CurrentPos = pos.add(i % diameter, i / diameter / diameter, (i / diameter) % diameter);

            BlockState state = this.host.getEntityWorld().getBlockState(CurrentPos);
            if (state.isAir(this.host.getEntityWorld(), CurrentPos) || state.getBlockHardness(this.host.getEntityWorld(), CurrentPos) < 0) {
                //Skip air and unbreakable blocks
                continue;
            }
            block = state.getBlock();
            if (block instanceof FlowingFluidBlock || block instanceof IFluidBlock) {
                //Skip liquids
                continue;
            }

            if(this.shouldMoveTo(this.host.world, CurrentPos)){
                this.BlockList.add(CurrentPos);
            }
        }
    }

    private boolean searchForDestination()
    {
        if(this.BlockList.size() > 0) {
            for (BlockPos pos : this.BlockList) {

                boolean flag = this.canSeeBlock(pos.up()) && this.shouldMoveTo(this.host.world, pos);
                if (flag) {
                    this.BlockPosBelowTarget = pos;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canSeeBlock(BlockPos blockpos1){
        Vector3d hostPositionVec = new Vector3d(this.host.getPosX(), this.host.getPosYEye(), this.host.getPosZ());
        Vector3d targetVec = new Vector3d(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());
        RayTraceContext rayTraceContext = new RayTraceContext(hostPositionVec, targetVec, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this.host);
        BlockRayTraceResult RayTraceResult = this.host.world.rayTraceBlocks(rayTraceContext);
        BlockPos hitpos = RayTraceResult.getPos();

        boolean isXEqual = hitpos.getX() == blockpos1.getX();
        boolean isYEqual = hitpos.getY() == blockpos1.getY();
        boolean isZEqual = hitpos.getZ() == blockpos1.getZ();

        return isXEqual && isYEqual && isZEqual;
    }

    @Override
    public void resetTask() {
        if(this.BlockPosBelowTarget != null) {
            BlockPos blockpos = this.BlockPosBelowTarget.up();
            this.host.world.sendBlockBreakProgress(this.host.getEntityId(), blockpos, -1);
            this.BlockPosBelowTarget = null;
        }
        this.host.getNavigator().clearPath();
        super.resetTask();
    }

    public void tick()
    {
        if(this.BlockPosBelowTarget != null) {
            BlockPos blockpos = this.BlockPosBelowTarget.up();
            if (blockpos.distanceSq(this.host.getPosX(), this.host.getPosY(), this.host.getPosZ(), true) <= 4d) {
                this.work = true;
            } else {
                this.work = !this.host.getNavigator().tryMoveToXYZ((double) ((float) blockpos.getX()) + 0.5D, (double) blockpos.getY(), (double) ((float) blockpos.getZ()) + 0.5D, this.speed);
            }
        }
    }

    abstract boolean shouldMoveTo(IWorldReader worldIn, BlockPos pos);
}