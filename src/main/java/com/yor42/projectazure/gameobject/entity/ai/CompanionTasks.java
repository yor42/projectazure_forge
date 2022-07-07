package com.yor42.projectazure.gameobject.entity.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.tac.guns.item.GunItem;
import com.yor42.projectazure.PAConfig;
import com.yor42.projectazure.gameobject.entity.ai.tasks.*;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.magicuser.AbstractCompanionMagicUser;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenBase;
import com.yor42.projectazure.gameobject.entity.companion.meleeattacker.AbstractSwordUserBase;
import com.yor42.projectazure.interfaces.IFGOServant;
import com.yor42.projectazure.setup.register.RegisterAI;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.BrainUtil;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.RangedInteger;
import net.minecraft.util.TickRangeConverter;
import net.minecraft.village.PointOfInterestType;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.hasAttackableCannon;
import static net.minecraft.entity.ai.brain.memory.MemoryModuleStatus.VALUE_PRESENT;
import static net.minecraft.entity.ai.brain.memory.MemoryModuleType.ATTACK_TARGET;
import static net.minecraft.entity.ai.brain.schedule.Activity.*;

public class CompanionTasks {

    //I have absolutely 0 idea what I am doing here! :D

    private static final RangedInteger RETREAT_DURATION = TickRangeConverter.rangeOfSeconds(5, 20);
    public static void registerBrain(Brain<AbstractEntityCompanion> brain, AbstractEntityCompanion companion){
        brain.addActivity(Activity.CORE, getCorePackage());
        //brain.addActivity(FOLLOWING_OWNER.get(), getFollowOwnerPackage());
        brain.addActivity(RegisterAI.INJURED.get(), getInjuredPackage());
        brain.addActivity(IDLE, getIdlePackage(companion, 0.75F));
        addRelaxActivity(brain, companion);
        addSittingActivity(brain, companion);
        addCombatActivity(brain, companion);
        addFollowOwnerActivity(brain, companion);
        addWaitPlayerActivity(brain, companion);
        initRetreatActivity(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(RegisterAI.FOLLOWING_OWNER.get());
        brain.useDefaultActivity();
        if(brain.getActiveNonCoreActivity().map((activity) -> activity == IDLE || activity == REST).orElse(false)) {
            brain.setSchedule(RegisterAI.CompanionSchedule.get());
            brain.updateActivityFromSchedule(companion.level.getDayTime(), companion.level.getGameTime());
        }
    }


    public static void UpdateActivity(AbstractEntityCompanion companion){
        if(companion.getBrain().getActiveNonCoreActivity().map((act)->act != RegisterAI.INJURED.get()).orElse(false) && companion.isAlive()) {
            Brain<AbstractEntityCompanion> brain = companion.getBrain();
            companion.setAggressive(companion.getBrain().hasMemoryValue(ATTACK_TARGET));
            if(brain.getActiveNonCoreActivity().map((activity) -> activity == IDLE || activity == REST).orElse(false)) {
                brain.setSchedule(RegisterAI.CompanionSchedule.get());
                brain.updateActivityFromSchedule(companion.level.getDayTime(), companion.level.getGameTime());
            }
            else{
                brain.setActiveActivityToFirstValid(ImmutableList.of(AVOID, Activity.FIGHT, RegisterAI.SITTING.get(), RegisterAI.WAITING.get(), RegisterAI.FOLLOWING_OWNER.get()));
            }
        }
    }

    public static ImmutableList<Pair<Integer, ? extends Task<? super AbstractEntityCompanion>>> getCorePackage() {
        return ImmutableList.of(
                Pair.of(0, new CompanionInteractWithDoorTask()),
                Pair.of(0, new LookTask(45, 90)),
                Pair.of(0, new CompanionWakeupTask()),
                Pair.of(1, new CompanionUseTotemTask()),
                Pair.of(1, new CompanionEndAttackTask()),
                Pair.of(1, new CompanionRunAwayTask()),
                Pair.of(1, new CompanionSprintTask()),
                Pair.of(2, new CompanionHealTask()),
                Pair.of(2, new CompanionMoveforWorkTask()),
                Pair.of(2, new CompanionHealAllyAndPlayerTask(40, 20, 10)),
                Pair.of(2, new CompanionUseWorldSkill()),
                Pair.of(3, new CompanionTakeFoodFromPantryGoal()),
                Pair.of(3, new WalkToTargetTask()),
                Pair.of(10, new GatherPOITask(PointOfInterestType.HOME, MemoryModuleType.HOME, false, Optional.of((byte)14))),
                Pair.of(2, new CompanionEatTask()));
    }

    private static void addWaitPlayerActivity(Brain<AbstractEntityCompanion> brain, AbstractEntityCompanion companion){
        brain.addActivityAndRemoveMemoryWhenStopped(RegisterAI.WAITING.get(), 10, ImmutableList.<net.minecraft.entity.ai.brain.task.Task<? super AbstractEntityCompanion>>of(
                new CompanionStayNearPointTask(RegisterAI.WAIT_POINT.get(), 0.5F, 1, 150, 1200),
                new SleepAtHomeTask(),
                new CompanionProtectOwnerTask(true),
                new PickupWantedItemTask<>(AbstractEntityCompanion::shouldPickupItem, 0.5F, false, 4),
                new FirstShuffledTask<>(ImmutableList.of(Pair.of(new LookAtEntityTask(EntityType.PLAYER, 8.0F), 2), Pair.of(new DummyTask(30, 60), 8)))

        ), RegisterAI.WAIT_POINT.get());
    }

    public static ImmutableList<Pair<Integer, ? extends Task<? super AbstractEntityCompanion>>> getIdlePackage(AbstractEntityCompanion companion, float p_220641_1_) {
        return ImmutableList.of(
                Pair.of(0, new FirstShuffledTask<>(ImmutableMap.of(MemoryModuleType.HOME, MemoryModuleStatus.REGISTERED),
                        ImmutableList.of(
                                Pair.of(new WalkRandomlyInsideTask(p_220641_1_), 4),
                                Pair.of(new DummyTask(20, 40), 2)))),
                //Pair.of(1, new CompanionStayNearPointTask(MemoryModuleType.HOME, p_220641_1_, 3, 150, 1200)),
                Pair.of(2, new FirstShuffledTask<>(ImmutableList.of(
                Pair.of(new FindWalkTargetTask(p_220641_1_), 1), Pair.of(new WalkTowardsLookTargetTask(p_220641_1_, 2), 1), Pair.of(new DummyTask(30, 60), 1)))),
                Pair.of(3, new FindInteractionAndLookTargetTask(EntityType.PLAYER, 4)),
                getFullLookBehavior(),
                Pair.of(99, new UpdateActivityTask()));
    }
    private static Pair<Integer, Task<LivingEntity>> getFullLookBehavior() {
        return Pair.of(5, new FirstShuffledTask<>(ImmutableList.of(Pair.of(new LookAtEntityTask(EntityType.CAT, 8.0F), 8), Pair.of(new LookAtEntityTask(EntityType.PLAYER, 8.0F), 2), Pair.of(new LookAtEntityTask(EntityClassification.CREATURE, 8.0F), 1), Pair.of(new LookAtEntityTask(EntityClassification.WATER_CREATURE, 8.0F), 1), Pair.of(new LookAtEntityTask(EntityClassification.WATER_AMBIENT, 8.0F), 1), Pair.of(new LookAtEntityTask(EntityClassification.MONSTER, 8.0F), 1), Pair.of(new DummyTask(30, 60), 2))));
    }

    private static void initRetreatActivity(Brain<AbstractEntityCompanion> p_234507_0_) {
        p_234507_0_.addActivityAndRemoveMemoryWhenStopped(AVOID, 10, ImmutableList.of(new FollowOwnerTask(), RunAwayTask.entity(MemoryModuleType.AVOID_TARGET, 1.0F, 12, true), createIdleLookBehaviors(), createIdleMovementBehaviors(), new PredicateTask<>(CompanionTasks::wantsToStopFleeing, MemoryModuleType.AVOID_TARGET)), MemoryModuleType.AVOID_TARGET);
    }

    private static FirstShuffledTask<AbstractEntityCompanion> createIdleLookBehaviors() {
        return new FirstShuffledTask<>(ImmutableList.of(Pair.of(new LookAtEntityTask(EntityType.PLAYER, 8.0F), 1), Pair.of(new LookAtEntityTask(8.0F), 1), Pair.of(new DummyTask(30, 60), 1)));
    }

    private static FirstShuffledTask<AbstractEntityCompanion> createIdleMovementBehaviors() {
        return new FirstShuffledTask<>(ImmutableList.of(Pair.of(new WalkRandomlyTask(0.6F), 2), Pair.of(InteractWithEntityTask.of(EntityType.PLAYER, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), 2), Pair.of(new DummyTask(30, 60), 1)));
    }

    private static void addSittingActivity(Brain<AbstractEntityCompanion> brain, AbstractEntityCompanion companion){
        brain.addActivityAndRemoveMemoryWhenStopped(RegisterAI.SITTING.get(), 0, ImmutableList.<net.minecraft.entity.ai.brain.task.Task<? super AbstractEntityCompanion>>of(
                new CompanionClearMovementTask(),
                new FindInteractionAndLookTargetTask(EntityType.PLAYER, 5)
                ), RegisterAI.MEMORY_SITTING.get());

    }

    private static void addFollowOwnerActivity(Brain<AbstractEntityCompanion> brain, AbstractEntityCompanion companion){
        brain.addActivityWithConditions(RegisterAI.FOLLOWING_OWNER.get(), ImmutableList.of(
                Pair.of(0, new FollowOwnerTask()),
                Pair.of(0, new CompanionFindEntitytoRideTask()),
                Pair.of(0, new CompanionStartRidingTask()),
                Pair.of(0, new CompanionSteerEntityTask()),
                Pair.of(1, new CompanionProtectOwnerTask(false)),
                Pair.of(2, new PickupWantedItemTask<>(AbstractEntityCompanion::shouldPickupItem, 0.75F, false, 4)),
                Pair.of(3, new CompanionPlaceTorchTask()),
                Pair.of(3, new CompanionMineTask()),
                Pair.of(4, new FindWalktargetbyChanceTask(0.5F,3,2, 0.1F)),
                Pair.of(4, new CompanionStopRidingEntityTask())), ImmutableSet.of(new Pair<>(RegisterAI.FOLLOWING_OWNER_MEMORY.get(), VALUE_PRESENT)));

    }

    private static boolean wantsToStopFleeing(AbstractEntityCompanion p_234533_0_) {
        Brain<AbstractEntityCompanion> brain = p_234533_0_.getBrain();
        if (!brain.hasMemoryValue(MemoryModuleType.AVOID_TARGET)) {
            return true;
        } else {
            return isFriendlyOutnumbered(p_234533_0_);
        }
    }

    private static void addRelaxActivity(Brain<AbstractEntityCompanion> brain, AbstractEntityCompanion companion){
        brain.addActivity(Activity.REST, getRestPackage(1F));

    }

    private static void addCombatActivity(Brain<AbstractEntityCompanion> brain, AbstractEntityCompanion companion){
        //brain.addActivityWithConditions(Activity.FIGHT, getFightPackage(companion), ImmutableSet.of(Pair.of(ATTACK_TARGET, VALUE_PRESENT)));
        brain.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10, ImmutableList.<net.minecraft.entity.ai.brain.task.Task<? super AbstractEntityCompanion>>of(
                        //new FindNewAttackTargetTask<>((p_234523_1_) -> !isNearestValidAttackTarget(companion, p_234523_1_)),
                        new SupplementedTask<>(CompanionTasks::shouldStrafe, new AttackStrafingTask<>(5, 0.75F)),
                        new CompanionMovetoTargetTask(companion,1.0F),
                        new CompanionLaunchPlaneTasks(20, 40, 50),
                        new CompanionShipRangedAttackTask(),
                        new CompanionUseSkillTask(),
                        new CompanionUseCrossbowTask(),
                        new CompanionBowRangedAttackTask(),
                        new CompanionShootGunTask(),
                        new CompanionNonVanillaMeleeAttackTask(),
                        new CompanionSpellRangedAttackTask(),
                        new CompanionUseShieldTask(),
                        new CompanionProtectOwnerTask(false),
                        new CompanionAttackTargetTask(15)),
                MemoryModuleType.ATTACK_TARGET);
    }

