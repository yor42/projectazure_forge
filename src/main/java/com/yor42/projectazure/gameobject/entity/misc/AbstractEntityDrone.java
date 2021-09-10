package com.yor42.projectazure.gameobject.entity.misc;

import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.entity.PlaneFlyMovementController;
import com.yor42.projectazure.gameobject.entity.ai.goals.DroneReturntoOwnerGoal;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.libs.utils.ItemStackUtils;
import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.BeeEntity;
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
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;
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

    protected static final DataParameter<Integer> AMMO = EntityDataManager.createKey(AbstractEntityDrone.class, DataSerializers.VARINT);
    protected static final DataParameter<Integer> FUEL = EntityDataManager.createKey(AbstractEntityDrone.class, DataSerializers.VARINT);
    protected static final DataParameter<Optional<UUID>> OWNER_UUID = EntityDataManager.createKey(AbstractEntityDrone.class, DataSerializers.OPTIONAL_UNIQUE_ID);

    protected AbstractEntityDrone(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
        this.moveController = new FlyingMovementController(this, 20, true);
        this.navigator = new FlyingPathNavigator(this, worldIn);
        this.setPathPriority(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathPriority(PathNodeType.WATER, -1.0F);
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
        return this.dataManager.get(OWNER_UUID);
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

                return !((TameableEntity) targetEntity).isOwner((LivingEntity) this.getOwner().get());
            }
        }
        return true;
    }

    public void setOwner(@Nullable LivingEntity owner) {
        this.getDataManager().set(OWNER_UUID, owner == null? Optional.empty() : Optional.of(owner.getUniqueID()));
    }

    public void setOwnerFromUUID(@Nullable UUID ownerUUID) {
        this.getDataManager().set(OWNER_UUID, ownerUUID == null? Optional.empty() : Optional.of(ownerUUID));
    }

    public boolean isOwner(LivingEntity owner){
        return this.getOwnerUUID().isPresent() && this.getOwnerUUID().get() == owner.getUniqueID();
    }

    public boolean isOutofFuel() {
        return false;
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("isreturning", this.isReturningToOwner());
        if(this.getOwnerUUID().isPresent()) {
            UUID OwnerID = this.getOwnerUUID().get();
            compound.putUniqueId("owner", OwnerID);
        }
        compound.putInt("fuel", this.getRemainingFuel());
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.setReturningtoOwner(compound.getBoolean("isreturning"));
        if (this.getServer() != null) {
            if (compound.hasUniqueId("owner")) {
                this.setOwnerFromUUID(compound.getUniqueId("owner"));
            }
        }
        this.setRemainingFuel(compound.getInt("fuel"));
    }

    @Override
    public ActionResultType applyPlayerInteraction(PlayerEntity player, Vector3d vec, Hand hand) {

        if (this.isOwner(player)) {
            if (player.isSneaking()) {
                this.serializePlane(this.getEntityWorld());
                return ActionResultType.SUCCESS;
            }
        }
        else if(!this.getOwner().isPresent()){
            this.remove();
        }

        return super.applyPlayerInteraction(player, vec, hand);
    }

    public int getRemainingFuel(){
        return this.dataManager.get(FUEL);
    }

    public void setRemainingFuel(int value){
        this.dataManager.set(FUEL, value);
    }

    public void serializePlane(World world) {
        if (!world.isRemote()) {
            ItemEntity entity = new ItemEntity(this.getEntityWorld(), this.getPosX(), this.getPosY(), this.getPosZ(), turnPlanetoItemStack());
            world.addEntity(entity);
            this.remove();
        }
    }

    public ItemStack turnPlanetoItemStack(){
        ItemStack stack = new ItemStack(this.getDroneItem());
        CompoundNBT nbt = stack.getOrCreateTag();
        CompoundNBT planedata = new CompoundNBT();
        this.writeAdditional(planedata);
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
        return this.dataManager.get(AMMO);
    }

    public void setAmmo(int value){
        this.dataManager.set(AMMO, value);
    }

    public int useAmmo(){
        if(this.getammo()>0) {
            int ammoAfterUse = Math.min(this.getammo() - 1,0);
            this.setAmmo(ammoAfterUse);
        }
        return 0;
    }

    @Override
    public boolean isOnLadder() {
        return false;
    }

    @Override
    public boolean hasNoGravity() {
        return this.isAlive() && !this.isOutofFuel();
    }

    @Override
    protected int calculateFallDamage(float distance, float damageMultiplier) {
        return 0;
    }

    public abstract Item getDroneItem();

    @Override
    public void livingTick() {
        super.livingTick();
        if (this.isOutofFuel() && this.getNavigator().hasPath()) {
            this.getNavigator().clearPath();
        }

        if(this.isInWater()){
            this.serializePlane(this.getEntityWorld());
        }
    }
    public Optional<Entity> getOwner(){
        if(!this.getEntityWorld().isRemote() && this.getOwnerUUID().isPresent()){
            return Optional.ofNullable(((ServerWorld)this.getEntityWorld()).getEntityByUuid(this.getOwnerUUID().get()));
        }
        return Optional.empty();
    }

    @Override
    public IPacket<?> createSpawnPacket() {
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
    protected void registerData() {
        super.registerData();
        this.dataManager.register(OWNER_UUID, Optional.empty());
        this.dataManager.register(AMMO, 0);
        this.dataManager.register(FUEL, 0);
    }

    class FlyRandomlyGoal extends Goal {
        FlyRandomlyGoal() {
            this.setMutexFlags(EnumSet.of(MOVE));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            return AbstractEntityDrone.this.navigator.noPath() && !AbstractEntityDrone.this.isOutofFuel() && AbstractEntityDrone.this.rand.nextInt(10) == 0;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting() {
            return AbstractEntityDrone.this.navigator.hasPath();
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            Vector3d vector3d = this.getRandomLocation();
            if (vector3d != null) {
                AbstractEntityDrone.this.navigator.setPath(AbstractEntityDrone.this.navigator.getPathToPos(new BlockPos(vector3d), 1), 1.0D);
            }

        }

        @Nullable
        private Vector3d getRandomLocation() {
            Vector3d vector3d;
            if (AbstractEntityDrone.this.getOwner().isPresent()) {
                Vector3d vector3d1 = AbstractEntityDrone.this.getOwner().get().getPositionVec();
                vector3d = vector3d1.subtract(AbstractEntityDrone.this.getPositionVec()).normalize();
            } else {
                vector3d = AbstractEntityDrone.this.getLook(0.0F);
            }

            Vector3d vector3d2 = RandomPositionGenerator.findAirTarget(AbstractEntityDrone.this, 8, 7, vector3d, ((float) Math.PI / 2F), 2, 1);
            return vector3d2 != null ? vector3d2 : RandomPositionGenerator.findGroundTarget(AbstractEntityDrone.this, 8, 4, -2, vector3d, (float) Math.PI / 2F);
        }
    }

    class DroneFollowOwnerGoal extends Goal {

        public DroneFollowOwnerGoal() {
            this.setMutexFlags(EnumSet.of(MOVE));
        }

        @Override
        public boolean shouldExecute() {
            if (AbstractEntityDrone.this.getOwner().isPresent() && AbstractEntityDrone.this.getOwner().get() instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity) AbstractEntityDrone.this.getOwner().get();
                return !livingentity.isSpectator() && livingentity.getEntityWorld() == AbstractEntityDrone.this.getEntityWorld() && !(AbstractEntityDrone.this.getDistanceSq(livingentity) < 16);
            }
            return false;
        }

        @Override
        public void resetTask() {
            super.resetTask();
        }

        public void tick() {


            if (AbstractEntityDrone.this.getOwner().isPresent()) {
                boolean cond1 = AbstractEntityDrone.this.getDistance(AbstractEntityDrone.this.getOwner().get()) >= 10.0D;
                boolean cond2 = !AbstractEntityDrone.this.getLeashed();
                boolean cond3 = !AbstractEntityDrone.this.isPassenger();

                if (cond1 && cond2 && cond3) {
                    //func_226330_g_();
                    AbstractEntityDrone.this.getLookController().setLookPositionWithEntity(AbstractEntityDrone.this.getOwner().get(), 10.0F, (float) AbstractEntityDrone.this.getVerticalFaceSpeed());
                    AbstractEntityDrone.this.getNavigator().tryMoveToEntityLiving(AbstractEntityDrone.this.getOwner().get(), 1);
                }

                if (AbstractEntityDrone.this.getDistance(AbstractEntityDrone.this.getOwner().get()) > 32) {
                    this.tryToTeleportNearEntity();
                }

            }
        }

        private void tryToTeleportNearEntity() {

            if(AbstractEntityDrone.this.getOwner().isPresent()) {

                BlockPos blockpos = AbstractEntityDrone.this.getOwner().get().getPosition();

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
            return AbstractEntityDrone.this.getRNG().nextInt(max - min + 1) + min;
        }

        private boolean tryToTeleportToLocation(int x, int y, int z) {

            if(!AbstractEntityDrone.this.getOwner().isPresent()){
                return false;
            }

            if (Math.abs((double) x - AbstractEntityDrone.this.getOwner().get().getPosX()) < 2.0D && Math.abs((double) z - AbstractEntityDrone.this.getOwner().get().getPosZ()) < 2.0D) {
                return false;
            } else if (!this.isTeleportFriendlyBlock(new BlockPos(x, y, z))) {
                return false;
            } else {
                AbstractEntityDrone.this.setLocationAndAngles((double) x + 0.5D, y, (double) z + 0.5D, AbstractEntityDrone.this.rotationYaw, AbstractEntityDrone.this.rotationPitch);
                AbstractEntityDrone.this.getNavigator().clearPath();
                return true;
            }
        }

        private boolean isTeleportFriendlyBlock(BlockPos pos) {
            PathNodeType pathnodetype = WalkNodeProcessor.func_237231_a_(AbstractEntityDrone.this.getEntityWorld(), pos.toMutable());
            if (pathnodetype != PathNodeType.WALKABLE) {
                return false;
            } else {
                BlockState blockstate = AbstractEntityDrone.this.getEntityWorld().getBlockState(pos.down());
                if (blockstate.getBlock() instanceof LeavesBlock) {
                    return false;
                } else {
                    BlockPos blockpos = pos.subtract(AbstractEntityDrone.this.getPosition());
                    return AbstractEntityDrone.this.getEntityWorld().hasNoCollisions(AbstractEntityDrone.this, AbstractEntityDrone.this.getBoundingBox().offset(blockpos));
                }
            }
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float p_70097_2_) {
        if(source.getTrueSource() == this){
            return false;
        }

        return super.attackEntityFrom(source, p_70097_2_);
    }
}