package com.yor42.projectazure.setup.register;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.yor42.projectazure.gameobject.entity.ai.sensor.HealTargetSensor;
import com.yor42.projectazure.gameobject.entity.ai.sensor.InventorySensor;
import com.yor42.projectazure.gameobject.entity.ai.sensor.NearbyAllysSensor;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.entity.schedule.Schedule;
import net.minecraft.world.entity.schedule.ScheduleBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class RegisterAI {
    public static final DeferredRegister<SensorType<?>> SENSORS = DeferredRegister.create(ForgeRegistries.SENSOR_TYPES, Constants.MODID);
    public static final DeferredRegister<Activity> ACTIVITIES = DeferredRegister.create(ForgeRegistries.ACTIVITIES, Constants.MODID);
    public static final DeferredRegister<MemoryModuleType<?>> MEMORYMODULES = DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, Constants.MODID);
    public static final DeferredRegister<Schedule> SCHEDULES = DeferredRegister.create(ForgeRegistries.SCHEDULES, Constants.MODID);
    public static final DeferredRegister<PoiType> POI = DeferredRegister.create(ForgeRegistries.POI_TYPES, Constants.MODID);
    //Activities
    public static final RegistryObject<Activity> FOLLOWING_OWNER = registerActivity("following_ownner");
    public static final RegistryObject<Activity> SITTING = registerActivity("sitting");
    public static final RegistryObject<Activity> WAITING = registerActivity("waiting");
    public static final RegistryObject<Activity> ACTIVITY_RELAXING = registerActivity("resting");
    public static final RegistryObject<Activity> INJURED = registerActivity("injured");
    //MemoryModuleType
    public static final RegistryObject<MemoryModuleType<GlobalPos>> WAIT_POINT = registerMemoryModuleType("wait_point", GlobalPos.CODEC);
    public static final RegistryObject<MemoryModuleType<GlobalPos>> FOOD_PANTRY = registerMemoryModuleType("food_pantry", GlobalPos.CODEC);
    public static final RegistryObject<MemoryModuleType<AbstractEntityCompanion>> NEARBY_ALLY = registerMemoryModuleType("nearby_ally");
    public static final RegistryObject<MemoryModuleType<Entity>> NEAREST_BOAT = registerMemoryModuleType("nearest_boat");
    public static final RegistryObject<MemoryModuleType<LivingEntity>> HEAL_TARGET = registerMemoryModuleType("heal_target");
    public static final RegistryObject<MemoryModuleType<Boolean>> RESTING = registerMemoryModuleType("resting");
    public static final RegistryObject<MemoryModuleType<Boolean>> MEMORY_SITTING = registerMemoryModuleType("sitting");
    public static final RegistryObject<MemoryModuleType<Integer>> FOOD_INDEX = registerMemoryModuleType("food_index");
    public static final RegistryObject<MemoryModuleType<Integer>> HEAL_POTION_INDEX = registerMemoryModuleType("heal_potion_index");
    public static final RegistryObject<MemoryModuleType<Integer>> REGENERATION_POTION_INDEX = registerMemoryModuleType("regneneration_potion_index");
    public static final RegistryObject<MemoryModuleType<Integer>> TOTEM_INDEX = registerMemoryModuleType("totem_index");
    public static final RegistryObject<MemoryModuleType<Integer>> TORCH_INDEX = registerMemoryModuleType("torch_index");
    public static final RegistryObject<MemoryModuleType<BlockPos>> NEAREST_WORLDSKILLABLE = registerMemoryModuleType("nearest_worldskillable");
    public static final RegistryObject<MemoryModuleType<Boolean>> FOLLOWING_OWNER_MEMORY = registerMemoryModuleType("following_owner");
    public static final RegistryObject<MemoryModuleType<Boolean>> INJURED_MEMORY = registerMemoryModuleType("injured_memory");
    public static final RegistryObject<MemoryModuleType<Boolean>> KILLED_ENTITY = registerMemoryModuleType("killed_entity");

    public static final RegistryObject<MemoryModuleType<List<Pair<BlockPos, BlockState>>>> NEAR_HARVESTABLES = registerMemoryModuleType("near_harvestable");
    public static final RegistryObject<MemoryModuleType<List<Pair<BlockPos, BlockState>>>> NEAR_ORES = registerMemoryModuleType("near_ores");
    public static final RegistryObject<MemoryModuleType<List<Pair<BlockPos, BlockState>>>> NEAR_BONEMEALABLE = registerMemoryModuleType("near_bonemealable");
    public static final RegistryObject<MemoryModuleType<List<Pair<BlockPos, BlockState>>>> NEAR_PLANTABLE = registerMemoryModuleType("near_plantable");

    public static final RegistryObject<SensorType<HealTargetSensor>> HEAL_TARGET_SENSOR = registerSensorType("heal_target_sensor", HealTargetSensor::new);
    public static final RegistryObject<SensorType<InventorySensor<? extends AbstractEntityCompanion>>> INVSENSOR = registerSensorType("invsensor", InventorySensor::new);
    public static final RegistryObject<SensorType<NearbyAllysSensor<? extends AbstractEntityCompanion>>> NEARBY_ALLY_SENSOR = registerSensorType("nearby_ally", NearbyAllysSensor::new);
    //POI
    //public static final RegistryObject<PoiType> POI_PANTRY = registerPOI("poi_pantry", ImmutableList.of(RegisterBlocks.OAK_PANTRY.get(),RegisterBlocks.SPRUCE_PANTRY.get(),RegisterBlocks.BIRCH_PANTRY.get(),RegisterBlocks.JUNGLE_PANTRY.get(),RegisterBlocks.DARK_OAK_PANTRY.get(),RegisterBlocks.ACACIA_PANTRY.get(),RegisterBlocks.WARPED_PANTRY.get(),RegisterBlocks.CRIMSON_PANTRY.get()).stream().flatMap((p_234171_0_) -> p_234171_0_.getStateDefinition().getPossibleStates().stream()).collect(ImmutableSet.toImmutableSet()), null, 1,1);

    public static RegistryObject<Activity> registerActivity(String ID){
        return ACTIVITIES.register(ID,()-> new Activity(ID));
    }

    public static RegistryObject<Schedule> registerSchedule(String ID, Function<ScheduleBuilder, Schedule> build){
        Schedule schedule = build.apply(new ScheduleBuilder(new Schedule()));
        return SCHEDULES.register(ID, ()->schedule);
    }

    public static RegistryObject<PoiType> registerPOI(String ID, Set<BlockState> state, @Nullable Predicate<PoiType> predicate, int maxticket, int validrange){
        if(predicate == null){
            return POI.register(ID, ()->new PoiType(ID, state, maxticket, validrange));
        }
        else{
            return POI.register(ID, ()->new PoiType(ID, state, maxticket, predicate, validrange));
        }
    }

    public static <U extends Sensor<?>> RegistryObject<SensorType<U>> registerSensorType(String ID, Supplier<U> sensor){
        return SENSORS.register(ID, ()->new SensorType<>(sensor));
    }

    public static <U> RegistryObject<MemoryModuleType<U>> registerMemoryModuleType(String ID, Codec<U> codec){
        return MEMORYMODULES.register(ID,()->new MemoryModuleType<>(Optional.of(codec)));
    }

    public static <U> RegistryObject<MemoryModuleType<U>> registerMemoryModuleType(String ID){
        return MEMORYMODULES.register(ID,()->new MemoryModuleType<>(Optional.empty()));
    }

    public enum Animations{
        LAUNCH_PLANE,
        SHOOT_CANNON,
        USE_SPELL,
        SHOOT_GUN(false),
        MELEE_ATTACK,
        WORLD_SKILL;

        private final boolean shouldStopNavigation;

        Animations(){
            this.shouldStopNavigation = true;
        }

        Animations(boolean shouldStopNagivation){
            this.shouldStopNavigation = shouldStopNagivation;
        }

        public boolean ShouldStopNavigation(){
            return this.shouldStopNavigation;
        }

    }

    public static void register() {
    }
}
