package com.yor42.projectazure.gameobject.blocks.tileentity;

import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.MultiblockBaseTE;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityBoundingBox extends BlockEntity {

    protected BlockEntity masterTile = null;
    protected boolean hasMaster;			//master flag
    private BlockPos masterPos = BlockPos.ZERO;

    public TileEntityBoundingBox(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }


    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putInt("masterX", masterPos.getX());
        compound.putInt("masterY", masterPos.getY());
        compound.putInt("masterZ", masterPos.getZ());
        compound.putBoolean("hasMaster", hasMaster);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.masterPos = new BlockPos(nbt.getInt("masterX"), nbt.getInt("masterY"), nbt.getInt("masterZ"));
        this.hasMaster = nbt.getBoolean("hasMaster");
    }

    public BlockEntity getMaster()
    {
        if (this.masterTile != null)
        {
            return this.masterTile;
        }
        else
        {
            //check master again
            if (hasMaster)
            {
                BlockEntity tile = this.level.getBlockEntity(this.masterPos);

                if (tile instanceof MultiblockBaseTE)
                {
                    this.setMaster((MultiblockBaseTE) tile);
                    return this.masterTile;
                }
            }
        }

        return null;
    }

    public boolean hasMaster()
    {
        return this.hasMaster;
    }

    public BlockPos getMasterPos()
    {
        return this.masterPos;
    }


    public void setMaster(BlockEntity master)
    {
        if (master != null && !master.isRemoved())
        {
            this.masterTile = master;
            this.setMasterCoords(master.getBlockPos());
        }
        else
        {
            this.masterTile = null;
        }
    }

    public void setHasMaster(boolean par1)
    {
        this.hasMaster = par1;
    }

    public void setMasterCoords(BlockPos pos)
    {
        this.masterPos = pos;
    }

}
