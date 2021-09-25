package com.yor42.projectazure.gameobject.blocks.fluid;

import com.yor42.projectazure.libs.utils.ResourceUtils;
import com.yor42.projectazure.setup.register.registerBlocks;
import com.yor42.projectazure.setup.register.registerFluids;
import com.yor42.projectazure.setup.register.registerItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.fluids.FluidAttributes;

public abstract class CrudeOilFluid extends FlowingFluid {
    @Override
    public Fluid getFlowingFluid() {
        return registerFluids.CRUDE_OIL_FLOWING;
    }

    @Override
    public Fluid getStillFluid() {
        return registerFluids.CRUDE_OIL_SOURCE;
    }

    @Override
    protected boolean canSourcesMultiply() {
        return false;
    }

    protected void beforeReplacingBlock(IWorld worldIn, BlockPos pos, BlockState state) {
        TileEntity tileentity = state.hasTileEntity() ? worldIn.getTileEntity(pos) : null;
        Block.spawnDrops(state, worldIn, pos, tileentity);
    }


    @Override
    public int getSlopeFindDistance(IWorldReader worldIn) {
        return 4;
    }

    @Override
    public int getLevelDecreasePerBlock(IWorldReader worldIn) {
        return 2;
    }

    @Override
    public Item getFilledBucket() {
        return registerItems.CRUDE_OIL_BUCKET.get();
    }

    @Override
    public boolean canDisplace(FluidState fluidState, IBlockReader blockReader, BlockPos pos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !fluid.isIn(FluidTags.WATER);
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
    protected FluidAttributes createAttributes() {
        return net.minecraftforge.fluids.FluidAttributes.builder(
                       ResourceUtils.ModResourceLocation("block/crude_oil_still"),
                        ResourceUtils.ModResourceLocation("block/crude_oil_flow"))
                .translationKey("block.projectazure.crude_oil").density(2000).viscosity(2000).color(0xFF633d00)
                .sound(SoundEvents.ITEM_BUCKET_FILL, SoundEvents.ITEM_BUCKET_EMPTY)
                .build(this);
    }

    @Override
    public BlockState getBlockState(FluidState state) {
        return registerBlocks.CRUDE_OIL.get().getDefaultState().with(FlowingFluidBlock.LEVEL, getLevelFromState(state));
    }

    @Override
    public boolean isEquivalentTo(Fluid fluidIn) {
        return fluidIn == registerFluids.CRUDE_OIL_FLOWING || fluidIn == registerFluids.CRUDE_OIL_SOURCE;
    }

    public static class Flowing extends CrudeOilFluid {
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

    public static class Source extends CrudeOilFluid {
        public int getLevel(FluidState state) {
            return 8;
        }

        public boolean isSource(FluidState state) {
            return true;
        }
    }
}
