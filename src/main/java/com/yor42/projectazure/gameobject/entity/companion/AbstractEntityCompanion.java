package com.yor42.projectazure.gameobject.entity.companion;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Dynamic;
import com.tac.guns.Config;
import com.tac.guns.client.render.pose.TwoHandedPose;
import com.tac.guns.common.GripType;
import com.tac.guns.common.Gun;
import com.tac.guns.common.ProjectileManager;
import com.tac.guns.init.ModEnchantments;
import com.tac.guns.interfaces.IProjectileFactory;
import com.tac.guns.item.GunItem;
import com.tac.guns.network.PacketHandler;
import com.tac.guns.network.message.MessageBulletTrail;
import com.tac.guns.network.message.MessageGunSound;
import com.tac.guns.util.GunEnchantmentHelper;
import com.tac.guns.util.GunModifierHelper;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.ProjectAzureWorldSavedData;
import com.yor42.projectazure.gameobject.capability.playercapability.CompanionTeam;
import com.yor42.projectazure.gameobject.capability.playercapability.ProjectAzurePlayerCapability;
import com.yor42.projectazure.gameobject.entity.CompanionDefaultMovementController;
import com.yor42.projectazure.gameobject.entity.CompanionGroundPathNavigator;
import com.yor42.projectazure.gameobject.entity.CompanionSwimMovementController;
import com.yor42.projectazure.gameobject.entity.CompanionSwimPathNavigator;
import com.yor42.projectazure.gameobject.entity.ai.CompanionTasks;
import com.yor42.projectazure.gameobject.entity.companion.magicuser.ISpellUser;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenBase;
import com.yor42.projectazure.gameobject.entity.misc.AbstractEntityDrone;
import com.yor42.projectazure.gameobject.entity.misc.AbstractEntityPlanes;
import com.yor42.projectazure.gameobject.items.ItemCannonshell;
import com.yor42.projectazure.gameobject.items.ItemMagazine;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
import com.yor42.projectazure.gameobject.items.tools.ItemBandage;
import com.yor42.projectazure.gameobject.items.tools.ItemCommandStick;
import com.yor42.projectazure.gameobject.items.tools.ItemDefibPaddle;
import com.yor42.projectazure.gameobject.misc.DamageSources;
import com.yor42.projectazure.interfaces.IAknOp;
import com.yor42.projectazure.interfaces.IAzurLaneKansen;
import com.yor42.projectazure.intermod.SolarApocalypse;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.utils.DirectionUtil;
import com.yor42.projectazure.libs.utils.ItemStackUtils;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.network.packets.DeleteHomePacket;
import com.yor42.projectazure.network.packets.EditTeamMemberPacket;
import com.yor42.projectazure.network.packets.EntityInteractionPacket;
import com.yor42.projectazure.network.packets.spawnParticlePacket;
import com.yor42.projectazure.setup.register.registerItems;
import com.yor42.projectazure.setup.register.registerManager;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.PushReaction;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.DebugPacketSender;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.village.PointOfInterestManager;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.example.client.renderer.entity.ExampleExtendedRendererEntityRenderer;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
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
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static com.yor42.projectazure.libs.utils.DirectionUtil.RelativeDirection.FRONT;
import static com.yor42.projectazure.libs.utils.ItemStackUtils.*;
import static com.yor42.projectazure.libs.utils.MathUtil.getRand;
import static com.yor42.projectazure.setup.register.registerManager.*;
import static net.minecraft.entity.ai.brain.memory.MemoryModuleType.*;
import static net.minecraft.entity.ai.brain.schedule.Activity.*;
import static net.minecraft.util.Hand.MAIN_HAND;
import static net.minecraft.util.Hand.OFF_HAND;
import static net.minecraftforge.fml.network.PacketDistributor.TRACKING_ENTITY;
import static net.minecraftforge.fml.network.PacketDistributor.TRACKING_ENTITY_AND_SELF;

public abstract class AbstractEntityCompanion extends TameableEntity implements ICrossbowUser, IAnimatable, IAnimationTickable {
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
    public ItemStackHandler AmmoStorage = new ItemStackHandler(8){
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {

            return stack.getItem() instanceof ItemCannonshell || stack.getItem() instanceof ItemMagazine || stack.getItem() instanceof ArrowItem;
        }
    };

    protected int patTimer;
    protected int shieldCoolDown;
    protected int qinteractionTimer;
    public boolean isClimbingUp = false;
    private int ItemSwapIndexOffhand = -1;
    private int ItemSwapIndexMainHand = -1;
    protected int lastAggroedTimeStamp = 0;
    protected int toldtomovecount = 0;
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
    public boolean canUseCrossbow;
    public boolean canUseBow;
    protected long lastSlept, lastWokenup;
    private int forcewakeupExpireTimer, forceWakeupCounter, expdelay;
    protected int StartedMeleeAttackTimeStamp = -1;
    protected int AttackCount = 0;
    protected int StartedSpellAttackTimeStamp = -1;

