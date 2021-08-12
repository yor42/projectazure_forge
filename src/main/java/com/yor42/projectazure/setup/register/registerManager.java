package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.gameobject.containers.entity.ContainerAKNInventory;
import com.yor42.projectazure.gameobject.containers.entity.ContainerBAInventory;
import com.yor42.projectazure.gameobject.containers.entity.ContainerGFLInventory;
import com.yor42.projectazure.gameobject.containers.entity.ContainerKansenInventory;
import com.yor42.projectazure.gameobject.containers.machine.ContainerAlloyFurnace;
import com.yor42.projectazure.gameobject.containers.machine.ContainerDryDock;
import com.yor42.projectazure.gameobject.containers.machine.ContainerMetalPress;
import com.yor42.projectazure.gameobject.containers.machine.ContainerRecruitBeacon;
import com.yor42.projectazure.gameobject.containers.riggingcontainer.RiggingContainer;
import com.yor42.projectazure.gameobject.entity.companion.gunusers.EntityM4A1;
import com.yor42.projectazure.gameobject.entity.companion.gunusers.bluearchive.EntityShiroko;
import com.yor42.projectazure.gameobject.entity.companion.kansen.*;
import com.yor42.projectazure.gameobject.entity.companion.sworduser.EntityChen;
import com.yor42.projectazure.gameobject.entity.misc.EntityF4fWildcat;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityCannonPelllet;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityProjectileBullet;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityProjectileTorpedo;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.potion.Effect;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;


