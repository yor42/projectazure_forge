package com.yor42.projectazure.gameobject.entity.projectiles;

import com.yor42.projectazure.gameobject.misc.DamageSources;
import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityKansenBase;
import com.yor42.projectazure.libs.utils.AmmoProperties;
import com.yor42.projectazure.setup.register.registerManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityCannonPelllet extends DamagingProjectileEntity {

    AmmoProperties properties;
    BlockPos originPos;

    public EntityCannonPelllet(EntityType<? extends DamagingProjectileEntity> entityType, World worldIn, AmmoProperties ammotype){
        super(entityType,worldIn);
        this.properties = ammotype;
    }

    public EntityCannonPelllet(World worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ, AmmoProperties properties){
        super(registerManager.PROJECTILECANNONSHELL, shooter, accelX, accelY, accelZ, worldIn);
        this.properties = properties;
        this.originPos = new BlockPos(shooter.getPosX(), shooter.getPosY(), shooter.getPosZ());
    }

    @OnlyIn(Dist.CLIENT)
    public EntityCannonPelllet(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ, AmmoProperties properties) {
        super(registerManager.PROJECTILECANNONSHELL, x, y, z, accelX, accelY, accelZ, worldIn);
        this.properties = properties;
        this.originPos = new BlockPos(x,y,z);
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
        double DistanceMultiplier = Math.min(20/getDistanceSq(this.originPos.getX(), this.originPos.getY(), this.originPos.getZ()), 1.0);
        if(this.properties != null) {
            if (target instanceof EntityKansenBase) {
                ((EntityKansenBase) target).attackEntityFromCannon(DamageSources.causeCannonDamage(this, this.func_234616_v_()), this.properties, DistanceMultiplier);
            } else target.attackEntityFrom(DamageSources.causeCannonDamage(this, this.func_234616_v_()), (float) (this.properties.getMaxDamage() * 1.2));
        }
        this.remove();
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
        Vector3d vector3d = this.getMotion();
        //disables constant acceleration;
        //this.setMotion(vector3d.add(-this.accelerationX, -this.accelerationY, -this.accelerationZ).scale(this.getMotionFactor()));

        super.tick();
    }
}
