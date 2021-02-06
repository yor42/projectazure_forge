package com.yor42.projectazure.gameobject.entity.ai.thread;

import com.yor42.projectazure.gameobject.entity.EntityKansenBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.IFluidBlock;

public class threadFindTargetBlock extends Thread{

    EntityKansenBase entity;

    public threadFindTargetBlock(EntityKansenBase host){
        this.entity = host;
    }

    @Override
    public void run() {

        BlockPos pos = this.entity.getPosition();
        int diameter = 8;
        int height = 8;
        int size = diameter*diameter*height;
        Block block;
        for(int i = 0; i<size; i++){
            if(this.entity.getShouldBeDead()){
                return;
            }
            // Mekanism Devs doing some big brain move and doing this in single loop. have my applause.
            BlockPos CurrentPos = pos.add(i % diameter, i / diameter / diameter, (i / diameter) % diameter);

            BlockState state = this.entity.getEntityWorld().getBlockState(CurrentPos);
            if (state.isAir(this.entity.getEntityWorld(), CurrentPos) || state.getBlockHardness(this.entity.getEntityWorld(), CurrentPos) < 0) {
                //Skip air and unbreakable blocks
                continue;
            }
            block = state.getBlock();
            if (block instanceof FlowingFluidBlock || block instanceof IFluidBlock) {
                //Skip liquids
                continue;
            }


        }
    }
}
