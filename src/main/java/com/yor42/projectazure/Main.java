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
import com.yor42.projectazure.gameobject.containers.entity.*;
import com.yor42.projectazure.gameobject.containers.machine.*;
import com.yor42.projectazure.gameobject.containers.riggingcontainer.RiggingContainer;
import com.yor42.projectazure.gameobject.entity.companion.bonus.EntityFrostnova;
import com.yor42.projectazure.gameobject.entity.companion.bonus.EntityTalulah;
import com.yor42.projectazure.gameobject.entity.companion.gunusers.EntityM4A1;
import com.yor42.projectazure.gameobject.entity.companion.gunusers.EntityShiroko;
import com.yor42.projectazure.gameobject.entity.companion.magicuser.EntityAmiya;
import com.yor42.projectazure.gameobject.entity.companion.magicuser.EntityRosmontis;
import com.yor42.projectazure.gameobject.entity.companion.magicuser.EntitySylvi;
import com.yor42.projectazure.gameobject.entity.companion.ranged.EntitySchwarz;
import com.yor42.projectazure.gameobject.entity.companion.ships.*;
import com.yor42.projectazure.gameobject.entity.companion.sworduser.*;
import com.yor42.projectazure.gameobject.entity.misc.EntityClaymore;
import com.yor42.projectazure.gameobject.entity.misc.EntityF4fWildcat;
import com.yor42.projectazure.gameobject.entity.misc.EntityMissileDrone;
import com.yor42.projectazure.gameobject.entity.projectiles.*;
import com.yor42.projectazure.intermod.top.TOPCompat;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.setup.CrushingRecipeCache;
import com.yor42.projectazure.setup.register.*;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
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
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.example.GeckoLibMod;
import software.bernie.example.client.renderer.entity.ExampleGeoRenderer;
import software.bernie.example.registry.EntityRegistry;
import software.bernie.geckolib3.GeckoLib;

import java.util.stream.Stream;

