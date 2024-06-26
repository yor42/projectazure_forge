package com.yor42.projectazure.gameobject.blocks.fluid;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
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
    protected void beforeDestroyingBlock(LevelAccessor worldIn, BlockPos pos, BlockState state) {
        BlockEntity tileentity = state.hasBlockEntity() ? worldIn.getBlockEntity(pos) : null;
        Block.dropResources(state, worldIn, pos, tileentity);
    }

    @Override
    protected int getSlopeFindDistance(LevelReader p_185698_1_) {
        return 6;
    }

    @Override
    protected int getDropOff(LevelReader p_204528_1_) {
        return 1;
    }

    @Override
    public Item getBucket() {
        return KETON_BUCKET.get();
    }

    @Override
    protected boolean canBeReplacedWith(FluidState p_215665_1_, BlockGetter p_215665_2_, BlockPos p_215665_3_, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !fluid.is(FluidTags.WATER);
    }

    @Override
    public int getTickDelay(LevelReader p_205569_1_) {
        return 5;
    }

    @Override
    protected float getExplosionResistance() {
        return 100;
    }

    @Nonnull
    @Override
    protected BlockState createLegacyBlock(@Nonnull FluidState p_204527_1_) {
        return KETON.get().defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(p_204527_1_));
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

    public static class Source extends KetonFluid {
        public int getAmount(FluidState state) {
            return 8;
        }

        public boolean isSource(FluidState state) {
            return true;
        }
    }

}