    public static ImmutableList<Pair<Integer, ? extends Task<? super AbstractEntityCompanion>>> getInjuredPackage() {
        return ImmutableList.of(Pair.of(0, new CompanionClearMovementTask()), Pair.of(3, new FindInteractionAndLookTargetTask(EntityType.PLAYER, 4)));
    }


    public static ImmutableList<Pair<Integer, ? extends Task<? super AbstractEntityCompanion>>> getRestPackage(float p_220635_1_) {
        return ImmutableList.of(Pair.of(0, new CompanionStayNearPointTask(MemoryModuleType.HOME, p_220635_1_, 1, 150, 1200))
                , Pair.of(3, new ExpirePOITask(PointOfInterestType.HOME, MemoryModuleType.HOME)),
                Pair.of(3, new SleepAtHomeTask()),
                Pair.of(5, new FirstShuffledTask<>(ImmutableMap.of(MemoryModuleType.HOME, MemoryModuleStatus.VALUE_ABSENT),
                        ImmutableList.of(Pair.of(new WalkToHouseTask(p_220635_1_), 1),
                                Pair.of(new WalkRandomlyInsideTask(p_220635_1_), 4),
                                Pair.of(new DummyTask(20, 40), 2)))),
                getMinimalLookBehavior(),
                Pair.of(99, new UpdateActivityTask()));
    }

