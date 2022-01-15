package com.yor42.projectazure.gameobject.entity.companion;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.capability.ProjectAzurePlayerCapability;
import com.yor42.projectazure.gameobject.entity.CompanionDefaultMovementController;
import com.yor42.projectazure.gameobject.entity.CompanionGroundPathNavigator;
import com.yor42.projectazure.gameobject.entity.CompanionSwimPathFinder;
import com.yor42.projectazure.gameobject.entity.CompanionSwimPathNavigator;
import com.yor42.projectazure.gameobject.entity.ai.goals.*;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenAircraftCarrier;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenBase;
import com.yor42.projectazure.gameobject.entity.companion.magicuser.ISpellUser;
import com.yor42.projectazure.gameobject.entity.misc.AbstractEntityDrone;
import com.yor42.projectazure.gameobject.items.tools.ItemBandage;
import com.yor42.projectazure.gameobject.items.ItemCannonshell;
import com.yor42.projectazure.gameobject.items.tools.ItemCommandStick;
import com.yor42.projectazure.gameobject.items.ItemMagazine;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
import com.yor42.projectazure.gameobject.misc.DamageSources;
import com.yor42.projectazure.interfaces.IAknOp;
import com.yor42.projectazure.interfaces.IAttributeable;
import com.yor42.projectazure.interfaces.IAzurLaneKansen;
import com.yor42.projectazure.intermod.SolarApocalypse;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.utils.BlockStateUtil;
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
import net.minecraft.entity.ai.attributes.*;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.TurtleEntity;
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
import net.minecraft.pathfinding.*;
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
import software.bernie.geckolib3.core.event.ParticleKeyFrameEvent;
import software.bernie.geckolib3.core.event.SoundKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;
import software.bernie.shadowed.eliotlash.mclib.utils.MathUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

import static com.yor42.projectazure.libs.utils.BlockStateUtil.RelativeDirection.FRONT;
import static com.yor42.projectazure.libs.utils.ItemStackUtils.*;
import static net.minecraft.util.Hand.MAIN_HAND;
import static net.minecraft.util.Hand.OFF_HAND;
import static net.minecraftforge.fml.network.PacketDistributor.TRACKING_ENTITY_AND_SELF;

public abstract class AbstractEntityCompanion extends TameableEntity implements IAnimatable, IAttributeable {
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
            AbstractEntityCompanion.this.setItemSlot(slottype, stack);
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
            return AbstractEntityCompanion.this.getItemBySlot(slottype);
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

            if(slot >= 12){
                return AbstractEntityCompanion.this.isSkillItem(stack);
            }

