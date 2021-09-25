package com.yor42.projectazure.gameobject.entity.projectiles;

import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;

public class EntityMissileDroneMissile extends DamagingProjectileEntity {
    @Nullable
    protected LivingEntity targetEntity;
    protected static final DataParameter<Optional<UUID>> TARGET = EntityDataManager.createKey(EntityMissileDroneMissile.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    private static final double TURNLIMIT = 9 * Math.PI;

    public EntityMissileDroneMissile(EntityType<EntityMissileDroneMissile> p_i50173_1_, World p_i50173_2_) {
        super(p_i50173_1_, p_i50173_2_);

    }

    public void shoot(LivingEntity shooter, LivingEntity targetEntity, double x, double y, double z){
        this.setTarget(targetEntity);
        this.setShooter(shooter);
        this.setMotion(shooter.getLookVec().scale(0.5));
        BlockPos blockpos = shooter.getPosition().offset(shooter.getHorizontalFacing());
        double d0 = (double)blockpos.getX() + 0.5D;
        double d1 = (double)blockpos.getY() + 0.5D;
        double d2 = (double)blockpos.getZ() + 0.5D;
        this.setLocationAndAngles(d0, d1, d2, this.rotationYaw, this.rotationPitch);
        shooter.getEntityWorld().addEntity(this);
    }

    @Override
    public boolean isBurning() {
        return false;
    }

    public Optional<UUID> getTargetUUID(){
        return this.getDataManager().get(TARGET);
    }

    public void setTarget(@Nullable  LivingEntity entity){
        this.getDataManager().set(TARGET, Optional.ofNullable(entity != null ? entity.getUniqueID() : null));
        this.targetEntity = entity;
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.getDataManager().register(TARGET, Optional.empty());
    }

    @Override
    public void writeAdditional(CompoundNBT compoundNBT) {
        super.writeAdditional(compoundNBT);
        this.getTargetUUID().ifPresent((UUID)->compoundNBT.putUniqueId("target", UUID));
    }

    @Override
    public void readAdditional(@ParametersAreNonnullByDefault CompoundNBT compoundNBT) {
        super.readAdditional(compoundNBT);
        if(compoundNBT.contains("target")){
            this.getDataManager().set(TARGET, Optional.of(compoundNBT.getUniqueId("target")));
        }
    }

    @Override
    public void tick() {
        Entity entity = this.func_234616_v_();
        if (this.world.isRemote || (entity == null || !entity.removed) && this.world.isBlockLoaded(this.getPosition())) {
            super.tick();

            RayTraceResult raytraceresult = ProjectileHelper.func_234618_a_(this, this::func_230298_a_);
            if (raytraceresult.getType() != RayTraceResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                this.onImpact(raytraceresult);
            }

            this.doBlockCollisions();
            Vector3d vector3d = this.getMotion();
            double d0 = this.getPosX() + vector3d.x;
            double d1 = this.getPosY() + vector3d.y;
            double d2 = this.getPosZ() + vector3d.z;
            this.setPosition(d0, d1, d2);
        } else {
            this.remove();
        }

        if(!this.getEntityWorld().isRemote()){

            if(this.targetEntity != null){
                Vector3d motion = this.getMotion();
                double speed = motion.length();

                Vector3d v2 = new Vector3d(this.targetEntity.getPosX(), this.targetEntity.getPosY()+0.5, this.targetEntity.getPosZ()).subtract(new Vector3d(this.getPosX(), this.getPosY(), this.getPosZ())).normalize();
                Vector3d v1 = motion.normalize();

                double angle = Math.acos(v1.dotProduct(v2));
                Vector3d axis = v1.crossProduct(v2).normalize();

                //angle = Math.min(angle, MAX_TURN_ANGLE);

                if (angle < TURNLIMIT) {
                    motion = v2.scale(speed);
                    motion = v2.scale(speed);
                }else {
                    motion = MathUtil.rotateVector(v1, axis, TURNLIMIT).scale(speed);
                }

                this.setMotion(motion);
            }

            if (this.targetEntity == null && this.getTargetUUID().isPresent()) {
                Entity TargetEntity = ((ServerWorld)this.world).getEntityByUuid(this.getTargetUUID().get());
                if (this.targetEntity == null) {
                    this.setTarget(null);
                }
                else if(TargetEntity instanceof LivingEntity) {
                    this.targetEntity = (LivingEntity) TargetEntity;
                }
            }

            if(!this.targetEntity.isAlive()){
                this.getEntityWorld().createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), 0, Explosion.Mode.NONE);
                this.remove();
            }

            RayTraceResult raytraceresult = ProjectileHelper.func_234618_a_(this, this::func_230298_a_);
            if (raytraceresult.getType() != RayTraceResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                this.onImpact(raytraceresult);
            }

        }
        Vector3d vector3d = this.getMotion();
        /*

        double d4 = vector3d.y;
        float f1 = MathHelper.sqrt(horizontalMag(vector3d));
        this.rotationPitch = (float)(MathHelper.atan2(d4, f1) * (double)(180F / (float)Math.PI));
        this.rotationPitch = func_234614_e_(this.prevRotationPitch, this.rotationPitch);
        this.rotationYaw = func_234614_e_(this.prevRotationYaw, this.rotationYaw);

         */

        ProjectileHelper.rotateTowardsMovement(this, 0);
        if(this.getEntityWorld().isRemote()){
            this.world.addParticle(ParticleTypes.FLAME, this.getPosX() - vector3d.x, this.getPosY() - vector3d.y + 0.15D, this.getPosZ() - vector3d.z, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        super.onImpact(result);
        this.Detonate();
    }

    @Override
    protected float getMotionFactor() {
        return 1.05F;
    }

    @Override
    public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_) {
        this.remove();
        return true;
    }

    @Override
    public boolean isImmuneToFire() {
        return true;
    }

    @Override
    protected boolean isFireballFiery() {
        return false;
    }

    @Override
    protected void onEntityHit(EntityRayTraceResult result) {
        if(result.getEntity() != this.func_234616_v_()) {
            this.Detonate();
        }
    }

    private void Detonate(){
        this.world.createExplosion(this, this.getPosX(), getPosY(), getPosZ(), 1F, Explosion.Mode.BREAK);
        this.remove();
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
