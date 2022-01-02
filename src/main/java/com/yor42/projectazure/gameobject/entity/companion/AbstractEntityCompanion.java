package com.yor42.projectazure.gameobject.entity.companion;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.capability.ProjectAzurePlayerCapability;
import com.yor42.projectazure.gameobject.entity.CompanionDefaultMovementController;
import com.yor42.projectazure.gameobject.entity.CompanionGroundPathNavigator;
import com.yor42.projectazure.gameobject.entity.CompanionSwimPathFinder;
import com.yor42.projectazure.gameobject.entity.CompanionSwimPathNavigator;
import com.yor42.projectazure.gameobject.entity.ai.goals.*;
import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityKansenAircraftCarrier;
import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityKansenBase;
import com.yor42.projectazure.gameobject.entity.companion.magicuser.AbstractCompanionMagicUser;
import com.yor42.projectazure.gameobject.entity.misc.AbstractEntityDrone;
import com.yor42.projectazure.gameobject.items.ItemBandage;
import com.yor42.projectazure.gameobject.items.ItemCannonshell;
import com.yor42.projectazure.gameobject.items.ItemCommandStick;
import com.yor42.projectazure.gameobject.items.ItemMagazine;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
import com.yor42.projectazure.intermod.ModCompatibilities;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.utils.ItemStackUtils;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.network.packets.EntityInteractionPacket;
import com.yor42.projectazure.network.packets.spawnParticlePacket;
import com.yor42.projectazure.setup.register.registerItems;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.PushReaction;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
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
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullConsumer;
import net.minecraftforge.common.util.NonNullSupplier;
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
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.*;
import static net.minecraft.util.Hand.MAIN_HAND;
import static net.minecraft.util.Hand.OFF_HAND;
import static net.minecraftforge.fml.network.PacketDistributor.TRACKING_ENTITY_AND_SELF;