    private static boolean shouldStrafe(LivingEntity livingEntity) {
        if(livingEntity instanceof AbstractEntityCompanion) {
            if ((livingEntity.isHolding((item) -> item instanceof GunItem)) && ((AbstractEntityCompanion) livingEntity).isUsingGun()) {
                return true;
            }
        }

        if(livingEntity instanceof EntityKansenBase){
            boolean isArmed = ((EntityKansenBase) livingEntity).canUseCannonOrTorpedo() && ((EntityKansenBase) livingEntity).canUseShell(((EntityKansenBase) livingEntity).getActiveShellCategory());
            boolean isSailing = ((EntityKansenBase) livingEntity).isSailing() || PAConfig.CONFIG.EnableShipLandCombat.get();
            return isArmed && isSailing;
        }
        return livingEntity.isHolding(item -> item instanceof net.minecraft.item.CrossbowItem) || livingEntity.isHolding(item -> item instanceof BowItem);
    }

    private static Pair<Integer, Task<AbstractEntityCompanion>> getMinimalLookBehavior() {
        return Pair.of(5, new FirstShuffledTask<>(ImmutableList.of(Pair.of(new LookAtEntityTask(EntityType.PLAYER, 8.0F), 2), Pair.of(new DummyTask(30, 60), 8))));
    }

