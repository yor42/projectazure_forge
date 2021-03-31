package com.yor42.projectazure;

import com.yor42.projectazure.client.ClientRegisterManager;
import com.yor42.projectazure.client.renderer.entity.*;
import com.yor42.projectazure.client.renderer.entity.misc.EntityPlanef4fwildcatRenderer;
import com.yor42.projectazure.client.renderer.entity.projectile.EntityGunBulletRenderer;
import com.yor42.projectazure.client.renderer.entity.projectile.EntityProjectileTorpedoRenderer;
import com.yor42.projectazure.client.renderer.entity.projectile.entityCannonPelletRenderer;
import com.yor42.projectazure.gameobject.capability.ProjectAzurePlayerCapability;
import com.yor42.projectazure.network.ModBusEventHandler;
import com.yor42.projectazure.network.proxy.ClientProxy;
import com.yor42.projectazure.network.proxy.CommonProxy;
import com.yor42.projectazure.setup.register.*;
import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.example.GeckoLibMod;
import software.bernie.geckolib3.GeckoLib;

import java.util.stream.Collectors;

import static com.yor42.projectazure.libs.defined.MODID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MODID)
public class Main
{
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static final SimpleChannel NETWORK = registerNetwork.getNetworkChannel();
    public static CommonProxy PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public static ItemGroup PA_GROUP = new ItemGroup(MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(registerItems.Rainbow_Wisdom_Cube.get());
        }
    };

    public static ItemGroup PA_SHIPS = new ItemGroup("PA_SHIP") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(registerItems.WISDOM_CUBE.get());
        }
    };

    public Main() {

        MinecraftForge.EVENT_BUS.register(new ModBusEventHandler());
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(SoundEvent.class, registerSounds::register);

        registerManager.register();
        GeckoLibMod.DISABLE_IN_DEV = true;
        GeckoLib.initialize();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        ProjectAzurePlayerCapability.registerCapability();
        registerEntity.RegisterAttributes();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
    	RenderingRegistry.registerEntityRenderingHandler(registerManager.AYANAMI.get(), entityAyanamiRenderer::new);
    	RenderingRegistry.registerEntityRenderingHandler(registerManager.GANGWON.get(), entityGangwonRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(registerManager.SHIROKO.get(), entityShirokoRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(registerManager.ENTERPRISE.get(), entityEnterpriseRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(registerManager.NAGATO.get(), entityNagatoRenderer::new);

    	RenderingRegistry.registerEntityRenderingHandler(registerManager.CANNONSHELL.get(), entityCannonPelletRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(registerManager.TORPEDO.get(), EntityProjectileTorpedoRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(registerManager.GUN_BULLET.get(), EntityGunBulletRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(registerManager.F4FWildCat.get(), EntityPlanef4fwildcatRenderer::new);
        ClientRegisterManager.registerScreen();
    }
}

/*
-----------------------------------------------
PROJECT: AZURE
CREDIT
-----------------------------------------------

Original Game by
Kadokawa games, Moe Fantasy, Shanghai Manjuu

Original Concept by
Waisse, yor42, guri, necrom, Aoichi

Developed by
yor42

Modeling by
yor42

Texture by
yor42

Thanks to
Domi, Rongmario, Nali_, Alex the 666, Tiviacz1337, Mojang studio, Gecko#5075, Pinkalulan, Dane Kenect

and YOU <3
 */
