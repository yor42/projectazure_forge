package com.yor42.projectazure.gameobject.blocks.fluid;

import com.yor42.projectazure.setup.register.registerFluids;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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

public abstract class CrystalChamberAqueousSolution extends FlowingFluid {

    protected FluidAttributes.Builder defaultAttributes = net.minecraftforge.fluids.FluidAttributes.builder(
            new ResourceLocation("block/water_still"),
            new ResourceLocation("block/water_flow"))
            .overlay(new ResourceLocation("block/water_overlay"))
            .density(2000).viscosity(2000).translationKey(this.getTranslationKey()).color(this.getColor())
            .sound(SoundEvents.ITEM_BUCKET_FILL, SoundEvents.ITEM_BUCKET_EMPTY);

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
        return 5;
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
    public int getTickRate(IWorldReader p_205569_1_) {
        return 5;
    }

    @Override
    protected float getExplosionResistance() {
        return 100;
    }

    @Override
    protected BlockState getBlockState(FluidState state) {
        return Blocks.AIR.getDefaultState();
    }

    @Override
    protected FluidAttributes createAttributes() {
        return this.defaultAttributes.build(this);
    }

    protected abstract String getTranslationKey();

    protected abstract int getColor();

    public abstract static class OriginiumSolution extends CrystalChamberAqueousSolution {

        @Override
        protected int getColor() {
            return 0x8832180b;
        }

        @Override
        protected String getTranslationKey() {
            return "block.projectazure.originium_solution";
        }

        @Override
        public Fluid getFlowingFluid() {
            return registerFluids.ORIGINIUM_SOLUTION_FLOWING;
        }

        @Override
        public Fluid getStillFluid() {
            return registerFluids.ORIGINIUM_SOLUTION_SOURCE;
        }

        public static class Source extends OriginiumSolution {
            @Override
            public boolean isSource(FluidState state) {
                return true;
            }

            @Override
            public int getLevel(FluidState state) {
                return 8;
            }
        }

        public static class flowing extends OriginiumSolution {
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
    }

    public abstract static class NetherQuartzSolution extends CrystalChamberAqueousSolution {

        @Override
        public Fluid getFlowingFluid() {
            return registerFluids.NETHER_QUARTZ_SOLUTION_FLOWING;
        }

        @Override
        public Fluid getStillFluid() {
            return registerFluids.NETHER_QUARTZ_SOLUTION_SOURCE;
        }

            @Override
        protected int getColor() {
            return 0xFFeae5de;
        }

        @Override
        protected FluidAttributes createAttributes() {
            return net.minecraftforge.fluids.FluidAttributes.builder(
                            new ResourceLocation("block/water_still"),
                            new ResourceLocation("block/water_flow"))
                    .overlay(new ResourceLocation("block/water_overlay"))
                    .density(2000).viscosity(2000).translationKey(this.getTranslationKey()).color(0xFFeae5de)
                    .sound(SoundEvents.ITEM_BUCKET_FILL, SoundEvents.ITEM_BUCKET_EMPTY).build(this);
        }

        @Override
        protected String getTranslationKey() {
            return "block.projectazure.nether_quartz_solution";
        }

        public static class Source extends OriginiumSolution {
            @Override
            public boolean isSource(FluidState state) {
                return true;
            }

            @Override
            public int getLevel(FluidState state) {
                return 8;
            }
        }

        public static class flowing extends OriginiumSolution {
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
    }
}
