package com.yor42.projectazure.gameobject.blocks.fluid;

import com.yor42.projectazure.setup.register.registerBlocks;
import com.yor42.projectazure.setup.register.registerItems;
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
    protected void beforeDestroyingBlock(LevelAccessor worldIn, BlockPos pos, BlockState state) {
        BlockEntity tileentity = state.hasBlockEntity() ? worldIn.getBlockEntity(pos) : null;
        Block.dropResources(state, worldIn, pos, tileentity);
    }

    @Override
    protected int getSlopeFindDistance(LevelReader worldIn) {
        return 4;
    }

    @Override
    protected int getDropOff(LevelReader worldIn) {
        return 1;
    }

    @Override
    public Item getBucket() {
        return registerItems.DIESEL_BUCKET.get();
    }

    @Override
    protected boolean canBeReplacedWith(FluidState p_76127_, BlockGetter p_76128_, BlockPos p_76129_, Fluid fluid, Direction direction) {
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
    public int getTickDelay(LevelReader p_205569_1_) {
        return 10;
    }

    @Override
    protected float getExplosionResistance() {
        return 100;
    }

    @Override
    protected BlockState createLegacyBlock(FluidState state) {
        return registerBlocks.DIESEL.get().defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state));
    }

    @Override
    public boolean isSame(Fluid fluidIn) {
        return fluidIn == DIESEL_SOURCE || fluidIn == DIESEL_FLOWING;
    }

    public static class Flowing extends DieselFluid {
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

    public static class Source extends DieselFluid {
        public int getAmount(FluidState state) {
            return 8;
        }

        public boolean isSource(FluidState state) {
            return true;
        }
    }

}
