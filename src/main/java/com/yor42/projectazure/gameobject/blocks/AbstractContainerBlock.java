package com.yor42.projectazure.gameobject.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

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
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {

        BlockEntity tile = worldIn.getBlockEntity(pos);

        //drop item
        if (canDropInventory(state) && tile instanceof Container)
        {
            Containers.dropContents(worldIn, pos, (Container) tile);
        }

        //alert block change
        if (shouldNotifyBlockChange())
        {
            worldIn.updateNeighbourForOutputSignal(pos, this);
        }

        super.onRemove(state, worldIn, pos, newState, isMoving);
    }
}
