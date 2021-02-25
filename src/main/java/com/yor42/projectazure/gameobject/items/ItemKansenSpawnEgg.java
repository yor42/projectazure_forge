package com.yor42.projectazure.gameobject.items;

import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityKansenBase;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class ItemKansenSpawnEgg extends itemBaseTooltip {

    LivingEntity Entity;

    public ItemKansenSpawnEgg(LivingEntity Entity, Properties properties) {
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
        if(this.Entity!=null) {
            this.Entity.setPosition(context.getPos().getX(), context.getPos().getY()+0.1, context.getPos().getZ());
            if(this.Entity instanceof EntityKansenBase) {
                ((EntityKansenBase) this.Entity).setTamedBy(context.getPlayer());
            }
            context.getWorld().addEntity(this.Entity);
            if(!context.getPlayer().isCreative())
                itemstack.shrink(1);
        }

        return ActionResultType.CONSUME;
    }

    //tbh its cube. not egg :omegalul:
}
