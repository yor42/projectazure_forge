package com.yor42.projectazure.gameobject.entity.projectiles;

import com.tac.guns.common.Gun;
import com.tac.guns.entity.ProjectileEntity;
import com.tac.guns.item.GunItem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EntitySupernovaProjectile extends ProjectileEntity {
    public EntitySupernovaProjectile(EntityType<? extends ProjectileEntity> entityType, Level worldIn) {
        super(entityType, worldIn);
    }

    public EntitySupernovaProjectile(EntityType<? extends ProjectileEntity> entityType, Level worldIn, LivingEntity shooter, ItemStack weapon, GunItem item, Gun modifiedGun, float power, float randx, float randy) {
        super(entityType, worldIn, shooter, weapon, item, modifiedGun, randx, randy);
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        this.tickCount=Math.max(10, this.tickCount);
    }
}
