package com.yor42.projectazure.gameobject.blocks;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.MultiBlockReenforcedConcrete;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.MultiblockBaseTE;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.MultiblockDrydockTE;
import com.yor42.projectazure.libs.utils.MultiblockHandler;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.BlockEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.PathNavigationRegion;
import net.minecraft.world.Level;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class MultiblockStructureBlocks extends AbstractMultiBlockBase{
    public MultiblockStructureBlocks(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity createTileEntity(BlockState state, PathNavigationRegion world) {
        return new MultiBlockReenforcedConcrete();
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return state.getValue(FORMED)? BlockRenderType.ENTITYBLOCK_ANIMATED: BlockRenderType.MODEL;
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType use(BlockState state, Level worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(!worldIn.isClientSide() && !player.isShiftKeyDown()){
            BlockEntity TE= worldIn.getBlockEntity(pos);
            if(TE instanceof MultiblockBaseTE){
                MultiblockBaseTE multiTE = (MultiblockBaseTE) TE;
                if(multiTE.hasMaster()){
                    Main.LOGGER.debug("Opening Master GUI");
                    MultiblockBaseTE Master = multiTE.getMaster();
                    NetworkHooks.openGui((ServerPlayerEntity) player, Master, Master::encodeExtraData);
                    return ActionResultType.SUCCESS;
                }
                else if(TE instanceof MultiblockDrydockTE){
                    int type = MultiblockHandler.checkMultiBlockForm(worldIn, pos);
                    if (type > 0) {
                        MultiblockHandler.setupStructure(worldIn, pos, type);
                        return ActionResultType.SUCCESS;

                    }
                }
            }
        }


        return state.getValue(FORMED)? ActionResultType.SUCCESS:ActionResultType.PASS;
    }

    @Override
    protected void interactWith(Level worldIn, BlockPos pos, PlayerEntity player) {
    }
}
