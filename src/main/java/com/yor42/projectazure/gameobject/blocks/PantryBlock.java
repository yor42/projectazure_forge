package com.yor42.projectazure.gameobject.blocks;

import com.yor42.projectazure.gameobject.blocks.tileentity.TileEntityPantry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Random;

public class PantryBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock, EntityBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public PantryBlock(Properties p_i48377_1_) {
        super(p_i48377_1_);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player p_225533_4_, InteractionHand p_225533_5_, BlockHitResult p_225533_6_) {
        if(!state.hasProperty(FACING)){
            return super.use(state, world, pos, p_225533_4_, p_225533_5_, p_225533_6_);
        }

        if(world.getBlockState(pos.relative(state.getValue(FACING))).getMaterial() != Material.AIR){
            return InteractionResult.FAIL;
        }
        else
        {
            if(!world.isClientSide()) {
                this.openGUI(pos, world, (ServerPlayer) p_225533_4_);
            }
            return InteractionResult.SUCCESS;
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void openGUI(BlockPos pos, Level world, ServerPlayer p_225533_4_)
    {
        if(!world.isClientSide())
        {
            NetworkHooks.openGui(p_225533_4_, new TileEntityPantry.ContainerProvider(world, pos, new TranslatableComponent(this.getDescriptionId())));
        }
    }

    @Override
    public void tick(BlockState p_225534_1_, ServerLevel p_225534_2_, BlockPos p_225534_3_, Random p_225534_4_) {
        BlockEntity tileentity = p_225534_2_.getBlockEntity(p_225534_3_);
        if (tileentity instanceof TileEntityPantry) {
            ((TileEntityPantry)tileentity).recheckOpen();
        }

    }

    @Override
    public BlockState updateShape(BlockState p_196271_1_, Direction p_196271_2_, BlockState p_196271_3_, LevelAccessor p_196271_4_, BlockPos p_196271_5_, BlockPos p_196271_6_) {
        if (p_196271_1_.getValue(WATERLOGGED)) {
            p_196271_4_.scheduleTick(p_196271_5_, Fluids.WATER, Fluids.WATER.getTickDelay(p_196271_4_));
        }

        return super.updateShape(p_196271_1_, p_196271_2_, p_196271_3_, p_196271_4_, p_196271_5_, p_196271_6_);
    }

    @Override
    public FluidState getFluidState(BlockState p_204507_1_) {
        return p_204507_1_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_204507_1_);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_196258_1_) {
        FluidState fluidstate = p_196258_1_.getLevel().getFluidState(p_196258_1_.getClickedPos());
        return Objects.requireNonNull(super.getStateForPlacement(p_196258_1_)).setValue(FACING, p_196258_1_.getHorizontalDirection().getOpposite()).setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER).setValue(OPEN, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_206840_1_) {
        super.createBlockStateDefinition(p_206840_1_);
        p_206840_1_.add(FACING).add(WATERLOGGED).add(OPEN);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockpos, BlockState blockstate) {
        return new TileEntityPantry(blockpos, blockstate);
    }
}
