package com.yor42.projectazure.gameobject.entity.companion;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.entity.CompanionSwimPathFinder;
import com.yor42.projectazure.gameobject.entity.CompanionSwimPathNavigator;
import com.yor42.projectazure.gameobject.entity.ai.goals.*;
import com.yor42.projectazure.gameobject.items.ItemBandage;
import com.yor42.projectazure.gameobject.items.ItemCannonshell;
import com.yor42.projectazure.gameobject.items.ItemMagazine;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
import com.yor42.projectazure.intermod.ModCompatibilities;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.network.packets.ChangeEntityBehaviorPacket;
import com.yor42.projectazure.setup.register.registerItems;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.PushReaction;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;
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
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.*;

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

            return stack.getItem() instanceof ItemCannonshell || stack.getItem() instanceof ItemMagazine;
        }
    };

    protected int patAnimationTime, patTimer;
    protected boolean isSwimmingUp;
    protected boolean isMeleeing;
    protected boolean isOpeningDoor;
    public boolean isMovingtoRecruitStation = false;
    protected int awakeningLevel;
    private final MovementController SwimController;
    private final MovementController MoveController;
    protected final SwimmerPathNavigator swimmingNav;
    private final GroundPathNavigator groundNav;
    protected CompanionFoodStats foodStats = new CompanionFoodStats();
    private List<ExperienceOrbEntity> nearbyExpList;

    protected long lastSlept, lastWokenup;
    private int forcewakeupExpireTimer, forceWakeupCounter, expdelay;

    protected static final DataParameter<Float> MORALE = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.FLOAT);
    protected static final DataParameter<Float> EXP = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.FLOAT);
    protected static final DataParameter<Integer> LIMITBREAKLEVEL = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.VARINT);
    protected static final DataParameter<Integer> LEVEL = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.VARINT);
    protected static final DataParameter<Float> AFFECTION = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.FLOAT);
    protected static final DataParameter<Boolean> SITTING = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> OPENINGDOOR = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> MELEEATTACKING = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> USINGBOW = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Integer> MAXPATEFFECTCOUNT = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.VARINT);
    protected static final DataParameter<Integer> PATEFFECTCOUNT = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.VARINT);
    protected static final DataParameter<Integer> PATCOOLDOWN = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.VARINT);
    protected static final DataParameter<Boolean> OATHED = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> PICKUP_ITEM = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> ISFREEROAMING = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Optional<BlockPos>> STAYPOINT = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.OPTIONAL_BLOCK_POS);
    protected static final DataParameter<Optional<BlockPos>> RecruitStationPos = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.OPTIONAL_BLOCK_POS);
    protected static final DataParameter<Optional<BlockPos>> HOMEPOS = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.OPTIONAL_BLOCK_POS);
    protected static final DataParameter<Float> VALID_HOME_DISTANCE = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.FLOAT);
    protected static final DataParameter<Boolean> ISFORCEWOKENUP = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> ISUSINGGUN = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Integer> HEAL_TIMER = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.VARINT);
    protected static final DataParameter<Integer> RELOAD_TIMER_MAINHAND = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.VARINT);
    protected static final DataParameter<Integer> RELOAD_TIMER_OFFHAND = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.VARINT);

    public abstract enums.EntityType getEntityType();
    protected AbstractEntityCompanion(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
        this.setAffection(40F);
        this.swimmingNav = new CompanionSwimPathNavigator(this, worldIn);
        this.groundNav = new GroundPathNavigator(this, worldIn);
        this.SwimController = new CompanionSwimPathFinder(this);
        this.MoveController = new MovementController(this);
    }

    public void setOathed(boolean bool){
        this.dataManager.set(OATHED, bool);
    }

    public boolean isOathed(){
        return this.dataManager.get(OATHED);
    }

    public int getLevel() {
        return this.getDataManager().get(LEVEL);
    }

    public void setLevel(int level) {
        this.getDataManager().set(LEVEL, level);
    }

    public void addLevel(int deltaLevel){
        if(this.getLevel()+deltaLevel > this.getMaxLevel()){
            return;
        }
        this.setLevel(this.getLevel() + deltaLevel);
    }

    public boolean isPVPenabled(){
        return PAConfig.CONFIG.EnablePVP.get();
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

    public ItemStack getMagazine(enums.AmmoCalibur calibur){
        for(int i=0; i<this.getAmmoStorage().getSlots();i++){
            if(this.getAmmoStorage().getStackInSlot(i).getItem() instanceof ItemMagazine && ((ItemMagazine) this.getAmmoStorage().getStackInSlot(i).getItem()).getCalibur() == calibur){
                return this.getAmmoStorage().getStackInSlot(i);
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void writeAdditional(@Nonnull CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putFloat("affection", this.dataManager.get(AFFECTION));
        compound.putInt("patcooldown", this.dataManager.get(PATCOOLDOWN));
        compound.putBoolean("oathed", this.dataManager.get(OATHED));
        compound.putBoolean("pickupitem", this.dataManager.get(PICKUP_ITEM));
        compound.putFloat("home_distance", this.dataManager.get(VALID_HOME_DISTANCE));
        compound.putBoolean("issitting", this.dataManager.get(SITTING));
        if(this.dataManager.get(STAYPOINT).isPresent()) {
            compound.putDouble("stayX", this.dataManager.get(STAYPOINT).get().getX());
            compound.putDouble("stayY", this.dataManager.get(STAYPOINT).get().getY());
            compound.putDouble("stayZ", this.dataManager.get(STAYPOINT).get().getZ());
        }
        if(this.dataManager.get(RecruitStationPos).isPresent()) {
            compound.putDouble("RecruitStationPosX", this.dataManager.get(RecruitStationPos).get().getX());
            compound.putDouble("RecruitStationPosY", this.dataManager.get(RecruitStationPos).get().getY());
            compound.putDouble("RecruitStationPosZ", this.dataManager.get(RecruitStationPos).get().getZ());
        }
        if(this.dataManager.get(HOMEPOS).isPresent()) {
            compound.putDouble("HomePosX", this.dataManager.get(HOMEPOS).get().getX());
            compound.putDouble("HomePosY", this.dataManager.get(HOMEPOS).get().getY());
            compound.putDouble("HomePosZ", this.dataManager.get(HOMEPOS).get().getZ());
        }
        compound.putBoolean("isforcewokenup", this.dataManager.get(ISFORCEWOKENUP));
        compound.putDouble("exp", this.dataManager.get(EXP));
        compound.putInt("level", this.getDataManager().get(LEVEL));
        compound.putInt("limitbreaklv", this.dataManager.get(LIMITBREAKLEVEL));
        compound.putInt("awaken", this.awakeningLevel);
        compound.putBoolean("isSitting", this.isSitting());
        compound.putBoolean("freeroaming", this.isFreeRoaming());
        compound.putDouble("morale", this.dataManager.get(MORALE));
        compound.putLong("lastslept", this.lastSlept);
        compound.putLong("lastwoken", this.lastWokenup);
        compound.putBoolean("isMovingtoRecruitStation", this.isMovingtoRecruitStation);
        compound.put("inventory", this.getInventory().serializeNBT());
        compound.put("ammostorage", this.getAmmoStorage().serializeNBT());
        this.foodStats.write(compound);
    }

    public void setHomeposAndDistance(BlockPos pos, float validDistance){
        this.setHOMEPOS(pos);
        this.dataManager.set(VALID_HOME_DISTANCE, validDistance);
    }

    public void clearHomePos(){
        this.getDataManager().set(HOMEPOS, Optional.empty());
        this.dataManager.set(VALID_HOME_DISTANCE, -1.0f);
    }

    public Optional<BlockPos> getRecruitStationPos() {
        return this.getDataManager().get(RecruitStationPos);
    }

    public void setRecruitStationPos(BlockPos pos){
        this.getDataManager().set(RecruitStationPos, Optional.of(pos));
    }

    public float getHomeDistance(){
        return this.dataManager.get(VALID_HOME_DISTANCE);
    }

    public boolean isInHomeRange(BlockPos Startpos){
        if(this.getHomeDistance() == -1.0f || !this.getHOMEPOS().isPresent()){
            return false;
        }
        else{
            return Startpos.withinDistance(this.getHOMEPOS().get(), this.getHomeDistance());
        }
    }

    public Optional<BlockPos> getHOMEPOS(){
        return this.getDataManager().get(HOMEPOS);
    }

    public void setHOMEPOS(BlockPos pos){
        this.getDataManager().set(HOMEPOS, Optional.of(pos));
    }

    @Override
    protected void collideWithEntity(@Nonnull Entity entityIn) {
        super.collideWithEntity(entityIn);
    }

    public boolean isInHomeRangefromCurrenPos(){
        return this.isInHomeRange(this.getPosition());
    }

    public void readAdditional(@Nonnull CompoundNBT compound) {
        super.readAdditional(compound);
        this.dataManager.set(AFFECTION, compound.getFloat("affection"));
        this.dataManager.set(PATCOOLDOWN, compound.getInt("patcooldown"));
        this.dataManager.set(OATHED, compound.getBoolean("oathed"));
        this.dataManager.set(PICKUP_ITEM, compound.getBoolean("pickupitem"));
        this.func_233687_w_(compound.getBoolean("isSitting"));
        boolean hasStayPos = compound.contains("stayX") && compound.contains("stayY") && compound.contains("stayZ");
        if(hasStayPos) {
            this.dataManager.set(RecruitStationPos, Optional.of(new BlockPos(compound.getDouble("stayX"), compound.getDouble("stayY"), compound.getDouble("stayZ"))));
        }
        this.dataManager.set(VALID_HOME_DISTANCE, compound.getFloat("home_distance"));
        boolean hasRecruitBeaconPos = compound.contains("RecruitStationPosX") && compound.contains("RecruitStationPosY") && compound.contains("RecruitStationPosZ");
        if(hasRecruitBeaconPos) {
            this.dataManager.set(RecruitStationPos, Optional.of(new BlockPos(compound.getDouble("RecruitStationPosX"), compound.getDouble("RecruitStationPosY"), compound.getDouble("RecruitStationPosZ"))));
        }

        boolean hasHomePos = compound.contains("HomePosX") && compound.contains("HomePosY") && compound.contains("HomePosZ");
        if(hasHomePos) {
            this.dataManager.set(HOMEPOS, Optional.of(new BlockPos(compound.getDouble("HomePosX"), compound.getDouble("HomePosY"), compound.getDouble("HomePosZ"))));
        }
        this.dataManager.set(ISFORCEWOKENUP, compound.getBoolean("isforcewokenup"));
        this.getDataManager().set(LEVEL, compound.getInt("level"));
        this.dataManager.set(EXP, compound.getFloat("exp"));
        this.dataManager.set(MORALE, compound.getFloat("morale"));
        this.dataManager.set(SITTING, compound.getBoolean("issitting"));
        this.dataManager.set(LIMITBREAKLEVEL, compound.getInt("limitbreaklv"));
        this.awakeningLevel = compound.getInt("awaken");
        this.isMovingtoRecruitStation = compound.getBoolean("isMovingtoRecruitStation");
        this.setFreeRoaming(compound.getBoolean("freeroaming"));
        this.getInventory().deserializeNBT(compound.getCompound("inventory"));
        this.lastSlept = compound.getLong("lastslept");
        this.lastWokenup = compound.getLong("lastwoken");
        this.getAmmoStorage().deserializeNBT(compound.getCompound("ammostorage"));
        this.getFoodStats().read(compound);
    }

    public ItemStackHandler getInventory(){
        return this.Inventory;
    }

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
        if(gun.getItem() instanceof ItemGunBase){
            AnimationController<?> gunAnimation = GeckoLibUtil.getControllerForStack(((ItemGunBase) gun.getItem()).getFactory(), gun, ((ItemGunBase) gun.getItem()).getFactoryName());
            gunAnimation.setAnimation(new AnimationBuilder().addAnimation("fire", false));
            ((ItemGunBase) gun.getItem()).shootGunCompanion(gun, this.getEntityWorld(), this, false, HandIn, target);
            useAmmo(gun);
        }
    }

    public int storeItemStack(ItemStack itemStackIn)
    {
        for(int i = 0; i < this.getInventory().getSlots(); ++i)
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
        return this.dataManager.get(ISFREEROAMING);
    }

    public void setFreeRoaming(boolean value) {
        this.dataManager.set(ISFREEROAMING, value);
        if(value) {
            this.setStayCenterPos(this.getPosition());
        }
        else{
            this.clearStayCenterPos();
        }
    }


    /*
    looks like this is for getting a child entity.
    we don't need these.
     */
    @Nullable
    @Override
    public AgeableEntity func_241840_a(@Nonnull ServerWorld p_241840_1_, @Nonnull AgeableEntity p_241840_2_) {
        return null;
    }

    /*
    No. just No.
     */
    @Override
    public boolean canMateWith(@Nonnull AnimalEntity otherAnimal) {
        return false;
    }

    /*
    determines if this entity can use ShipGirl Riggings.
    Off by default.
     */
    public boolean canUseRigging(){
        return false;
    }

    public boolean canUseCannonOrTorpedo(){
        return false;
    }

    public boolean shouldUseGun() {
        ItemStack gunstack = this.getGunStack();
        if (gunstack.getItem() instanceof ItemGunBase) {
            ItemGunBase gunitem = (ItemGunBase) gunstack.getItem();
            return (!this.canUseCannonOrTorpedo() || !this.isSailing()) && this.getGunStack() != ItemStack.EMPTY && (this.HasRightMagazine(gunitem.getCalibur()) || getRemainingAmmo(gunstack)>0);
        }
        return false;
    }

    public boolean HasRightMagazine(enums.AmmoCalibur calibur){
        return this.getMagazine(calibur).getItem() instanceof ItemMagazine;
    }

    public boolean isSailing(){
        return false;
    }

    public boolean isPushedByWater() {
        return !this.isSwimming();
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
        animationData.addAnimationController(new AnimationController<>(this, "controller_lowerbody", 1, this::predicate_lowerbody));
        animationData.addAnimationController(new AnimationController<>(this, "controller_upperbody", 1, this::predicate_upperbody));
        animationData.addAnimationController(new AnimationController<>(this, "controller_head", 1, this::predicate_head));
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
        this.dataManager.register(EXP, 0.0F);
        this.dataManager.register(LIMITBREAKLEVEL, 0);
        this.dataManager.register(MORALE, 0.0F);
        this.dataManager.register(LEVEL, 0);
        this.dataManager.register(AFFECTION, 0.0F);
        this.dataManager.register(SITTING, false);
        this.dataManager.register(OPENINGDOOR, false);
        this.dataManager.register(MELEEATTACKING, false);
        this.dataManager.register(MAXPATEFFECTCOUNT, 0);
        this.dataManager.register(PATEFFECTCOUNT, 0);
        this.dataManager.register(PATCOOLDOWN, 0);
        this.dataManager.register(OATHED, false);
        this.dataManager.register(USINGBOW, false);
        this.dataManager.register(STAYPOINT, Optional.empty());
        this.dataManager.register(RecruitStationPos, Optional.empty());
        this.dataManager.register(HOMEPOS, Optional.empty());
        this.dataManager.register(ISFORCEWOKENUP, false);
        this.dataManager.register(ISUSINGGUN, false);
        this.dataManager.register(ISFREEROAMING, false);
        this.dataManager.register(VALID_HOME_DISTANCE, -1.0f);
        this.dataManager.register(HEAL_TIMER, 0);
        this.dataManager.register(RELOAD_TIMER_MAINHAND, 0);
        this.dataManager.register(RELOAD_TIMER_OFFHAND, 0);
        this.dataManager.register(PICKUP_ITEM, false);
    }

    public void setOpeningdoor(boolean openingdoor){
        this.isOpeningDoor = openingdoor;
        this.dataManager.set(OPENINGDOOR, this.isOpeningDoor);
    }



    public boolean shouldPickupItem(){
        return this.dataManager.get(PICKUP_ITEM);
    }

    public void setPickupItem(boolean value){
        this.dataManager.set(PICKUP_ITEM, value);
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

    public void setMovingtoRecruitStation(BlockPos pos){
        this.isMovingtoRecruitStation=true;
        this.setRecruitStationPos(pos);
    }

    public void stopMovingtoRecruitStation(){
        this.getNavigator().clearPath();
        this.isMovingtoRecruitStation = false;
        this.dataManager.set(RecruitStationPos, Optional.empty());
    }

    public boolean canEat(boolean ignoreHunger) {
        return ignoreHunger || this.foodStats.needFood();
    }

    public void addExhaustion(float exhaustion) {
        if (!this.world.isRemote) {
            this.foodStats.addExhaustion(exhaustion);
        }
    }

    public ItemStack onFoodEaten(World p_213357_1_, ItemStack p_213357_2_) {
        this.getFoodStats().consume(p_213357_2_.getItem(), p_213357_2_);
        p_213357_1_.playSound(null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.PLAYERS, 0.5F, p_213357_1_.rand.nextFloat() * 0.1F + 0.9F);
        return super.onFoodEaten(p_213357_1_, p_213357_2_);
    }

    public CompanionFoodStats getFoodStats() {
        return this.foodStats;
    }

    public boolean shouldHeal() {
        return this.getHealth() > 0.0F && this.getHealth() < this.getMaxHealth();
    }

    public boolean isBeingPatted() {
        return this.patAnimationTime !=0;
    }

    public double getMaxExp(){
        return 10+(5*this.getLevel());
    }

    public int getLimitBreakLv() {
        return this.dataManager.get(LIMITBREAKLEVEL);
    }

    public void setLimitBreakLv(int value) {
        this.dataManager.set(LIMITBREAKLEVEL, value);
    }

    public void addLimitBreak(int delta){
        setLimitBreakLv(this.getLimitBreakLv() + delta);
    }

    public void doLimitBreak(){
        this.addLimitBreak(1);
    }

    public double getAffection() {
        return this.getDataManager().get(AFFECTION);
    }

    public void setAffection(float affection){
        this.getDataManager().set(AFFECTION, affection);
    }

    public double getmaxAffection(){
        return this.isOathed()? 200:100;
    }

    public void addAffection(double Delta){
        this.setAffection((float) Math.min(this.getAffection() + Delta, this.getmaxAffection()));
    }

    public void MaxFillHunger(){
        this.getFoodStats().addStats(20, 20);
    }

    public void setExp(double exp) {
        this.dataManager.set(EXP, (float) exp);
    }

    public double getExp() {
        return this.dataManager.get(EXP);
    }

    public int getMaxLevel(){
        switch (this.getLimitBreakLv()) {
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

    public void addExp(double deltaExp){
        if(this.getExp()+deltaExp >= this.getMaxExp()) {
            this.setExp((float) (this.getExp()+deltaExp));
            this.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
            while(this.getExp()>this.getMaxExp() && this.getLevel() <= this.getMaxLevel()){
                this.addLevel(1);
                this.setExp(this.getExp()-this.getMaxExp());
            }
        }
        this.setExp(this.getExp()+deltaExp);
        this.getExp();
    }

    @Override
    public boolean canFallInLove() {
        return false;
    }

    @Override
    public boolean isInLove() {
        return false;
    }

    private Optional<BlockPos> findHomePosition(ServerWorld world, AbstractEntityCompanion entity) {
        return world.getPointOfInterestManager().take(PointOfInterestType.HOME.getPredicate(), (pos) -> this.canReachHomePosition(entity, pos), entity.getPosition(), 20);
    }

    private boolean canReachHomePosition(AbstractEntityCompanion entity, BlockPos pos) {
        Path path = entity.getNavigator().getPathToPos(pos, PointOfInterestType.HOME.getValidRange());
        return path != null && path.reachesTarget();
    }


    @Override
    public void livingTick() {
        super.livingTick();
        if (this.world.getDifficulty() == Difficulty.PEACEFUL && this.world.getGameRules().getBoolean(GameRules.NATURAL_REGENERATION)) {
            if (this.getHealth() < this.getMaxHealth() && this.ticksExisted % 20 == 0) {
                this.heal(1.0F);
            }

            if (this.foodStats.needFood() && this.ticksExisted % 10 == 0) {
                this.foodStats.setFoodLevel(this.foodStats.getFoodLevel() + 1);
            }
        }

        if(!this.getEntityWorld().isRemote() && this.ticksExisted % 200 == 0&& !this.getHOMEPOS().isPresent()){
            Optional<BlockPos> optional = this.findHomePosition((ServerWorld) this.getEntityWorld(), this);
            optional.ifPresent(blockPos -> this.setHomeposAndDistance(blockPos, 20));
        }

        this.updateArmSwingProgress();

        if(this.getLastAttackedEntityTime()>200 && this.getGunAmmoCount() <= 0){
            this.setReloadDelay();
        }

        if(this.getDataManager().get(SITTING) != this.isSitting()) {
            this.func_233687_w_(this.getDataManager().get(SITTING));
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
                    this.addAffection(-0.008);
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
        int mainhandDelay = this.getDataManager().get(RELOAD_TIMER_MAINHAND);
        if(mainhandDelay>0){
            this.getDataManager().set(RELOAD_TIMER_MAINHAND, mainhandDelay - 1);
            if(this.getDataManager().get(RELOAD_TIMER_MAINHAND) == 0){
                this.reloadAmmo();
            }
        }

        if(this.ticksExisted%10==0){
            this.setSneaking((this.getOwner()!= null && this.getOwner().isSneaking()) || this.getEntityWorld().getBlockState(this.getPosition().down()).getBlock() == Blocks.MAGMA_BLOCK);
        }

        else if(this.forcewakeupExpireTimer==0 || !this.isSleeping()){
            this.forceWakeupCounter=0;
        }

        if(this.getAffection() <0)
            this.getDataManager().set(AFFECTION, 0F);
        else if(this.getAffection()>100){
            if(!this.isOathed())
                this.getDataManager().set(AFFECTION, 100F);
            else if(this.getAffection() > 200){
                this.getDataManager().set(AFFECTION, 200F);
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
        else if(this.isSitting() && this.ticksExisted%20 == 0){
            this.addMorale(0.015);
        }
        else if(!this.isFreeRoaming()&& this.ticksExisted%20 == 0){
            this.addMorale(-0.01);
        }
        if(!this.world.isRemote() && this.getEntityWorld().isDaytime()) {

            if (this.isSleeping()) {
                this.wakeUp();
            }
        }



        if(!this.getEntityWorld().isRemote() && (this.getEntityWorld().isNightTime() && this.isSitting() && this.isInHomeRangefromCurrenPos())){
            this.func_233687_w_(false);
        }

    }

    @Override
    public float getBlockPathWeight(@Nonnull BlockPos pos, IWorldReader worldIn) {
        if(!worldIn.isRemote() && ModCompatibilities.isSunlightDangerous((ServerWorld) worldIn)){
            return 0.0F-worldIn.getLightFor(LightType.SKY, pos);
        }
        else {
            return super.getBlockPathWeight(pos, worldIn);
        }
    }

    public void reloadAmmo(){
        ItemGunBase gun = (ItemGunBase) this.getGunStack().getItem();
        ItemStack MagStack = this.getMagazine(((ItemGunBase) this.getGunStack().getItem()).getCalibur());
        int i;
        if (gun.getRoundsPerReload() > 0) {
            i = Math.min(gun.getRoundsPerReload(), getRemainingAmmo(MagStack));
        } else {
            i = Math.min(gun.getMaxAmmo(), getRemainingAmmo(MagStack));
        }
        addAmmo(this.getGunStack(), i);
        MagStack.shrink(1);
        ItemStack EmptyMag = new ItemStack(((ItemGunBase) this.getGunStack().getItem()).getMagItem());
        emptyAmmo(EmptyMag);

        //Return Empty Magazine for Reloading
        if(!this.addStackToInventory(EmptyMag)){
            if(!this.addStackToAmmoStorage(EmptyMag)){
                ItemEntity itemEntity = new ItemEntity(this.getEntityWorld(), this.getPosX(), this.getPosY(), this.getPosZ(), EmptyMag);
                this.getEntityWorld().addEntity(itemEntity);
            }
        }
    }

    public short getGunAmmoCount(){
        return getGunAmmo(this.getGunStack());
    }

    public static short getGunAmmo(ItemStack stack){
        if(stack.getItem() instanceof ItemGunBase) {
            CompoundNBT compound = stack.getOrCreateTag();
            return compound.getShort("ammo");
        }
        else return 0;
    }

    public ItemStack getGunStack(){
        if(this.getHeldItem(this.getValidGunHand()).getItem() instanceof ItemGunBase){
            return this.getHeldItem(this.getValidGunHand());
        }
        return ItemStack.EMPTY;
    }

    private Hand getValidGunHand(){
        if(this.getHeldItemMainhand().getItem() instanceof ItemGunBase){
            return Hand.MAIN_HAND;
        }
        else
            return Hand.OFF_HAND;
    }

    public enums.GunClass getGunSpecialty(){
        return enums.GunClass.NONE;
    }

    public void ShootArrow(LivingEntity target, float distanceFactor) {
        ItemStack itemstack = this.findArrow();
        AbstractArrowEntity abstractarrowentity = this.fireArrow(itemstack, distanceFactor, this.getItemStackFromSlot(EquipmentSlotType.MAINHAND));
        if (this.getHeldItemMainhand().getItem() instanceof net.minecraft.item.BowItem)
            abstractarrowentity = ((net.minecraft.item.BowItem)this.getHeldItemMainhand().getItem()).customArrow(abstractarrowentity);
        double d0 = target.getPosX() - this.getPosX();
        double d1 = target.getPosYHeight(0.3333333333333333D) - abstractarrowentity.getPosY();
        double d2 = target.getPosZ() - this.getPosZ();
        double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
        abstractarrowentity.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, 3);
        this.playSound(SoundEvents.ENTITY_ARROW_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
        this.world.addEntity(abstractarrowentity);
    }

    protected AbstractArrowEntity fireArrow(ItemStack arrowStack, float distanceFactor, ItemStack ItemInHand) {
        return ProjectileHelper.fireArrow(this, arrowStack, distanceFactor);
    }

    protected ItemStack findArrow() {
        for (int i = 0; i < this.getInventory().getSlots(); i++) {
            if (this.getInventory().getStackInSlot(i).getItem() instanceof ArrowItem) {
                return this.getInventory().getStackInSlot(i);
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new CompanionMoveToRecruitStationGoal(this));
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
        this.goalSelector.addGoal(15, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(16, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(17, new CompanionPlaceTorchGoal(this));
        //this.goalSelector.addGoal(9, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setCallsForHelp());
    }

    @Override
    public void travel(Vector3d travelVector) {
        double d0 = this.getPosX();
        double d1 = this.getPosY();
        double d2 = this.getPosZ();
        super.travel(travelVector);
        this.calculateTravelHunger(this.getPosX() - d0, this.getPosY() - d1, this.getPosZ() - d2);
    }

    public void calculateTravelHunger(double p_71000_1_, double p_71000_3_, double p_71000_5_) {
        if (!this.isPassenger()) {
            double movemlength = p_71000_1_ * p_71000_1_ + p_71000_3_ * p_71000_3_ + p_71000_5_ * p_71000_5_;
            if (this.isSwimming()) {
                int i = Math.round(MathHelper.sqrt(movemlength) * 100.0F);
                if (i > 0) {
                    this.addExhaustion(0.005F * (float)i * 0.005F);
                }
            } else if (this.areEyesInFluid(FluidTags.WATER)) {
                int j = Math.round(MathHelper.sqrt(movemlength) * 100.0F);
                if (j > 0) {
                    this.addExhaustion(0.005F * (float)j * 0.005F);
                }
            } else if (this.isInWater()) {
                int k = Math.round(MathHelper.sqrt(p_71000_1_ * p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0F);
                if (k > 0) {
                    this.addExhaustion(0.005F * (float)k * 0.005F);
                }
            } else if (this.onGround) {
                int l = Math.round(MathHelper.sqrt(p_71000_1_ * p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0F);
                if (l > 0) {
                    if (this.isSprinting()) {
                        this.addExhaustion(0.05F * (float)l * 0.005F);
                    } else if (this.isCrouching()) {
                        this.addExhaustion(0.0F * (float)l * 0.0025F);
                    } else {
                        this.addExhaustion(0.0F * (float)l * 0.0025F);
                    }
                }
            }

        }
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
    public void startSleeping(@Nonnull BlockPos pos) {
        super.startSleeping(pos);
        this.setLastSlept(this.getEntityWorld().getDayTime());
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

    @MethodsReturnNonnullByDefault
    @Override
    public PushReaction getPushReaction() {
        return this.isSleeping()? PushReaction.IGNORE:super.getPushReaction();
    }

    @Nonnull
    @Override
    public ActionResultType applyPlayerInteraction(@Nonnull PlayerEntity player, @Nonnull Vector3d vec, @Nonnull Hand hand) {
        if(this.isOwner(player) && !(player.getHeldItem(hand).getItem() instanceof ItemRiggingBase)){

            if(this.getRidingEntity() != null){
                this.stopRiding();
            }

            if(player.isSneaking() && !(player.getHeldItem(hand).getItem() instanceof ItemBandage)){
                if(!this.world.isRemote) {
                    this.openGUI((ServerPlayerEntity) player);
                }
                Main.PROXY.setSharedMob(this);
                return ActionResultType.SUCCESS;
            }
            else if(player.getHeldItem(hand).getItem() != registerItems.ORIGINIUM_PRIME.get() && player.getHeldItem(hand).getItem().isFood() && this.canEat(player.getHeldItem(hand).getItem().getFood().canEatWhenFull())){
                ItemStack stack = this.onFoodEaten(this.getEntityWorld(), player.getHeldItem(hand));
                this.addAffection(0.03);
                if(!player.isCreative()){
                    player.setHeldItem(hand, stack);
                }
                return ActionResultType.SUCCESS;
            }
            else if(player.getHeldItem(hand).getItem() instanceof PotionItem){
                ItemStack stack =player.getHeldItem(hand);
                for(EffectInstance effectinstance : PotionUtils.getEffectsFromStack(stack)) {
                    if (effectinstance.getPotion().isInstant()) {
                        effectinstance.getPotion().affectEntity(player, player, this, effectinstance.getAmplifier(), 1.0D);
                    } else {
                        this.addPotionEffect(new EffectInstance(effectinstance));
                    }
                    this.addAffection(effectinstance.getPotion().isBeneficial()? 0.05:-0.075);
                }
                if(!player.isCreative()) {
                    player.setHeldItem(hand, new ItemStack(Items.GLASS_BOTTLE));
                }
                return ActionResultType.SUCCESS;
            }
            else{

                ItemStack heldstacks = player.getHeldItemMainhand();

                if(heldstacks != ItemStack.EMPTY) {
                    if (heldstacks.getItem() == registerItems.OATHRING.get()) {
                        if (this.getAffection() < 100 && !player.isCreative()) {
                            player.sendMessage(new TranslationTextComponent("entity.not_enough_affection"), this.getUniqueID());
                            return ActionResultType.FAIL;
                        }
                        else {
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
                            if(!this.world.isRemote) {
                                this.heal(1.0f);
                                if (!player.isCreative()) {
                                    if (heldstacks.attemptDamageItem(1, MathUtil.getRand(), (ServerPlayerEntity) player)) {
                                        heldstacks.shrink(1);
                                    }
                                }
                                double d0 = this.rand.nextGaussian() * 0.02D;
                                double d1 = this.rand.nextGaussian() * 0.02D;
                                double d2 = this.rand.nextGaussian() * 0.02D;
                                player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.0f, 1.0f);
                                this.getDataManager().set(HEAL_TIMER, 50);
                                this.addAffection(0.12F);
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
                    Vector3d LegDelta = new Vector3d(this.getPosX() - player.getPosX(), this.getPosY()+0.4 - player.getPosYEye(), this.getPosZ() - player.getPosZ());
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
                        this.SwitchSittingStatus();
                        return ActionResultType.SUCCESS;
                        //this.func_233687_w_(!this.isSitting());
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

    private void SwitchSittingStatus() {
        boolean value = !this.isSitting();
        this.func_233687_w_(value);
        if(this.getEntityWorld().isRemote()) {
            Main.NETWORK.sendToServer(new ChangeEntityBehaviorPacket(this.getEntityId(), ChangeEntityBehaviorPacket.EntityBehaviorType.SIT, value));
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.setPathPriority(PathNodeType.WATER, this.getOwner() != null && this.getOwner().isInWater()? 0.0F:-1.0F);
        if(this.getHOMEPOS().isPresent()&&this.ticksExisted%30==0){
            BlockState blockstate = this.world.getBlockState(this.getHOMEPOS().get());
            if(!blockstate.isBed(this.getEntityWorld(),this.getHOMEPOS().get(), this)) {
                this.clearHomePos();
            }
        }
        if(!this.getEntityWorld().isRemote()){
            this.foodStats.tick(this);
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

    @Nonnull
    @Override
    public IPacket<?> createSpawnPacket() {
        NetworkHooks.getEntitySpawningPacket(this);
        return super.createSpawnPacket();
    }

    public void SwitchItemBehavior(){
        Main.NETWORK.sendToServer(new ChangeEntityBehaviorPacket(this.getEntityId(), ChangeEntityBehaviorPacket.EntityBehaviorType.ITEMPICKUP, !this.shouldPickupItem()));
    }

    public void SwitchFreeRoamingStatus() {
        Main.NETWORK.sendToServer(new ChangeEntityBehaviorPacket(this.getEntityId(), ChangeEntityBehaviorPacket.EntityBehaviorType.HOMEMODE, !this.isFreeRoaming()));
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

        long deltaTime = lastSlept-lastWokenup;

        this.addAffection(0.02*((float)deltaTime/300));
        this.addMorale(((float)deltaTime/1000)*30);
    }

    public double getMorale() {
        return this.dataManager.get(MORALE);
    }

    public void setMorale(float morale) {
        this.dataManager.set(MORALE, morale);
    }

    public void addMorale(double value){
        double prevmorale = this.getMorale();


        this.setMorale((float) MathUtils.clamp(prevmorale+value, 0, 150));
    }

    public Optional<BlockPos> getStayCenterPos() {
        return this.getDataManager().get(STAYPOINT);
    }

    public void setStayCenterPos(BlockPos stayCenterPos) {
        this.getDataManager().set(STAYPOINT,Optional.of(stayCenterPos));
    }

    public void clearStayCenterPos() {
        this.getDataManager().set(STAYPOINT, Optional.empty());
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

    public boolean addStackToInventory(ItemStack stack){
        for(int i=0; i<this.getInventory().getSlots(); i++){
            if(this.getInventory().insertItem(i, stack, false) == ItemStack.EMPTY){
                return true;
            }
        }
        return false;
    }

    public boolean addStackToAmmoStorage(ItemStack stack){
        for(int i=0; i<this.getAmmoStorage().getSlots(); i++){
            if(this.getAmmoStorage().insertItem(i, stack, false) == ItemStack.EMPTY){
                return true;
            }
        }
        return false;
    }

    public abstract enums.CompanionRarity getRarity();

    protected float getSitHeight(){
        return 1F;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    @MethodsReturnNonnullByDefault
    public EntitySize getSize(@ParametersAreNonnullByDefault Pose poseIn) {
        if(this.isSitting()){
            return new EntitySize(this.getWidth(), this.getSitHeight(), false);
        }
        return super.getSize(poseIn);
    }

    public void func_233687_w_(boolean p_233687_1_) {
        this.getDataManager().set(SITTING, p_233687_1_);
        super.func_233687_w_(p_233687_1_);
        this.recalculateSize();
    }

}
