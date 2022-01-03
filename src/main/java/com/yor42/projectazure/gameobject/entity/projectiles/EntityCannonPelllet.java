package com.yor42.projectazure.gameobject.entity.projectiles;

import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityKansenBase;
import com.yor42.projectazure.gameobject.misc.DamageSources;
import com.yor42.projectazure.libs.utils.AmmoProperties;
import com.yor42.projectazure.setup.register.registerManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityCannonPelllet extends DamagingProjectileEntity {

    AmmoProperties properties;
    BlockPos originPos;

    public EntityCannonPelllet(World worldIn, AmmoProperties ammotype){
        super(registerManager.PROJECTILECANNONSHELL,worldIn);
        this.properties = ammotype;
    }

    public EntityCannonPelllet(World worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ, AmmoProperties properties){
        super(registerManager.PROJECTILECANNONSHELL, shooter, accelX, accelY, accelZ, worldIn);
        this.properties = properties;
        this.originPos = new BlockPos(shooter.getPosX(), shooter.getPosYHeight(0.5), shooter.getPosZ());
    }

    public EntityCannonPelllet(EntityType<? extends DamagingProjectileEntity> entityType, World worldIn) {
        super(entityType,worldIn);
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
    protected void registerData() {
    }

    public AmmoProperties getProperties() {
        return this.properties;
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if(this.properties != null) {
            super.onImpact(result);

            if (!this.world.isRemote()) {

                if (this.properties.ShouldDamageMultipleComponent()) {
                    boolean flag = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this.func_234616_v_());
                    this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), (float) 1, this.properties.isFiery(), flag ? Explosion.Mode.DESTROY : Explosion.Mode.NONE);
                }
            }
        }
        this.remove();
    }

    @Override
    protected void onEntityHit(EntityRayTraceResult result) {
        Entity target = result.getEntity();
        double DistanceMultiplier = Math.min(400/getDistanceSq(this.originPos.getX(), this.originPos.getY(), this.originPos.getZ()), 1.0);
        if(this.properties != null) {
            if (target instanceof EntityKansenBase) {
                ((EntityKansenBase) target).attackEntityFromCannon(DamageSources.causeCannonDamage(this, this.func_234616_v_()), this.properties, DistanceMultiplier);
            } else target.attackEntityFrom(DamageSources.causeCannonDamage(this, this.func_234616_v_()), (float) (this.properties.getMaxDamage() * 1.2));
        }
        this.remove();
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
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        if(this.ticksExisted == 600 || this.isInWater()){
            this.remove();
        }
        super.tick();
    }
}
