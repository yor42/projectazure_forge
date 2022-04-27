package com.yor42.projectazure.gameobject.entity.misc;

import com.yor42.projectazure.gameobject.entity.PlaneFlyMovementController;
import com.yor42.projectazure.gameobject.entity.ai.goals.PlaneReturntoOwnerGoal;
import com.yor42.projectazure.gameobject.entity.ai.goals.PlaneWanderAroundCarrierGoal;
import com.yor42.projectazure.gameobject.entity.ai.goals.planeBombRunGoal;
import com.yor42.projectazure.gameobject.entity.ai.targetAI.PlaneInterceptGoal;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemEquipmentPlaneBase;
import com.yor42.projectazure.libs.enums;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public abstract class AbstractEntityPlanes extends PathfinderMob implements IAnimatable {

    private AbstractEntityCompanion owner;
    private boolean hasPayloads;
    private boolean isOnBombRun;
    private int maxOperativetime;
    private boolean isReturningToOwner;
    private boolean isLifeLimited;
    protected float attackdamage;
    private final boolean hasRadar;

    protected AbstractEntityPlanes(EntityType<? extends PathfinderMob> type, Level worldIn) {
        super(type, worldIn);
        this.moveControl = new PlaneFlyMovementController(this, 20, true);
        this.navigation = new FlyingPathNavigation(this, worldIn);
        this.isLifeLimited = false;
        this.hasRadar = false;
        this.isReturningToOwner = false;
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.attackdamage = this.getPlaneItem().getAttackDamage();
    }

    public float getAttackDamage() {
        return this.attackdamage;
    }

    public void setAttackDamage(float attackDamage) {
        this.attackdamage = attackDamage;
    }

    protected final AnimationFactory factory = new AnimationFactory(this);

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller", 5, this::predicate));
    }

    public boolean hasRadar(){
        return this.hasRadar;
    }

    public void setReturningtoOwner(boolean value){
        this.isReturningToOwner = value;
    }

    public boolean isReturningToOwner(){
        return this.isReturningToOwner;
    }

    protected abstract <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event);

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public void setOnBombRun(boolean onBombRun) {
        this.isOnBombRun = onBombRun;
    }

    public boolean isOnBombRun() {
        return this.isOnBombRun;
    }

    public boolean canAttack(){
        return this.hasPayload() && this.getTarget() != null && this.getMaxOperativeTick() - this.tickCount >200;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.setNoGravity(this.isAlive());
        //in water = crash
        if(this.isInWater()){
            this.level.explode(this, this.getX(), getY(), getZ(), 0.5F, Explosion.BlockInteraction.DESTROY);
            this.remove(RemovalReason.KILLED);
        }
        if(this.isLimitedLifespan()) {
            if (this.tickCount >= this.getMaxOperativeTick()) {
                this.crashThePlane();
            }
        }
        if(this.getOwner() != null && !this.getOwner().isAlive()){
            this.crashThePlane();
        }
    }

    public enums.PLANE_TYPE getPlaneType(){
        return this.getPlaneItem().getType();
    };

    public void setOwner(AbstractEntityCompanion owner) {
        this.owner = owner;
    }

    public AbstractEntityCompanion getOwner() {
        return this.owner;
    }

    public void setPayloads(boolean payload) {
        this.hasPayloads = payload;
    }

    public void setMaxOperativetime(int maxOperativetime) {
        this.setLimitedLifespan(true);
        this.maxOperativetime = maxOperativetime;
    }

    public int getMaxOperativeTick(){
        return this.maxOperativetime;
    };

    public void setLimitedLifespan(boolean value){
        this.isLifeLimited = value;
    }

    public boolean isLimitedLifespan() {
        return this.isLifeLimited;
    }

    public void usePayload(LivingEntity entity){
        this.doAttack(entity);
        this.hasPayloads = false;
    }

    public boolean hasPayload(){
        return this.hasPayloads;
    }

    protected abstract void doAttack(LivingEntity entity);

    public abstract ItemEquipmentPlaneBase getPlaneItem();

    public void crashThePlane(){
        this.removeAfterChangingDimensions();
    }

    @Override
    protected void tickDeath() {
        Vec3 movement = this.getDeltaMovement();
        double d0 = this.getX() + movement.x;
        double d1 = this.getY() + movement.y;
        double d2 = this.getZ() + movement.z;
        this.level.addParticle(ParticleTypes.SMOKE, d0, d1 + 0.5D, d2, 0.0D, 0.0D, 0.0D);

        if(this.isOnGround() || isInWater()) {
            this.level.explode(this, this.getX(), this.getY(0.0625D), this.getZ(), 1.0F, Explosion.BlockInteraction.DESTROY);
            this.remove(RemovalReason.KILLED);
        }

    }

    public void die(DamageSource cause) {
        if (net.minecraftforge.common.ForgeHooks.onLivingDeath(this, cause)) return;
        if (!this.dead) {
            Entity entity = cause.getEntity();
            LivingEntity livingentity = this.getKillCredit();
            if (this.deathScore >= 0 && livingentity != null) {
                livingentity.awardKillScore(this, this.deathScore, cause);
            }

            if (this.isSleeping()) {
                this.stopSleeping();
            }

            if(this.getTarget()!= null){
                if(this.distanceToSqr(this.getTarget())<20F && this.getTarget().getY()<this.getY()){
                    this.getMoveControl().setWantedPosition(this.getTarget().getX(), this.getTarget().getY(), this.getTarget().getZ(), 1.0F);
                }
            }

            this.dead = true;
            this.getCombatTracker().recheckStatus();
            if (this.level instanceof ServerLevel) {
                if (entity != null) {
                    entity.killed((ServerLevel) this.level, this);
                }
            }

            this.level.broadcastEntityEvent(this, (byte)3);
        }
    }

    @Override
    public boolean isNoGravity() {
        return this.isAlive();
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        super.travel(travelVector);
        if (this.isOnGround())
        {
            this.setDeltaMovement(this.getDeltaMovement().x(), this.getDeltaMovement().y()+0.02, this.getDeltaMovement().z());
        }
    }

    @Override
    public boolean onClimbable() {
        return false;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected int calculateFallDamage(float distance, float damageMultiplier) {
        return 0;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new planeBombRunGoal(this));
        this.goalSelector.addGoal(1, new PlaneReturntoOwnerGoal(this));
        this.goalSelector.addGoal(2, new PlaneWanderAroundCarrierGoal(this));
        this.targetSelector.addGoal(1, new PlaneInterceptGoal(this));
    }
}

