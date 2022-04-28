package com.yor42.projectazure.gameobject.blocks;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.MultiBlockReenforcedConcrete;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.MultiblockBaseTE;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.MultiblockDrydockTE;
import com.yor42.projectazure.libs.utils.MultiblockHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;

public class MultiblockStructureBlocks extends AbstractMultiBlockBase{
    public MultiblockStructureBlocks(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MultiBlockReenforcedConcrete(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return state.getValue(FORMED)? RenderShape.ENTITYBLOCK_ANIMATED: RenderShape.MODEL;
    }

    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if(!worldIn.isClientSide() && !player.isShiftKeyDown()){
            BlockEntity TE= worldIn.getBlockEntity(pos);
            if(TE instanceof MultiblockBaseTE){
                MultiblockBaseTE multiTE = (MultiblockBaseTE) TE;
                if(multiTE.hasMaster()){
                    Main.LOGGER.debug("Opening Master GUI");
                    MultiblockBaseTE Master = multiTE.getMaster();
                    if(Master instanceof MenuProvider) {
                        NetworkHooks.openGui((ServerPlayer) player, (MenuProvider) Master, Master::encodeExtraData);
                        return InteractionResult.SUCCESS;
                    }
                }
                else if(TE instanceof MultiblockDrydockTE){
                    int type = MultiblockHandler.checkMultiBlockForm(worldIn, pos);
                    if (type > 0) {
                        MultiblockHandler.setupStructure(worldIn, pos, type);
                        return InteractionResult.SUCCESS;

                    }
                }
            }
        }


        return state.getValue(FORMED)? InteractionResult.SUCCESS:InteractionResult.PASS;
    }

    @Override
    protected void interactWith(Level worldIn, BlockPos pos, Player player) {

    }
}
