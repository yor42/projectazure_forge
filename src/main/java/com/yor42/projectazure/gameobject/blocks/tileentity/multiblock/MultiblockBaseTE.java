/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of Immersive Engineering.
 */
package com.yor42.projectazure.gameobject.blocks.tileentity.multiblock;

import com.yor42.projectazure.gameobject.blocks.AbstractMultiBlockBase;
import com.yor42.projectazure.gameobject.blocks.tileentity.AbstractAnimatedTileEntityMachines;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public abstract class MultiblockBaseTE extends AbstractAnimatedTileEntityMachines {

    protected MultiblockBaseTE masterTile = null;
    protected boolean hasMaster, isMaster;			//master flag
    protected String customName;
    protected int structType;						//MBS info
    private BlockPos masterPos = BlockPos.ZERO;	//master pos

    protected MultiblockBaseTE(TileEntityType<?> typeIn) {
        super(typeIn);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putInt("masterX", masterPos.getX());
        compound.putInt("masterY", masterPos.getY());
        compound.putInt("masterZ", masterPos.getZ());
        compound.putInt("structType", structType);
        compound.putBoolean("hasMaster", hasMaster);
        compound.putBoolean("isMaster", isMaster);
        return compound;
    }

    //getter
    public int getStructType()
    {
        return this.structType;
    }

    public MultiblockBaseTE getMaster()
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

    public boolean isMaster()
    {
        return this.isMaster;
    }

    public BlockPos getMasterPos()
    {
        return this.masterPos;
    }

    //setter
    /** set multi-block structure type, NOT blockstate!! */
    public void setStructType(int type, World world)
    {
        //set type
        this.structType = type;

        //set blockstate
        //type: 0:NO mbs, 1:mbs INACTIVE, 2:mbs ACTIVE
        if (type == 0)
        {
            AbstractMultiBlockBase.updateMultiBlockState(0, world, this.getPos());  //NO MBS
        }
        else
        {
            AbstractMultiBlockBase.updateMultiBlockState(1, world, this.getPos());  //INACTIVE
        }
    }

    /** set mater tile, separate from setHasMaster */
    public void setMaster(MultiblockBaseTE master)
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

    /** set master flag, separate from setMaster due to tile loading order problem */
    public void setHasMaster(boolean par1)
    {
        this.hasMaster = par1;
    }

    public void setIsMaster(boolean par1)
    {
        this.isMaster = par1;
    }

    public void setMasterCoords(BlockPos pos)
    {
        this.masterPos = pos;
    }

}
