package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import net.minecraft.block.Block;
import net.minecraft.block.TorchBlock;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

import static net.minecraft.util.Hand.MAIN_HAND;
import static net.minecraft.util.Hand.OFF_HAND;

public class CompanionPlaceTorchGoal extends Goal {

    private final AbstractEntityCompanion companion;
    private Block torchBlock;
    private Hand torchHand;

    public CompanionPlaceTorchGoal(AbstractEntityCompanion companion){
        this.companion = companion;
    }

    @Override
    public boolean shouldExecute() {

        ItemStack mainHandStack = this.companion.getHeldItem(MAIN_HAND);
        ItemStack offHandStack = this.companion.getHeldItem(OFF_HAND);

        if(mainHandStack.getItem() instanceof ItemGunBase){
            if(((ItemGunBase) mainHandStack.getItem()).isTwoHanded()){
                return false;
            }
        }

        if(offHandStack.getItem() instanceof ItemGunBase){
            if(((ItemGunBase) offHandStack.getItem()).isTwoHanded()){
                return false;
            }
        }

        boolean hasTorch = false;

        if(mainHandStack.getItem() instanceof BlockItem && ((BlockItem) mainHandStack.getItem()).getBlock() instanceof TorchBlock){
            this.torchBlock = ((BlockItem) mainHandStack.getItem()).getBlock();
            hasTorch = true;
            this.torchHand = MAIN_HAND;
        }
        else if(offHandStack.getItem() instanceof BlockItem && ((BlockItem) offHandStack.getItem()).getBlock() instanceof TorchBlock){
            this.torchBlock = ((BlockItem) offHandStack.getItem()).getBlock();
            hasTorch = true;
            this.torchHand = OFF_HAND;
        }

        boolean isDark = this.companion.getEntityWorld().getLight(this.companion.getPosition())<7;
        boolean canPlaceTorch = this.companion.getEntityWorld().getBlockState(this.companion.getPosition().down()).isSolid();
        boolean EntityAwake = !this.companion.isEntitySleeping() && !this.companion.isSleeping();
        return hasTorch && isDark && canPlaceTorch && EntityAwake;
    }

    @Override
    public void startExecuting() {
        ItemStack torchStack = this.companion.getHeldItem(this.torchHand);

        if(this.torchBlock != null && this.companion.getEntityWorld().getLight(this.companion.getPosition())<7) {
            this.companion.swing(this.torchHand, true);
            torchStack.shrink(1);
            this.companion.getEntityWorld().setBlockState(this.companion.getPosition(), torchBlock.getDefaultState(), 2);
        }
    }
}
