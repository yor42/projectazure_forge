package com.yor42.projectazure.gameobject.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AbstractContainerBlock extends Block {
    public AbstractContainerBlock(Properties properties) {
        super(properties);
    }

    public boolean canDropInventory(BlockState state){
        return false;
    }

    public boolean shouldNotifyBlockChange(){
        return true;
    }

    @Override
    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {

        TileEntity tile = worldIn.getBlockEntity(pos);

        //drop item
        if (canDropInventory(state) && tile instanceof IInventory)
        {
            InventoryHelper.dropContents(worldIn, pos, (IInventory) tile);
        }

        //alert block change
        if (shouldNotifyBlockChange())
        {
            worldIn.updateNeighbourForOutputSignal(pos, this);
        }

        super.onRemove(state, worldIn, pos, newState, isMoving);
    }
}
