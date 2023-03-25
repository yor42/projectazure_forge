package com.yor42.projectazure.gameobject.entity.ai.sensor;

import com.google.common.collect.ImmutableSet;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.setup.register.RegisterAI;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.phys.AABB;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class NearestEnemySensor extends Sensor<AbstractEntityCompanion> {


    @Override
    protected void doTick(ServerLevel world, AbstractEntityCompanion entity) {
        AABB axisalignedbb = entity.getBoundingBox().inflate(16.0D, 16.0D, 16.0D);
        List<LivingEntity> list = world.getEntitiesOfClass(LivingEntity.class, axisalignedbb, (candidate) -> candidate instanceof Monster);
        list.sort(Comparator.comparingDouble(entity::distanceToSqr));
        Brain<?> brain = entity.getBrain();
        brain.setMemory(RegisterAI.NEARBY_HOSTILES.get(), list);
        brain.setMemory(MemoryModuleType.NEAREST_HOSTILE, list.stream().min((p_220986_2_, p_220986_3_) -> this.compareMobDistance(entity, p_220986_2_, p_220986_3_)));
        TargetingConditions TARGET_CONDITIONS = (TargetingConditions.forCombat()).range(16.0D);
        List<LivingEntity> enemies = list.stream().filter((p_220981_1_) -> TARGET_CONDITIONS.test(entity, p_220981_1_)).collect(Collectors.toList());
        brain.setMemory(RegisterAI.VISIBLE_HOSTILES.get(), enemies);
        brain.setMemory(RegisterAI.VISIBLE_HOSTILE_COUNT.get(), enemies.size());
    }

    private int compareMobDistance(LivingEntity p_220983_1_, LivingEntity p_220983_2_, LivingEntity p_220983_3_) {
        return Mth.floor(p_220983_2_.distanceToSqr(p_220983_1_) - p_220983_3_.distanceToSqr(p_220983_1_));
    }

    @Nonnull
    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_HOSTILE, RegisterAI.NEARBY_HOSTILES.get(), RegisterAI.VISIBLE_HOSTILE_COUNT.get(), RegisterAI.VISIBLE_HOSTILES.get());
    }
}
