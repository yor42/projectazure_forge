package com.yor42.projectazure;

import com.lowdragmc.multiblocked.api.recipe.ItemsIngredient;
import com.lowdragmc.multiblocked.common.capability.FEMultiblockCapability;
import com.lowdragmc.multiblocked.common.capability.ItemMultiblockCapability;
import com.tac.guns.client.render.gun.IOverrideModel;
import com.tac.guns.client.render.gun.ModelOverrides;
import com.tac.guns.init.ModItems;
import com.tac.guns.item.TransitionalTypes.TimelessGunItem;
import com.yor42.projectazure.client.ClientRegisterManager;
import com.yor42.projectazure.client.renderer.block.MachineMetalPressRenderer;
import com.yor42.projectazure.client.renderer.block.MachineRecruitBeaconRenderer;
import com.yor42.projectazure.client.renderer.entity.*;
import com.yor42.projectazure.client.renderer.entity.misc.EntityClaymoreRenderer;
import com.yor42.projectazure.client.renderer.entity.misc.EntityMissileDroneRenderer;
import com.yor42.projectazure.client.renderer.entity.misc.EntityPlanef4fwildcatRenderer;
import com.yor42.projectazure.client.renderer.entity.projectile.*;
import com.yor42.projectazure.events.ModBusEventHandler;
import com.yor42.projectazure.events.ModBusEventHandlerClient;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.AmmoPressControllerTE;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.OriginiumGeneratorControllerTE;
import com.yor42.projectazure.gameobject.capability.multiinv.CapabilityMultiInventory;
import com.yor42.projectazure.gameobject.capability.playercapability.ProjectAzurePlayerCapability;
import com.yor42.projectazure.intermod.top.TOPCompat;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.setup.CrushingRecipeCache;
import com.yor42.projectazure.setup.WorldgenInit;
import com.yor42.projectazure.setup.register.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.example.GeckoLibMod;
import software.bernie.geckolib3.GeckoLib;

