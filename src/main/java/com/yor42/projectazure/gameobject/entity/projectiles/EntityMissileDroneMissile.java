package com.yor42.projectazure.gameobject.entity.projectiles;

import com.yor42.projectazure.libs.utils.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.Level;
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
    protected static final DataParameter<Optional<UUID>> TARGET = EntityDataManager.defineId(EntityMissileDroneMissile.class, DataSerializers.OPTIONAL_UUID);
    private static final double TURNLIMIT = 9 * Math.PI;

    public EntityMissileDroneMissile(EntityType<EntityMissileDroneMissile> p_i50173_1_, Level p_i50173_2_) {
        super(p_i50173_1_, p_i50173_2_);
    }

    public void shoot(LivingEntity shooter, LivingEntity targetEntity, double x, double y, double z){
        this.setTarget(targetEntity);
        this.setOwner(shooter);
        this.setDeltaMovement(shooter.getLookAngle().scale(0.5));
        BlockPos blockpos = shooter.blockPosition().relative(shooter.getDirection());
        double d0 = (double)blockpos.getX() + 0.5D;
        double d1 = (double)blockpos.getY() + 0.5D;
        double d2 = (double)blockpos.getZ() + 0.5D;
        this.moveTo(d0, d1, d2, this.yRot, this.xRot);
        shooter.getCommandSenderWorld().addFreshEntity(this);
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    public Optional<UUID> getTargetUUID(){
        return this.getEntityData().get(TARGET);
    }

    public void setTarget(@Nullable  LivingEntity entity){
        this.getEntityData().set(TARGET, Optional.ofNullable(entity != null ? entity.getUUID() : null));
        this.targetEntity = entity;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(TARGET, Optional.empty());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        this.getTargetUUID().ifPresent((UUID)->compoundNBT.putUUID("target", UUID));
    }

    @Override
    public void readAdditionalSaveData(@ParametersAreNonnullByDefault CompoundTag compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        if(compoundNBT.contains("target")){
            this.getEntityData().set(TARGET, Optional.of(compoundNBT.getUUID("target")));
        }
    }

    @Override
    public void tick() {
        Entity entity = this.getOwner();
        if (this.level.isClientSide || (entity == null || !entity.removed) && this.level.hasChunkAt(this.blockPosition())) {
            super.tick();

            RayTraceResult raytraceresult = ProjectileHelper.getHitResult(this, this::canHitEntity);
            if (raytraceresult.getType() != RayTraceResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                this.onHit(raytraceresult);
            }

            this.checkInsideBlocks();
            Vector3d vector3d = this.getDeltaMovement();
            double d0 = this.getX() + vector3d.x;
            double d1 = this.getY() + vector3d.y;
            double d2 = this.getZ() + vector3d.z;
            this.setPos(d0, d1, d2);
        } else {
            this.remove();
        }

        if(!this.getCommandSenderWorld().isClientSide()){

            if(this.targetEntity != null){
                Vector3d motion = this.getDeltaMovement();
                double speed = motion.length();

                Vector3d v2 = new Vector3d(this.targetEntity.getX(), this.targetEntity.getY()+0.5, this.targetEntity.getZ()).subtract(new Vector3d(this.getX(), this.getY(), this.getZ())).normalize();
                Vector3d v1 = motion.normalize();

                double angle = Math.acos(v1.dot(v2));
                Vector3d axis = v1.cross(v2).normalize();

                //angle = Math.min(angle, MAX_TURN_ANGLE);

                if (angle < TURNLIMIT) {
                    motion = v2.scale(speed);
                    motion = v2.scale(speed);
                }else {
                    motion = MathUtil.rotateVector(v1, axis, TURNLIMIT).scale(speed);
                }

                this.setDeltaMovement(motion);
            }

            if (this.targetEntity == null && this.getTargetUUID().isPresent()) {
                Entity TargetEntity = ((ServerWorld)this.level).getEntity(this.getTargetUUID().get());
                if (this.targetEntity == null) {
                    this.setTarget(null);
                }
                else if(TargetEntity instanceof LivingEntity) {
                    this.targetEntity = (LivingEntity) TargetEntity;
                }
            }

            if(!this.targetEntity.isAlive()){
                this.getCommandSenderWorld().explode(this, this.getX(), this.getY(), this.getZ(), 0, Explosion.Mode.NONE);
                this.remove();
            }

            RayTraceResult raytraceresult = ProjectileHelper.getHitResult(this, this::canHitEntity);
            if (raytraceresult.getType() != RayTraceResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                this.onHit(raytraceresult);
            }

        }
        Vector3d vector3d = this.getDeltaMovement();
        /*

        double d4 = vector3d.y;
        float f1 = MathHelper.sqrt(horizontalMag(vector3d));
        this.rotationPitch = (float)(MathHelper.atan2(d4, f1) * (double)(180F / (float)Math.PI));
        this.rotationPitch = lerpRotation(this.prevRotationPitch, this.rotationPitch);
        this.rotationYaw = lerpRotation(this.prevRotationYaw, this.rotationYaw);

         */

        ProjectileHelper.rotateTowardsMovement(this, 0.5F);
        if(this.getCommandSenderWorld().isClientSide()){
            this.level.addParticle(ParticleTypes.FLAME, this.getX() - vector3d.x, this.getY() - vector3d.y + 0.15D, this.getZ() - vector3d.z, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected void onHit(RayTraceResult result) {
        super.onHit(result);
        this.Detonate();
    }

    @Override
    protected float getInertia() {
        return 1.05F;
    }

    @Override
    public boolean hurt(DamageSource p_70097_1_, float p_70097_2_) {
        this.remove();
        return true;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult result) {
        if(result.getEntity() != this.getOwner()) {
            this.Detonate();
        }
    }

    private void Detonate(){
        this.level.explode(this, this.getX(), getY(), getZ(), 1F, Explosion.Mode.BREAK);
        this.remove();
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
