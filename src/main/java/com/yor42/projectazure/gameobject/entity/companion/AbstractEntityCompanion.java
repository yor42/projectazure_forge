package com.yor42.projectazure.gameobject.entity.companion;

import com.google.common.collect.ImmutableList;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.entity.ai.*;
import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityKansenBase;
import com.yor42.projectazure.gameobject.items.ItemBandage;
import com.yor42.projectazure.setup.register.registerItems;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.crypto.Data;
import java.util.Date;

public abstract class AbstractEntityCompanion extends TameableEntity implements IAnimatable {

    protected final IItemHandlerModifiable EQUIPMENT = new IItemHandlerModifiable() {

        private final EquipmentSlotType[] EQUIPMENTSLOTS = new EquipmentSlotType[]{EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET};


        @Override
        public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
            EquipmentSlotType slottype;

            switch (slot) {
                case 1:
                    slottype = EquipmentSlotType.OFFHAND;
                    break;
                case 2:
                    slottype = EquipmentSlotType.HEAD;
                    break;
                case 3:

                    slottype = EquipmentSlotType.CHEST;
                    break;
                case 4:
                    slottype = EquipmentSlotType.LEGS;
                    break;
                case 5:
                    slottype = EquipmentSlotType.FEET;
                    break;
                default:
                    slottype = EquipmentSlotType.MAINHAND;
            }
            AbstractEntityCompanion.this.setItemStackToSlot(slottype, stack);
        }

        @Override
        public int getSlots() {
            return 6;
        }

        @Nonnull
        @Override
        public ItemStack getStackInSlot(int slot) {
            EquipmentSlotType slottype;

            switch (slot) {
                case 1:
                    slottype = EquipmentSlotType.OFFHAND;
                    break;
                case 2:
                    slottype = EquipmentSlotType.HEAD;
                    break;
                case 3:
                    slottype = EquipmentSlotType.CHEST;
                    break;
                case 4:
                    slottype = EquipmentSlotType.LEGS;
                    break;
                case 5:
                    slottype = EquipmentSlotType.FEET;
                    break;
                default:
                    slottype = EquipmentSlotType.MAINHAND;
            }
            return AbstractEntityCompanion.this.getItemStackFromSlot(slottype);
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

            if(!isItemValid(slot, stack)){
                return stack;
            }
            ItemStack existingstack = getStackInSlot(slot);
            int limit = this.getSlotLimit(slot);

            if(!existingstack.isEmpty()){
                if (!ItemHandlerHelper.canItemStacksStack(stack, existingstack)) {
                    return stack;
                }
                limit -= existingstack.getCount();
            }

            if (limit <= 0)
                return stack;

            boolean reachedLimit = stack.getCount() > limit;

            if (!simulate)
            {
                if (existingstack.isEmpty()) {
                    setStackInSlot(slot, stack);
                }
                else {
                    existingstack.grow(reachedLimit ? limit : stack.getCount());
                    setStackInSlot(slot, existingstack);
                }
            }
            return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount()- limit) : ItemStack.EMPTY;
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (amount == 0)
                return ItemStack.EMPTY;

            ItemStack existing = getStackInSlot(slot);

            if (existing.isEmpty())
                return ItemStack.EMPTY;

            int toExtract = Math.min(amount, existing.getMaxStackSize());

            if (existing.getCount() <= toExtract)
            {
                if (!simulate)
                {
                    setStackInSlot(slot, ItemStack.EMPTY);
                    return existing;
                }
                else
                {
                    return existing.copy();
                }
            }
            else
            {
                if (!simulate)
                {
                    setStackInSlot(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
                }

                return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
            }
        }

