package com.yor42.projectazure.gameobject.entity.ai;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;


/**
 * Basically fork of Furenzu's work AI but with less intrusion and possibility for world break.
 *
 */

public class KansenWorkGoal extends KansenMoveEntityForWorkGoal {


    private int breakingTime;
    private int previousBreakProgress = -1;

    public KansenWorkGoal(AbstractEntityCompanion entity, double speed){
        super(entity, speed);
    }

    @Override
    public boolean shouldExecute() {
        Item handItem = this.host.getItemStackFromSlot(EquipmentSlotType.MAINHAND).getItem();

        if(!this.isItemWorkable(handItem)){
            return false;
        }
        if(!(this.host.getOwner() instanceof PlayerEntity)){
            return false;
        }

        return super.shouldExecute();
    }

    @Override
    boolean shouldMoveTo(IWorldReader worldIn, BlockPos pos) {
        Block targetBlock = worldIn.getBlockState(pos).getBlock();
        Item mainHandItem = this.host.getHeldItem(Hand.MAIN_HAND).getItem();
        BlockPos pos1 = pos.up();
        BlockState blockstate1blockabove = worldIn.getBlockState(pos1);

        if(this.isItemSeed(mainHandItem)){
            if(targetBlock == Blocks.FARMLAND){

                return blockstate1blockabove.getMaterial() == Material.AIR;
            }
        }
        else {
            BlockPos pos3 = pos.up();
            Block block = worldIn.getBlockState(pos3).getBlock();
            BlockState iblockstate = worldIn.getBlockState(pos3);
            if(pos3.getY() > 0)
            {
                if(mainHandItem == Items.BONE_MEAL)
                {
                    return block instanceof CropsBlock && !((CropsBlock) block).isMaxAge(iblockstate);
                }

                else if(mainHandItem instanceof HoeItem)
                {
                    return block == Blocks.MELON || block == Blocks.PUMPKIN || (block instanceof CropsBlock && ((CropsBlock) block).isMaxAge(iblockstate));
                }
                else if(mainHandItem.canHarvestBlock(iblockstate))
                {
                    if(mainHandItem instanceof PickaxeItem)
                    {
                        return block instanceof OreBlock;
                    }
                }

            }

        }
        return false;
    }

    @Override
    public boolean shouldContinueExecuting() {
        ItemStack handItemstack = this.host.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
        if(this.host.isSleeping() || handItemstack == ItemStack.EMPTY){
            return false;
        }
        return super.shouldContinueExecuting();
    }

