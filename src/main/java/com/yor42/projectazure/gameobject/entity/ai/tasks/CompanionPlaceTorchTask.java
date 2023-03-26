package com.yor42.projectazure.gameobject.entity.ai.tasks;

import com.google.common.collect.ImmutableMap;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.setup.register.RegisterAI;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LightLayer;
import net.minecraft.server.level.ServerLevel;

import java.util.Optional;

import static com.yor42.projectazure.setup.register.RegisterAI.TORCH_INDEX;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;

public class CompanionPlaceTorchTask extends Behavior<AbstractEntityCompanion> {

    private int Cooldown =0;

    public CompanionPlaceTorchTask() {
        super(ImmutableMap.of(RegisterAI.TORCH_INDEX.get(), MemoryStatus.REGISTERED));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel world, AbstractEntityCompanion entity) {

        if (this.Cooldown > 0) {
            this.Cooldown--;
            return false;
        }
        else {
            if (this.getTorchHand(entity).isPresent()) {
                return !world.canSeeSky(entity.blockPosition()) && world.getBrightness(LightLayer.BLOCK, entity.blockPosition())<1;
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
    protected void start(ServerLevel world, AbstractEntityCompanion entity, long p_212831_3_) {
        this.getTorchHand(entity).ifPresent((hand) -> {
            ItemStack stack = entity.getItemInHand(hand);
            Item item = stack.getItem();
            if(item instanceof BlockItem){
                Vec3 vector3d = new Vec3(entity.getX(), entity.getEyeY(), entity.getZ());
                Vec3 vector3d2 = new Vec3(entity.getX(), entity.getY()-4, entity.getZ());
                ClipContext rtx =new ClipContext(vector3d, vector3d2, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null);
                BlockHitResult result = world.clip(rtx);
                if(result.getType() == HitResult.Type.BLOCK) {
                    BlockState state = ((BlockItem) item).getBlock().getStateForPlacement(new BlockPlaceContext(world, null, hand, stack, result));
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
    protected void stop(ServerLevel p_212835_1_, AbstractEntityCompanion entity, long p_212835_3_) {
        if(entity.getItemSwapIndexOffHand()>-1) {
            ItemStack buffer = entity.getOffhandItem();
            entity.setItemInHand(InteractionHand.OFF_HAND, entity.getInventory().getStackInSlot(entity.getItemSwapIndexOffHand()));
            entity.getInventory().setStackInSlot(entity.getItemSwapIndexOffHand(), buffer);
            entity.setItemSwapIndexOffHand(-1);
        }
    }

    private Optional<InteractionHand> getTorchHand(AbstractEntityCompanion entity){
        for(InteractionHand hand : InteractionHand.values()){
            Item item = entity.getItemInHand(hand).getItem();
            if(item == Items.TORCH || item == Items.SOUL_TORCH){
                return Optional.of(hand);
            }
        }
        return Optional.empty();
    }

    private void ChangeItem(int index, AbstractEntityCompanion entity){
        ItemStack Buffer = entity.getOffhandItem();
        entity.setItemInHand(InteractionHand.OFF_HAND, entity.getInventory().getStackInSlot(index));
        entity.getInventory().setStackInSlot(index, Buffer);
        entity.setItemSwapIndexOffHand(index);
    }


}
