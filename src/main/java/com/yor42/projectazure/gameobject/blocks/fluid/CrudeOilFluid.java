package com.yor42.projectazure.gameobject.blocks.fluid;

import com.yor42.projectazure.libs.utils.ResourceUtils;
import com.yor42.projectazure.setup.register.RegisterBlocks;
import com.yor42.projectazure.setup.register.RegisterFluids;
import com.yor42.projectazure.setup.register.RegisterItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
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

    protected void beforeDestroyingBlock(LevelAccessor worldIn, BlockPos pos, BlockState state) {
        BlockEntity tileentity = state.hasTileEntity() ? worldIn.getBlockEntity(pos) : null;
        Block.dropResources(state, worldIn, pos, tileentity);
    }


    @Override
    public int getSlopeFindDistance(LevelReader worldIn) {
        return 4;
    }

    @Override
    public int getDropOff(LevelReader worldIn) {
        return 2;
    }

    @Override
    public Item getBucket() {
        return RegisterItems.CRUDE_OIL_BUCKET.get();
    }

    @Override
    public boolean canBeReplacedWith(FluidState fluidState, BlockGetter blockReader, BlockPos pos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !fluid.is(FluidTags.WATER);
    }

    @Override
    public int getTickDelay(LevelReader p_205569_1_) {
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
        return RegisterBlocks.CRUDE_OIL.get().defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state));
    }

    @Override
    public boolean isSame(Fluid fluidIn) {
        return fluidIn == RegisterFluids.CRUDE_OIL_FLOWING_REGISTRY.get() || fluidIn == RegisterFluids.CRUDE_OIL_SOURCE_REGISTRY.get();
    }

    public static class Flowing extends CrudeOilFluid {
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
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
