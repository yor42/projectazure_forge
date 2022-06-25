package com.yor42.projectazure.gameobject.entity.projectiles;

import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.misc.DamageSources;
import com.yor42.projectazure.setup.register.registerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
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
        super(registerEntity.PROJECTILE_TORPEDO.get(), p_i50174_2_, p_i50174_4_, p_i50174_6_, p_i50174_8_, p_i50174_10_, p_i50174_12_, p_i50174_14_);
    }

    public EntityProjectileTorpedo(LivingEntity ShooterIn, double AccelX, double AccelY, double AccelZ, World WorldIn) {
        super(registerEntity.PROJECTILE_TORPEDO.get(), ShooterIn, AccelX, AccelY, AccelZ, WorldIn);
    }

    @OnlyIn(Dist.CLIENT)
    public EntityProjectileTorpedo(World worldIn, double x, double y, double z, double motionXIn, double motionYIn, double motionZIn) {
        this(registerEntity.PROJECTILE_TORPEDO.get(), worldIn);
        this.moveTo(x, y, z, this.yRot, this.xRot);
        this.setDeltaMovement(motionXIn, motionYIn, motionZIn);
    }

    public EntityProjectileTorpedo(EntityType<EntityProjectileTorpedo> TypeIn, World world) {
        super(TypeIn, world);
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
    public IPacket<?> getAddEntityPacket() {
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
        this.setDeltaMovement(this.getDeltaMovement().add(this.xPower, this.yPower, this.zPower).scale((double)multiplier));

        changeheading();

        boolean flag = !this.isInWater();

        ProjectileHelper.rotateTowardsMovement(this, 0.5F);
        Vector3d vector3d4 = this.getDeltaMovement();
        if (flag) {
            this.setDeltaMovement(vector3d4.x, vector3d4.y - (double)0.1F, vector3d4.z);
        }
        else{
            if(this.getDeltaMovement().y()<0)
                this.setDeltaMovement(vector3d4.x, vector3d4.y + (double)0.05F, vector3d4.z);
            else {
                this.setDeltaMovement(vector3d4.x, 0, vector3d4.z);
            }
        }

    }

    private void changeheading(){
        Vector3d vector3d = this.getDeltaMovement();

        if (vector3d.lengthSqr() != 0.0D) {
            float f = MathHelper.sqrt(Entity.getHorizontalDistanceSqr(vector3d));
            this.yRot = (float)(MathHelper.atan2(vector3d.z, vector3d.x) * (double)(180F / (float)Math.PI)) + 90.0F;

            while(this.xRot - this.xRotO >= 180.0F) {
                this.xRotO += 360.0F;
            }

            while(this.yRot - this.yRotO < -180.0F) {
                this.yRotO -= 360.0F;
            }

            while(this.yRot - this.yRotO >= 180.0F) {
                this.yRotO += 360.0F;
            }
        }
    }

    @Override
    protected void onHit(RayTraceResult result) {
        explode();
        this.remove();
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult result) {
        Entity entity = result.getEntity();
        entity.hurt(DamageSources.causeTorpedoDamage(this, this.getOwner()), 10F);
        explode();
        super.onHitEntity(result);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        selfDestruct();
        return true;
    }

    private void explode(){
        this.level.explode(this, this.getX(), this.getY(0.0625D), this.getZ(), 3.5F, PAConfig.CONFIG.EnableTorpedoBlockDamage.get()? Explosion.Mode.DESTROY:Explosion.Mode.NONE);
    }

    private void selfDestruct(){
        this.level.explode(this, this.getX(), this.getY(0.0625D), this.getZ(), 2.0F, Explosion.Mode.BREAK);
        this.remove();
    }

    @Override
    public boolean isNoGravity() {
        return this.isInWater();
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }
}
