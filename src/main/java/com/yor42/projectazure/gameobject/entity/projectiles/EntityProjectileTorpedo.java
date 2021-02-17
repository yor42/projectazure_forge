package com.yor42.projectazure.gameobject.entity.projectiles;

import com.yor42.projectazure.setup.register.registerManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.world.World;

public class EntityProjectileTorpedo extends DamagingProjectileEntity {

    public EntityProjectileTorpedo(double p_i50174_2_, double p_i50174_4_, double p_i50174_6_, double p_i50174_8_, double p_i50174_10_, double p_i50174_12_, World p_i50174_14_) {
        super(registerManager.PROJECTILETORPEDO, p_i50174_2_, p_i50174_4_, p_i50174_6_, p_i50174_8_, p_i50174_10_, p_i50174_12_, p_i50174_14_);
    }

    public EntityProjectileTorpedo(LivingEntity ShooterIn, double AccelX, double AccelY, double AccelZ, World WorldIn) {
        super(registerManager.PROJECTILETORPEDO, ShooterIn, AccelX, AccelY, AccelZ, WorldIn);
    }

    public EntityProjectileTorpedo(EntityType<EntityProjectileTorpedo> TypeIn, World world) {
        super(TypeIn, world);
    }

}
