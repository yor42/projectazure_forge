package com.yor42.projectazure.gameobject.blocks.fluid;

import com.yor42.projectazure.setup.register.registerBlocks;
import com.yor42.projectazure.setup.register.registerItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.fluids.FluidAttributes;

import static com.yor42.projectazure.setup.register.registerFluids.*;

public abstract class NetherQuartzSolutionFluid extends FlowingFluid {
    @Override
    public Fluid getFlowingFluid() {
        return NETHER_QUARTZ_SOLUTION_FLOWING;
    }

    @Override
    public Fluid getStillFluid() {
        return NETHER_QUARTZ_SOLUTION_SOURCE;
    }

    @Override
    protected boolean canSourcesMultiply() {
        return false;
    }

    @Override
    protected void beforeReplacingBlock(IWorld worldIn, BlockPos pos, BlockState state) {
        TileEntity tileentity = state.hasTileEntity() ? worldIn.getTileEntity(pos) : null;
        Block.spawnDrops(state, worldIn, pos, tileentity);
    }

    @Override
    protected int getSlopeFindDistance(IWorldReader worldIn) {
        return 4;
    }

    @Override
    protected int getLevelDecreasePerBlock(IWorldReader worldIn) {
        return 1;
    }

    @Override
    public Item getFilledBucket() {
        return Items.AIR;
    }

    @Override
    protected boolean canDisplace(FluidState fluidState, IBlockReader blockReader, BlockPos pos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !fluid.isIn(FluidTags.WATER);
    }

    @Override
    protected FluidAttributes createAttributes() {
        return FluidAttributes.builder(
                        new ResourceLocation("block/water_still"),
                        new ResourceLocation("block/water_flow"))
                .overlay(new ResourceLocation("block/water_overlay"))
                .translationKey("block.projectazure.originium_solution")
                .color(0xFFEAE5DE).density(2000).viscosity(2000)
                .sound(SoundEvents.ITEM_BUCKET_FILL, SoundEvents.ITEM_BUCKET_EMPTY)
                .build(this);
    }

    @Override
    public int getTickRate(IWorldReader p_205569_1_) {
        return 10;
    }

    @Override
    protected float getExplosionResistance() {
        return 100;
    }

    @Override
    protected BlockState getBlockState(FluidState state) {
        return registerBlocks.GASOLINE.get().getDefaultState().with(FlowingFluidBlock.LEVEL, getLevelFromState(state));
    }

    @Override
    public boolean isEquivalentTo(Fluid fluidIn) {
        return fluidIn == GASOLINE_SOURCE || fluidIn == GASOLINE_FLOWING;
    }

    public static class Flowing extends NetherQuartzSolutionFluid {
        protected void fillStateContainer(StateContainer.Builder<Fluid, FluidState> builder) {
            super.fillStateContainer(builder);
            builder.add(LEVEL_1_8);
        }

        public int getLevel(FluidState state) {
            return state.get(LEVEL_1_8);
        }

        public boolean isSource(FluidState state) {
            return false;
        }
    }

    public static class Source extends NetherQuartzSolutionFluid {
        public int getLevel(FluidState state) {
            return 8;
        }

        public boolean isSource(FluidState state) {
            return true;
        }
    }
}
