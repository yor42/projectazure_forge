/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is licensed under "Blu's License of Common Sense"
 * Details can be found in the license file in the root folder of Immersive Engineering.
 */
package com.yor42.projectazure.gameobject.blocks.tileentity.multiblock;

import com.yor42.projectazure.gameobject.blocks.AbstractMultiBlockBase;
import com.yor42.projectazure.gameobject.blocks.tileentity.AbstractTileEntityGacha;
import net.minecraft.block.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tileentity.BlockEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.level.Level;


public abstract class MultiblockBaseTE extends AbstractTileEntityGacha {

    protected MultiblockBaseTE masterTile = null;
    protected boolean hasMaster, isMaster;			//master flag
    protected String customName;
    protected int structType;						//MBS info
    private BlockPos masterPos = BlockPos.ZERO;	//master pos

    protected MultiblockBaseTE(TileEntityType<?> typeIn) {
        super(typeIn);
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        super.save(compound);
        compound.putInt("masterX", masterPos.getX());
        compound.putInt("masterY", masterPos.getY());
        compound.putInt("masterZ", masterPos.getZ());
        compound.putInt("structType", structType);
        compound.putBoolean("hasMaster", hasMaster);
        compound.putBoolean("isMaster", isMaster);
        return compound;
    }

    @Override
    public void load(BlockState state, CompoundTag nbt) {
        super.load(state, nbt);
        this.masterPos = new BlockPos(nbt.getInt("masterX"), nbt.getInt("masterY"), nbt.getInt("masterZ"));
        this.structType = nbt.getInt("structType");
        this.hasMaster = nbt.getBoolean("hasMaster");
        this.isMaster = nbt.getBoolean("isMaster");
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
    public void setStructType(int type, Level world)
    {
        //set type
        this.structType = type;

        //set blockstate
        //type: 0:NO mbs, 1:mbs INACTIVE, 2:mbs ACTIVE
        //INACTIVE
        AbstractMultiBlockBase.updateMultiBlockState(type !=0 , world, this.getBlockPos());  //NO MBS
    }

    /** set mater tile, separate from setHasMaster */
    public void setMaster(MultiblockBaseTE master)
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
