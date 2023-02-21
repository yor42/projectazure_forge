package com.yor42.projectazure.gameobject.blocks.machines;

import com.yor42.projectazure.gameobject.blocks.AbstractMachineBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class DroneDockingStationBlock extends AbstractMachineBlock {
    public DroneDockingStationBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return null;
    }

    @Override
    protected void interactWith(World worldIn, BlockPos pos, PlayerEntity player) {

    }
}
