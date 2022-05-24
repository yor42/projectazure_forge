package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.setup.register.registerManager;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.item.BedItem;
import net.minecraft.item.BoatItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

import static com.yor42.projectazure.setup.register.registerManager.FALL_BREAK_ITEM_INDEX;
import static net.minecraft.fluid.Fluids.WATER;
import static net.minecraft.item.Items.COBWEB;
import static net.minecraft.util.Hand.OFF_HAND;

public class CompanionFallClutchTask extends Task<AbstractEntityCompanion> {
    @Nullable
    private Hand hand;
    int ItemSwitchCooldown = 0;
    boolean success = false;
    //DREAM CLUTCH
    public static Predicate<ItemStack> IS_FALL_BREAKER = (stack)->{
        boolean isButerWacket = FluidUtil.getFluidContained(stack).map((fluid)->fluid.getFluid() == WATER && fluid.getAmount()>=1000).orElse(false);
        boolean fallbreakerBlock = stack.getItem() == COBWEB && stack.getItem() instanceof BoatItem;
        return isButerWacket || fallbreakerBlock;
    };

    public CompanionFallClutchTask() {
        super(ImmutableMap.of(FALL_BREAK_ITEM_INDEX.get(), MemoryModuleStatus.REGISTERED));
    }

    @Override
    protected boolean checkExtraStartConditions(@Nonnull ServerWorld world, @Nonnull AbstractEntityCompanion entity) {

        if(this.ItemSwitchCooldown>0){
            this.ItemSwitchCooldown--;
            return false;
        }

        Optional<Hand> hand = getFallBreakerHand(entity);
        Brain<AbstractEntityCompanion> brain = entity.getBrain();
        if(hand.isPresent() && entity.fallDistance>=6){
            this.hand = hand.get();
            return true;
        }
        else if(entity.fallDistance>=4) {
            brain.getMemory(FALL_BREAK_ITEM_INDEX.get()).ifPresent((idx) -> {
                if (entity.getItemSwapIndexOffHand() == -1) {
                    this.ChangeItem(idx, entity);
                    this.ItemSwitchCooldown = 5;
                }
            });
        }
        return false;
    }

    @Override
    protected void tick(ServerWorld world, AbstractEntityCompanion entity, long timestamp) {
        if(entity.fallDistance<4){
            doStop(world, entity, timestamp);
        }
        RayTraceContext ctx = new RayTraceContext(entity.position(), entity.position().add(0, -4, 0), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.ANY, entity);
        BlockRayTraceResult result = world.clip(ctx);
        RayTraceResult.Type type = result.getType();
        if(type == RayTraceResult.Type.BLOCK){
            BlockPos pos = result.getBlockPos();
            //if(!this.success && world.getBlockState(pos).getFluidState())
        }
    }

    @Override
    protected void stop(ServerWorld p_212835_1_, AbstractEntityCompanion entity, long p_212835_3_) {
        if(entity.getItemSwapIndexOffHand()>-1) {
            ItemStack buffer = entity.getOffhandItem();
            entity.setItemInHand(OFF_HAND, entity.getInventory().getStackInSlot(entity.getItemSwapIndexOffHand()));
            entity.getInventory().setStackInSlot(entity.getItemSwapIndexOffHand(), buffer);
            entity.setItemSwapIndexOffHand(-1);
        }
        this.hand = null;
        success = false;
    }

    @Override
    protected boolean canStillUse(ServerWorld p_212834_1_, AbstractEntityCompanion entity, long p_212834_3_) {
        return true;
    }



    private static Optional<Hand> getFallBreakerHand(@Nonnull AbstractEntityCompanion entity){
        for(Hand hand : Hand.values()){
            if(IS_FALL_BREAKER.test(entity.getItemInHand(hand))){
                return Optional.of(hand);
            }
        }
        return Optional.empty();
    }

    private void ChangeItem(int index, AbstractEntityCompanion entity){
        ItemStack Buffer = entity.getOffhandItem();
        entity.setItemInHand(OFF_HAND, entity.getInventory().getStackInSlot(index));
        entity.getInventory().setStackInSlot(index, Buffer);
        entity.setItemSwapIndexOffHand(index);
    }
}
