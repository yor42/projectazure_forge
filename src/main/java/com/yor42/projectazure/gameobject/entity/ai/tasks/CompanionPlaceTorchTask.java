package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.tac.guns.item.GunItem;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;

import com.yor42.projectazure.setup.register.registerManager;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.item.*;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.LightType;
import net.minecraft.world.server.ServerWorld;

import java.util.Optional;

import static com.yor42.projectazure.setup.register.registerManager.TORCH_INDEX;
import static net.minecraft.util.Hand.MAIN_HAND;
import static net.minecraft.util.Hand.OFF_HAND;

public class CompanionPlaceTorchTask extends Task<AbstractEntityCompanion> {

    private int Cooldown =0;

    public CompanionPlaceTorchTask() {
        super(ImmutableMap.of(registerManager.TORCH_INDEX.get(), MemoryModuleStatus.REGISTERED));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerWorld world, AbstractEntityCompanion entity) {

        if (this.Cooldown > 0) {
            this.Cooldown--;
            return false;
        }
        else {
            if (this.getTorchHand(entity).isPresent()) {
                return !world.canSeeSky(entity.blockPosition()) && world.getBrightness(LightType.BLOCK, entity.blockPosition())<1;
            } else {
                entity.getBrain().getMemory(TORCH_INDEX.get()).ifPresent((idx) -> {
                    if (entity.getItemSwapIndexOffHand() == -1) {
                        this.ChangeItem(idx, entity);
                        this.Cooldown = 20;
                    }
                });
            }
        }
        return false;
    }

    @Override
    protected void start(ServerWorld world, AbstractEntityCompanion entity, long p_212831_3_) {
        this.getTorchHand(entity).ifPresent((hand) -> {
            ItemStack stack = entity.getItemInHand(hand);
            Item item = stack.getItem();
            if(item instanceof BlockItem){
                Vector3d vector3d = new Vector3d(entity.getX(), entity.getEyeY(), entity.getZ());
                Vector3d vector3d2 = new Vector3d(entity.getX(), entity.getY()-4, entity.getZ());
                RayTraceContext rtx =new RayTraceContext(vector3d, vector3d2, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, null);
                BlockRayTraceResult result = world.clip(rtx);
                if(result.getType() == RayTraceResult.Type.BLOCK) {
                    BlockState state = ((BlockItem) item).getBlock().getStateForPlacement(new BlockItemUseContext(world, null, hand, stack, result));
                    if (state != null) {
                        world.setBlock(result.getBlockPos(), state, 2);
                        stack.shrink(1);
                        entity.swing(hand, true);
                    }
                }
            }
        });
    }

    @Override
    protected void stop(ServerWorld p_212835_1_, AbstractEntityCompanion entity, long p_212835_3_) {
        if(entity.getItemSwapIndexOffHand()>-1) {
            ItemStack buffer = entity.getOffhandItem();
            entity.setItemInHand(OFF_HAND, entity.getInventory().getStackInSlot(entity.getItemSwapIndexOffHand()));
            entity.getInventory().setStackInSlot(entity.getItemSwapIndexOffHand(), buffer);
            entity.setItemSwapIndexOffHand(-1);
        }
    }

    private Optional<Hand> getTorchHand(AbstractEntityCompanion entity){
        for(Hand hand : Hand.values()){
            Item item = entity.getItemInHand(hand).getItem();
            if(item == Items.TORCH || item == Items.SOUL_TORCH){
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
