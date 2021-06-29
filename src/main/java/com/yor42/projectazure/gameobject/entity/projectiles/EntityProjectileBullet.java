package com.yor42.projectazure.gameobject.entity.projectiles;

import com.yor42.projectazure.setup.register.registerManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import static com.yor42.projectazure.gameobject.misc.DamageSources.causeGunDamage;

public class EntityProjectileBullet extends ThrowableEntity {

    float damage;

    protected EntityProjectileBullet(World worldIn, float damage) {
        super(registerManager.PROJECTILE_GUN_BULLET, worldIn);
        this.damage = damage;
    }

    protected EntityProjectileBullet(double x, double y, double z, World worldIn, float damage) {
        super(registerManager.PROJECTILE_GUN_BULLET, x, y, z, worldIn);
        this.damage = damage;
    }

    public EntityProjectileBullet(LivingEntity shooter, World worldIn, float damage) {
        super(registerManager.PROJECTILE_GUN_BULLET, shooter, worldIn);
        this.damage = damage;
    }

    public EntityProjectileBullet(EntityType<EntityProjectileBullet> type, World world) {
        super(type, world);
    }

    @Override
    protected float getGravityVelocity() {
        return 0.001F;
    }

    @Override
    protected void onEntityHit(EntityRayTraceResult rayTraceResult) {

        if(!world.isRemote()) {
            Entity target = rayTraceResult.getEntity();
            Entity shooter = this.func_234616_v_();
            target.attackEntityFrom(causeGunDamage(this, shooter), this.damage);
            super.onEntityHit(rayTraceResult);
            this.remove();
        }
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        super.onImpact(result);
        this.remove();
    }

    public void ShootFromPlayer(Entity Shooter, float pitch, float yaw, float offset, float velocity, float inAccuracy, Hand firingHand) {

        this.setPosition(Shooter.getPosX(), Shooter.getPosYEye(), Shooter.getPosZ());

        float f = -MathHelper.sin(yaw * ((float)Math.PI / 180F)) * MathHelper.cos(pitch * ((float)Math.PI / 180F));
        float f1 = -MathHelper.sin((pitch + offset) * ((float)Math.PI / 180F));
        float f2 = MathHelper.cos(yaw * ((float)Math.PI / 180F)) * MathHelper.cos(pitch * ((float)Math.PI / 180F));
        this.shoot((double)f, (double)f1, (double)f2, velocity, inAccuracy);
        Vector3d vector3d = Shooter.getMotion();
        this.setMotion(this.getMotion().add(vector3d.x, Shooter.isOnGround() ? 0.0D : vector3d.y, vector3d.z));
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        super.shoot(x, y, z, velocity, inaccuracy);
    }

    @Override
    protected void registerData() {

    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
