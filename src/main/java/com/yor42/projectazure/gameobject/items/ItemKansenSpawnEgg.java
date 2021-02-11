package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.gameobject.entity.EntityKansenBase;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Objects;

public class ItemKansenSpawnEgg extends itemBaseTooltip {

    EntityType<?> Entity;

    public ItemKansenSpawnEgg(EntityType<?> Entity, Properties properties) {
        super(properties);
        this.Entity = Entity;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();

        if(context.getPlayer() == null)
            return ActionResultType.FAIL;

        if (!(world instanceof ServerWorld)) {
            return ActionResultType.SUCCESS;
        }
        ItemStack itemstack = context.getItem();
        BlockPos blockpos = context.getPos();
        Direction direction = context.getFace();
        BlockState blockstate = world.getBlockState(blockpos);
        BlockPos blockpos1;
        if (blockstate.getCollisionShape(world, blockpos).isEmpty()) {
            blockpos1 = blockpos;
        } else {
            blockpos1 = blockpos.offset(direction);
        }
        EntityKansenBase spawnedEntity = (EntityKansenBase) this.Entity.spawn((ServerWorld) context.getWorld(), itemstack, context.getPlayer(), blockpos1, SpawnReason.SPAWN_EGG, true, !Objects.equals(blockpos, blockpos1) && direction == Direction.UP);
        if(spawnedEntity!=null) {
            spawnedEntity.setTamedBy(context.getPlayer());
            if(!context.getPlayer().isCreative())
                itemstack.shrink(1);
        }

        return ActionResultType.CONSUME;
    }

    //tbh its cube. not egg :omegalul:
}
