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
import com.yor42.projectazure.gameobject.blocks.*;
import com.yor42.projectazure.gameobject.blocks.fluid.*;
import com.yor42.projectazure.gameobject.blocks.tileentity.*;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.MultiBlockReenforcedConcrete;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.MultiblockDrydockTE;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.MultiblockSteelFrame;
import com.yor42.projectazure.gameobject.containers.entity.*;
import com.yor42.projectazure.gameobject.containers.machine.*;
import com.yor42.projectazure.gameobject.containers.riggingcontainer.RiggingContainer;
import com.yor42.projectazure.gameobject.crafting.*;
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
import com.yor42.projectazure.gameobject.items.*;
import com.yor42.projectazure.gameobject.items.gun.ItemAbydos550;
import com.yor42.projectazure.gameobject.items.materials.ModMaterials;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBBDefault;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingCVDefault;
import com.yor42.projectazure.gameobject.items.rigging.itemRiggingDDDefault;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemEquipmentGun127Mm;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemEquipmentTorpedo533Mm;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemPlanef4Fwildcat;
import com.yor42.projectazure.gameobject.items.tools.*;
import com.yor42.projectazure.gameobject.misc.ModFoods;
import com.yor42.projectazure.intermod.top.TOPCompat;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.libs.utils.TooltipUtils;
import com.yor42.projectazure.setup.CrushingRecipeCache;
import com.yor42.projectazure.setup.register.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.*;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.OreBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
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
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MODID);

    public static final RegistryObject<Item> CONTRIBUTOR_BONUS = ITEMS.register("contributor_bonus", () -> new ItemContributorBonus(new Item.Properties()));
    public static final RegistryObject<Item> DEVELOPER_BONUS = ITEMS.register("developer_bonus", () -> new ItemContributorBonus(new Item.Properties()));
    public static final RegistryObject<Item> STASIS_CRYSTAL = ITEMS.register("stasis_crystal", ItemStasisCrystal::new);
    public static final RegistryObject<Item> DEFIB_CHARGER = ITEMS.register("defib_charger", ItemDefibCharger::new);
    public static final RegistryObject<Item> DEFIB_PADDLE = ITEMS.register("defib_paddle", ItemDefibPaddle::new);
    public static final RegistryObject<Item> MEDKIT = ITEMS.register("medkit", ItemMedKit::new);
    public static final RegistryObject<Item> COMMANDING_STICK = ITEMS.register("commanding_stick", ItemCommandStick::new);
    public static final RegistryObject<Item> CLAYMORE_ITEM = ITEMS.register("claymore", ItemClaymore::new);
    public static final RegistryObject<Item> STEEL_CUTTER = ITEMS.register("steel_cutter", () -> new ItemCraftTool(70));
    public static final RegistryObject<Item> HAMMER_IRON = ITEMS.register("hammer_iron", () -> new ItemCraftTool(43));
    //crafting items
    public static final RegistryObject<Item> MORTAR_IRON = ITEMS.register("mortar_iron", () -> new ItemCraftTool(50));
    public static final RegistryObject<Item> MOLD_EXTRACTION = ITEMS.register("mold_extraction", () -> new ItemCraftTool(128));
    public static final RegistryObject<Item> MOLD_WIRE = ITEMS.register("mold_wire", () -> new ItemCraftTool(128));
    public static final RegistryObject<Item> MOLD_PLATE = ITEMS.register("mold_plate", () -> new ItemCraftTool(128));
    //Electronic Stuff
    public static final RegistryObject<Item> COPPER_WIRE = ITEMS.register("copper_wire", () -> new ItemResource("copper", enums.ResourceType.WIRE));
    public static final RegistryObject<Item> NUGGET_BRASS = ITEMS.register("nugget_brass", () -> new ItemResource("brass", enums.ResourceType.NUGGET));
    public static final RegistryObject<Item> NUGGET_STEEL = ITEMS.register("nugget_steel", () -> new ItemResource("steel", enums.ResourceType.NUGGET));
    public static final RegistryObject<Item> NUGGET_ZINC = ITEMS.register("nugget_zinc", () -> new ItemResource("zinc", enums.ResourceType.NUGGET));
    public static final RegistryObject<Item> NUGGET_BRONZE = ITEMS.register("nugget_bronze", () -> new ItemResource("bronze", enums.ResourceType.NUGGET));
    public static final RegistryObject<Item> NUGGET_TIN = ITEMS.register("nugget_tin", () -> new ItemResource("tin", enums.ResourceType.NUGGET));
    public static final RegistryObject<Item> NUGGET_LEAD = ITEMS.register("nugget_lead", () -> new ItemResource("lead", enums.ResourceType.NUGGET));
    public static final RegistryObject<Item> NUGGET_COPPER = ITEMS.register("nugget_copper", () -> new ItemResource("copper", enums.ResourceType.NUGGET));
    public static final RegistryObject<Item> PLATE_BRASS = ITEMS.register("plate_brass", () -> new ItemResource("brass", enums.ResourceType.PLATE));
    public static final RegistryObject<Item> PLATE_STEEL = ITEMS.register("plate_steel", () -> new ItemResource("steel", enums.ResourceType.PLATE));
    public static final RegistryObject<Item> PLATE_GOLD = ITEMS.register("plate_gold", () -> new ItemResource("gold", enums.ResourceType.PLATE));
    public static final RegistryObject<Item> PLATE_IRON = ITEMS.register("plate_iron", () -> new ItemResource("iron", enums.ResourceType.PLATE));
    public static final RegistryObject<Item> PLATE_ZINC = ITEMS.register("plate_zinc", () -> new ItemResource("zinc", enums.ResourceType.PLATE));
    public static final RegistryObject<Item> PLATE_BRONZE = ITEMS.register("plate_bronze", () -> new ItemResource("bronze", enums.ResourceType.PLATE));
    public static final RegistryObject<Item> PLATE_ALUMINIUM = ITEMS.register("plate_aluminium", () -> new ItemResource("aluminium", enums.ResourceType.PLATE));
    public static final RegistryObject<Item> PLATE_TIN = ITEMS.register("plate_tin", () -> new ItemResource("tin", enums.ResourceType.PLATE));
    public static final RegistryObject<Item> PLATE_LEAD = ITEMS.register("plate_lead", () -> new ItemResource("lead", enums.ResourceType.PLATE));
    public static final RegistryObject<Item> PLATE_COPPER = ITEMS.register("plate_copper", () -> new ItemResource("copper", enums.ResourceType.PLATE));
    public static final RegistryObject<Item> DUST_NETHER_QUARTZ = ITEMS.register("dust_quartz", () -> new ItemResource("nether_quartz", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_ORIGINIUM = ITEMS.register("dust_originium", () -> new ItemResource("originium", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_GOLD = ITEMS.register("dust_gold", () -> new ItemResource("gold", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_BRASS = ITEMS.register("dust_brass", () -> new ItemResource("brass", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_STEEL = ITEMS.register("dust_steel", () -> new ItemResource("steel", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_COAL = ITEMS.register("dust_coal", () -> new ItemResource("coal", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_IRON = ITEMS.register("dust_iron", () -> new ItemResource("iron", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_ZINC = ITEMS.register("dust_zinc", () -> new ItemResource("bronze", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_BRONZE = ITEMS.register("dust_bronze", () -> new ItemResource("bronze", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_ALUMINIUM = ITEMS.register("dust_aluminium", () -> new ItemResource("aluminium", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_TIN = ITEMS.register("dust_tin", () -> new ItemResource("tin", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_LEAD = ITEMS.register("dust_lead", () -> new ItemResource("lead", enums.ResourceType.DUST));
    public static final RegistryObject<Item> DUST_COPPER = ITEMS.register("dust_copper", () -> new ItemResource("copper", enums.ResourceType.DUST));
    public static final RegistryObject<Item> GEAR_GOLD = ITEMS.register("gear_gold", () -> new ItemResource("gold", enums.ResourceType.GEAR));
    public static final RegistryObject<Item> GEAR_IRON = ITEMS.register("gear_iron", () -> new ItemResource("iron", enums.ResourceType.GEAR));
    public static final RegistryObject<Item> GEAR_STEEL = ITEMS.register("gear_steel", () -> new ItemResource("steel", enums.ResourceType.GEAR));
    public static final RegistryObject<Item> GEAR_BRONZE = ITEMS.register("gear_bronze", () -> new ItemResource("bronze", enums.ResourceType.GEAR));
    public static final RegistryObject<Item> GEAR_TIN = ITEMS.register("gear_tin", () -> new ItemResource("tin", enums.ResourceType.GEAR));
    public static final RegistryObject<Item> GEAR_COPPER = ITEMS.register("gear_copper", () -> new ItemResource("copper", enums.ResourceType.GEAR));
    public static final RegistryObject<Item> INGOT_BRASS = ITEMS.register("ingot_brass", () -> new ItemResource("brass", enums.ResourceType.INGOT));
    public static final RegistryObject<Item> INGOT_STEEL = ITEMS.register("ingot_steel", () -> new ItemResource("steel", enums.ResourceType.INGOT));
    public static final RegistryObject<Item> INGOT_ZINC = ITEMS.register("ingot_zinc", () -> new ItemResource("zinc", enums.ResourceType.INGOT));
    public static final RegistryObject<Item> INGOT_BRONZE = ITEMS.register("ingot_bronze", () -> new ItemResource("bronze", enums.ResourceType.INGOT));
    public static final RegistryObject<Item> INGOT_ALUMINIUM = ITEMS.register("ingot_aluminium", () -> new ItemResource("aluminium", enums.ResourceType.INGOT));
    public static final RegistryObject<Item> INGOT_TIN = ITEMS.register("ingot_tin", () -> new ItemResource("tin", enums.ResourceType.INGOT));
    public static final RegistryObject<Item> INGOT_LEAD = ITEMS.register("ingot_lead", () ->new ItemResource("lead", enums.ResourceType.INGOT));
    //Resources.
    //Remove on 1.17
    public static final RegistryObject<Item> INGOT_COPPER = ITEMS.register("ingot_copper", () -> new ItemResource("copper", enums.ResourceType.INGOT));
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MODID);
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
    public static final RegistryObject<MenuType<ContainerCrystalGrowthChamber>> GROWTH_CHAMBER_CONTAINER = CONTAINER.register("crystal_growth_chamber_container", () -> GROWTH_CHAMBER_CONTAINER_TYPE);//Container
    public static final RegistryObject<Block> BAUXITE_ORE = registerMetalOre_Stone("aluminium");
    public static final RegistryObject<Block> COPPER_ORE = registerMetalOre_Stone("copper");
    public static final RegistryObject<Block> TIN_ORE = registerMetalOre_Stone("tin");
    public static final RegistryObject<Block> LEAD_ORE = registerMetalOre_Stone("lead");
    public static final RegistryObject<Block> ZINC_ORE = registerMetalOre_Stone("zinc");
    public static final RegistryObject<Fluid> CRUDE_OIL_FLOWING_REGISTRY = FLUIDS.register("crude_oil_flowing", CrudeOilFluid.Flowing::new);
    public static final RegistryObject<Fluid> GASOLINE_FLOWING_REGISTRY = FLUIDS.register("gasoline_flowing", GasolineFluid.Flowing::new);
    public static final RegistryObject<Fluid> DIESEL_FLOWING_REGISTRY = FLUIDS.register("diesel_flowing", DieselFluid.Flowing::new);
    public static final RegistryObject<Fluid> FUEL_OIL_FLOWING_REGISTRY = FLUIDS.register("fuel_oil_flowing", FuelOilFluid.Flowing::new);
    public static final RegistryObject<Fluid> ORIGINIUM_SOLUTION_FLOWING_REGISTRY = FLUIDS.register("originium_solution_flowing", OriginiumSolutionFluid.Flowing::new);
    public static final RegistryObject<Fluid> NETHER_QUARTZ_SOLUTION_FLOWING_REGISTRY = FLUIDS.register("nether_quartz_flowing", NetherQuartzSolutionFluid.Flowing::new);
    public static final RegistryObject<Fluid> CRUDE_OIL_REGISTRY = FLUIDS.register("crude_oil_source", CrudeOilFluid.Source::new);
    public static final RegistryObject<Fluid> GASOLINE_REGISTRY = FLUIDS.register("gasoline_source", GasolineFluid.Source::new);
    public static final RegistryObject<Fluid> DIESEL_REGISTRY = FLUIDS.register("diesel_source", DieselFluid.Source::new);
    public static final RegistryObject<Fluid> FUEL_OIL_REGISTRY = FLUIDS.register("fuel_oil_source", FuelOilFluid.Source::new);
    public static final RegistryObject<Fluid> ORIGINIUM_SOLUTION_REGISTRY = FLUIDS.register("originium_solution_source", OriginiumSolutionFluid.Source::new);
    public static final RegistryObject<Item> FUEL_OIL_BUCKET = ITEMS.register("fuel_oil_bucket", ()-> new BucketItem(FUEL_OIL_REGISTRY, (new Item.Properties()).craftRemainder(Items.BUCKET).stacksTo(1).tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> DIESEL_BUCKET = ITEMS.register("diesel_bucket", ()-> new BucketItem(DIESEL_REGISTRY, (new Item.Properties()).craftRemainder(Items.BUCKET).stacksTo(1).tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> GASOLINE_BUCKET = ITEMS.register("gasoline_bucket", ()-> new BucketItem(GASOLINE_REGISTRY, (new Item.Properties()).craftRemainder(Items.BUCKET).stacksTo(1).tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Item> CRUDE_OIL_BUCKET = ITEMS.register("crude_oil_bucket", ()-> new BucketItem(CRUDE_OIL_REGISTRY, (new Item.Properties()).craftRemainder(Items.BUCKET).stacksTo(1).tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<Block> CRUDE_OIL = register_noItem("crude_oil", ()->new LiquidBlock(()-> (FlowingFluid) CRUDE_OIL_REGISTRY.get(), BlockBehaviour.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));
    public static final RegistryObject<Block> GASOLINE = register_noItem("gasoline", ()->new LiquidBlock(()-> (FlowingFluid) GASOLINE_REGISTRY.get(), BlockBehaviour.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));
    public static final RegistryObject<Block> DIESEL = register_noItem("diesel", ()->new LiquidBlock(()-> (FlowingFluid) DIESEL_REGISTRY.get(), BlockBehaviour.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));
    public static final RegistryObject<Block> FUEL_OIL = register_noItem("fuel_oil", ()->new LiquidBlock(()-> (FlowingFluid) FUEL_OIL_REGISTRY.get(), BlockBehaviour.Properties.of(Material.WATER).noCollission().strength(100.0F).noDrops()));
    public static final RegistryObject<Block> BOUNDING_BOX = register_noItem("boundingbox", ()->new BoundingBoxBlock(BlockBehaviour.Properties.of(Material.METAL).strength(3, 10).sound(SoundType.METAL).noOcclusion()));
    public static final RegistryObject<Fluid> NETHER_QUARTZ_SOLUTION_REGISTRY = FLUIDS.register("nether_quartz_source", NetherQuartzSolutionFluid.Source::new);

    public static final RegistryObject<RecipeSerializer<PressingRecipe>> PRESSING = registerRecipes("pressing", PressingRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<CrushingRecipe>> CRUSHING = registerRecipes("crushing", CrushingRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<AlloyingRecipe>> ALLOYING = registerRecipes("alloying", AlloyingRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<CrystalizingRecipe>> CRYSTALIZING = registerRecipes("crystalizing", CrystalizingRecipe.Serializer::new);
    //Special Crafting Recipe
    public static final RegistryObject<RecipeSerializer<ReloadRecipes>> RELOADING = registerRecipes("reloading", ()-> new SimpleRecipeSerializer<>(ReloadRecipes::new));
    public static final RegistryObject<RecipeSerializer<RepairRecipe>> REPAIRING = registerRecipes("repairing", ()-> new SimpleRecipeSerializer<>(RepairRecipe::new));
    private static final MenuType<ContainerKansenInventory> SHIP_INVENTORY = new MenuType<>((IContainerFactory<ContainerKansenInventory>)ContainerKansenInventory::new);
    public static final RegistryObject<MenuType<ContainerKansenInventory>> SHIP_CONTAINER = CONTAINER.register("kansen_inventory", () -> SHIP_INVENTORY);
    private static final MenuType<RiggingContainer> RIGGING_INVENTORY_TYPE = new MenuType<>((IContainerFactory<RiggingContainer>)RiggingContainer::new);
    public static final RegistryObject<MenuType<RiggingContainer>> RIGGING_INVENTORY = CONTAINER.register("rigging_inventory", () -> RIGGING_INVENTORY_TYPE);
    //entity
    public static final RegistryObject<EntityType<EntityAyanami>> AYANAMI = ENTITIES.register("entityayanami", () -> EntityType.Builder.of(EntityAyanami::new, MobCategory.CREATURE).sized(0.572F, 1.525F).build(ModResourceLocation("entityayanami").toString()));
    public static final RegistryObject<EntityType<EntityJavelin>> JAVELIN = ENTITIES.register("entityjavelin", () -> EntityType.Builder.of(EntityJavelin::new, MobCategory.CREATURE).sized(0.572F, 1.525F).build(ModResourceLocation("entityayanami").toString()));
    public static final RegistryObject<EntityType<EntityZ23>> Z23 = ENTITIES.register("entityz23", () -> EntityType.Builder.of(EntityZ23::new, MobCategory.CREATURE).sized(0.572F, 1.525F).build(ModResourceLocation("entityz23").toString()));
    public static final RegistryObject<EntityType<EntityLaffey>> LAFFEY = ENTITIES.register("entitylaffey", () -> EntityType.Builder.of(EntityLaffey::new, MobCategory.CREATURE).sized(0.572F, 1.525F).build(ModResourceLocation("entitylaffey").toString()));
    public static final RegistryObject<EntityType<EntityGangwon>> GANGWON = ENTITIES.register("entitygangwon", () -> EntityType.Builder.of(EntityGangwon::new, MobCategory.CREATURE).sized(0.572F, 1.35F).build(ModResourceLocation("entitygandwon").toString()));
    public static final RegistryObject<EntityType<EntityEnterprise>> ENTERPRISE = ENTITIES.register("entityenterprise", () -> EntityType.Builder.of(EntityEnterprise::new, MobCategory.CREATURE).sized(0.65F, 1.825F).build(ModResourceLocation("entityenterprise").toString()));
    public static final RegistryObject<EntityType<EntityM4A1>> M4A1 = ENTITIES.register("entitym4a1", () -> EntityType.Builder.of(EntityM4A1::new, MobCategory.CREATURE).sized(0.65F, 1.825F).build(ModResourceLocation("entitym4a1").toString()));
    public static final RegistryObject<EntityType<EntityShiroko>> SHIROKO = ENTITIES.register("entityshiroko", () -> EntityType.Builder.of(EntityShiroko::new, MobCategory.CREATURE).sized(0.572F, 1.575F).build(ModResourceLocation("entityshiroko").toString()));
    public static final RegistryObject<EntityType<EntityNagato>> NAGATO = ENTITIES.register("entitynagato", () -> EntityType.Builder.of(EntityNagato::new, MobCategory.CREATURE).sized(0.572F, 1.32F).build(ModResourceLocation("entitynagato").toString()));
    public static final RegistryObject<EntityType<EntityChen>> CHEN = ENTITIES.register("entitychen", () -> EntityType.Builder.of(EntityChen::new, MobCategory.CREATURE).sized(0.572F, 1.68F).build(ModResourceLocation("entitychen").toString()));
    public static final RegistryObject<EntityType<EntityMudrock>> MUDROCK = ENTITIES.register("entitymudrock", () -> EntityType.Builder.of(EntityMudrock::new, MobCategory.CREATURE).sized(0.572F, 1.63F).build(ModResourceLocation("entitymudrock").toString()));
    public static final RegistryObject<EntityType<EntityAmiya>> AMIYA = ENTITIES.register("entityamiya", () -> EntityType.Builder.of(EntityAmiya::new, MobCategory.CREATURE).sized(0.572F, 1.42F).build(ModResourceLocation("entityamiya").toString()));
    public static final RegistryObject<EntityType<EntityRosmontis>> ROSMONTIS = ENTITIES.register("entityrosmontis", () -> EntityType.Builder.of(EntityRosmontis::new, MobCategory.CREATURE).sized(0.572F, 1.63F).build(ModResourceLocation("entityamiya").toString()));
    public static final RegistryObject<EntityType<EntityTalulah>> TALULAH = ENTITIES.register("entitytalulah", () -> EntityType.Builder.of(EntityTalulah::new, MobCategory.CREATURE).sized(0.572F, 1.63F).build(ModResourceLocation("entitytalulah").toString()));
    public static final RegistryObject<EntityType<EntityTexas>> TEXAS = ENTITIES.register("entitytexas", () -> EntityType.Builder.of(EntityTexas::new, MobCategory.CREATURE).sized(0.572F, 1.61F).build(ModResourceLocation("entitytexas").toString()));
    public static final RegistryObject<EntityType<EntityFrostnova>> FROSTNOVA = ENTITIES.register("entityfrostnova", () -> EntityType.Builder.of(EntityFrostnova::new, MobCategory.CREATURE).sized(0.572F, 1.63F).build(ModResourceLocation("entityfrostnova").toString()));
    public static final RegistryObject<EntityType<EntityLappland>> LAPPLAND = ENTITIES.register("entitylappland", () -> EntityType.Builder.of(EntityLappland::new, MobCategory.CREATURE).sized(0.572F, 1.61F).build(ModResourceLocation("entitylappland").toString()));
    public static final RegistryObject<EntityType<EntitySiege>> SIEGE = ENTITIES.register("entitysiege", () -> EntityType.Builder.of(EntitySiege::new, MobCategory.CREATURE).sized(0.572F, 1.72F).build(ModResourceLocation("entitysiege").toString()));
    public static final RegistryObject<EntityType<EntitySchwarz>> SCHWARZ = ENTITIES.register("entityschwarz", () -> EntityType.Builder.of(EntitySchwarz::new, MobCategory.CREATURE).sized(0.572F, 1.69F).build(ModResourceLocation("entityschwarz").toString()));
    public static final RegistryObject<EntityType<EntitySylvi>> SYLVI = ENTITIES.register("entitysylvi", () -> EntityType.Builder.of(EntitySylvi::new, MobCategory.CREATURE).sized(0.572F, 1.69F).build(ModResourceLocation("entityschwarz").toString()));
    public static final RegistryObject<EntityType<EntityYamato>> YAMATO = ENTITIES.register("entityyamato", () -> EntityType.Builder.of(EntityYamato::new, MobCategory.CREATURE).sized(0.572F, 1.69F).build(ModResourceLocation("entityyamato").toString()));
    //projectile
    public static final RegistryObject<EntityType<EntityCannonPelllet>> CANNONSHELL = ENTITIES.register("entitycannonshell", () -> EntityType.Builder.<EntityCannonPelllet>of(EntityCannonPelllet::new, MobCategory.MISC).sized(0.5F, 0.5F).build(ModResourceLocation("projectilecannonshell").toString()));
    public static final RegistryObject<EntityType<EntityArtsProjectile>> PROJECTILEARTS = ENTITIES.register("entityartsprojectile", () -> EntityType.Builder.<EntityArtsProjectile>of(EntityArtsProjectile::new, MobCategory.MISC).sized(0.5F, 0.5F).build(ModResourceLocation("projectilearts").toString()));
    public static final RegistryObject<EntityType<EntityProjectileTorpedo>> TORPEDO = ENTITIES.register("entitytorpedo", () -> EntityType.Builder.<EntityProjectileTorpedo>of(EntityProjectileTorpedo::new, MobCategory.MISC).sized(0.5F, 0.5F).build(ModResourceLocation("projectiletorpedo").toString()));
    public static final RegistryObject<EntityType<EntityProjectileBullet>> GUN_BULLET = ENTITIES.register("entitybullet", () -> EntityType.Builder.<EntityProjectileBullet>of(EntityProjectileBullet::new, MobCategory.MISC).sized(0.2F, 0.2F).build(ModResourceLocation("projectilebullet").toString()));
    public static final RegistryObject<EntityType<EntityMissileDroneMissile>> DRONE_MISSILE = ENTITIES.register("projectiledrone_missile", () -> EntityType.Builder.of(EntityMissileDroneMissile::new, MobCategory.MISC).sized(0.2F, 0.2F).build(ModResourceLocation("projectiledrone_missile").toString()));
    public static final RegistryObject<EntityType<EntityThrownKnifeProjectile>> THROWN_KNIFE = ENTITIES.register("projectile_knife", () -> EntityType.Builder.<EntityThrownKnifeProjectile>of(EntityThrownKnifeProjectile::new, MobCategory.MISC).sized(0.2F, 0.2F).build(ModResourceLocation("projectile_knife").toString()));
    //?????
    public static final RegistryObject<EntityType<EntityClaymore>> CLAYMORE = ENTITIES.register("entityclaymore", () -> EntityType.Builder.of(EntityClaymore::new, MobCategory.CREATURE).sized(0.7F, 2.31F).build(ModResourceLocation("entityclaymore").toString()));
    //Planes
    public static final RegistryObject<EntityType<EntityF4fWildcat>> F4FWildCat = ENTITIES.register("planef4fwildcat", () -> EntityType.Builder.of(EntityF4fWildcat::new, MobCategory.MISC).sized(0.5F, 0.5F).build(ModResourceLocation("entityplanef4fwildcat").toString()));
    public static final RegistryObject<EntityType<EntityMissileDrone>> MISSILEDRONE = ENTITIES.register("missiledrone", () -> EntityType.Builder.of(EntityMissileDrone::new, MobCategory.MISC).sized(0.5F, 0.5F).build(ModResourceLocation("missiledrone").toString()));

    public static CreativeModeTab PA_GROUP = new CreativeModeTab(MODID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Rainbow_Wisdom_Cube.get());
        }
    };
    public static CreativeModeTab PA_MACHINES = new CreativeModeTab("pa_machines") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(METAL_PRESS.get().asItem());
        }
    };
    public static final RegistryObject<Block> RECRUIT_BEACON = registerAnimatedMachines("recruit_beacon", RecruitBeaconBlock::new, PA_MACHINES, new Item.Properties());
    public static final RegistryObject<BlockEntityType<TileEntityRecruitBeacon>> RECRUIT_BEACON_BLOCK_ENTITY = register_block_entities("recruit_beacon_te", TileEntityRecruitBeacon::new, RECRUIT_BEACON);
    public static final RegistryObject<Block> REENFORCEDCONCRETE = register_blockWithToolTiponItem("reenforced_concrete",()-> new MultiblockStructureBlocks(BlockBehaviour.Properties.of(Material.STONE).strength(3, 10).sound(SoundType.STONE).noOcclusion()), PA_MACHINES);
    public static final RegistryObject<BlockEntityType<MultiBlockReenforcedConcrete>> MULTIBLOCKSTRUCTURE_CONCRETE_BLOCK_ENTITY = register_block_entities("multiblock_structure_concrete_te", MultiBlockReenforcedConcrete::new, REENFORCEDCONCRETE);
    public static final RegistryObject<Block> DRYDOCKCONTROLLER = register_blockWithToolTiponItem("drydock_controller",()-> new blockMultiblockDryDockController(BlockBehaviour.Properties.of(Material.METAL).strength(3, 10).sound(SoundType.METAL).noOcclusion()), PA_MACHINES);
    public static final RegistryObject<BlockEntityType<MultiblockDrydockTE>> DRYDOCK_BLOCK_ENTITY = register_block_entities("drydock", MultiblockDrydockTE::new, DRYDOCKCONTROLLER);
    public static final RegistryObject<Block> CRYSTAL_GROWTH_CHAMBER = register_blockWithToolTiponItem("crystal_growth_chamber", ()->new CrystalGrowthChamberBlock(BlockBehaviour.Properties.of(Material.STONE).strength(3, 10).lightLevel((block)->0).sound(SoundType.STONE).noOcclusion()), PA_MACHINES);
    public static final RegistryObject<BlockEntityType<TileEntityCrystalGrowthChamber>> CRYSTAL_GROWTH_CHAMBER_BLOCK_ENTITY = register_block_entities("crystal_growth_chamber_te", TileEntityCrystalGrowthChamber::new, CRYSTAL_GROWTH_CHAMBER);
    public static final RegistryObject<Block> BASIC_REFINERY = register_blockWithToolTiponItem("basic_refinery", ()->new BasicRefineryBlock(BlockBehaviour.Properties.of(Material.STONE).strength(3, 10).lightLevel(getLightValueLit(8)).sound(SoundType.STONE).noOcclusion()), PA_MACHINES);
    public static final RegistryObject<BlockEntityType<TileEntityBasicRefinery>> BASIC_REFINERY_BLOCK_ENTITY = register_block_entities("basic_refinery_te", TileEntityBasicRefinery::new, BASIC_REFINERY);
    public static final RegistryObject<Block> ALLOY_FURNACE = register_blockWithToolTiponItem("alloy_furnace", AlloyFurnaceBlock::new, PA_MACHINES);
    public static final RegistryObject<BlockEntityType<TileEntityAlloyFurnace>> ALLOY_FURNACE_BLOCK_ENTITY = register_block_entities("alloy_furnace", TileEntityAlloyFurnace::new, ALLOY_FURNACE);
    public static final RegistryObject<Block> METAL_PRESS = register_blockWithToolTiponItem("metal_press", MetalPressBlock::new, PA_MACHINES);
    public static final RegistryObject<BlockEntityType<TileEntityMetalPress>> METAL_PRESS_BLOCK_ENTITY = register_block_entities("metal_press_te", TileEntityMetalPress::new, METAL_PRESS);


    public static CreativeModeTab PA_WEAPONS = new CreativeModeTab("pa_weapons") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(BONKBAT.get());
        }
    };
    public static final RegistryObject<Item> BANDAGE_ROLL = ITEMS.register("bandage_roll", () -> new ItemBandage(new Item.Properties()
            .tab(PA_GROUP)));
    public static final RegistryObject<Item> DISC_ENTERTHEBEGINNING = ITEMS.register("disc_enterthebeginning", () -> new RecordItem(15, ()->registerSounds.DISC_ENTERTHEBEGINNING, new Item.Properties()
            .tab(PA_GROUP).stacksTo(1))
    {
        @Nonnull
        @Override
        public Component getName(@Nonnull ItemStack stack) {
            return new TranslatableComponent("item.projectazure.music_disc");
        }

        @Override
        public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
            super.appendHoverText(stack, worldIn, tooltip, flagIn);
            tooltip.add(new TranslatableComponent("disc.enterthebeginning.desc1").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(7829367))));
        }
    });
    public static final RegistryObject<Item> DISC_SANDROLL = ITEMS.register("disc_sandroll", () -> new RecordItem(15, ()->registerSounds.DISC_SANDROLL, new Item.Properties()
            .tab(PA_GROUP).stacksTo(1))
    {
        @Nonnull
        @Override
        public Component getName(@Nonnull ItemStack stack) {
            return new TranslatableComponent("item.projectazure.music_disc");
        }
    });
    public static final RegistryObject<Item> DISC_SANDSTORM = ITEMS.register("disc_sandstorm", () -> new RecordItem(15, ()->registerSounds.DISC_SANDSTORM, new Item.Properties()
            .tab(PA_GROUP).stacksTo(1))
    {
        @Nonnull
        @Override
        public Component getName(@Nonnull ItemStack stack) {
            return new TranslatableComponent("item.projectazure.music_disc");
        }
    });
    public static final RegistryObject<Item> DISC_CC5 = ITEMS.register("disc_cc5", () -> new RecordItem(15, ()->registerSounds.DISC_CC5, new Item.Properties()
            .tab(PA_GROUP).stacksTo(1))
    {
        @Nonnull
        @Override
        public Component getName(@Nonnull ItemStack stack) {
            return new TranslatableComponent("item.projectazure.music_disc");
        }
    });
    //LET THE BASS KICK O-oooooooooo AAAA E A A I A U
    public static final RegistryObject<Item> DISC_BRAINPOWER = ITEMS.register("disc_brainpower", () -> new RecordItem(15, ()->registerSounds.DISC_BRAINPOWER, new Item.Properties()
            .tab(PA_GROUP).stacksTo(1))
    {
        @Override
        public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level worldIn, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flagIn) {
            super.appendHoverText(stack, worldIn, tooltip, flagIn);
            tooltip.add(new TranslatableComponent("disc.brainpower.desc1").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(7829367))));
        }

        @Nonnull
        @Override
        public Component getName(@Nonnull ItemStack stack) {
            return new TranslatableComponent("item.projectazure.music_disc");
        }
    });
    //I'M READY TO GO TONIIIIGHT YEAH THERES PARTY ALRIGHTTTTTTT WE DON'T NEED REASON FOR JOY OH YEAHHHHH
    public static final RegistryObject<Item> DISC_FRIDAYNIGHT = ITEMS.register("disc_fridaynight", () -> new RecordItem(15, ()->registerSounds.DISC_FRIDAY_NIGHT, new Item.Properties()
            .tab(PA_GROUP).stacksTo(1)){
        @Nonnull
        @Override
        public Component getName(@Nonnull ItemStack stack) {
            return new TranslatableComponent("item.projectazure.music_disc");
        }
    });
    public static final RegistryObject<Item> AMMO_MISSILE = ITEMS.register("missile_ammo", () -> new ItemAmmo(enums.AmmoCalibur.DRONE_MISSILE, 1 ,new Item.Properties()
            .tab(PA_GROUP).stacksTo(1)));
    public static final RegistryObject<Item> AMMO_TORPEDO = ITEMS.register("torpedo_ammo", () -> new ItemAmmo(enums.AmmoCalibur.TORPEDO, 1 ,new Item.Properties()
            .tab(PA_GROUP).stacksTo(1)));
    public static final RegistryObject<Item> AMMO_5_56 = ITEMS.register("5_56_ammo", () -> new ItemAmmo(enums.AmmoCalibur.AMMO_5_56, 6 ,new Item.Properties()
            .tab(PA_GROUP).stacksTo(5)));
    public static final RegistryObject<Item> AMMO_GENERIC = ITEMS.register("ammo_generic", () -> new ItemCannonshell(enums.AmmoCategory.GENERIC ,new Item.Properties()
            .tab(PA_GROUP)));
    public static final RegistryObject<Item> STEEL_RIFLE_FRAME = ITEMS.register("steel_gunframe_rifle", () -> new Item(new Item.Properties()
            .tab(PA_GROUP)));
    public static final RegistryObject<Item> PISTOL_GRIP = ITEMS.register("pistol_grip", () -> new Item(new Item.Properties()
            .tab(PA_GROUP)));
    public static final RegistryObject<Item> OATHRING = ITEMS.register("oath_ring", () -> new ItemBaseTooltip(new Item.Properties()
            .tab(PA_GROUP)
            .rarity(Rarity.EPIC)
            .stacksTo(1)){
        @Override
        public boolean isFoil(ItemStack stack) {
            return true;
        }
    });
    public static final RegistryObject<Item> WISDOM_CUBE = ITEMS.register("wisdomcube", () -> new ItemBaseTooltip(new Item.Properties()
            .tab(PA_GROUP)
            .rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> Rainbow_Wisdom_Cube = ITEMS.register("rainbow_wisdomcube", () -> new itemRainbowWisdomCube(new Item.Properties()
            .tab(PA_GROUP)
            .rarity(Rarity.EPIC).stacksTo(1)));
    public static final RegistryObject<Item> MAGAZINE_5_56 = ITEMS.register("5.56_magazine", () -> new ItemMagazine(enums.AmmoCalibur.AMMO_5_56, 30, new Item.Properties()
            .tab(PA_GROUP)
            .stacksTo(1)
            .durability(7)));
    public static final RegistryObject<Item> ABYDOS_550 = ITEMS.register("abydos550", () -> new ItemAbydos550(false, 2, 30, 72, 3, registerSounds.RIFLE_FIRE_SUPPRESSED, SoundEvents.LEVER_CLICK, 0, 0.5F, new Item.Properties()
            .tab(PA_WEAPONS).stacksTo(1), true, MAGAZINE_5_56.get()));
    public static final RegistryObject<Item> ENERGY_DRINK_DEBUG = ITEMS.register("energy_drink", () -> new Item(new Item.Properties()
            .tab(PA_GROUP)
            .rarity(Rarity.EPIC)){
        @Override
        public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
            super.appendHoverText(stack, worldIn, tooltip, flagIn);
            if(worldIn != null && worldIn.isClientSide) {
                if (Screen.hasShiftDown()) {
                    tooltip.add(new TranslatableComponent("item.projectazure.energy_drink.tooltip1").setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(0xff00fc)).withItalic(true)));
                    tooltip.add(new TranslatableComponent("item.projectazure.energy_drink.tooltip2"));
                    tooltip.add(new TranslatableComponent("item.projectazure.energy_drink.tooltip3"));
                    tooltip.add(new TranslatableComponent("item.projectazure.energy_drink.tooltip4").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x990000)).withItalic(true)));
                    tooltip.add(new TranslatableComponent("item.projectazure.energy_drink.tooltip5").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x5e5e5e)).withItalic(true)));
                    tooltip.add(new TranslatableComponent("item.projectazure.energy_drink.tooltip6").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x5e5e5e)).withItalic(true)));
                    tooltip.add(new TranslatableComponent("item.projectazure.energy_drink.tooltip7").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x999900)).withItalic(true)));
                    tooltip.add(new TranslatableComponent("item.projectazure.energy_drink.tooltip8").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x5e5e5e)).withItalic(true)));
                    tooltip.add(new TranslatableComponent("item.projectazure.energy_drink.tooltip9").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x5e5e5e)).withItalic(true)));
                    tooltip.add(new TranslatableComponent("item.projectazure.energy_drink.tooltip10").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x5e5e5e)).withItalic(true)));
                } else {
                    Component shift = new TextComponent("[SHIFT]").withStyle(ChatFormatting.YELLOW);
                    tooltip.add(new TranslatableComponent("item.projectazure.energy_drink.desc1").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0x5e5e5e))));
                    tooltip.add(new TranslatableComponent("item.projectazure.energy_drink.shiftinfo", shift).withStyle(ChatFormatting.GRAY));
                }
            }
        }
    });
    public static final RegistryObject<Item> BITUMEN = ITEMS.register("bitumen", () -> new Item(new Item.Properties()
            .tab(PA_GROUP)));

    public static CreativeModeTab PA_SHIPS = new CreativeModeTab("pa_ship") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(WISDOM_CUBE.get());
        }
    };
    public static final RegistryObject<Item> SPAWM_YAMATO = ITEMS.register("spawnyamato", () -> new ItemKansenSpawnEgg(YAMATO, new Item.Properties()
            .tab(PA_SHIPS)));
    public static final RegistryObject<Item> SPAWM_SYLVI = ITEMS.register("spawnsylvi", () -> new ItemKansenSpawnEgg(SYLVI, new Item.Properties()
            .tab(PA_SHIPS)));
    public static final RegistryObject<Item> SPAWN_LAPPLAND = ITEMS.register("spawnlappland", () -> new ItemKansenSpawnEgg(LAPPLAND, new Item.Properties()
            .tab(PA_SHIPS)));
    public static final RegistryObject<Item> SPAWN_SCHWARZ = ITEMS.register("spawnschwarz", () -> new ItemKansenSpawnEgg(SCHWARZ, new Item.Properties()
            .tab(PA_SHIPS)));
    public static final RegistryObject<Item> SPAWN_SIEGE = ITEMS.register("spawnsiege", () -> new ItemKansenSpawnEgg(SIEGE, new Item.Properties()
            .tab(PA_SHIPS)));
    public static final RegistryObject<Item> SPAWN_FROSTNOVA = ITEMS.register("spawnfrostnova", () -> new ItemKansenSpawnEgg(FROSTNOVA, new Item.Properties()
            .tab(PA_SHIPS)));
    public static final RegistryObject<Item> SPAWN_TEXAS = ITEMS.register("spawntexas", () -> new ItemKansenSpawnEgg(TEXAS, new Item.Properties()
            .tab(PA_SHIPS)));
    public static final RegistryObject<Item> SPAWN_SHIROKO = ITEMS.register("spawnshiroko", () -> new ItemKansenSpawnEgg(SHIROKO, new Item.Properties()
            .tab(PA_SHIPS)));
    public static final RegistryObject<Item> SPAWN_TALULAH = ITEMS.register("spawntalulah", () -> new ItemKansenSpawnEgg(TALULAH, new Item.Properties()
            .tab(PA_SHIPS)));
    public static final RegistryObject<Item> SPAWN_ROSMONTIS = ITEMS.register("spawnrosmontis", () -> new ItemKansenSpawnEgg(ROSMONTIS, new Item.Properties()
            .tab(PA_SHIPS)));
    public static final RegistryObject<Item> SPAWN_AMIYA = ITEMS.register("spawnamiya", () -> new ItemKansenSpawnEgg(AMIYA, new Item.Properties()
            .tab(PA_SHIPS)));
    public static final RegistryObject<Item> SPAWN_M4A1 = ITEMS.register("spawnm4a1", () -> new ItemKansenSpawnEgg(M4A1, new Item.Properties()
            .tab(PA_SHIPS)));
    public static final RegistryObject<Item> SPAWN_MUDROCK = ITEMS.register("spawnmudrock", () -> new ItemKansenSpawnEgg(MUDROCK, new Item.Properties()
            .tab(PA_SHIPS)));
    public static final RegistryObject<Item> SPAWN_CHEN = ITEMS.register("spawnchen", () -> new ItemKansenSpawnEgg(CHEN, new Item.Properties()
            .tab(PA_SHIPS)));
    public static final RegistryObject<Item> SPAWN_NAGATO = ITEMS.register("spawnnagato", () -> new ItemKansenSpawnEgg(NAGATO, new Item.Properties()
            .tab(PA_SHIPS)));
    public static final RegistryObject<Item> SPAWM_ENTERPRISE = ITEMS.register("spawnenterprise", ()-> new ItemKansenSpawnEgg(ENTERPRISE, new Item.Properties().tab(PA_SHIPS)));
    public static final RegistryObject<Item> SPAWN_LAFFEY = ITEMS.register("spawnlaffey", () -> new ItemKansenSpawnEgg(LAFFEY, new Item.Properties()
            .tab(PA_SHIPS)));
    public static final RegistryObject<Item> SPAWN_GANGWON = ITEMS.register("spawngangwon", () -> new ItemKansenSpawnEgg(GANGWON, new Item.Properties()
            .tab(PA_SHIPS)));
    public static final RegistryObject<Item> SPAWN_Z23 = ITEMS.register("spawnz23", () -> new ItemKansenSpawnEgg(Z23, new Item.Properties()
            .tab(PA_SHIPS)));
    public static final RegistryObject<Item> SPAWM_JAVELIN = ITEMS.register("spawnjavelin", () -> new ItemKansenSpawnEgg(JAVELIN, new Item.Properties()
            .tab(PA_SHIPS)));
    public static final RegistryObject<Item> SPAWM_AYANAMI = ITEMS.register("spawnayanami", () -> new ItemKansenSpawnEgg(AYANAMI, new Item.Properties()
            .tab(PA_SHIPS)));

    public static CreativeModeTab PA_RESOURCES = new CreativeModeTab("pa_resources") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(INGOT_COPPER.get().asItem());
        }
    };
    public static final RegistryObject<Block> MACHINE_FRAME = register("machine_frame", () ->
            new MultiblockStructureBlocks((BlockBehaviour.Properties.of(Material.METAL).strength(3, 10).sound(SoundType.METAL))){

                @Nullable
                @Override
                public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
                    return new MultiblockSteelFrame(pos, state);
                }
            }, PA_RESOURCES);
    public static final RegistryObject<BlockEntityType<MultiblockSteelFrame>> MULTIBLOCKSTRUCTURE_STEELFRAME_BLOCK_ENTITY = register_block_entities("multiblock_structure_steel_te", MultiblockSteelFrame::new, MACHINE_FRAME);
    public static final RegistryObject<Block> REENFORCED_PLANK = register("reenforced_plank",() -> new Block(BlockBehaviour.Properties.of(Material.WOOD).strength(4F, 6F)), PA_RESOURCES);
    public static final RegistryObject<Block> ORIROCK = register("orirock",() -> new OreBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5F, 3F)), PA_RESOURCES);
    public static final RegistryObject<Item> ORUNDUM = ITEMS.register("orundum", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> HEADHUNTING_PCB = ITEMS.register("headhunting_pcb", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES).stacksTo(16)));
    public static final RegistryObject<Item> ORIGINIUM_PRIME = ITEMS.register("originium_prime", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES).food(ModFoods.ORIGINIUM_PRIME)));
    public static final RegistryObject<Item> ORIGINITE = ITEMS.register("originite", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> NETHER_QUARTZ_SEED = ITEMS.register("quartz_seed", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    //Originium Engineering
    public static final RegistryObject<Item> ORIGINIUM_SEED = ITEMS.register("originium_seed", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> PLATE_POLYMER = ITEMS.register("polymer_plate", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    //Natural Resource
    public static final RegistryObject<Item> TREE_SAP = ITEMS.register("tree_sap", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> RESISTOR_PRIMITIVE = ITEMS.register("resistor_primitive", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> CAPACITOR_PRIMITIVE = ITEMS.register("capacitor_primitive", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> COPPER_COIL = ITEMS.register("copper_coil", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> ADVANCED_CIRCUIT = ITEMS.register("circuit_advanced", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> PRIMITIVE_CIRCUIT = ITEMS.register("circuit_primitive", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> BASIC_MOTOR = ITEMS.register("motor_basic", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> MECHANICAL_PARTS = ITEMS.register("mechanical_parts", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> STEEL_PIPE = ITEMS.register("steel_pipe", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> IRON_PIPE = ITEMS.register("iron_pipe", () -> new Item(new Item.Properties()
            .tab(PA_RESOURCES)));
    public static final RegistryObject<Item> DRONE_BAMISSILE = ITEMS.register("missiledrone", () -> new ItemMissleDrone(new Item.Properties()
            .tab(PA_WEAPONS).stacksTo(1),10, 20000));
    public static final RegistryObject<Item> EQUIPMENT_GUN_127MM = ITEMS.register("equipment_gun_127mm", () -> new ItemEquipmentGun127Mm(new Item.Properties()
            .tab(PA_WEAPONS).stacksTo(1), 40));
    public static final RegistryObject<Item> EQUIPMENT_TORPEDO_533MM = ITEMS.register("equipment_torpedo_533mm", () -> new ItemEquipmentTorpedo533Mm(new Item.Properties()
            .tab(PA_WEAPONS).stacksTo(1), 40));
    public static final RegistryObject<Item> BB_DEFAULT_RIGGING = ITEMS.register("bb_default_rigging", () -> new ItemRiggingBBDefault(new Item.Properties()
            .tab(PA_WEAPONS).stacksTo(1), 1200));
    public static final RegistryObject<Item> CV_DEFAULT_RIGGING = ITEMS.register("cv_default_rigging", () -> new ItemRiggingCVDefault(new Item.Properties()
            .tab(PA_WEAPONS).stacksTo(1), 750));
    public static final RegistryObject<Item> DD_DEFAULT_RIGGING = ITEMS.register("dd_default_rigging", () -> new itemRiggingDDDefault(new Item.Properties()
    .tab(PA_WEAPONS).stacksTo(1), 500));
    public static final RegistryObject<Item> SLEDGEHAMMER = ITEMS.register("sledgehammer", () -> new ItemSledgeHammer(10, -3.75F, ModMaterials.SLEDGEHAMMER, new Item.Properties().tab(PA_WEAPONS).stacksTo(1)));
    public static final RegistryObject<Item> BONKBAT = ITEMS.register("bonk_bat", () -> new ItemBonkBat(new Item.Properties()
            .tab(PA_WEAPONS)
            .rarity(Rarity.EPIC)
            .stacksTo(1)));
    public static final RegistryObject<Item> COMPOUNDBOW = ITEMS.register("compoundbow", () -> new BowItem(new Item.Properties().tab(PA_WEAPONS).durability(1000)));
    public static final RegistryObject<Item> TACTICAL_KNIFE = ITEMS.register("tactical_knife", () -> new ItemThrowableKnife(ModMaterials.KNIFE, 3, -0.2F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> FLEXABLE_SWORD_THINGY = ITEMS.register("flexsword", () -> new ModSwordItem(ModMaterials.FLEXABLESWORD, 1, -1.0F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> WARHAMMER = ITEMS.register("warhammer", () -> new ModSwordItem(ModMaterials.WARHAMMER, 1, -1.8F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> CRESCENTKATANA_KURO = ITEMS.register("crescentkatana_kuro", () -> new ModSwordItem(ModMaterials.CRESCENT_KATANA_KURO, 1, -1.2F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> CRESCENTKATANA_SHIRO = ITEMS.register("crescentkatana_shiro", () -> new ModSwordItem(ModMaterials.CRESCENT_KATANA_SHIRO, 1, -1.2F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> SHEATH = ITEMS.register("sheath", () -> new ModSwordItem(ModMaterials.SHEATH, 1, -1.5F, new Item.Properties().tab(PA_WEAPONS)));
    public static final RegistryObject<Item> CHIXIAO = ITEMS.register("chixiao", () -> new ModSwordItem(ModMaterials.CHIXIAO, 1, -1.7F, new Item.Properties().tab(PA_WEAPONS)));
    //You know the rules and so do I
    public static final RegistryObject<Item> DISC_RICKROLL = ITEMS.register("disc_rickroll", () -> new RecordItem(15, ()->registerSounds.DISC_RICKROLL, new Item.Properties()
            .tab(PA_WEAPONS).stacksTo(1))
    {
        @Nonnull
        @Override
        public Component getName(@Nonnull ItemStack stack) {
            return new TranslatableComponent("item.projectazure.music_disc");
        }
    });
    public static float WildcatHP = 30;
    public static final RegistryObject<Item> EQUIPMENT_PLANE_F4FWildcat = ITEMS.register("equipment_plane_f4fwildcat", () -> new ItemPlanef4Fwildcat(new Item.Properties()
            .tab(PA_WEAPONS).stacksTo(1), (int) WildcatHP));

    public Main() {
        IEventBus eventbus = FMLJavaModLoadingContext.get().getModEventBus();
        //BIOMES.register(eventbus);
        FLUIDS.register(eventbus);
        BLOCKS.register(eventbus);
        ENTITIES.register(eventbus);
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

    private static <T extends Block> RegistryObject<T> register_noItem(String name, Supplier<T> block){
        return BLOCKS.register(name, block);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block, CreativeModeTab group){
        return register(name, block, group, new Item.Properties().tab(group));
    }

    private static <T extends Block> RegistryObject<T> register_blockWithToolTiponItem(String name, Supplier<T> block, CreativeModeTab group){
        RegistryObject<T> ret = register_noItem(name, block);
        ITEMS.register(name, () -> new BlockItem(ret.get(), new Item.Properties().tab(group)){
            @Override
            public void appendHoverText(@Nonnull ItemStack stack, @Nullable Level worldIn, @Nonnull List<Component> tooltip, @Nonnull TooltipFlag flagIn) {
                super.appendHoverText(stack, worldIn, tooltip, flagIn);
                if (worldIn != null && worldIn.isClientSide) {
                    TooltipUtils.addOnShift(tooltip, () -> addInformationAfterShift(stack, tooltip));
                }
            }

            @OnlyIn(Dist.CLIENT)
            public void addInformationAfterShift(ItemStack stack, List<Component> tooltip){
                tooltip.add(new TranslatableComponent(stack.getItem().getDescriptionId()+".tooltip").withStyle(ChatFormatting.GRAY));
            }

        });
        return ret;
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> block, CreativeModeTab group, Item.Properties properties){
        RegistryObject<T> ret = register_noItem(name, block);
        ITEMS.register(name, () -> new BlockItem(ret.get(), properties));
        return ret;
    }

    private static <T extends Block> RegistryObject<T> registerAnimatedMachines(String name, Supplier<T> block, CreativeModeTab group, Item.Properties properties){
        RegistryObject<T> ret = register_noItem(name, block);
        ITEMS.register(name, () -> new RecruitBeaconItem(ret.get(), properties.tab(group), true));
        return ret;
    }

    private static RegistryObject<Block> registerMetalOre_Stone(String materialName){
        return registerMetalOre("ore_stone_"+materialName, materialName);
    }

    private static RegistryObject<Block> registerMetalOre(String registryName, String materialName){
        RegistryObject<Block> ret = register_noItem(registryName, () -> new PAOreBlock(materialName));
        ITEMS.register(registryName, () -> new PAOreBlockItem(ret.get(), materialName));
        return ret;
    }

    public static ToIntFunction<BlockState> getLightValueLit(int lightValue) {
        return (state) -> {
            return state.getValue(AbstractMachineBlock.ACTIVE) ? lightValue : 0;
        };
    }

    private static <T extends Recipe<?>> RegistryObject<RecipeSerializer<T>> registerRecipes(String name, Supplier<RecipeSerializer<T>> serializer){
        return RECIPE_SERIALIZERS.register(name, serializer);
    }

    private static <T extends Recipe<?>> RegistryObject<RecipeSerializer<T>> register_special_recipe(String name, Supplier<RecipeSerializer<T>> serializer){
        return RECIPE_SERIALIZERS.register(name, serializer);
    }

    private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register_block_entities(String name, BlockEntityType.BlockEntitySupplier<T> factory, RegistryObject<? extends Block> block){

        //About Mojang's Data Fixer. Afaik Mod can't even use it. and its annotated to non null. KEKW
        //noinspection ConstantConditions
        return TILE_ENTITY.register(name, () -> BlockEntityType.Builder.of(factory, block.get()).build(null));
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

        event.registerBlockEntityRenderer(METAL_PRESS_BLOCK_ENTITY.get(), MachineMetalPressRenderer::new);
        event.registerBlockEntityRenderer(DRYDOCK_BLOCK_ENTITY.get(), DrydockControllerRenderer::new);
        event.registerBlockEntityRenderer(RECRUIT_BEACON_BLOCK_ENTITY.get(), MachineRecruitBeaconRenderer::new);

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