public abstract class AbstractEntityCompanion extends TameableEntity implements IAnimatable {
    private static final AttributeModifier USE_ITEM_SPEED_PENALTY = new AttributeModifier(UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E"), "Use item speed penalty", -0.15D, AttributeModifier.Operation.ADDITION);

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

    protected final ItemStackHandler Inventory = new ItemStackHandler(12+this.getSkillItemCount()){
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {

            if(slot == 12){
                return AbstractEntityCompanion.this.isSkillItem(stack);
            }

            return super.isItemValid(slot, stack);
        }
    };
    public ItemStackHandler AmmoStorage = new ItemStackHandler(0){
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {

            return stack.getItem() instanceof ItemCannonshell || stack.getItem() instanceof ItemMagazine;
        }
    };

    protected int patTimer, shieldCoolDown;
    private int ItemSwapIndexOffhand = -1;
    private int ItemSwapIndexMainHand = -1;
    protected boolean isSwimmingUp;
    protected boolean shouldBeSitting;
    protected boolean isMeleeing;
    protected boolean isOpeningDoor;
    public boolean isMovingtoRecruitStation = false;
    protected int awakeningLevel;
    @Nullable
    public BlockPos RECRUIT_BEACON_POS;
    private final MovementController SwimController;
    private final MovementController MoveController;
    protected final SwimmerPathNavigator swimmingNav;
    private final GroundPathNavigator groundNav;
    protected CompanionFoodStats foodStats = new CompanionFoodStats();
    private List<ExperienceOrbEntity> nearbyExpList;

    protected long lastSlept, lastWokenup;
    private int forcewakeupExpireTimer, forceWakeupCounter, expdelay;
    protected static final DataParameter<Integer> SKILLDELAYTICK = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.VARINT);
    protected static final DataParameter<Boolean> EATING = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> USING_SKILL = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
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
    protected static final DataParameter<Integer> PAT_ANIMATION_TIME = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.VARINT);
    protected static final DataParameter<Boolean> OATHED = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> PICKUP_ITEM = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> ISFREEROAMING = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Optional<BlockPos>> STAYPOINT = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.OPTIONAL_BLOCK_POS);
    protected static final DataParameter<Optional<BlockPos>> HOMEPOS = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.OPTIONAL_BLOCK_POS);
    protected static final DataParameter<Float> VALID_HOME_DISTANCE = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.FLOAT);
    protected static final DataParameter<Boolean> ISFORCEWOKENUP = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> ISUSINGGUN = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Integer> HEAL_TIMER = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.VARINT);
    protected static final DataParameter<Integer> RELOAD_TIMER_MAINHAND = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.VARINT);
    protected static final DataParameter<Integer> RELOAD_TIMER_OFFHAND = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.VARINT);
    protected static final DataParameter<Integer> FOODLEVEL = EntityDataManager.createKey(AbstractEntityCompanion.class, DataSerializers.VARINT);
    public abstract enums.EntityType getEntityType();
    protected AbstractEntityCompanion(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
        this.setAffection(40F);
        this.swimmingNav = new CompanionSwimPathNavigator(this, worldIn);
        this.groundNav = new CompanionGroundPathNavigator(this, worldIn);
        this.SwimController = new CompanionSwimPathFinder(this);
        this.MoveController = new CompanionDefaultMovementController(this);
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

    public void onLevelup(){
        ModifiableAttributeInstance modifiableattributeinstance = this.getAttribute(Attributes.MAX_HEALTH);
        modifiableattributeinstance.setBaseValue(this.getAttributeValue(Attributes.MAX_HEALTH)+this.getLevel());
        this.heal(this.getMaxHealth());
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

        ItemStack stack2Return = ItemStack.EMPTY;
        int currentMaxAmmo = 0;
        for(int i=0; i<this.getAmmoStorage().getSlots();i++){
            ItemStack candidateStack = this.getAmmoStorage().getStackInSlot(i);
            if(candidateStack.getItem() instanceof ItemMagazine && ((ItemMagazine) candidateStack.getItem()).getAmmoType() == calibur){
                int AmmoInCandidate = ItemStackUtils.getRemainingAmmo(candidateStack);
                if(AmmoInCandidate>currentMaxAmmo) {
                    stack2Return = candidateStack;
                    currentMaxAmmo = AmmoInCandidate;
                }
            }
        }
        return stack2Return;
    }

    protected boolean isMoving(){
        return  !(this.limbSwingAmount > -0.1F && this.limbSwingAmount < 0.1F) && !this.isSitting();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        if(capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            return LazyOptional.of(this::getInventory).cast();
        }
        return super.getCapability(capability, facing);
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return this.isActiveItemStackBlocking()? SoundEvents.ITEM_SHIELD_BLOCK:super.getHurtSound(damageSourceIn);
    }

    @Override
    protected void damageArmor(DamageSource damageSource, float damage) {
        if (damage >= 0.0F) {
            damage = damage / 4.0F;
            if (damage < 1.0F) {
                damage = 1.0F;
            }
            for (int i = 0; i < this.getInventory().getSlots(); ++i) {
                ItemStack itemstack = this.getInventory().getStackInSlot(i);
                if ((!damageSource.isFireDamage() || !itemstack.getItem().isImmuneToFire()) && itemstack.getItem() instanceof ArmorItem) {
                    int j = i;
                    itemstack.damageItem((int) damage, this, (p_214023_1_) -> p_214023_1_.sendBreakAnimation(EquipmentSlotType.fromSlotTypeAndIndex(EquipmentSlotType.Group.ARMOR, j)));
                }
            }
        }
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
        if(this.RECRUIT_BEACON_POS != null) {
            compound.putDouble("RecruitStationPosX", this.RECRUIT_BEACON_POS.getX());
            compound.putDouble("RecruitStationPosY", this.RECRUIT_BEACON_POS.getY());
            compound.putDouble("RecruitStationPosZ", this.RECRUIT_BEACON_POS.getZ());
        }
        if(this.dataManager.get(HOMEPOS).isPresent()) {
            compound.putDouble("HomePosX", this.dataManager.get(HOMEPOS).get().getX());
            compound.putDouble("HomePosY", this.dataManager.get(HOMEPOS).get().getY());
            compound.putDouble("HomePosZ", this.dataManager.get(HOMEPOS).get().getZ());
        }
        compound.putBoolean("eating", this.dataManager.get(EATING));
        compound.putBoolean("shouldbeSitting", this.shouldBeSitting);
        compound.putBoolean("isforcewokenup", this.dataManager.get(ISFORCEWOKENUP));
        compound.putDouble("exp", this.dataManager.get(EXP));
        compound.putInt("level", this.getDataManager().get(LEVEL));
        compound.putInt("limitbreaklv", this.dataManager.get(LIMITBREAKLEVEL));
        compound.putInt("awaken", this.awakeningLevel);
        compound.putBoolean("isSitting", this.isSitting());
        compound.putBoolean("freeroaming", this.isFreeRoaming());
        compound.putDouble("morale", this.dataManager.get(MORALE));
        compound.putInt("pat_animation", this.dataManager.get(PAT_ANIMATION_TIME));
        compound.putLong("lastslept", this.lastSlept);
        compound.putLong("lastwoken", this.lastWokenup);
        compound.putBoolean("isMovingtoRecruitStation", this.isMovingtoRecruitStation);
        compound.put("inventory", this.getInventory().serializeNBT());
        compound.putInt("shieldcooldown", this.shieldCoolDown);
        compound.put("ammostorage", this.getAmmoStorage().serializeNBT());
        this.foodStats.write(compound);
        compound.putInt("SwapIndexMainHand", this.ItemSwapIndexMainHand);
        compound.putInt("SwapIndexOffHand", this.ItemSwapIndexOffhand);
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
            this.dataManager.set(STAYPOINT, Optional.of(new BlockPos(compound.getDouble("stayX"), compound.getDouble("stayY"), compound.getDouble("stayZ"))));
        }
        this.dataManager.set(VALID_HOME_DISTANCE, compound.getFloat("home_distance"));
        boolean hasRecruitBeaconPos = compound.contains("RecruitStationPosX") && compound.contains("RecruitStationPosY") && compound.contains("RecruitStationPosZ");
        if(hasRecruitBeaconPos) {
            this.RECRUIT_BEACON_POS = new BlockPos(compound.getDouble("RecruitStationPosX"), compound.getDouble("RecruitStationPosY"), compound.getDouble("RecruitStationPosZ"));
        }

        boolean hasHomePos = compound.contains("HomePosX") && compound.contains("HomePosY") && compound.contains("HomePosZ");
        if(hasHomePos) {
            this.dataManager.set(HOMEPOS, Optional.of(new BlockPos(compound.getDouble("HomePosX"), compound.getDouble("HomePosY"), compound.getDouble("HomePosZ"))));
        }
        this.dataManager.set(ISFORCEWOKENUP, compound.getBoolean("isforcewokenup"));
        this.shouldBeSitting = compound.getBoolean("shouldbeSitting");
        this.getDataManager().set(LEVEL, compound.getInt("level"));
        this.dataManager.set(EXP, compound.getFloat("exp"));
        this.dataManager.set(MORALE, compound.getFloat("morale"));
        this.dataManager.set(SITTING, compound.getBoolean("issitting"));
        this.dataManager.set(LIMITBREAKLEVEL, compound.getInt("limitbreaklv"));
        this.dataManager.set(PAT_ANIMATION_TIME, compound.getInt("pat_animation"));
        this.dataManager.set(EATING, compound.getBoolean("eating"));
        this.shieldCoolDown = compound.getInt("shieldcooldown");
        this.awakeningLevel = compound.getInt("awaken");
        this.isMovingtoRecruitStation = compound.getBoolean("isMovingtoRecruitStation");
        this.setFreeRoaming(compound.getBoolean("freeroaming"));
        this.getInventory().deserializeNBT(compound.getCompound("inventory"));
        this.lastSlept = compound.getLong("lastslept");
        this.lastWokenup = compound.getLong("lastwoken");
        this.getAmmoStorage().deserializeNBT(compound.getCompound("ammostorage"));
        this.getFoodStats().read(compound);
        this.ItemSwapIndexMainHand = compound.getInt("SwapIndexMainHand");
        this.ItemSwapIndexOffhand = compound.getInt("SwapIndexOffHand");

        ModifiableAttributeInstance modifiableattributeinstance = this.getAttribute(Attributes.MAX_HEALTH);
        if(modifiableattributeinstance != null) {
            modifiableattributeinstance.setBaseValue(this.getAttributeValue(Attributes.MAX_HEALTH) + this.getLevel());
        }
    }

    public int getItemSwapIndexMainHand(){
        return this.ItemSwapIndexMainHand;
    }

    public void setItemSwapIndexMainHand(int value) {
        this.ItemSwapIndexMainHand = value;
    }

    public int getItemSwapIndexOffHand(){
        return this.ItemSwapIndexOffhand;
    }

    public void setItemSwapIndexOffHand(int value) {
        this.ItemSwapIndexOffhand = value;
    }

    public int getItemSwapIndex(Hand hand){
        return hand == MAIN_HAND? this.getItemSwapIndexMainHand() : this.getItemSwapIndexOffHand();
    }

    public void setItemswapIndex(Hand hand, int value){
        if (hand == MAIN_HAND) {
            setItemSwapIndexMainHand(value);
        } else {
            setItemSwapIndexOffHand(value);
        }
    }

    public Optional<Hand> getFreeHand(){
        if(this.getItemSwapIndexMainHand() == -1){
            return Optional.of(MAIN_HAND);
        }
        else if (this.getItemSwapIndexOffHand() == -1){
            return Optional.of(OFF_HAND);
        }
        return Optional.empty();
    }

    @Override
    public boolean shouldAttackEntity(LivingEntity target, LivingEntity owner) {

        if(target instanceof TameableEntity){
            if(((TameableEntity) target).isOwner(owner)){
                return false;
            }
        }
        else if(target instanceof PlayerEntity){
            return !this.isOwner(target) && this.isPVPenabled();
        }
        if(target instanceof AbstractEntityDrone && ((AbstractEntityDrone)target).getOwner().isPresent() && (((AbstractEntityDrone) target).getOwner().get() == this ||  (((AbstractEntityDrone) target).getOwner().get() instanceof AbstractEntityCompanion && this.getOwner() != null && ((AbstractEntityCompanion) ((AbstractEntityDrone) target).getOwner().get()).isOwner(this.getOwner())))){
            return false;
        }

        return super.shouldAttackEntity(target, owner);
    }

    public void setHomeposAndDistance(BlockPos pos, float validDistance){
        this.getDataManager().set(HOMEPOS, Optional.of(pos));
        this.dataManager.set(VALID_HOME_DISTANCE, validDistance);
    }

    public void clearHomePos(){
        this.getDataManager().set(HOMEPOS, Optional.empty());
        this.dataManager.set(VALID_HOME_DISTANCE, -1.0f);
    }


    public Optional<BlockPos> getRecruitStationPos() {
        return Optional.ofNullable(this.RECRUIT_BEACON_POS);
    }

    public void setRecruitStationPos(BlockPos pos){
        this.RECRUIT_BEACON_POS= pos;
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

    public void setHomePos(BlockPos pos){
        this.setHomeposAndDistance(pos, 64);
    }

    @Override
    protected void collideWithEntity(@Nonnull Entity entityIn) {
        super.collideWithEntity(entityIn);
    }

    public boolean isInHomeRangefromCurrenPos(){
        return this.isInHomeRange(this.getPosition());
    }

    @Nonnull
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

    public boolean isEating(){
        return this.getDataManager().get(EATING);
    }

    public void setEating(boolean value){
        this.dataManager.set(EATING, value);
    }

    public boolean isFreeRoaming() {
        return this.dataManager.get(ISFREEROAMING);
    }

    public void setFreeRoaming(boolean value) {
        this.getNavigator().clearPath();
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

    public ItemStack getRigging(){
        return ItemStack.EMPTY;
    }

    public boolean shouldUseGun() {
        ItemStack gunstack = this.getGunStack();
        if (gunstack.getItem() instanceof ItemGunBase) {
            ItemGunBase gunitem = (ItemGunBase) gunstack.getItem();
            return (!this.canUseCannonOrTorpedo() || !this.isSailing()) && this.getGunStack() != ItemStack.EMPTY && (this.HasRightMagazine(gunitem.getAmmoType()) || getRemainingAmmo(gunstack)>0);
        }
        return false;
    }

    public boolean HasRightMagazine(enums.AmmoCalibur calibur){
        return !this.getMagazine(calibur).isEmpty();
    }

    public boolean isSailing(){
        return false;
    }

    public void disableShield(boolean increase) {
        float chance = 0.25F + (float) EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;
        if (increase)
            chance += 0.75;
        if (this.rand.nextFloat() < chance) {
            this.shieldCoolDown = 100;
            this.stopActiveHand();
            this.world.setEntityState(this, (byte) 30);
        }
    }

    @Override
    protected void damageShield(float damage) {
        if (this.activeItemStack.isShield(this)) {
            if (damage >= 3.0F) {
                int i = (int) (1 + Math.floor(damage));
                Hand hand = this.getActiveHand();
                this.activeItemStack.damageItem(i, this, (entity) -> entity.sendBreakAnimation(hand));
                if (this.activeItemStack.isEmpty()) {
                    if (hand == MAIN_HAND) {
                        this.setItemStackToSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
                    } else {
                        this.setItemStackToSlot(EquipmentSlotType.OFFHAND, ItemStack.EMPTY);
                    }
                    this.activeItemStack = ItemStack.EMPTY;
                    this.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + this.world.rand.nextFloat() * 0.4F);
                }
            }
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {

        if(source.getTrueSource() instanceof TameableEntity && this.getOwner() != null && ((TameableEntity) source.getTrueSource()).isOwner(this.getOwner())){
            return false;
        }

        if(source.getTrueSource() instanceof AbstractEntityDrone && ((AbstractEntityDrone) source.getTrueSource()).getOwner().isPresent() && (((AbstractEntityDrone) source.getTrueSource()).getOwner().get() == this ||  (((AbstractEntityDrone) source.getTrueSource()).getOwner().get() instanceof AbstractEntityCompanion && this.getOwner() != null && ((AbstractEntityCompanion) ((AbstractEntityDrone) source.getTrueSource()).getOwner().get()).isOwner(this.getOwner())))){
            return false;
        }

        return super.attackEntityFrom(source, amount);
    }

    public int getShieldCoolDown(){
        return this.shieldCoolDown;
    }

    @Override
    protected void blockUsingShield(LivingEntity entityIn) {
        super.blockUsingShield(entityIn);
        if (entityIn.getHeldItemMainhand().canDisableShield(this.activeItemStack, this, entityIn))
            this.disableShield(true);
    }

    @Override
    public void setActiveHand(Hand hand) {
        ItemStack itemstack = this.getHeldItem(hand);
        if (itemstack.isShield(this)) {
            ModifiableAttributeInstance modifiableattributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
            modifiableattributeinstance.removeModifier(USE_ITEM_SPEED_PENALTY);
            modifiableattributeinstance.applyNonPersistentModifier(USE_ITEM_SPEED_PENALTY);
        }
        super.setActiveHand(hand);
    }

    @Override
    public void stopActiveHand() {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(USE_ITEM_SPEED_PENALTY))
            this.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(USE_ITEM_SPEED_PENALTY);
        super.stopActiveHand();
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
        this.dataManager.register(SKILLDELAYTICK, 0);
        this.dataManager.register(EXP, 0.0F);
        this.dataManager.register(LIMITBREAKLEVEL, 0);
        this.dataManager.register(MORALE, 0.0F);
        this.dataManager.register(EATING, false);
        this.dataManager.register(LEVEL, 0);
        this.dataManager.register(AFFECTION, 0.0F);
        this.dataManager.register(SITTING, false);
        this.dataManager.register(OPENINGDOOR, false);
        this.dataManager.register(MELEEATTACKING, false);
        this.dataManager.register(MAXPATEFFECTCOUNT, 0);
        this.dataManager.register(PATEFFECTCOUNT, 0);
        this.dataManager.register(PATCOOLDOWN, 0);
        this.dataManager.register(PAT_ANIMATION_TIME, 0);
        this.dataManager.register(OATHED, false);
        this.dataManager.register(USINGBOW, false);
        this.dataManager.register(USING_SKILL, false);
        this.dataManager.register(STAYPOINT, Optional.empty());
        this.dataManager.register(HOMEPOS, Optional.empty());
        this.dataManager.register(ISFORCEWOKENUP, false);
        this.dataManager.register(ISUSINGGUN, false);
        this.dataManager.register(ISFREEROAMING, false);
        this.dataManager.register(VALID_HOME_DISTANCE, -1.0f);
        this.dataManager.register(HEAL_TIMER, 0);
        this.dataManager.register(RELOAD_TIMER_MAINHAND, 0);
        this.dataManager.register(RELOAD_TIMER_OFFHAND, 0);
        this.dataManager.register(PICKUP_ITEM, false);
        this.dataManager.register(FOODLEVEL, 0);
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
        this.RECRUIT_BEACON_POS= null;
    }

    public boolean canEat(boolean ignoreHunger) {
        return ignoreHunger || this.foodStats.needFood();
    }

    public void addExhaustion(float exhaustion) {
        if (!this.world.isRemote) {
            this.foodStats.addExhaustion(exhaustion);
        }
    }

    @MethodsReturnNonnullByDefault
    public ItemStack onFoodEaten(World WorldIn, ItemStack foodStack) {
        this.getFoodStats().consume(foodStack.getItem(), foodStack);
        if(!this.getEntityWorld().isRemote()) {
            Main.NETWORK.send(TRACKING_ENTITY_AND_SELF.with(() -> this), new spawnParticlePacket(this, foodStack));
        }
        WorldIn.playSound(null, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.PLAYERS, 0.5F, WorldIn.rand.nextFloat() * 0.1F + 0.9F);
        if(foodStack.getItem().isFood()){
            Food food = foodStack.getItem().getFood();
            if(food != null) {
                this.addMorale(foodStack.getItem().getFood().getHealing() * 4);
            }
        }
        return super.onFoodEaten(WorldIn, foodStack);
    }

    public CompanionFoodStats getFoodStats() {
        return this.foodStats;
    }

    public boolean shouldHeal() {
        return this.getHealth() > 0.0F && this.getHealth() < this.getMaxHealth();
    }

    public boolean isBeingPatted() {
        return this.getDataManager().get(PAT_ANIMATION_TIME) !=0;
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
                this.onLevelup();
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

        if(this.getOwner() != null && this.getOwner() instanceof PlayerEntity && this.isAlive()){
            ProjectAzurePlayerCapability cap = ProjectAzurePlayerCapability.getCapability((PlayerEntity) this.getOwner());
            if(!cap.getCompanionList().contains(this)){
                cap.addCompanion(this);
            }
        }

        if (this.world.getDifficulty() == Difficulty.PEACEFUL && this.world.getGameRules().getBoolean(GameRules.NATURAL_REGENERATION)) {
            if (this.getHealth() < this.getMaxHealth() && this.ticksExisted % 20 == 0) {
                this.heal(1.0F);
            }

            if (this.foodStats.needFood() && this.ticksExisted % 10 == 0) {
                this.foodStats.setFoodLevel(this.foodStats.getFoodLevel() + 1);
            }
        }
        if (this.shieldCoolDown > 0) {
            --this.shieldCoolDown;
        }

        if(!this.getEntityWorld().isRemote() && this.ticksExisted % 200 == 0&& !this.getHOMEPOS().isPresent()){
            Optional<BlockPos> optional = this.findHomePosition((ServerWorld) this.getEntityWorld(), this);
            optional.ifPresent(blockPos -> this.setHomeposAndDistance(blockPos, 64));
        }

        if(this.isPotionActive(Effects.HUNGER)){
            EffectInstance hunger = this.getActivePotionEffect(Effects.HUNGER);
            if((hunger != null ? hunger.getDuration() : 0) >0 && hunger.getPotion().isReady(hunger.getDuration(), hunger.getAmplifier())){
                this.addExhaustion(0.005F * (float)(hunger.getAmplifier() + 1));
            }
        }

        if(this.isPotionActive(Effects.SATURATION)){
            EffectInstance Saturation = this.getActivePotionEffect(Effects.SATURATION);
            if((Saturation != null ? Saturation.getDuration() : 0) >0 && Saturation.getPotion().isReady(Saturation.getDuration(), Saturation.getAmplifier())){
                this.getFoodStats().addStats(Saturation.getAmplifier() + 1, 1.0F);
            }
        }

        this.updateArmSwingProgress();
        this.updateSkillDelay();

        if(this.getDataManager().get(SITTING) != this.isSitting()) {
            this.func_233687_w_(this.getDataManager().get(SITTING));
        }

        if(this.expdelay>0){
            this.expdelay--;
        }

        if(this.isSprinting() && !this.isMoving()){
            this.setSprinting(false);
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

        if(this.getDataManager().get(PAT_ANIMATION_TIME) >0){

            this.navigator.clearPath();

            this.getDataManager().set(PAT_ANIMATION_TIME, this.getDataManager().get(PAT_ANIMATION_TIME)-1);
            this.patTimer++;

            if (this.patTimer%20 == 0 && !this.getEntityWorld().isRemote()) {
                if (this.dataManager.get(PATEFFECTCOUNT) < this.dataManager.get(MAXPATEFFECTCOUNT)) {
                    this.dataManager.set(PATEFFECTCOUNT, this.dataManager.get(PATEFFECTCOUNT)+1);
                    this.addAffection(0.025);
                    this.addMorale(0.5);
                    Main.NETWORK.send(TRACKING_ENTITY_AND_SELF.with(()->this), new spawnParticlePacket(this, spawnParticlePacket.Particles.AFFECTION_HEART));

                } else {
                    this.dataManager.set(PATEFFECTCOUNT, this.dataManager.get(MAXPATEFFECTCOUNT));
                    if(this.dataManager.get(PATCOOLDOWN) == 0){
                        this.dataManager.set(PATCOOLDOWN, (7+this.rand.nextInt(5))*1200);
                    }
                    Main.NETWORK.send(TRACKING_ENTITY_AND_SELF.with(()->this), new spawnParticlePacket(this, spawnParticlePacket.Particles.AFFECTION_SMOKE));
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
            if(this.getDataManager().get(RELOAD_TIMER_MAINHAND) == 0 && this.getGunStack().getItem() instanceof ItemGunBase){
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

        if(this.isSitting() && this.ticksExisted%20 == 0){
            this.addMorale(0.015);
        }
        else if(!this.isFreeRoaming()&& this.ticksExisted%20 == 0){
            this.addMorale(-0.0001);
        }
        if(!this.world.isRemote() && this.getEntityWorld().isDaytime()) {
            if (this.isSleeping()) {
                this.wakeUp();
                if(this.shouldBeSitting){
                    this.func_233687_w_(true);
                }
            }
        }

        if(!this.getEntityWorld().isRemote() && this.isFreeRoaming() && (this.getEntityWorld().isNightTime() && this.isSitting() && this.isInHomeRangefromCurrenPos() && this.getNavigator().getPathToPos(this.getHOMEPOS().get(), 0) != null && this.getNavigator().getPathToPos(this.getHOMEPOS().get(), 0).reachesTarget())){
            this.shouldBeSitting = this.isSitting();
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

        Item guncandidate = this.getGunStack().getItem();
        if(guncandidate instanceof ItemGunBase) {
            ItemGunBase gun = (ItemGunBase) guncandidate;
            ItemStack MagStack = this.getMagazine(((ItemGunBase) this.getGunStack().getItem()).getAmmoType());
            if (!MagStack.isEmpty()) {
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

                //Return Empty Magazine for Reloading by player
                if (!this.addStackToInventory(EmptyMag)) {
                    if (!this.addStackToAmmoStorage(EmptyMag)) {
                        ItemEntity itemEntity = new ItemEntity(this.getEntityWorld(), this.getPosX(), this.getPosY(), this.getPosZ(), EmptyMag);
                        this.getEntityWorld().addEntity(itemEntity);
                    }
                }
            }
        }
    }

    public int getGunAmmoCount(){
        return ItemStackUtils.getRemainingAmmo(this.getGunStack());
    }

    public ItemStack getGunStack(){
        if(this.getHeldItem(this.getValidGunHand()).getItem() instanceof ItemGunBase){
            return this.getHeldItem(this.getValidGunHand());
        }
        return ItemStack.EMPTY;
    }

    private Hand getValidGunHand(){
        if(this.getHeldItemMainhand().getItem() instanceof ItemGunBase){
            return MAIN_HAND;
        }
        else
            return OFF_HAND;
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

    public int getSkillDelayTick(){
        return this.getDataManager().get(SKILLDELAYTICK);
    }

    public void setSkillDelayTick(int value){
        this.getDataManager().set(SKILLDELAYTICK, value);
    }

    public void setSkillDelaySeconds(int value){
        this.getDataManager().set(SKILLDELAYTICK, value*20);
    }

    public int getSkillItemCount(){
        return 1;
    }

    public void updateSkillDelay(){
        int skilldelay = this.getDataManager().get(SKILLDELAYTICK);
        if(skilldelay>0){
            this.getDataManager().set(SKILLDELAYTICK, skilldelay-1);
        }
    }

    public boolean performOneTimeSkill(LivingEntity target){
        return true;
    }

    public boolean performSkillTick(LivingEntity target, int Timer){
        return true;
    }

    public void resetSkill(){
        this.setUsingSkill(false);
    }

    public boolean isUsingSKill(){
        return this.getDataManager().get(USING_SKILL);
    }

    public void setUsingSkill(boolean value){
        this.getDataManager().set(USING_SKILL, value);
    }

    public boolean canUseSkill(){
        return false;
    }

    public void setSkillDelay(){
        this.setSkillDelayTick(2400);
    }

    public List<ItemStack> getSkillItem(){
        List<ItemStack> itemstack = new ArrayList<>();
        for(int k = 0; k<this.getSkillItemCount(); k++){
            itemstack.add(this.getInventory().getStackInSlot(12+k));
        }
        return itemstack;
    }

    public boolean isSkillItem(ItemStack stack){
        return false;
    }

    public boolean hasSkillItem(){
        List<ItemStack> itemstack = getSkillItem();
        for (ItemStack itemStack : itemstack) {
            if (!this.isSkillItem(itemStack)) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new CompanionMoveToRecruitStationGoal(this));
        this.goalSelector.addGoal(2, new CompanionSleepGoal(this));
        this.goalSelector.addGoal(3, new SitGoal(this));
        this.goalSelector.addGoal(4, new CompanionUseSkillGoal(this));
        if(this instanceof EntityKansenBase){
            if(this instanceof EntityKansenAircraftCarrier) {
                this.goalSelector.addGoal(5, new KansenLaunchPlaneGoal((EntityKansenAircraftCarrier) this, 20, 40, 50));
            }
            this.goalSelector.addGoal(6, new KansenRangedAttackGoal((EntityKansenBase) this, 0.8F, 10,20, 80, 100));
        }
        if(this instanceof AbstractCompanionMagicUser){
            this.goalSelector.addGoal(7, new CompanionSpellRangedAttackGoal((AbstractCompanionMagicUser) this, 10));
        }
        this.goalSelector.addGoal(8, new CompanionUseShieldGoal(this));
        this.goalSelector.addGoal(9, new CompanionHealandEatFoodGoal(this));
        this.goalSelector.addGoal(10, new CompanionsUseTotem(this));
        this.goalSelector.addGoal(11, new CompanionUseGunGoal(this, 40, 0.6));
        this.goalSelector.addGoal(12, new CompanionRideBoatAlongPlayerGoal(this, 1.0));
        this.goalSelector.addGoal(13, new CompanionMeleeGoal(this, 1.0D, true));
        this.goalSelector.addGoal(14, new CompanionFollowOwnerGoal(this, 0.75D, 5.0F, 2.0F, false));
        this.goalSelector.addGoal(15, new WorkGoal(this, 1.0D));
        this.goalSelector.addGoal(16, new CompanionHealOwnerAndAllyGoal(this, 20, 10, 1.25, 10F));
        this.goalSelector.addGoal(17, new CompanionOpenDoorGoal(this, true));
        this.goalSelector.addGoal(18, new CompanionFreeroamGoal(this, 60, true));
        this.goalSelector.addGoal(19, new CompanionPickupItemGoal(this));
        this.goalSelector.addGoal(20, new CompanionPlaceTorchGoal(this));
        this.goalSelector.addGoal(21, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(22, new LookRandomlyGoal(this));

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
        return !this.isInWater() && !this.isSitting() && !this.isSleeping() && !this.isFreeRoaming();
    }

    public boolean isReloadingMainHand(){
        int remainingTime = this.getDataManager().get(RELOAD_TIMER_MAINHAND);
        return remainingTime>0;
    }

    public void setReloadDelay(){
        this.getDataManager().set(RELOAD_TIMER_MAINHAND, this.Reload_Anim_Delay());
    }

    public int Reload_Anim_Delay(){
        return 63;
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
        ProjectAzurePlayerCapability capability = ProjectAzurePlayerCapability.getCapability(player);
        capability.addCompanion(this);
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

        ItemStack heldstacks = player.getHeldItemMainhand();
        Item HeldItem = heldstacks.getItem();

        if(this.isOwner(player) && !(this instanceof EntityKansenBase && HeldItem instanceof ItemRiggingBase)) {

            if (this.getRidingEntity() != null) {
                this.stopRiding();
            }

            if (HeldItem instanceof ArmorItem || HeldItem instanceof TieredItem) {
                ItemStack stack = player.getHeldItem(hand).copy();
                @Nullable
                ArmorItem item = stack.getItem() instanceof ArmorItem ? (ArmorItem) stack.getItem() : null;
                EquipmentSlotType type = getSlotForItemStack(stack);
                ItemStack EquippedStack = this.getItemStackFromSlot(type).copy();
                if (stack.canEquip(type, this)) {
                    this.setItemStackToSlot(type, stack);
                    if (stack == this.getItemStackFromSlot(type)) {
                        player.setHeldItem(hand, EquippedStack);
                        this.playSound(item == null ? SoundEvents.ITEM_ARMOR_EQUIP_IRON : item.getArmorMaterial().getSoundEvent(), 0.8F, 0.8F + this.world.rand.nextFloat() * 0.4F);
                        return ActionResultType.SUCCESS;
                    }
                }
            }
            //food
            else if (HeldItem != registerItems.ORIGINIUM_PRIME.get() && HeldItem.getFood() != null && this.canEat(HeldItem.getFood().canEatWhenFull())) {
                ItemStack stack = this.onFoodEaten(this.getEntityWorld(), player.getHeldItem(hand));
                this.addAffection(0.03);
                if (!player.isCreative()) {
                    player.setHeldItem(hand, stack);
                }
                return ActionResultType.SUCCESS;
                //Potion
            } else if (HeldItem instanceof PotionItem) {
                ItemStack stack = player.getHeldItem(hand);
                for (EffectInstance effectinstance : PotionUtils.getEffectsFromStack(stack)) {
                    if (effectinstance.getPotion().isInstant()) {
                        effectinstance.getPotion().affectEntity(player, player, this, effectinstance.getAmplifier(), 1.0D);
                    } else {
                        this.addPotionEffect(new EffectInstance(effectinstance));
                    }
                    this.addAffection(effectinstance.getPotion().isBeneficial() ? 0.05 : -0.075);
                }
                this.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 0.8F, 0.8F + this.world.rand.nextFloat() * 0.4F);
                if (!player.isCreative()) {
                    player.setHeldItem(hand, new ItemStack(Items.GLASS_BOTTLE));
                }
                return ActionResultType.SUCCESS;
            } else if (heldstacks.getItem() == registerItems.OATHRING.get()) {
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
            } else if (heldstacks.getItem() == registerItems.ENERGY_DRINK_DEBUG.get()) {
                this.setMorale(150);
                if (!player.isCreative()) {
                    heldstacks.shrink(1);
                }
            } else if (heldstacks.getItem() instanceof ItemBandage) {
                if (this.getHealth() < this.getMaxHealth()) {
                    if (!this.world.isRemote) {
                        this.doHeal(1.0f);
                        if (!player.isCreative()) {
                            if (heldstacks.attemptDamageItem(1, MathUtil.getRand(), (ServerPlayerEntity) player)) {
                                heldstacks.shrink(1);
                            }
                        }
                        this.playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.8F, 0.8F + this.world.rand.nextFloat() * 0.4F);
                    }
                    return ActionResultType.SUCCESS;
                }
            } else if (heldstacks.getItem() instanceof ItemCommandStick) {
                CompoundNBT compound = heldstacks.getOrCreateTag();
                if (player.isSneaking()) {
                    if(!this.getEntityWorld().isRemote()) {
                        if (compound.contains("BedX") && compound.contains("BedY") && compound.contains("BedZ")) {
                            BlockPos BedPos = new BlockPos(compound.getInt("BedX"), compound.getInt("BedY"), compound.getInt("BedZ"));
                            if (this.getEntityWorld().getBlockState(BedPos).isBed(this.getEntityWorld(), BedPos, this)) {
                                if(this.getOwner() instanceof PlayerEntity) {

                                    ProjectAzurePlayerCapability cap = ProjectAzurePlayerCapability.getCapability((PlayerEntity)this.getOwner());
                                    boolean isBedDuplicate = false;
                                    @Nullable
                                    AbstractEntityCompanion duplicatedComp = null;
                                    for(AbstractEntityCompanion companion:cap.getCompanionList()){
                                        if(companion.getDataManager().get(HOMEPOS).isPresent()){
                                            BlockPos pos = companion.getDataManager().get(HOMEPOS).get();
                                            if(pos.getX() == compound.getInt("BedX") && pos.getY() == compound.getInt("BedY") && pos.getZ() == compound.getInt("BedZ")){
                                                isBedDuplicate = true;
                                                duplicatedComp = companion;
                                                break;
                                            }
                                        }
                                    }
                                    if(duplicatedComp == this){
                                        player.sendStatusMessage(new TranslationTextComponent("message.commandstick.entity_bedpos_same_companion_same_bed", "[" + BedPos.getX() + ", " + BedPos.getY() + ", " + BedPos.getZ() + "]"), true);
                                    }
                                    else if(isBedDuplicate){
                                        player.sendStatusMessage(new TranslationTextComponent("message.commandstick.entity_bedpos_failed_duplicate", "[" + BedPos.getX() + ", " + BedPos.getY() + ", " + BedPos.getZ() + "]", this.getDisplayName(), duplicatedComp.getDisplayName()), true);
                                    }
                                    else {
                                        this.clearHomePos();
                                        this.setHomePos(BedPos);
                                        player.sendStatusMessage(new TranslationTextComponent("message.commandstick.entity_bedpos_applied", "[" + BedPos.getX() + ", " + BedPos.getY() + ", " + BedPos.getZ() + "]", this.getDisplayName()), true);
                                    }
                                }
                            } else {
                                player.sendStatusMessage(new TranslationTextComponent("message.commandstick.entity_bedpos_failed", "["+ BedPos.getX()+", "+ BedPos.getY()+", "+ BedPos.getZ() +"]", this.getDisplayName()), true);
                            }
                        } else if (this.hasHomePos()) {
                            this.clearHomePos();
                            player.sendStatusMessage(new TranslationTextComponent("message.commandstick.entity_bedpos_cleared", this.getDisplayName()), true);
                        }
                    }
                    return ActionResultType.CONSUME;
                }
            } else {
                if(player.isSneaking()) {
                    if (!this.world.isRemote) {
                        this.openGUI((ServerPlayerEntity) player);
                    }
                    Main.PROXY.setSharedMob(this);
                    return ActionResultType.SUCCESS;
                }

                //check is player is looking at Head
                Vector3d PlayerLook = player.getLook(1.0F).normalize();
                float eyeHeight = (float) this.getPosYEye();


                Vector3d EyeDelta = new Vector3d(this.getPosX() - player.getPosX(), eyeHeight - player.getPosYEye(), this.getPosZ() - player.getPosZ());
                double EyeDeltaLength = EyeDelta.length();
                EyeDelta = EyeDelta.normalize();
                double EyeCheckFinal = PlayerLook.dotProduct(EyeDelta);

                //check is player is looking at leg
                Vector3d LegDelta = new Vector3d(this.getPosX() - player.getPosX(), this.getPosY() + 0.4 - player.getPosYEye(), this.getPosZ() - player.getPosZ());
                double LegDeltaLength = LegDelta.length();
                LegDelta = LegDelta.normalize();
                double LegCheckFinal = PlayerLook.dotProduct(LegDelta);

                if (this.isSleeping()) {
                    if (this.forceWakeupCounter > 4) {
                        this.forceWakeup();
                    } else {
                        this.forceWakeupCounter++;
                        this.forcewakeupExpireTimer = 20;
                    }
                    return ActionResultType.SUCCESS;
                } else if (EyeCheckFinal > 1.0D - 0.025D / EyeDeltaLength && player.getHeldItemMainhand() == ItemStack.EMPTY && getDistanceSq(player) < 4.0F) {
                    if (this.getEntityWorld().isRemote()) {
                        Main.NETWORK.sendToServer(new EntityInteractionPacket(this.getEntityId(), EntityInteractionPacket.EntityBehaviorType.PAT, true));
                    }
                    return ActionResultType.SUCCESS;
                } else if (LegCheckFinal > 1.0D - 0.015D / LegDeltaLength) {
                    this.SwitchSittingStatus();
                    return ActionResultType.SUCCESS;
                    //this.func_233687_w_(!this.isSitting());
                }
            }
            return ActionResultType.FAIL;
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

    private boolean hasHomePos() {
        return this.getDataManager().get(HOMEPOS).isPresent() && this.getDataManager().get(VALID_HOME_DISTANCE)>0;
    }

    private void SwitchSittingStatus() {
        boolean value = !this.isSitting();
        this.func_233687_w_(value);
        if(this.getEntityWorld().isRemote()) {
            Main.NETWORK.sendToServer(new EntityInteractionPacket(this.getEntityId(), EntityInteractionPacket.EntityBehaviorType.SIT, value));
        }
    }

    public void doHeal(float amount) {
        this.heal(amount);
        this.addAffection(0.12F);
        this.getDataManager().set(HEAL_TIMER, 50);
        if(!this.getEntityWorld().isRemote()) {
            Main.NETWORK.send(TRACKING_ENTITY_AND_SELF.with(() -> this), new spawnParticlePacket(this, spawnParticlePacket.Particles.AFFECTION_HEART));
        }
    }

    @Override
    public void tick() {
        super.tick();
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
        if (!this.world.isRemote && this.ticksExisted%10 == 0) {
            double waterheight = this.func_233571_b_(FluidTags.WATER);
            boolean rigging = !this.canUseRigging();
            if (this.isServerWorld() && waterheight > 0.9&&rigging) {
                this.navigator = this.swimmingNav;
                this.moveController = this.SwimController;
                this.setSwimming(true);
            }else {
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

    @Override
    public boolean canBeLeashedTo(PlayerEntity player) {
        if(this.getOwner() == player){
            player.sendStatusMessage(new TranslationTextComponent("message.easteregg.leash"), true);
        }
        return false;
    }

    @Override
    protected void setDead() {
        if(this.getOwner() != null && this.getOwner() instanceof PlayerEntity){
            ProjectAzurePlayerCapability cap = ProjectAzurePlayerCapability.getCapability((PlayerEntity) this.getOwner());
            cap.removeCompanion(this);
        }
        super.setDead();
    }

    protected abstract void openGUI(ServerPlayerEntity player);

    public void beingpatted(){
        if(this.getDataManager().get(PAT_ANIMATION_TIME) == 0 || this.dataManager.get(MAXPATEFFECTCOUNT) == 0){
            this.dataManager.set(MAXPATEFFECTCOUNT, 5+this.rand.nextInt(3));
        }
        this.getDataManager().set(PAT_ANIMATION_TIME, 25);
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
        Main.NETWORK.sendToServer(new EntityInteractionPacket(this.getEntityId(), EntityInteractionPacket.EntityBehaviorType.ITEMPICKUP, !this.shouldPickupItem()));
    }

    public void SwitchFreeRoamingStatus() {
        Main.NETWORK.sendToServer(new EntityInteractionPacket(this.getEntityId(), EntityInteractionPacket.EntityBehaviorType.HOMEMODE, !this.isFreeRoaming()));
    }

    public void setForceWaken(boolean value){
        this.getDataManager().set(ISFORCEWOKENUP, value);
    }

    public boolean isForceWaken(){
        return this.getDataManager().get(ISFORCEWOKENUP);
    }

    public void forceWakeup(){
        if(this.isSleeping()) {
            this.shouldBeSitting = false;
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

    @Nonnull
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

    public void func_233687_w_(boolean val) {
        this.getDataManager().set(SITTING, val);
        super.func_233687_w_(val);
        this.recalculateSize();
    }

    @Nullable
    @Override
    public ModifiableAttributeInstance getAttribute(Attribute attribute) {
        return super.getAttribute(attribute);
    }
}