import java.lang.reflect.Field;
import java.util.Locale;
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
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::finishSetup);
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

    private void finishSetup(FMLLoadCompleteEvent t) {
        t.enqueueWork(()->{

        });
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(()->{
            try{
                OriginiumGeneratorControllerTE.OriginiumGeneratorDefinition.recipeMap.start().name("prime_to_power").input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(registerItems.ORIGINIUM_PRIME.get()), 1)).perTick(true).output(FEMultiblockCapability.CAP, 2000).duration(1200).buildAndRegister();
                OriginiumGeneratorControllerTE.OriginiumGeneratorDefinition.recipeMap.start().name("originite_to_power").input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(registerItems.ORIGINITE.get()), 1)).perTick(true).output(FEMultiblockCapability.CAP, 4000).duration(400).buildAndRegister();

                AmmoPressControllerTE.AmmoPressDefinition.recipeMap.start().name("9mm")
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(registerItems.PLATE_BRASS.get())))
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Items.GUNPOWDER)))
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(registerItems.INGOT_LEAD.get())))
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(registerItems.INGOT_COPPER.get())))
                        .output(ItemMultiblockCapability.CAP,  new ItemsIngredient(new ItemStack(ModItems.BULLET_9.get(), 16)))
                        .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

                AmmoPressControllerTE.AmmoPressDefinition.recipeMap.start().name("12gauge")
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(registerItems.PLATE_BRASS.get())))
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Items.GUNPOWDER)))
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(registerItems.NUGGET_LEAD.get()), 12))
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Items.PAPER), 2))
                        .output(ItemMultiblockCapability.CAP,  new ItemsIngredient(new ItemStack(ModItems.BULLET_10g.get(), 16)))
                        .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

                AmmoPressControllerTE.AmmoPressDefinition.recipeMap.start().name("357magnum")
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(registerItems.PLATE_BRASS.get())))
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Items.GUNPOWDER), 2))
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(registerItems.INGOT_LEAD.get()), 2))
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Items.DIAMOND), 1))
                        .output(ItemMultiblockCapability.CAP,  new ItemsIngredient(new ItemStack(ModItems.MAGNUM_BULLET.get(), 12)))
                        .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

                AmmoPressControllerTE.AmmoPressDefinition.recipeMap.start().name("50bmg")
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(registerItems.INGOT_BRASS.get()), 2))
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Items.GUNPOWDER), 3))
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(registerItems.INGOT_LEAD.get()), 3))
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Items.DIAMOND), 1))
                        .output(ItemMultiblockCapability.CAP,  new ItemsIngredient(new ItemStack(ModItems.MAGNUM_BULLET.get(), 12)))
                        .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

                AmmoPressControllerTE.AmmoPressDefinition.recipeMap.start().name("30winchester")
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(registerItems.INGOT_BRASS.get()), 1))
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Items.GUNPOWDER), 2))
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(registerItems.NUGGET_COPPER.get()), 8))
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(registerItems.NUGGET_LEAD.get()), 8))
                        .output(ItemMultiblockCapability.CAP,  new ItemsIngredient(new ItemStack(ModItems.BULLET_30_WIN.get(), 10)))
                        .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

                AmmoPressControllerTE.AmmoPressDefinition.recipeMap.start().name("308win")
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(registerItems.INGOT_BRASS.get()), 1))
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Items.GUNPOWDER), 3))
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(registerItems.NUGGET_LEAD.get()), 8))
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(registerItems.INGOT_COPPER.get())))
                        .output(ItemMultiblockCapability.CAP,  new ItemsIngredient(new ItemStack(ModItems.BULLET_308.get(), 24)))
                        .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

                AmmoPressControllerTE.AmmoPressDefinition.recipeMap.start().name("7.62x25")
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(registerItems.PLATE_BRASS.get()), 1))
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Items.GUNPOWDER)))
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(registerItems.INGOT_LEAD.get()), 1))
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(registerItems.NUGGET_COPPER.get()), 4))
                        .output(ItemMultiblockCapability.CAP,  new ItemsIngredient(new ItemStack(ModItems.BULLET_762x39.get(), 30)))
                        .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

                AmmoPressControllerTE.AmmoPressDefinition.recipeMap.start().name("7.62x39")
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(registerItems.INGOT_BRASS.get()), 1))
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Items.GUNPOWDER)))
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(registerItems.INGOT_LEAD.get()), 1))
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(registerItems.NUGGET_COPPER.get()), 4))
                        .output(ItemMultiblockCapability.CAP,  new ItemsIngredient(new ItemStack(ModItems.BULLET_762x39.get(), 30)))
                        .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

                AmmoPressControllerTE.AmmoPressDefinition.recipeMap.start().name("7.62x54")
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(registerItems.INGOT_BRASS.get()), 2))
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Items.GUNPOWDER)))
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(registerItems.INGOT_LEAD.get()), 2))
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(registerItems.INGOT_COPPER.get()), 2))
                        .output(ItemMultiblockCapability.CAP,  new ItemsIngredient(new ItemStack(ModItems.BULLET_762x39.get(), 20)))
                        .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

                AmmoPressControllerTE.AmmoPressDefinition.recipeMap.start().name("45acp")
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(registerItems.PLATE_BRASS.get()), 1))
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Items.GUNPOWDER)))
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(registerItems.NUGGET_LEAD.get()), 16))
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(registerItems.INGOT_COPPER.get()), 1))
                        .output(ItemMultiblockCapability.CAP,  new ItemsIngredient(new ItemStack(ModItems.BULLET_45.get(), 16)))
                        .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

                AmmoPressControllerTE.AmmoPressDefinition.recipeMap.start().name("5.56")
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(registerItems.PLATE_BRASS.get()), 1))
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Items.GUNPOWDER)))
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(registerItems.NUGGET_LEAD.get()), 16))
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(registerItems.NUGGET_COPPER.get()), 24))
                        .output(ItemMultiblockCapability.CAP,  new ItemsIngredient(new ItemStack(ModItems.BULLET_556.get(), 32)))
                        .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();

                AmmoPressControllerTE.AmmoPressDefinition.recipeMap.start().name("5.8mm")
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(registerItems.PLATE_IRON.get()), 1))
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(Items.GUNPOWDER), 2))
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(registerItems.INGOT_LEAD.get()), 1))
                        .input(ItemMultiblockCapability.CAP, new ItemsIngredient(Ingredient.of(registerItems.INGOT_COPPER.get()), 1))
                        .output(ItemMultiblockCapability.CAP,  new ItemsIngredient(new ItemStack(ModItems.BULLET_58x42.get(), 40)))
                        .perTick(true).input(FEMultiblockCapability.CAP, 100).duration(240).buildAndRegister();
            }
            catch (Exception e){
                Main.LOGGER.error("Failed to register recipe:"+e.getMessage());
            }
        });
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

        //register Entity renderers
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
        RenderingRegistry.registerEntityRenderingHandler(registerManager.CROWNSLAYER.get(), EntityCrownslayerRenderer::new);
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
        ClientRegistry.bindTileEntityRenderer(registerTE.RECRUIT_BEACON.get(), MachineRecruitBeaconRenderer::new);

        ModBusEventHandlerClient.setup();

        RenderingRegistry.registerEntityRenderingHandler(registerManager.F4FWildCat.get(), EntityPlanef4fwildcatRenderer::new);
        ClientRegisterManager.registerScreen();

        //We do some reflect magic for gun here
        for (Field field : registerItems.class.getDeclaredFields()) {
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
