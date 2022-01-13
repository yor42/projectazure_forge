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

import static com.yor42.projectazure.setup.register.registerFluids.DIESEL_FLOWING;
import static com.yor42.projectazure.setup.register.registerFluids.DIESEL_SOURCE;

public abstract class DieselFluid extends FlowingFluid {

    @Override
    public Fluid getFlowing() {
        return DIESEL_FLOWING;
    }

    @Override
    public Fluid getSource() {
        return DIESEL_SOURCE;
    }

    @Override
    protected boolean canConvertToSource() {
        return false;
    }

    @Override
    protected void beforeDestroyingBlock(IWorld worldIn, BlockPos pos, BlockState state) {
        TileEntity tileentity = state.hasTileEntity() ? worldIn.getBlockEntity(pos) : null;
        Block.dropResources(state, worldIn, pos, tileentity);
    }

    @Override
    protected int getSlopeFindDistance(IWorldReader worldIn) {
        return 4;
    }

    @Override
    protected int getDropOff(IWorldReader worldIn) {
        return 1;
    }

    @Override
    public Item getBucket() {
        return registerItems.DIESEL_BUCKET.get();
    }

    @Override
    protected boolean canBeReplacedWith(FluidState fluidState, IBlockReader blockReader, BlockPos pos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !fluid.is(FluidTags.WATER);
    }

    @Override
    protected FluidAttributes createAttributes() {
        return net.minecraftforge.fluids.FluidAttributes.builder(
                        new ResourceLocation("block/water_still"),
                        new ResourceLocation("block/water_flow"))
                .overlay(new ResourceLocation("block/water_overlay"))
                .translationKey("block.projectazure.diesel")
                .color(0x88aa7d00).density(2000).viscosity(2000)
                .sound(SoundEvents.BUCKET_FILL, SoundEvents.BUCKET_EMPTY)
                .build(this);
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
    protected BlockState createLegacyBlock(FluidState state) {
        return registerBlocks.DIESEL.get().defaultBlockState().setValue(FlowingFluidBlock.LEVEL, getLegacyLevel(state));
    }

    @Override
    public boolean isSame(Fluid fluidIn) {
        return fluidIn == DIESEL_SOURCE || fluidIn == DIESEL_FLOWING;
    }

    public static class Flowing extends DieselFluid {
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

    public static class Source extends DieselFluid {
        public int getAmount(FluidState state) {
            return 8;
        }

        public boolean isSource(FluidState state) {
            return true;
        }
    }

}
