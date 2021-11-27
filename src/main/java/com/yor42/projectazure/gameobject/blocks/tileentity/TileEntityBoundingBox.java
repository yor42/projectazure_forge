package com.yor42.projectazure.gameobject.blocks.tileentity;

import com.yor42.projectazure.gameobject.blocks.AbstractMultiBlockBase;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.MultiblockBaseTE;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityBoundingBox extends TileEntity {

    protected TileEntity masterTile = null;
    protected boolean hasMaster;			//master flag
    private BlockPos masterPos = BlockPos.ZERO;

    public TileEntityBoundingBox(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putInt("masterX", masterPos.getX());
        compound.putInt("masterY", masterPos.getY());
        compound.putInt("masterZ", masterPos.getZ());
        compound.putBoolean("hasMaster", hasMaster);
        return compound;
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        this.masterPos = new BlockPos(nbt.getInt("masterX"), nbt.getInt("masterY"), nbt.getInt("masterZ"));
        this.hasMaster = nbt.getBoolean("hasMaster");
    }

    public TileEntity getMaster()
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
                TileEntity tile = this.world.getTileEntity(this.masterPos);

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


    public void setMaster(TileEntity master)
    {
        if (master != null && !master.isRemoved())
        {
            this.masterTile = master;
            this.setMasterCoords(master.getPos());
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
