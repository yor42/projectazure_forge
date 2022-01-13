package com.yor42.projectazure.gameobject.entity.projectiles;

import com.yor42.projectazure.setup.register.registerManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
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
    protected float getGravity() {
        return 0.001F;
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult rayTraceResult) {

        if(!level.isClientSide()) {
            Entity target = rayTraceResult.getEntity();
            Entity shooter = this.getOwner();
            target.hurt(causeGunDamage(this, shooter), this.damage);
            super.onHitEntity(rayTraceResult);
            this.remove();
        }
    }

    @Override
    public void tick() {
        super.tick();
        ProjectileHelper.rotateTowardsMovement(this, 0F);
    }

    @Override
    protected void onHitBlock(BlockRayTraceResult blockRayTraceResult) {
        if(this.getCommandSenderWorld().isClientSide()){
            this.addBlockHitEffects(blockRayTraceResult);
        }
        super.onHitBlock(blockRayTraceResult);
    }

    @Override
    protected void onHit(RayTraceResult result) {
        super.onHit(result);
        this.remove();
    }

    private void addBlockHitEffects(BlockRayTraceResult result){
        Minecraft.getInstance().particleEngine.addBlockHitEffects(result.getBlockPos(), result);
    }

    public void ShootFromPlayer(Entity Shooter, float pitch, float yaw, float offset, float velocity, float inAccuracy, Hand firingHand) {

        this.setPos(Shooter.getX(), Shooter.getEyeY()-0.1F, Shooter.getZ());

        float f = -MathHelper.sin(yaw * ((float)Math.PI / 180F)) * MathHelper.cos(pitch * ((float)Math.PI / 180F));
        float f1 = -MathHelper.sin((pitch + offset) * ((float)Math.PI / 180F));
        float f2 = MathHelper.cos(yaw * ((float)Math.PI / 180F)) * MathHelper.cos(pitch * ((float)Math.PI / 180F));
        this.shoot((double)f, (double)f1, (double)f2, velocity, inAccuracy);
        Vector3d vector3d = Shooter.getDeltaMovement();
        this.setDeltaMovement(this.getDeltaMovement().add(vector3d.x, Shooter.isOnGround() ? 0.0D : vector3d.y, vector3d.z));
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        super.shoot(x, y, z, velocity, inaccuracy);
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
