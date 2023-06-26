package com.yor42.projectazure.gameobject.entity.ai.sensor;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.setup.register.RegisterAI;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.tslat.smartbrainlib.api.core.sensor.EntityFilteringSensor;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiPredicate;

import static com.yor42.projectazure.setup.register.RegisterAI.HEAL_TARGET_SENSOR;

public class HealTargetSensor extends EntityFilteringSensor<LivingEntity, AbstractEntityCompanion> {
    @Override
    protected MemoryModuleType<LivingEntity> getMemory() {
        return RegisterAI.HEAL_TARGET.get();
    }

    @Override
    protected BiPredicate<LivingEntity, AbstractEntityCompanion> predicate() {
        return (target, entity) -> entity.isAlly(target);
    }

    @Override
    protected @Nullable LivingEntity findMatches(AbstractEntityCompanion entity, NearestVisibleLivingEntities matcher) {

        Iterator<LivingEntity> iter = matcher.findAll((tgt)->tgt.getHealth()/tgt.getHealth()<=0.75F).iterator();

        AtomicReference<LivingEntity> tgt = new AtomicReference<>(null);

        iter.forEachRemaining((candidate)->{
            if(tgt.get() == null || candidate.getHealth() < tgt.get().getHealth()){
                tgt.set(candidate);
            }
        });

        return tgt.get();
    }

    @Override
    public SensorType<? extends ExtendedSensor<?>> type() {
        return HEAL_TARGET_SENSOR.get();
    }
}
