package com.yor42.projectazure;

import com.yor42.projectazure.client.ClientRegisterManager;
import com.yor42.projectazure.client.renderer.block.MachineMetalPressRenderer;
import com.yor42.projectazure.client.renderer.entity.*;
import com.yor42.projectazure.client.renderer.entity.misc.EntityPlanef4fwildcatRenderer;
import com.yor42.projectazure.client.renderer.entity.projectile.EntityGunBulletRenderer;
import com.yor42.projectazure.client.renderer.entity.projectile.EntityProjectileTorpedoRenderer;
import com.yor42.projectazure.client.renderer.entity.projectile.entityCannonPelletRenderer;
import com.yor42.projectazure.events.ModBusEventHandler;
import com.yor42.projectazure.gameobject.capability.ProjectAzurePlayerCapability;
import com.yor42.projectazure.network.proxy.ClientProxy;
import com.yor42.projectazure.network.proxy.CommonProxy;
import com.yor42.projectazure.setup.WorldgenInit;
import com.yor42.projectazure.setup.register.*;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.example.GeckoLibMod;
import software.bernie.geckolib3.GeckoLib;

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

    public static ItemGroup PA_SHIPS = new ItemGroup("pa_ship") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(registerItems.WISDOM_CUBE.get());
        }
    };

    public static ItemGroup PA_RESOURCES = new ItemGroup("pa_resources") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(registerItems.INGOT_COPPER.get().asItem());
        }
    };

    public static ItemGroup PA_MACHINES = new ItemGroup("pa_machines") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(registerBlocks.METAL_PRESS.get().asItem());
        }
    };

    public Main() {
        MinecraftForge.EVENT_BUS.register(this);

        MinecraftForge.EVENT_BUS.register(new ModBusEventHandler());
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(SoundEvent.class, registerSounds::register);

        registerManager.register();
        GeckoLibMod.DISABLE_IN_DEV = true;
        GeckoLib.initialize();

        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, WorldgenInit::registerWorldgen);

        // Register ourselves for server and other game events we are interested i
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
        RenderingRegistry.registerEntityRenderingHandler(registerManager.CHEN.get(), EntityChenRenderer::new);

    	RenderingRegistry.registerEntityRenderingHandler(registerManager.CANNONSHELL.get(), entityCannonPelletRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(registerManager.TORPEDO.get(), EntityProjectileTorpedoRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(registerManager.GUN_BULLET.get(), EntityGunBulletRenderer::new);

        ClientRegistry.bindTileEntityRenderer(registerTE.METAL_PRESS.get(), MachineMetalPressRenderer::new);

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
Kadokawa games, Moe Fantasy, Shanghai Manjuu, Hyperglyph, NAT GAMES

Original Concept by
Waisse, yor42, guri, necrom, Aoichi

Developed by
yor42, FakeDomi

Modeling by
yor42

Texture by
yor42

Thanks to
Domi, Rongmario, Nali_, Alex the 666, Tiviacz1337, Mojang studio, Gecko#5075, Pinkalulan, Dane Kenect

and YOU <3

This mod contains codes from these dev/mods:
Immersive engineering by BluSunrize on multiblock
Botania by Vazkii on Shift-Tooltip
Techgun by pWn3d1337 on gun related stuff
Code by Fakedomi for Container Server<>Client sync

Are you missing from this credit? Let me know on:
yor42#0420
Issue tab of this project:
 */
