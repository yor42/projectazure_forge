package com.yor42.projectazure;

import com.lowdragmc.multiblocked.Multiblocked;
import com.lowdragmc.multiblocked.api.definition.ComponentDefinition;
import com.lowdragmc.multiblocked.api.definition.ControllerDefinition;
import com.lowdragmc.multiblocked.api.recipe.RecipeMap;
import com.lowdragmc.multiblocked.api.registry.MbdComponents;
import com.mojang.datafixers.util.Pair;
import com.tac.guns.client.render.gun.IOverrideModel;
import com.tac.guns.client.render.gun.ModelOverrides;
import com.tac.guns.common.ProjectileManager;
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import com.yor42.projectazure.client.ClientRegisterManager;
import com.yor42.projectazure.client.renderer.armor.GasMaskRenderer;
import com.yor42.projectazure.client.renderer.block.MachineMetalPressRenderer;
import com.yor42.projectazure.client.renderer.block.MachineRecruitBeaconRenderer;
import com.yor42.projectazure.events.ModBusEventHandler;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.capability.CompanionMultiblockCapability;
import com.yor42.projectazure.gameobject.capability.multiinv.IMultiInventory;
import com.yor42.projectazure.gameobject.capability.playercapability.ProjectAzurePlayerCapability;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityRailgunProjectile;
import com.yor42.projectazure.gameobject.items.GasMaskItem;
import com.yor42.projectazure.intermod.curios.CuriosCompat;
import com.yor42.projectazure.intermod.curios.client.RenderDefib;
import com.yor42.projectazure.intermod.curios.client.RenderGasMask;
import com.yor42.projectazure.intermod.top.TOPCompat;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.libs.utils.ClientUtils;
import com.yor42.projectazure.libs.utils.CompatibilityUtils;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import com.yor42.projectazure.setup.CrushingRecipeCache;
import com.yor42.projectazure.setup.WorldgenInit;
import com.yor42.projectazure.setup.register.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.example.GeckoLibMod;
import software.bernie.geckolib3.GeckoLib;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.stream.Stream;

import static com.lowdragmc.multiblocked.api.registry.MbdCapabilities.registerCapability;
import static com.yor42.projectazure.libs.Constants.MODID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MODID)
public class Main
{
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static final SimpleChannel NETWORK = registerNetwork.getNetworkChannel();
    public static final CrushingRecipeCache CRUSHING_REGISTRY = new CrushingRecipeCache();
    public static CreativeModeTab PA_GROUP = new CreativeModeTab(MODID) {
        @Nonnull
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(RegisterItems.Rainbow_Wisdom_Cube.get());
        }
    };

    public static CreativeModeTab PA_COMPANIONS = new CreativeModeTab("pa_ship") {
        @Nonnull
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(RegisterItems.WISDOM_CUBE.get());
        }
    };

    public static CreativeModeTab PA_RESOURCES = new CreativeModeTab("pa_resources") {
        @Nonnull
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(RegisterItems.ORUNDUM.get().asItem());
        }
    };

    public static CreativeModeTab PA_MACHINES = new CreativeModeTab("pa_machines") {
        @Nonnull
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(RegisterBlocks.METAL_PRESS.get().asItem());
        }
    };

    public static CreativeModeTab PA_WEAPONS = new CreativeModeTab("pa_weapons") {
        @Nonnull
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(RegisterItems.BONKBAT.get());
        }
    };

    public static boolean isClient(){
        return FMLEnvironment.dist == Dist.CLIENT;
    }

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

        registerCapability(CompanionMultiblockCapability.CAP);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, WorldgenInit::registerWorldgen);

        // Register ourselves for server and other game events we are interested i
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        ProjectileManager.getInstance().registerFactory(RegisterItems.RAILGUN_AMMO.get(), (worldIn, entity, weapon, item, modifiedGun, randx, randy) -> new EntityRailgunProjectile(registerEntity.PROJECTILE_RAILGUN.get(), worldIn, entity, weapon, item, modifiedGun, 4F, randx, randy));
        ProjectileManager.getInstance().registerFactory(RegisterItems.SUPERNOVA_AMMO.get(), (worldIn, entity, weapon, item, modifiedGun, randx, randy) -> new EntityRailgunProjectile(registerEntity.PROJECTILE_SUPERNOVA.get(), worldIn, entity, weapon, item, modifiedGun, 1F, randx, randy));

        for (Pair<String, ResourceLocation> pair:registerMultiBlocks.DEFINITIONS){
        ComponentDefinition def = MbdComponents.DEFINITION_REGISTRY.get(pair.getSecond());
        if(def instanceof ControllerDefinition) {
            String name = pair.getFirst();
            try {
                RecipeMap map = getRecipeFromJSON(ResourceUtils.ModResourceLocation("recipe_map/" + name + ".json"));
                ((ControllerDefinition) def).setRecipeMap(map);
                RecipeMap.register(map);
            } catch (Exception var9) {
                Multiblocked.LOGGER.error("error while loading the definition resource {}", name);
            }
        }
        MbdComponents.registerComponent(def);
    }

    }

    private static RecipeMap getRecipeFromJSON(ResourceLocation location) throws IOException {
        InputStream stream = Minecraft.getInstance().getResourceManager().getResource(location).getInputStream();
        InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
        RecipeMap map = GsonHelper.fromJson(Multiblocked.GSON, reader, RecipeMap.class);
        if (map != null) {
            return map;
        }

        return RecipeMap.EMPTY;
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        if (ModList.get().isLoaded("theoneprobe")) TOPCompat.register();
        if(CompatibilityUtils.isCurioLoaded()) CuriosCompat.sendImc(event);
    }

    private void processIMC(final InterModProcessEvent event)
    {

        Stream<InterModComms.IMCMessage> messageStream = InterModComms.getMessages(Constants.MODID);
        messageStream.forEach((msg)->{
        });
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client

        //register Entity renderers
    	registerEntity.registerRenderer();
        ClientUtils.RegisterModelProperties();
        GeoArmorRenderer.registerArmorRenderer(GasMaskItem.class, GasMaskRenderer::new);

        ClientRegisterManager.registerScreen();

        CuriosRendererRegistry.register(RegisterItems.GASMASK.get(), RenderGasMask::new);
        CuriosRendererRegistry.register(RegisterItems.DEFIB_CHARGER.get(), RenderDefib::new);

        //We do some reflect magic for gun here
        for (Field field : RegisterItems.class.getDeclaredFields()) {
            RegistryObject<?> object;
            try {
                object = (RegistryObject<?>) field.get(null);
            } catch (ClassCastException | IllegalAccessException e) {
                continue;
            }
            if (TimelessGunItem.class.isAssignableFrom(object.get().getClass())) {
                try {
                    ModelOverrides.register(
                            (Item) object.get(),
                            (IOverrideModel) Class.forName("com.yor42.projectazure.client.renderer.gun." + field.getName().toLowerCase(Locale.ENGLISH) + "_animation").newInstance()
                    );
                } catch (ClassNotFoundException e) {
                    LOGGER.warn("Could not load animations for gun - " + field.getName()+" / expected class name:"+field.getName().toLowerCase(Locale.ENGLISH)+ "_animation");
                } catch (IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @SubscribeEvent
    public void RegisterCapabilities(RegisterCapabilitiesEvent event){
        event.register(ProjectAzurePlayerCapability.class);
        event.register(IMultiInventory.class);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(registerTE.METAL_PRESS.get(), MachineMetalPressRenderer::new);
        event.registerBlockEntityRenderer(registerTE.RECRUIT_BEACON.get(), MachineRecruitBeaconRenderer::new);
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
