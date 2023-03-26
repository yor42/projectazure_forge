package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.gameobject.blocks.PantryBlock;
import com.yor42.projectazure.gameobject.blocks.tileentity.TileEntityPantry;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.items.CapabilityItemHandler;

import static com.yor42.projectazure.setup.register.RegisterAI.*;

public class CompanionTakeFoodFromPantryGoal extends Behavior<AbstractEntityCompanion> {

    private long chestopenTimestamp;
    int emptyinvslot = -1;

    public CompanionTakeFoodFromPantryGoal() {
        super(ImmutableMap.of(FOOD_PANTRY.get(), MemoryStatus.VALUE_PRESENT, FOOD_INDEX.get(), MemoryStatus.VALUE_ABSENT), 2400);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel world, AbstractEntityCompanion entity) {
        Brain<AbstractEntityCompanion> brain = entity.getBrain();

        for(int i=0; i<entity.getInventory().getSlots();i++){
            ItemStack stack = entity.getInventory().getStackInSlot(i);
            if(stack.isEmpty()){
                this.emptyinvslot = i;
                break;
            }
        }

        if(this.emptyinvslot<0){
            return false;
        }

        if(brain.hasMemoryValue(FOOD_INDEX.get())){
            return false;
        }

        GlobalPos pos = brain.getMemory(FOOD_PANTRY.get()).get();
        LivingEntity owner = entity.getOwner();
        BlockPos target = pos.pos();
        if(!pos.dimension().equals(world.dimension())){
            return false;
        }
        if(owner == null){
            return false;
        }
        BlockEntity te = world.getBlockEntity(target);
        if(te == null || !te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent()){
            brain.eraseMemory(FOOD_PANTRY.get());
            return false;
        }

        if(te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).map((inventory)->{
            for(int i=0; i<inventory.getSlots(); i++){
                ItemStack stack = inventory.getStackInSlot(i);
                if(isFood(stack)){
                    return false;
                }
            }
            return true;
        }).orElse(true)){
            return false;
        }

        if(brain.getActiveNonCoreActivity().map((activity)->activity==FOLLOWING_OWNER.get()).orElse(false)){
            boolean isCloseEnough = Math.sqrt(owner.distanceToSqr(target.getX(), target.getY(), target.getZ()))<48;
            return isCloseEnough;
        }

        return Math.sqrt(entity.distanceToSqr(target.getX(), target.getY(), target.getZ()))<48;
    }

    @Override
    protected void start(ServerLevel p_212831_1_, AbstractEntityCompanion entity, long p_212831_3_) {
        Brain<AbstractEntityCompanion> brain = entity.getBrain();
        GlobalPos pos = brain.getMemory(FOOD_PANTRY.get()).get();
        BlockPos target = pos.pos();
        if(!target.closerThan(entity.blockPosition(), 3)) {
            BehaviorUtils.setWalkAndLookTargetMemories(entity, target, 1, 2);
        }
        else{
            brain.eraseMemory(MemoryModuleType.WALK_TARGET);
        }
    }

    @Override
    protected void tick(ServerLevel world, AbstractEntityCompanion entity, long tickcount) {
        Brain<AbstractEntityCompanion> brain = entity.getBrain();
        GlobalPos pos = brain.getMemory(FOOD_PANTRY.get()).get();
        BlockPos target = pos.pos();
        BlockEntity te = world.getBlockEntity(target);

        if(te == null || !te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent()){
            this.doStop(world, entity, tickcount);
            brain.eraseMemory(FOOD_PANTRY.get());
        }

        if(!target.closerThan(entity.blockPosition(), 3)){
            return;
        }

        if(this.chestopenTimestamp<0){
            if(te instanceof TileEntityPantry){
                entity.swing(InteractionHand.MAIN_HAND);
                this.chestopenTimestamp = tickcount;
                playSound(target, world, SoundEvents.CHEST_OPEN);
                BlockState state = world.getBlockState(target);
                world.setBlock(target, state.setValue(PantryBlock.OPEN, true), 2);
            }
        }
        else if(tickcount-this.chestopenTimestamp>40){
            te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent((inventory)->{
                for(int i=0; i<inventory.getSlots(); i++){
                    ItemStack stack = inventory.getStackInSlot(i);
                    if(isFood(stack)){
                        if(this.emptyinvslot >-1) {
                            if (entity.getInventory().insertItem(this.emptyinvslot, inventory.extractItem(i, stack.getCount(), true), true).isEmpty()) {
                                entity.getInventory().insertItem(this.emptyinvslot, inventory.extractItem(i, stack.getCount(), false), false);
                                brain.setMemory(FOOD_INDEX.get(), this.emptyinvslot);
                                break;
                            }
                            else {
                                this.emptyinvslot = -1;
                            }
                        }
                        boolean transfersuccessful = false;
                        for (int j = 0; i < entity.getInventory().getSlots(); i++) {
                            if(entity.getInventory().insertItem(j, inventory.extractItem(i, stack.getCount(), true), true).isEmpty()){
                                entity.getInventory().insertItem(j, inventory.extractItem(i, stack.getCount(), false), false);
                                brain.setMemory(FOOD_INDEX.get(), j);
                                transfersuccessful = true;
                                break;
                            }
                        }
                        if(transfersuccessful){
                            break;
                        }
                    }
                }
            });
            playSound(target, world, SoundEvents.CHEST_CLOSE);
            BlockState state = world.getBlockState(target);
            world.setBlock(target, state.setValue(PantryBlock.OPEN, false), 2);
        }

        super.tick(world, entity, tickcount);
    }

    private static boolean isFood(ItemStack stack){

        if(stack.isEmpty()){
            return false;
        }

        FoodProperties food = stack.getItem().getFoodProperties();
        if(food == null){
            return false;
        }

        if(food.getEffects().isEmpty()){
            return true;
        }
        else{
            for(Pair<MobEffectInstance, Float> effect : food.getEffects()){
                if(effect.getFirst().getEffect().getCategory() == MobEffectCategory.HARMFUL){
                    return false;
                }
            }
        }

        return true;
    }


    private static void playSound(BlockPos pos, ServerLevel world, SoundEvent p_213965_2_) {
        Vec3i vector3i = world.getBlockState(pos).getValue(PantryBlock.FACING).getNormal();
        double d0 = (double)pos.getX() + 0.5D + (double)vector3i.getX() / 2.0D;
        double d1 = (double)pos.getY() + 0.5D + (double)vector3i.getY() / 2.0D;
        double d2 = (double)pos.getZ() + 0.5D + (double)vector3i.getZ() / 2.0D;
        world.playSound(null, d0, d1, d2, p_213965_2_, SoundSource.BLOCKS, 0.5F, world.random.nextFloat() * 0.1F + 1.5F);
    }

    @Override
    protected void stop(ServerLevel p_212835_1_, AbstractEntityCompanion p_212835_2_, long p_212835_3_) {
        super.stop(p_212835_1_, p_212835_2_, p_212835_3_);
        this.chestopenTimestamp = -1;
        this.emptyinvslot = -1;
    }

    @Override
    protected boolean canStillUse(ServerLevel p_212834_1_, AbstractEntityCompanion p_212834_2_, long p_212834_3_) {
        return this.checkExtraStartConditions(p_212834_1_, p_212834_2_);
    }
}
