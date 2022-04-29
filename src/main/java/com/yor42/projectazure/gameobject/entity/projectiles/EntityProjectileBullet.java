package com.yor42.projectazure.gameobject.entity.projectiles;

import com.yor42.projectazure.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.Packet;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

import static com.yor42.projectazure.gameobject.misc.DamageSources.causeGunDamage;

public class EntityProjectileBullet extends ThrowableItemProjectile {

    float damage;

    protected EntityProjectileBullet(Level worldIn, float damage) {
        super(Main.GUN_BULLET.get(), worldIn);
        this.damage = damage;
    }

    protected EntityProjectileBullet(double x, double y, double z, Level worldIn, float damage) {
        super(Main.GUN_BULLET.get(), x, y, z, worldIn);
        this.damage = damage;
    }

    public EntityProjectileBullet(LivingEntity shooter, Level worldIn, float damage) {
        super(Main.GUN_BULLET.get(), shooter, worldIn);
        this.damage = damage;
    }

    public EntityProjectileBullet(EntityType<EntityProjectileBullet> type, Level world) {
        super(type, world);
    }

    @Override
    protected float getGravity() {
        return 0.001F;
    }

    @Override
    protected void onHitEntity(EntityHitResult rayTraceResult) {

        if(!level.isClientSide()) {
            Entity target = rayTraceResult.getEntity();
            Entity shooter = this.getOwner();
            target.hurt(causeGunDamage(this, shooter), this.damage);
            super.onHitEntity(rayTraceResult);
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    public void tick() {
        super.tick();
        ProjectileUtil.rotateTowardsMovement(this, 0.5F);
    }

    @Override
    protected void onHitBlock(BlockHitResult blockRayTraceResult) {
        if(this.getCommandSenderWorld().isClientSide()){
            this.addBlockHitEffects(blockRayTraceResult);
        }
        super.onHitBlock(blockRayTraceResult);
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        this.remove(RemovalReason.DISCARDED);
    }

    private void addBlockHitEffects(BlockHitResult result){
        Minecraft.getInstance().particleEngine.addBlockHitEffects(result.getBlockPos(), result);
    }

    public void ShootFromPlayer(Entity Shooter, float pitch, float yaw, float offset, float velocity, float inAccuracy, InteractionHand firingHand) {

        this.setPos(Shooter.getX(), Shooter.getEyeY()-0.1F, Shooter.getZ());

        float f = -Mth.sin(yaw * ((float)Math.PI / 180F)) * Mth.cos(pitch * ((float)Math.PI / 180F));
        float f1 = -Mth.sin((pitch + offset) * ((float)Math.PI / 180F));
        float f2 = Mth.cos(yaw * ((float)Math.PI / 180F)) * Mth.cos(pitch * ((float)Math.PI / 180F));
        this.shoot((double)f, (double)f1, (double)f2, velocity, inAccuracy);
        Vec3 vector3d = Shooter.getDeltaMovement();
        this.setDeltaMovement(this.getDeltaMovement().add(vector3d.x, Shooter.isOnGround() ? 0.0D : vector3d.y, vector3d.z));
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        super.shoot(x, y, z, velocity, inaccuracy);
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.AIR;
    }
}
