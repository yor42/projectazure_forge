package com.yor42.projectazure.gameobject.entity.ai.sensor;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.mixin.EntityAccessor;
import com.yor42.projectazure.setup.register.RegisterAI;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.pathfinder.Path;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.PredicateSensor;
import net.tslat.smartbrainlib.object.SquareRadius;
import net.tslat.smartbrainlib.util.BrainUtils;
import net.tslat.smartbrainlib.util.EntityRetrievalUtil;

import java.util.List;

public class NearestBoatSensor<E extends AbstractEntityCompanion> extends PredicateSensor<Boat, E> {

    private static final List<MemoryModuleType<?>> MEMORIES = ObjectArrayList.of(RegisterAI.NEAREST_BOAT.get());

    protected SquareRadius radius = new SquareRadius(32, 16);
    public NearestBoatSensor(){
        setPredicate(((boat, e) -> {
            Path path = e.getNavigation().createPath(boat, 0);
            return ((EntityAccessor)boat).invokecanAddPassenger(e)&& e.hasLineOfSight(boat) && path != null && path.canReach();}));
    }

    @Override
    public List<MemoryModuleType<?>> memoriesUsed() {
        return MEMORIES;
    }

    @Override
    public SensorType<? extends ExtendedSensor<?>> type() {
        return null;
    }

    @Override
    protected void doTick(ServerLevel level, E entity) {
        BrainUtils.setMemory(entity, RegisterAI.NEAREST_BOAT.get(), EntityRetrievalUtil.getNearestEntity(level, this.radius.inflateAABB(entity.getBoundingBox()), entity.position(), obj -> obj instanceof Boat boat && predicate().test(boat, entity)));
    }
}
