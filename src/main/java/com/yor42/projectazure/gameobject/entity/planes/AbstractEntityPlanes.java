package com.yor42.projectazure.gameobject.entity.planes;

import com.yor42.projectazure.gameobject.entity.PlaneFlyMovementController;
import com.yor42.projectazure.gameobject.entity.ai.goals.PlaneReturntoOwnerGoal;
import com.yor42.projectazure.gameobject.entity.ai.goals.PlaneWanderAroundCarrierGoal;
import com.yor42.projectazure.gameobject.entity.ai.goals.PlaneAttackGoal;
import com.yor42.projectazure.gameobject.entity.ai.targetAI.PlaneInterceptGoal;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenAircraftCarrier;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemEquipmentPlaneBase;
import com.yor42.projectazure.libs.enums;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.controller.BodyController;
import net.minecraft.entity.ai.controller.LookController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.IItemHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import java.util.EnumSet;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.serializePlane;

public abstract class AbstractEntityPlanes extends FlyingEntity implements IAnimatable {

    private AbstractEntityCompanion owner;
    private boolean hasPayloads;
    public Vector3d moveTargetPoint = Vector3d.ZERO;
    public BlockPos anchorPoint = BlockPos.ZERO;
    public PlaneStatus planestatus = PlaneStatus.ATTACKING;
    private int maxOperativetime;
    private boolean isLifeLimited;
    protected float attackdamage;
    private final boolean hasRadar;