public class registerManager {

    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, Constants.MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MODID);
    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, Constants.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, Constants.MODID);
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Constants.MODID);
    public static final DeferredRegister<ContainerType<?>> CONTAINER = DeferredRegister.create(ForgeRegistries.CONTAINERS, Constants.MODID);
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Constants.MODID);

    //Container
    private static final ContainerType<ContainerKansenInventory> SHIP_INVENTORY = new ContainerType<>(ContainerKansenInventory::new);
    public static final RegistryObject<ContainerType<ContainerKansenInventory>> SHIP_CONTAINER = CONTAINER.register("kansen_inventory", () -> SHIP_INVENTORY);

    private static final ContainerType<RiggingContainer> RIGGING_INVENTORY_TYPE = new ContainerType<>(RiggingContainer::new);
    public static final RegistryObject<ContainerType<RiggingContainer>> RIGGING_INVENTORY = CONTAINER.register("rigging_inventory", () -> RIGGING_INVENTORY_TYPE);

    public static final ContainerType<ContainerBAInventory> BA_INVENTORY_TYPE = new ContainerType<>(ContainerBAInventory::new);
    public static final RegistryObject<ContainerType<ContainerBAInventory>> BA_CONTAINER = CONTAINER.register("bluearchive_inventory", () -> BA_INVENTORY_TYPE);

    public static final ContainerType<ContainerAKNInventory> AKN_INVENTORY_TYPE = new ContainerType<>(ContainerAKNInventory::new);
    public static final RegistryObject<ContainerType<ContainerAKNInventory>> AKN_CONTAINER = CONTAINER.register("arknights_inventory", () -> AKN_INVENTORY_TYPE);

    public static final ContainerType<ContainerGFLInventory> GFL_INVENTORY_TYPE = new ContainerType<>(ContainerGFLInventory::new);
    public static final RegistryObject<ContainerType<ContainerGFLInventory>> GFL_CONTAINER = CONTAINER.register("girlsfrontline_inventory", () -> GFL_INVENTORY_TYPE);


    public static final ContainerType<ContainerMetalPress> METAL_PRESS_CONTAINER_TYPE = IForgeContainerType.create(ContainerMetalPress::new);
    public static final RegistryObject<ContainerType<ContainerMetalPress>> METAL_PRESS_CONTAINER = CONTAINER.register("metal_press_container", () -> METAL_PRESS_CONTAINER_TYPE);

    public static final ContainerType<ContainerAlloyFurnace> CONTAINER_ALLOY_FURNACE_CONTAINER_TYPE = IForgeContainerType.create(ContainerAlloyFurnace::new);
    public static final RegistryObject<ContainerType<ContainerAlloyFurnace>> ALLOY_FURNACE_CONTAINER = CONTAINER.register("alloy_furnace_container", () -> CONTAINER_ALLOY_FURNACE_CONTAINER_TYPE);

    public static final ContainerType<ContainerRecruitBeacon> RECRUIT_BEACON_CONTAINER_TYPE = IForgeContainerType.create(ContainerRecruitBeacon::new);
    public static final RegistryObject<ContainerType<ContainerRecruitBeacon>> RECRUIT_BEACON_CONTAINER = CONTAINER.register("recruit_beacon_container", () -> RECRUIT_BEACON_CONTAINER_TYPE);

    public static final ContainerType<ContainerDryDock> DRYDOCK_CONTAINER_TYPE = IForgeContainerType.create(ContainerDryDock::new);
    public static final RegistryObject<ContainerType<ContainerDryDock>> DRYDOCK_CONTAINER = CONTAINER.register("drydock_container", () -> DRYDOCK_CONTAINER_TYPE);

    //entity
    public static final EntityType<EntityAyanami> ENTITYTYPE_AYANAMI = EntityType.Builder.create(EntityAyanami::new, EntityClassification.CREATURE).size(0.572F, 1.525F).build(ModResourceLocation("entityayanami").toString());
    public static final RegistryObject<EntityType<EntityAyanami>> AYANAMI = ENTITIES.register("entityayanami", () -> ENTITYTYPE_AYANAMI);

    public static final EntityType<EntityJavelin> ENTITYTYPE_JAVELIN = EntityType.Builder.create(EntityJavelin::new, EntityClassification.CREATURE).size(0.572F, 1.525F).build(ModResourceLocation("entityayanami").toString());
    public static final RegistryObject<EntityType<EntityJavelin>> JAVELIN = ENTITIES.register("entityjavelin", () -> ENTITYTYPE_JAVELIN);

    public static final EntityType<EntityZ23> ENTITYTYPE_Z23 = EntityType.Builder.create(EntityZ23::new, EntityClassification.CREATURE).size(0.572F, 1.525F).build(ModResourceLocation("entityz23").toString());
    public static final RegistryObject<EntityType<EntityZ23>> Z23 = ENTITIES.register("entityz23", () -> ENTITYTYPE_Z23);

    public static final EntityType<EntityGangwon> ENTITYTYPE_GANGWON = EntityType.Builder.create(EntityGangwon::new, EntityClassification.CREATURE).size(0.572F, 1.35F).build(ModResourceLocation("entitygandwon").toString());
    public static final RegistryObject<EntityType<EntityGangwon>> GANGWON = ENTITIES.register("entitygangwon", () -> ENTITYTYPE_GANGWON);

    public static final EntityType<EntityEnterprise> ENTITYTYPE_ENTERPRISE = EntityType.Builder.create(EntityEnterprise::new, EntityClassification.CREATURE).size(1F, 1.825F).build(ModResourceLocation("entityenterprise").toString());
    public static final RegistryObject<EntityType<EntityEnterprise>> ENTERPRISE = ENTITIES.register("entityenterprise", () -> ENTITYTYPE_ENTERPRISE);

    public static final EntityType<EntityM4A1> ENTITYTYPE_M4A1 = EntityType.Builder.create(EntityM4A1::new, EntityClassification.CREATURE).size(1F, 1.825F).build(ModResourceLocation("entitym4a1").toString());
    public static final RegistryObject<EntityType<EntityM4A1>> M4A1 = ENTITIES.register("entitym4a1", () -> ENTITYTYPE_M4A1);

    public static final EntityType<EntityShiroko> ENTITYTYPE_SHIROKO = EntityType.Builder.create(EntityShiroko::new, EntityClassification.CREATURE).size(0.572F, 1.575F).build(ModResourceLocation("entityshiroko").toString());
    public static final RegistryObject<EntityType<EntityShiroko>> SHIROKO = ENTITIES.register("entityshiroko", () -> ENTITYTYPE_SHIROKO);

    public static final EntityType<EntityNagato> ENTITYTYPE_NAGATO = EntityType.Builder.create(EntityNagato::new, EntityClassification.CREATURE).size(0.572F, 1.32F).build(ModResourceLocation("entitynagato").toString());
    public static final RegistryObject<EntityType<EntityNagato>> NAGATO = ENTITIES.register("entitynagato", () -> ENTITYTYPE_NAGATO);

    public static final EntityType<EntityChen> ENTITYTYPE_CHEN = EntityType.Builder.create(EntityChen::new, EntityClassification.CREATURE).size(0.572F, 1.68F).build(ModResourceLocation("entitychen").toString());
    public static final RegistryObject<EntityType<EntityChen>> CHEN = ENTITIES.register("entitychen", () -> ENTITYTYPE_CHEN);

    //projectile
    public static final EntityType<EntityCannonPelllet> PROJECTILECANNONSHELL = EntityType.Builder.<EntityCannonPelllet>create(EntityCannonPelllet::new, EntityClassification.MISC).size(0.5F, 0.5F).build(ModResourceLocation("projectilecannonshell").toString());
    public static final RegistryObject<EntityType<EntityCannonPelllet>> CANNONSHELL = ENTITIES.register("entitycannonshell", () -> PROJECTILECANNONSHELL);

    public static final EntityType<EntityProjectileTorpedo> PROJECTILETORPEDO = EntityType.Builder.<EntityProjectileTorpedo>create(EntityProjectileTorpedo::new, EntityClassification.MISC).size(0.5F, 0.5F).build(ModResourceLocation("projectiletorpedo").toString());
    public static final RegistryObject<EntityType<EntityProjectileTorpedo>> TORPEDO = ENTITIES.register("entitytorpedo", () -> PROJECTILETORPEDO);

    public static final EntityType<EntityProjectileBullet> PROJECTILE_GUN_BULLET = EntityType.Builder.<EntityProjectileBullet>create(EntityProjectileBullet::new, EntityClassification.MISC).size(0.2F, 0.2F).build(ModResourceLocation("projectilebullet").toString());
    public static final RegistryObject<EntityType<EntityProjectileBullet>> GUN_BULLET = ENTITIES.register("entitybullet", () -> PROJECTILE_GUN_BULLET);

    //Planes
    public static final EntityType<EntityF4fWildcat> PLANEF4FWildCat = EntityType.Builder.<EntityF4fWildcat>create(EntityF4fWildcat::new, EntityClassification.MISC).size(0.5F, 0.5F).build(ModResourceLocation("entityplanef4fwildcat").toString());
    public static final RegistryObject<EntityType<EntityF4fWildcat>> F4FWildCat = ENTITIES.register("planef4fwildcat", () -> PLANEF4FWildCat);

    public static void register() {
        IEventBus eventbus = FMLJavaModLoadingContext.get().getModEventBus();
        ENTITIES.register(eventbus);
        BIOMES.register(eventbus);
        BLOCKS.register(eventbus);
        ITEMS.register(eventbus);
        CONTAINER.register(eventbus);
        TILE_ENTITY.register(eventbus);
        RECIPE_SERIALIZERS.register(eventbus);
        EFFECTS.register(eventbus);
        registerBlocks.register();
        registerBiomes.register();
        registerItems.register();
        registerTE.register();
        registerRecipes.Serializers.register();
        registerRecipes.Types.register();
        RegisterPotionEffects.register();
    }


}
