package com.yor42.projectazure;

import com.yor42.projectazure.client.ClientRegisterManager;
import com.yor42.projectazure.client.renderer.block.DrydockControllerRenderer;
import com.yor42.projectazure.client.renderer.block.MachineMetalPressRenderer;
import com.yor42.projectazure.client.renderer.block.MachineRecruitBeaconRenderer;
import com.yor42.projectazure.client.renderer.entity.*;
import com.yor42.projectazure.client.renderer.entity.misc.EntityClaymoreRenderer;
import com.yor42.projectazure.client.renderer.entity.misc.EntityMissileDroneRenderer;
import com.yor42.projectazure.client.renderer.entity.misc.EntityPlanef4fwildcatRenderer;
import com.yor42.projectazure.client.renderer.entity.projectile.*;
import com.yor42.projectazure.events.ModBusEventHandler;
import com.yor42.projectazure.events.ModBusEventHandlerClient;
import com.yor42.projectazure.gameobject.capability.ProjectAzurePlayerCapability;
import com.yor42.projectazure.gameobject.capability.multiinv.CapabilityMultiInventory;
import com.yor42.projectazure.intermod.top.TOPCompat;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.setup.CrushingRecipeCache;
import com.yor42.projectazure.setup.WorldgenInit;
import com.yor42.projectazure.setup.register.*;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.example.GeckoLibMod;
import software.bernie.geckolib3.GeckoLib;

import java.util.stream.Stream;

import static com.yor42.projectazure.libs.Constants.MODID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MODID)
public class Main
{
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static final SimpleChannel NETWORK = registerNetwork.getNetworkChannel();
    public static final CrushingRecipeCache CRUSHING_REGISTRY = new CrushingRecipeCache();

    public static ItemGroup PA_GROUP = new ItemGroup(MODID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(registerItems.Rainbow_Wisdom_Cube.get());
        }
    };

    public static ItemGroup PA_SHIPS = new ItemGroup("pa_ship") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(registerItems.WISDOM_CUBE.get());
        }
    };

    public static ItemGroup PA_RESOURCES = new ItemGroup("pa_resources") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(registerItems.INGOT_COPPER.get().asItem());
        }
    };

    public static ItemGroup PA_MACHINES = new ItemGroup("pa_machines") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(registerBlocks.METAL_PRESS.get().asItem());
        }
    };

    public static ItemGroup PA_WEAPONS = new ItemGroup("pa_weapons") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(registerItems.BONKBAT.get());
        }
    };

    public Main() {
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, PAConfig.CONFIG_SPEC, "projectazure.toml");
        MinecraftForge.EVENT_BUS.register(new ModBusEventHandler());
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
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
        CapabilityMultiInventory.register();
        registerEntity.RegisterAttributes();
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        if (ModList.get().isLoaded("theoneprobe")) TOPCompat.register();
    }

    private void processIMC(final InterModProcessEvent event)
    {

        Stream<InterModComms.IMCMessage> messageStream = InterModComms.getMessages(Constants.MODID);
        messageStream.forEach((msg)->{
        });
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
    	RenderingRegistry.registerEntityRenderingHandler(registerManager.AYANAMI.get(), entityAyanamiRenderer::new);
    	RenderingRegistry.registerEntityRenderingHandler(registerManager.JAVELIN.get(), entityJavelinRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(registerManager.Z23.get(), entityZ23Renderer::new);
        RenderingRegistry.registerEntityRenderingHandler(registerManager.LAFFEY.get(), EntityLaffeyRenderer::new);
    	RenderingRegistry.registerEntityRenderingHandler(registerManager.GANGWON.get(), entityGangwonRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(registerManager.SHIROKO.get(), entityShirokoRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(registerManager.ENTERPRISE.get(), entityEnterpriseRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(registerManager.NAGATO.get(), entityNagatoRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(registerManager.CHEN.get(), EntityChenRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(registerManager.MUDROCK.get(), EntityMudrockRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(registerManager.ROSMONTIS.get(), EntityRosmontisRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(registerManager.TALULAH.get(), EntityTalulahRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(registerManager.AMIYA.get(), EntityAmiyaRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(registerManager.M4A1.get(), EntityM4A1Renderer::new);
        RenderingRegistry.registerEntityRenderingHandler(registerManager.TEXAS.get(), EntityTexasRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(registerManager.FROSTNOVA.get(), EntityFrostNovaRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(registerManager.LAPPLAND.get(), EntityLapplandRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(registerManager.SIEGE.get(), EntitySiegeRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(registerManager.SCHWARZ.get(), EntitySchwarzRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(registerManager.SYLVI.get(), EntitySylviRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(registerManager.YAMATO.get(), EntityYamatoRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(registerManager.NEARL.get(), EntityNearlRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(registerManager.MISSILEDRONE.get(), EntityMissileDroneRenderer::new);

    	RenderingRegistry.registerEntityRenderingHandler(registerManager.CANNONSHELL.get(), entityCannonPelletRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(registerManager.TORPEDO.get(), EntityProjectileTorpedoRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(registerManager.PROJECTILEARTS.get(), EntityArtsProjectileRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(registerManager.GUN_BULLET.get(), EntityGunBulletRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(registerManager.DRONE_MISSILE.get(), MissileDroneMissileRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(registerManager.THROWN_KNIFE.get(), EntityThrownKnifeRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(registerManager.CLAYMORE.get(), EntityClaymoreRenderer::new);

        ClientRegistry.bindTileEntityRenderer(registerTE.METAL_PRESS.get(), MachineMetalPressRenderer::new);
        ClientRegistry.bindTileEntityRenderer(registerTE.DRYDOCK.get(), DrydockControllerRenderer::new);
        ClientRegistry.bindTileEntityRenderer(registerTE.RECRUIT_BEACON.get(), MachineRecruitBeaconRenderer::new);

        ModBusEventHandlerClient.setup();

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
Kadokawa games, Moe Fantasy, Shanghai Manjuu, Hyperglyph, NAT GAMES, MICA TEAM

Original Concept by
Waisse, yor42, guri, necrom, Aoichi

Developed by
yor42, FakeDomi

Modeling by
yor42

Texture by
yor42, malcolmriley

Thanks to
Domi, Rongmario, Nali_, Alex the 666, Tiviacz1337, Mojang studio, Gecko#5075, Pinkalulan, Dane Kenect

and YOU <3

This mod contains codes from these dev/mods:
Botania by Vazkii on Shift-Tooltip
Techgun by pWn3d1337 on gun related stuff
Fakedomi for Container Server<->Client sync
MrCrayfish/Ocelot on Gun related Stuff

Are you missing from this credit? Let me know on:
yor42#0420
Issue tab of this project
 */