    protected AbstractEntityPlanes(EntityType<? extends FlyingEntity> type, World worldIn) {
        super(type, worldIn);
        this.lookControl = new LookHelperController(this);
        this.moveControl = new PlaneFlyMovementController(this, 20, true);
        this.navigation = new FlyingPathNavigator(this, worldIn);
        this.isLifeLimited = false;
        this.hasRadar = false;
        this.setPathfindingMalus(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(PathNodeType.WATER, -1.0F);
        this.attackdamage = this.getPlaneItem().getAttackDamage();
    }

    @Nonnull
    protected BodyController createBodyControl() {
        return new BodyHelperController(this);
    }

    @OnlyIn(Dist.CLIENT)
    public boolean shouldRenderAtSqrDistance(double p_70112_1_) {
        return true;
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

    public void setReturningtoOwner(){
        this.planestatus = PlaneStatus.RETURNING;
    }

    public boolean isReturningToOwner(){
        return this.planestatus == PlaneStatus.RETURNING;
    }

    protected abstract <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event);

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public void setOnBombRun() {
        this.planestatus = PlaneStatus.ATTACKING;
    }

    public boolean isOnBombRun() {
        return this.planestatus == PlaneStatus.ATTACKING;
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
            this.level.explode(this, this.getX(), getY(), getZ(), 0.5F, Explosion.Mode.DESTROY);
            this.remove();
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

    public void startCircling(BlockPos anchorpoint){
        this.planestatus = PlaneStatus.CIRCLING;
        this.anchorPoint = anchorpoint;
    }

    public void startCircling(Entity anchorpoint){
        this.planestatus = PlaneStatus.CIRCLING;
        this.anchorPoint = anchorpoint.blockPosition();
    }

    public void startCircling(){
        this.planestatus = PlaneStatus.CIRCLING;
        this.anchorPoint = this.blockPosition();
    }

    @Override
    protected void tickDeath() {
        Vector3d movement = this.getDeltaMovement();
        double d0 = this.getX() + movement.x;
        double d1 = this.getY() + movement.y;
        double d2 = this.getZ() + movement.z;
        this.level.addParticle(ParticleTypes.SMOKE, d0, d1 + 0.5D, d2, 0.0D, 0.0D, 0.0D);

        if(this.isOnGround() || isInWater()) {
            this.level.explode(this, this.getX(), this.getY(0.0625D), this.getZ(), 1.0F, Explosion.Mode.DESTROY);
            this.remove();
        }

    }

    public void die(DamageSource cause) {
        if (net.minecraftforge.common.ForgeHooks.onLivingDeath(this, cause)) return;
        if (!this.removed && !this.dead) {
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
            if (this.level instanceof ServerWorld) {
                if (entity != null) {
                    entity.killed((ServerWorld) this.level, this);
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
    public IPacket<?> getAddEntityPacket() {
        NetworkHooks.getEntitySpawningPacket(this);
        return super.getAddEntityPacket();
    }

    @Override
    protected int calculateFallDamage(float distance, float damageMultiplier) {
        return 0;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new PlaneAttackGoal(this));
        this.goalSelector.addGoal(1, new PlaneReturntoOwnerGoal(this));
        this.goalSelector.addGoal(2, new PlaneWanderAroundCarrierGoal(this));
        this.targetSelector.addGoal(1, new PlaneInterceptGoal(this));
    }

    abstract class MoveGoal extends Goal {
        public MoveGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        protected boolean touchingTarget() {
            return AbstractEntityPlanes.this.moveTargetPoint.distanceToSqr(AbstractEntityPlanes.this.getX(), AbstractEntityPlanes.this.getY(), AbstractEntityPlanes.this.getZ()) < 4.0D;
        }
    }

    class OrbitPointGoal extends MoveGoal {
        private float angle;
        private float distance;
        private float height;
        private float clockwise;

        private OrbitPointGoal() {
        }

        public boolean canUse() {
            return AbstractEntityPlanes.this.getTarget() == null || AbstractEntityPlanes.this.planestatus == PlaneStatus.CIRCLING;
        }

        public void start() {
            this.distance = 5.0F + AbstractEntityPlanes.this.random.nextFloat() * 10.0F;
            this.height = -4.0F + AbstractEntityPlanes.this.random.nextFloat() * 9.0F;
            this.clockwise = AbstractEntityPlanes.this.random.nextBoolean() ? 1.0F : -1.0F;
            this.selectNext();
        }

        public void tick() {
            if (AbstractEntityPlanes.this.random.nextInt(350) == 0) {
                this.height = -4.0F + AbstractEntityPlanes.this.random.nextFloat() * 9.0F;
            }

            if (AbstractEntityPlanes.this.random.nextInt(250) == 0) {
                ++this.distance;
                if (this.distance > 15.0F) {
                    this.distance = 5.0F;
                    this.clockwise = -this.clockwise;
                }
            }

            if (AbstractEntityPlanes.this.random.nextInt(450) == 0) {
                this.angle = AbstractEntityPlanes.this.random.nextFloat() * 2.0F * (float)Math.PI;
                this.selectNext();
            }

            if (this.touchingTarget()) {
                this.selectNext();
            }

            if (AbstractEntityPlanes.this.moveTargetPoint.y < AbstractEntityPlanes.this.getY() && !AbstractEntityPlanes.this.level.isEmptyBlock(AbstractEntityPlanes.this.blockPosition().below(1))) {
                this.height = Math.max(1.0F, this.height);
                this.selectNext();
            }

            if (AbstractEntityPlanes.this.moveTargetPoint.y > AbstractEntityPlanes.this.getY() && !AbstractEntityPlanes.this.level.isEmptyBlock(AbstractEntityPlanes.this.blockPosition().above(1))) {
                this.height = Math.min(-1.0F, this.height);
                this.selectNext();
            }

            if(AbstractEntityPlanes.this.tickCount%15==0){
                this.checkForHangerAvailability();
            }

        }

        private void selectNext() {
            if (BlockPos.ZERO.equals(AbstractEntityPlanes.this.anchorPoint)) {
                AbstractEntityPlanes.this.anchorPoint = AbstractEntityPlanes.this.blockPosition();
            }

            this.angle += this.clockwise * 15.0F * ((float)Math.PI / 180F);
            Vector3d pos = Vector3d.atLowerCornerOf(AbstractEntityPlanes.this.anchorPoint).add((double)(this.distance * MathHelper.cos(this.angle)), (double)(-4.0F + this.height), (double)(this.distance * MathHelper.sin(this.angle)));;
            AbstractEntityPlanes.this.getNavigation().moveTo(pos.x, pos.y, pos.z, 1);
        }

        protected void checkForHangerAvailability(){
            if(AbstractEntityPlanes.this.getOwner() instanceof EntityKansenAircraftCarrier) {
                AbstractEntityPlanes.this.getNavigation().moveTo(AbstractEntityPlanes.this.getOwner(), 2);
                AbstractEntityPlanes.this.setReturningtoOwner();

                if (!(((EntityKansenAircraftCarrier) AbstractEntityPlanes.this.getOwner()).hasRigging())) {
                    AbstractEntityPlanes.this.crashThePlane();
                    return;
                }
                IItemHandler Hanger = ((EntityKansenAircraftCarrier) AbstractEntityPlanes.this.getOwner()).getHanger();

                if (Hanger == null) {
                    return;
                }

                ItemStack PlaneStack = serializePlane(AbstractEntityPlanes.this);
                for (int i = 0; i < Hanger.getSlots(); i++) {
                    if (Hanger.insertItem(i, PlaneStack, true) == ItemStack.EMPTY) {
                        AbstractEntityPlanes.this.setReturningtoOwner();
                        break;
                    }
                }

            }
        };
    }

    class BodyHelperController extends BodyController {
        public BodyHelperController(MobEntity p_i49925_2_) {
            super(p_i49925_2_);
        }

        public void clientTick() {
            AbstractEntityPlanes.this.yHeadRot = AbstractEntityPlanes.this.yBodyRot;
            AbstractEntityPlanes.this.yBodyRot = AbstractEntityPlanes.this.yRot;
        }
    }

    static class LookHelperController extends LookController {
        public LookHelperController(MobEntity p_i48802_2_) {
            super(p_i48802_2_);
        }

        public void tick() {
        }
    }
    
    public enum PlaneStatus{
        CIRCLING,
        ATTACKING,
        RETURNING
    }
}
