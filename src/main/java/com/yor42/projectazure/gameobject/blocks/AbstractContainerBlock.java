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
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {

        TileEntity tile = worldIn.getTileEntity(pos);

        //drop item
        if (canDropInventory(state) && tile instanceof IInventory)
        {
            InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) tile);
        }

        //alert block change
        if (shouldNotifyBlockChange())
        {
            worldIn.updateComparatorOutputLevel(pos, this);
        }

        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }
}
