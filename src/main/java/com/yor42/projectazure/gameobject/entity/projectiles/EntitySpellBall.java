package com.yor42.projectazure.gameobject.entity.projectiles;

import com.yor42.projectazure.libs.utils.AmmoProperties;
import com.yor42.projectazure.setup.register.registerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;
import java.util.Random;

public class EntitySpellBall extends DamagingProjectileEntity {
    private BlockPos originPos;
    public EntitySpellBall(EntityType<? extends DamagingProjectileEntity> p_i50173_1_, World p_i50173_2_) {
        super(p_i50173_1_, p_i50173_2_);
    }

    public EntitySpellBall(World p_i50173_2_, LivingEntity shooter) {
        this(p_i50173_2_, shooter,0,0,0,null);
    }

    public EntitySpellBall(World worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ, AmmoProperties properties){
        super(registerEntity.PROJECTILE_SPELLBALL.get(), shooter, accelX, accelY, accelZ, worldIn);
        this.originPos = new BlockPos(shooter.getX(), shooter.getY(0.5), shooter.getZ());
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

    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vector3d vector3d = (new Vector3d(x, y, z)).normalize().add(this.random.nextGaussian() * (double)0.0075F * (double)inaccuracy, this.random.nextGaussian() * (double)0.0075F * (double)inaccuracy, this.random.nextGaussian() * (double)0.0075F * (double)inaccuracy).scale(velocity);
        this.setDeltaMovement(vector3d);
        float f = MathHelper.sqrt(getHorizontalDistanceSqr(vector3d));
        this.yRot = (float)(MathHelper.atan2(vector3d.x, vector3d.z) * (double)(180F / (float)Math.PI));
        this.xRot = (float)(MathHelper.atan2(vector3d.y, f) * (double)(180F / (float)Math.PI));
        this.yRotO = this.yRot;
        this.xRotO = this.xRot;
    }

    @Override
    protected void onHit(RayTraceResult result) {
        BlockPos blockPosIn = this.blockPosition();
        Random random = new Random();
        super.onHit(result);
        if(!this.getCommandSenderWorld().getBlockState(blockPosIn).isPathfindable(this.getCommandSenderWorld(), blockPosIn, PathType.AIR)) {
            for (int i2 = 0; i2 < 8; ++i2) {
                this.level.addParticle(ParticleTypes.PORTAL, (double) blockPosIn.getX() + random.nextDouble(), (double) blockPosIn.getY() + 1.2D, (double) blockPosIn.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
            }
            this.remove();
        }
        this.remove();
    }

    @Override
    protected void onHitBlock(BlockRayTraceResult p_230299_1_) {
        BlockPos blockPosIn = this.blockPosition();
        Random random = new Random();
        if(!this.getCommandSenderWorld().getBlockState(blockPosIn).isPathfindable(this.getCommandSenderWorld(), blockPosIn, PathType.AIR)) {
            for (int i2 = 0; i2 < 8; ++i2) {
                this.level.addParticle(ParticleTypes.PORTAL, (double) blockPosIn.getX() + random.nextDouble(), (double) blockPosIn.getY() + 1.2D, (double) blockPosIn.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
            }
            this.remove();
        }
        this.remove();
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult result) {
        Entity target = result.getEntity();
        if(target != this.getOwner() && target instanceof LivingEntity) {
            double DistanceMultiplier = this.originPos != null ? Math.min(25 / distanceToSqr(this.originPos.getX(), this.originPos.getY(), this.originPos.getZ()), 1.0) : 1;
            target.hurt(DamageSource.MAGIC, (float) (5 * DistanceMultiplier));

            this.remove();
        }
    }

    @Override
    public boolean hurt(DamageSource p_70097_1_, float p_70097_2_) {
        if(p_70097_2_>=8) {
            for (int i2 = 0; i2 < 8; ++i2) {
                this.level.addParticle(ParticleTypes.PORTAL, this.getX() + random.nextDouble(), this.getY() + 1.2D, this.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
            }
            this.remove();
        }

        return false;
    }

    @Nonnull
    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        if(this.tickCount == 600){
            this.remove();
        }
        super.tick();
    }

}
