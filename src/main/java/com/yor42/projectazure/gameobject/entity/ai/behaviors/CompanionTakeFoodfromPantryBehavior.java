package com.yor42.projectazure.gameobject.entity.ai.behaviors;

import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.gameobject.blocks.PantryBlock;
import com.yor42.projectazure.gameobject.blocks.tileentity.TileEntityPantry;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraftforge.items.CapabilityItemHandler;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;

import static com.yor42.projectazure.setup.register.RegisterAI.FOOD_INDEX;
import static com.yor42.projectazure.setup.register.RegisterAI.FOOD_PANTRY;

public class CompanionTakeFoodfromPantryBehavior extends ExtendedBehaviour<AbstractEntityCompanion> {
    private long chestopenTimestamp;
    int emptyinvslot = -1;

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of(Pair.of(FOOD_PANTRY.get(), MemoryStatus.VALUE_PRESENT), Pair.of(FOOD_INDEX.get(), MemoryStatus.VALUE_ABSENT));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, AbstractEntityCompanion entity) {

        GlobalPos pantry = BrainUtils.getMemory(entity, FOOD_PANTRY.get());

        if(pantry == null || pantry.dimension() != entity.level.dimension()){
            return false;
        }

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

        BlockPos target = pantry.pos();

        BlockEntity te = level.getBlockEntity(target);
        if(te == null || !te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent()){
            BrainUtils.clearMemory(entity, FOOD_PANTRY.get());
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

        Path path = entity.getNavigation().createPath(target, 3);

        return Math.sqrt(entity.distanceToSqr(target.getX(), target.getY(), target.getZ())) <3 || (path != null && path.canReach());
    }

    @Override
    protected boolean canStillUse(ServerLevel level, AbstractEntityCompanion entity, long gameTime) {
        GlobalPos pantry = BrainUtils.getMemory(entity, FOOD_PANTRY.get());

        if(pantry == null || pantry.dimension() != entity.level.dimension()){
            return false;
        }

        BlockPos target = pantry.pos();
        return Math.sqrt(entity.distanceToSqr(target.getX(), target.getY(), target.getZ())) <3;
    }

    @Override
    protected void tick(AbstractEntityCompanion entity) {
        GlobalPos pantry = BrainUtils.getMemory(entity, FOOD_PANTRY.get());
        Level world = entity.level;

        if(pantry == null || pantry.dimension() != entity.level.dimension()){
            return;
        }

        BlockPos target = pantry.pos();
        BlockEntity te = world.getBlockEntity(target);

        if(te == null || !te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent()){
            BrainUtils.clearMemory(entity, FOOD_PANTRY.get());
            this.doStop((ServerLevel) world, entity, world.getGameTime());
        }
        else if(this.chestopenTimestamp<0 && te instanceof TileEntityPantry){
            entity.swing(InteractionHand.MAIN_HAND);
            this.chestopenTimestamp = world.getGameTime();
            playSound(target, (ServerLevel) world, SoundEvents.CHEST_OPEN);
            BlockState state = world.getBlockState(target);
            world.setBlock(target, state.setValue(PantryBlock.OPEN, true), 2);
        }else if(entity.level.getGameTime()-this.chestopenTimestamp>40){
            te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent((inventory)->{
                for(int i=0; i<inventory.getSlots(); i++){
                    ItemStack stack = inventory.getStackInSlot(i);
                    if(isFood(stack)){
                        if(this.emptyinvslot >-1) {
                            if (entity.getInventory().insertItem(this.emptyinvslot, inventory.extractItem(i, stack.getCount(), true), true).isEmpty()) {
                                entity.getInventory().insertItem(this.emptyinvslot, inventory.extractItem(i, stack.getCount(), false), false);
                                BrainUtils.setMemory(entity, FOOD_INDEX.get(), this.emptyinvslot);
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
                                BrainUtils.setMemory(entity, FOOD_INDEX.get(), j);
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
            playSound(target, (ServerLevel) world, SoundEvents.CHEST_CLOSE);
            BlockState state = world.getBlockState(target);
            world.setBlock(target, state.setValue(PantryBlock.OPEN, false), 2);
        }

    }

    @Override
    protected void stop(AbstractEntityCompanion entity) {
        super.stop(entity);
        this.chestopenTimestamp = -1;
        this.emptyinvslot = -1;
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
}