            return super.isItemValid(slot, stack);
        }

        @Override
        protected void onContentsChanged(int slot) {
            switch (slot){
                case 12:
                    AbstractEntityCompanion.this.getEntityData().set(SKILL_ITEM_0, this.getStackInSlot(slot));
                    break;
                case 13:
                    ItemStack stack = this.getStackInSlot(slot);
                    AbstractEntityCompanion.this.getEntityData().set(SKILL_ITEM_1, stack);
                    break;
                case 14:
                    AbstractEntityCompanion.this.getEntityData().set(SKILL_ITEM_2, this.getStackInSlot(slot));
                    break;
                case 15:
                    AbstractEntityCompanion.this.getEntityData().set(SKILL_ITEM_3, this.getStackInSlot(slot));
                    break;
            }
        }

        @Override
        protected void onLoad() {
            super.onLoad();
            for(int i=0; i<AbstractEntityCompanion.this.getSkillItemCount(); i++){
                DataParameter<ItemStack>[] stacks = new DataParameter[]{SKILL_ITEM_0,SKILL_ITEM_1,SKILL_ITEM_2,SKILL_ITEM_3};
                AbstractEntityCompanion.this.getEntityData().set(stacks[i], this.getStackInSlot(12+i));
            }
        }
    };
    public ItemStackHandler AmmoStorage = new ItemStackHandler(0){
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {

            return stack.getItem() instanceof ItemCannonshell || stack.getItem() instanceof ItemMagazine;
        }
    };

    protected int patTimer, shieldCoolDown, qinteractionTimer;
    public boolean isClimbingUp = false;
    private int ItemSwapIndexOffhand = -1;
    private int ItemSwapIndexMainHand = -1;
    protected int lastAggroedTimeStamp = 0;
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
    private final PathNavigator sailingNav;
    protected CompanionFoodStats foodStats = new CompanionFoodStats();
    private List<ExperienceOrbEntity> nearbyExpList;

    protected long lastSlept, lastWokenup;
    private int forcewakeupExpireTimer, forceWakeupCounter, expdelay;
    protected int StartedMeleeAttackTimeStamp = -1;
    protected int AttackCount = 0;
    protected int StartedSpellAttackTimeStamp = -1;

    //I'd really like to get off from datamanager's wild ride.
    protected static final DataParameter<Integer> SPELLDELAY = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.INT);
    protected static final DataParameter<Integer> NONVANILLAMELEEATTACKDELAY = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.INT);
    protected static final DataParameter<Integer> SKILLDELAYTICK = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.INT);
    protected static final DataParameter<Boolean> EATING = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> USING_SKILL = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Float> MORALE = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.FLOAT);
    protected static final DataParameter<Float> EXP = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.FLOAT);
    protected static final DataParameter<Integer> LIMITBREAKLEVEL = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.INT);
    protected static final DataParameter<Integer> LEVEL = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.INT);
    protected static final DataParameter<Integer> ANGRYTIMER = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.INT);
    protected static final DataParameter<Float> AFFECTION = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.FLOAT);
    protected static final DataParameter<Boolean> SITTING = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> OPENINGDOOR = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> MELEEATTACKING = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> USINGBOW = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Integer> MAXPATEFFECTCOUNT = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.INT);
    protected static final DataParameter<Integer> PATEFFECTCOUNT = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.INT);
    protected static final DataParameter<Integer> PATCOOLDOWN = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.INT);
    protected static final DataParameter<Integer> PAT_ANIMATION_TIME = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.INT);
    protected static final DataParameter<Integer> QUESTIONABLE_INTERACTION_ANIMATION_TIME = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.INT);
    protected static final DataParameter<Integer> INTERACTION_WARNING_COUNT = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.INT);
    protected static final DataParameter<Boolean> OATHED = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> PICKUP_ITEM = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> ISFREEROAMING = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Optional<BlockPos>> STAYPOINT = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.OPTIONAL_BLOCK_POS);
    protected static final DataParameter<Optional<BlockPos>> HOMEPOS = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.OPTIONAL_BLOCK_POS);
    protected static final DataParameter<Float> VALID_HOME_DISTANCE = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.FLOAT);
    protected static final DataParameter<Boolean> ISFORCEWOKENUP = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> ISUSINGGUN = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Integer> HEAL_TIMER = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.INT);
    protected static final DataParameter<ItemStack> SKILL_ITEM_0 = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.ITEM_STACK);
    protected static final DataParameter<ItemStack> SKILL_ITEM_1 = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.ITEM_STACK);
    protected static final DataParameter<ItemStack> SKILL_ITEM_2 = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.ITEM_STACK);
    protected static final DataParameter<ItemStack> SKILL_ITEM_3 = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.ITEM_STACK);
    protected static final DataParameter<Integer> RELOAD_TIMER_MAINHAND = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.INT);
    protected static final DataParameter<Integer> RELOAD_TIMER_OFFHAND = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.INT);
    protected static final DataParameter<Integer> FOODLEVEL = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.INT);
    public abstract enums.EntityType getEntityType();
    protected AbstractEntityCompanion(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
        this.setAffection(40F);
        this.swimmingNav = new CompanionSwimPathNavigator(this, worldIn);
        this.groundNav = new CompanionGroundPathNavigator(this, worldIn);
        this.SwimController = new CompanionSwimPathFinder(this);
        this.MoveController = new CompanionDefaultMovementController(this);
        this.sailingNav = new SwimmerPathNavigator(this, worldIn){
            protected PathFinder createPathFinder(int p_179679_1_) {
                this.nodeEvaluator = new WalkAndSwimNodeProcessor();
                return new PathFinder(this.nodeEvaluator, p_179679_1_);
            }
            public boolean isStableDestination(BlockPos p_188555_1_) {
                return !this.level.getBlockState(p_188555_1_.below()).isAir();
            }
        };
    }



    public void setOathed(boolean bool){
        this.entityData.set(OATHED, bool);
    }

    public boolean isOathed(){
        return this.entityData.get(OATHED);
    }

    public int getLevel() {
        return this.getEntityData().get(LEVEL);
    }

    public void setLevel(int level) {
        this.getEntityData().set(LEVEL, level);
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
        this.getEntityData().set(ISUSINGGUN, val);
    }

    public boolean isUsingGun(){
        return this.getEntityData().get(ISUSINGGUN);
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
        return  !(this.animationSpeed > -0.1F && this.animationSpeed < 0.1F) && !this.isOrderedToSit();
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
        return this.isBlocking()? SoundEvents.SHIELD_BLOCK:super.getHurtSound(damageSourceIn);
    }

    @Override
    protected void hurtArmor(DamageSource damageSource, float damage) {
        if (damage >= 0.0F) {
            damage = damage / 4.0F;
            if (damage < 1.0F) {
                damage = 1.0F;
            }
            for (int i = 0; i < this.getInventory().getSlots(); ++i) {
                ItemStack itemstack = this.getInventory().getStackInSlot(i);
                if ((!damageSource.isFire() || !itemstack.getItem().isFireResistant()) && itemstack.getItem() instanceof ArmorItem) {
                    int j = i;
                    itemstack.hurtAndBreak((int) damage, this, (p_214023_1_) -> p_214023_1_.broadcastBreakEvent(EquipmentSlotType.byTypeAndIndex(EquipmentSlotType.Group.ARMOR, j)));
                }
            }
        }
    }

    @Override
    public void addAdditionalSaveData(@Nonnull CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("spelldelay", this.getEntityData().get(SPELLDELAY));
        compound.putFloat("affection", this.entityData.get(AFFECTION));
        compound.putInt("patcooldown", this.entityData.get(PATCOOLDOWN));
        compound.putInt("angrytimer", this.entityData.get(ANGRYTIMER));
        compound.putBoolean("oathed", this.entityData.get(OATHED));
        compound.putBoolean("pickupitem", this.entityData.get(PICKUP_ITEM));
        compound.putFloat("home_distance", this.entityData.get(VALID_HOME_DISTANCE));
        compound.putBoolean("issitting", this.entityData.get(SITTING));
        if(this.entityData.get(STAYPOINT).isPresent()) {
            compound.putDouble("stayX", this.entityData.get(STAYPOINT).get().getX());
            compound.putDouble("stayY", this.entityData.get(STAYPOINT).get().getY());
            compound.putDouble("stayZ", this.entityData.get(STAYPOINT).get().getZ());
        }
        if(this.RECRUIT_BEACON_POS != null) {
            compound.putDouble("RecruitStationPosX", this.RECRUIT_BEACON_POS.getX());
            compound.putDouble("RecruitStationPosY", this.RECRUIT_BEACON_POS.getY());
            compound.putDouble("RecruitStationPosZ", this.RECRUIT_BEACON_POS.getZ());
        }
        if(this.entityData.get(HOMEPOS).isPresent()) {
            compound.putDouble("HomePosX", this.entityData.get(HOMEPOS).get().getX());
            compound.putDouble("HomePosY", this.entityData.get(HOMEPOS).get().getY());
            compound.putDouble("HomePosZ", this.entityData.get(HOMEPOS).get().getZ());
        }
        compound.putBoolean("eating", this.entityData.get(EATING));
        compound.putBoolean("shouldbeSitting", this.shouldBeSitting);
        compound.putBoolean("isforcewokenup", this.entityData.get(ISFORCEWOKENUP));
        compound.putDouble("exp", this.entityData.get(EXP));
        compound.putInt("level", this.getEntityData().get(LEVEL));
        compound.putInt("limitbreaklv", this.entityData.get(LIMITBREAKLEVEL));
        compound.putInt("awaken", this.awakeningLevel);
        compound.putBoolean("isSitting", this.isOrderedToSit());
        compound.putBoolean("freeroaming", this.isFreeRoaming());
        compound.putDouble("morale", this.entityData.get(MORALE));
        compound.putLong("lastslept", this.lastSlept);
        compound.putLong("lastwoken", this.lastWokenup);
        compound.putBoolean("isMovingtoRecruitStation", this.isMovingtoRecruitStation);
        compound.put("inventory", this.getInventory().serializeNBT());
        compound.putInt("shieldcooldown", this.shieldCoolDown);
        compound.put("ammostorage", this.getAmmoStorage().serializeNBT());
        this.foodStats.write(compound);
        compound.putInt("attackdelay", this.getEntityData().get(NONVANILLAMELEEATTACKDELAY));
        compound.putInt("SwapIndexMainHand", this.ItemSwapIndexMainHand);
        compound.putInt("SwapIndexOffHand", this.ItemSwapIndexOffhand);
    }

    public void readAdditionalSaveData(@Nonnull CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        this.getEntityData().set(SPELLDELAY, compound.getInt("spelldelay"));
        this.entityData.set(AFFECTION, compound.getFloat("affection"));
        this.entityData.set(PATCOOLDOWN, compound.getInt("patcooldown"));
        this.entityData.set(OATHED, compound.getBoolean("oathed"));
        this.getEntityData().set(NONVANILLAMELEEATTACKDELAY, compound.getInt("attackdelay"));
        this.entityData.set(PICKUP_ITEM, compound.getBoolean("pickupitem"));
        this.setOrderedToSit(compound.getBoolean("isSitting"));
        boolean hasStayPos = compound.contains("stayX") && compound.contains("stayY") && compound.contains("stayZ");
        if(hasStayPos) {
            this.entityData.set(STAYPOINT, Optional.of(new BlockPos(compound.getDouble("stayX"), compound.getDouble("stayY"), compound.getDouble("stayZ"))));
        }
        this.entityData.set(VALID_HOME_DISTANCE, compound.getFloat("home_distance"));
        boolean hasRecruitBeaconPos = compound.contains("RecruitStationPosX") && compound.contains("RecruitStationPosY") && compound.contains("RecruitStationPosZ");
        if(hasRecruitBeaconPos) {
            this.RECRUIT_BEACON_POS = new BlockPos(compound.getDouble("RecruitStationPosX"), compound.getDouble("RecruitStationPosY"), compound.getDouble("RecruitStationPosZ"));
        }

        boolean hasHomePos = compound.contains("HomePosX") && compound.contains("HomePosY") && compound.contains("HomePosZ");
        if(hasHomePos) {
            this.entityData.set(HOMEPOS, Optional.of(new BlockPos(compound.getDouble("HomePosX"), compound.getDouble("HomePosY"), compound.getDouble("HomePosZ"))));
        }
        this.entityData.set(ISFORCEWOKENUP, compound.getBoolean("isforcewokenup"));
        this.shouldBeSitting = compound.getBoolean("shouldbeSitting");
        this.getEntityData().set(LEVEL, compound.getInt("level"));
        this.entityData.set(EXP, compound.getFloat("exp"));
        this.entityData.set(MORALE, compound.getFloat("morale"));
        this.entityData.set(ANGRYTIMER, compound.getInt("angrytimer"));
        this.entityData.set(SITTING, compound.getBoolean("issitting"));
        this.entityData.set(LIMITBREAKLEVEL, compound.getInt("limitbreaklv"));
        this.entityData.set(EATING, compound.getBoolean("eating"));
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

    public float getAttackDamage(){
        ItemStack stack = this.getItemInHand(Hand.MAIN_HAND);
        Item item = stack.getItem();
        if(item instanceof TieredItem){
            Float dmg;
            if(item instanceof SwordItem){
                dmg =((SwordItem) item).getDamage();
            }
            else {
                dmg = ((TieredItem) item).getTier().getAttackDamageBonus();
            }
            float basedmg = this.getAttribute(Attributes.ATTACK_DAMAGE) == null? 0: (float) this.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
            return basedmg+dmg;
        }

        return 1F;
    }

    public int getAngerWarningCount(){
        return this.entityData.get(INTERACTION_WARNING_COUNT);
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
    public boolean wantsToAttack(LivingEntity target, LivingEntity owner) {

        if(target instanceof TameableEntity){
            if(((TameableEntity) target).isOwnedBy(owner)){
                return false;
            }
        }
        else if(target instanceof PlayerEntity){
            return !this.isOwnedBy(target) && this.isPVPenabled();
        }
        if(target instanceof AbstractEntityDrone && ((AbstractEntityDrone)target).getOwner().isPresent() && (((AbstractEntityDrone) target).getOwner().get() == this ||  (((AbstractEntityDrone) target).getOwner().get() instanceof AbstractEntityCompanion && this.getOwner() != null && ((AbstractEntityCompanion) ((AbstractEntityDrone) target).getOwner().get()).isOwnedBy(this.getOwner())))){
            return false;
        }

        return super.wantsToAttack(target, owner);
    }

    public void setHomeposAndDistance(BlockPos pos, float validDistance){
        this.getEntityData().set(HOMEPOS, Optional.of(pos));
        this.entityData.set(VALID_HOME_DISTANCE, validDistance);
    }

    public void clearHomePos(){
        this.getEntityData().set(HOMEPOS, Optional.empty());
        this.entityData.set(VALID_HOME_DISTANCE, -1.0f);
    }


    public Optional<BlockPos> getRecruitStationPos() {
        return Optional.ofNullable(this.RECRUIT_BEACON_POS);
    }

    public void setRecruitStationPos(BlockPos pos){
        this.RECRUIT_BEACON_POS= pos;
    }

    public float getHomeDistance(){
        return this.entityData.get(VALID_HOME_DISTANCE);
    }

    public boolean isInHomeRange(BlockPos Startpos){
        if(this.getHomeDistance() == -1.0f || !this.getHOMEPOS().isPresent()){
            return false;
        }
        else{
            return Startpos.closerThan(this.getHOMEPOS().get(), this.getHomeDistance());
        }
    }

    public Optional<BlockPos> getHOMEPOS(){
        return this.getEntityData().get(HOMEPOS);
    }

    public void setHomePos(BlockPos pos){
        this.setHomeposAndDistance(pos, 64);
    }

    @Override
    protected void doPush(@Nonnull Entity entityIn) {
        super.doPush(entityIn);
    }

    public boolean isInHomeRangefromCurrenPos(){
        return this.isInHomeRange(this.blockPosition());
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
            ((ItemGunBase) gun.getItem()).shootGunCompanion(gun, this.getCommandSenderWorld(), this, false, HandIn, target);
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
        return stack1.getItem() == stack2.getItem() && ItemStack.tagMatches(stack1, stack2);
    }

    public boolean isEating(){
        return this.getEntityData().get(EATING);
    }

    public void setEating(boolean value){
        this.entityData.set(EATING, value);
    }

    public boolean isFreeRoaming() {
        return this.entityData.get(ISFREEROAMING);
    }

    public void setFreeRoaming(boolean value) {
        this.getNavigation().stop();
        this.entityData.set(ISFREEROAMING, value);
        if(value) {
            this.setStayCenterPos(this.blockPosition());
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
    public AgeableEntity getBreedOffspring(@Nonnull ServerWorld p_241840_1_, @Nonnull AgeableEntity p_241840_2_) {
        return null;
    }

    /*
    No. just No.
     */
    @Override
    public boolean canMate(@Nonnull AnimalEntity otherAnimal) {
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
        float chance = 0.25F + (float) EnchantmentHelper.getBlockEfficiency(this) * 0.05F;
        if (increase)
            chance += 0.75;
        if (this.random.nextFloat() < chance) {
            this.shieldCoolDown = 100;
            this.releaseUsingItem();
            this.level.broadcastEntityEvent(this, (byte) 30);
        }
    }

    @Override
    protected void hurtCurrentlyUsedShield(float damage) {
        if (this.useItem.isShield(this)) {
            if (damage >= 3.0F) {
                int i = (int) (1 + Math.floor(damage));
                Hand hand = this.getUsedItemHand();
                this.useItem.hurtAndBreak(i, this, (entity) -> entity.broadcastBreakEvent(hand));
                if (this.useItem.isEmpty()) {
                    if (hand == MAIN_HAND) {
                        this.setItemSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
                    } else {
                        this.setItemSlot(EquipmentSlotType.OFFHAND, ItemStack.EMPTY);
                    }
                    this.useItem = ItemStack.EMPTY;
                    this.playSound(SoundEvents.SHIELD_BREAK, 0.8F, 0.8F + this.getRandom().nextFloat() * 0.4F);
                }
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {

        if(source.getEntity() instanceof TameableEntity && this.getOwner() != null && ((TameableEntity) source.getEntity()).isOwnedBy(this.getOwner())){
            return false;
        }

        if(source.getEntity() instanceof AbstractEntityDrone && ((AbstractEntityDrone) source.getEntity()).getOwner().isPresent() && (((AbstractEntityDrone) source.getEntity()).getOwner().get() == this ||  (((AbstractEntityDrone) source.getEntity()).getOwner().get() instanceof AbstractEntityCompanion && this.getOwner() != null && ((AbstractEntityCompanion) ((AbstractEntityDrone) source.getEntity()).getOwner().get()).isOwnedBy(this.getOwner())))){
            return false;
        }

        return super.hurt(source, amount);
    }

    public int getShieldCoolDown(){
        return this.shieldCoolDown;
    }

    @Override
    protected void blockUsingShield(LivingEntity entityIn) {
        super.blockUsingShield(entityIn);
        if (entityIn.getMainHandItem().canDisableShield(this.useItem, this, entityIn))
            this.disableShield(true);
    }

    @Override
    public void startUsingItem(Hand hand) {
        ItemStack itemstack = this.getItemInHand(hand);
        if (itemstack.isShield(this)) {
            ModifiableAttributeInstance modifiableattributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
            modifiableattributeinstance.removeModifier(USE_ITEM_SPEED_PENALTY);
            modifiableattributeinstance.addTransientModifier(USE_ITEM_SPEED_PENALTY);
        }
        super.startUsingItem(hand);
    }

    @Override
    public void releaseUsingItem() {
        if (this.getAttribute(Attributes.MOVEMENT_SPEED).hasModifier(USE_ITEM_SPEED_PENALTY))
            this.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(USE_ITEM_SPEED_PENALTY);
        super.releaseUsingItem();
    }

    public boolean isPushedByFluid() {
        return !this.isSwimming();
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    protected final AnimationFactory factory = new AnimationFactory(this);

    public boolean isAngry(){
        return this.entityData.get(ANGRYTIMER)>0;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        AnimationController UpperBodycontroller = new AnimationController<>(this, "controller_lowerbody", 7, this::predicate_lowerbody);
        animationData.addAnimationController(UpperBodycontroller);
        animationData.addAnimationController(new AnimationController<>(this, "controller_upperbody", 7, this::predicate_upperbody));
        animationData.addAnimationController(new AnimationController<>(this, "controller_head", 1, this::predicate_head));

        UpperBodycontroller.registerSoundListener(this::soundListener);
        UpperBodycontroller.registerParticleListener(this::particleListener);
    }

    protected void particleListener(ParticleKeyFrameEvent particleKeyFrameEvent) {
    }

    protected void soundListener(SoundKeyframeEvent event) {
    }

    protected abstract <P extends IAnimatable> PlayState predicate_upperbody(AnimationEvent<P> pAnimationEvent);

    protected abstract <P extends IAnimatable> PlayState predicate_head(AnimationEvent<P> pAnimationEvent);

    public boolean isMeleeing(){
        return this.entityData.get(MELEEATTACKING);
    }

    protected abstract <E extends IAnimatable> PlayState predicate_lowerbody(AnimationEvent<E> event);

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(SPELLDELAY, 0);
        this.entityData.define(SKILLDELAYTICK, 0);
        this.entityData.define(EXP, 0.0F);
        this.entityData.define(LIMITBREAKLEVEL, 0);
        this.entityData.define(MORALE, 0.0F);
        this.entityData.define(EATING, false);
        this.entityData.define(LEVEL, 0);
        this.entityData.define(AFFECTION, 0.0F);
        this.entityData.define(SITTING, false);
        this.entityData.define(OPENINGDOOR, false);
        this.entityData.define(MELEEATTACKING, false);
        this.entityData.define(MAXPATEFFECTCOUNT, 0);
        this.entityData.define(PATEFFECTCOUNT, 0);
        this.getEntityData().define(NONVANILLAMELEEATTACKDELAY, 0);
        this.entityData.define(PATCOOLDOWN, 0);
        this.entityData.define(PAT_ANIMATION_TIME, 0);
        this.entityData.define(OATHED, false);
        this.entityData.define(USINGBOW, false);
        this.entityData.define(USING_SKILL, false);
        this.entityData.define(STAYPOINT, Optional.empty());
        this.entityData.define(HOMEPOS, Optional.empty());
        this.entityData.define(ISFORCEWOKENUP, false);
        this.entityData.define(ISUSINGGUN, false);
        this.entityData.define(ISFREEROAMING, false);
        this.entityData.define(VALID_HOME_DISTANCE, -1.0f);
        this.entityData.define(HEAL_TIMER, 0);
        this.entityData.define(RELOAD_TIMER_MAINHAND, 0);
        this.entityData.define(RELOAD_TIMER_OFFHAND, 0);
        this.entityData.define(PICKUP_ITEM, false);
        this.entityData.define(FOODLEVEL, 0);
        this.entityData.define(ANGRYTIMER, 0);
        this.entityData.define(QUESTIONABLE_INTERACTION_ANIMATION_TIME, 0);
        this.entityData.define(INTERACTION_WARNING_COUNT, 0);
        this.entityData.define(SKILL_ITEM_0, ItemStack.EMPTY);
        this.entityData.define(SKILL_ITEM_1, ItemStack.EMPTY);
        this.entityData.define(SKILL_ITEM_2, ItemStack.EMPTY);
        this.entityData.define(SKILL_ITEM_3, ItemStack.EMPTY);
    }

    public void setOpeningdoor(boolean openingdoor){
        this.isOpeningDoor = openingdoor;
        this.entityData.set(OPENINGDOOR, this.isOpeningDoor);
    }

    public int getSpellDelay(){
        return this.getEntityData().get(SPELLDELAY);
    }

    public void setSpellDelay(int value) {
        this.getEntityData().set(SPELLDELAY, value);
    }

    public boolean isUsingSpell(){
        return this.getSpellDelay()>0;
    }

    public boolean shouldPickupItem(){
        return this.entityData.get(PICKUP_ITEM);
    }

    public void setPickupItem(boolean value){
        this.entityData.set(PICKUP_ITEM, value);
    }

    public boolean isOpeningDoor() {
        return this.entityData.get(OPENINGDOOR);
    }

    public void setUsingbow(boolean usingbow){
        this.entityData.set(USINGBOW, usingbow);
    }

    public boolean isUsingBow() {
        return this.entityData.get(USINGBOW);
    }

    public void setMeleeing(boolean attacking) {
        this.isMeleeing = attacking;
        this.entityData.set(MELEEATTACKING, this.isMeleeing);

    }

    public void setMovingtoRecruitStation(BlockPos pos){
        this.isMovingtoRecruitStation=true;
        this.setRecruitStationPos(pos);
    }

    public void stopMovingtoRecruitStation(){
        this.getNavigation().stop();
        this.isMovingtoRecruitStation = false;
        this.RECRUIT_BEACON_POS= null;
    }

    public boolean canEat(boolean ignoreHunger) {
        return ignoreHunger || this.foodStats.needFood();
    }

    public void addExhaustion(float exhaustion) {
        if (!this.level.isClientSide) {
            this.foodStats.addExhaustion(exhaustion);
        }
    }

    @MethodsReturnNonnullByDefault
    public ItemStack eat(World WorldIn, ItemStack foodStack) {
        this.getFoodStats().consume(foodStack.getItem(), foodStack);
        if(!this.getCommandSenderWorld().isClientSide()) {
            Main.NETWORK.send(TRACKING_ENTITY_AND_SELF.with(() -> this), new spawnParticlePacket(this, foodStack));
        }
        WorldIn.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.GENERIC_EAT, SoundCategory.PLAYERS, 0.5F, WorldIn.random.nextFloat() * 0.1F + 0.9F);
        if(foodStack.getItem().isEdible()){
            Food food = foodStack.getItem().getFoodProperties();
            if(food != null) {
                this.addMorale(foodStack.getItem().getFoodProperties().getNutrition() * 4);
            }
        }
        return super.eat(WorldIn, foodStack);
    }

    public CompanionFoodStats getFoodStats() {
        return this.foodStats;
    }

    public boolean shouldHeal() {
        return this.getHealth() > 0.0F && this.getHealth() < this.getMaxHealth();
    }

    public boolean isBeingPatted() {
        return this.getEntityData().get(PAT_ANIMATION_TIME) !=0;
    }

    public double getMaxExp(){
        return 10+(5*this.getLevel());
    }

    public int getLimitBreakLv() {
        return this.entityData.get(LIMITBREAKLEVEL);
    }

    public void setLimitBreakLv(int value) {
        this.entityData.set(LIMITBREAKLEVEL, value);
    }

    public void addLimitBreak(int delta){
        setLimitBreakLv(this.getLimitBreakLv() + delta);
    }

    public void doLimitBreak(){
        this.addLimitBreak(1);
    }

    public float getAffection() {
        return this.getEntityData().get(AFFECTION);
    }

    public void setAffection(float affection){
        this.getEntityData().set(AFFECTION, affection);
    }

    public double getmaxAffection(){
        if(this instanceof IAknOp){
            return 200;
        }
        return this.isOathed()? 200:100;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {

        if(this.isAngry()){
            return null;
        }

        if(this instanceof IAknOp){
            float trust = this.getAffection();
            if(trust>=100){
                return ((IAknOp)this).getAffection3AmbientSounds();
            }
            else if(trust>=50){
                return ((IAknOp)this).getAffection2AmbientSounds();
            }
            else if(trust>=25){
                return ((IAknOp)this).getAffection1AmbientSounds();
            }
            else{
                return ((IAknOp)this).getNormalAmbientSounds();
            }
        }
        else if(this instanceof IAzurLaneKansen){
            IAzurLaneKansen kansen = (IAzurLaneKansen) this;
            switch (kansen.affectionValuetoEnum()){
                case DISAPPOINTED:
                    return kansen.getDisappointedAmbientSound();
                case STRANGER:
                    return kansen.getStrangerAmbientSound();
                case FRIENDLY:
                    return kansen.getFriendlyAmbientSound();
                case CRUSH:
                    return kansen.getLikeAmbientSound();
                case LOVE:
                case OATH:
                    return kansen.getLoveAmbientSound();
            }
        }

        return super.getAmbientSound();
    }

    public void addAffection(double Delta){
        this.setAffection((float) Math.min(this.getAffection() + Delta, this.getmaxAffection()));
    }

    public void MaxFillHunger(){
        this.getFoodStats().addStats(20, 20);
    }

    public void setExp(double exp) {
        this.entityData.set(EXP, (float) exp);
    }

    public double getExp() {
        return this.entityData.get(EXP);
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
            this.playSound(SoundEvents.PLAYER_LEVELUP, 1.0F, 1.0F);
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
        return world.getPoiManager().take(PointOfInterestType.HOME.getPredicate(), (pos) -> this.canReachHomePosition(entity, pos), entity.blockPosition(), 20);
    }

    private boolean canReachHomePosition(AbstractEntityCompanion entity, BlockPos pos) {
        Path path = entity.getNavigation().createPath(pos, PointOfInterestType.HOME.getValidRange());
        return path != null && path.canReach();
    }


    @Override
    public void aiStep() {
        super.aiStep();

        if(this.getOwner() != null && this.getOwner() instanceof PlayerEntity && this.isAlive()){
            ProjectAzurePlayerCapability cap = ProjectAzurePlayerCapability.getCapability((PlayerEntity) this.getOwner());
            if(!cap.getCompanionList().contains(this)){
                cap.addCompanion(this);
            }
        }

        if (this.level.getDifficulty() == Difficulty.PEACEFUL && this.level.getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION)) {
            if (this.getHealth() < this.getMaxHealth() && this.tickCount % 20 == 0) {
                this.heal(1.0F);
            }

            if (this.foodStats.needFood() && this.tickCount % 10 == 0) {
                this.foodStats.setFoodLevel(this.foodStats.getFoodLevel() + 1);
            }
        }
        if (this.shieldCoolDown > 0) {
            --this.shieldCoolDown;
        }


        if(!this.getCommandSenderWorld().isClientSide() && this.tickCount % 200 == 0&& !this.getHOMEPOS().isPresent()){
            Optional<BlockPos> optional = this.findHomePosition((ServerWorld) this.getCommandSenderWorld(), this);
            optional.ifPresent(blockPos -> this.setHomeposAndDistance(blockPos, 64));
        }

        if(this.hasEffect(Effects.HUNGER)){
            EffectInstance hunger = this.getEffect(Effects.HUNGER);
            if((hunger != null ? hunger.getDuration() : 0) >0 && hunger.getEffect().isDurationEffectTick(hunger.getDuration(), hunger.getAmplifier())){
                this.addExhaustion(0.005F * (float)(hunger.getAmplifier() + 1));
            }
        }

        if(this.hasEffect(Effects.SATURATION)){
            EffectInstance Saturation = this.getEffect(Effects.SATURATION);
            if((Saturation != null ? Saturation.getDuration() : 0) >0 && Saturation.getEffect().isDurationEffectTick(Saturation.getDuration(), Saturation.getAmplifier())){
                this.getFoodStats().addStats(Saturation.getAmplifier() + 1, 1.0F);
            }
        }

        this.updateSwingTime();
        this.updateSkillDelay();

        if(this.getEntityData().get(SITTING) != this.isOrderedToSit()) {
            this.setOrderedToSit(this.getEntityData().get(SITTING));
        }

        if(this.expdelay>0){
            this.expdelay--;
        }

        if(this.isSprinting() && !this.isMoving()){
            this.setSprinting(false);
        }

        if(this.nearbyExpList == null || this.tickCount%5 == 0){
            this.nearbyExpList = this.getCommandSenderWorld().getEntitiesOfClass(ExperienceOrbEntity.class, this.getBoundingBox().inflate(2));
        }

        if(!this.nearbyExpList.isEmpty()) {
            for(ExperienceOrbEntity orbEntity: this.nearbyExpList){
                if(this.distanceTo(orbEntity)>1.5) {
                    this.pickupExpOrb(orbEntity);
                }
            }
        }
        int healAnimationTime = this.entityData.get(HEAL_TIMER);
        if(healAnimationTime>0){
            this.entityData.set(HEAL_TIMER, --healAnimationTime);
        }

        if(this.getEntityData().get(PAT_ANIMATION_TIME) >0){


            this.getEntityData().set(PAT_ANIMATION_TIME, this.getEntityData().get(PAT_ANIMATION_TIME)-1);
            if(!this.isAngry()) {
                this.patTimer++;
                this.navigation.stop();
                if (this.patTimer % 30 == 0 && !this.getCommandSenderWorld().isClientSide()) {
                    if (this.entityData.get(PATEFFECTCOUNT) < this.entityData.get(MAXPATEFFECTCOUNT)) {
                        this.entityData.set(PATEFFECTCOUNT, this.entityData.get(PATEFFECTCOUNT) + 1);
                        this.addAffection(0.025);
                        this.addMorale(0.5);
                        Main.NETWORK.send(TRACKING_ENTITY_AND_SELF.with(() -> this), new spawnParticlePacket(this, spawnParticlePacket.Particles.AFFECTION_HEART));

                    } else {
                        this.entityData.set(PATEFFECTCOUNT, this.entityData.get(MAXPATEFFECTCOUNT));
                        if (this.entityData.get(PATCOOLDOWN) == 0) {
                            this.entityData.set(PATCOOLDOWN, (7 + this.random.nextInt(5)) * 1200);
                        }
                        Main.NETWORK.send(TRACKING_ENTITY_AND_SELF.with(() -> this), new spawnParticlePacket(this, spawnParticlePacket.Particles.AFFECTION_SMOKE));
                    }
                }
            }
        }
        else{
            if(patTimer>0) {
                this.patTimer = 0;
            }

            if(this.entityData.get(PATCOOLDOWN) > 0){
                this.entityData.set(PATCOOLDOWN, this.entityData.get(PATCOOLDOWN) -1);
            }
            else{
                this.entityData.set(MAXPATEFFECTCOUNT,0);
                this.entityData.set(PATEFFECTCOUNT,0);
            }
        }

        if(!this.getCommandSenderWorld().isClientSide()) {
            int AngerWarningCount = this.entityData.get(INTERACTION_WARNING_COUNT);
            if(this.getOwner() != null && this.getOwner().isAlive()){
                if (this.getOwner() != null && this.getOwner().isAlive() && this.getEntityData().get(QUESTIONABLE_INTERACTION_ANIMATION_TIME) > 0) {
                    this.lookAt(this.getOwner(), 30F, 30F);
                    this.getLookControl().setLookAt(this.getOwner(), 30, 30);
                    int interactionTime = this.getEntityData().get(QUESTIONABLE_INTERACTION_ANIMATION_TIME);
                    this.getEntityData().set(QUESTIONABLE_INTERACTION_ANIMATION_TIME, interactionTime - 1);
                    this.qinteractionTimer += 1;
                    if (this.qinteractionTimer % 30 == 0) {
                        int newWarningCount = AngerWarningCount+1;
                        this.entityData.set(INTERACTION_WARNING_COUNT,newWarningCount);
                        if (newWarningCount == 1 && this.distanceTo(this.getOwner()) < 5) {
                            this.swing(MAIN_HAND);
                            this.getOwner().hurt(DamageSources.causeRevengeDamage(this), 2F);
                        } else if (newWarningCount >= 3 && this.getSensing().canSee(this.getOwner())) {
                            this.addAffection(-2F);
                            this.setTarget(this.getOwner());
                            this.getEntityData().set(QUESTIONABLE_INTERACTION_ANIMATION_TIME, 0);
                            this.getEntityData().set(ANGRYTIMER, 6000);
                            this.entityData.set(INTERACTION_WARNING_COUNT,0);
                        }
                    }
                }
                else if (this.qinteractionTimer > 0 || AngerWarningCount > 0) {
                    this.qinteractionTimer = 0;
                    this.entityData.set(INTERACTION_WARNING_COUNT,0);
                }
            }else if (this.isAngry() || this.qinteractionTimer > 0 || AngerWarningCount > 0){
                this.qinteractionTimer = 0;
                this.entityData.set(INTERACTION_WARNING_COUNT,0);
                this.entityData.set(ANGRYTIMER,0);
            }


            if(this instanceof IMeleeAttacker) {
                int currentspelldelay = this.getNonVanillaMeleeAttackDelay();
                @Nullable
                LivingEntity target = this.getTarget();
                if (currentspelldelay > 0) {
                    this.getNavigation().stop();
                    if (target == null || !target.isAlive()) {
                        this.setMeleeAttackDelay(0);
                        this.AttackCount = 0;
                    } else {
                        this.setMeleeAttackDelay(currentspelldelay - 1);
                        int delay = this.tickCount - this.StartedMeleeAttackTimeStamp;
                        if(!((IMeleeAttacker) this).getMeleeAnimationAudioCueDelay().isEmpty() && ((IMeleeAttacker) this).getMeleeAnimationAudioCueDelay().contains(delay)){
                            this.playMeleeAttackPreSound();
                        }
                        if (((IMeleeAttacker)this).getAttackPreAnimationDelay().contains(delay) && this.distanceTo(target)<=((IMeleeAttacker)this).getAttackRange(((IMeleeAttacker)this).isUsingTalentedWeapon())) {
                            this.AttackCount+=1;
                            ((IMeleeAttacker)this).PerformMeleeAttack(target, this.getAttackDamage(), this.AttackCount);
                        }
                    }
                }
                else if(this.AttackCount>0){
                    this.AttackCount = 0;
                }

            }

            if(this instanceof ISpellUser) {
                int currentspelldelay = this.getSpellDelay();
                @Nullable
                LivingEntity target = this.getTarget();
                if (currentspelldelay > 0) {
                    if (target == null || !target.isAlive()) {
                        this.setSpellDelay(0);
                    } else {
                        this.getNavigation().stop();
                        setSpellDelay(currentspelldelay - 1);
                        int delay = this.tickCount - this.StartedSpellAttackTimeStamp;
                        if (delay == ((ISpellUser)this).getProjectilePreAnimationDelay()) {
                            ((ISpellUser)this).ShootProjectile(this.getCommandSenderWorld(), target);
                        }
                    }
                }
            }
            //Very cheap fix to Datamanager not syncing
            for(int i=0; i<this.getSkillItemCount(); i++){
                DataParameter<ItemStack>[] stacks = new DataParameter[]{SKILL_ITEM_0,SKILL_ITEM_1,SKILL_ITEM_2,SKILL_ITEM_3};
                ItemStack serverSideItem = this.getInventory().getStackInSlot(12+i);
                this.getEntityData().set(stacks[i], serverSideItem);
            }

        }

        if(!this.getCommandSenderWorld().isClientSide() && this.isAngry()){
            int newangertimer = this.entityData.get(ANGRYTIMER)-1;
            this.entityData.set(ANGRYTIMER, newangertimer);
            if(newangertimer <=0){
                this.setTarget(null);
            }else if(this.getTarget() != this.getOwner()){
                this.setTarget(this.getOwner());
            }
        }


        if(this.getEntityData().get(HEAL_TIMER)>0){
            this.getEntityData().set(HEAL_TIMER, this.getEntityData().get(HEAL_TIMER)-1);
        }

        if(this.isSleeping()){
            this.getNavigation().stop();
        }

        if (this.forcewakeupExpireTimer>0){
            this.forcewakeupExpireTimer--;
        }

        int mainhandDelay = this.getEntityData().get(RELOAD_TIMER_MAINHAND);
        if(mainhandDelay>0){
            this.getEntityData().set(RELOAD_TIMER_MAINHAND, mainhandDelay - 1);
            if(this.getEntityData().get(RELOAD_TIMER_MAINHAND) == 0 && this.getGunStack().getItem() instanceof ItemGunBase){
                this.reloadAmmo();
            }
        }

        if(this.tickCount%10==0){
            this.setShiftKeyDown((this.getOwner()!= null && this.getOwner().isShiftKeyDown()) || this.getCommandSenderWorld().getBlockState(this.blockPosition().below()).getBlock() == Blocks.MAGMA_BLOCK);
        }

        else if(this.forcewakeupExpireTimer==0 || !this.isSleeping()){
            this.forceWakeupCounter=0;
        }

        if(this.getAffection() <0)
            this.getEntityData().set(AFFECTION, 0F);
        else if(this.getAffection()>100){
            if(!this.isOathed())
                this.getEntityData().set(AFFECTION, 100F);
            else if(this.getAffection() > 200){
                this.getEntityData().set(AFFECTION, 200F);
            }
        }
        this.navigation.getNodeEvaluator().setCanPassDoors(this.canOpenDoor());
        this.navigation.getNodeEvaluator().setCanOpenDoors(this.canOpenDoor());

        if(this.isForceWaken() && this.getCommandSenderWorld().isDay()){
            this.setForceWaken(false);
        }

        if(this.isOrderedToSit() && this.tickCount%20 == 0){
            this.addMorale(0.015);
        }
        else if(!this.isFreeRoaming()&& this.tickCount%20 == 0){
            this.addMorale(-0.0001);
        }
        if(!this.level.isClientSide() && this.getCommandSenderWorld().isDay()) {
            if (this.isSleeping()) {
                this.stopSleeping();
                if(this.shouldBeSitting){
                    this.setOrderedToSit(true);
                }
            }
        }

        if(!this.getCommandSenderWorld().isClientSide() && this.isFreeRoaming() && (this.getCommandSenderWorld().isNight() && this.isOrderedToSit() && this.isInHomeRangefromCurrenPos() && this.getNavigation().createPath(this.getHOMEPOS().get(), 0) != null && this.getNavigation().createPath(this.getHOMEPOS().get(), 0).canReach())){
            this.shouldBeSitting = this.isOrderedToSit();
            this.setOrderedToSit(false);
        }
    }

    public ItemStack getSkillItem1(){
        return this.getEntityData().get(SKILL_ITEM_1);
    }

    public void playMeleeAttackPreSound(){}

    public int getNonVanillaMeleeAttackDelay(){
        return this.getEntityData().get(NONVANILLAMELEEATTACKDELAY);
    }

    public void setMeleeAttackDelay(int value) {
        this.getEntityData().set(NONVANILLAMELEEATTACKDELAY, value);
    }
    public boolean isNonVanillaMeleeAttacking(){
        return this.getNonVanillaMeleeAttackDelay()>0;
    }

    public void setSkillItemSlotContent(int index, ItemStack stack){
        DataParameter<ItemStack>[] stacks = new DataParameter[]{SKILL_ITEM_0,SKILL_ITEM_1,SKILL_ITEM_2,SKILL_ITEM_3};
        this.getInventory().setStackInSlot(12+index, stack);
        this.getEntityData().set(stacks[index], stack);
    }

    public ItemStack getNextSkillItem(){
        int index = this.getNextSkillItemindex();
        return index>=0? this.getSkillItem(index):ItemStack.EMPTY;
    }

    public int getNextSkillItemindex(){
        for(int i = 0; i<this.getSkillItemCount(); i++){
            if(this.isSkillItemInindex(i)){
                return i;
            }
        }
        return -1;
    }

    @Override
    public float getWalkTargetValue(@Nonnull BlockPos pos, IWorldReader worldIn) {
        if(!worldIn.isClientSide() && SolarApocalypse.isSunlightDangerous((ServerWorld) worldIn)){
            return 0.0F-worldIn.getBrightness(LightType.SKY, pos);
        }
        else {
            return super.getWalkTargetValue(pos, worldIn);
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
                        ItemEntity itemEntity = new ItemEntity(this.getCommandSenderWorld(), this.getX(), this.getY(), this.getZ(), EmptyMag);
                        this.getCommandSenderWorld().addFreshEntity(itemEntity);
                    }
                }
            }
        }
    }

    public int getGunAmmoCount(){
        return ItemStackUtils.getRemainingAmmo(this.getGunStack());
    }

    public ItemStack getGunStack(){
        if(this.getItemInHand(this.getValidGunHand()).getItem() instanceof ItemGunBase){
            return this.getItemInHand(this.getValidGunHand());
        }
        return ItemStack.EMPTY;
    }

    private Hand getValidGunHand(){
        if(this.getMainHandItem().getItem() instanceof ItemGunBase){
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
        AbstractArrowEntity abstractarrowentity = this.fireArrow(itemstack, distanceFactor, this.getItemBySlot(EquipmentSlotType.MAINHAND));
        if (this.getMainHandItem().getItem() instanceof net.minecraft.item.BowItem)
            abstractarrowentity = ((net.minecraft.item.BowItem)this.getMainHandItem().getItem()).customArrow(abstractarrowentity);
        double d0 = target.getX() - this.getX();
        double d1 = target.getY(0.3333333333333333D) - abstractarrowentity.getY();
        double d2 = target.getZ() - this.getZ();
        double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
        abstractarrowentity.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, 3);
        this.playSound(SoundEvents.ARROW_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(abstractarrowentity);
    }

    protected AbstractArrowEntity fireArrow(ItemStack arrowStack, float distanceFactor, ItemStack ItemInHand) {
        return ProjectileHelper.getMobArrow(this, arrowStack, distanceFactor);
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
        return this.getEntityData().get(SKILLDELAYTICK);
    }

    public void setSkillDelayTick(int value){
        this.getEntityData().set(SKILLDELAYTICK, value);
    }

    public void setSkillDelaySeconds(int value){
        this.getEntityData().set(SKILLDELAYTICK, value*20);
    }

    public int getSkillItemCount(){
        return 1;
    }

    public void updateSkillDelay(){
        int skilldelay = this.getEntityData().get(SKILLDELAYTICK);
        if(skilldelay>0){
            this.getEntityData().set(SKILLDELAYTICK, skilldelay-1);
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
        return this.getEntityData().get(USING_SKILL);
    }

    public void setUsingSkill(boolean value){
        this.getEntityData().set(USING_SKILL, value);
    }

    public boolean canUseSkill(){
        return false;
    }

    public void setSkillDelay(){
        this.setSkillDelayTick(2400);
    }

    public ItemStack getSkillItem(int index){
        if(this.getCommandSenderWorld().isClientSide){
            DataParameter<ItemStack>[] stacks = new DataParameter[]{SKILL_ITEM_0,SKILL_ITEM_1,SKILL_ITEM_2,SKILL_ITEM_3};
            ItemStack stack = this.getEntityData().get(stacks[index]);
            return stack;
        }
        return this.getInventory().getStackInSlot(12+index);
    }

    public boolean isSkillItemInindex(int index){
        return isSkillItem(getSkillItem(index));
    }

    public boolean isSkillItem(ItemStack stack){
        return false;
    }

    public boolean hasSkillItem(){
        for (int i = 0; i<this.getSkillItemCount(); i++) {
            if (!this.isSkillItem(this.getSkillItem(i))) {
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
        this.goalSelector.addGoal(4, new CompanionClimbLadderGoal(this));
        this.goalSelector.addGoal(5, new CompanionUseSkillGoal(this));
        if(this instanceof EntityKansenBase){
            if(this instanceof EntityKansenAircraftCarrier) {
                this.goalSelector.addGoal(6, new KansenLaunchPlaneGoal((EntityKansenAircraftCarrier) this, 20, 40, 50));
            }
            this.goalSelector.addGoal(7, new KansenRangedAttackGoal((EntityKansenBase) this, 0.8F, 10,20, 80, 100));
        }
        else if(this instanceof ISpellUser){
            this.goalSelector.addGoal(8, new CompanionSpellRangedAttackGoal(this, 10));
        }
        else if(this instanceof IMeleeAttacker){
            this.goalSelector.addGoal(9, new CompanionSwordUserMeleeAttack((IMeleeAttacker) this));
        }
        this.goalSelector.addGoal(10, new CompanionHealandEatFoodGoal(this));
        this.goalSelector.addGoal(11, new CompanionsUseTotem(this));
        this.goalSelector.addGoal(12, new CompanionPlaceTorchGoal(this));
        this.goalSelector.addGoal(13, new CompanionUseShieldGoal(this));
        this.goalSelector.addGoal(14, new WorkGoal(this, 1.0D));
        this.goalSelector.addGoal(15, new CompanionUseGunGoal(this, 40, 0.6));
        this.goalSelector.addGoal(16, new CompanionRideBoatAlongPlayerGoal(this, 1.0));
        this.goalSelector.addGoal(17, new CompanionVanillaMeleeGoal(this, 1.0D, true));
        this.goalSelector.addGoal(18, new CompanionFollowOwnerGoal(this, 0.75D, 5.0F, 2.0F, false));
        this.goalSelector.addGoal(19, new CompanionHealOwnerAndAllyGoal(this, 20, 10, 1.25, 10F));
        this.goalSelector.addGoal(20, new CompanionOpenDoorGoal(this, true));
        this.goalSelector.addGoal(21, new CompanionFreeroamGoal(this, 60, true));
        this.goalSelector.addGoal(22, new CompanionPickupItemGoal(this));
        this.goalSelector.addGoal(23, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(24, new LookRandomlyGoal(this));

        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());
    }

    @Nonnull
    public Vector3d handleRelativeFrictionAndCalculateMovement(@Nonnull Vector3d p_233633_1_, float p_233633_2_) {
        this.moveRelative(this.getFrictionInfluencedSpeed(p_233633_2_), p_233633_1_);
        this.setDeltaMovement(this.handleOnClimbable(this.getDeltaMovement()));
        this.move(MoverType.SELF, this.getDeltaMovement());
        Vector3d vector3d = this.getDeltaMovement();
        if ((this.horizontalCollision || this.jumping) && this.onClimbable() && this.isClimbingUp) {
            vector3d = new Vector3d(vector3d.x, 0.2D, vector3d.z);
        }

        return vector3d;
    }

    private Vector3d handleOnClimbable(Vector3d p_213362_1_) {
        if (this.onClimbable()) {
            this.fallDistance = 0.0F;
            float f = 0.15F;
            double d0 = MathHelper.clamp(p_213362_1_.x, (double)-0.15F, (double)0.15F);
            double d1 = MathHelper.clamp(p_213362_1_.z, (double)-0.15F, (double)0.15F);
            double d2 = Math.max(p_213362_1_.y, (double)-0.15F);
            if (d2 < 0.0D && !this.getFeetBlockState().isScaffolding(this)) {
                this.isSuppressingSlidingDownLadder();
            }

            p_213362_1_ = new Vector3d(d0, d2, d1);
        }

        return p_213362_1_;
    }

    private float getFrictionInfluencedSpeed(float p_213335_1_) {
        return this.onGround ? this.getSpeed() * (0.21600002F / (p_213335_1_ * p_213335_1_ * p_213335_1_)) : this.flyingSpeed;
    }

    @Override
    public void travel(Vector3d travelVector) {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        super.travel(travelVector);
        this.calculateTravelHunger(this.getX() - d0, this.getY() - d1, this.getZ() - d2);
    }

    public void calculateTravelHunger(double p_71000_1_, double p_71000_3_, double p_71000_5_) {
        if (!this.isPassenger()) {
            double movemlength = p_71000_1_ * p_71000_1_ + p_71000_3_ * p_71000_3_ + p_71000_5_ * p_71000_5_;
            if (this.isSwimming()) {
                int i = Math.round(MathHelper.sqrt(movemlength) * 100.0F);
                if (i > 0) {
                    this.addExhaustion(0.005F * (float)i * 0.005F);
                }
            } else if (this.isEyeInFluid(FluidTags.WATER)) {
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
        return !this.isInWater() && !this.isOrderedToSit() && !this.isSleeping() && !this.isFreeRoaming();
    }

    public boolean isReloadingMainHand(){
        int remainingTime = this.getEntityData().get(RELOAD_TIMER_MAINHAND);
        return remainingTime>0;
    }

    public void setReloadDelay(){
        this.getEntityData().set(RELOAD_TIMER_MAINHAND, this.Reload_Anim_Delay());
    }

    public int Reload_Anim_Delay(){
        return 63;
    }
    @Override
    public void startSleeping(@Nonnull BlockPos pos) {
        super.startSleeping(pos);
        this.setLastSlept(this.getCommandSenderWorld().getDayTime());
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

    public void tame(PlayerEntity player) {
        this.setTame(true);
        this.setOwnerUUID(player.getUUID());
        ProjectAzurePlayerCapability capability = ProjectAzurePlayerCapability.getCapability(player);
        capability.addCompanion(this);
        //Triggering Tame Animal goal at the beginning of the world doesn't feel right. :d
    }
    @Override
    public boolean isPushable() {
        return super.isPushable() && !this.isSleeping();
    }

    @MethodsReturnNonnullByDefault
    @Override
    public PushReaction getPistonPushReaction() {
        return this.isSleeping()? PushReaction.IGNORE:super.getPistonPushReaction();
    }

    public boolean isinQinteraction(){
        return this.getEntityData().get(QUESTIONABLE_INTERACTION_ANIMATION_TIME)>0;
    }


    @Nonnull
    @Override
    public ActionResultType interactAt(@Nonnull PlayerEntity player, @Nonnull Vector3d vec, @Nonnull Hand hand) {

        ItemStack heldstacks = player.getMainHandItem();
        Item HeldItem = heldstacks.getItem();

        if(this.isOwnedBy(player) && !(this instanceof EntityKansenBase && HeldItem instanceof ItemRiggingBase)) {

            if (this.getVehicle() != null) {
                this.stopRiding();
            }

            if (HeldItem instanceof ArmorItem || HeldItem instanceof TieredItem) {
                ItemStack stack = player.getItemInHand(hand).copy();
                EquipmentSlotType type = getEquipmentSlotForItem(stack);
                ItemStack EquippedStack = this.getItemBySlot(type).copy();
                if (stack.canEquip(type, this)) {
                    this.setItemSlot(type, stack);
                    if (stack == this.getItemBySlot(type)) {
                        player.setItemInHand(hand, EquippedStack);
                        this.playEquipSound(stack);
                        return ActionResultType.SUCCESS;
                    }
                }
            }
            //food
            else if (HeldItem != registerItems.ORIGINIUM_PRIME.get() && HeldItem.getFoodProperties() != null && this.canEat(HeldItem.getFoodProperties().canAlwaysEat())) {
                ItemStack stack = this.eat(this.getCommandSenderWorld(), player.getItemInHand(hand));
                this.addAffection(0.03);
                if (!player.isCreative()) {
                    player.setItemInHand(hand, stack);
                }
                return ActionResultType.SUCCESS;
            }
            //Potion
            else if (HeldItem instanceof PotionItem && !(HeldItem instanceof ThrowablePotionItem)) {
                ItemStack stack = player.getItemInHand(hand);
                for (EffectInstance effectinstance : PotionUtils.getMobEffects(stack)) {
                    if (effectinstance.getEffect().isInstantenous()) {
                        effectinstance.getEffect().applyInstantenousEffect(player, player, this, effectinstance.getAmplifier(), 1.0D);
                    } else {
                        this.addEffect(new EffectInstance(effectinstance));
                    }
                    this.addAffection(effectinstance.getEffect().isBeneficial() ? 0.05 : -0.075);
                }
                this.playSound(SoundEvents.GENERIC_DRINK, 1F, 0.8F + this.level.random.nextFloat() * 0.4F);
                if (!player.isCreative()) {
                    player.setItemInHand(hand, new ItemStack(Items.GLASS_BOTTLE));
                }
                return ActionResultType.SUCCESS;
            } else if (heldstacks.getItem() == registerItems.OATHRING.get()) {
                if (this.getAffection() < 100 && !player.isCreative()) {
                    player.sendMessage(new TranslationTextComponent("entity.not_enough_affection"), this.getUUID());
                    return ActionResultType.FAIL;
                } else {
                    if (!this.isOathed()) {
                        if (player.isCreative()) {
                            this.setAffection(100);
                        } else {
                            player.getItemInHand(hand).shrink(1);
                        }
                        this.setOathed(true);
                        if(this.getOathSound()!= null) {
                            this.playSound(this.getOathSound(), 1.0f, 1.0f);
                        }
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
                    if (!this.level.isClientSide) {
                        this.doHeal(1.0f);
                        if (!player.isCreative()) {
                            if (heldstacks.hurt(1, MathUtil.getRand(), (ServerPlayerEntity) player)) {
                                heldstacks.shrink(1);
                            }
                        }
                        this.playSound(SoundEvents.ARMOR_EQUIP_LEATHER, 0.8F, 0.8F + this.level.random.nextFloat() * 0.4F);
                    }
                    return ActionResultType.SUCCESS;
                }
            } else if (heldstacks.getItem() instanceof ItemCommandStick) {
                CompoundNBT compound = heldstacks.getOrCreateTag();
                if (player.isShiftKeyDown()) {
                    if(!this.getCommandSenderWorld().isClientSide()) {
                        if (compound.contains("BedX") && compound.contains("BedY") && compound.contains("BedZ")) {
                            BlockPos BedPos = new BlockPos(compound.getInt("BedX"), compound.getInt("BedY"), compound.getInt("BedZ"));
                            if (this.getCommandSenderWorld().getBlockState(BedPos).isBed(this.getCommandSenderWorld(), BedPos, this)) {
                                if(this.getOwner() instanceof PlayerEntity) {

                                    ProjectAzurePlayerCapability cap = ProjectAzurePlayerCapability.getCapability((PlayerEntity)this.getOwner());
                                    boolean isBedDuplicate = false;
                                    @Nullable
                                    AbstractEntityCompanion duplicatedComp = null;
                                    for(AbstractEntityCompanion companion:cap.getCompanionList()){
                                        if(companion.getEntityData().get(HOMEPOS).isPresent()){
                                            BlockPos pos = companion.getEntityData().get(HOMEPOS).get();
                                            if(pos.getX() == compound.getInt("BedX") && pos.getY() == compound.getInt("BedY") && pos.getZ() == compound.getInt("BedZ")){
                                                isBedDuplicate = true;
                                                duplicatedComp = companion;
                                                break;
                                            }
                                        }
                                    }
                                    if(duplicatedComp == this){
                                        player.displayClientMessage(new TranslationTextComponent("message.commandstick.entity_bedpos_same_companion_same_bed", "[" + BedPos.getX() + ", " + BedPos.getY() + ", " + BedPos.getZ() + "]"), true);
                                    }
                                    else if(isBedDuplicate){
                                        player.displayClientMessage(new TranslationTextComponent("message.commandstick.entity_bedpos_failed_duplicate", "[" + BedPos.getX() + ", " + BedPos.getY() + ", " + BedPos.getZ() + "]", this.getDisplayName(), duplicatedComp.getDisplayName()), true);
                                    }
                                    else {
                                        this.clearHomePos();
                                        this.setHomePos(BedPos);
                                        player.displayClientMessage(new TranslationTextComponent("message.commandstick.entity_bedpos_applied", "[" + BedPos.getX() + ", " + BedPos.getY() + ", " + BedPos.getZ() + "]", this.getDisplayName()), true);
                                    }
                                }
                            } else {
                                player.displayClientMessage(new TranslationTextComponent("message.commandstick.entity_bedpos_failed", "["+ BedPos.getX()+", "+ BedPos.getY()+", "+ BedPos.getZ() +"]", this.getDisplayName()), true);
                            }
                        } else if (this.hasHomePos()) {
                            this.clearHomePos();
                            player.displayClientMessage(new TranslationTextComponent("message.commandstick.entity_bedpos_cleared", this.getDisplayName()), true);
                        }
                    }
                    return ActionResultType.CONSUME;
                }
            } else if(player.getItemInHand(hand).isEmpty() && !this.isAngry()) {
                if(player.isShiftKeyDown()) {
                    if (!this.level.isClientSide) {
                        this.openGUI((ServerPlayerEntity) player);
                    }
                    this.playSound(SoundEvents.ARMOR_EQUIP_CHAIN, 0.8F+(0.4F*this.getRandom().nextFloat()),0.8F+(0.4F*this.getRandom().nextFloat()));
                    Main.PROXY.setSharedMob(this);
                    return ActionResultType.SUCCESS;
                }

                BlockStateUtil.RelativeDirection interactionDirection;
                float entityHitAngle = (float) ((Math.atan2(player.getZ() - getZ(), player.getX() - getX()) * (180 / Math.PI) - 90) % 360);
                float entityAttackingAngle = yBodyRot % 360;
                if (entityHitAngle < 0) {
                    entityHitAngle += 360;
                }
                if (entityAttackingAngle < 0) {
                    entityAttackingAngle += 360;
                }
                float entityRelativeAngle = entityHitAngle - entityAttackingAngle;

                if (entityRelativeAngle < 0) {
                    entityRelativeAngle += 360;
                }

                if((entityRelativeAngle > 320 || entityRelativeAngle < 45)){
                    interactionDirection = FRONT;
                }
                else if(entityRelativeAngle>45 && entityRelativeAngle<135){
                    interactionDirection = BlockStateUtil.RelativeDirection.RIGHT;
                }
                else if(entityRelativeAngle>135 && entityRelativeAngle<225){
                    interactionDirection = BlockStateUtil.RelativeDirection.BACK;
                }
                else{
                    interactionDirection = BlockStateUtil.RelativeDirection.RIGHT;
                }

                Vector3d PlayerLook = player.getViewVector(1.0F).normalize();
                float eyeHeight = (float) this.getEyeY();


                Vector3d EyeDelta = new Vector3d(this.getX() - player.getX(), eyeHeight - player.getEyeY(), this.getZ() - player.getZ());
                double EyeDeltaLength = EyeDelta.length();
                EyeDelta = EyeDelta.normalize();
                double EyeCheckFinal = PlayerLook.dot(EyeDelta);


                if(player.getMainHandItem().isEmpty() && this.distanceTo(player)<=2) {
                    if (this.isSleeping()) {
                        if (this.forceWakeupCounter > 4) {
                            this.forceWakeup();
                        } else {
                            this.forceWakeupCounter++;
                            this.forcewakeupExpireTimer = 20;
                        }
                        return ActionResultType.SUCCESS;
                    } else if (EyeCheckFinal > 0.998 && this.entityData.get(QUESTIONABLE_INTERACTION_ANIMATION_TIME)==0) {
                        if (this.getCommandSenderWorld().isClientSide()) {
                            Main.NETWORK.sendToServer(new EntityInteractionPacket(this.getId(), EntityInteractionPacket.EntityBehaviorType.PAT, true));
                        }
                        return ActionResultType.SUCCESS;
                    }
                    else if (this.isDoingVerticalInteraction(player, 0.65F, -1) && interactionDirection==FRONT) {
                        if (this.getCommandSenderWorld().isClientSide()) {
                            Main.NETWORK.sendToServer(new EntityInteractionPacket(this.getId(), EntityInteractionPacket.EntityBehaviorType.QUESTIONABLE, true));
                        }
                        return ActionResultType.SUCCESS;
                    }
                    else if (this.isDoingVerticalInteraction(player, 0.2F, -1)) {
                        this.SwitchSittingStatus();
                        return ActionResultType.SUCCESS;
                        //this.setOrderedToSit(!this.isSitting());
                    }
                }
            }
            return ActionResultType.FAIL;
        }
        else{
            if(player.getMainHandItem().getItem() == registerItems.Rainbow_Wisdom_Cube.get()&&!this.isTame()){
                this.tame(player);
                player.getItemInHand(hand).shrink(1);
                return ActionResultType.CONSUME;
            }
        }
        return super.interactAt(player, vec, hand);
    }
    @Nullable
    public SoundEvent getOathSound(){
        return null;
    }

    public boolean isDoingVerticalInteraction(LivingEntity player, float heightpoint, float distance){
        Vector3d PlayerLook = player.getViewVector(1.0F).normalize();
        Vector3d EyeDelta = new Vector3d(this.getX() - player.getX(), this.getY(heightpoint) - player.getEyeY(), this.getZ() - player.getZ());
        EyeDelta = EyeDelta.normalize();
        double EyeCheckFinal = PlayerLook.dot(EyeDelta);
        return EyeCheckFinal > 0.998;
    }

    public boolean isDoingHeadpat(LivingEntity player, float distance){
        Vector3d PlayerLook = player.getViewVector(1.0F).normalize();
        float eyeHeight = (float) this.getEyeY();


        Vector3d EyeDelta = new Vector3d(this.getX() - player.getX(), eyeHeight - player.getEyeY(), this.getZ() - player.getZ());
        double EyeDeltaLength = EyeDelta.length();
        EyeDelta = EyeDelta.normalize();
        double EyeCheckFinal = PlayerLook.dot(EyeDelta);
        return EyeCheckFinal > 1.0D / EyeDeltaLength;
    }

    private boolean hasHomePos() {
        return this.getEntityData().get(HOMEPOS).isPresent() && this.getEntityData().get(VALID_HOME_DISTANCE)>0;
    }

    private void SwitchSittingStatus() {
        boolean value = !this.isOrderedToSit();
        this.setOrderedToSit(value);
        if(this.getCommandSenderWorld().isClientSide()) {
            Main.NETWORK.sendToServer(new EntityInteractionPacket(this.getId(), EntityInteractionPacket.EntityBehaviorType.SIT, value));
        }
    }

    public void doHeal(float amount) {
        this.heal(amount);
        this.addAffection(0.12F);
        this.getEntityData().set(HEAL_TIMER, 50);
        if(!this.getCommandSenderWorld().isClientSide()) {
            Main.NETWORK.send(TRACKING_ENTITY_AND_SELF.with(() -> this), new spawnParticlePacket(this, spawnParticlePacket.Particles.AFFECTION_HEART));
        }
    }

    @Override
    public int getAmbientSoundInterval() {
        return 3000;
    }

    @Override
    protected float getVoicePitch() {
        return 0.9F+(this.getRandom().nextFloat()*0.2F);
    }

    @Override
    public void setAggressive(boolean value) {
        if(this.getOwner() != null && this.tickCount - this.lastAggroedTimeStamp>=this.getMinimumAggressiveVoiceInterval()){
            if(this.getTarget() != null && this.getTarget() != this.getOwner() && this.getAggroedSoundEvent() != null){
                this.playSound(this.getAggroedSoundEvent(), 1.0f, this.getVoicePitch());
            }
        }
        this.lastAggroedTimeStamp = value? this.tickCount:this.lastAggroedTimeStamp;

        super.setAggressive(value);
    }
    protected int getMinimumAggressiveVoiceInterval(){
        return 600;
    }
    @Nullable
    protected SoundEvent getAggroedSoundEvent(){
        return null;
    }

    @Override
    public void tick() {
        super.tick();
        if(this.getHOMEPOS().isPresent()&&this.tickCount%30==0){
            BlockState blockstate = this.level.getBlockState(this.getHOMEPOS().get());
            if(!blockstate.isBed(this.getCommandSenderWorld(),this.getHOMEPOS().get(), this)) {
                this.clearHomePos();
            }
        }
        if(!this.getCommandSenderWorld().isClientSide()){
            this.foodStats.tick(this);
        }
    }

    public void updateSwimming() {
        if (!this.level.isClientSide && this.tickCount%10 == 0) {
            double waterheight = this.getFluidHeight(FluidTags.WATER);
            boolean rigging = !this.canUseRigging();
            if (this.isEffectiveAi() && waterheight > 0.9&&rigging) {
                this.navigation = this.swimmingNav;
                this.moveControl = this.SwimController;
                this.setSwimming(true);
            }
            else if(this.isSailing()){
                this.navigation = this.sailingNav;
                this.moveControl = this.MoveController;
                this.setSwimming(false);
            }
            else {
                this.navigation = this.groundNav;
                this.moveControl = this.MoveController;
                this.setSwimming(false);
            }
        }

    }

    public void pickupExpOrb(ExperienceOrbEntity orbEntity){
        if(!this.level.isClientSide){
            if(orbEntity.throwTime <= 0 && this.expdelay <= 0){
                this.take(orbEntity, 1);
                this.expdelay = 2;
                Map.Entry<EquipmentSlotType, ItemStack> entry = EnchantmentHelper.getRandomItemWith(Enchantments.MENDING, this, ItemStack::isDamaged);
                if (entry != null) {
                    ItemStack itemstack = entry.getValue();
                    if (!itemstack.isEmpty() && itemstack.isDamaged()) {
                        int i = Math.min((int)(orbEntity.value * itemstack.getXpRepairRatio()), itemstack.getDamageValue());
                        orbEntity.value -= i/2;
                        itemstack.setDamageValue(itemstack.getDamageValue() - i);
                    }
                }
                if (orbEntity.value > 0) {
                    this.addExp(orbEntity.value);
                }
                orbEntity.remove();
            }
        }
    }

    @Override
    public boolean canBeLeashed(PlayerEntity player) {
        if(this.getOwner() == player){
            player.displayClientMessage(new TranslationTextComponent("message.easteregg.leash"), true);
        }
        return false;
    }

    @Override
    protected void removeAfterChangingDimensions() {
        if(this.getOwner() != null && this.getOwner() instanceof PlayerEntity){
            ProjectAzurePlayerCapability cap = ProjectAzurePlayerCapability.getCapability((PlayerEntity) this.getOwner());
            cap.removeCompanion(this);
        }
        super.removeAfterChangingDimensions();
    }

    protected abstract void openGUI(ServerPlayerEntity player);

    public void beingpatted(){
        if(this.entityData.get(MAXPATEFFECTCOUNT) == 0){
            this.entityData.set(MAXPATEFFECTCOUNT, 5+this.random.nextInt(5));
            if(this.getPatSoundEvent() != null){
                this.playSound(this.getPatSoundEvent(), 1, this.getVoicePitch() );
            }
        }
        this.getEntityData().set(PAT_ANIMATION_TIME, 20);
    }

    public void startqinteraction(){
        this.getEntityData().set(QUESTIONABLE_INTERACTION_ANIMATION_TIME, 20);
    }
    @Nullable
    public SoundEvent getPatSoundEvent(){
        return null;
    }

    public boolean isGettingHealed(){
        return this.entityData.get(HEAL_TIMER)>0;
    }

    public IItemHandlerModifiable getEquipment(){
        return this.EQUIPMENT;
    }

    public ItemStackHandler getAmmoStorage() {
        return this.AmmoStorage;
    }

    @Nonnull
    @Override
    public IPacket<?> getAddEntityPacket() {
        NetworkHooks.getEntitySpawningPacket(this);
        return super.getAddEntityPacket();
    }

    public void SwitchItemBehavior(){
        Main.NETWORK.sendToServer(new EntityInteractionPacket(this.getId(), EntityInteractionPacket.EntityBehaviorType.ITEMPICKUP, !this.shouldPickupItem()));
    }

    public void SwitchFreeRoamingStatus() {
        Main.NETWORK.sendToServer(new EntityInteractionPacket(this.getId(), EntityInteractionPacket.EntityBehaviorType.HOMEMODE, !this.isFreeRoaming()));
    }

    public void setForceWaken(boolean value){
        this.getEntityData().set(ISFORCEWOKENUP, value);
    }

    public boolean isForceWaken(){
        return this.getEntityData().get(ISFORCEWOKENUP);
    }

    public void forceWakeup(){
        if(this.isSleeping()) {
            this.shouldBeSitting = false;
            this.stopSleeping();
            this.setForceWaken(true);
        }
    }

    @Override
    public void stopSleeping() {
        super.stopSleeping();
        this.setLastWokenup(this.getCommandSenderWorld().getDayTime());
    }

    @SubscribeEvent
    public void OnTimeSkip(PlayerWakeUpEvent event){
        this.setLastWokenup(this.getCommandSenderWorld().getDayTime());
        this.CalculateMoraleBasedonTime(this.getLastSlept(), this.getLastWokenup());
    }

    private void CalculateMoraleBasedonTime(long lastSlept, long lastWokenup) {

        long deltaTime = lastSlept-lastWokenup;

        this.addAffection(0.02*((float)deltaTime/300));
        this.addMorale(((float)deltaTime/1000)*30);
    }

    public double getMorale() {
        return this.entityData.get(MORALE);
    }

    public void setMorale(float morale) {
        this.entityData.set(MORALE, morale);
    }

    public void addMorale(double value){
        double prevmorale = this.getMorale();


        this.setMorale((float) MathUtils.clamp(prevmorale+value, 0, 150));
    }

    public Optional<BlockPos> getStayCenterPos() {
        return this.getEntityData().get(STAYPOINT);
    }

    public void setStayCenterPos(BlockPos stayCenterPos) {
        this.getEntityData().set(STAYPOINT,Optional.of(stayCenterPos));
    }

    public void clearStayCenterPos() {
        this.getEntityData().set(STAYPOINT, Optional.empty());
    }

    public void PickUpItem(ItemEntity target) {
        this.take(target, target.getItem().getCount());
        ItemStack stack = target.getItem();

        this.onItemPickup(target);
        this.take(target, stack.getCount());
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
    public EntitySize getDimensions(@ParametersAreNonnullByDefault Pose poseIn) {
        if(this.isOrderedToSit()){
            return new EntitySize(this.getBbWidth(), this.getSitHeight(), false);
        }
        return super.getDimensions(poseIn);
    }

    public void setOrderedToSit(boolean val) {
        this.getEntityData().set(SITTING, val);
        super.setOrderedToSit(val);
        this.refreshDimensions();
    }

    @Nullable
    @Override
    public ModifiableAttributeInstance getAttribute(Attribute attribute) {
        return super.getAttribute(attribute);
    }
}
