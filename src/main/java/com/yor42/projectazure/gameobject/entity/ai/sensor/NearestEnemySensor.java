package com.yor42.projectazure.gameobject.entity.ai.sensor;

import com.google.common.collect.ImmutableSet;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.setup.register.registerManager;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.yor42.projectazure.setup.register.registerManager.*;

public class NearestEnemySensor extends Sensor<AbstractEntityCompanion> {


    @Override
    protected void doTick(ServerWorld world, AbstractEntityCompanion entity) {
        AxisAlignedBB axisalignedbb = entity.getBoundingBox().inflate(16.0D, 16.0D, 16.0D);
        List<LivingEntity> list = world.getEntitiesOfClass(LivingEntity.class, axisalignedbb, (candidate) -> candidate instanceof MonsterEntity);
        list.sort(Comparator.comparingDouble(entity::distanceToSqr));
        Brain<?> brain = entity.getBrain();
        brain.setMemory(registerManager.NEARBY_HOSTILES.get(), list);
        brain.setMemory(MemoryModuleType.NEAREST_HOSTILE, list.stream().min((p_220986_2_, p_220986_3_) -> this.compareMobDistance(entity, p_220986_2_, p_220986_3_)));
        EntityPredicate TARGET_CONDITIONS = (new EntityPredicate()).range(16.0D).allowSameTeam().allowNonAttackable().selector((ety)->entity.getSensing().canSee(ety));
        List<LivingEntity> enemies = list.stream().filter((p_220981_1_) -> TARGET_CONDITIONS.test(entity, p_220981_1_)).collect(Collectors.toList());
        brain.setMemory(registerManager.VISIBLE_HOSTILES.get(), enemies);
        brain.setMemory(VISIBLE_HOSTILE_COUNT.get(), enemies.size());
    }

    private int compareMobDistance(LivingEntity p_220983_1_, LivingEntity p_220983_2_, LivingEntity p_220983_3_) {
        return MathHelper.floor(p_220983_2_.distanceToSqr(p_220983_1_) - p_220983_3_.distanceToSqr(p_220983_1_));
    }

    @Nonnull
    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_HOSTILE, NEARBY_HOSTILES.get(), VISIBLE_HOSTILE_COUNT.get(), VISIBLE_HOSTILES.get());
    }
}
