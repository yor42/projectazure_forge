package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.intermod.tconstruct.TinkersRegistry;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.libs.utils.CompatibilityUtils;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class registerManager {
    public static void register() {
        registerEntity.loadClass();
        RegisterFluids.register();
        RegisterItems.register();
        RegisterFluids.register();
        if(CompatibilityUtils.isTConLoaded()){
            TinkersRegistry.register();
        }
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
        RegisterFluids.FLUIDS.register(eventbus);
        if(CompatibilityUtils.isTConLoaded()){
            TinkersRegistry.registerTinkers(eventbus);
        }
        //DO NOT CHANGE THESE 2's PLACE//
        RegisterBlocks.BLOCKS.register(eventbus);
        RegisterItems.ITEMS.register(eventbus);

        registerRecipes.Types.RECIPE_TYPES.register(eventbus);
        registerRecipes.Serializers.RECIPE_SERIALIZERS.register(eventbus);
        RegisterContainer.CONTAINER.register(eventbus);
        RegisterContainer.TILE_ENTITY.register(eventbus);
        RegisterAI.ACTIVITIES.register(eventbus);
        RegisterAI.MEMORYMODULES.register(eventbus);
        RegisterAI.SENSORS.register(eventbus);
        RegisterAI.SCHEDULES.register(eventbus);
        RegisterAI.POI.register(eventbus);
        registerPotionEffects.EFFECTS.register(eventbus);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void registerMultiBlocksonEvent(RegistryEvent.Register<Block> event) {
        if(CompatibilityUtils.isMultiblockedLoaded()){
            registerMultiBlocks.registerMultiBlocksonEvent(event);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onServerStart(ServerStartedEvent event){
        if(CompatibilityUtils.isMultiblockedLoaded()){
            registerMultiBlocks.registerRecipes(event);
        }

    }

}
