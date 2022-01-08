package com.yor42.projectazure.gameobject.entity.misc;

import com.yor42.projectazure.gameobject.entity.PlaneFlyMovementController;
import com.yor42.projectazure.gameobject.entity.ai.goals.PlaneReturntoOwnerGoal;
import com.yor42.projectazure.gameobject.entity.ai.goals.PlaneWanderAroundCarrierGoal;
import com.yor42.projectazure.gameobject.entity.ai.goals.planeBombRunGoal;
import com.yor42.projectazure.gameobject.entity.ai.targetAI.PlaneInterceptGoal;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemEquipmentPlaneBase;
import com.yor42.projectazure.libs.enums;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public abstract class AbstractEntityPlanes extends CreatureEntity implements IAnimatable {

    private AbstractEntityCompanion owner;
    private boolean hasPayloads;
    private boolean isOnBombRun;
    private int maxOperativetime;
    private boolean isReturningToOwner;
    private boolean isLifeLimited;
    protected float attackdamage;
    private final boolean hasRadar;

    protected AbstractEntityPlanes(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
        this.moveController = new PlaneFlyMovementController(this, 20, true);
        this.navigator = new FlyingPathNavigator(this, worldIn);
        this.isLifeLimited = false;
        this.hasRadar = false;
        this.isReturningToOwner = false;
        this.setPathPriority(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathPriority(PathNodeType.WATER, -1.0F);
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
        return this.hasPayload() && this.getAttackTarget() != null && this.getMaxOperativeTick() - this.ticksExisted >200;
    }

    @Override
    public void livingTick() {
        super.livingTick();
        this.setNoGravity(this.isAlive());
        //in water = crash
        if(this.isInWater()){
            this.world.createExplosion(this, this.getPosX(), getPosY(), getPosZ(), 0.5F, Explosion.Mode.DESTROY);
            this.remove();
        }
        if(this.isLimitedLifespan()) {
            if (this.ticksExisted >= this.getMaxOperativeTick()) {
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
        this.setDead();
    }

    @Override
    protected void onDeathUpdate() {
        Vector3d movement = this.getMotion();
        double d0 = this.getPosX() + movement.x;
        double d1 = this.getPosY() + movement.y;
        double d2 = this.getPosZ() + movement.z;
        this.world.addParticle(ParticleTypes.SMOKE, d0, d1 + 0.5D, d2, 0.0D, 0.0D, 0.0D);

        if(this.isOnGround() || isInWater()) {
            this.world.createExplosion(this, this.getPosX(), this.getPosYHeight(0.0625D), this.getPosZ(), 1.0F, Explosion.Mode.DESTROY);
            this.remove();
        }

    }

    public void onDeath(DamageSource cause) {
        if (net.minecraftforge.common.ForgeHooks.onLivingDeath(this, cause)) return;
        if (!this.removed && !this.dead) {
            Entity entity = cause.getTrueSource();
            LivingEntity livingentity = this.getAttackingEntity();
            if (this.scoreValue >= 0 && livingentity != null) {
                livingentity.awardKillScore(this, this.scoreValue, cause);
            }

            if (this.isSleeping()) {
                this.wakeUp();
            }

            if(this.getAttackTarget()!= null){
                if(this.getDistanceSq(this.getAttackTarget())<20F && this.getAttackTarget().getPosY()<this.getPosY()){
                    this.getMoveHelper().setMoveTo(this.getAttackTarget().getPosX(), this.getAttackTarget().getPosY(), this.getAttackTarget().getPosZ(), 1.0F);
                }
            }

            this.dead = true;
            this.getCombatTracker().reset();
            if (this.world instanceof ServerWorld) {
                if (entity != null) {
                    entity.func_241847_a((ServerWorld) this.world, this);
                }
            }

            this.world.setEntityState(this, (byte)3);
        }
    }

    @Override
    public boolean hasNoGravity() {
        return this.isAlive();
    }

    @Override
    public void travel(Vector3d travelVector) {
        super.travel(travelVector);
        if (this.isOnGround())
        {
            this.setMotion(this.getMotion().getX(), this.getMotion().getY()+0.02, this.getMotion().getZ());
        }
    }

    @Override
    public boolean isOnLadder() {
        return false;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        NetworkHooks.getEntitySpawningPacket(this);
        return super.createSpawnPacket();
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