    //I'd really like to get off from datamanager's wild ride.
    protected static final DataParameter<Integer> SPELLDELAY = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.INT);
    protected static final DataParameter<Integer> NONVANILLAMELEEATTACKDELAY = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.INT);
    protected static final DataParameter<Integer> SKILLDELAYTICK = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.INT);
    protected static final DataParameter<Boolean> CHARGING_CROSSBOW = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> USING_SKILL = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Float> MORALE = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.FLOAT);
    protected static final DataParameter<Float> EXP = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.FLOAT);
    protected static final DataParameter<Integer> LIMITBREAKLEVEL = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.INT);
    protected static final DataParameter<Integer> LEVEL = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.INT);
    protected static final DataParameter<Integer> ANGRYTIMER = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.INT);
    protected static final DataParameter<Integer> INJURYCURETIMER = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.INT);
    protected static final DataParameter<Float> AFFECTION = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.FLOAT);
    protected static final DataParameter<Boolean> SITTING = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> CRITICALLYINJURED = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> OPENINGDOOR = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> MELEEATTACKING = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> USINGBOW = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Integer> MAXPATEFFECTCOUNT = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.INT);
    protected static final DataParameter<Integer> PATEFFECTCOUNT = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.INT);
    protected static final DataParameter<Integer> PATCOOLDOWN = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.INT);
    protected static final DataParameter<Integer> MOVE_MODE = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.INT);
    protected static final DataParameter<Integer> PAT_ANIMATION_TIME = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.INT);
    protected static final DataParameter<Integer> ECCI_ANIMATION_TIME = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.INT);
    protected static final DataParameter<Integer> INTERACTION_WARNING_COUNT = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.INT);
    protected static final DataParameter<Boolean> OATHED = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> PICKUP_ITEM = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> ISFREEROAMING = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Optional<BlockPos>> STAYPOINT = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.OPTIONAL_BLOCK_POS);
    protected static final DataParameter<Optional<BlockPos>> HOMEPOS = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.OPTIONAL_BLOCK_POS);
    protected static final DataParameter<Float> VALID_HOME_DISTANCE = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.FLOAT);
    protected static final DataParameter<Boolean> ISFORCEWOKENUP = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> ISUSINGGUN = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> USINGWORLDSKILL = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Integer> HEAL_TIMER = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.INT);
    protected static final DataParameter<Integer> SKILL_POINTS = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.INT);
    protected static final DataParameter<Integer> SKILL_ANIMATION_TIME = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.INT);
    protected static final DataParameter<ItemStack> SKILL_ITEM_0 = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.ITEM_STACK);
    protected static final DataParameter<ItemStack> SKILL_ITEM_1 = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.ITEM_STACK);
    protected static final DataParameter<ItemStack> SKILL_ITEM_2 = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.ITEM_STACK);
    protected static final DataParameter<ItemStack> SKILL_ITEM_3 = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.ITEM_STACK);
    protected static final DataParameter<Integer> RELOAD_TIMER_MAINHAND = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.INT);
    protected static final DataParameter<Integer> RELOAD_TIMER_OFFHAND = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.INT);
    protected static final DataParameter<Integer> FOODLEVEL = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.INT);
    protected static final DataParameter<Optional<UUID>> TeamUUID = EntityDataManager.defineId(AbstractEntityCompanion.class, DataSerializers.OPTIONAL_UUID);

    private static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
            HOME, RESTING.get(), ATTACK_TARGET, ATTACK_COOLING_DOWN, registerManager.VISIBLE_ALLYS_COUNT.get(),
            registerManager.VISIBLE_HOSTILE_COUNT.get(),registerManager.VISIBLE_HOSTILES.get(),registerManager.NEARBY_HOSTILES.get(),
            registerManager.WAIT_POINT.get(), registerManager.HEAL_TARGET.get(), MEMORY_SITTING.get(), MemoryModuleType.LIVING_ENTITIES,
            MemoryModuleType.VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_PLAYERS, NEAREST_HOSTILE,
            MemoryModuleType.NEAREST_VISIBLE_PLAYER, RIDE_TARGET, registerManager.WAIT_POINT.get(), registerManager.NEARBY_ALLYS.get(),
            registerManager.VISIBLE_ALLYS.get(), MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER,
            FOOD_INDEX.get(), HEAL_POTION_INDEX.get(),REGENERATION_POTION_INDEX.get(),TOTEM_INDEX.get(),
            TORCH_INDEX.get(),FIRE_EXTINGIGH_ITEM.get(),FALL_BREAK_ITEM_INDEX.get(),
            MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleType.WALK_TARGET, MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.BREED_TARGET, MemoryModuleType.PATH,
            MemoryModuleType.DOORS_TO_CLOSE, MemoryModuleType.NEAREST_BED, MemoryModuleType.HURT_BY,
            MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.LAST_SLEPT,
            MemoryModuleType.LAST_WOKEN, HURT_AT.get(), NEAREST_BOAT.get(), NEAREST_ORE.get(), NEAREST_HARVESTABLE.get(), NEAREST_PLANTABLE.get(),
            NEAREST_BONEMEALABLE.get(), NEAREST_WORLDSKILLABLE.get(), FOLLOWING_OWNER_MEMORY.get());
    private static final ImmutableList<SensorType<? extends Sensor<? super AbstractEntityCompanion>>> SENSOR_TYPES = ImmutableList.of(
            SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS,
            SensorType.NEAREST_ITEMS, SensorType.NEAREST_BED, SensorType.HURT_BY,
            registerManager.ENTITY_SENSOR.get(), WORLD_SENSOR.get(),
            INVENTORY_SENSOR.get());

    public static final Map<MemoryModuleType<GlobalPos>, BiPredicate<AbstractEntityCompanion, PointOfInterestType>> POI_MEMORIES = ImmutableMap.of(HOME, (p_213769_0_, p_213769_1_) -> p_213769_1_ == PointOfInterestType.HOME);
    public abstract enums.EntityType getEntityType();
    protected AbstractEntityCompanion(EntityType<? extends TameableEntity> type, World worldIn) {
        super(type, worldIn);
        this.setAffection(40F);
        this.swimmingNav = new CompanionSwimPathNavigator(this, worldIn);
        this.groundNav = new CompanionGroundPathNavigator(this, worldIn);
        this.SwimController = new CompanionSwimMovementController(this);
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
        this.noCulling = true;
        this.canUseCrossbow = false;
        this.canUseBow = false;
    }

    @Override
    public boolean startRiding(@Nonnull Entity p_184205_1_, boolean p_184205_2_) {
        if (!super.startRiding(p_184205_1_, p_184205_2_)) {
            return false;
        } else {
            if (p_184205_1_ instanceof BoatEntity) {
                this.yRotO = p_184205_1_.yRot;
                this.yRot = p_184205_1_.yRot;
            }

            return true;
        }
    }

    public boolean isAlly(LivingEntity entity){

        if(this.getOwner()==null){
            return false;
        }

        if(entity instanceof TameableEntity){
            return ((TameableEntity) entity).isOwnedBy(this.getOwner());
        }
        else if(entity instanceof AbstractEntityPlanes){
            return ((AbstractEntityPlanes) entity).getOwner().isOwnedBy(this.getOwner());
        }
        return false;

    }

    public void setTeam(@Nullable UUID team) {
        this.getEntityData().set(TeamUUID, Optional.ofNullable(team));
    }

    public void removeTeam(){
        this.getEntityData().set(TeamUUID, Optional.empty());
    }

    public Optional<UUID> getTeamUUID(){
        return this.getEntityData().get(TeamUUID);
    }

    public Optional<CompanionTeam> getCompanionTeam(){
        if(this.getCommandSenderWorld().isClientSide()){
            return this.getCompanionTeamClient();
        }
        else{
            ServerWorld world = (ServerWorld) this.getCommandSenderWorld();
            return this.getTeamUUID().flatMap((UUID)-> ProjectAzureWorldSavedData.getSaveddata(world).getTeambyUUID(UUID));
        }
    }

    @Nonnull
    @Override
    public ITextComponent getName() {
        ITextComponent itextcomponent = this.getCustomName();
        if(itextcomponent==null && this.isOathed() && this instanceof IAknOp) {
            ITextComponent realname = ((IAknOp) this).getRealname();
            if(realname!=null) {
                return realname;
            }
        }
        return super.getName();
    }

    @OnlyIn(Dist.CLIENT)
    private Optional<CompanionTeam> getCompanionTeamClient(){
        return this.getTeamUUID().flatMap(UUID -> ProjectAzureWorldSavedData.getTeambyUUIDClient(uuid));
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
        return this.getMainHandItem().getItem() instanceof GunItem && this.getEntityData().get(ISUSINGGUN);
    }

    public ItemStack[] getMagazine(ResourceLocation id){
        ArrayList<ItemStack> stacks = new ArrayList();
        for(int i = 0; i < this.getAmmoStorage().getSlots(); ++i) {
            ItemStack stack = this.getAmmoStorage().getStackInSlot(i);
            if (isAmmo(stack, id)) {
                stacks.add(stack);
            }
        }
        return stacks.toArray(new ItemStack[0]);
    }

    private static boolean isAmmo(ItemStack stack, ResourceLocation id) {
        return stack != null && stack.getItem().getRegistryName().equals(id);
    }

    protected boolean isMoving(){
        return  !(this.animationSpeed > -0.1F && this.animationSpeed < 0.1F) && !this.isOrderedToSit();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if(capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            return LazyOptional.of(this::getInventory).cast();
        }
        return super.getCapability(capability, facing);
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@Nonnull DamageSource damageSourceIn) {
        return this.isBlocking()? SoundEvents.SHIELD_BLOCK:super.getHurtSound(damageSourceIn);
    }

    @Override
    protected void hurtArmor(@Nonnull DamageSource damageSource, float damage) {
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
        compound.putInt("injury_curetimer", this.entityData.get(INJURYCURETIMER));
        compound.putBoolean("oathed", this.entityData.get(OATHED));
        compound.putBoolean("pickupitem", this.entityData.get(PICKUP_ITEM));
        compound.putFloat("home_distance", this.entityData.get(VALID_HOME_DISTANCE));
        compound.putBoolean("isCriticallyInjured", this.entityData.get(CRITICALLYINJURED));
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
        this.getBrain().getMemory(HOME).ifPresent((pos)->{
            compound.putDouble("HomePosX", pos.pos().getX());
            compound.putDouble("HomePosY", pos.pos().getY());
            compound.putDouble("HomePosZ", pos.pos().getZ());
            compound.putString("HomePosDim", pos.dimension().location().toString());
        });

        CompoundNBT NBT = new CompoundNBT();
        this.getBrain().getActiveNonCoreActivity().ifPresent((activity)->{
            if(activity == REST){
                NBT.putString("status", "resting");
            }
            else if(activity == (IDLE)){
                NBT.putString("status", "idle");
            }
            else if(activity == registerManager.SITTING.get()){
                NBT.putString("status", "sitting");
            }
            else if(activity == WAITING.get()){
                NBT.putString("status", "waiting");
                this.getBrain().getMemory(WAIT_POINT.get()).ifPresent((globalpos)->{
                    NBT.putString("waitpointDim", globalpos.dimension().location().toString());
                    NBT.putInt("waitpointx", globalpos.pos().getX());
                    NBT.putInt("waitpointy", globalpos.pos().getY());
                    NBT.putInt("waitpointz", globalpos.pos().getZ());
                });
            }
            else{
                NBT.putString("status", "following");
            }
        });

        compound.put("status", NBT);

        compound.putBoolean("shouldbeSitting", this.shouldBeSitting);
        compound.putBoolean("isforcewokenup", this.entityData.get(ISFORCEWOKENUP));
        compound.putDouble("exp", this.entityData.get(EXP));
        compound.putInt("level", this.getEntityData().get(LEVEL));
        compound.putInt("limitbreaklv", this.entityData.get(LIMITBREAKLEVEL));
        compound.putInt("awaken", this.awakeningLevel);
        compound.putDouble("morale", this.entityData.get(MORALE));
        compound.putLong("lastslept", this.lastSlept);
        compound.putLong("lastwoken", this.lastWokenup);
        compound.putBoolean("isMovingtoRecruitStation", this.isMovingtoRecruitStation);
        compound.put("inventory", this.getInventory().serializeNBT());
        compound.putInt("shieldcooldown", this.shieldCoolDown);
        compound.put("ammostorage", this.getAmmoStorage().serializeNBT());
        this.getEntityData().get(TeamUUID).ifPresent((UUID)->compound.putUUID("team", UUID));
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
        this.getEntityData().set(CRITICALLYINJURED, compound.getBoolean("isCriticallyInjured"));
        this.entityData.set(PICKUP_ITEM, compound.getBoolean("pickupitem"));
        boolean hasStayPos = compound.contains("stayX") && compound.contains("stayY") && compound.contains("stayZ");
        if(hasStayPos) {
            this.entityData.set(STAYPOINT, Optional.of(new BlockPos(compound.getDouble("stayX"), compound.getDouble("stayY"), compound.getDouble("stayZ"))));
        }
        this.entityData.set(VALID_HOME_DISTANCE, compound.getFloat("home_distance"));
        boolean hasRecruitBeaconPos = compound.contains("RecruitStationPosX") && compound.contains("RecruitStationPosY") && compound.contains("RecruitStationPosZ");
        if(hasRecruitBeaconPos) {
            this.RECRUIT_BEACON_POS = new BlockPos(compound.getDouble("RecruitStationPosX"), compound.getDouble("RecruitStationPosY"), compound.getDouble("RecruitStationPosZ"));
        }

        boolean hasHomePos = compound.contains("HomePosX") && compound.contains("HomePosY") && compound.contains("HomePosZ") && compound.contains("HomePosDim");
        if(hasHomePos) {
            BlockPos blockpos = new BlockPos(compound.getDouble("HomePosX"), compound.getDouble("HomePosY"), compound.getDouble("HomePosZ"));
            ResourceLocation resource = new ResourceLocation(compound.getString("HomePosDim"));
            RegistryKey<World> registrykey = RegistryKey.create(Registry.DIMENSION_REGISTRY, resource);
            GlobalPos pos = GlobalPos.of(registrykey, blockpos);
            this.getBrain().setMemory(HOME, pos);
            this.entityData.set(HOMEPOS, Optional.of(blockpos));
        }
        if(compound.getBoolean("isresting")){
            this.getBrain().setMemory(RESTING.get(), true);
        }

        CompoundNBT NBT = compound.getCompound("status");

        switch (NBT.getString("status")){
            case "resting":{
                this.getBrain().setActiveActivityIfPossible(REST);
                this.setFreeRoaming(true);
                break;
            }
            case "idle":{
                this.setResting();
                this.entityData.set(ISFREEROAMING, true);
                this.getBrain().setMemory(RESTING.get(), true);
                break;
            }
            case "sitting":{
                this.setOrderedToSit(true);
                this.entityData.set(ISFREEROAMING, false);
                break;
            }
            case "waiting":{
                BlockPos blockpos = new BlockPos(NBT.getDouble("waitpointx"), NBT.getDouble("waitpointy"), NBT.getDouble("waitpointz"));
                ResourceLocation resource = new ResourceLocation(NBT.getString("waitpointDim"));
                RegistryKey<World> registrykey = RegistryKey.create(Registry.DIMENSION_REGISTRY, resource);
                this.entityData.set(ISFREEROAMING, true);
                GlobalPos pos = GlobalPos.of(registrykey, blockpos);
                this.getBrain().setMemory(WAIT_POINT.get(), pos);
                break;
            }
            default:
                this.setFollowingOwner();
                this.entityData.set(ISFREEROAMING, false);
        }

        this.getEntityData().set(TeamUUID, compound.hasUUID("team")? Optional.of(compound.getUUID("team")):Optional.empty());
        this.entityData.set(ISFORCEWOKENUP, compound.getBoolean("isforcewokenup"));
        this.shouldBeSitting = compound.getBoolean("shouldbeSitting");
        this.getEntityData().set(LEVEL, compound.getInt("level"));
        this.entityData.set(EXP, compound.getFloat("exp"));
        this.entityData.set(MORALE, compound.getFloat("morale"));
        this.entityData.set(ANGRYTIMER, compound.getInt("angrytimer"));
        this.entityData.set(INJURYCURETIMER, compound.getInt("injury_curetimer"));
        this.entityData.set(LIMITBREAKLEVEL, compound.getInt("limitbreaklv"));
        this.shieldCoolDown = compound.getInt("shieldcooldown");
        this.awakeningLevel = compound.getInt("awaken");
        this.isMovingtoRecruitStation = compound.getBoolean("isMovingtoRecruitStation");
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

    public float getAttackDamageMainHand(){
        return this.getAttackDamageForStack(this.getMainHandItem());
    }

    public float getAttackDamageOffHand(){
        return this.getAttackDamageForStack(this.getOffhandItem());
    }

    public float getAttackDamageForStack(ItemStack stack){
        Item item = stack.getItem();
        if(item instanceof TieredItem){
            float dmg;
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

    @Override
    protected void tickDeath() {
        ++this.deathTime;
        int deathtimethreshold = this.isCriticallyInjured()? 40:24000;
        if (this.deathTime >= deathtimethreshold) {
            for(int i = 0; i < 20; ++i) {
                double d0 = this.random.nextGaussian() * 0.02D;
                double d1 = this.random.nextGaussian() * 0.02D;
                double d2 = this.random.nextGaussian() * 0.02D;
                this.level.addParticle(ParticleTypes.POOF, this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D), d0, d1, d2);
            }
            LivingEntity livingentity = this.getKillCredit();
            this.dropEquipment();
            this.createWitherRose(livingentity);
            this.getTeamUUID().ifPresent((team)->{
                Main.NETWORK.sendToServer(new EditTeamMemberPacket(team, this.getUUID(), EditTeamMemberPacket.ACTION.REMOVE));
            });
            this.remove(false);
        }
    }

    public void die(@Nonnull DamageSource p_70645_1_) {
        if (net.minecraftforge.common.ForgeHooks.onLivingDeath(this, p_70645_1_)) return;
        if (!this.level.isClientSide && this.level.getGameRules().getBoolean(GameRules.RULE_SHOWDEATHMESSAGES) && this.getOwner() instanceof ServerPlayerEntity) {
            this.getOwner().sendMessage(this.getCombatTracker().getDeathMessage(), Util.NIL_UUID);
        }
        if (!this.removed && !this.dead) {
            Entity entity = p_70645_1_.getEntity();
            LivingEntity livingentity = this.getKillCredit();
            if (this.deathScore >= 0 && livingentity != null) {
                livingentity.awardKillScore(this, this.deathScore, p_70645_1_);
            }

            if (this.isSleeping()) {
                this.stopSleeping();
            }

            this.getCombatTracker().recheckStatus();
            if (this.level instanceof ServerWorld) {
                if (entity != null) {
                    entity.killed((ServerWorld)this.level, this);
                }
            }
            this.setMovementMode(MOVE_STATUS.FAINTED);
            this.level.broadcastEntityEvent(this, (byte)3);
        }
    }

    public void reviveCompanion(){
        this.revive();
        this.getBrain().setActiveActivityIfPossible(registerManager.INJURED.get());
        this.setMovementMode(MOVE_STATUS.INJURED);
        this.setHealth(2);
        this.setCriticallyinjured(true);
        this.setOrderedToSit(true);
        this.setPose(Pose.STANDING);
        this.deathTime = 0;
    }

    @Override
    protected void dropAllDeathLoot(DamageSource p_213345_1_) {
        Entity entity = p_213345_1_.getEntity();

        int i = net.minecraftforge.common.ForgeHooks.getLootingLevel(this, entity, p_213345_1_);
        this.captureDrops(new java.util.ArrayList<>());

        boolean flag = this.lastHurtByPlayerTime > 0;
        if (this.shouldDropLoot() && this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            this.dropFromLootTable(p_213345_1_, flag);
            this.dropCustomDeathLoot(p_213345_1_, i, flag);
        }

        this.dropExperience();

        Collection<ItemEntity> drops = captureDrops(new java.util.ArrayList<>());
        if (!net.minecraftforge.common.ForgeHooks.onLivingDrops(this, p_213345_1_, drops, i, lastHurtByPlayerTime > 0))
            drops.forEach(e -> level.addFreshEntity(e));
    }

    @Override
    protected void dropEquipment() {
        for(int i=0; i<this.getInventory().getSlots(); i++){
            ItemStack stack = this.getInventory().getStackInSlot(i);
            if(!stack.isEmpty()) {
                this.spawnAtLocation(stack);
            }
        }

        for(int i=0; i<this.getAmmoStorage().getSlots(); i++){
            ItemStack stack = this.getInventory().getStackInSlot(i);
            if(!stack.isEmpty()) {
                this.spawnAtLocation(stack);
            }
        }

        if(!PAConfig.CONFIG.permadeath.get()){
            this.reviveCompanion();
            this.setLevel(0);
            this.setLimitBreakLv(0);
            this.addAffection(-20);
            ItemStack stack = new ItemStack(registerItems.STASIS_CRYSTAL.get());
            CompoundNBT nbt = stack.getOrCreateTag();
            nbt.putInt("cost", 10+this.getLevel());
            nbt.put("entity", this.serializeNBT());
            this.spawnAtLocation(stack);
        }
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
        this.getBrain().setMemory(HOME, GlobalPos.of(this.getCommandSenderWorld().dimension(), pos));
        this.entityData.set(HOMEPOS, Optional.ofNullable(pos));
        this.entityData.set(VALID_HOME_DISTANCE, validDistance);
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
        return this.entityData.get(HOMEPOS);
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

    private static final Predicate<LivingEntity> HOSTILE_ENTITIES = (entity) -> entity.getSoundSource() == SoundCategory.HOSTILE && !Config.COMMON.aggroMobs.exemptEntities.get().contains(entity.getType().getRegistryName().toString());

    public void AttackUsingGun(LivingEntity target, ItemStack gun){
        if(gun.getItem() instanceof GunItem){
            GunItem item = (GunItem)gun.getItem();
            Gun modifiedGun = item.getModifiedGun(gun);
            if (modifiedGun != null) {
                int count = modifiedGun.getGeneral().getProjectileAmount();
                Gun.Projectile projectileProps = modifiedGun.getProjectile();
                com.tac.guns.entity.ProjectileEntity[] spawnedProjectiles = new com.tac.guns.entity.ProjectileEntity[count];
                for(int i = 0; i < count; ++i) {
                    IProjectileFactory factory = ProjectileManager.getInstance().getFactory(projectileProps.getItem());
                    com.tac.guns.entity.ProjectileEntity projectileEntity = factory.create(this.getCommandSenderWorld(), this, gun, item, modifiedGun);
                    projectileEntity.setWeapon(gun);
                    projectileEntity.setAdditionalDamage(Gun.getAdditionalDamage(gun));
                    this.level.addFreshEntity(projectileEntity);
                    spawnedProjectiles[i] = projectileEntity;
                    projectileEntity.tick();
                }

                if (!projectileProps.isVisible()) {
                    MessageBulletTrail messageBulletTrail = new MessageBulletTrail(spawnedProjectiles, projectileProps, this.getId());
                    PacketHandler.getPlayChannel().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getX(), this.getY(), this.getZ(), (Double) Config.COMMON.network.projectileTrackingRange.get(), this.level.dimension())), messageBulletTrail);
                }

                double posY;
                double posZ;
                double posX;
                if (Config.COMMON.aggroMobs.enabled.get()) {
                    double radius = GunModifierHelper.getModifiedFireSoundRadius(gun, Config.COMMON.aggroMobs.range.get());
                    posX = this.getX();
                    posY = this.getY() + 0.5D;
                    posZ = this.getZ();
                    AxisAlignedBB box = new AxisAlignedBB(posX - radius, posY - radius, posZ - radius, posX + radius, posY + radius, posZ + radius);
                    radius *= radius;

                    for (LivingEntity entity : this.getCommandSenderWorld().getEntitiesOfClass(LivingEntity.class, box, HOSTILE_ENTITIES)) {
                        double dx = posX - entity.getX();
                        double dy = posY - entity.getY();
                        double dz = posZ - entity.getZ();
                        if (dx * dx + dy * dy + dz * dz <= radius) {
                            entity.setLastHurtByMob(Config.COMMON.aggroMobs.angerHostileMobs.get() ? this : entity);
                        }
                    }
                }

                boolean silenced = GunModifierHelper.isSilencedFire(gun);
                ResourceLocation fireSound = silenced ? modifiedGun.getSounds().getSilencedFire() : modifiedGun.getSounds().getFire();
                if (fireSound != null) {
                    posX = this.getX();
                    posY = this.getY() + (double)this.getEyeHeight();
                    posZ = this.getZ();
                    float volume = GunModifierHelper.getFireSoundVolume(gun);
                    float pitch = 0.9F + this.getCommandSenderWorld().random.nextFloat() * 0.2F;
                    double radius = GunModifierHelper.getModifiedFireSoundRadius(gun, (Double)Config.SERVER.gunShotMaxDistance.get());
                    boolean muzzle = modifiedGun.getDisplay().getFlash() != null;
                    MessageGunSound messageSound = new MessageGunSound(fireSound, SoundCategory.PLAYERS, (float)posX, (float)posY, (float)posZ, volume, pitch, this.getId(), muzzle, false);
                    PacketDistributor.TargetPoint targetPoint = new PacketDistributor.TargetPoint(posX, posY, posZ, radius, this.level.dimension());
                    PacketHandler.getPlayChannel().send(PacketDistributor.NEAR.with(() -> targetPoint), messageSound);
                }

                CompoundNBT tag = gun.getOrCreateTag();
                if (!tag.getBoolean("IgnoreAmmo")) {
                    int level = EnchantmentHelper.getItemEnchantmentLevel((Enchantment) ModEnchantments.RECLAIMED.get(), gun);
                    if (level == 0 || this.level.random.nextInt(4 - MathHelper.clamp(level, 1, 2)) != 0) {
                        tag.putInt("AmmoCount", Math.max(0, tag.getInt("AmmoCount") - 1));
                    }
                }
            }
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
        if(this.isUsingItem()) {
            return this.getItemInHand(this.getUsedItemHand()).getUseAnimation() == UseAction.EAT || this.getItemInHand(this.getUsedItemHand()).getUseAnimation() == UseAction.DRINK;
        }
        return false;
    }

    public void setChargingCrossbow(boolean value){
        this.entityData.set(CHARGING_CROSSBOW, value);
    }

    public boolean isChargingCrossbow(){
       return this.entityData.get(CHARGING_CROSSBOW);
    }

    @Override
    public void shootCrossbowProjectile(LivingEntity p_230284_1_, ItemStack p_230284_2_, ProjectileEntity p_230284_3_, float p_230284_4_) {
        this.shootCrossbowProjectile(this, p_230284_1_, p_230284_3_, p_230284_4_, 1.6F);
    }

    @Override
    public void performCrossbowAttack(LivingEntity p_234281_1_, float p_234281_2_) {
        Hand hand = ProjectileHelper.getWeaponHoldingHand(this, item -> item instanceof CrossbowItem);
        ItemStack itemstack = this.getItemInHand(hand);
        if (this.getMainHandItem().getItem() instanceof CrossbowItem) {
            CrossbowItem.performShooting(this.level, this, hand, itemstack, p_234281_2_, (float)(14 - p_234281_1_.level.getDifficulty().getId() * 4));
        }

        this.onCrossbowAttackPerformed();
    }

    @Override
    public void onCrossbowAttackPerformed() {
        this.noActionTime = 0;
    }

    public ItemStack getProjectile(ItemStack p_213356_1_) {
        if (!(p_213356_1_.getItem() instanceof ShootableItem)) {
            return ItemStack.EMPTY;
        } else {
            Predicate<ItemStack> predicate = ((ShootableItem)p_213356_1_.getItem()).getSupportedHeldProjectiles();
            ItemStack itemstack = ShootableItem.getHeldProjectile(this, predicate);
            if (!itemstack.isEmpty()) {
                return itemstack;
            } else {
                predicate = ((ShootableItem)p_213356_1_.getItem()).getAllSupportedProjectiles();

                for(int i = 0; i < this.getAmmoStorage().getSlots(); ++i) {
                    ItemStack itemstack1 = this.getAmmoStorage().getStackInSlot(i);
                    if (predicate.test(itemstack1)) {
                        return itemstack1;
                    }
                }

                return ItemStack.EMPTY;
            }
        }
    }

    public void performRangedAttack(LivingEntity p_82196_1_, float p_82196_2_) {

        ItemStack itemstack = this.getProjectile(this.getItemInHand(MAIN_HAND));
        ArrowItem arrowitem = (ArrowItem)(itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);
        AbstractArrowEntity abstractarrowentity = arrowitem.createArrow(this.level, itemstack, this);
        if (this.getMainHandItem().getItem() instanceof net.minecraft.item.BowItem)
            abstractarrowentity = ((net.minecraft.item.BowItem)this.getMainHandItem().getItem()).customArrow(abstractarrowentity);
        double d0 = p_82196_1_.getX() - this.getX();
        double d1 = p_82196_1_.getY(0.3333333333333333D) - abstractarrowentity.getY();
        double d2 = p_82196_1_.getZ() - this.getZ();
        double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
        abstractarrowentity.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, (float)(14 - this.level.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.ARROW_SHOOT,1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(abstractarrowentity);
    }

    public boolean isFreeRoaming() {
        return this.entityData.get(ISFREEROAMING);
    }

    public void setFreeRoaming(boolean value) {
        this.getNavigation().stop();
        this.entityData.set(ISFREEROAMING, value);
        if(value) {
            this.setRestOrStay();
            //this.setStayCenterPos(this.blockPosition());
        }
        else{
            //this.getBrain().setActiveActivityIfPossible(registerManager.FOLLOWING_OWNER.get());
            this.setFollowingOwner();
            //this.clearStayCenterPos();
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
        if (gunstack.getItem() instanceof GunItem) {
            GunItem gunitem = (GunItem) gunstack.getItem();
            CompoundNBT tag = gunstack.getOrCreateTag();
            int ammocount = tag.getInt("AmmoCount");
            boolean hasAmmo = ammocount>0;
            return (!this.canUseCannonOrTorpedo() || !this.isSailing()) && this.getGunStack() != ItemStack.EMPTY && (this.HasRightMagazine(gunstack) || hasAmmo);
        }
        return false;
    }

    public boolean HasRightMagazine(ItemStack gunStack) {
        Item gunItem = gunStack.getItem();
        if(!(gunItem instanceof GunItem)){
            return false;
        }
        Gun gun = ((GunItem)gunStack.getItem()).getModifiedGun(gunStack);

        return this.getMagazine(gun.getProjectile().getItem()).length >0;
    }

    public boolean isSailing(){
        return false;
    }

    @Override
    public boolean wantsToPickUp(ItemStack stack) {
        boolean isInventoryfree = false;
        for(int i=0; i<this.getInventory().getSlots(); i++){
            if(this.getInventory().insertItem(i, stack, true) == ItemStack.EMPTY){
                isInventoryfree = true;
                break;
            }
        }

        return this.shouldPickupItem() && (this.canEquip(stack) || isInventoryfree);
    }

    private boolean canEquip(ItemStack stack){
        EquipmentSlotType equipmentslottype = getEquipmentSlotForItem(stack);
        ItemStack itemstack = this.getItemBySlot(equipmentslottype);
        if(this.canReplaceCurrentItem(stack, itemstack)){
            if(!itemstack.isEmpty()){
                for(int i=0; i<this.getInventory().getSlots(); i++){
                    if(this.getInventory().insertItem(i, itemstack, true) == ItemStack.EMPTY){
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }

    @Override
    protected void pickUpItem(ItemEntity p_175445_1_) {
        ItemStack itemstack = p_175445_1_.getItem();
        if (this.equipItemIfPossible(itemstack)) {
            this.onItemPickup(p_175445_1_);
            this.take(p_175445_1_, itemstack.getCount());
            p_175445_1_.remove();
        }
        else{
            for(int i=0; i<this.getInventory().getSlots(); i++){
                if(this.getInventory().insertItem(i, itemstack, true) == ItemStack.EMPTY){
                    this.getInventory().insertItem(i, itemstack, false);
                    this.onItemPickup(p_175445_1_);
                    this.take(p_175445_1_, itemstack.getCount());
                    p_175445_1_.remove();
                    break;
                }
            }
        }
    }

    @Override
    public boolean canPickUpLoot() {
        return true;
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
    public double getMyRidingOffset() {
        if(this.getVehicle() instanceof PlayerEntity){
            return 0.45D;
        }
        else {
            return 0.3D;
        }
    }

    @Override
    public void remove(boolean keepData) {
        if(this.getOwner() instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) this.getOwner();
            ProjectAzurePlayerCapability.getCapability(player).removeCompanion(this);
        }
        if(!this.getCommandSenderWorld().isClientSide()){
            this.getTeamUUID().ifPresent((team)-> ProjectAzureWorldSavedData.getSaveddata((ServerWorld) this.getCommandSenderWorld()).removeMember(team, this.getUUID()));
        }
        this.removeTeam();
        super.remove(keepData);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(this.getVehicle() != null && source.getEntity() == this.getVehicle() || source == DamageSource.IN_WALL) {
            return false;
        }
        else if(source.getEntity() instanceof TameableEntity && this.getOwner() != null && ((TameableEntity) source.getEntity()).isOwnedBy(this.getOwner())){
            return false;
        }
        else if(source.getEntity() instanceof AbstractEntityDrone && ((AbstractEntityDrone) source.getEntity()).getOwner().isPresent() && (((AbstractEntityDrone) source.getEntity()).getOwner().get() == this ||  (((AbstractEntityDrone) source.getEntity()).getOwner().get() instanceof AbstractEntityCompanion && this.getOwner() != null && ((AbstractEntityCompanion) ((AbstractEntityDrone) source.getEntity()).getOwner().get()).isOwnedBy(this.getOwner())))){
            return false;
        }
        if(source.getEntity() instanceof PlayerEntity && this.isOwnedBy((LivingEntity) source.getEntity())){
            if(this.toldtomovecount >=3){
                this.toldtomovecount = 0;
                Vector3d loc = this.WanderRNG();
                if(loc != null) {
                    this.getNavigation().moveTo(loc.x, loc.y, loc.z, 1);
                }
            }
            else{
                this.toldtomovecount +=1;
            }
            return false;
        }

        boolean flag = super.hurt(source, amount);
        if (this.level.isClientSide) {
            return false;
        } else {
            if(source.getEntity() instanceof LivingEntity) {
                CompanionTasks.wasHurtBy(this, (LivingEntity) source.getEntity());
            }
            else{
                this.getBrain().setMemoryWithExpiry(HURT_AT.get(), this.blockPosition(), 400L);
            }
            return flag;
        }
    }

    protected Vector3d WanderRNG() {

        if (!this.getStayCenterPos().isPresent()){
            this.setStayCenterPos(this.blockPosition());
        }

        BlockPos homepos = this.getStayCenterPos().get();
        BlockPos originPosition;
        boolean flag = false;

        if (this.isWithinRestriction()) {
            homepos = this.getRestrictCenter();
            flag = this.getCommandSenderWorld().dimension() == World.OVERWORLD && this.blockPosition().closerThan(homepos, 32);
        }

        originPosition = flag? homepos : this.getStayCenterPos().get();

        int x = ((int)(getRand().nextFloat()*6)-3);
        int z = ((int)(getRand().nextFloat()*6)-3);

        return new Vector3d(originPosition.getX()+x, originPosition.getY(), originPosition.getZ()+z);
    }

    public int getShieldCoolDown(){
        return this.shieldCoolDown;
    }

    @Override
    protected void blockUsingShield(@Nonnull LivingEntity entityIn) {
        super.blockUsingShield(entityIn);
        if (entityIn.getMainHandItem().canDisableShield(this.useItem, this, entityIn))
            this.disableShield(true);
    }

    @Override
    public void startUsingItem(@Nonnull Hand hand) {
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
        AnimationController UpperBodycontroller = new AnimationController<>(this, "controller_lowerbody", 1, this::predicate_lowerbody);
        animationData.addAnimationController(UpperBodycontroller);
        animationData.addAnimationController(new AnimationController<>(this, "controller_upperbody", 1, this::predicate_upperbody));
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
        this.entityData.define(CHARGING_CROSSBOW, false);
        this.entityData.define(LEVEL, 0);
        this.entityData.define(AFFECTION, 0.0F);
        this.entityData.define(SITTING, false);
        this.entityData.define(OPENINGDOOR, false);
        this.entityData.define(MELEEATTACKING, false);
        this.entityData.define(CRITICALLYINJURED, false);
        this.entityData.define(MAXPATEFFECTCOUNT, 0);
        this.entityData.define(PATEFFECTCOUNT, 0);
        this.getEntityData().define(NONVANILLAMELEEATTACKDELAY, 0);
        this.entityData.define(PATCOOLDOWN, 0);
        this.entityData.define(MOVE_MODE, 0);
        this.entityData.define(PAT_ANIMATION_TIME, 0);
        this.entityData.define(OATHED, false);
        this.entityData.define(USINGBOW, false);
        this.entityData.define(USING_SKILL, false);
        this.entityData.define(STAYPOINT, Optional.empty());
        this.entityData.define(HOMEPOS, Optional.empty());
        this.entityData.define(ISFORCEWOKENUP, false);
        this.entityData.define(ISUSINGGUN, false);
        this.entityData.define(USINGWORLDSKILL, false);
        this.entityData.define(ISFREEROAMING, false);
        this.entityData.define(VALID_HOME_DISTANCE, -1.0f);
        this.entityData.define(HEAL_TIMER, 0);
        this.entityData.define(SKILL_POINTS, 0);
        this.entityData.define(SKILL_ANIMATION_TIME, 0);
        this.entityData.define(RELOAD_TIMER_MAINHAND, 0);
        this.entityData.define(RELOAD_TIMER_OFFHAND, 0);
        this.entityData.define(PICKUP_ITEM, false);
        this.entityData.define(FOODLEVEL, 0);
        this.entityData.define(TeamUUID, Optional.empty());
        this.entityData.define(ANGRYTIMER, 0);
        this.entityData.define(INJURYCURETIMER, -1);
        this.entityData.define(ECCI_ANIMATION_TIME, 0);
        this.entityData.define(INTERACTION_WARNING_COUNT, 0);
        this.entityData.define(SKILL_ITEM_0, ItemStack.EMPTY);
        this.entityData.define(SKILL_ITEM_1, ItemStack.EMPTY);
        this.entityData.define(SKILL_ITEM_2, ItemStack.EMPTY);
        this.entityData.define(SKILL_ITEM_3, ItemStack.EMPTY);
    }

    public boolean isUsingWorldSkill() {
        return this.getEntityData().get(USINGWORLDSKILL);
    }

    public void setUsingWorldSkill(boolean value){
        this.getEntityData().set(USINGWORLDSKILL, value);
    }

    public int getInjuryCureTimer(){
        return this.entityData.get(INJURYCURETIMER);
    }

    public void setInjurycuretimer(int value){
        this.entityData.set(INJURYCURETIMER, value);
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.isCriticallyInjured() || this.isSleeping();
    }

    public boolean isCriticallyInjured(){
        return this.entityData.get(CRITICALLYINJURED);
    }

    public void setCriticallyinjured(boolean value){
        this.entityData.set(CRITICALLYINJURED, value);
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

    @Nonnull
    @MethodsReturnNonnullByDefault
    public ItemStack eat(@Nonnull World WorldIn, @Nonnull ItemStack foodStack) {
        this.getFoodStats().consume(foodStack.getItem(), foodStack);
        if(!this.getCommandSenderWorld().isClientSide()) {
            Main.NETWORK.send(TRACKING_ENTITY.with(() -> this), new spawnParticlePacket(this, foodStack));
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

        if(this.isAngry() || this.isSleeping()){
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

    public int getSkillPoints(){
        return this.entityData.get(SKILL_POINTS);
    }

    public void setSkillPoints(int points) {
        this.entityData.set(SKILL_POINTS, points);
    }

    public void addSkillPoints(int points) {
        int previousValue = this.getSkillPoints();
        int value = previousValue + points;
        if(this.maxSkillPoint()>0){
            value = Math.min(value, this.maxSkillPoint());
        }
        this.setSkillPoints(Math.max(0, value));
    }

    public void addSkillPoints() {
        this.addSkillPoints(1);
    }

    public int maxSkillPoint(){
        return 0;
    }

    public int getSkillAnimationTime(){
        return this.entityData.get(SKILL_ANIMATION_TIME);
    }

    public void setSkillAnimationTime(int value){
        this.entityData.set(SKILL_ANIMATION_TIME, value);
    }

    public void addSkillAnimationTime(int points) {
        int previousValue = this.getSkillAnimationTime();
        int value = previousValue + points;
        this.setSkillAnimationTime(Math.max(0, value));
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

        if(this.isSprinting() && !this.isMoving()){
            this.setSprinting(false);
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

        if(this.getCommandSenderWorld().isClientSide() && this.getEntityData().get(SITTING)!=this.isOrderedToSit()){
            super.setOrderedToSit(this.getEntityData().get(SITTING));
            this.refreshDimensions();
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

        if(this.expdelay>0){
            this.expdelay--;
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
                this.getBrain().eraseMemory(WALK_TARGET);
                this.navigation.stop();
                if (this.patTimer % 30 == 0 && !this.getCommandSenderWorld().isClientSide()) {
                    if (this.entityData.get(PATEFFECTCOUNT) < this.entityData.get(MAXPATEFFECTCOUNT)) {
                        this.entityData.set(PATEFFECTCOUNT, this.entityData.get(PATEFFECTCOUNT) + 1);
                        this.addAffection(0.025);
                        this.addMorale(0.5);
                        Main.NETWORK.send(TRACKING_ENTITY.with(() -> this), new spawnParticlePacket(this, spawnParticlePacket.Particles.AFFECTION_HEART));

                    } else {
                        this.entityData.set(PATEFFECTCOUNT, this.entityData.get(MAXPATEFFECTCOUNT));
                        if (this.entityData.get(PATCOOLDOWN) == 0) {
                            this.entityData.set(PATCOOLDOWN, (7 + this.random.nextInt(5)) * 1200);
                        }
                        Main.NETWORK.send(TRACKING_ENTITY.with(() -> this), new spawnParticlePacket(this, spawnParticlePacket.Particles.AFFECTION_SMOKE));
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
                if (!this.isAngry() && this.getOwner() != null && this.getOwner().isAlive() && this.getEntityData().get(ECCI_ANIMATION_TIME) > 0) {
                    this.lookAt(this.getOwner(), 30F, 30F);
                    this.getBrain().eraseMemory(WALK_TARGET);
                    this.getLookControl().setLookAt(this.getOwner(), 30, 30);
                    int interactionTime = this.getEntityData().get(ECCI_ANIMATION_TIME);
                    this.getEntityData().set(ECCI_ANIMATION_TIME, interactionTime - 1);
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
                            if(this.isOrderedToSit()){
                                this.setOrderedToSit(false);
                            }
                            this.getEntityData().set(ECCI_ANIMATION_TIME, 0);
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


            this.UpdateandPerformNonVanillaMeleeAttack();
            if(this.getSkillAnimationTime()>0){
                this.addSkillAnimationTime(-1);
                this.getNavigation().stop();
            }

            if(this instanceof ISpellUser) {
                int currentspelldelay = this.getSpellDelay();
                @Nullable
                LivingEntity target = this.getBrain().getMemory(ATTACK_TARGET).orElse(null);
                if (currentspelldelay > 0) {
                    this.getNavigation().stop();
                    if(this.isSprinting()){
                        this.setSprinting(false);
                    }
                    setSpellDelay(currentspelldelay - 1);
                    int delay = this.tickCount - this.StartedSpellAttackTimeStamp;
                    if (delay == ((ISpellUser)this).getProjectilePreAnimationDelay()) {
                        if(target!=null) {
                            ((ISpellUser) this).ShootProjectile(this.getCommandSenderWorld(), target);
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
            this.addMorale(-0.001);
        }

        if(this.isSleeping()) {
            this.setDeltaMovement(new Vector3d(0, 0, 0));
            this.getSleepingPos().ifPresent((pos)->this.setPos((double)pos.getX() + 0.5D, (double)pos.getY() + 0.6875D, (double)pos.getZ() + 0.5D));

            if(this.tickCount %600 ==0){
                this.heal(1F);
                this.addAffection(0.1);
                this.addMorale(2.5F);
            }

            if(this.isCriticallyInjured()){
                int curetime = this.getInjuryCureTimer();
                if(curetime>=0){
                    curetime++;
                }
                if(curetime>=168000){
                    curetime = -1;
                }
                if(curetime == -1){
                    this.stopSleeping();
                    this.getBrain().setMemory(MEMORY_SITTING.get(), true);
                    this.setCriticallyinjured(false);
                }
                this.setInjurycuretimer(curetime);
            }
            if (!this.level.isClientSide() && this.getCommandSenderWorld().isDay() && !this.isCriticallyInjured()) {
                this.stopSleeping();
                if (this.shouldBeSitting) {
                    this.setOrderedToSit(true);
                }
            }
        }

        if(!this.getCommandSenderWorld().isClientSide() && this.isFreeRoaming() && (this.getCommandSenderWorld().isNight() && this.isOrderedToSit() && this.isInHomeRangefromCurrenPos() && this.getNavigation().createPath(this.getHOMEPOS().get(), 0) != null && this.getNavigation().createPath(this.getHOMEPOS().get(), 0).canReach())){
            this.shouldBeSitting = this.isOrderedToSit();
            this.setOrderedToSit(false);
        }
    }

    public void UpdateandPerformNonVanillaMeleeAttack(){
        if(this instanceof IMeleeAttacker) {
            int currentspelldelay = this.getNonVanillaMeleeAttackDelay();
            if (currentspelldelay > 0) {
                this.getBrain().eraseMemory(WALK_TARGET);
                @Nullable
                LivingEntity target = this.getBrain().getMemory(ATTACK_TARGET).orElse(null);
                if(this.isSprinting()){
                    this.setSprinting(false);
                }
                this.getBrain().setMemory(LOOK_TARGET, Optional.ofNullable(target == null?null:new EntityPosWrapper(target, true)));
                this.setMeleeAttackDelay(currentspelldelay - 1);
                int delay = this.tickCount - this.StartedMeleeAttackTimeStamp;
                if (!((IMeleeAttacker) this).getMeleeAnimationAudioCueDelay().isEmpty() && ((IMeleeAttacker) this).getMeleeAnimationAudioCueDelay().contains(delay)) {
                    this.playMeleeAttackPreSound();
                }
                if (target!= null && ((IMeleeAttacker) this).getAttackDamageDelay().contains(delay) && this.distanceTo(target) <= ((IMeleeAttacker) this).getAttackRange(((IMeleeAttacker) this).isTalentedWeaponinMainHand())) {
                    this.AttackCount += 1;
                    ((IMeleeAttacker) this).PerformMeleeAttack(target, this.getAttackDamageMainHand(), this.AttackCount);
                }

            } else if (this.AttackCount > 0) {
                this.AttackCount = 0;
            }
        }
    }

    public ItemStack getSkillItem1(){
        return this.getEntityData().get(SKILL_ITEM_1);
    }

    /*
    Play sounds that should play before hitsound here. Weapon swinging for example.
     */
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

    @Override
    public void heal(float p_70691_1_) {
        if(!this.isCriticallyInjured()) {
            super.heal(p_70691_1_);
        }
    }

    public void reloadAmmo(){
        ItemStack gunStack = this.getGunStack();
        Item gunItem = gunStack.getItem();
        if(gunItem instanceof GunItem) {
            GunItem ItemGun = (GunItem) gunItem;
            Gun gun = (ItemGun).getModifiedGun(gunStack);
            CompoundNBT tag = gunStack.getOrCreateTag();
            int remainingAmmo = tag.getInt("AmmoCount");
            ItemStack[] MagStack = this.getMagazine(gun.getProjectile().getItem());
            int magazinecap = GunEnchantmentHelper.getAmmoCapacity(gunStack, gun);
            int availableammo = 0;
            for(ItemStack stack:MagStack){
                availableammo+=stack.getCount();
                if(availableammo+remainingAmmo>=magazinecap){
                    break;
                }
            }

            int ammo2Reload = Math.min(availableammo, magazinecap)-remainingAmmo;
            for(ItemStack stack:MagStack) {
                if (ammo2Reload > 0) {
                    int shrinkableCount = Math.min(ammo2Reload, stack.getCount());
                    stack.shrink(shrinkableCount);
                }
            }
            tag.putInt("AmmoCount", tag.getInt("AmmoCount") + ammo2Reload);

        }
    }
    public ItemStack getGunStack(){
        return this.getValidGunHand().map(this::getItemInHand).orElse(ItemStack.EMPTY);
    }

    private Optional<Hand> getValidGunHand(){
        for(Hand hand:Hand.values()){
            if(this.getItemInHand(hand).getItem() instanceof GunItem){
                return Optional.of(hand);
            }
        }
        return Optional.empty();
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

    /*
    Execute skills that requires loops here.
    Target = as it says, Target entity of host.
    Timer = ticks elapsed after skill activated
     */
    public boolean performSkillTick(LivingEntity target, int Timer){
        return true;
    }

    public void resetSkill(){
        this.setUsingSkill(false);
    }

    public void setUsingSkill(boolean value){
        this.getEntityData().set(USING_SKILL, value);
    }

    public boolean canUseSkill(@Nullable LivingEntity target){
        return this.getSkillDelayTick() == 0 && this.getTarget() != null && this.getTarget().isAlive();
    }

    public void setSkillDelay(){
        this.setSkillDelayTick(2400);
    }

    public ItemStack getSkillItem(int index){
        if(this.getCommandSenderWorld().isClientSide){
            DataParameter<ItemStack>[] stacks = new DataParameter[]{SKILL_ITEM_0,SKILL_ITEM_1,SKILL_ITEM_2,SKILL_ITEM_3};
            return this.getEntityData().get(stacks[index]);
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
    protected void customServerAiStep() {
        this.level.getProfiler().push("CompanionBrain");
        this.getBrain().tick((ServerWorld)this.level, this);
        this.level.getProfiler().pop();

        if(!this.getBrain().getMemory(HOME).isPresent()){
            this.getEntityData().set(HOMEPOS, Optional.empty());
            this.getEntityData().set(VALID_HOME_DISTANCE, -1F);
        }
        else if(this.getHOMEPOS().map((pos)-> this.getBrain().getMemory(HOME).map((homepos)->!pos.equals(homepos.pos())).orElse(true)).orElse(true)){
            this.getBrain().getMemory(HOME).ifPresent((pos)->this.setHomePos(pos.pos()));
        }

        CompanionTasks.UpdateActivity(this);

        this.getBrain().getActiveNonCoreActivity().ifPresent((activity)->{
            MOVE_STATUS status = MOVE_STATUS.fromActivity(activity);
            if(status !=null) {
                this.getEntityData().set(MOVE_MODE, status.ordinal());
            }
        });

        super.customServerAiStep();
    }

    //I know what I am doing, GAME!
    @Nonnull
    @SuppressWarnings("unchecked")
    public Brain<AbstractEntityCompanion> getBrain() {
        return (Brain<AbstractEntityCompanion>)super.getBrain();
    }
    @Nonnull
    @Override
    protected Brain<?> makeBrain(@Nonnull Dynamic<?> p_213364_1_) {
        Brain<AbstractEntityCompanion> brain = this.brainProvider().makeBrain(p_213364_1_);
        CompanionTasks.registerBrain(brain, this);
        return brain;
    }

    public void releasePoi(MemoryModuleType<GlobalPos> p_213742_1_) {
        if (this.level instanceof ServerWorld) {
            MinecraftServer minecraftserver = ((ServerWorld)this.level).getServer();
            this.brain.getMemory(p_213742_1_).ifPresent((p_213752_3_) -> {
                ServerWorld serverworld = minecraftserver.getLevel(p_213752_3_.dimension());
                if (serverworld != null) {
                    PointOfInterestManager pointofinterestmanager = serverworld.getPoiManager();
                    Optional<PointOfInterestType> optional = pointofinterestmanager.getType(p_213752_3_.pos());
                    BiPredicate<AbstractEntityCompanion, PointOfInterestType> bipredicate = POI_MEMORIES.get(p_213742_1_);
                    if (optional.isPresent() && bipredicate.test(this, optional.get())) {
                        pointofinterestmanager.release(p_213752_3_.pos());
                        DebugPacketSender.sendPoiTicketCountPacket(serverworld, p_213752_3_.pos());
                    }

                }
            });
        }
    }

    @Override
    public void travel(@Nonnull Vector3d travelVector) {
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

    public void setReloadDelay(ItemStack gun){
        this.getEntityData().set(RELOAD_TIMER_MAINHAND, this.Reload_Anim_Delay());

        ResourceLocation reloadSound = ((GunItem)gun.getItem()).getGun().getSounds().getCock();
        if (reloadSound != null) {
            MessageGunSound message = new MessageGunSound(reloadSound, SoundCategory.PLAYERS, (float)this.getX(), (float)this.getY() + 1.0F, (float)this.getZ(), 1.0F, 1.0F, this.getId(), false, true);
            PacketHandler.getPlayChannel().send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(this.getX(), this.getY() + 1.0D, this.getZ(), 16.0D, this.level.dimension())), message);
        }

    }

    public int Reload_Anim_Delay(){
        if (((GunItem)this.getMainHandItem().getItem()).getGun().getGeneral().getGripType().getHeldAnimation() instanceof TwoHandedPose) {
            return 63;
        }
        else{
            return 52;
        }
    }
    @Override
    public void startSleeping(@Nonnull BlockPos pos) {
        super.startSleeping(pos);
        this.setLastSlept(this.getCommandSenderWorld().getDayTime());
        this.setDeltaMovement(new Vector3d(0, 0, 0));
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

    @Nonnull
    @MethodsReturnNonnullByDefault
    @Override
    public PushReaction getPistonPushReaction() {
        return this.isSleeping()? PushReaction.IGNORE:super.getPistonPushReaction();
    }

    public boolean islewded(){
        return this.getEntityData().get(ECCI_ANIMATION_TIME)>0;
    }

    @Override
    public void rideTick() {
        super.rideTick();
        if(this.getOwner() != null && this.getVehicle() != null && this.getVehicle() == this.getOwner()){
            LivingEntity player = (LivingEntity) this.getVehicle();
            this.yRot = player.yBodyRot;
            this.clampRotation(this);

            if(this.tickCount%1200 == 0){
                this.addAffection(0.5F);
                this.addMorale(5);
            }
        }
    }

    protected void clampRotation(Entity p_184454_1_) {
        p_184454_1_.setYBodyRot(this.yRot);
        float f = MathHelper.wrapDegrees(p_184454_1_.yRot - this.yRot);
        float f1 = MathHelper.clamp(f, -105.0F, 105.0F);
        p_184454_1_.yRotO += f1 - f;
        p_184454_1_.yRot += f1 - f;
        p_184454_1_.setYHeadRot(p_184454_1_.yRot);
    }

    @Nonnull
    @Override
    public ActionResultType interactAt(@Nonnull PlayerEntity player, @Nonnull Vector3d vec, @Nonnull Hand hand) {

        ItemStack heldstacks = player.getMainHandItem();
        Item HeldItem = heldstacks.getItem();

        if(this.isOwnedBy(player) && !(this instanceof EntityKansenBase && HeldItem instanceof ItemRiggingBase)) {
            if((this.isDeadOrDying() || this.isCriticallyInjured())){
                if(player.getMainHandItem().getItem() instanceof ItemDefibPaddle || player.getOffhandItem().getItem() instanceof ItemDefibPaddle){
                    return ActionResultType.PASS;
                }
                else if(this.isSleeping()){
                    this.stopSleeping();
                }
                boolean isobstructed = false;
                for(int i=0; i<4; i++){
                    if(player.level.getBlockState(player.blockPosition().above(i)).isSuffocating(player.level, player.blockPosition().above(i))){
                        isobstructed = true;
                    }
                }

                if(isobstructed){
                    return ActionResultType.PASS;
                }
                else {
                    this.startRiding(player, true);
                    return ActionResultType.SUCCESS;
                }
            }
            else if (this.getVehicle() != null) {
                this.stopRiding();
                return ActionResultType.SUCCESS;
            }
            else if (HeldItem instanceof ArmorItem || HeldItem instanceof TieredItem) {
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
            else if(this.isOnFire() && HeldItem == Items.WATER_BUCKET){
                this.clearFire();
                this.playSound(SoundEvents.FIRE_EXTINGUISH, 0.8F+(0.4F*this.getRandom().nextFloat()), 0.8F+(0.4F*this.getRandom().nextFloat()));
            }
            //food
            else if (HeldItem != registerItems.ORIGINIUM_PRIME.get() && HeldItem.getFoodProperties() != null && this.canEat(HeldItem.getFoodProperties().canAlwaysEat())) {
                ItemStack stack = this.eat(this.getCommandSenderWorld(), player.getItemInHand(hand));
                this.addAffection(0.03);
                if (!player.isCreative()) {
                    player.setItemInHand(hand, stack);
                }
                this.playSound(SoundEvents.GENERIC_EAT, 0.8F+(0.4F*this.getRandom().nextFloat()), 0.8F+(0.4F*this.getRandom().nextFloat()));
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
                                        this.setHomePos(BedPos);
                                        player.displayClientMessage(new TranslationTextComponent("message.commandstick.entity_bedpos_applied", "[" + BedPos.getX() + ", " + BedPos.getY() + ", " + BedPos.getZ() + "]", this.getDisplayName()), true);
                                    }
                                }
                            } else {
                                player.displayClientMessage(new TranslationTextComponent("message.commandstick.entity_bedpos_failed", "["+ BedPos.getX()+", "+ BedPos.getY()+", "+ BedPos.getZ() +"]", this.getDisplayName()), true);
                            }
                        } else if (this.hasHomePos()) {
                            this.releasePoi(HOME);
                            this.getBrain().eraseMemory(HOME);
                            player.displayClientMessage(new TranslationTextComponent("message.commandstick.entity_bedpos_cleared", this.getDisplayName()), true);
                        }
                    }
                    return ActionResultType.CONSUME;
                }
            } else if(player.getItemInHand(hand).isEmpty() && !this.isAngry()) {

                /*
                 * This Part of class is based on EntityWroughtnaut.java class from Mowzie's mob
                 * Get the Source Code in github:
                 * https://github.com/BobMowzie/MowziesMobs/blob/1.16.5/src/main/java/com/bobmowzie/mowziesmobs/server/entity/wroughtnaut/EntityWroughtnaut.java
                 * Get Mod on curseforge:
                 *  https://www.curseforge.com/minecraft/mc-mods/mowzies-mobs
                 *
                 * Mowzie's mob is licensed under cutom license. I do hope tht this falls under Non-Compete/Non-Imitate/Non-Manipulate...
                 */

                DirectionUtil.RelativeDirection interactionDirection;
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
                    interactionDirection = DirectionUtil.RelativeDirection.RIGHT;
                }
                else if(entityRelativeAngle>135 && entityRelativeAngle<225){
                    interactionDirection = DirectionUtil.RelativeDirection.BACK;
                }
                else{
                    interactionDirection = DirectionUtil.RelativeDirection.LEFT;
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
                    } else if (EyeCheckFinal > 0.998 && this.entityData.get(ECCI_ANIMATION_TIME)==0) {
                        if (this.getCommandSenderWorld().isClientSide()) {
                            Main.NETWORK.sendToServer(new EntityInteractionPacket(this.getId(), EntityInteractionPacket.EntityBehaviorType.PAT, true));
                        }
                        return ActionResultType.SUCCESS;
                    }
                    else if (this.CheckVerticalInteraction(player, 0.65F) && interactionDirection==FRONT) {
                        if (this.getCommandSenderWorld().isClientSide()) {
                            Main.NETWORK.sendToServer(new EntityInteractionPacket(this.getId(), EntityInteractionPacket.EntityBehaviorType.QUESTIONABLE, true));
                        }
                        return ActionResultType.SUCCESS;
                    }
                    else if (this.CheckVerticalInteraction(player, 0.2F)) {
                        if(player.isCrouching() && player.getPassengers().isEmpty() && !this.isOrderedToSit()) {
                            boolean isobstructed = false;
                            for(int i=0; i<4; i++){
                                if(player.level.getBlockState(player.blockPosition().above(i)).isSuffocating(player.level, player.blockPosition().above(i))){
                                    isobstructed = true;
                                }
                            }

                            if(isobstructed){
                                return ActionResultType.PASS;
                            }
                            else {
                                this.startRiding(player, true);
                                return ActionResultType.SUCCESS;
                            }
                        }
                        else {
                            this.SwitchSittingStatus();
                        }
                        return ActionResultType.SUCCESS;
                        //this.setOrderedToSit(!this.isSitting());
                    }
                    else if(player.isShiftKeyDown()) {
                        if (!this.level.isClientSide) {
                            this.openGUI((ServerPlayerEntity) player);
                        }
                        this.playSound(SoundEvents.ARMOR_EQUIP_ELYTRA, 0.8F+(0.4F*this.getRandom().nextFloat()),0.8F+(0.4F*this.getRandom().nextFloat()));
                        return ActionResultType.SUCCESS;
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

    @Override
    public boolean isCustomNameVisible() {
        return super.isCustomNameVisible();
    }

    public boolean CheckVerticalInteraction(LivingEntity player, float heightpoint){
        Vector3d PlayerLook = player.getViewVector(1.0F).normalize();
        Vector3d EyeDelta = new Vector3d(this.getX() - player.getX(), this.getY(heightpoint) - player.getEyeY(), this.getZ() - player.getZ());
        EyeDelta = EyeDelta.normalize();
        double EyeCheckFinal = PlayerLook.dot(EyeDelta);
        return EyeCheckFinal > 0.998;
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
            Main.NETWORK.send(TRACKING_ENTITY.with(() -> this), new spawnParticlePacket(this, spawnParticlePacket.Particles.AFFECTION_HEART));
        }
    }

    @Override
    public int getAmbientSoundInterval() {
        return 3000+this.getRandom().nextInt(1000);
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
                this.releasePoi(HOME);
                this.getBrain().eraseMemory(HOME);
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
    public boolean canBeLeashed(@Nonnull PlayerEntity player) {
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
        this.getEntityData().set(ECCI_ANIMATION_TIME, 20);
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

    public void clearHomePos(){
        Main.NETWORK.sendToServer(new DeleteHomePacket(this.getId()));
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
        if(this.isOrderedToSit() || this.getVehicle() != null){
            return new EntitySize(this.getBbWidth(), this.getSitHeight(), false);
        }
        return super.getDimensions(poseIn);
    }

    public void setOrderedToSit(boolean val) {
        //You cant expect someone nearly dead to walk *shrug*
        if(!val && this.isCriticallyInjured()){
            return;
        }

        if(val){
            this.getBrain().setMemory(MEMORY_SITTING.get(), true);
            this.getBrain().eraseMemory(FOLLOWING_OWNER_MEMORY.get());
        }
        else{
            this.getBrain().eraseMemory(MEMORY_SITTING.get());
            this.getBrain().setMemory(FOLLOWING_OWNER_MEMORY.get(), true);
        }

        this.getEntityData().set(SITTING, val);
        super.setOrderedToSit(val);
        this.refreshDimensions();
    }

    private void setRestOrStay(){
        if(this.getBrain().getMemory(HOME).map((pos)->pos.dimension() == this.level.dimension() && this.getOnPos().closerThan(pos.pos(), this.getHomeDistance())).orElse(false)){
            this.setResting();
        }
        else{
            this.setWaiting();
        }
    }

    private void setResting(){
        this.getBrain().setMemory(RESTING.get(), true);
        this.getBrain().eraseMemory(FOLLOWING_OWNER_MEMORY.get());
        this.getBrain().setActiveActivityIfPossible(IDLE);
    }

    private void setWaiting(){
        this.getBrain().setMemory(registerManager.WAIT_POINT.get(), GlobalPos.of(this.level.dimension(), this.getOnPos()));
    }

    private void setFollowingOwner(){
        this.getBrain().eraseMemory(registerManager.WAIT_POINT.get());
        this.getBrain().eraseMemory(RESTING.get());
        this.getBrain().setMemory(FOLLOWING_OWNER_MEMORY.get(), true);
        this.getBrain().setActiveActivityIfPossible(FOLLOWING_OWNER.get());
    }

    @Override
    public int tickTimer() {
        return this.tickCount;
    }

    @Nonnull
    protected Brain.BrainCodec<AbstractEntityCompanion> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    public void setMovementMode(MOVE_STATUS status){
        this.getEntityData().set(MOVE_MODE, status.ordinal());
        if(status.getActivity()!=null) {
            this.getBrain().setActiveActivityIfPossible(status.getActivity());
        }
    }

    public MOVE_STATUS getMoveStatus() {
        return MOVE_STATUS.values()[this.getEntityData().get(MOVE_MODE)];
    }


    public boolean shouldHelpMine(){
        return this.getMainHandItem().getItem() instanceof PickaxeItem && this.shouldPickupItem();
    }

    public boolean shouldHelpFarm(){
        return this.getBrain().getActiveNonCoreActivity().map((activity)->activity == WORK && this.getMainHandItem().getItem() instanceof HoeItem).orElse(false);
    }

    public enum ARMPOSES{
        IDLE,
        SLEEPING,
        PAT,
        SITTING,
        SWIMMING,
        WALKING,
        RUNNING,
        LEWDING,
        OPENING_DOOR,
        USING_GUN,
        RELOADING_GUN,
        SWINGING,
        MELEEATTACK,
        HOLDING_BOW,
        CHARGING_BOW,
        HOLDING_CROSSBOW,
        CHARGING_CROSSBOW
    }

    public enum MOVE_STATUS{
        FOLLOWING_OWNER(registerManager.FOLLOWING_OWNER.get(), new TranslationTextComponent("entity.status.following").withStyle(TextFormatting.AQUA)),
        WAITING(registerManager.WAITING.get(), new TranslationTextComponent("entity.status.waiting").withStyle(TextFormatting.YELLOW)),
        SLEEPING(REST, new TranslationTextComponent("entity.status.sleeping").withStyle(TextFormatting.GRAY)),
        SITTING(registerManager.SITTING.get(), new TranslationTextComponent("entity.status.sitting").withStyle(TextFormatting.BLUE)),
        FAINTED(null, new TranslationTextComponent("entity.status.fainted").withStyle(TextFormatting.DARK_RED)),
        IDLE(Activity.IDLE, new TranslationTextComponent("entity.status.relaxing").withStyle(TextFormatting.GREEN)),
        INJURED(registerManager.INJURED.get(), new TranslationTextComponent("entity.status.injured").withStyle(TextFormatting.RED)),
        RETREAT(AVOID, new TranslationTextComponent("entity.status.retreat").withStyle(TextFormatting.DARK_PURPLE)),
        COMBAT(FIGHT, new TranslationTextComponent("entity.status.combat").withStyle(TextFormatting.DARK_BLUE));

        @Nullable
        private final Activity activity;
        @Nonnull
        private final IFormattableTextComponent displayname;

        MOVE_STATUS(@Nullable Activity activity, @Nonnull IFormattableTextComponent displayname){
            this.displayname = displayname;
            this.activity = activity;
        }

        public IFormattableTextComponent getDIsplayname(){
            return this.displayname;
        }

        @Nullable
        public Activity getActivity(){
            return this.activity;
        }

        @Nullable
        public static MOVE_STATUS fromActivity(Activity activity){
            for(int i=0; i<MOVE_STATUS.values().length; i++){
                if(MOVE_STATUS.values()[i].getActivity() == activity){
                    return MOVE_STATUS.values()[i];
                }
            }
            return null;
        }
    }
}
