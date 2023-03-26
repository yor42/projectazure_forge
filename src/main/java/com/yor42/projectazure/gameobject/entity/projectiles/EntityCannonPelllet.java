package com.yor42.projectazure.gameobject.entity.projectiles;

import com.yor42.projectazure.gameobject.SMath;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenBase;
import com.yor42.projectazure.gameobject.misc.DamageSources;
import com.yor42.projectazure.libs.utils.AmmoProperties;
import com.yor42.projectazure.setup.register.registerEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class EntityCannonPelllet extends AbstractHurtingProjectile {

    private AmmoProperties properties;
    private LivingEntity shooter;
    private BlockPos originPos;

    public EntityCannonPelllet(Level worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ, AmmoProperties properties){
        super(registerEntity.PROJECTILE_CANNONSHELL.get(), shooter, accelX, accelY, accelZ, worldIn);
        this.properties = properties;
        this.shooter = shooter;
        this.originPos = shooter.blockPosition();
    }

    public EntityCannonPelllet(EntityType<? extends AbstractHurtingProjectile> entityType, Level worldIn) {
        super(entityType,worldIn);
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

    public AmmoProperties getProperties() {
        return this.properties;
    }

    @Override
    protected void onHitBlock(BlockHitResult p_230299_1_) {
        super.onHitBlock(p_230299_1_);

        if(this.properties == null) {
            return;
        }
        BlockPos blockPosIn = this.blockPosition();
        if(this.getCommandSenderWorld().getBlockState(blockPosIn).isPathfindable(this.getCommandSenderWorld(), blockPosIn, PathComputationType.AIR)) {
            return;
        }

        boolean flag = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this.getOwner());
        this.level.explode(this, this.getX(), this.getY(), this.getZ(), this.properties.isExplosive()? 1F:0.2F, this.properties.isFiery(), flag ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE);
        this.discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity target = result.getEntity();
        if(target == this.shooter){
            return;
        }
        if(!this.level.isClientSide()) {
            double DistanceMultiplier = Math.min(400 / distanceToSqr(this.originPos.getX(), this.originPos.getY(), this.originPos.getZ()), 1.0);
            if (this.properties != null) {
                if (target instanceof EntityKansenBase) {
                    ((EntityKansenBase) target).attackEntityFromCannon(DamageSources.causeCannonDamage(this, this.getOwner()), this.properties, DistanceMultiplier);
                } else
                    target.hurt(DamageSources.causeCannonDamage(this, this.getOwner()), (float) (this.properties.getMaxDamage() * 1.2));
            }
        }
        this.discard();
    }

    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vec3 vector3d = (new Vec3(x, y, z)).normalize().add(this.random.nextGaussian() * (double)0.0075F * (double)inaccuracy, this.random.nextGaussian() * (double)0.0075F * (double)inaccuracy, this.random.nextGaussian() * (double)0.0075F * (double)inaccuracy).scale(velocity);
        this.setDeltaMovement(vector3d);
        float f = (float)Math.sqrt(SMath.getHorizontalDistanceSqr(vector3d));
        this.setYRot((float)(Mth.atan2(vector3d.x, vector3d.z) * (double)(180F / (float)Math.PI)));
        this.setXRot((float)(Mth.atan2(vector3d.y, f) * (double)(180F / (float)Math.PI)));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    @Override
    public boolean hurt(DamageSource p_70097_1_, float p_70097_2_) {
        if(p_70097_2_>=8) {
            if (this.properties != null && this.properties.getCategory().ShouldDamageMultipleComponent()) {
                this.getCommandSenderWorld().explode(this.getOwner(), this.getX(), this.getY(), this.getZ(), 2.0F, Explosion.BlockInteraction.DESTROY);
            } else {
                this.discard();
            }
        }

        return false;
    }

    @Nonnull
    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        if(this.tickCount == 600 || this.isInWater()){
            this.discard();
        }
        super.tick();
    }
}
