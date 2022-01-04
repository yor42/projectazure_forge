package com.yor42.projectazure.gameobject.entity.projectiles;

import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityKansenBase;
import com.yor42.projectazure.gameobject.misc.DamageSources;
import com.yor42.projectazure.setup.register.registerManager;
import com.yor42.projectazure.setup.register.registerSounds;
import net.minecraft.client.particle.Particle;
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
import net.minecraft.world.Explosion;
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
        this.originPos = new BlockPos(shooter.getPosX(), shooter.getPosYHeight(0.7F), shooter.getPosZ());
    }

    @Override
    protected float getMotionFactor() {
        return 1.0F;
    }

    @Override
    protected boolean isFireballFiery() {
        return false;
    }

    @Override
    public boolean isBurning() {
        return false;
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        BlockPos blockPosIn = this.getPosition();
        Random random = new Random();
        super.onImpact(result);
        if(!this.getEntityWorld().getBlockState(blockPosIn).allowsMovement(this.getEntityWorld(), blockPosIn, PathType.AIR)) {
            for (int i2 = 0; i2 < 8; ++i2) {
                this.world.addParticle(ParticleTypes.LARGE_SMOKE, (double) blockPosIn.getX() + random.nextDouble(), (double) blockPosIn.getY() + 1.2D, (double) blockPosIn.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
            }
            this.remove();
        }
    }

    @Override
    public void remove() {
        this.playSound(registerSounds.CHIMERA_PROJECTILE_HIT, 1F, 0.8F + this.world.rand.nextFloat() * 0.4F);
        super.remove();
    }

    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vector3d vector3d = (new Vector3d(x, y, z)).normalize().add(this.rand.nextGaussian() * (double)0.0075F * (double)inaccuracy, this.rand.nextGaussian() * (double)0.0075F * (double)inaccuracy, this.rand.nextGaussian() * (double)0.0075F * (double)inaccuracy).scale((double)velocity);
        this.setMotion(vector3d);
        float f = MathHelper.sqrt(horizontalMag(vector3d));
        this.rotationYaw = (float)(MathHelper.atan2(vector3d.x, vector3d.z) * (double)(180F / (float)Math.PI));
        this.rotationPitch = (float)(MathHelper.atan2(vector3d.y, (double)f) * (double)(180F / (float)Math.PI));
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
    }

    @Override
    public void tick() {
        super.tick();
        if(this.ticksExisted == 600 || this.isInWater()){
            this.remove();
        }
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void onEntityHit(EntityRayTraceResult result) {
        Entity target = result.getEntity();
        if(target != this.func_234616_v_()) {
            double DistanceMultiplier = this.originPos != null ? Math.min(25 / getDistanceSq(this.originPos.getX(), this.originPos.getY(), this.originPos.getZ()), 1.0) : 1;
            target.attackEntityFrom(DamageSources.causeArtsDamage(this, this.func_234616_v_()), (float) (5 * DistanceMultiplier));
            this.remove();
        }
    }

}
