package com.yor42.projectazure.gameobject.entity.ai.behaviors;

import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.gameobject.entity.ai.behaviors.base.ExtendedItemSwitchingBehavior;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.setup.register.RegisterAI;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

import static net.minecraft.world.entity.ai.memory.MemoryStatus.REGISTERED;

public class CompanionTorchBehavior extends ExtendedItemSwitchingBehavior<AbstractEntityCompanion> {

    public CompanionTorchBehavior(){
        cooldownFor((ety)->20);
        startCondition((entity)->entity.isOnGround() && !entity.level.canSeeSky(entity.blockPosition()) && entity.level.getBrightness(LightLayer.BLOCK, entity.blockPosition())<1);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, AbstractEntityCompanion entity) {
        boolean bool = getItemHand(entity, (stack)->stack.getItem() == Items.TORCH || stack.getItem() == Items.SOUL_TORCH) != null;

        if(!bool){
            this.ChangeItemwithMemory(RegisterAI.TORCH_INDEX.get(), entity);
        }
        return bool;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of(Pair.of(RegisterAI.TORCH_INDEX.get(), REGISTERED));
    }

    @Override
    protected void start(AbstractEntityCompanion entity) {
        InteractionHand hand = getItemHand(entity, (stack)->stack.getItem() == Items.TORCH || stack.getItem() == Items.SOUL_TORCH);

        if(hand == null){
            return;
        }

        ItemStack stack = entity.getItemInHand(hand);
        Item item = stack.getItem();
        Level world = entity.level;
        if (!(item instanceof BlockItem)) {
            return;
        }
        Vec3 vector3d = new Vec3(entity.getX(), entity.getEyeY(), entity.getZ());
        Vec3 vector3d2 = new Vec3(entity.getX(), entity.getY() - 4, entity.getZ());
        ClipContext rtx = new ClipContext(vector3d, vector3d2, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null);
        BlockHitResult result = world.clip(rtx);
        if (result.getType() == HitResult.Type.BLOCK) {
            BlockState state = ((BlockItem) item).getBlock().getStateForPlacement(new BlockPlaceContext(world, null, hand, stack, result));
            if (state != null) {
                world.setBlock(result.getBlockPos(), state, 2);
                stack.shrink(1);
                entity.swing(hand, true);
            }
        }
    }

    @Override
    protected InteractionHand SwapHand() {
        return InteractionHand.OFF_HAND;
    }
}
