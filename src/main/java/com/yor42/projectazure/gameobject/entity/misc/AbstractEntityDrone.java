package com.yor42.projectazure.gameobject.entity.misc;

import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.entity.ai.goals.DroneReturntoOwnerGoal;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.libs.utils.ItemStackUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.WalkNodeProcessor;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Optional;
import java.util.UUID;

import static net.minecraft.entity.ai.goal.Goal.Flag.MOVE;

public abstract class AbstractEntityDrone extends CreatureEntity implements IAnimatable {
    public AnimationFactory factory = new AnimationFactory(this);
    private boolean isReturningToOwner = false;

    protected static final DataParameter<Integer> AMMO = EntityDataManager.defineId(AbstractEntityDrone.class, DataSerializers.INT);
    protected static final DataParameter<Integer> FUEL = EntityDataManager.defineId(AbstractEntityDrone.class, DataSerializers.INT);
    protected static final DataParameter<Optional<UUID>> OWNER_UUID = EntityDataManager.defineId(AbstractEntityDrone.class, DataSerializers.OPTIONAL_UUID);

    protected AbstractEntityDrone(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
        this.moveControl = new FlyingMovementController(this, 20, true);
        this.navigation = new FlyingPathNavigator(this, worldIn);
        this.setPathfindingMalus(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(PathNodeType.WATER, -1.0F);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "propeller_controller", 2, this::propeller_predicate));
        data.addAnimationController(new AnimationController<>(this, "body_controller", 20, this::body_predicate));
    }

    protected abstract <T extends IAnimatable> PlayState body_predicate(AnimationEvent<T> tAnimationEvent);

    protected abstract PlayState propeller_predicate(AnimationEvent<AbstractEntityDrone> abstractEntityDroneAnimationEvent);

    public void setReturningtoOwner(boolean value) {
        this.isReturningToOwner = value;
    }

    public boolean isReturningToOwner() {
        return this.isReturningToOwner;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    public Optional<UUID> getOwnerUUID(){
        return this.entityData.get(OWNER_UUID);
    }

    public boolean hasOwner(){
        return this.getOwnerUUID().isPresent();
    }

    public boolean shouldAttackEntity(LivingEntity targetEntity, LivingEntity owner){

        if(this.getOwner().isPresent()) {
            if (targetEntity instanceof TameableEntity) {

                if(targetEntity instanceof AbstractEntityCompanion && ((AbstractEntityCompanion) targetEntity).getOwner() != null){
                    return PAConfig.CONFIG.EnablePVP.get();
                }

                return !((TameableEntity) targetEntity).isOwnedBy((LivingEntity) this.getOwner().get());
            }
        }
        return true;
    }

    public void setOwner(@Nullable LivingEntity owner) {
        this.getEntityData().set(OWNER_UUID, owner == null? Optional.empty() : Optional.of(owner.getUUID()));
    }

    public void setOwnerFromUUID(@Nullable UUID ownerUUID) {
        this.getEntityData().set(OWNER_UUID, ownerUUID == null? Optional.empty() : Optional.of(ownerUUID));
    }

    public boolean isOwner(LivingEntity owner){
        return this.getOwnerUUID().isPresent() && this.getOwnerUUID().get() == owner.getUUID();
    }

    public boolean isOutofFuel() {
        return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("isreturning", this.isReturningToOwner());
        if(this.getOwnerUUID().isPresent()) {
            UUID OwnerID = this.getOwnerUUID().get();
            compound.putUUID("owner", OwnerID);
        }
        compound.putInt("fuel", this.getRemainingFuel());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        this.setReturningtoOwner(compound.getBoolean("isreturning"));
        if (this.getServer() != null) {
            if (compound.hasUUID("owner")) {
                this.setOwnerFromUUID(compound.getUUID("owner"));
            }
        }
        this.setRemainingFuel(compound.getInt("fuel"));
    }

    @Override
    public ActionResultType interactAt(PlayerEntity player, Vector3d vec, Hand hand) {

        if (this.isOwner(player)) {
            if (player.isShiftKeyDown()) {
                this.serializePlane(this.getCommandSenderWorld());
                return ActionResultType.SUCCESS;
            }
        }
        else if(!this.getOwner().isPresent()){
            this.remove();
        }

        return super.interactAt(player, vec, hand);
    }

    public int getRemainingFuel(){
        return this.entityData.get(FUEL);
    }

    public void setRemainingFuel(int value){
        this.entityData.set(FUEL, value);
    }

    public void serializePlane(World world) {
        if (!world.isClientSide()) {
            ItemEntity entity = new ItemEntity(this.getCommandSenderWorld(), this.getX(), this.getY(), this.getZ(), turnPlanetoItemStack());
            world.addFreshEntity(entity);
            this.remove();
        }
    }

    public ItemStack turnPlanetoItemStack(){
        ItemStack stack = new ItemStack(this.getDroneItem());
        CompoundNBT nbt = stack.getOrCreateTag();
        CompoundNBT planedata = new CompoundNBT();
        this.addAdditionalSaveData(planedata);
        nbt.put("planedata", planedata);
        nbt.putInt("fuel", this.getRemainingFuel());
        ItemStackUtils.setCurrentHP(stack, (int) this.getHealth());
        ItemStackUtils.setAmmo(stack, this.getammo());
        return stack;
    }

    public boolean hasAmmo(){
        return this.getammo()>0;
    }

    public int getammo() {
        return this.entityData.get(AMMO);
    }

    public void setAmmo(int value){
        this.entityData.set(AMMO, value);
    }

    public int useAmmo(){
        if(this.getammo()>0) {
            int ammoAfterUse = Math.min(this.getammo() - 1,0);
            this.setAmmo(ammoAfterUse);
        }
        return 0;
    }

    @Override
    public boolean onClimbable() {
        return false;
    }

    @Override
    public boolean isNoGravity() {
        return this.isAlive() && !this.isOutofFuel();
    }

    @Override
    protected int calculateFallDamage(float distance, float damageMultiplier) {
        return 0;
    }

    public abstract Item getDroneItem();

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isOutofFuel() && this.getNavigation().isInProgress()) {
            this.getNavigation().stop();
        }

        if(this.isInWater()){
            this.serializePlane(this.getCommandSenderWorld());
        }
    }
    public Optional<Entity> getOwner(){
        if(!this.getCommandSenderWorld().isClientSide() && this.getOwnerUUID().isPresent()){
            return Optional.ofNullable(((ServerWorld)this.getCommandSenderWorld()).getEntity(this.getOwnerUUID().get()));
        }
        return Optional.empty();
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new DroneReturntoOwnerGoal(this));
        this.goalSelector.addGoal(3, new DroneFollowOwnerGoal());
        this.goalSelector.addGoal(4, new FlyRandomlyGoal());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(OWNER_UUID, Optional.empty());
        this.entityData.define(AMMO, 0);
        this.entityData.define(FUEL, 0);
    }

    class FlyRandomlyGoal extends Goal {
        FlyRandomlyGoal() {
            this.setFlags(EnumSet.of(MOVE));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            return AbstractEntityDrone.this.navigation.isDone() && !AbstractEntityDrone.this.isOutofFuel() && AbstractEntityDrone.this.random.nextInt(10) == 0;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            return AbstractEntityDrone.this.navigation.isInProgress();
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            Vector3d vector3d = this.getRandomLocation();
            if (vector3d != null) {
                AbstractEntityDrone.this.navigation.moveTo(AbstractEntityDrone.this.navigation.createPath(new BlockPos(vector3d), 1), 1.0D);
            }

        }

        @Nullable
        private Vector3d getRandomLocation() {
            Vector3d vector3d;
            if (AbstractEntityDrone.this.getOwner().isPresent()) {
                Vector3d vector3d1 = AbstractEntityDrone.this.getOwner().get().position();
                vector3d = vector3d1.subtract(AbstractEntityDrone.this.position()).normalize();
            } else {
                vector3d = AbstractEntityDrone.this.getViewVector(0.0F);
            }

            Vector3d vector3d2 = RandomPositionGenerator.getAboveLandPos(AbstractEntityDrone.this, 8, 7, vector3d, ((float) Math.PI / 2F), 2, 1);
            return vector3d2 != null ? vector3d2 : RandomPositionGenerator.getAirPos(AbstractEntityDrone.this, 8, 4, -2, vector3d, (float) Math.PI / 2F);
        }
    }

    class DroneFollowOwnerGoal extends Goal {

        public DroneFollowOwnerGoal() {
            this.setFlags(EnumSet.of(MOVE));
        }

        @Override
        public boolean canUse() {
            if (AbstractEntityDrone.this.getOwner().isPresent() && AbstractEntityDrone.this.getOwner().get() instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity) AbstractEntityDrone.this.getOwner().get();
                return !livingentity.isSpectator() && livingentity.getCommandSenderWorld() == AbstractEntityDrone.this.getCommandSenderWorld() && !(AbstractEntityDrone.this.distanceToSqr(livingentity) < 16);
            }
            return false;
        }

        @Override
        public void stop() {
            super.stop();
        }

        public void tick() {


            if (AbstractEntityDrone.this.getOwner().isPresent()) {
                boolean cond1 = AbstractEntityDrone.this.distanceTo(AbstractEntityDrone.this.getOwner().get()) >= 10.0D;
                boolean cond2 = !AbstractEntityDrone.this.isLeashed();
                boolean cond3 = !AbstractEntityDrone.this.isPassenger();

                if (cond1 && cond2 && cond3) {
                    //teleportToOwner();
                    AbstractEntityDrone.this.getLookControl().setLookAt(AbstractEntityDrone.this.getOwner().get(), 10.0F, (float) AbstractEntityDrone.this.getMaxHeadXRot());
                    AbstractEntityDrone.this.getNavigation().moveTo(AbstractEntityDrone.this.getOwner().get(), 1);
                }

                if (AbstractEntityDrone.this.distanceTo(AbstractEntityDrone.this.getOwner().get()) > 32) {
                    this.tryToTeleportNearEntity();
                }

            }
        }

        private void tryToTeleportNearEntity() {

            if(AbstractEntityDrone.this.getOwner().isPresent()) {

                BlockPos blockpos = AbstractEntityDrone.this.getOwner().get().blockPosition();

                for (int i = 0; i < 10; ++i) {
                    int j = this.getRandomNumber(-3, 3);
                    int k = this.getRandomNumber(-1, 1);
                    int l = this.getRandomNumber(-3, 3);
                    boolean flag = this.tryToTeleportToLocation(blockpos.getX() + j, blockpos.getY() + k, blockpos.getZ() + l);
                    if (flag) {
                        return;
                    }
                }
            }
        }

        private int getRandomNumber(int min, int max) {
            return AbstractEntityDrone.this.getRandom().nextInt(max - min + 1) + min;
        }

        private boolean tryToTeleportToLocation(int x, int y, int z) {

            if(!AbstractEntityDrone.this.getOwner().isPresent()){
                return false;
            }

            if (Math.abs((double) x - AbstractEntityDrone.this.getOwner().get().getX()) < 2.0D && Math.abs((double) z - AbstractEntityDrone.this.getOwner().get().getZ()) < 2.0D) {
                return false;
            } else if (!this.isTeleportFriendlyBlock(new BlockPos(x, y, z))) {
                return false;
            } else {
                AbstractEntityDrone.this.moveTo((double) x + 0.5D, y, (double) z + 0.5D, AbstractEntityDrone.this.yRot, AbstractEntityDrone.this.xRot);
                AbstractEntityDrone.this.getNavigation().stop();
                return true;
            }
        }

        private boolean isTeleportFriendlyBlock(BlockPos pos) {
            PathNodeType pathnodetype = WalkNodeProcessor.getBlockPathTypeStatic(AbstractEntityDrone.this.getCommandSenderWorld(), pos.mutable());
            if (pathnodetype != PathNodeType.WALKABLE) {
                return false;
            } else {
                BlockState blockstate = AbstractEntityDrone.this.getCommandSenderWorld().getBlockState(pos.below());
                if (blockstate.getBlock() instanceof LeavesBlock) {
                    return false;
                } else {
                    BlockPos blockpos = pos.subtract(AbstractEntityDrone.this.blockPosition());
                    return AbstractEntityDrone.this.getCommandSenderWorld().noCollision(AbstractEntityDrone.this, AbstractEntityDrone.this.getBoundingBox().move(blockpos));
                }
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float p_70097_2_) {
        if(source.getEntity() == this){
            return false;
        }

        return super.hurt(source, p_70097_2_);
    }
}
