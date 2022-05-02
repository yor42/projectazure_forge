package com.yor42.projectazure.gameobject.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractContainerBlock extends HorizontalDirectionalBlock implements EntityBlock {
    public AbstractContainerBlock(Properties properties) {
        super(properties);
    }

    public boolean canDropInventory(BlockState state){
        return false;
    }
}
