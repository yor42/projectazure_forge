package com.yor42.projectazure.gameobject.blocks.machines;

import com.yor42.projectazure.gameobject.blocks.AbstractMachineBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class DroneDockingStationBlock extends AbstractMachineBlock {
    public DroneDockingStationBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
        return null;
    }

    @Override
    protected void interactWith(Level worldIn, BlockPos pos, Player player) {

    }
}