    private static boolean isFriendlyOutnumbered(AbstractEntityCompanion entity) {
        int i = entity.getBrain().getMemory(RegisterAI.VISIBLE_ALLYS_COUNT.get()).orElse(0) + 1;
        if(entity.getOwner()!=null && (entity.canSee(entity.getOwner()) || entity.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER).map(entity::isOwnedBy).orElse(false))){
            i+=2;
        }
        int j = entity.getBrain().getMemory(RegisterAI.VISIBLE_HOSTILE_COUNT.get()).orElse(0);
        return j > i;
    }

    public static void wasHurtBy(AbstractEntityCompanion companion, LivingEntity target) {
        Brain<AbstractEntityCompanion> brain = companion.getBrain();
        List<LivingEntity> injuredmob = getVisibleFriendlies(companion).stream().filter((entity)->entity.getHealth()/entity.getMaxHealth()<=0.3F).collect(Collectors.toList());

        getAvoidTarget(companion).ifPresent((p_234462_2_) -> {
            if (p_234462_2_.getType() != target.getType()) {
                brain.eraseMemory(MemoryModuleType.AVOID_TARGET);
            }

        });
        if(companion.getHealth()/companion.getMaxHealth()<=0.5F){
            retreatforAwhile(companion, target);
        }
        else if (isFriendlyOutnumbered(companion) && injuredmob.size()>2) {
            retreatforAwhile(companion, target);
            broadcastRetreat(companion, target);
        } else {
            maybeRetaliate(companion, target);
        }
    }

    private static void maybeRetaliate(AbstractEntityCompanion companion, LivingEntity target) {
        if (!companion.getBrain().isActive(AVOID)) {
            if (EntityPredicates.ATTACK_ALLOWED.test(target)) {
                if (hasWeapon(companion, target) && (companion.getOwner() == null || companion.wantsToAttack(target, companion.getOwner()))) {

                    if(companion.getVehicle() != null){
                        if(companion.getVehicle() == companion.getOwner()){
                            return;
                        }
                        else{
                            companion.stopRiding();
                        }
                    }

                    if (!BrainUtil.isOtherTargetMuchFurtherAwayThanCurrentAttackTarget(companion, target, 4.0D) || (companion.getBrain().getMemory(ATTACK_TARGET).isPresent() && target instanceof MonsterEntity && ((MonsterEntity) target).getTarget() == companion && (!(companion.getBrain().getMemory(ATTACK_TARGET).get() instanceof MonsterEntity)|| ((MonsterEntity)companion.getBrain().getMemory(ATTACK_TARGET).get()).getTarget() !=companion) )) {
                        setAttackTarget(companion, target);
                        broadcastAttackTarget(companion, target);
                    }
                }
            }
        }
    }

    private static boolean hasWeapon(AbstractEntityCompanion companion, LivingEntity tgt){
        ItemStack stack = companion.getItemBySlot(EquipmentSlotType.MAINHAND);
        Item item = stack.getItem();

        if(companion instanceof IFGOServant){
            return ((IFGOServant) companion).SwitchItem();
        }

        boolean isEquippingsword = item instanceof SwordItem;
        boolean hasboworCrossbow = (item instanceof BowItem || item instanceof CrossbowItem) && !companion.getProjectile(stack).isEmpty();
        boolean canUseGun = companion.shouldUseGun();
        boolean canUseRigging = companion instanceof EntityKansenBase && !hasAttackableCannon(companion.getRigging()) && ((EntityKansenBase) companion).canUseShell(((EntityKansenBase) companion).getActiveShellCategory());
        boolean CanUseNonValinnaAttack = companion instanceof AbstractSwordUserBase && ((AbstractSwordUserBase)companion).shouldUseNonVanillaAttack(tgt);
        boolean canUseSpell = companion instanceof AbstractCompanionMagicUser && ((AbstractCompanionMagicUser) companion).shouldUseSpell();
        return isEquippingsword||canUseGun || canUseRigging || CanUseNonValinnaAttack||canUseSpell || hasboworCrossbow;
    }

    private static void setAttackTarget(AbstractEntityCompanion p_234397_0_, LivingEntity p_234397_1_) {
        if(hasWeapon(p_234397_0_, p_234397_1_)) {
            Brain<AbstractEntityCompanion> brain = p_234397_0_.getBrain();
            brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
            brain.setMemoryWithExpiry(ATTACK_TARGET, p_234397_1_, 400L);
        }
    }

    private static void broadcastAttackTarget(AbstractEntityCompanion p_234399_0_, LivingEntity p_234399_1_) {
        getVisibleAllies(p_234399_0_).stream().filter((entity)->entity instanceof AbstractEntityCompanion).forEach((p_234375_1_) -> {
            setAttackTargetIfCloserThanCurrent((AbstractEntityCompanion) p_234375_1_, p_234399_1_);
        });
    }

    public static void setAttackTargetIfCloserThanCurrent(AbstractEntityCompanion p_234401_0_, LivingEntity p_234401_1_) {
        Optional<LivingEntity> optional = p_234401_0_.getBrain().getMemory(ATTACK_TARGET);
        LivingEntity livingentity = BrainUtil.getNearestTarget(p_234401_0_, optional, p_234401_1_);
        setAttackTarget(p_234401_0_, livingentity);
    }

    private static List<LivingEntity> getVisibleAllies(AbstractEntityCompanion p_234400_0_) {
        return p_234400_0_.getBrain().getMemory(RegisterAI.VISIBLE_ALLYS.get()).orElse(ImmutableList.of());
    }

    private static boolean isAttackAllowed(LivingEntity p_234506_0_) {
        return EntityPredicates.ATTACK_ALLOWED.test(p_234506_0_);
    }

    private static void broadcastRetreat(AbstractEntityCompanion p_234516_0_, LivingEntity p_234516_1_) {
        getVisibleFriendlies(p_234516_0_).stream().filter((p_242341_0_) -> p_242341_0_ instanceof AbstractEntityCompanion).forEach((p_234463_1_) -> retreatFromNearestTarget((AbstractEntityCompanion) p_234463_1_, p_234516_1_));
    }

    private static List<LivingEntity> getVisibleFriendlies(AbstractEntityCompanion p_234529_0_) {
        return p_234529_0_.getBrain().getMemory(RegisterAI.VISIBLE_ALLYS.get()).orElse(ImmutableList.of()).stream().filter((entity)->entity instanceof AbstractEntityCompanion).collect(Collectors.toList());
    }

    private static void retreatFromNearestTarget(AbstractEntityCompanion companion, LivingEntity attacker) {
        Brain<AbstractEntityCompanion> brain = companion.getBrain();
        LivingEntity lvt_3_1_ = BrainUtil.getNearestTarget(companion, brain.getMemory(MemoryModuleType.AVOID_TARGET), attacker);
        lvt_3_1_ = BrainUtil.getNearestTarget(companion, brain.getMemory(ATTACK_TARGET), lvt_3_1_);
        retreatforAwhile(companion, lvt_3_1_);
    }

    private static void retreatforAwhile(AbstractEntityCompanion p_234521_0_, LivingEntity p_234521_1_) {
        p_234521_0_.getBrain().eraseMemory(ATTACK_TARGET);
        p_234521_0_.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        p_234521_0_.getBrain().setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, p_234521_1_, RETREAT_DURATION.randomValue(p_234521_0_.level.random));
    }

    public static Optional<LivingEntity> getAvoidTarget(AbstractEntityCompanion p_234515_0_) {
        return p_234515_0_.getBrain().hasMemoryValue(MemoryModuleType.AVOID_TARGET) ? p_234515_0_.getBrain().getMemory(MemoryModuleType.AVOID_TARGET) : Optional.empty();
    }

}
