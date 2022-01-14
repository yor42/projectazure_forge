package com.yor42.projectazure.gameobject.entity.ai.goals;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

import java.util.EnumSet;

import static net.minecraft.util.Hand.MAIN_HAND;


import net.minecraft.entity.ai.goal.Goal.Flag;

/**
 * Basically fork of Furenzu's work AI but with less intrusion and possibility for world break.
 *
 */

public class WorkGoal extends MoveEntityForWorkGoal {


    private int breakingTime;
    private int previousBreakProgress = -1;

    public WorkGoal(AbstractEntityCompanion entity, double speed){
        super(entity, speed);
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Flag.LOOK, Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        Item handItem = this.host.getItemBySlot(EquipmentSlotType.MAINHAND).getItem();

        if(!this.isItemWorkable(handItem)){
            return false;
        }
        if(!(this.host.getOwner() instanceof PlayerEntity)){
            return false;
        }

        return super.canUse();
    }

    @Override
    boolean shouldMoveTo(IWorldReader worldIn, BlockPos pos) {
        Block targetBlock = worldIn.getBlockState(pos).getBlock();
        Item mainHandItem = this.host.getItemInHand(MAIN_HAND).getItem();
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

    @Override
    public boolean canContinueToUse() {
        ItemStack handItemstack = this.host.getItemBySlot(EquipmentSlotType.MAINHAND);
        if(this.host.isSleeping() || handItemstack == ItemStack.EMPTY){
            return false;
        }
        return super.canContinueToUse();
    }

    @Override
    public void tick() {
        super.tick();
        if(this.BlockPosBelowTarget != null) {
            BlockPos blockpos = this.BlockPosBelowTarget.above();
            if (this.shouldMoveTo(this.host.getCommandSenderWorld(), this.BlockPosBelowTarget)) {
                this.host.getLookControl().setLookAt((double) blockpos.getX() + 0.5D, (double) (blockpos.getY()), (double) blockpos.getZ() + 0.5D, 10.0F, (float) this.host.getMaxHeadXRot());
            }
            if (this.work) {
                if(!this.host.swinging){
                    this.host.swing(MAIN_HAND);
                }
                ItemStack mainHandStack = this.host.getItemBySlot(EquipmentSlotType.MAINHAND);
                BlockState BlockAboveDestinationState = this.host.level.getBlockState(blockpos);
                Block blockAboveDestination = BlockAboveDestinationState.getBlock();

                if (mainHandStack.getItem() == Items.BONE_MEAL) {
                    this.host.level.levelEvent(2005, blockpos, 0);
                    this.host.level.setBlockAndUpdate(blockpos, ((CropsBlock) blockAboveDestination).getStateForAge(((CropsBlock) blockAboveDestination).getMaxAge()));
                    mainHandStack.shrink(1);
                } else if (BlockAboveDestinationState.getMaterial() == Material.AIR && blockAboveDestination.defaultBlockState().getBlock() == Blocks.FARMLAND) {


                    if (mainHandStack.getItem() == Items.WHEAT_SEEDS) {
                        this.host.level.setBlock(blockpos, Blocks.WHEAT.defaultBlockState(), 3);
                        mainHandStack.shrink(1);
                    } else if (mainHandStack.getItem() == Items.POTATO) {
                        this.host.level.setBlock(blockpos, Blocks.POTATOES.defaultBlockState(), 3);
                        mainHandStack.shrink(1);
                    } else if (mainHandStack.getItem() == Items.CARROT) {
                        this.host.level.setBlock(blockpos, Blocks.CARROTS.defaultBlockState(), 3);
                        mainHandStack.shrink(1);
                    } else if (mainHandStack.getItem() == Items.BEETROOT_SEEDS) {
                        this.host.level.setBlock(blockpos, Blocks.BEETROOTS.defaultBlockState(), 3);
                        mainHandStack.shrink(1);
                    }
                }
                this.breakingTime = (int) (this.breakingTime + mainHandStack.getDestroySpeed(BlockAboveDestinationState));
                int i = (int) ((float) (this.breakingTime) / 60.0F * 10);
                if (this.breakingTime % 2 == 0) {
                    if (blockAboveDestination.defaultBlockState().getMaterial() == Material.GRASS) {
                        this.host.level.playSound(null, blockpos.getX(), blockpos.getY(), blockpos.getZ(), SoundEvents.GRASS_HIT, this.host.getSoundSource(), 1F, 1F);
                    }
                    if (blockAboveDestination.defaultBlockState().getMaterial() == Material.DIRT || blockAboveDestination.defaultBlockState().getMaterial() == Material.CLAY) {
                        this.host.level.playSound(null, blockpos.getX(), blockpos.getY(), blockpos.getZ(), SoundEvents.GRAVEL_HIT, this.host.getSoundSource(), 1F, 1F);
                    }
                    if (blockAboveDestination.defaultBlockState().getMaterial() == Material.TOP_SNOW) {
                        this.host.level.playSound(null, blockpos.getX(), blockpos.getY(), blockpos.getZ(), SoundEvents.SNOW_HIT, this.host.getSoundSource(), 1F, 1F);
                    }
                    if (blockAboveDestination.defaultBlockState().getMaterial() == Material.SAND) {
                        this.host.level.playSound(null, blockpos.getX(), blockpos.getY(), blockpos.getZ(), SoundEvents.SAND_HIT, this.host.getSoundSource(), 1F, 1F);
                    }
                    if (blockAboveDestination.defaultBlockState().getMaterial() == Material.STONE || blockAboveDestination.defaultBlockState().getMaterial() == Material.ICE || blockAboveDestination.defaultBlockState().getMaterial() == Material.ICE_SOLID) {
                        this.host.level.playSound(null, blockpos.getX(), blockpos.getY(), blockpos.getZ(), SoundEvents.STONE_HIT, this.host.getSoundSource(), 1F, 1F);
                    }
                    if (blockAboveDestination.defaultBlockState().getMaterial() == Material.WOOD) {
                        this.host.level.playSound(null, blockpos.getX(), blockpos.getY(), blockpos.getZ(), SoundEvents.WOOD_HIT, this.host.getSoundSource(), 1F, 1F);
                    }
                    if (blockAboveDestination.defaultBlockState().getMaterial() == Material.WOOL) {
                        this.host.level.playSound(null, blockpos.getX(), blockpos.getY(), blockpos.getZ(), SoundEvents.WOOL_HIT, this.host.getSoundSource(), 1F, 1F);
                    }
                }
                if (i != this.previousBreakProgress) {
                    this.host.level.destroyBlockProgress(this.host.getId(), blockpos, i);
                    this.previousBreakProgress = i;
                    if (!this.host.swinging) {
                        this.host.swing(MAIN_HAND);
                    }
                }
                if (this.breakingTime > 60.0F) {
                    this.host.level.destroyBlock(blockpos, true);
                    mainHandStack.hurtAndBreak(1, this.host, b -> b.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
                    this.breakingTime = 0;
                }

                if (!shouldMoveTo(this.host.getCommandSenderWorld(), this.BlockPosBelowTarget)) {
                    this.BlockPosBelowTarget = null;
                }
            }
        }
    }

    private void BlockDropItem(ItemStack stack, float offsetY)
    {

        if(!stack.isEmpty())
        {
            ItemEntity entityitem = new ItemEntity(this.host.level, this.BlockPosBelowTarget.getX(), this.BlockPosBelowTarget.getY() + (double)offsetY, this.BlockPosBelowTarget.getZ(), stack);
            entityitem.setDefaultPickUpDelay();
            this.host.level.addFreshEntity(entityitem);
        }
    }

    private boolean isItemWorkable(Item item){
        return isItemSeed(item) || isItemTool(item);
    }

    private boolean isItemTool(Item item){
        return item instanceof ToolItem;
    }

    private boolean isItemSeed(Item item){
        return item == Items.WHEAT_SEEDS || item == Items.POTATO || item == Items.CARROT || item == Items.BEETROOT_SEEDS || item == Items.BONE_MEAL;
    }

}
