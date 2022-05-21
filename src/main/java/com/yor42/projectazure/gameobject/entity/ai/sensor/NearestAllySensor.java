package com.yor42.projectazure.gameobject.entity.ai.sensor;

import com.google.common.collect.ImmutableSet;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.setup.register.registerManager;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.server.ServerWorld;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class NearestAllySensor extends Sensor<AbstractEntityCompanion> {
    @Override
    protected void doTick(ServerWorld world, AbstractEntityCompanion entity) {
        AxisAlignedBB axisalignedbb = entity.getBoundingBox().inflate(16.0D, 16.0D, 16.0D);
        List<LivingEntity> list = world.getEntitiesOfClass(LivingEntity.class, axisalignedbb, (candidate) -> {
            if(candidate != entity && candidate.isAlive()){
                if(candidate instanceof TameableEntity) {
                    if (entity.getOwner() != null) {
                        return ((TameableEntity) candidate).isOwnedBy(entity.getOwner());
                    }
                }
                return entity.isOwnedBy(candidate);
            }
            return false;
        });
        list.sort(Comparator.comparingDouble(entity::distanceToSqr));
        Brain<?> brain = entity.getBrain();
        brain.setMemory(registerManager.NEARBY_ALLYS.get(), list);
        EntityPredicate TARGET_CONDITIONS = (new EntityPredicate()).range(16.0D).allowSameTeam().allowNonAttackable().selector((ety)->entity.getSensing().canSee(ety));

        List<LivingEntity> friends = list.stream().filter((p_220981_1_) -> TARGET_CONDITIONS.test(entity, p_220981_1_)).collect(Collectors.toList());
        brain.setMemory(registerManager.VISIBLE_ALLYS.get(), friends);
        brain.setMemory(registerManager.VISIBLE_ALLYS_COUNT.get(), friends.size());
        brain.setMemory(registerManager.HEAL_TARGET.get(), list.stream().filter((tgt)-> {
            if(tgt instanceof AbstractEntityCompanion && ((AbstractEntityCompanion) tgt).isCriticallyInjured()){
                return false;
            }
            return TARGET_CONDITIONS.test(entity, tgt) && (tgt.getHealth()/tgt.getMaxHealth())<=0.5F;
        }).min((ety1, ety2)-> (int) (ety1.getHealth()-ety2.getHealth())));

    }

    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(registerManager.VISIBLE_ALLYS_COUNT.get(), registerManager.NEARBY_ALLYS.get(), registerManager.VISIBLE_ALLYS.get(), registerManager.HEAL_TARGET.get());
    }
}