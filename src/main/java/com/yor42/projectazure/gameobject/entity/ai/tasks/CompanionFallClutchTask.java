package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.fluids.FluidUtil;
import staticnet.minecraft.world.item.Items.OFF_HAND;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Predicate;

import static com.yor42.projectazure.setup.register.RegisterAI.FALL_BREAK_ITEM_INDEX;
import static net.minecraft.fluid.Fluids.WATER;
import static net.minecraft.item.Items.COBWEB;

public class CompanionFallClutchTask extends Behavior<AbstractEntityCompanion> {
    @Nullable
    private InteractionHand hand;
    int ItemSwitchCooldown = 0;
    boolean success = false;
    //DREAM CLUTCH
    public static Predicate<ItemStack> IS_FALL_BREAKER = (stack)->{
        boolean isButerWacket = FluidUtil.getFluidContained(stack).map((fluid)->fluid.getFluid() == WATER && fluid.getAmount()>=1000).orElse(false);
        boolean fallbreakerBlock = stack.getItem() == COBWEB && stack.getItem() instanceof BoatItem;
        return isButerWacket || fallbreakerBlock;
    };

    public CompanionFallClutchTask() {
        super(ImmutableMap.of(FALL_BREAK_ITEM_INDEX.get(), MemoryStatus.REGISTERED));
    }

    @Override
    protected boolean checkExtraStartConditions(@Nonnull ServerLevel world, @Nonnull AbstractEntityCompanion entity) {

        if(this.ItemSwitchCooldown>0){
            this.ItemSwitchCooldown--;
            return false;
        }

        Optional<InteractionHand> hand = getFallBreakerHand(entity);
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
    protected void tick(ServerLevel world, AbstractEntityCompanion entity, long timestamp) {
        if(entity.fallDistance<4){
            doStop(world, entity, timestamp);
        }
        ClipContext ctx = new ClipContext(entity.position(), entity.position().add(0, -4, 0), ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, entity);
        BlockHitResult result = world.clip(ctx);
        HitResult.Type type = result.getType();
        if(type == HitResult.Type.BLOCK){
            BlockPos pos = result.getBlockPos();
            //if(!this.success && world.getBlockState(pos).getFluidState())
        }
    }

    @Override
    protected void stop(ServerLevel p_212835_1_, AbstractEntityCompanion entity, long p_212835_3_) {
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
    protected boolean canStillUse(ServerLevel p_212834_1_, AbstractEntityCompanion entity, long p_212834_3_) {
        return true;
    }



    private static Optional<InteractionHand> getFallBreakerHand(@Nonnull AbstractEntityCompanion entity){
        for(InteractionHand hand : InteractionHand.values()){
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
