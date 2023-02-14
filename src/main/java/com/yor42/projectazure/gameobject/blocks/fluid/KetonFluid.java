package com.yor42.projectazure.gameobject.blocks.fluid;

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

import javax.annotation.Nonnull;

import static com.yor42.projectazure.setup.register.RegisterBlocks.KETON;
import static com.yor42.projectazure.setup.register.RegisterFluids.KETON_FLOWING_REGISTRY;
import static com.yor42.projectazure.setup.register.RegisterFluids.KETON_SOURCE_REGISTRY;
import static com.yor42.projectazure.setup.register.RegisterItems.KETON_BUCKET;

public abstract class KetonFluid extends FlowingFluid {
    @Override
    public Fluid getFlowing() {
        return KETON_FLOWING_REGISTRY.get();
    }

    @Override
    public Fluid getSource() {
        return KETON_SOURCE_REGISTRY.get();
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
    protected int getSlopeFindDistance(IWorldReader p_185698_1_) {
        return 6;
    }

    @Override
    protected int getDropOff(IWorldReader p_204528_1_) {
        return 1;
    }

    @Override
    public Item getBucket() {
        return KETON_BUCKET.get();
    }

    @Override
    protected boolean canBeReplacedWith(FluidState p_215665_1_, IBlockReader p_215665_2_, BlockPos p_215665_3_, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !fluid.is(FluidTags.WATER);
    }

    @Override
    public int getTickDelay(IWorldReader p_205569_1_) {
        return 5;
    }

    @Override
    protected float getExplosionResistance() {
        return 100;
    }

    @Nonnull
    @Override
    protected BlockState createLegacyBlock(@Nonnull FluidState p_204527_1_) {
        return KETON.get().defaultBlockState().setValue(FlowingFluidBlock.LEVEL, getLegacyLevel(p_204527_1_));
    }

    @Nonnull
    @Override
    protected FluidAttributes createAttributes() {
        return net.minecraftforge.fluids.FluidAttributes.builder(
                        new ResourceLocation("block/water_still"),
                        new ResourceLocation("block/water_flow"))
                .overlay(new ResourceLocation("block/water_overlay"))
                .translationKey("block.projectazure.keton")
                .color(0xfffad900).density(1500).viscosity(1500)
                .sound(SoundEvents.BUCKET_FILL, SoundEvents.BUCKET_EMPTY)
                .build(this);
    }

    public static class Flowing extends KetonFluid {
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

    public static class Source extends KetonFluid {
        public int getAmount(FluidState state) {
            return 8;
        }

        public boolean isSource(FluidState state) {
            return true;
        }
    }

}
