package com.yor42.projectazure.gameobject.entity.ai.sensor;

import com.google.common.collect.ImmutableSet;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.setup.register.RegisterAI;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.item.BoatEntity;
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

public class EntitySensor extends Sensor<AbstractEntityCompanion> {
    @Override
    protected void doTick(ServerWorld world, AbstractEntityCompanion entity) {
        AxisAlignedBB axisalignedbb = entity.getBoundingBox().inflate(16.0D, 16.0D, 16.0D);
        List<Entity> entitylist = world.getEntitiesOfClass(Entity.class, axisalignedbb, (candidate) -> {
            if (candidate instanceof LivingEntity) {
                if (candidate != entity && candidate.isAlive()) {
                    if (candidate instanceof TameableEntity) {
                        if (entity.getOwner() != null) {
                            return ((TameableEntity) candidate).isOwnedBy(entity.getOwner());
                        }
                    }
                    return entity.isOwnedBy((LivingEntity) candidate);
                }
            }
            return candidate instanceof BoatEntity || candidate instanceof MonsterEntity;
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
        entitylist.stream().filter((etr)->etr instanceof BoatEntity).map(BoatEntity.class::cast).findFirst().ifPresent((boat)->brain.setMemory(RegisterAI.NEAREST_BOAT.get(), boat));

        List<LivingEntity> list = entitylist.stream().filter((etr)->etr instanceof LivingEntity).map(LivingEntity.class::cast).collect(Collectors.toList());
        brain.setMemory(RegisterAI.NEARBY_ALLYS.get(), list);
        EntityPredicate TARGET_CONDITIONS = (new EntityPredicate()).range(16.0D).allowSameTeam().allowNonAttackable().selector((ety)->entity.getSensing().canSee(ety));

        //HOSTILE
        List<LivingEntity> hostiles = entitylist.stream().filter((etr)->etr instanceof MonsterEntity).map(LivingEntity.class::cast).collect(Collectors.toList());
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
        return MathHelper.floor(p_220983_2_.distanceToSqr(p_220983_1_) - p_220983_3_.distanceToSqr(p_220983_1_));
    }

    @Nonnull
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_HOSTILE, RegisterAI.NEARBY_HOSTILES.get(), RegisterAI.VISIBLE_HOSTILE_COUNT.get(), RegisterAI.VISIBLE_HOSTILES.get(), RegisterAI.VISIBLE_ALLYS_COUNT.get(), RegisterAI.NEAREST_BOAT.get(), RegisterAI.NEARBY_ALLYS.get(), RegisterAI.VISIBLE_ALLYS.get(), RegisterAI.HEAL_TARGET.get());
    }
}
