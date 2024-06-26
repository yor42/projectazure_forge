package com.yor42.projectazure.libs.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Function;

public class BlockUtil {

    public static RelativeDirection getRelativeDirection(Direction direction, Direction MachineFacings) {
        switch (MachineFacings) {
            case NORTH:{
                switch (direction) {
                    case UP:
                        return RelativeDirection.UP;
                    case NORTH:
                        return RelativeDirection.BACK;
                    case EAST:
                        return RelativeDirection.RIGHT;
                    case WEST:
                        return RelativeDirection.LEFT;
                    case SOUTH:
                        return RelativeDirection.FRONT;
                    case DOWN:
                        return RelativeDirection.DOWN;
                }
            }
            case EAST:{
                switch (direction) {
                    case UP:
                        return RelativeDirection.UP;
                    case NORTH:
                        return RelativeDirection.LEFT;
                    case EAST:
                        return RelativeDirection.FRONT;
                    case WEST:
                        return RelativeDirection.BACK;
                    case SOUTH:
                        return RelativeDirection.RIGHT;
                    case DOWN:
                        return RelativeDirection.DOWN;
                }
            }
            case WEST:{
                switch (direction) {
                    case UP:
                        return RelativeDirection.UP;
                    case NORTH:
                        return RelativeDirection.RIGHT;
                    case EAST:
                        return RelativeDirection.FRONT;
                    case WEST:
                        return RelativeDirection.BACK;
                    case SOUTH:
                        return RelativeDirection.LEFT;
                    case DOWN:
                        return RelativeDirection.DOWN;
                }
            }
            case SOUTH:{
                switch (direction) {
                    case UP:
                        return RelativeDirection.UP;
                    case NORTH:
                        return RelativeDirection.BACK;
                    case EAST:
                        return RelativeDirection.LEFT;
                    case WEST:
                        return RelativeDirection.RIGHT;
                    case SOUTH:
                        return RelativeDirection.FRONT;
                    case DOWN:
                        return RelativeDirection.DOWN;
                }
            }
            case UP:{
                switch (direction) {
                    case UP:
                        return RelativeDirection.FRONT;
                    case NORTH:
                        return RelativeDirection.UP;
                    case EAST:
                        return RelativeDirection.LEFT;
                    case WEST:
                        return RelativeDirection.RIGHT;
                    case SOUTH:
                        return RelativeDirection.DOWN;
                    case DOWN:
                        return RelativeDirection.BACK;
                }
            }
            case DOWN:{
                switch (direction) {
                    case UP:
                        return RelativeDirection.BACK;
                    case NORTH:
                        return RelativeDirection.UP;
                    case EAST:
                        return RelativeDirection.LEFT;
                    case WEST:
                        return RelativeDirection.RIGHT;
                    case SOUTH:
                        return RelativeDirection.DOWN;
                    case DOWN:
                        return RelativeDirection.FRONT;
                }
            }
        }
        return RelativeDirection.FRONT;
    }

    @Nonnull
    public static Optional<BlockPos> getBedHeadPos(@Nonnull LevelReader world, @Nonnull BlockPos Bedpos, @Nullable LivingEntity sleeper){
        BlockState state = world.getBlockState(Bedpos);
        if(state.isBed(world, Bedpos, sleeper)){
            Block block = state.getBlock();
            BlockPos pos = block instanceof BedBlock ? state.getValue(BedBlock.PART) == BedPart.HEAD ? Bedpos : Bedpos.relative(state.getValue(BedBlock.FACING)) : null;
            return Optional.ofNullable(pos);
        }
        else{
            return Optional.empty();
        }
    }

    public static Optional<BlockPos> getAvailableBedPos(@Nonnull LevelReader world, @Nonnull BlockPos Bedpos, @Nullable LivingEntity sleeper){
        return getBedHeadPos(world, Bedpos, sleeper).map((pos)->{
            @Nullable
            BlockPos blockpos;
            if(world.getBlockState(pos).getBlock() instanceof BedBlock && !world.getBlockState(pos).getValue(BedBlock.OCCUPIED)){
                blockpos = pos;
            }
            else{
                blockpos = null;
            }
            return Optional.ofNullable(blockpos);
        }).orElse(Optional.empty());
    }

    public enum RelativeDirection {
        FRONT(Function.identity(), Direction.Axis.Z),
        LEFT(Direction::getCounterClockWise, Direction.Axis.X),
        RIGHT(Direction::getClockWise, Direction.Axis.X),
        BACK(Direction::getOpposite, Direction.Axis.Z),
        UP(f -> Direction.UP, Direction.Axis.Y),
        DOWN(f -> Direction.DOWN, Direction.Axis.Y);

        final Function<Direction, Direction> actualFacing;
        public final Direction.Axis axis;

        RelativeDirection(Function<Direction, Direction> actualFacing, Direction.Axis axis) {
            this.actualFacing = actualFacing;
            this.axis = axis;
        }

        public Direction getActualFacing(Direction facing) {
            return actualFacing.apply(facing);
        }

        public boolean isSameAxis(RelativeDirection dir) {
            return this.axis == dir.axis;
        }

    }
}
