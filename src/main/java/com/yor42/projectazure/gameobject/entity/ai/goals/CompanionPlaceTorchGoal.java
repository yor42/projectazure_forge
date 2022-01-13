package com.yor42.projectazure.gameobject.entity.ai.goals;
/*
import com.chaosthedude.realistictorches.blocks.RealisticTorchBlock;
import com.chaosthedude.realistictorches.items.MatchboxItem;
import com.chaosthedude.realistictorches.items.UnlitTorchItem;

 */
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import net.minecraft.block.Block;
import net.minecraft.block.TorchBlock;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.ModList;

import java.util.EnumSet;

import static net.minecraft.util.Hand.MAIN_HAND;
import static net.minecraft.util.Hand.OFF_HAND;

public class CompanionPlaceTorchGoal extends Goal {

    private final AbstractEntityCompanion companion;
    private boolean isHoldingRealistictorches;
    private Block torchBlock;
    private Hand torchHand;

    public CompanionPlaceTorchGoal(AbstractEntityCompanion companion){
        this.companion = companion;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {

        ItemStack mainHandStack = this.companion.getItemInHand(MAIN_HAND);
        ItemStack offHandStack = this.companion.getItemInHand(OFF_HAND);

        if(this.companion.isOrderedToSit() || this.companion.isSleeping()){
            return false;
        }

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

            boolean isTorchUseable;

            isTorchUseable = mainHandStack.getItem() == Items.TORCH;
            this.isHoldingRealistictorches = false;
            /*
            if(ModList.get().isLoaded("realistictorches")){
                isTorchUseable = this.isRealisticTorchReady(mainHandStack ,MAIN_HAND);
            }
            else{
                isTorchUseable = mainHandStack.getItem() == Items.TORCH;
                this.isHoldingRealistictorches = false;
            }

             */

            if(isTorchUseable) {
                this.torchBlock = ((BlockItem) mainHandStack.getItem()).getBlock();
                hasTorch = true;
                this.torchHand = MAIN_HAND;
            }
        }
        else if(offHandStack.getItem() instanceof BlockItem && ((BlockItem) offHandStack.getItem()).getBlock() instanceof TorchBlock){
            boolean isTorchUseable;

            isTorchUseable = offHandStack.getItem() == Items.TORCH;
            this.isHoldingRealistictorches = false;
            /*
            if(ModList.get().isLoaded("realistictorches")){
                isTorchUseable = this.isRealisticTorchReady(mainHandStack ,MAIN_HAND);
            }
            else{
                isTorchUseable = offHandStack.getItem() == Items.TORCH;
                this.isHoldingRealistictorches = false;
            }

             */

            if(isTorchUseable) {
                this.torchBlock = ((BlockItem) offHandStack.getItem()).getBlock();
                hasTorch = true;
                this.torchHand = OFF_HAND;
            }
        }

        boolean isDark = this.companion.getCommandSenderWorld().getMaxLocalRawBrightness(this.companion.blockPosition())<7;
        boolean canPlaceTorch = this.companion.getCommandSenderWorld().getBlockState(this.companion.blockPosition().below()).canOcclude();

        return hasTorch && isDark && canPlaceTorch;
    }
/*
    private boolean isRealisticTorchReady(ItemStack torchstack, Hand hand){
        if(torchstack.getItem() instanceof UnlitTorchItem) {
            Hand othersidehand = hand == MAIN_HAND ? OFF_HAND : MAIN_HAND;
            if(!(this.companion.getItemInHand(othersidehand).getItem() instanceof MatchboxItem) || (this.companion.getCommandSenderWorld().canSeeSky(this.companion.blockPosition()) && this.companion.getCommandSenderWorld().isRaining())) {
                this.isHoldingRealistictorches = false;
                return false;
            }
        }
        this.isHoldingRealistictorches = true;
        return true;
    }

 */

    @Override
    public void start() {
        ItemStack torchStack = this.companion.getItemInHand(this.torchHand);

        if(this.torchBlock != null && this.companion.getCommandSenderWorld().getMaxLocalRawBrightness(this.companion.blockPosition())<7) {
            this.companion.swing(this.torchHand, true);
            torchStack.shrink(1);
            this.companion.getCommandSenderWorld().setBlock(this.companion.blockPosition(), torchBlock.defaultBlockState(), 2);
            /*
            if(this.isHoldingRealistictorches && ModList.get().isLoaded("realistictorches")){
                this.lightUpTorch(this.torchHand);
            }

             */

        }
    }
    /*
    private void lightUpTorch(Hand hand){
        if(this.companion.getCommandSenderWorld().getBlockState(this.companion.blockPosition()).getBlock() instanceof RealisticTorchBlock){
            RealisticTorchBlock torchBlock = (RealisticTorchBlock)this.companion.getCommandSenderWorld().getBlockState(this.companion.blockPosition()).getBlock();
            torchBlock.playLightingSound(this.companion.getCommandSenderWorld(), this.companion.blockPosition());
            Hand othersidehand = hand == MAIN_HAND ? OFF_HAND : MAIN_HAND;
            this.companion.getItemInHand(othersidehand).hurtAndBreak(1, this.companion,(e)->{e.broadcastBreakEvent(othersidehand);});
            this.companion.swing(othersidehand);
            torchBlock.changeToLit(this.companion.getCommandSenderWorld(), this.companion.blockPosition(), this.companion.getCommandSenderWorld().getBlockState(this.companion.blockPosition()));

        }
    }

     */

}
