package com.yor42.projectazure.gameobject.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractContainerBlock extends Block implements EntityBlock {
    public AbstractContainerBlock(Properties properties) {
        super(properties);
    }

    public boolean canDropInventory(BlockState state){
        return false;
    }
}
