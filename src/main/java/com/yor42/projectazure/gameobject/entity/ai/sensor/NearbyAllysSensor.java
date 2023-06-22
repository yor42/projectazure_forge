package com.yor42.projectazure.gameobject.entity.ai.sensor;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.setup.register.RegisterAI;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.api.core.sensor.EntityFilteringSensor;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiPredicate;

import static com.yor42.projectazure.setup.register.RegisterAI.NEARBY_ALLY_SENSOR;

public class NearbyAllysSensor<E extends AbstractEntityCompanion> extends EntityFilteringSensor<AbstractEntityCompanion,E> {
    @Override
    protected MemoryModuleType<AbstractEntityCompanion> getMemory() {
        return RegisterAI.NEARBY_ALLY.get();
    }

    @Override
    protected BiPredicate<LivingEntity, E> predicate() {
        return (target, entity) -> entity.isAlly(target);
    }

    @Override
    protected @Nullable AbstractEntityCompanion findMatches(E entity, NearestVisibleLivingEntities matcher) {
        return (AbstractEntityCompanion) matcher.findClosest(target -> predicate().test(target, entity)).orElse(null);
    }

    @Override
    public SensorType<? extends ExtendedSensor<?>> type() {
        return NEARBY_ALLY_SENSOR.get();
    }
}
