package com.yor42.projectazure.gameobject.entity.projectiles;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class EntityPlasmaArrow extends AbstractArrow {
    protected EntityPlasmaArrow(EntityType<? extends AbstractArrow> type, Level worldIn) {
        super(type, worldIn);
        this.pickup = Pickup.DISALLOWED;
    }

    protected EntityPlasmaArrow(EntityType<? extends AbstractArrow> type, double x, double y, double z, Level worldIn) {
        super(type, x, y, z, worldIn);
        this.pickup = Pickup.DISALLOWED;
    }

    protected EntityPlasmaArrow(EntityType<? extends AbstractArrow> type, LivingEntity shooter, Level worldIn) {
        super(type, shooter, worldIn);
        this.pickup = Pickup.DISALLOWED;
    }

    @Override
    protected ItemStack getPickupItem() {
        return null;
    }

    @Override
    protected void onHit(HitResult result) {
        this.discard();
        super.onHit(result);
    }
}
