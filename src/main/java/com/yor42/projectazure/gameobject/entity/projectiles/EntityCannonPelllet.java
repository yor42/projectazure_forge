package com.yor42.projectazure.gameobject.entity.projectiles;

import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenBase;
import com.yor42.projectazure.gameobject.misc.DamageSources;
import com.yor42.projectazure.libs.utils.AmmoProperties;
import com.yor42.projectazure.setup.register.registerManager;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import static net.minecraft.world.entity.Entity.RemovalReason.DISCARDED;

public class EntityCannonPelllet extends AbstractHurtingProjectile {

    AmmoProperties properties;
    BlockPos originPos;

    public EntityCannonPelllet(Level worldIn, AmmoProperties ammotype){
        super(registerManager.PROJECTILECANNONSHELL,worldIn);
        this.properties = ammotype;
    }

    public EntityCannonPelllet(Level worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ, AmmoProperties properties){
        super(registerManager.PROJECTILECANNONSHELL, shooter, accelX, accelY, accelZ, worldIn);
        this.properties = properties;
        this.originPos = new BlockPos(shooter.getX(), shooter.getY(0.5), shooter.getZ());
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

    @Override
    protected void defineSynchedData() {
    }

    public AmmoProperties getProperties() {
        return this.properties;
    }

    @Override
    protected void onHit(HitResult result) {
        if(this.properties != null) {
            super.onHit(result);

            if (!this.level.isClientSide()) {

                if (this.properties.ShouldDamageMultipleComponent()) {
                    boolean flag = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this.getOwner());
                    this.level.explode(this, this.getX(), this.getY(), this.getZ(), (float) 1, this.properties.isFiery(), flag ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE);
                }
            }
        }
        this.remove(RemovalReason.KILLED);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity target = result.getEntity();
        double DistanceMultiplier = Math.min(400/distanceToSqr(this.originPos.getX(), this.originPos.getY(), this.originPos.getZ()), 1.0);
        if(this.properties != null) {
            if (target instanceof EntityKansenBase) {
                ((EntityKansenBase) target).attackEntityFromCannon(DamageSources.causeCannonDamage(this, this.getOwner()), this.properties, DistanceMultiplier);
            } else target.hurt(DamageSources.causeCannonDamage(this, this.getOwner()), (float) (this.properties.getMaxDamage() * 1.2));
        }
        this.remove(RemovalReason.KILLED);
    }

    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vec3 vector3d = (new Vec3(x, y, z)).normalize().add(this.random.nextGaussian() * (double)0.0075F * (double)inaccuracy, this.random.nextGaussian() * (double)0.0075F * (double)inaccuracy, this.random.nextGaussian() * (double)0.0075F * (double)inaccuracy).scale((double)velocity);
        this.setDeltaMovement(vector3d);
        float f = Mth.sqrt((float) distanceToSqr(vector3d));
        this.setYRot((float)(Mth.atan2(vector3d.x, vector3d.z) * (double)(180F / (float)Math.PI)));
        this.setXRot((float)(Mth.atan2(vector3d.y, (double)f) * (double)(180F / (float)Math.PI)));
        this.yRotO = this.getXRot();
        this.xRotO = this.getYRot();
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        if(this.tickCount == 600 || this.isInWater()){
            this.remove(DISCARDED);
        }
        super.tick();
    }
}
