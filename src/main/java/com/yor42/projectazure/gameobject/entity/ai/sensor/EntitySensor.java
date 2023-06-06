package com.yor42.projectazure.gameobject.entity.ai.sensor;

import com.google.common.collect.ImmutableSet;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.setup.register.RegisterAI;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EntitySensor extends Sensor<AbstractEntityCompanion> {
    @Override
    protected void doTick(ServerLevel world, AbstractEntityCompanion entity) {
        AABB axisalignedbb = entity.getBoundingBox().inflate(16.0D, 16.0D, 16.0D);
        List<Entity> entitylist = world.getEntitiesOfClass(Entity.class, axisalignedbb, (candidate) -> {
            if (candidate instanceof LivingEntity) {
                if (candidate != entity && candidate.isAlive()) {
                    if (candidate instanceof TamableAnimal) {
                        if (entity.getOwner() != null) {
                            return ((TamableAnimal) candidate).isOwnedBy(entity.getOwner());
                        }
                        return entity.isOwnedBy((LivingEntity) candidate);
                    }
                }
            }
            return candidate instanceof Boat || candidate instanceof Monster;
        });
        Brain<?> brain = entity.getBrain();

        brain.eraseMemory(RegisterAI.NEARBY_ALLYS.get());
        brain.eraseMemory(RegisterAI.NEARBY_HOSTILES.get());
        brain.eraseMemory(MemoryModuleType.NEAREST_HOSTILE);
        brain.eraseMemory(RegisterAI.VISIBLE_HOSTILES.get());
        brain.eraseMemory(RegisterAI.VISIBLE_HOSTILE_COUNT.get());
        brain.eraseMemory(RegisterAI.VISIBLE_ALLYS.get());
        brain.eraseMemory(RegisterAI.VISIBLE_ALLYS_COUNT.get());
        brain.eraseMemory(RegisterAI.HEAL_TARGET.get());
        brain.eraseMemory(RegisterAI.NEAREST_BOAT.get());

        //B O A T E M
        entitylist.sort(Comparator.comparingDouble(entity::distanceToSqr));
        entitylist.stream().filter((etr)->etr instanceof Boat).map(Boat.class::cast).findFirst().ifPresent((boat)->brain.setMemory(RegisterAI.NEAREST_BOAT.get(), boat));

        List<LivingEntity> list = entitylist.stream().filter((etr)->etr instanceof LivingEntity).map(LivingEntity.class::cast).collect(Collectors.toList());
        brain.setMemory(RegisterAI.NEARBY_ALLYS.get(), list);
        TargetingConditions TARGET_CONDITIONS = TargetingConditions.DEFAULT.range(16.0D);

        //HOSTILE
        List<LivingEntity> hostiles = entitylist.stream().filter((etr)->etr instanceof Monster).map(LivingEntity.class::cast).collect(Collectors.toList());
        brain.setMemory(RegisterAI.NEARBY_HOSTILES.get(), hostiles);
        brain.setMemory(MemoryModuleType.NEAREST_HOSTILE, hostiles.stream().min((p_220986_2_, p_220986_3_) -> this.compareMobDistance(entity, p_220986_2_, p_220986_3_)));
        List<LivingEntity> enemies = hostiles.stream().filter((p_220981_1_) -> TARGET_CONDITIONS.test(entity, p_220981_1_)).collect(Collectors.toList());
        brain.setMemory(RegisterAI.VISIBLE_HOSTILES.get(), enemies);
        brain.setMemory(RegisterAI.VISIBLE_HOSTILE_COUNT.get(), enemies.size());

        //ALLYS
        List<LivingEntity> friends = list.stream().filter((p_220981_1_) -> TARGET_CONDITIONS.test(entity, p_220981_1_)).collect(Collectors.toList());
        brain.setMemory(RegisterAI.VISIBLE_ALLYS.get(), friends);
        brain.setMemory(RegisterAI.VISIBLE_ALLYS_COUNT.get(), friends.size());
        brain.setMemory(RegisterAI.HEAL_TARGET.get(), list.stream().filter((tgt)-> {
            if(tgt instanceof AbstractEntityCompanion && ((AbstractEntityCompanion) tgt).isCriticallyInjured()){
                return false;
            }
            return TARGET_CONDITIONS.test(entity, tgt) && (tgt.getHealth()/tgt.getMaxHealth())<=0.5F;
        }).min((ety1, ety2)-> (int) (ety1.getHealth()-ety2.getHealth())));

    }

    private int compareMobDistance(LivingEntity p_220983_1_, LivingEntity p_220983_2_, LivingEntity p_220983_3_) {
        return Mth.floor(p_220983_2_.distanceToSqr(p_220983_1_) - p_220983_3_.distanceToSqr(p_220983_1_));
    }

    @Nonnull
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_HOSTILE, RegisterAI.NEARBY_HOSTILES.get(), RegisterAI.VISIBLE_HOSTILE_COUNT.get(), RegisterAI.VISIBLE_HOSTILES.get(), RegisterAI.VISIBLE_ALLYS_COUNT.get(), RegisterAI.NEAREST_BOAT.get(), RegisterAI.NEARBY_ALLYS.get(), RegisterAI.VISIBLE_ALLYS.get(), RegisterAI.HEAL_TARGET.get());
    }
}
