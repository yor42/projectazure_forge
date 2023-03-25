package com.yor42.projectazure.gameobject.entity.projectiles;

import com.yor42.projectazure.gameobject.misc.DamageSources;
import com.yor42.projectazure.setup.register.registerEntity;
import com.yor42.projectazure.setup.register.registerSounds;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.network.protocol.Packet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Random;

public class EntityArtsProjectile extends AbstractHurtingProjectile {
    private BlockPos originPos;
    private float damage = 5.0F;
    @Nullable
    private MobEffectInstance Effect;
    public EntityArtsProjectile(EntityType<? extends AbstractHurtingProjectile> entityType, Level worldin) {
        super(entityType, worldin);
    }

    public EntityArtsProjectile(Level worldIn, LivingEntity shooter){
        this(worldIn, shooter, 5.0F, null);
    }

    public EntityArtsProjectile(Level worldIn, LivingEntity shooter, float damage, @Nullable MobEffectInstance effect) {
        super(registerEntity.PROJECTILE_ARTS.get(), shooter, 0, 0, 0, worldIn);
        this.originPos = new BlockPos(shooter.getX(), shooter.getY(0.7F), shooter.getZ());
        this.damage = damage;
        this.Effect = effect;
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
    protected void onHitBlock(BlockHitResult p_230299_1_) {
        BlockPos blockPosIn = this.blockPosition();
        Random random = new Random();
        if(!this.getCommandSenderWorld().getBlockState(blockPosIn).isPathfindable(this.getCommandSenderWorld(), blockPosIn, PathComputationType.AIR)) {
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
        Vec3 vector3d = (new Vec3(x, y, z)).normalize().add(this.random.nextGaussian() * (double)0.0075F * (double)inaccuracy, this.random.nextGaussian() * (double)0.0075F * (double)inaccuracy, this.random.nextGaussian() * (double)0.0075F * (double)inaccuracy).scale(velocity);
        this.setDeltaMovement(vector3d);
        float f = Mth.sqrt(getHorizontalDistanceSqr(vector3d));
        this.yRot = (float)(Mth.atan2(vector3d.x, vector3d.z) * (double)(180F / (float)Math.PI));
        this.xRot = (float)(Mth.atan2(vector3d.y, f) * (double)(180F / (float)Math.PI));
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
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity target = result.getEntity();
        if(target != this.getOwner() && target instanceof LivingEntity) {
            LivingEntity LivingTarget = (LivingEntity)target;
            double DistanceMultiplier = this.originPos != null ? Math.min(25 / distanceToSqr(this.originPos.getX(), this.originPos.getY(), this.originPos.getZ()), 1.0) : 1;
            target.hurt(DamageSources.causeArtsDamage(this, this.getOwner()), (float) (this.damage * DistanceMultiplier));
            if(this.Effect != null){
                LivingTarget.addEffect(this.Effect);
            }

            this.remove();
        }
    }

}
