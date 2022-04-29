package com.yor42.projectazure.gameobject.entity.projectiles;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.misc.DamageSources;
import net.minecraft.network.protocol.Packet;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import static net.minecraft.world.entity.Entity.RemovalReason.KILLED;

public class EntityProjectileTorpedo extends AbstractHurtingProjectile implements IAnimatable {

    private float existingtime;

    private final AnimationFactory factory = new AnimationFactory(this);

    public EntityProjectileTorpedo(double p_i50174_2_, double p_i50174_4_, double p_i50174_6_, double p_i50174_8_, double p_i50174_10_, double p_i50174_12_, Level p_i50174_14_) {
        super(Main.PROJECTILETORPEDO, p_i50174_2_, p_i50174_4_, p_i50174_6_, p_i50174_8_, p_i50174_10_, p_i50174_12_, p_i50174_14_);
    }

    public EntityProjectileTorpedo(LivingEntity ShooterIn, double AccelX, double AccelY, double AccelZ, Level WorldIn) {
        super(Main.PROJECTILETORPEDO, ShooterIn, AccelX, AccelY, AccelZ, WorldIn);
    }

    @OnlyIn(Dist.CLIENT)
    public EntityProjectileTorpedo(Level worldIn, double x, double y, double z, double motionXIn, double motionYIn, double motionZIn) {
        this(Main.PROJECTILETORPEDO, worldIn);
        this.moveTo(x, y, z, this.getXRot(), this.getYRot());
        this.setDeltaMovement(motionXIn, motionYIn, motionZIn);
    }

    public EntityProjectileTorpedo(EntityType<EntityProjectileTorpedo> TypeIn, Level world) {
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
    public Packet<?> getAddEntityPacket() {
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

        ProjectileUtil.rotateTowardsMovement(this, 0.5F);
        Vec3 vector3d4 = this.getDeltaMovement();
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
        Vec3 vector3d = this.getDeltaMovement();

        if (vector3d.lengthSqr() != 0.0D) {
            this.setYRot((float)(Mth.atan2(vector3d.z, vector3d.x) * (double)(180F / (float)Math.PI)) + 90.0F);

            while(this.getXRot() - this.xRotO >= 180.0F) {
                this.xRotO += 360.0F;
            }

            while(this.getYRot() - this.yRotO < -180.0F) {
                this.yRotO -= 360.0F;
            }

            while(this.getYRot() - this.yRotO >= 180.0F) {
                this.yRotO += 360.0F;
            }
        }
    }

    @Override
    protected void onHit(HitResult result) {
        explode();
        this.remove(KILLED);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
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
        this.level.explode(this, this.getX(), this.getY(0.0625D), this.getZ(), 3.5F, PAConfig.CONFIG.EnableTorpedoBlockDamage.get()? Explosion.BlockInteraction.DESTROY:Explosion.BlockInteraction.NONE);
    }

    private void selfDestruct(){
        this.level.explode(this, this.getX(), this.getY(0.0625D), this.getZ(), 2.0F, Explosion.BlockInteraction.BREAK);
        this.remove(KILLED);
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
