package com.yor42.projectazure.gameobject.entity.projectiles;

import com.yor42.projectazure.gameobject.entity.EntityKansenBase;
import com.yor42.projectazure.libs.enums;
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

    enums.AmmoTypes ammotype;

    public EntityCannonShell(EntityType<? extends DamagingProjectileEntity> entityType, World worldIn, enums.AmmoTypes ammotype){
        super(entityType,worldIn);
        this.ammotype = ammotype;
    }

    public EntityCannonShell(World worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ, enums.AmmoTypes ammotype){
        super(registerManager.PROJECTILECANNONSHELL, shooter, accelX, accelY, accelZ, worldIn);
        this.ammotype = ammotype;
    }

    @OnlyIn(Dist.CLIENT)
    public EntityCannonShell(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ, enums.AmmoTypes ammotype) {
        super(registerManager.PROJECTILECANNONSHELL, x, y, z, accelX, accelY, accelZ, worldIn);
        this.ammotype = ammotype;
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

    private boolean isExplosive(){
        return this.ammotype == enums.AmmoTypes.APHE||this.ammotype == enums.AmmoTypes.HE || this.ammotype == enums.AmmoTypes.APHEI;
    }

    private boolean isFiery(){
        return this.ammotype == enums.AmmoTypes.INCENDIARY||this.ammotype == enums.AmmoTypes.APHEI||this.ammotype == enums.AmmoTypes.API;
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        super.onImpact(result);
        if(!this.world.isRemote()){
            if(this.isExplosive()){
                boolean flag = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this.func_234616_v_());
                this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), (float)1, this.isFiery(), flag ? Explosion.Mode.DESTROY : Explosion.Mode.NONE);
            }
            else{
                BlockPos hitblockPos = new BlockPos(result.getHitVec().x,result.getHitVec().y,result.getHitVec().z);
                this.world.setBlockState(hitblockPos, AbstractFireBlock.getFireForPlacement(this.world, hitblockPos));
            }
        }
    }

    @Override
    protected void onEntityHit(EntityRayTraceResult result) {
        super.onEntityHit(result);
        if(!this.world.isRemote){
            Entity target = result.getEntity();
            boolean flag = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this.func_234616_v_());
            if(target instanceof EntityKansenBase){
                //taking consideration of ricochets/penetration angle
                float damage = this.ammotype.isHit()? this.ammotype.getDamage():1;
                target.attackEntityFrom(DAMAGE_GUN, damage);
                if(this.isExplosive()){
                    this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), (float)1, this.isFiery(), flag ? Explosion.Mode.DESTROY : Explosion.Mode.NONE);
                }
                else if (this.isFiery()){
                    target.setFire(8);
                }
            }
            else{
                //for Other mob? hah! theres no savior for you!
                target.attackEntityFrom(DAMAGE_GUN, this.ammotype.getDamage());
            }
        }
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