    @Override
    public void tick() {
        super.tick();
        this.host.getLookController().setLookPosition((double)this.targetBlockPos.getX() + 0.5D, (double)(this.targetBlockPos.getY()), (double)this.targetBlockPos.getZ() + 0.5D, 10.0F, (float)this.host.getVerticalFaceSpeed());
        if(this.work){
            ItemStack mainHandStack = this.host.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
            BlockPos blockpos = this.targetBlockPos.up();
            BlockState BlockAboveDestinationState = this.host.world.getBlockState(blockpos);
            Block blockAboveDestination = BlockAboveDestinationState.getBlock();
            ItemStack item = blockAboveDestination.getItem(this.host.world, blockpos, BlockAboveDestinationState);

            if(mainHandStack.getItem() == Items.BONE_MEAL){
                this.host.world.playEvent(2005, blockpos, 0);
                this.host.world.setBlockState(blockpos, ((CropsBlock)blockAboveDestination).withAge(((CropsBlock)blockAboveDestination).getMaxAge()));
                mainHandStack.shrink(1);
            }

            else if(BlockAboveDestinationState.getMaterial() == Material.AIR&&blockAboveDestination.getDefaultState().getBlock() == Blocks.FARMLAND)
            {


                if(mainHandStack.getItem() == Items.WHEAT_SEEDS)
                {
                    this.host.world.setBlockState(blockpos, Blocks.WHEAT.getDefaultState(), 3);
                    mainHandStack.shrink(1);
                }
                else if(mainHandStack.getItem() == Items.POTATO)
                {
                    this.host.world.setBlockState(blockpos, Blocks.POTATOES.getDefaultState(), 3);
                    mainHandStack.shrink(1);
                }
                else if(mainHandStack.getItem() == Items.CARROT)
                {
                    this.host.world.setBlockState(blockpos, Blocks.CARROTS.getDefaultState(), 3);
                    mainHandStack.shrink(1);
                }
                else if(mainHandStack.getItem() == Items.BEETROOT_SEEDS)
                {
                    this.host.world.setBlockState(blockpos, Blocks.BEETROOTS.getDefaultState(), 3);
                    mainHandStack.shrink(1);
                }
            }
            this.breakingTime = (int)(this.breakingTime + mainHandStack.getDestroySpeed(BlockAboveDestinationState));
            int i = (int)((float)(this.breakingTime)/60.0F*10);
            if(this.breakingTime % 2 == 0)
            {
                if(blockAboveDestination.getDefaultState().getMaterial() == Material.ORGANIC)
                {
                    this.host.world.playSound(null, blockpos.getX(), blockpos.getY(), blockpos.getZ(), SoundEvents.BLOCK_GRASS_HIT, this.host.getSoundCategory(), 1F, 1F);
                }
                if(blockAboveDestination.getDefaultState().getMaterial() == Material.EARTH || blockAboveDestination.getDefaultState().getMaterial() == Material.CLAY)
                {
                    this.host.world.playSound(null, blockpos.getX(), blockpos.getY(), blockpos.getZ(), SoundEvents.BLOCK_GRAVEL_HIT, this.host.getSoundCategory(), 1F, 1F);
                }
                if(blockAboveDestination.getDefaultState().getMaterial() == Material.SNOW)
                {
                    this.host.world.playSound(null, blockpos.getX(), blockpos.getY(), blockpos.getZ(), SoundEvents.BLOCK_SNOW_HIT, this.host.getSoundCategory(), 1F, 1F);
                }
                if(blockAboveDestination.getDefaultState().getMaterial() == Material.SAND)
                {
                    this.host.world.playSound(null, blockpos.getX(), blockpos.getY(), blockpos.getZ(), SoundEvents.BLOCK_SAND_HIT, this.host.getSoundCategory(), 1F, 1F);
                }
                if(blockAboveDestination.getDefaultState().getMaterial() == Material.ROCK || blockAboveDestination.getDefaultState().getMaterial() == Material.ICE || blockAboveDestination.getDefaultState().getMaterial() == Material.PACKED_ICE)
                {
                    this.host.world.playSound(null, blockpos.getX(), blockpos.getY(), blockpos.getZ(), SoundEvents.BLOCK_STONE_HIT, this.host.getSoundCategory(), 1F, 1F);
                }
                if(blockAboveDestination.getDefaultState().getMaterial() == Material.WOOD)
                {
                    this.host.world.playSound(null, blockpos.getX(), blockpos.getY(), blockpos.getZ(), SoundEvents.BLOCK_WOOD_HIT, this.host.getSoundCategory(), 1F, 1F);
                }
                if(blockAboveDestination.getDefaultState().getMaterial() == Material.WOOL)
                {
                    this.host.world.playSound(null, blockpos.getX(), blockpos.getY(), blockpos.getZ(), SoundEvents.BLOCK_WOOL_HIT, this.host.getSoundCategory(), 1F, 1F);
                }
            }
            if(i != this.previousBreakProgress)
            {
                this.host.world.sendBlockBreakProgress(this.host.getEntityId(), blockpos, i);
                this.previousBreakProgress = i;
            }
            if(this.breakingTime > 60.0F)
            {
                this.host.world.destroyBlock(blockpos, true);
                mainHandStack.damageItem(1, this.host, b -> b.sendBreakAnimation(EquipmentSlotType.MAINHAND));
                this.breakingTime = 0;
            }
        }
    }

    private void BlockDropItem(ItemStack stack, float offsetY)
    {

        if(!stack.isEmpty())
        {
            ItemEntity entityitem = new ItemEntity(this.host.world, this.targetBlockPos.getX(), this.targetBlockPos.getY() + (double)offsetY, this.targetBlockPos.getZ(), stack);
            entityitem.setDefaultPickupDelay();
            this.host.world.addEntity(entityitem);
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