import static com.yor42.projectazure.libs.Constants.MODID;
import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MODID)
public class Main
{
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static final SimpleChannel NETWORK = registerNetwork.getNetworkChannel();
    public static final CrushingRecipeCache CRUSHING_REGISTRY = new CrushingRecipeCache();

    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MODID);
    public static final DeferredRegister<MenuType<?>> CONTAINER = DeferredRegister.create(ForgeRegistries.CONTAINERS, MODID);
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MODID);
    public static final MenuType<ContainerBAInventory> BA_INVENTORY_TYPE = new MenuType<>((IContainerFactory<ContainerBAInventory>)ContainerBAInventory::new);
    public static final RegistryObject<MenuType<ContainerBAInventory>> BA_CONTAINER = CONTAINER.register("bluearchive_inventory", () -> BA_INVENTORY_TYPE);
    public static final MenuType<ContainerAKNInventory> AKN_INVENTORY_TYPE = new MenuType<>((IContainerFactory<ContainerAKNInventory>)ContainerAKNInventory::new);
    public static final RegistryObject<MenuType<ContainerAKNInventory>> AKN_CONTAINER = CONTAINER.register("arknights_inventory", () -> AKN_INVENTORY_TYPE);
    public static final MenuType<ContainerGFLInventory> GFL_INVENTORY_TYPE = new MenuType<>((IContainerFactory<ContainerGFLInventory>)ContainerGFLInventory::new);
    public static final RegistryObject<MenuType<ContainerGFLInventory>> GFL_CONTAINER = CONTAINER.register("girlsfrontline_inventory", () -> GFL_INVENTORY_TYPE);
    public static final MenuType<ContainerCLSInventory> CLS_INVENTORY_TYPE = new MenuType<>((IContainerFactory<ContainerCLSInventory>)ContainerCLSInventory::new);
    public static final RegistryObject<MenuType<ContainerCLSInventory>> CLS_CONTAINER = CONTAINER.register("closers_inventory", () -> CLS_INVENTORY_TYPE);
    public static final MenuType<ContainerMetalPress> METAL_PRESS_CONTAINER_TYPE = IForgeMenuType.create(ContainerMetalPress::new);
    public static final RegistryObject<MenuType<ContainerMetalPress>> METAL_PRESS_CONTAINER = CONTAINER.register("metal_press_container", () -> METAL_PRESS_CONTAINER_TYPE);
    public static final MenuType<ContainerAlloyFurnace> CONTAINER_ALLOY_FURNACE_CONTAINER_TYPE = IForgeMenuType.create(ContainerAlloyFurnace::new);
    public static final RegistryObject<MenuType<ContainerAlloyFurnace>> ALLOY_FURNACE_CONTAINER = CONTAINER.register("alloy_furnace_container", () -> CONTAINER_ALLOY_FURNACE_CONTAINER_TYPE);
    public static final MenuType<ContainerRecruitBeacon> RECRUIT_BEACON_CONTAINER_TYPE = IForgeMenuType.create(ContainerRecruitBeacon::new);
    public static final RegistryObject<MenuType<ContainerRecruitBeacon>> RECRUIT_BEACON_CONTAINER = CONTAINER.register("recruit_beacon_container", () -> RECRUIT_BEACON_CONTAINER_TYPE);
    public static final MenuType<ContainerDryDock> DRYDOCK_CONTAINER_TYPE = IForgeMenuType.create(ContainerDryDock::new);
    public static final RegistryObject<MenuType<ContainerDryDock>> DRYDOCK_CONTAINER = CONTAINER.register("drydock_container", () -> DRYDOCK_CONTAINER_TYPE);
    public static final MenuType<ContainerBasicRefinery> BASIC_REFINERY_CONTAINER_TYPE = IForgeMenuType.create(ContainerBasicRefinery::new);
    public static final RegistryObject<MenuType<ContainerBasicRefinery>> BASIC_REFINERY_CONTAINER = CONTAINER.register("basic_refinery_container", () -> BASIC_REFINERY_CONTAINER_TYPE);
    public static final MenuType<ContainerCrystalGrowthChamber> GROWTH_CHAMBER_CONTAINER_TYPE = IForgeMenuType.create(ContainerCrystalGrowthChamber::new);
    public static final RegistryObject<MenuType<ContainerCrystalGrowthChamber>> GROWTH_CHAMBER_CONTAINER = CONTAINER.register("crystal_growth_chamber_container", () -> GROWTH_CHAMBER_CONTAINER_TYPE);
    //entity
    public static final EntityType<EntityAyanami> ENTITYTYPE_AYANAMI = EntityType.Builder.of(EntityAyanami::new, MobCategory.CREATURE).sized(0.572F, 1.525F).build(ModResourceLocation("entityayanami").toString());
    public static final RegistryObject<EntityType<EntityAyanami>> AYANAMI = ENTITIES.register("entityayanami", () -> ENTITYTYPE_AYANAMI);
    public static final EntityType<EntityJavelin> ENTITYTYPE_JAVELIN = EntityType.Builder.of(EntityJavelin::new, MobCategory.CREATURE).sized(0.572F, 1.525F).build(ModResourceLocation("entityayanami").toString());
    public static final RegistryObject<EntityType<EntityJavelin>> JAVELIN = ENTITIES.register("entityjavelin", () -> ENTITYTYPE_JAVELIN);
    public static final EntityType<EntityZ23> ENTITYTYPE_Z23 = EntityType.Builder.of(EntityZ23::new, MobCategory.CREATURE).sized(0.572F, 1.525F).build(ModResourceLocation("entityz23").toString());
    public static final RegistryObject<EntityType<EntityZ23>> Z23 = ENTITIES.register("entityz23", () -> ENTITYTYPE_Z23);
    public static final EntityType<EntityLaffey> ENTITYTYPE_LAFFEY = EntityType.Builder.of(EntityLaffey::new, MobCategory.CREATURE).sized(0.572F, 1.525F).build(ModResourceLocation("entitylaffey").toString());
    public static final RegistryObject<EntityType<EntityLaffey>> LAFFEY = ENTITIES.register("entitylaffey", () -> ENTITYTYPE_LAFFEY);
    public static final EntityType<EntityGangwon> ENTITYTYPE_GANGWON = EntityType.Builder.of(EntityGangwon::new, MobCategory.CREATURE).sized(0.572F, 1.35F).build(ModResourceLocation("entitygandwon").toString());
    public static final RegistryObject<EntityType<EntityGangwon>> GANGWON = ENTITIES.register("entitygangwon", () -> ENTITYTYPE_GANGWON);
    public static final EntityType<EntityEnterprise> ENTITYTYPE_ENTERPRISE = EntityType.Builder.of(EntityEnterprise::new, MobCategory.CREATURE).sized(0.65F, 1.825F).build(ModResourceLocation("entityenterprise").toString());
    public static final RegistryObject<EntityType<EntityEnterprise>> ENTERPRISE = ENTITIES.register("entityenterprise", () -> ENTITYTYPE_ENTERPRISE);
    public static final EntityType<EntityM4A1> ENTITYTYPE_M4A1 = EntityType.Builder.of(EntityM4A1::new, MobCategory.CREATURE).sized(0.65F, 1.825F).build(ModResourceLocation("entitym4a1").toString());
    public static final RegistryObject<EntityType<EntityM4A1>> M4A1 = ENTITIES.register("entitym4a1", () -> ENTITYTYPE_M4A1);
    public static final EntityType<EntityShiroko> ENTITYTYPE_SHIROKO = EntityType.Builder.of(EntityShiroko::new, MobCategory.CREATURE).sized(0.572F, 1.575F).build(ModResourceLocation("entityshiroko").toString());
    public static final RegistryObject<EntityType<EntityShiroko>> SHIROKO = ENTITIES.register("entityshiroko", () -> ENTITYTYPE_SHIROKO);
    public static final EntityType<EntityNagato> ENTITYTYPE_NAGATO = EntityType.Builder.of(EntityNagato::new, MobCategory.CREATURE).sized(0.572F, 1.32F).build(ModResourceLocation("entitynagato").toString());
    public static final RegistryObject<EntityType<EntityNagato>> NAGATO = ENTITIES.register("entitynagato", () -> ENTITYTYPE_NAGATO);
    public static final EntityType<EntityChen> ENTITYTYPE_CHEN = EntityType.Builder.of(EntityChen::new, MobCategory.CREATURE).sized(0.572F, 1.68F).build(ModResourceLocation("entitychen").toString());
    public static final RegistryObject<EntityType<EntityChen>> CHEN = ENTITIES.register("entitychen", () -> ENTITYTYPE_CHEN);
    public static final EntityType<EntityMudrock> ENTITYTYPE_MUDROCK = EntityType.Builder.of(EntityMudrock::new, MobCategory.CREATURE).sized(0.572F, 1.63F).build(ModResourceLocation("entitymudrock").toString());
    public static final RegistryObject<EntityType<EntityMudrock>> MUDROCK = ENTITIES.register("entitymudrock", () -> ENTITYTYPE_MUDROCK);
    public static final EntityType<EntityAmiya> ENTITYTYPE_AMIYA = EntityType.Builder.of(EntityAmiya::new, MobCategory.CREATURE).sized(0.572F, 1.42F).build(ModResourceLocation("entityamiya").toString());
    public static final RegistryObject<EntityType<EntityAmiya>> AMIYA = ENTITIES.register("entityamiya", () -> ENTITYTYPE_AMIYA);
    public static final EntityType<EntityRosmontis> ENTITYTYPE_ROSMONTIS = EntityType.Builder.of(EntityRosmontis::new, MobCategory.CREATURE).sized(0.572F, 1.63F).build(ModResourceLocation("entityamiya").toString());
    public static final RegistryObject<EntityType<EntityRosmontis>> ROSMONTIS = ENTITIES.register("entityrosmontis", () -> ENTITYTYPE_ROSMONTIS);
    public static final EntityType<EntityTalulah> ENTITYTYPE_TALULAH = EntityType.Builder.of(EntityTalulah::new, MobCategory.CREATURE).sized(0.572F, 1.63F).build(ModResourceLocation("entitytalulah").toString());
    public static final RegistryObject<EntityType<EntityTalulah>> TALULAH = ENTITIES.register("entitytalulah", () -> ENTITYTYPE_TALULAH);
    public static final EntityType<EntityTexas> ENTITYTYPE_TEXAS = EntityType.Builder.of(EntityTexas::new, MobCategory.CREATURE).sized(0.572F, 1.61F).build(ModResourceLocation("entitytexas").toString());
    public static final RegistryObject<EntityType<EntityTexas>> TEXAS = ENTITIES.register("entitytexas", () -> ENTITYTYPE_TEXAS);
    public static final EntityType<EntityFrostnova> ENTITYTYPE_FROSTNOVA = EntityType.Builder.of(EntityFrostnova::new, MobCategory.CREATURE).sized(0.572F, 1.63F).build(ModResourceLocation("entityfrostnova").toString());
    public static final RegistryObject<EntityType<EntityFrostnova>> FROSTNOVA = ENTITIES.register("entityfrostnova", () -> ENTITYTYPE_FROSTNOVA);
    public static final EntityType<EntityLappland> ENTITYTYPE_LAPPLAND = EntityType.Builder.of(EntityLappland::new, MobCategory.CREATURE).sized(0.572F, 1.61F).build(ModResourceLocation("entitylappland").toString());
    public static final RegistryObject<EntityType<EntityLappland>> LAPPLAND = ENTITIES.register("entitylappland", () -> ENTITYTYPE_LAPPLAND);
    public static final EntityType<EntitySiege> ENTITYTYPE_SIEGE = EntityType.Builder.of(EntitySiege::new, MobCategory.CREATURE).sized(0.572F, 1.72F).build(ModResourceLocation("entitysiege").toString());
    public static final RegistryObject<EntityType<EntitySiege>> SIEGE = ENTITIES.register("entitysiege", () -> ENTITYTYPE_SIEGE);
    public static final EntityType<EntitySchwarz> ENTITYTYPE_SCHWARZ = EntityType.Builder.of(EntitySchwarz::new, MobCategory.CREATURE).sized(0.572F, 1.69F).build(ModResourceLocation("entityschwarz").toString());
    public static final RegistryObject<EntityType<EntitySchwarz>> SCHWARZ = ENTITIES.register("entityschwarz", () -> ENTITYTYPE_SCHWARZ);
    public static final EntityType<EntitySylvi> ENTITYTYPE_SYLVI = EntityType.Builder.of(EntitySylvi::new, MobCategory.CREATURE).sized(0.572F, 1.69F).build(ModResourceLocation("entityschwarz").toString());
    public static final RegistryObject<EntityType<EntitySylvi>> SYLVI = ENTITIES.register("entitysylvi", () -> ENTITYTYPE_SYLVI);
    public static final EntityType<EntityYamato> ENTITYTYPE_YAMATO = EntityType.Builder.of(EntityYamato::new, MobCategory.CREATURE).sized(0.572F, 1.69F).build(ModResourceLocation("entityyamato").toString());
    public static final RegistryObject<EntityType<EntityYamato>> YAMATO = ENTITIES.register("entityyamato", () -> ENTITYTYPE_YAMATO);
    //projectile
    public static final EntityType<EntityCannonPelllet> PROJECTILECANNONSHELL = EntityType.Builder.<EntityCannonPelllet>of(EntityCannonPelllet::new, MobCategory.MISC).sized(0.5F, 0.5F).build(ModResourceLocation("projectilecannonshell").toString());
    public static final RegistryObject<EntityType<EntityCannonPelllet>> CANNONSHELL = ENTITIES.register("entitycannonshell", () -> PROJECTILECANNONSHELL);
    public static final EntityType<EntityArtsProjectile> PROJECTILEARTS_ENTITYTYPE = EntityType.Builder.<EntityArtsProjectile>of(EntityArtsProjectile::new, MobCategory.MISC).sized(0.5F, 0.5F).build(ModResourceLocation("projectilearts").toString());
    public static final RegistryObject<EntityType<EntityArtsProjectile>> PROJECTILEARTS = ENTITIES.register("entityartsprojectile", () -> PROJECTILEARTS_ENTITYTYPE);
    public static final EntityType<EntityProjectileTorpedo> PROJECTILETORPEDO = EntityType.Builder.<EntityProjectileTorpedo>of(EntityProjectileTorpedo::new, MobCategory.MISC).sized(0.5F, 0.5F).build(ModResourceLocation("projectiletorpedo").toString());
    public static final RegistryObject<EntityType<EntityProjectileTorpedo>> TORPEDO = ENTITIES.register("entitytorpedo", () -> PROJECTILETORPEDO);
    public static final EntityType<EntityProjectileBullet> PROJECTILE_GUN_BULLET = EntityType.Builder.<EntityProjectileBullet>of(EntityProjectileBullet::new, MobCategory.MISC).sized(0.2F, 0.2F).build(ModResourceLocation("projectilebullet").toString());
    public static final RegistryObject<EntityType<EntityProjectileBullet>> GUN_BULLET = ENTITIES.register("entitybullet", () -> PROJECTILE_GUN_BULLET);
    public static final EntityType<EntityMissileDroneMissile> PROJECTILE_DRONE_MISSILE = EntityType.Builder.of(EntityMissileDroneMissile::new, MobCategory.MISC).sized(0.2F, 0.2F).build(ModResourceLocation("projectiledrone_missile").toString());
    public static final RegistryObject<EntityType<EntityMissileDroneMissile>> DRONE_MISSILE = ENTITIES.register("projectiledrone_missile", () -> PROJECTILE_DRONE_MISSILE);
    public static final EntityType<EntityThrownKnifeProjectile> PROJECTILE_KNIFE = EntityType.Builder.<EntityThrownKnifeProjectile>of(EntityThrownKnifeProjectile::new, MobCategory.MISC).sized(0.2F, 0.2F).build(ModResourceLocation("projectile_knife").toString());
    public static final RegistryObject<EntityType<EntityThrownKnifeProjectile>> THROWN_KNIFE = ENTITIES.register("projectile_knife", () -> PROJECTILE_KNIFE);
    //?????
    public static final EntityType<EntityClaymore> ENTITYTYPE_CLAYMORE = EntityType.Builder.of(EntityClaymore::new, MobCategory.CREATURE).sized(0.7F, 2.31F).build(ModResourceLocation("entityclaymore").toString());
    public static final RegistryObject<EntityType<EntityClaymore>> CLAYMORE = ENTITIES.register("entityclaymore", () -> ENTITYTYPE_CLAYMORE);
    //Planes
    public static final EntityType<EntityF4fWildcat> PLANEF4FWildCat = EntityType.Builder.of(EntityF4fWildcat::new, MobCategory.MISC).sized(0.5F, 0.5F).build(ModResourceLocation("entityplanef4fwildcat").toString());
    public static final RegistryObject<EntityType<EntityF4fWildcat>> F4FWildCat = ENTITIES.register("planef4fwildcat", () -> PLANEF4FWildCat);
    public static final EntityType<EntityMissileDrone> ENTITYTYPE_MISSILEDRONE = EntityType.Builder.of(EntityMissileDrone::new, MobCategory.MISC).sized(0.5F, 0.5F).build(ModResourceLocation("missiledrone").toString());
    public static final RegistryObject<EntityType<EntityMissileDrone>> MISSILEDRONE = ENTITIES.register("missiledrone", () -> ENTITYTYPE_MISSILEDRONE);
    //Container
    private static final MenuType<ContainerKansenInventory> SHIP_INVENTORY = new MenuType<>((IContainerFactory<ContainerKansenInventory>)ContainerKansenInventory::new);
    public static final RegistryObject<MenuType<ContainerKansenInventory>> SHIP_CONTAINER = CONTAINER.register("kansen_inventory", () -> SHIP_INVENTORY);
    private static final MenuType<RiggingContainer> RIGGING_INVENTORY_TYPE = new MenuType<>((IContainerFactory<RiggingContainer>)RiggingContainer::new);
    public static final RegistryObject<MenuType<RiggingContainer>> RIGGING_INVENTORY = CONTAINER.register("rigging_inventory", () -> RIGGING_INVENTORY_TYPE);

    public static CreativeModeTab PA_GROUP = new CreativeModeTab(MODID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(registerItems.Rainbow_Wisdom_Cube.get());
        }
    };

    public static CreativeModeTab PA_SHIPS = new CreativeModeTab("pa_ship") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(registerItems.WISDOM_CUBE.get());
        }
    };

    public static CreativeModeTab PA_RESOURCES = new CreativeModeTab("pa_resources") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(registerItems.INGOT_COPPER.get().asItem());
        }
    };

    public static CreativeModeTab PA_MACHINES = new CreativeModeTab("pa_machines") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(registerBlocks.METAL_PRESS.get().asItem());
        }
    };

    public static CreativeModeTab PA_WEAPONS = new CreativeModeTab("pa_weapons") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(registerItems.BONKBAT.get());
        }
    };

    public Main() {
        IEventBus eventbus = FMLJavaModLoadingContext.get().getModEventBus();
        ENTITIES.register(eventbus);
        //BIOMES.register(eventbus);
        FLUIDS.register(eventbus);
        BLOCKS.register(eventbus);
        ITEMS.register(eventbus);
        CONTAINER.register(eventbus);
        TILE_ENTITY.register(eventbus);
        RECIPE_SERIALIZERS.register(eventbus);
        EFFECTS.register(eventbus);
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
        GeckoLibMod.DISABLE_IN_DEV = true;
        GeckoLib.initialize();

        //MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, WorldgenInit::registerWorldgen);

        // Register ourselves for server and other game events we are interested i
    }

    private void setup(final FMLCommonSetupEvent event)
    {
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

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityRegistry.GEO_EXAMPLE_ENTITY.get(), ExampleGeoRenderer::new);
        event.registerEntityRenderer(AYANAMI.get(), entityAyanamiRenderer::new);
        event.registerEntityRenderer(JAVELIN.get(), entityJavelinRenderer::new);
        event.registerEntityRenderer(Z23.get(), entityZ23Renderer::new);
        event.registerEntityRenderer(LAFFEY.get(), EntityLaffeyRenderer::new);
        event.registerEntityRenderer(GANGWON.get(), entityGangwonRenderer::new);
        event.registerEntityRenderer(SHIROKO.get(), entityShirokoRenderer::new);
        event.registerEntityRenderer(ENTERPRISE.get(), entityEnterpriseRenderer::new);
        event.registerEntityRenderer(NAGATO.get(), entityNagatoRenderer::new);
        event.registerEntityRenderer(CHEN.get(), EntityChenRenderer::new);
        event.registerEntityRenderer(MUDROCK.get(), EntityMudrockRenderer::new);
        event.registerEntityRenderer(ROSMONTIS.get(), EntityRosmontisRenderer::new);
        event.registerEntityRenderer(TALULAH.get(), EntityTalulahRenderer::new);
        event.registerEntityRenderer(AMIYA.get(), EntityAmiyaRenderer::new);
        event.registerEntityRenderer(M4A1.get(), EntityM4A1Renderer::new);
        event.registerEntityRenderer(TEXAS.get(), EntityTexasRenderer::new);
        event.registerEntityRenderer(FROSTNOVA.get(), EntityFrostNovaRenderer::new);
        event.registerEntityRenderer(LAPPLAND.get(), EntityLapplandRenderer::new);
        event.registerEntityRenderer(SIEGE.get(), EntitySiegeRenderer::new);
        event.registerEntityRenderer(SCHWARZ.get(), EntitySchwarzRenderer::new);
        event.registerEntityRenderer(SYLVI.get(), EntitySylviRenderer::new);
        event.registerEntityRenderer(YAMATO.get(), EntityYamatoRenderer::new);

        event.registerEntityRenderer(MISSILEDRONE.get(), EntityMissileDroneRenderer::new);

        event.registerEntityRenderer(CANNONSHELL.get(), entityCannonPelletRenderer::new);
        event.registerEntityRenderer(TORPEDO.get(), EntityProjectileTorpedoRenderer::new);
        event.registerEntityRenderer(PROJECTILEARTS.get(), EntityArtsProjectileRenderer::new);
        event.registerEntityRenderer(GUN_BULLET.get(), EntityGunBulletRenderer::new);
        event.registerEntityRenderer(DRONE_MISSILE.get(), MissileDroneMissileRenderer::new);
        event.registerEntityRenderer(THROWN_KNIFE.get(), EntityThrownKnifeRenderer::new);

        event.registerEntityRenderer(CLAYMORE.get(), EntityClaymoreRenderer::new);

        event.registerBlockEntityRenderer(registerTE.METAL_PRESS.get(), MachineMetalPressRenderer::new);
        event.registerBlockEntityRenderer(registerTE.DRYDOCK.get(), DrydockControllerRenderer::new);
        event.registerBlockEntityRenderer(registerTE.RECRUIT_BEACON.get(), MachineRecruitBeaconRenderer::new);

        event.registerEntityRenderer(F4FWildCat.get(), EntityPlanef4fwildcatRenderer::new);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        ModBusEventHandlerClient.setup();
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
