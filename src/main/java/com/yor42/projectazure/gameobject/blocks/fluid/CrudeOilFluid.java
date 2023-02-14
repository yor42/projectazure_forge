package com.yor42.projectazure.gameobject.blocks.fluid;

import com.yor42.projectazure.libs.utils.ResourceUtils;
import com.yor42.projectazure.setup.register.RegisterBlocks;
import com.yor42.projectazure.setup.register.RegisterFluids;
import com.yor42.projectazure.setup.register.RegisterItems;
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
    public Fluid getFlowing() {
        return RegisterFluids.CRUDE_OIL_FLOWING_REGISTRY.get();
    }

    @Override
    public Fluid getSource() {
        return RegisterFluids.CRUDE_OIL_SOURCE_REGISTRY.get();
    }

    @Override
    protected boolean canConvertToSource() {
        return false;
    }

    protected void beforeDestroyingBlock(IWorld worldIn, BlockPos pos, BlockState state) {
        TileEntity tileentity = state.hasTileEntity() ? worldIn.getBlockEntity(pos) : null;
        Block.dropResources(state, worldIn, pos, tileentity);
    }


    @Override
    public int getSlopeFindDistance(IWorldReader worldIn) {
        return 4;
    }

    @Override
    public int getDropOff(IWorldReader worldIn) {
        return 2;
    }

    @Override
    public Item getBucket() {
        return RegisterItems.CRUDE_OIL_BUCKET.get();
    }

    @Override
    public boolean canBeReplacedWith(FluidState fluidState, IBlockReader blockReader, BlockPos pos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !fluid.is(FluidTags.WATER);
    }

    @Override
    public int getTickDelay(IWorldReader p_205569_1_) {
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
                .sound(SoundEvents.BUCKET_FILL, SoundEvents.BUCKET_EMPTY)
                .build(this);
    }

    @Override
    public BlockState createLegacyBlock(FluidState state) {
        return RegisterBlocks.CRUDE_OIL.get().defaultBlockState().setValue(FlowingFluidBlock.LEVEL, getLegacyLevel(state));
    }

    @Override
    public boolean isSame(Fluid fluidIn) {
        return fluidIn == RegisterFluids.CRUDE_OIL_FLOWING_REGISTRY.get() || fluidIn == RegisterFluids.CRUDE_OIL_SOURCE_REGISTRY.get();
    }

    public static class Flowing extends CrudeOilFluid {
        protected void createFluidStateDefinition(StateContainer.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        public int getAmount(FluidState state) {
            return state.getValue(LEVEL);
        }

        public boolean isSource(FluidState state) {
            return false;
        }
    }

    public static class Source extends CrudeOilFluid {
        public int getAmount(FluidState state) {
            return 8;
        }

        public boolean isSource(FluidState state) {
            return true;
        }
    }
}
