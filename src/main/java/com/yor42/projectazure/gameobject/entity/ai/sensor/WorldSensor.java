package com.yor42.projectazure.gameobject.entity.ai.sensor;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fluids.IFluidBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static net.minecraft.util.Hand.MAIN_HAND;

public class WorldSensor extends Sensor<AbstractEntityCompanion> {

    BlockPos LastUpdated;

    @Override
    protected void doTick(ServerWorld p_212872_1_, AbstractEntityCompanion entity) {
        if((this.LastUpdated == null || this.LastUpdated.distSqr(entity.blockPosition())>=8) && this.shouldWork(entity)){

        }
    }

    private List<BlockPos> UpdateBlockList(AbstractEntityCompanion host){

        int diameter = 16;
        int height = 16;
        ArrayList<BlockPos> BlockList = new ArrayList<>();
        BlockPos pos = new BlockPos(host.blockPosition().getX()-(diameter/2), Math.max(host.blockPosition().getY()-(height/2), 0), host.blockPosition().getZ()-(diameter/2));
        int size = diameter*diameter*height;
        Block block;
        for(int i = 0; i<size; i++) {
            BlockPos CurrentPos = pos.offset(i % diameter, i / diameter / diameter, (i / diameter) % diameter);

            BlockState state = host.getCommandSenderWorld().getBlockState(CurrentPos);
            if (state.isAir(host.getCommandSenderWorld(), CurrentPos) || state.getDestroySpeed(host.getCommandSenderWorld(), CurrentPos) < 0) {
                //Skip air and unbreakable blocks
                continue;
            }
            block = state.getBlock();
            if (block instanceof FlowingFluidBlock || block instanceof IFluidBlock) {
                //Skip liquids
                continue;
            }

            if(this.shouldMoveTo(host, host.level, CurrentPos)){
                BlockList.add(CurrentPos);
            }
        }
        return BlockList;
    }

    boolean shouldMoveTo(AbstractEntityCompanion companion, IWorldReader worldIn, BlockPos pos) {
        Block targetBlock = worldIn.getBlockState(pos).getBlock();
        Item mainHandItem = companion.getItemInHand(MAIN_HAND).getItem();
        BlockPos pos1 = pos.above();
        BlockState blockstate1blockabove = worldIn.getBlockState(pos1);

        if(this.isItemSeed(mainHandItem)){
            if(targetBlock == Blocks.FARMLAND){
                return blockstate1blockabove.getMaterial() == Material.AIR;
            }
        }
        else {
            BlockPos pos3 = pos.above();
            Block blockup = worldIn.getBlockState(pos3).getBlock();
            BlockState iblockstate = worldIn.getBlockState(pos3);

            if (mainHandItem == Items.BONE_MEAL) {
                return blockup instanceof CropsBlock && !((CropsBlock) blockup).isMaxAge(iblockstate);
            } else if (mainHandItem instanceof HoeItem) {
                return blockup == Blocks.MELON || blockup == Blocks.PUMPKIN || (blockup instanceof CropsBlock && ((CropsBlock) blockup).isMaxAge(iblockstate));
            } else if (mainHandItem.isCorrectToolForDrops(iblockstate)) {
                if (mainHandItem instanceof PickaxeItem) {
                    return blockup instanceof OreBlock;
                }
            }

        }

        return false;
    }

    private boolean isItemSeed(Item item){
        return item == Items.WHEAT_SEEDS || item == Items.POTATO || item == Items.CARROT || item == Items.BEETROOT_SEEDS || item == Items.BONE_MEAL;
    }

    private boolean shouldWork(AbstractEntityCompanion entity){
        Item mainHandItem = entity.getMainHandItem().getItem();
        return mainHandItem == Items.BONE_MEAL||mainHandItem instanceof HoeItem ||mainHandItem instanceof PickaxeItem;
    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return null;
    }
}
