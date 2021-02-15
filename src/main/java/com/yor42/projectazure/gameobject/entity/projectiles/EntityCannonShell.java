package com.yor42.projectazure.gameobject.entity.projectiles;

import com.yor42.projectazure.gameobject.DamageSources;
import com.yor42.projectazure.gameobject.entity.EntityKansenBase;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.utils.AmmoProperties;
import com.yor42.projectazure.setup.register.registerEntity;
import com.yor42.projectazure.setup.register.registerManager;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.yor42.projectazure.gameobject.DamageSources.DAMAGE_GUN;

public class EntityCannonShell extends DamagingProjectileEntity {

    AmmoProperties properties;
    BlockPos originPos;

    public EntityCannonShell(EntityType<? extends DamagingProjectileEntity> entityType, World worldIn, AmmoProperties ammotype){
        super(entityType,worldIn);
        this.properties = ammotype;
    }

    public EntityCannonShell(World worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ, AmmoProperties properties){
        super(registerManager.PROJECTILECANNONSHELL, shooter, accelX, accelY, accelZ, worldIn);
        this.properties = properties;
        this.originPos = new BlockPos(shooter.getPosX(), shooter.getPosY(), shooter.getPosZ());
    }

    @OnlyIn(Dist.CLIENT)
    public EntityCannonShell(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ, AmmoProperties properties) {
        super(registerManager.PROJECTILECANNONSHELL, x, y, z, accelX, accelY, accelZ, worldIn);
        this.properties = properties;
        this.originPos = new BlockPos(x,y,z);
    }

    public EntityCannonShell(EntityType<? extends DamagingProjectileEntity> entityType, World worldIn) {
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

    @Override
    protected void onImpact(RayTraceResult result) {
        super.onImpact(result);
        if(!this.world.isRemote()){
            if(this.properties.ShouldDamageMultipleComponent()){
                boolean flag = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this.func_234616_v_());
                this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), (float)1, this.properties.isFiery(), flag ? Explosion.Mode.DESTROY : Explosion.Mode.NONE);
            }
            else{
                BlockPos hitblockPos = new BlockPos(result.getHitVec().x,result.getHitVec().y,result.getHitVec().z);
                this.world.setBlockState(hitblockPos, AbstractFireBlock.getFireForPlacement(this.world, hitblockPos));
            }
        }
    }

    @Override
    protected void onEntityHit(EntityRayTraceResult result) {
        Entity target = result.getEntity();
        double DistanceMultiplier = Math.min(20/getDistanceSq(this.originPos.getX(), this.originPos.getY(), this.originPos.getZ()), 1.0);
        if(target instanceof EntityKansenBase){
            ((EntityKansenBase) target).attackEntityFromCannon(DAMAGE_GUN, this.properties, DistanceMultiplier);
        }
        else target.attackEntityFrom(DAMAGE_GUN, (float) (this.properties.getMaxDamage()*1.2));
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        Entity entity = this.func_234616_v_();
        int i = entity == null ? 0 : entity.getEntityId();
        return new SSpawnObjectPacket(this.getEntityId(), this.getUniqueID(), this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationPitch, this.rotationYaw, this.getType(), i, new Vector3d(this.accelerationX, this.accelerationY, this.accelerationZ));
    }

    @Override
    public void tick() {
        if(this.ticksExisted == 600){
            this.remove();
        }
        super.tick();
    }
}
