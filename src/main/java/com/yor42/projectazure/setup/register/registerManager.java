package com.yor42.projectazure.setup.register;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;


public class registerManager {
    public static void register() {
        registerEntity.loadClass();
        registerFluids.register();
        registerItems.register();
        registerFluids.register();
        registerTE.register();
        RegisterContainer.register();
        registerBiomes.register();
        registerPotionEffects.register();
        registerRecipes.register();
        registerRecipes.Types.register();
        registerRecipes.Serializers.register();
        RegisterAI.register();
        IEventBus eventbus = FMLJavaModLoadingContext.get().getModEventBus();
        registerEntity.ENTITIES.register(eventbus);
        registerBiomes.BIOMES.register(eventbus);
        registerFluids.FLUIDS.register(eventbus);
        registerBlocks.BLOCKS.register(eventbus);
        registerItems.ITEMS.register(eventbus);
        RegisterContainer.CONTAINER.register(eventbus);
        RegisterContainer.TILE_ENTITY.register(eventbus);
        RegisterAI.ACTIVITIES.register(eventbus);
        RegisterAI.MEMORYMODULES.register(eventbus);
        RegisterAI.SENSORS.register(eventbus);
        RegisterAI.SCHEDULES.register(eventbus);
        RegisterAI.POI.register(eventbus);
        registerRecipes.RECIPE_SERIALIZERS.register(eventbus);
        registerPotionEffects.EFFECTS.register(eventbus);
    }


}
