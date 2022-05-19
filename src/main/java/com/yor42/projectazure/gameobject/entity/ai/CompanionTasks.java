package com.yor42.projectazure.gameobject.entity.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.gameobject.entity.ai.tasks.*;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.setup.register.registerManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.village.PointOfInterestType;

import java.util.Optional;

import static com.yor42.projectazure.setup.register.registerManager.*;

public class CompanionTasks {

    public static void registerBrain(Brain<AbstractEntityCompanion> brain){
        brain.addActivity(Activity.CORE, getCorePackage(0.5F));
        brain.addActivity(FOLLOWING_OWNER.get(), getFollowOwnerPackage());
        brain.addActivity(SITTING.get(), getSittingPackage());
        brain.addActivity(WAITING.get(), getWaitPackages(0.5F));
        brain.addActivity(Activity.REST, getRestPackage(0.5F));
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(FOLLOWING_OWNER.get());
    }

    public static ImmutableList<Pair<Integer, ? extends Task<? super AbstractEntityCompanion>>> getCorePackage(float p_220638_1_) {
        return ImmutableList.of(Pair.of(0, new SwimTask(0.8F)), Pair.of(0, new InteractWithDoorTask()),
                Pair.of(0, new LookTask(45, 90)), Pair.of(0, new WakeUpTask()),
                Pair.of(5, new PickupWantedItemTask<>(p_220638_1_, false, 4)),
                Pair.of(1, new WalkToTargetTask()), Pair.of(10, new GatherPOITask(PointOfInterestType.HOME, MemoryModuleType.HOME, false, Optional.of((byte)14))));
    }

    public static ImmutableList<Pair<Integer, ? extends Task<? super AbstractEntityCompanion>>> getFollowOwnerPackage() {
        return ImmutableList.of(Pair.of(0, new FollowOwnerTask()), Pair.of(1, new FindInteractionAndLookTargetTask(EntityType.PLAYER, 4)),
                Pair.of(2, new CompanionFindEntitytoRideTask()),
                Pair.of(3, new CompanionStartRidingTask()),
                Pair.of(4, new CompanionSteerEntityTask()),
                Pair.of(5, new FindWalktargetbyChanceTask(0.5F,3,2, 0.1F)),
                Pair.of(6, new CompanionStopRidingEntityTask()));
    }

    public static ImmutableList<Pair<Integer, ? extends Task<? super AbstractEntityCompanion>>> getSittingPackage() {
        return ImmutableList.of(Pair.of(0, new CompanionSitTask()), Pair.of(3, new FindInteractionAndLookTargetTask(EntityType.PLAYER, 4)));
    }


    public static ImmutableList<Pair<Integer, ? extends Task<? super AbstractEntityCompanion>>> getRestPackage(float p_220635_1_) {
        return ImmutableList.of(Pair.of(2, new CompanionStayNearPointTask(MemoryModuleType.HOME, p_220635_1_, 1, 150, 1200)), Pair.of(3, new ExpirePOITask(PointOfInterestType.HOME, MemoryModuleType.HOME)), Pair.of(3, new SleepAtHomeTask()), Pair.of(5, new FirstShuffledTask<>(ImmutableMap.of(MemoryModuleType.HOME, MemoryModuleStatus.VALUE_ABSENT), ImmutableList.of(Pair.of(new WalkToHouseTask(p_220635_1_), 1), Pair.of(new WalkRandomlyInsideTask(p_220635_1_), 4), Pair.of(new DummyTask(20, 40), 2)))), getMinimalLookBehavior(), Pair.of(99, new UpdateActivityTask()));
    }

    public static ImmutableList<Pair<Integer, ? extends Task<? super AbstractEntityCompanion>>> getWaitPackages(float p_220635_1_) {
        return ImmutableList.of(
                Pair.of(2, new CompanionStayNearPointTask(registerManager.WAIT_POINT.get(), p_220635_1_, 1, 150, 1200))
                , Pair.of(3, new FindInteractionAndLookTargetTask(EntityType.PLAYER, 4))
                , Pair.of(1, new FindWalktargetbyChanceTask(p_220635_1_, 5,2, 0.2F)));
    }

    private static Pair<Integer, Task<AbstractEntityCompanion>> getMinimalLookBehavior() {
        return Pair.of(5, new FirstShuffledTask<>(ImmutableList.of(Pair.of(new LookAtEntityTask(EntityType.PLAYER, 8.0F), 2), Pair.of(new DummyTask(30, 60), 8))));
    }

}