        @Override
        public int getSlotLimit(int slot) {
            switch (slot){
                case 0:
                case 1:
                    return 64;
                default:
                    return 1;
            }
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            switch(slot){
                case 0:
                case 1:
                    return true;
                default:
                    if(stack.getItem() instanceof ArmorItem) {
                        return stack.canEquip(EQUIPMENTSLOTS[slot-2], AbstractEntityCompanion.this);
                    }
                    else return false;
            }
        }
    };

    protected int level,  patAnimationTime, LimitBreakLv, patTimer, healAnimationTime;
    protected double affection, exp;
    protected boolean isFreeRoaming;
    protected boolean isMeleeing, isOpeningDoor;
    protected int awakeningLevel;

    private BlockPos StayCenterPos;

    protected static final DataParameter<Boolean> SITTING = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> OPENINGDOOR = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> MELEEATTACKING = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> USINGBOW = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Integer> MAXPATEFFECTCOUNT = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.VARINT);
    protected static final DataParameter<Integer> PATEFFECTCOUNT = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.VARINT);
    protected static final DataParameter<Integer> PATCOOLDOWN = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.VARINT);
    protected static final DataParameter<Boolean> OATHED = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<BlockPos> STAYPOINT = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.BLOCK_POS);

    private static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.HOME, MemoryModuleType.MOBS, MemoryModuleType.VISIBLE_MOBS, MemoryModuleType.VISIBLE_VILLAGER_BABIES, MemoryModuleType.NEAREST_PLAYERS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER, MemoryModuleType.WALK_TARGET, MemoryModuleType.LOOK_TARGET, MemoryModuleType.PATH, MemoryModuleType.OPENED_DOORS, MemoryModuleType.NEAREST_BED, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.NEAREST_HOSTILE, MemoryModuleType.SECONDARY_JOB_SITE, MemoryModuleType.HIDING_PLACE, MemoryModuleType.HEARD_BELL_TIME, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.LAST_SLEPT, MemoryModuleType.LAST_WOKEN);

    protected AbstractEntityCompanion(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
        this.setAffection(40F);
        this.getAttribute(ForgeMod.SWIM_SPEED.get()).setBaseValue(1.0F);
        this.setFreeRoaming(false);
    }

    public void setOathed(boolean bool){
        this.dataManager.set(OATHED, bool);
    }

    public boolean isOathed(){
        return this.dataManager.get(OATHED);
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean addLevel(int deltaLevel){
        if(this.getLevel()+deltaLevel > this.getMaxLevel()){
            return false;
        }
        this.setLevel(this.level += deltaLevel);
        return true;
    }

    protected byte[] BodyHeightStand;
    protected byte[] BodyHeightSit;

    public boolean isPVPenabled(){
        return false;
    }

    protected int getAwakeningLevel(){
        return this.awakeningLevel;
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putDouble("affection", this.affection);
        compound.putInt("patcooldown", this.dataManager.get(PATCOOLDOWN));
        compound.putBoolean("oathed", this.dataManager.get(OATHED));
        compound.putDouble("exp", this.exp);
        compound.putInt("level", this.level);
        compound.putInt("limitbreaklv", this.LimitBreakLv);
        compound.putInt("awaken", this.awakeningLevel);
        compound.putBoolean("freeroaming", this.isFreeRoaming());
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.affection = compound.getFloat("affection");
        this.dataManager.set(PATCOOLDOWN, compound.getInt("patcooldown"));
        this.dataManager.set(OATHED, compound.getBoolean("oathed"));
        this.level = compound.getInt("level");
        this.exp = compound.getFloat("exp");
        this.LimitBreakLv = compound.getInt("limitbreaklv");
        this.awakeningLevel = compound.getInt("awaken");
        this.setFreeRoaming(compound.getBoolean("freeroaming"));
    }

    public boolean isFreeRoaming() {
        return this.isFreeRoaming;
    }

    public void setFreeRoaming(boolean freeRoaming) {
        this.isFreeRoaming = freeRoaming;
    }

    @Nullable
    @Override
    public AgeableEntity func_241840_a(ServerWorld p_241840_1_, AgeableEntity p_241840_2_) {
        return null;
    }

    @Override
    public boolean canMateWith(AnimalEntity otherAnimal) {
        return false;
    }

    public boolean canUseRigging(){
        return false;
    }

    @Override
    public boolean isChild() {
        return false;
    }

    @Override
    public boolean canDespawn(double distanceToClosestPlayer) {
        return false;
    }

    protected final AnimationFactory factory = new AnimationFactory(this);

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller", 5, this::predicate));
    }

    public boolean isMeleeing(){
        return this.dataManager.get(MELEEATTACKING);
    }

    protected abstract <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event);

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(SITTING, this.isSitting());
        this.dataManager.register(OPENINGDOOR, false);
        this.dataManager.register(MELEEATTACKING, false);
        this.dataManager.register(MAXPATEFFECTCOUNT, 0);
        this.dataManager.register(PATEFFECTCOUNT, 0);
        this.dataManager.register(PATCOOLDOWN, 0);
        this.dataManager.register(OATHED, false);
        this.dataManager.register(USINGBOW, false);
        this.dataManager.register(STAYPOINT, BlockPos.ZERO);
    }

    public void setOpeningdoor(boolean openingdoor){
        this.isOpeningDoor = openingdoor;
        this.dataManager.set(OPENINGDOOR, this.isOpeningDoor);
    }

    public boolean isOpeningDoor() {
        return this.dataManager.get(OPENINGDOOR);
    }

    public void setUsingbow(boolean usingbow){
        this.dataManager.set(USINGBOW, usingbow);
    }

    public boolean isUsingBow() {
        return this.dataManager.get(USINGBOW);
    }

    public void setMeleeing(boolean attacking) {
        this.isMeleeing = attacking;
        this.dataManager.set(MELEEATTACKING, this.isMeleeing);

    }

    public boolean isBeingPatted() {
        return this.patAnimationTime !=0;
    }

    public double getMaxExp(){
        return 10+(5*this.getLevel());
    }

    public int getLimitBreakLv() {
        return this.LimitBreakLv;
    }

    public void setLimitBreakLv(int value) {
        this.LimitBreakLv = value;
    }

    public void addLimitBreak(int delta){
        this.LimitBreakLv += delta;
    }

    public void doLimitBreak(){
        this.addLimitBreak(1);
    }

    public double getAffection() {
        return this.affection;
    }

    public void setAffection(double affection){
        this.affection = affection;
    }

    public double getmaxAffextion(){
        return this.isOathed()? 200:100;
    };

    public void addAffection(double Delta){
        this.setAffection(Math.min(this.getAffection() + Delta, this.getmaxAffextion()));
    }

    public void setExp(double exp) {
        this.exp = exp;
    }

    public double getExp() {
        return this.exp;
    }

    public int getMaxLevel(){
        switch (this.LimitBreakLv) {
            default:
                return 70;
            case 1:
                return 80;
            case 2:
                return 90;
            case 3:
                switch (this.getAwakeningLevel()){
                    default:
                        return 100;
                    case 1:
                        return 110;
                    case 2:
                        return 120;
                }
        }
    }

    public double addExp(double deltaExp){
        if(this.getExp()+deltaExp >= this.getMaxExp()) {
            this.exp = this.getExp()+deltaExp;
            this.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
            while(this.exp>this.getMaxExp() && this.getLevel() <= this.getMaxLevel()){
                this.addLevel(1);
                this.exp-=this.getMaxExp();
            }
        }
        this.exp+=deltaExp;
        return this.exp;
    }

    @Override
    public boolean canFallInLove() {
        return false;
    }

    @Override
    public boolean isInLove() {
        return false;
    }

    @Override
    public void livingTick() {
        super.livingTick();

        if(this.healAnimationTime>0){
            this.healAnimationTime--;
        }

        if(this.patAnimationTime >0){

            this.navigator.clearPath();

            this.patAnimationTime--;
            this.patTimer++;

            double d0 = this.rand.nextGaussian() * 0.02D;
            double d1 = this.rand.nextGaussian() * 0.02D;
            double d2 = this.rand.nextGaussian() * 0.02D;
            if (this.patTimer%20 == 0) {
                if (this.dataManager.get(PATEFFECTCOUNT) < this.dataManager.get(MAXPATEFFECTCOUNT)) {
                    this.dataManager.set(PATEFFECTCOUNT, this.dataManager.get(PATEFFECTCOUNT)+1);
                    this.addAffection(0.1);
                    this.world.addParticle(ParticleTypes.HEART, this.getPosXRandom(1.0D), this.getPosYRandom() + 0.5D, this.getPosZRandom(1.0D), d0, d1, d2);

                } else {
                    this.dataManager.set(PATEFFECTCOUNT, this.dataManager.get(MAXPATEFFECTCOUNT));
                    this.addAffection(-0.15);
                    if(this.dataManager.get(PATCOOLDOWN) == 0){
                        this.dataManager.set(PATCOOLDOWN, (7+this.rand.nextInt(5))*1200);
                    }
                    this.world.addParticle(ParticleTypes.SMOKE, this.getPosXRandom(1.0D), this.getPosYRandom() + 0.5D, this.getPosZRandom(1.0D), d0, d1, d2);
                }
            }
        }
        else{

            this.patTimer = 0;

            if(this.dataManager.get(PATCOOLDOWN) > 0){
                this.dataManager.set(PATCOOLDOWN, this.dataManager.get(PATCOOLDOWN) -1);
            }
            else{
                this.dataManager.set(MAXPATEFFECTCOUNT,0);
                this.dataManager.set(PATEFFECTCOUNT,0);
            }
        }

        if(this.affection <0)
            this.affection = 0;
        else if(this.affection>100){
            if(!this.isOathed())
                this.affection=100;
            else if(this.affection > 200){
                this.affection = 200;
            }
        }

        this.navigator.getNodeProcessor().setCanEnterDoors(this.canOpenDoor());
        this.navigator.getNodeProcessor().setCanOpenDoors(this.canOpenDoor());
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new CompanionSwimGoal(this));
        this.goalSelector.addGoal(2, new SitGoal(this));
        this.goalSelector.addGoal(3, new KansenRideBoatAlongPlayerGoal(this, 1.0));
        this.goalSelector.addGoal(6, new CompanionMeleeGoal(this, 1.0D, true));
        this.goalSelector.addGoal(7, new CompanionFollowOwnerGoal(this, 1.0D, 5.0F, 2.0F, false));
        this.goalSelector.addGoal(8, new KansenWorkGoal(this, 1.0D));
       this.goalSelector.addGoal(9, new CompanionOpenDoorGoal(this, true));
       this.goalSelector.addGoal(10, new CompanionFreeroamGoal(this, 60, true));
        this.goalSelector.addGoal(11, new CompanionFindBedGoal(this));
        this.goalSelector.addGoal(12, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(13, new LookRandomlyGoal(this));
        //this.goalSelector.addGoal(9, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setCallsForHelp());
    }

    protected boolean canOpenDoor() {
        return true;
    }

    public void setTamedBy(PlayerEntity player) {
        this.setTamed(true);
        this.setOwnerId(player.getUniqueID());
        //Triggering Tame Animal goal at the beginning of the world doesn't feel right.
    }

    @Override
    public ActionResultType applyPlayerInteraction(PlayerEntity player, Vector3d vec, Hand hand) {
        if(this.isOwner(player)){
            if(player.isSneaking()){
                if(!world.isRemote) {
                    this.openGUI(player);
                    Main.PROXY.setSharedMob(this);
                    return ActionResultType.SUCCESS;
                }
            }
            else{
                //check is player is looking at Head
                Vector3d PlayerLook = player.getLook(1.0F).normalize();
                float eyeHeight = (float) (this.isEntitySleeping()? this.getPosYEye()-0.575: this.getPosYEye());


                Vector3d EyeDelta = new Vector3d(this.getPosX() - player.getPosX(), eyeHeight - player.getPosYEye(), this.getPosZ() - player.getPosZ());
                double EyeDeltaLength = EyeDelta.length();
                EyeDelta = EyeDelta.normalize();
                double EyeCheckFinal = PlayerLook.dotProduct(EyeDelta);

                //check is player is looking at leg
                Vector3d LegDelta = new Vector3d(this.getPosX() - player.getPosX(), this.getPosY()+0.3 - player.getPosYEye(), this.getPosZ() - player.getPosZ());
                double LegDeltaLength = LegDelta.length();
                LegDelta = LegDelta.normalize();
                double LegCheckFinal = PlayerLook.dotProduct(LegDelta);

                ItemStack heldstacks = player.getHeldItemMainhand();


                if(!(heldstacks.getItem() instanceof ItemBandage)) {
                    if (EyeCheckFinal > 1.0D - 0.025D / EyeDeltaLength && player.getHeldItemMainhand() == ItemStack.EMPTY && getDistanceSq(player) < 4.0F) {
                        this.beingpatted();
                        return ActionResultType.SUCCESS;
                    } else if (LegCheckFinal > 1.0D - 0.015D / LegDeltaLength) {
                        this.func_233687_w_(!this.isSitting());
                        return ActionResultType.SUCCESS;
                    }
                }
                else if(player.getHeldItemMainhand() != ItemStack.EMPTY) {
                    if (heldstacks.getItem() == registerItems.OATHRING.get()) {
                        if (this.getAffection() < 100 && !player.isCreative()) {
                            player.sendMessage(new TranslationTextComponent("entity.not_enough_affection"), this.getUniqueID());
                            return ActionResultType.FAIL;
                        } else {
                            if (!this.isOathed()) {
                                this.setOathed(true);
                                if (player.isCreative()) {
                                    this.setAffection(100);
                                } else {
                                    player.getHeldItem(hand).shrink(1);
                                }
                                return ActionResultType.CONSUME;
                            }
                        }
                    }
                    else if(heldstacks.getItem() instanceof ItemBandage){
                        if(this.getHealth()<this.getMaxHealth()){
                            if(this.ticksExisted %20 == 0) {
                                this.heal(1.0f);
                                heldstacks.damageItem(1, player, (playerEntity) -> playerEntity.sendBreakAnimation(player.getActiveHand()));
                                player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.0f, 1.0f);
                                this.healAnimationTime = 20;
                            }
                            return ActionResultType.SUCCESS;
                        }
                    }
                }
                return ActionResultType.FAIL;
            }
        }
        else{
            if(player.getHeldItemMainhand().getItem() == registerItems.Rainbow_Wisdom_Cube.get()&&!this.isTamed()){
                this.setTamedBy(player);
                player.getHeldItem(hand).shrink(1);
                return ActionResultType.CONSUME;
            }
        }
        return super.applyPlayerInteraction(player, vec, hand);
    }

    public boolean isNearHome(){
        return this.getBrain().hasMemory(MemoryModuleType.HOME) && this.getBrain().getMemory(MemoryModuleType.HOME).get().getDimension() == this.world.getDimensionKey() && this.getPosition().withinDistance(this.getBrain().getMemory(MemoryModuleType.HOME).get().getPos(), 64);
    }

    protected abstract void openGUI(PlayerEntity player);

    protected void beingpatted(){
        if(this.patAnimationTime == 0 || this.dataManager.get(MAXPATEFFECTCOUNT) == 0){
            this.dataManager.set(MAXPATEFFECTCOUNT, 5+this.rand.nextInt(3));
        }
        this.patAnimationTime = 20;
    }

    public boolean isGettingHealed(){
        return this.healAnimationTime>0;
    }

    public IItemHandlerModifiable getEquipment(){
        return this.EQUIPMENT;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        NetworkHooks.getEntitySpawningPacket(this);
        return super.createSpawnPacket();
    }

    public void SwitchFreeRoamingStatus() {
        this.setStayCenterPos(this.getPosition());
        this.setFreeRoaming(!this.isFreeRoaming());
    }

    public BlockPos getStayCenterPos() {
        return this.getDataManager().get(STAYPOINT);
    }

    public void setStayCenterPos(BlockPos stayCenterPos) {
        this.getDataManager().set(STAYPOINT,stayCenterPos);
    }
}
