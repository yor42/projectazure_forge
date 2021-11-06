package com.yor42.projectazure.gameobject.blocks.fluid;

import com.yor42.projectazure.libs.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.fluids.FluidAttributes;

public class FluidWithoutBlock extends Fluid {
    private static final String EXCEPTION_MSG = "Fluid does not have a block form.";
    private static final String TRANSLATION_KEY_PREFIX = "block." + Constants.MODID + ".";

    private final FluidAttributes attributes;

    public FluidWithoutBlock(ResourceLocation sprite, String unlocalizedName, int color)
    {
        this.attributes = FluidAttributes.builder(sprite, sprite).translationKey(TRANSLATION_KEY_PREFIX + unlocalizedName).color(color).build(this);
    }

    public FluidWithoutBlock(String unlocalizedName, int color)
    {
        this(new ResourceLocation("block/water_still"), unlocalizedName, color);
    }

    @Override
    protected FluidAttributes createAttributes() {
        return this.attributes;
    }

    @Override
    public Item getFilledBucket() {
        throw new UnsupportedOperationException(EXCEPTION_MSG);
    }

    @Override
    protected boolean canDisplace(FluidState fluidState, IBlockReader blockReader, BlockPos pos, Fluid fluid, Direction direction) {
        throw new UnsupportedOperationException(EXCEPTION_MSG);
    }

    @Override
    protected Vector3d getFlow(IBlockReader blockReader, BlockPos pos, FluidState fluidState) {
        throw new UnsupportedOperationException(EXCEPTION_MSG);
    }

    @Override
    public int getTickRate(IWorldReader p_205569_1_) {
        throw new UnsupportedOperationException(EXCEPTION_MSG);
    }

    @Override
    protected float getExplosionResistance() {
        throw new UnsupportedOperationException(EXCEPTION_MSG);
    }

    @Override
    public float getActualHeight(FluidState p_215662_1_, IBlockReader p_215662_2_, BlockPos p_215662_3_) {
        throw new UnsupportedOperationException(EXCEPTION_MSG);
    }

    @Override
    public float getHeight(FluidState p_223407_1_) {
        throw new UnsupportedOperationException(EXCEPTION_MSG);
    }

    @Override
    protected BlockState getBlockState(FluidState state) {
        throw new UnsupportedOperationException(EXCEPTION_MSG);
    }

    @Override
    public boolean isSource(FluidState state) {
        return false; // Thanks Mekanism
    }

    @Override
    public int getLevel(FluidState state) {
        throw new UnsupportedOperationException(EXCEPTION_MSG);
    }

    @Override
    public VoxelShape func_215664_b(FluidState p_215664_1_, IBlockReader p_215664_2_, BlockPos p_215664_3_) {
        throw new UnsupportedOperationException(EXCEPTION_MSG);
    }
}
