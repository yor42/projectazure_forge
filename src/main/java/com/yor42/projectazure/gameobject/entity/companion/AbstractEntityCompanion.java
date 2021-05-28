package com.yor42.projectazure.gameobject.entity.companion;

import com.google.common.collect.ImmutableList;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.entity.CompanionSwimPathFinder;
import com.yor42.projectazure.gameobject.entity.ai.*;
import com.yor42.projectazure.gameobject.entity.companion.gunusers.EntityGunUserBase;
import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityKansenBase;
import com.yor42.projectazure.gameobject.items.ItemBandage;
import com.yor42.projectazure.gameobject.items.ItemCannonshell;
import com.yor42.projectazure.gameobject.items.ItemMagazine;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.setup.register.registerItems;
import javafx.scene.shape.MoveTo;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SCollectItemPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;
import software.bernie.shadowed.eliotlash.mclib.utils.MathUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;
import java.util.Map;

import static com.yor42.projectazure.libs.utils.MathUtil.getRand;

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

    protected final ItemStackHandler Inventory = new ItemStackHandler(12);
    public ItemStackHandler AmmoStorage = new ItemStackHandler(0){
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {

            AbstractEntityCompanion entity = AbstractEntityCompanion.this;

            if (entity instanceof EntityKansenBase){
                return stack.getItem() instanceof ItemCannonshell;
            }
            else if(entity instanceof EntityGunUserBase){
                return stack.getItem() instanceof ItemMagazine;
            }
            else
                return false;
        }
    };

    protected int level,  patAnimationTime, LimitBreakLv, patTimer;
    private final EntitySize originalsize;
    protected double affection, exp, morale;
    protected boolean isFreeRoaming, isSwimmingUp;
    protected boolean isMeleeing, isOpeningDoor, isstuck;
    protected int awakeningLevel;
    private final MovementController SwimController;
    private final MovementController MoveController;
    protected final SwimmerPathNavigator swimmingNav;
    private final GroundPathNavigator groundNav;
    private final FlyingPathNavigator airNav;
    private List<ExperienceOrbEntity> nearbyExpList;

    protected long lastSlept, lastWokenup;

    private int forcewakeupExpireTimer, forceWakeupCounter, expdelay;

    protected static final DataParameter<Boolean> SITTING = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> OPENINGDOOR = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> MELEEATTACKING = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> USINGBOW = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Integer> MAXPATEFFECTCOUNT = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.VARINT);
    protected static final DataParameter<Integer> PATEFFECTCOUNT = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.VARINT);
    protected static final DataParameter<Integer> PATCOOLDOWN = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.VARINT);
    protected static final DataParameter<Boolean> OATHED = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<BlockPos> STAYPOINT = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.BLOCK_POS);
    protected static final DataParameter<BlockPos> HOMEPOS = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.BLOCK_POS);
    protected static final DataParameter<Float> VALID_HOME_DISTANCE = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.FLOAT);
    protected static final DataParameter<Boolean> ISFORCEWOKENUP = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> ISUSINGGUN = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Integer> HEAL_TIMER = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.VARINT);
    protected static final DataParameter<Integer> RELOAD_TIMER_MAINHAND = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.VARINT);
    protected static final DataParameter<Integer> RELOAD_TIMER_OFFHAND = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.VARINT);

    public abstract enums.EntityType getEntityType();

    private static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.HOME, MemoryModuleType.MOBS, MemoryModuleType.VISIBLE_MOBS, MemoryModuleType.VISIBLE_VILLAGER_BABIES, MemoryModuleType.NEAREST_PLAYERS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER, MemoryModuleType.WALK_TARGET, MemoryModuleType.LOOK_TARGET, MemoryModuleType.PATH, MemoryModuleType.OPENED_DOORS, MemoryModuleType.NEAREST_BED, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.NEAREST_HOSTILE, MemoryModuleType.SECONDARY_JOB_SITE, MemoryModuleType.HIDING_PLACE, MemoryModuleType.HEARD_BELL_TIME, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.LAST_SLEPT, MemoryModuleType.LAST_WOKEN);

    protected AbstractEntityCompanion(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
        this.setAffection(40F);
        this.getAttribute(ForgeMod.SWIM_SPEED.get()).setBaseValue(1.0F);
        this.setFreeRoaming(false);
        this.swimmingNav = new SwimmerPathNavigator(this, worldIn);
        this.groundNav = new GroundPathNavigator(this, worldIn);
        this.airNav = new FlyingPathNavigator(this, worldIn);
        this.SwimController = new CompanionSwimPathFinder(this);
        this.MoveController = new MovementController(this);
        this.originalsize = type.getSize();;
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

    public boolean isPVPenabled(){
        return false;
    }

    protected int getAwakeningLevel(){
        return this.awakeningLevel;
    }

    public void setUsingGun(boolean val){
        this.getDataManager().set(ISUSINGGUN, val);
    }

    public boolean isUsingGun(){
        return this.getDataManager().get(ISUSINGGUN);
    }

    public ItemStack HasRightMagazine(enums.AmmoCalibur calibur){
        return ItemStack.EMPTY;
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putDouble("affection", this.affection);
        compound.putInt("patcooldown", this.dataManager.get(PATCOOLDOWN));
        compound.putBoolean("oathed", this.dataManager.get(OATHED));
        compound.putDouble("homeX", this.dataManager.get(HOMEPOS).getX());
        compound.putDouble("homeY", this.dataManager.get(HOMEPOS).getY());
        compound.putDouble("homeZ", this.dataManager.get(HOMEPOS).getZ());
        compound.putFloat("home_distance", this.dataManager.get(VALID_HOME_DISTANCE));
        compound.putDouble("stayX", this.dataManager.get(STAYPOINT).getX());
        compound.putDouble("stayY", this.dataManager.get(STAYPOINT).getY());
        compound.putDouble("stayZ", this.dataManager.get(STAYPOINT).getZ());
        compound.putBoolean("isforcewokenup", this.dataManager.get(ISFORCEWOKENUP));
        compound.putDouble("exp", this.exp);
        compound.putInt("level", this.level);
        compound.putInt("limitbreaklv", this.LimitBreakLv);
        compound.putInt("awaken", this.awakeningLevel);
        compound.putBoolean("freeroaming", this.isFreeRoaming());
        compound.putDouble("morale", this.morale);
        compound.putLong("lastslept", this.lastSlept);
        compound.putLong("lastwoken", this.lastWokenup);
        compound.put("inventory", this.getInventory().serializeNBT());
    }

    public void setHomepos(BlockPos pos, float validDistance){
        this.dataManager.set(HOMEPOS, pos);
        this.dataManager.set(VALID_HOME_DISTANCE, validDistance);
    }

    public BlockPos getHomePos() {
        return this.dataManager.get(HOMEPOS);
    }

    public float getHomeDistance(){
        return this.dataManager.get(VALID_HOME_DISTANCE);
    }

    public boolean isInHomeRange(BlockPos Startpos){
        if(this.getHomeDistance() == -1.0f || this.getHomePos() == BlockPos.ZERO){
            return false;
        }
        else{
            return Startpos.withinDistance(this.getHomePos(), this.getHomeDistance());
        }
    }

    @Override
    protected void collideWithEntity(Entity entityIn) {
        super.collideWithEntity(entityIn);
    }

    public boolean isInHomeRangefromCurrenPos(){
        return this.isInHomeRange(this.getPosition());
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.affection = compound.getFloat("affection");
        this.dataManager.set(PATCOOLDOWN, compound.getInt("patcooldown"));
        this.dataManager.set(OATHED, compound.getBoolean("oathed"));
        this.dataManager.set(HOMEPOS, new BlockPos(compound.getDouble("homeX"), compound.getDouble("homeY"), compound.getDouble("homeZ")));
        this.dataManager.set(STAYPOINT, new BlockPos(compound.getDouble("stayX"), compound.getDouble("stayY"), compound.getDouble("stayZ")));
        this.dataManager.set(VALID_HOME_DISTANCE, compound.getFloat("home_distance"));
        this.dataManager.set(ISFORCEWOKENUP, compound.getBoolean("isforcewokenup"));
        this.level = compound.getInt("level");
        this.exp = compound.getFloat("exp");
        this.LimitBreakLv = compound.getInt("limitbreaklv");
        this.awakeningLevel = compound.getInt("awaken");
        this.setFreeRoaming(compound.getBoolean("freeroaming"));
        this.morale = compound.getDouble("morale");
        this.getInventory().deserializeNBT(compound.getCompound("inventory"));

        this.lastSlept = compound.getLong("lastslept");
        this.lastWokenup = compound.getLong("lastwoken");
    }

    public ItemStackHandler getInventory(){
        return this.Inventory;
    };

    public int getFirstEmptyStack() {
        for(int i=0; i<this.getInventory().getSlots();i++){
            if(this.getInventory().getStackInSlot(i) == ItemStack.EMPTY){
                return i;
            }
        }
        return -1;
    }

    public boolean isSwimmingUp() {
        return this.isSwimmingUp;
    }

    public void setSwimmingUp(boolean swimmingUp) {
        this.isSwimmingUp = swimmingUp;
    }

    public void AttackUsingGun(LivingEntity target, ItemStack gun, Hand HandIn){
        if(this instanceof EntityGunUserBase && gun.getItem() instanceof ItemGunBase){
            AnimationController gunAnimation = GeckoLibUtil.getControllerForStack(((ItemGunBase) gun.getItem()).getFactory(), gun, ((ItemGunBase) gun.getItem()).getFactoryName());
            gunAnimation.setAnimation(new AnimationBuilder().addAnimation("animation.abydos550.fire", false));
            this.playSound(((ItemGunBase) gun.getItem()).getFireSound(), 1.0F, (getRand().nextFloat() - getRand().nextFloat()) * 0.2F + 1.0F);
            ((ItemGunBase) gun.getItem()).shootGunLivingEntity(gun, this.getEntityWorld(), this, false, HandIn, target);
            ((ItemGunBase) gun.getItem()).useAmmo(gun, (short) 1);
        }
    }

    public int storeItemStack(ItemStack itemStackIn)
    {
        for(int i = 0; i < 27; ++i)
        {
            if(this.canMergeStacks(this.getInventory().getStackInSlot(i).getStack(), itemStackIn, i))
            {
                return i;
            }
        }
        return -1;
    }

    private boolean canMergeStacks(ItemStack stack1, ItemStack stack2, int index)
    {
        return !stack1.isEmpty() && this.stackEqualExact(stack1, stack2) && stack1.isStackable() && stack1.getCount() < stack1.getMaxStackSize() && stack1.getCount() < this.getInventory().getSlotLimit(index);
    }

    private boolean stackEqualExact(ItemStack stack1, ItemStack stack2)
    {
        return stack1.getItem() == stack2.getItem() && ItemStack.areItemStackTagsEqual(stack1, stack2);
    }

    public boolean isFreeRoaming() {
        return this.isFreeRoaming;
    }

    public void setFreeRoaming(boolean freeRoaming) {
        this.isFreeRoaming = freeRoaming;
    }


    /*
    looks like this is for getting a child entity.
    we don't need these.
     */
    @Nullable
    @Override
    public AgeableEntity func_241840_a(ServerWorld p_241840_1_, AgeableEntity p_241840_2_) {
        return null;
    }

    /*
    No. just No.
     */
    @Override
    public boolean canMateWith(AnimalEntity otherAnimal) {
        return false;
    }

    /*
    determines if this entity can use ShipGirl Riggings.
    Off by default.
     */
    public boolean canUseRigging(){
        return false;
    }

    /*
    determines if this entity can use Gun.
    Off by default.
     */
    public boolean canUseGun(){
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
        animationData.addAnimationController(new AnimationController<>(this, "controller_lowerbody", 10, this::predicate_lowerbody));
        animationData.addAnimationController(new AnimationController<>(this, "controller_upperbody", 10, this::predicate_upperbody));
        animationData.addAnimationController(new AnimationController<>(this, "controller_head", 10, this::predicate_head));
    }

    protected abstract <P extends IAnimatable> PlayState predicate_upperbody(AnimationEvent<P> pAnimationEvent);

    protected abstract <P extends IAnimatable> PlayState predicate_head(AnimationEvent<P> pAnimationEvent);

    public boolean isMeleeing(){
        return this.dataManager.get(MELEEATTACKING);
    }

    protected abstract <E extends IAnimatable> PlayState predicate_lowerbody(AnimationEvent<E> event);

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
        this.dataManager.register(ISFORCEWOKENUP, false);
        this.dataManager.register(ISUSINGGUN, false);
        this.dataManager.register(HOMEPOS, BlockPos.ZERO);
        this.dataManager.register(VALID_HOME_DISTANCE, -1.0f);
        this.dataManager.register(HEAL_TIMER, 0);
        this.dataManager.register(RELOAD_TIMER_MAINHAND, 0);
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

        this.dataManager.set(SITTING, this.isSitting());

        if(this.collidedHorizontally && this.isInWater()) {
            Vector3d vec3d = this.getMotion();
            isstuck = true;
            this.setSwimmingUp(this.collidedHorizontally);
            this.setMotion(vec3d.x, vec3d.y + (double) (0.02F), vec3d.z);
        }else if(this.isstuck && !this.collidedHorizontally){
            this.setSwimmingUp(false);
            this.setSwimmingUp(false);
        }

        if(this.expdelay>0){
            this.expdelay--;
        }

        if(this.nearbyExpList == null || this.ticksExisted%5 == 0){
            this.nearbyExpList = this.getEntityWorld().getEntitiesWithinAABB(ExperienceOrbEntity.class, this.getBoundingBox().grow(2));
        }

        if(!this.nearbyExpList.isEmpty()) {
            for(ExperienceOrbEntity orbEntity: this.nearbyExpList){
                if(this.getDistance(orbEntity)>1.5) {
                    this.pickupExpOrb(orbEntity);
                }
            }
        }
        int healAnimationTime = this.dataManager.get(HEAL_TIMER);
        if(healAnimationTime>0){
            this.dataManager.set(HEAL_TIMER, --healAnimationTime);
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
                    this.addAffection(0.01);
                    this.world.addParticle(ParticleTypes.HEART, this.getPosXRandom(1.0D), this.getPosYRandom() + 0.5D, this.getPosZRandom(1.0D), d0, d1, d2);

                } else {
                    this.dataManager.set(PATEFFECTCOUNT, this.dataManager.get(MAXPATEFFECTCOUNT));
                    this.addAffection(-0.015);
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

        if(this.getDataManager().get(HEAL_TIMER)>0){
            this.getDataManager().set(HEAL_TIMER, this.getDataManager().get(HEAL_TIMER)-1);
        }

        if(this.isSleeping()){
            this.getNavigator().clearPath();
        }

        if (this.forcewakeupExpireTimer>0){
            this.forcewakeupExpireTimer--;
        }

        if(this.getDataManager().get(RELOAD_TIMER_MAINHAND)>0){
            this.getDataManager().set(RELOAD_TIMER_MAINHAND, this.getDataManager().get(RELOAD_TIMER_MAINHAND)-1);
            if(this.getDataManager().get(RELOAD_TIMER_MAINHAND) == 0){
                if(this.getHeldItemMainhand().getItem() instanceof ItemGunBase){
                    ((ItemGunBase) this.getHeldItemMainhand().getItem()).reloadAmmo(this.getHeldItemMainhand());
                }
                else if(this.getHeldItemOffhand().getItem() instanceof ItemGunBase && !((ItemGunBase) this.getHeldItemOffhand().getItem()).isTwoHanded()){
                    ((ItemGunBase) this.getHeldItemOffhand().getItem()).reloadAmmo(this.getHeldItemOffhand());
                }
            }
        }


        else if(this.forcewakeupExpireTimer==0 || !this.isSleeping()){
            this.forceWakeupCounter=0;
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

        if(this.isForceWaken() && this.getEntityWorld().isDaytime()){
            this.setForceWaken(false);
        }

        if(this.isBeingPatted() && this.ticksExisted%20 ==0){
            this.addMorale(0.02);
        }
        else if(this.isEntitySleeping() && this.ticksExisted%20 == 0){
            this.addMorale(0.015);
        }
        else if(!this.isFreeRoaming()&& this.ticksExisted%20 == 0){
            this.addMorale(-0.01);
        }

        if(!this.world.isRemote() && this.getEntityWorld().isDaytime() && this.isSleeping())
        {
            this.wakeUp();
        }

        if(!this.getEntityWorld().isRemote() && this.getEntityWorld().isNightTime() && this.isEntitySleeping() && this.isInHomeRangefromCurrenPos()){
            this.func_233687_w_(false);
        }

    }



    @Override
    protected void registerGoals() {
        //this.goalSelector.addGoal(1, new CompanionSwimGoal(this));
        this.goalSelector.addGoal(2, new CompanionSleepGoal(this));
        this.goalSelector.addGoal(3, new SitGoal(this));
        this.goalSelector.addGoal(6, new CompanionUseGunGoal(this, 40, 0.6));
        this.goalSelector.addGoal(8, new KansenRideBoatAlongPlayerGoal(this, 1.0));
        this.goalSelector.addGoal(9, new CompanionMeleeGoal(this, 1.0D, true));
        this.goalSelector.addGoal(10, new CompanionFollowOwnerGoal(this, 0.75D, 5.0F, 2.0F, false));
        this.goalSelector.addGoal(11, new KansenWorkGoal(this, 1.0D));
       this.goalSelector.addGoal(12, new CompanionOpenDoorGoal(this, true));
       this.goalSelector.addGoal(13, new CompanionFreeroamGoal(this, 60, true));
       this.goalSelector.addGoal(14, new CompanionPickupItemGoal(this));
        this.goalSelector.addGoal(15, new CompanionFindBedGoal(this));
        this.goalSelector.addGoal(16, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(17, new LookRandomlyGoal(this));
        //this.goalSelector.addGoal(9, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setCallsForHelp());
    }

    protected boolean canOpenDoor() {
        return true;
    }

    public boolean ShouldPlayReloadAnim(){
        return this.getDataManager().get(RELOAD_TIMER_MAINHAND)>0;
    }

    public void setReloadDelay(){
        this.getDataManager().set(RELOAD_TIMER_MAINHAND, this.Reload_Anim_Delay());
    }

    public int Reload_Anim_Delay(){
        return 0;
    }

    @Override
    public void startSleeping(BlockPos pos) {
        super.startSleeping(pos);
        this.setLastSlept(this.getEntityWorld().getGameTime());
    }

    public void setLastSlept(long lastSlept) {
        this.lastSlept = lastSlept;
    }

    public long getLastSlept() {
        return this.lastSlept;
    }

    public long getLastWokenup() {
        return this.lastWokenup;
    }

    public void setLastWokenup(long lastWokenup) {
        this.lastWokenup = lastWokenup;
    }

    public void setTamedBy(PlayerEntity player) {
        this.setTamed(true);
        this.setOwnerId(player.getUniqueID());
        //Triggering Tame Animal goal at the beginning of the world doesn't feel right.
    }
    @Override
    public boolean canBePushed() {
        return super.canBePushed() && !this.isSleeping();
    }

    @Override
    public boolean canBeCollidedWith() {
        return super.canBeCollidedWith()&&!this.isSleeping();
    }


    @Override
    public ActionResultType applyPlayerInteraction(PlayerEntity player, Vector3d vec, Hand hand) {
        if(this.isOwner(player) && !(player.getHeldItem(hand).getItem() instanceof ItemRiggingBase)){

            if(this.getRidingEntity() != null){
                this.stopRiding();
            }

            if(player.isSneaking() && !this.world.isRemote && !(player.getHeldItem(hand).getItem() instanceof ItemBandage)){
                    this.openGUI((ServerPlayerEntity) player);
                    Main.PROXY.setSharedMob(this);
                    return ActionResultType.SUCCESS;
            }
            else{

                ItemStack heldstacks = player.getHeldItemMainhand();

                if(heldstacks != ItemStack.EMPTY) {
                    if (heldstacks.getItem() == registerItems.OATHRING.get()) {
                        if (this.getAffection() < 100 && !player.isCreative()) {
                            player.sendMessage(new TranslationTextComponent("entity.not_enough_affection"), this.getUniqueID());
                            return ActionResultType.FAIL;
                        } else {
                            if (!this.isOathed()) {
                                if (player.isCreative()) {
                                    this.setAffection(100);
                                } else {
                                    player.getHeldItem(hand).shrink(1);
                                }
                                this.setOathed(true);
                                return ActionResultType.CONSUME;
                            }
                        }
                    }
                    else if(heldstacks.getItem() == registerItems.ENERGY_DRINK_DEBUG.get()){
                        this.setMorale(150);
                        if(!player.isCreative()){
                            heldstacks.shrink(1);
                        }
                    }
                    else if(heldstacks.getItem() instanceof ItemBandage){
                        if(this.getHealth()<this.getMaxHealth()){
                            if(this.ticksExisted %20 == 0) {
                                this.heal(1.0f);
                                if(!player.isCreative()) {
                                    if(heldstacks.attemptDamageItem(1, MathUtil.getRand(), (ServerPlayerEntity) player)){
                                        heldstacks.shrink(1);
                                    }
                                }
                                double d0 = this.rand.nextGaussian() * 0.02D;
                                double d1 = this.rand.nextGaussian() * 0.02D;
                                double d2 = this.rand.nextGaussian() * 0.02D;
                                player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.0f, 1.0f);
                                this.getDataManager().set(HEAL_TIMER, 50);
                                this.addAffection(0.2F);
                                this.world.addParticle(ParticleTypes.HEART, this.getPosXRandom(1.0D), this.getPosYRandom() + 0.5D, this.getPosZRandom(1.0D), d0, d1, d2);
                            }
                            return ActionResultType.SUCCESS;
                        }
                    }
                }else{

                    //check is player is looking at Head
                    Vector3d PlayerLook = player.getLook(1.0F).normalize();
                    float eyeHeight = (float) this.getPosYEye();


                    Vector3d EyeDelta = new Vector3d(this.getPosX() - player.getPosX(), eyeHeight - player.getPosYEye(), this.getPosZ() - player.getPosZ());
                    double EyeDeltaLength = EyeDelta.length();
                    EyeDelta = EyeDelta.normalize();
                    double EyeCheckFinal = PlayerLook.dotProduct(EyeDelta);

                    //check is player is looking at leg
                    Vector3d LegDelta = new Vector3d(this.getPosX() - player.getPosX(), this.getPosY()+0.3 - player.getPosYEye(), this.getPosZ() - player.getPosZ());
                    double LegDeltaLength = LegDelta.length();
                    LegDelta = LegDelta.normalize();
                    double LegCheckFinal = PlayerLook.dotProduct(LegDelta);

                    if(this.isSleeping()){
                        if(this.forceWakeupCounter>4){
                            this.forceWakeup();
                        }
                        else{
                            this.forceWakeupCounter++;
                            this.forcewakeupExpireTimer = 20;
                        }
                        return ActionResultType.SUCCESS;
                    }
                    else if (EyeCheckFinal > 1.0D - 0.025D / EyeDeltaLength && player.getHeldItemMainhand() == ItemStack.EMPTY && getDistanceSq(player) < 4.0F) {
                        this.beingpatted();
                        return ActionResultType.SUCCESS;
                    } else if (LegCheckFinal > 1.0D - 0.015D / LegDeltaLength) {
                        this.func_233687_w_(!this.isSitting());
                        return ActionResultType.SUCCESS;
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

    @Override
    public void tick() {
        super.tick();
        this.setPathPriority(PathNodeType.WATER, this.getOwner() != null && this.getOwner().isInWater()? 0.0F:-1.0F);
        if(this.getHomePosition()!=BlockPos.ZERO&&this.ticksExisted%20==0){
            BlockState blockstate = this.world.getBlockState(this.getHomePos());
            if(!blockstate.isBed(this.getEntityWorld(),this.getHomePos(), this)) {
                this.setHomepos(BlockPos.ZERO, -1);
            };
        }
    }

    public void updateSwimming() {
        if (!this.world.isRemote &&!this.canUseRigging()&& this.ticksExisted%10 == 0) {
            double waterheight = this.func_233571_b_(FluidTags.WATER);
            if (this.isServerWorld() && this.isInWater() && (!this.isOnGround() || this.eyesInWater) && waterheight > 0.9) {
                this.navigator = this.swimmingNav;
                this.moveController = this.SwimController;
                this.setSwimming(true);
            } else {
                this.navigator = this.groundNav;
                this.moveController = this.MoveController;
                this.setSwimming(false);
            }
        }

    }

    public void pickupExpOrb(ExperienceOrbEntity orbEntity){
        if(!this.world.isRemote){
            if(orbEntity.delayBeforeCanPickup <= 0 && this.expdelay <= 0){
                this.onItemPickup(orbEntity, 1);
                this.expdelay = 2;
                Map.Entry<EquipmentSlotType, ItemStack> entry = EnchantmentHelper.getRandomEquippedWithEnchantment(Enchantments.MENDING, this, ItemStack::isDamaged);
                if (entry != null) {
                    ItemStack itemstack = entry.getValue();
                    if (!itemstack.isEmpty() && itemstack.isDamaged()) {
                        int i = Math.min((int)(orbEntity.xpValue * itemstack.getXpRepairRatio()), itemstack.getDamage());
                        orbEntity.xpValue -= i/2;
                        itemstack.setDamage(itemstack.getDamage() - i);
                    }
                }
                if (orbEntity.xpValue > 0) {
                    this.addExp(orbEntity.xpValue);
                }
                orbEntity.remove();
            }
        }
    }

    protected abstract void openGUI(ServerPlayerEntity player);

    protected void beingpatted(){
        if(this.patAnimationTime == 0 || this.dataManager.get(MAXPATEFFECTCOUNT) == 0){
            this.dataManager.set(MAXPATEFFECTCOUNT, 5+this.rand.nextInt(3));
        }
        this.patAnimationTime = 20;
    }

    public boolean isGettingHealed(){
        return this.dataManager.get(HEAL_TIMER)>0;
    }

    public IItemHandlerModifiable getEquipment(){
        return this.EQUIPMENT;
    }

    public ItemStackHandler getAmmoStorage() {
        return this.AmmoStorage;
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

    public void setForceWaken(boolean value){
        this.getDataManager().set(ISFORCEWOKENUP, value);
    }

    public boolean isForceWaken(){
        return this.getDataManager().get(ISFORCEWOKENUP);
    }

    public void forceWakeup(){
        if(this.isSleeping()) {
            this.wakeUp();
            this.setForceWaken(true);
        }

    }

    @Override
    public void wakeUp() {
        super.wakeUp();
        this.setLastWokenup(this.getEntityWorld().getDayTime());
    }

    @SubscribeEvent
    public void OnTimeSkip(PlayerWakeUpEvent event){
        this.setLastWokenup(this.getEntityWorld().getDayTime());

        this.CalculateMoraleBasedonTime(this.getLastSlept(), this.getLastWokenup());
    }

    private void CalculateMoraleBasedonTime(long lastSlept, long lastWokenup) {

        long deltaTime;
        if(lastWokenup<=lastSlept){
            deltaTime = 24000-lastSlept-lastWokenup;
        }
        else{
            deltaTime = lastSlept-lastWokenup;
        }

        this.addAffection(0.02*((float)deltaTime/300));
        this.addMorale(((float)deltaTime/1000)*30);
    }

    public double getMorale() {
        return this.morale;
    }

    public void setMorale(double morale) {
        this.morale = morale;
    }

    public void addMorale(double value){
        double prevmorale = this.getMorale();


        this.setMorale(MathUtils.clamp(prevmorale+value, 0, 150));
    }

    public BlockPos getStayCenterPos() {
        return this.getDataManager().get(STAYPOINT);
    }

    public void setStayCenterPos(BlockPos stayCenterPos) {
        this.getDataManager().set(STAYPOINT,stayCenterPos);
    }

    public void PickUpItem(ItemEntity target) {
        this.onItemPickup(target, target.getItem().getCount());
        ItemStack stack = target.getItem();

        this.triggerItemPickupTrigger(target);
        this.onItemPickup(target, stack.getCount());
        ItemStack itemstack1 = stack.copy();
        for(int i = 0; i<this.getInventory().getSlots(); i++){
            itemstack1 = this.getInventory().insertItem(i, stack, false);
            if(itemstack1.isEmpty()){
                target.remove();
                break;
            }

        }
        if(!itemstack1.isEmpty()){
            target.setItem(itemstack1);
        }
    }

    protected float getSitHeight(){
        return 1F;
    }

    @Override
    public EntitySize getSize(Pose poseIn) {

        if(this.dataManager.get(SITTING)){
            return new EntitySize(this.getWidth(), this.getSitHeight(), false);
        }

        return super.getSize(poseIn);
    }

    public void func_233687_w_(boolean p_233687_1_) {
        super.func_233687_w_(p_233687_1_);
        this.recalculateSize();
    }

}
