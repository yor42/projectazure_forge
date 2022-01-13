package com.yor42.projectazure.gameobject.entity.projectiles;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import net.minecraft.entity.projectile.AbstractArrowEntity.PickupStatus;

public class EntityPlasmaArrow extends AbstractArrowEntity {
    protected EntityPlasmaArrow(EntityType<? extends AbstractArrowEntity> type, World worldIn) {
        super(type, worldIn);
        this.pickup = PickupStatus.DISALLOWED;
    }

    protected EntityPlasmaArrow(EntityType<? extends AbstractArrowEntity> type, double x, double y, double z, World worldIn) {
        super(type, x, y, z, worldIn);
        this.pickup = PickupStatus.DISALLOWED;
    }

    protected EntityPlasmaArrow(EntityType<? extends AbstractArrowEntity> type, LivingEntity shooter, World worldIn) {
        super(type, shooter, worldIn);
        this.pickup = PickupStatus.DISALLOWED;
    }

    @Override
    protected ItemStack getPickupItem() {
        return null;
    }

    @Override
    protected void onHit(RayTraceResult result) {
        this.remove();
        super.onHit(result);
    }
}
