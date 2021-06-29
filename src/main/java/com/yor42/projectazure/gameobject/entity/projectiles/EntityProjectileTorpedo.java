package com.yor42.projectazure.gameobject.entity.projectiles;

import com.yor42.projectazure.gameobject.misc.DamageSources;
import com.yor42.projectazure.setup.register.registerManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class EntityProjectileTorpedo extends DamagingProjectileEntity implements IAnimatable {

    private float existingtime;

    private final AnimationFactory factory = new AnimationFactory(this);

    public EntityProjectileTorpedo(double p_i50174_2_, double p_i50174_4_, double p_i50174_6_, double p_i50174_8_, double p_i50174_10_, double p_i50174_12_, World p_i50174_14_) {
        super(registerManager.PROJECTILETORPEDO, p_i50174_2_, p_i50174_4_, p_i50174_6_, p_i50174_8_, p_i50174_10_, p_i50174_12_, p_i50174_14_);
    }

    public EntityProjectileTorpedo(LivingEntity ShooterIn, double AccelX, double AccelY, double AccelZ, World WorldIn) {
        super(registerManager.PROJECTILETORPEDO, ShooterIn, AccelX, AccelY, AccelZ, WorldIn);
    }

    @OnlyIn(Dist.CLIENT)
    public EntityProjectileTorpedo(World worldIn, double x, double y, double z, double motionXIn, double motionYIn, double motionZIn) {
        this(registerManager.PROJECTILETORPEDO, worldIn);
        this.setLocationAndAngles(x, y, z, this.rotationYaw, this.rotationPitch);
        this.setMotion(motionXIn, motionYIn, motionZIn);
    }

    public EntityProjectileTorpedo(EntityType<EntityProjectileTorpedo> TypeIn, World world) {
        super(TypeIn, world);
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
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<EntityProjectileTorpedo>(this, "controller", 0, this::predicate));
    }

    protected <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event)
    {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.torpedo.screwrotation", true));
        return PlayState.CONTINUE;
    }

    @Override
    public void tick() {
        super.tick();
        if(this.existingtime >=600){
            selfDestruct();
        }
        else
            this.existingtime++;
        //1.25 is to compensate valilla slowdown in water.
        float multiplier = this.isInWater()? 1.25F:0.8F;
        this.setMotion(this.getMotion().add(this.accelerationX, this.accelerationY, this.accelerationZ).scale((double)multiplier));

        changeheading();

        boolean flag = !this.isInWater();

        Vector3d vector3d4 = this.getMotion();
        if (flag) {
            this.setMotion(vector3d4.x, vector3d4.y - (double)0.1F, vector3d4.z);
        }
        else{
            if(this.getMotion().getY()<0)
                this.setMotion(vector3d4.x, vector3d4.y + (double)0.05F, vector3d4.z);
            else {
                this.setMotion(vector3d4.x, 0, vector3d4.z);
            }
        }

    }

    private void changeheading(){
        Vector3d vector3d = this.getMotion();

        if (vector3d.lengthSquared() != 0.0D) {
            float f = MathHelper.sqrt(Entity.horizontalMag(vector3d));
            this.rotationYaw = (float)(MathHelper.atan2(vector3d.z, vector3d.x) * (double)(180F / (float)Math.PI)) + 90.0F;

            for(this.rotationPitch = (float)(MathHelper.atan2((double)f, vector3d.y) * (double)(180F / (float)Math.PI)) - 90.0F; this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
            }

            while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
                this.prevRotationPitch += 360.0F;
            }

            while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
                this.prevRotationYaw -= 360.0F;
            }

            while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
                this.prevRotationYaw += 360.0F;
            }
        }
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        explode();
        this.remove();
    }

    @Override
    protected void onEntityHit(EntityRayTraceResult result) {
        Entity entity = result.getEntity();
        entity.attackEntityFrom(DamageSources.causeTorpedoDamage(this, this.func_234616_v_()), 10F);
        explode();
        super.onEntityHit(result);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        selfDestruct();
        return true;
    }

    private void explode(){
        this.world.createExplosion(this, this.getPosX(), this.getPosYHeight(0.0625D), this.getPosZ(), 3.5F, Explosion.Mode.BREAK);
    }

    private void selfDestruct(){
        this.world.createExplosion(this, this.getPosX(), this.getPosYHeight(0.0625D), this.getPosZ(), 2.0F, Explosion.Mode.BREAK);
        this.remove();
    }

    @Override
    public boolean hasNoGravity() {
        return this.isInWater();
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
