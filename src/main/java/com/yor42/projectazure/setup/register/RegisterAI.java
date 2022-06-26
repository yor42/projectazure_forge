package com.yor42.projectazure.setup.register;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.yor42.projectazure.gameobject.entity.ai.sensor.EntitySensor;
import com.yor42.projectazure.gameobject.entity.ai.sensor.InventorySensor;
import com.yor42.projectazure.gameobject.entity.ai.sensor.WorldSensor;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.ai.brain.schedule.Schedule;
import net.minecraft.entity.ai.brain.schedule.ScheduleBuilder;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

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
    public static final DeferredRegister<PointOfInterestType> POI = DeferredRegister.create(ForgeRegistries.POI_TYPES, Constants.MODID);
    //Activities
    public static final RegistryObject<Activity> FOLLOWING_OWNER = registerActivity("following_ownner");
    public static final RegistryObject<Activity> SITTING = registerActivity("sitting");
    public static final RegistryObject<Activity> WAITING = registerActivity("waiting");
    public static final RegistryObject<Activity> INJURED = registerActivity("injured");
    //MemoryModuleType
    public static final RegistryObject<MemoryModuleType<GlobalPos>> WAIT_POINT = registerMemoryModuleType("wait_point", GlobalPos.CODEC);
    public static final RegistryObject<MemoryModuleType<GlobalPos>> FOOD_PANTRY = registerMemoryModuleType("food_pantry", GlobalPos.CODEC);
    public static final RegistryObject<MemoryModuleType<BlockPos>> HURT_AT = registerMemoryModuleType("hurt_at");
    public static final RegistryObject<MemoryModuleType<List<LivingEntity>>> NEARBY_ALLYS = registerMemoryModuleType("nearby_allys");
    public static final RegistryObject<MemoryModuleType<List<LivingEntity>>> VISIBLE_ALLYS = registerMemoryModuleType("visible_allys");
    public static final RegistryObject<MemoryModuleType<Integer>> VISIBLE_ALLYS_COUNT = registerMemoryModuleType("visible_allys_count");
    public static final RegistryObject<MemoryModuleType<List<LivingEntity>>> NEARBY_HOSTILES = registerMemoryModuleType("nearby_hostiles");
    public static final RegistryObject<MemoryModuleType<List<LivingEntity>>> VISIBLE_HOSTILES = registerMemoryModuleType("visible_hostiles");
    public static final RegistryObject<MemoryModuleType<Integer>> VISIBLE_HOSTILE_COUNT = registerMemoryModuleType("visible_hostile_count");
    public static final RegistryObject<MemoryModuleType<BoatEntity>> NEAREST_BOAT = registerMemoryModuleType("nearest_boat");
    public static final RegistryObject<MemoryModuleType<LivingEntity>> HEAL_TARGET = registerMemoryModuleType("heal_target");
    public static final RegistryObject<MemoryModuleType<Boolean>> RESTING = registerMemoryModuleType("resting");
    public static final RegistryObject<MemoryModuleType<Boolean>> MEMORY_SITTING = registerMemoryModuleType("sitting");
    public static final RegistryObject<MemoryModuleType<Integer>> FOOD_INDEX = registerMemoryModuleType("food_index");
    public static final RegistryObject<MemoryModuleType<Integer>> HEAL_POTION_INDEX = registerMemoryModuleType("heal_potion_index");
    public static final RegistryObject<MemoryModuleType<Integer>> REGENERATION_POTION_INDEX = registerMemoryModuleType("regneneration_potion_index");
    public static final RegistryObject<MemoryModuleType<Integer>> TOTEM_INDEX = registerMemoryModuleType("totem_index");
    public static final RegistryObject<MemoryModuleType<Integer>> TORCH_INDEX = registerMemoryModuleType("torch_index");
    public static final RegistryObject<MemoryModuleType<Integer>> FIRE_EXTINGIGH_ITEM = registerMemoryModuleType("fire_extinguish_index");
    public static final RegistryObject<MemoryModuleType<Integer>> FALL_BREAK_ITEM_INDEX = registerMemoryModuleType("fall_break_index");
    public static final RegistryObject<MemoryModuleType<BlockPos>> NEAREST_ORE = registerMemoryModuleType("nearest_ore");
    public static final RegistryObject<MemoryModuleType<BlockPos>> NEAREST_HARVESTABLE = registerMemoryModuleType("nearest_harvestable");
    public static final RegistryObject<MemoryModuleType<BlockPos>> NEAREST_PLANTABLE = registerMemoryModuleType("nearest_plantable");
    public static final RegistryObject<MemoryModuleType<BlockPos>> NEAREST_BONEMEALABLE = registerMemoryModuleType("nearest_bonemealable");
    public static final RegistryObject<MemoryModuleType<BlockPos>> NEAREST_WORLDSKILLABLE = registerMemoryModuleType("nearest_worldskillable");
    public static final RegistryObject<MemoryModuleType<Boolean>> FOLLOWING_OWNER_MEMORY = registerMemoryModuleType("following_owner");
    public static final RegistryObject<MemoryModuleType<Boolean>> KILLED_ENTITY = registerMemoryModuleType("killed_entity");
    //Sensors
    public static final RegistryObject<SensorType<EntitySensor>> ENTITY_SENSOR = registerSensorType("entity_sensor", EntitySensor::new);
    public static final RegistryObject<SensorType<InventorySensor>> INVENTORY_SENSOR = registerSensorType("inventory_sensor", InventorySensor::new);
    public static final RegistryObject<SensorType<WorldSensor>> WORLD_SENSOR = registerSensorType("world_sensor", WorldSensor::new);
    public static final RegistryObject<Schedule> CompanionSchedule = registerSchedule("companion_schedule", (builder)-> builder.changeActivityAt(10, Activity.IDLE).changeActivityAt(12000, Activity.REST).build());
    @SuppressWarnings("UnstableApiUsage")
    //POI
    //public static final RegistryObject<PointOfInterestType> POI_PANTRY = registerPOI("poi_pantry",ImmutableList.of(registerBlocks.OAK_PANTRY.get(),registerBlocks.SPRUCE_PANTRY.get(),registerBlocks.BIRCH_PANTRY.get(),registerBlocks.JUNGLE_PANTRY.get(),registerBlocks.DARK_OAK_PANTRY.get(),registerBlocks.ACACIA_PANTRY.get(),registerBlocks.WARPED_PANTRY.get(),registerBlocks.CRIMSON_PANTRY.get()).stream().flatMap((p_234171_0_) -> p_234171_0_.getStateDefinition().getPossibleStates().stream()).collect(ImmutableSet.toImmutableSet()), null, 1,1);

    public static RegistryObject<Activity> registerActivity(String ID){
        return ACTIVITIES.register(ID,()-> new Activity(ID));
    }

    public static RegistryObject<Schedule> registerSchedule(String ID, Function<ScheduleBuilder, Schedule> build){
        Schedule schedule = build.apply(new ScheduleBuilder(new Schedule()));
        return SCHEDULES.register(ID, ()->schedule);
    }

    public static RegistryObject<PointOfInterestType> registerPOI(String ID, Set<BlockState> state, @Nullable Predicate<PointOfInterestType> predicate, int maxticket, int validrange){
        if(predicate == null){
            return POI.register(ID, ()->new PointOfInterestType(ID, state, maxticket, validrange));
        }
        else{
            return POI.register(ID, ()->new PointOfInterestType(ID, state, maxticket, predicate, validrange));
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

    public static void register() {
    }
}
