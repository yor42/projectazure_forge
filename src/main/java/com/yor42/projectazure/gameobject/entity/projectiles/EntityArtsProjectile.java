package com.yor42.projectazure.gameobject.entity.projectiles;

import com.yor42.projectazure.gameobject.misc.DamageSources;
import com.yor42.projectazure.setup.register.registerManager;
import com.yor42.projectazure.setup.register.registerSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.Random;

public class EntityArtsProjectile extends DamagingProjectileEntity {
    private BlockPos originPos;
    public EntityArtsProjectile(EntityType<? extends DamagingProjectileEntity> entityType, World worldin) {
        super(entityType, worldin);
    }

    public EntityArtsProjectile(World worldIn, LivingEntity shooter){
        super(registerManager.PROJECTILEARTS_ENTITYTYPE, shooter, 0, 0, 0, worldIn);
        this.originPos = new BlockPos(shooter.getX(), shooter.getY(0.7F), shooter.getZ());
    }

    @Override
    protected float getInertia() {
        return 1.0F;
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    protected void onHit(RayTraceResult result) {
        BlockPos blockPosIn = this.blockPosition();
        Random random = new Random();
        super.onHit(result);
        if(!this.getCommandSenderWorld().getBlockState(blockPosIn).isPathfindable(this.getCommandSenderWorld(), blockPosIn, PathType.AIR)) {
            for (int i2 = 0; i2 < 8; ++i2) {
                this.level.addParticle(ParticleTypes.LARGE_SMOKE, (double) blockPosIn.getX() + random.nextDouble(), (double) blockPosIn.getY() + 1.2D, (double) blockPosIn.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
            }
            this.remove();
        }
    }

    @Override
    public void remove() {
        this.playSound(registerSounds.CHIMERA_PROJECTILE_HIT, 1F, 0.8F + this.level.random.nextFloat() * 0.4F);
        super.remove();
    }

    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vector3d vector3d = (new Vector3d(x, y, z)).normalize().add(this.random.nextGaussian() * (double)0.0075F * (double)inaccuracy, this.random.nextGaussian() * (double)0.0075F * (double)inaccuracy, this.random.nextGaussian() * (double)0.0075F * (double)inaccuracy).scale((double)velocity);
        this.setDeltaMovement(vector3d);
        float f = MathHelper.sqrt(getHorizontalDistanceSqr(vector3d));
        this.yRot = (float)(MathHelper.atan2(vector3d.x, vector3d.z) * (double)(180F / (float)Math.PI));
        this.xRot = (float)(MathHelper.atan2(vector3d.y, (double)f) * (double)(180F / (float)Math.PI));
        this.yRotO = this.yRot;
        this.xRotO = this.xRot;
    }

    @Override
    public void tick() {
        super.tick();
        if(this.tickCount == 600 || this.isInWater()){
            this.remove();
        }
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult result) {
        Entity target = result.getEntity();
        if(target != this.getOwner()) {
            double DistanceMultiplier = this.originPos != null ? Math.min(25 / distanceToSqr(this.originPos.getX(), this.originPos.getY(), this.originPos.getZ()), 1.0) : 1;
            target.hurt(DamageSources.causeArtsDamage(this, this.getOwner()), (float) (5 * DistanceMultiplier));
            this.remove();
        }
    }

}
