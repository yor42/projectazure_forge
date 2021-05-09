package com.yor42.projectazure.gameobject.entity.ai;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.fluids.IFluidBlock;

import java.util.ArrayList;
import java.util.EnumSet;

abstract class KansenMoveEntityForWorkGoal extends Goal {

    protected AbstractEntityCompanion host;
    private final double speed;
    protected BlockPos targetBlockPos;
    protected boolean work;
    private int UpdateTimer;
    private ArrayList<BlockPos> BlockList = new ArrayList<>();

    protected KansenMoveEntityForWorkGoal(AbstractEntityCompanion entity, double speed) {
        this.host = entity;
        this.speed = speed;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
    }

    public boolean shouldExecute()
    {

        if(this.UpdateTimer == 0){
            this.UpdateTimer = 40;
            this.UpdateBlockList();
        }
        else{
            this.UpdateTimer--;
        }


        if(this.targetBlockPos != null)
        {
            return false;
        }
        return this.searchForDestination();
    }

    @Override
    public boolean shouldContinueExecuting() {
        if(this.targetBlockPos == null)
        {
            return false;
        }
        else
        {
            return this.shouldMoveTo(this.host.world, this.targetBlockPos);
        }
    }

    private void UpdateBlockList(){
        BlockPos pos = this.host.getPosition();
        int diameter = 16;
        int height = 16;
        int size = diameter*diameter*height;
        Block block;
        for(int i = 0; i<size; i++) {
            if (this.host.getShouldBeDead()) {
                return;
            }
            // Mekanism Devs doing some big brain move and doing this in single loop. have my applause.
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

                boolean flag = this.canSeeBlock(pos);
                if (flag) {
                    this.targetBlockPos = pos;
                    this.host.getNavigator().getPathToPos(this.targetBlockPos, 16);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canSeeBlock(BlockPos blockpos1){
        Vector3d hostPositionVec = new Vector3d((double) MathHelper.floor(this.host.getPosX())+0.5D, (double) MathHelper.floor(this.host.getPosY())+1, (double) MathHelper.floor(this.host.getPosZ())+0.5D);
        Vector3d targetVec = new Vector3d(blockpos1.getX()+0.5D, blockpos1.getY()+0.5D, blockpos1.getZ()+0.5D);
        RayTraceContext rayTraceContext = new RayTraceContext(hostPositionVec, targetVec, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this.host);
        BlockRayTraceResult RayTraceResult = this.host.world.rayTraceBlocks(rayTraceContext);
        return RayTraceResult.getPos().equals(blockpos1);
    }

    @Override
    public void resetTask() {
        BlockPos blockpos = this.targetBlockPos.up();
        this.host.getNavigator().clearPath();
        this.host.world.sendBlockBreakProgress(this.host.getEntityId(), blockpos, -1);
        this.targetBlockPos = null;
        super.resetTask();
    }

    public void tick()
    {
        BlockPos blockpos = this.targetBlockPos.up();
        if(blockpos.distanceSq(this.host.getPosX(), this.host.getPosY(), this.host.getPosZ(), false) <= 2d)
        {
            this.work = true;
        }
        else
        {
            this.work = !this.host.getNavigator().tryMoveToXYZ((double) ((float) blockpos.getX()) + 0.5D, (double) blockpos.getY(), (double) ((float) blockpos.getZ()) + 0.5D, this.speed);
        }
    }

    abstract boolean shouldMoveTo(IWorldReader worldIn, BlockPos pos);
}
