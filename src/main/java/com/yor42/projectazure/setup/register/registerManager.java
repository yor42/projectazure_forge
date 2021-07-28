package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.gameobject.containers.entity.ContainerAKNInventory;
import com.yor42.projectazure.gameobject.containers.entity.ContainerBAInventory;
import com.yor42.projectazure.gameobject.containers.entity.ContainerKansenInventory;
import com.yor42.projectazure.gameobject.containers.machine.ContainerAlloyFurnace;
import com.yor42.projectazure.gameobject.containers.machine.ContainerDryDock;
import com.yor42.projectazure.gameobject.containers.machine.ContainerMetalPress;
import com.yor42.projectazure.gameobject.containers.machine.ContainerRecruitBeacon;
import com.yor42.projectazure.gameobject.containers.riggingcontainer.RiggingContainer;
import com.yor42.projectazure.gameobject.entity.companion.gunusers.bluearchive.EntityShiroko;
import com.yor42.projectazure.gameobject.entity.companion.kansen.*;
import com.yor42.projectazure.gameobject.entity.companion.sworduser.EntityChen;
import com.yor42.projectazure.gameobject.entity.misc.EntityF4fWildcat;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityCannonPelllet;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityProjectileBullet;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityProjectileTorpedo;
import com.yor42.projectazure.libs.defined;
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

    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, defined.MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, defined.MODID);
    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, defined.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, defined.MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, defined.MODID);
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, defined.MODID);
    public static final DeferredRegister<ContainerType<?>> CONTAINER = DeferredRegister.create(ForgeRegistries.CONTAINERS, defined.MODID);
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, defined.MODID);

    //Container
    private static final ContainerType<ContainerKansenInventory> SHIP_INVENTORY = new ContainerType<>(ContainerKansenInventory::new);
    public static final RegistryObject<ContainerType<ContainerKansenInventory>> SHIP_CONTAINER = CONTAINER.register("kansen_inventory", () -> SHIP_INVENTORY);

    private static final ContainerType<RiggingContainer> RIGGING_INVENTORY_TYPE = new ContainerType<>(RiggingContainer::new);
    public static final RegistryObject<ContainerType<RiggingContainer>> RIGGING_INVENTORY = CONTAINER.register("rigging_inventory", () -> RIGGING_INVENTORY_TYPE);

    public static final ContainerType<ContainerBAInventory> BA_INVENTORY_TYPE = new ContainerType<>(ContainerBAInventory::new);
    public static final RegistryObject<ContainerType<ContainerBAInventory>> BA_CONTAINER = CONTAINER.register("bluearchive_inventory", () -> BA_INVENTORY_TYPE);

    public static final ContainerType<ContainerAKNInventory> AKN_INVENTORY_TYPE = new ContainerType<>(ContainerAKNInventory::new);
    public static final RegistryObject<ContainerType<ContainerAKNInventory>> AKN_CONTAINER = CONTAINER.register("arknights_inventory", () -> AKN_INVENTORY_TYPE);

    public static final ContainerType<ContainerMetalPress> METAL_PRESS_CONTAINER_TYPE = IForgeContainerType.create(ContainerMetalPress::new);
    public static final RegistryObject<ContainerType<ContainerMetalPress>> METAL_PRESS_CONTAINER = CONTAINER.register("metal_press_container", () -> METAL_PRESS_CONTAINER_TYPE);

    public static final ContainerType<ContainerAlloyFurnace> CONTAINER_ALLOY_FURNACE_CONTAINER_TYPE = IForgeContainerType.create(ContainerAlloyFurnace::new);
    public static final RegistryObject<ContainerType<ContainerAlloyFurnace>> ALLOY_FURNACE_CONTAINER = CONTAINER.register("alloy_furnace_container", () -> CONTAINER_ALLOY_FURNACE_CONTAINER_TYPE);

    public static final ContainerType<ContainerRecruitBeacon> RECRUIT_BEACON_CONTAINER_TYPE = IForgeContainerType.create(ContainerRecruitBeacon::new);
    public static final RegistryObject<ContainerType<ContainerRecruitBeacon>> RECRUIT_BEACON_CONTAINER = CONTAINER.register("recruit_beacon_container", () -> RECRUIT_BEACON_CONTAINER_TYPE);

    public static final ContainerType<ContainerDryDock> DRYDOCK_CONTAINER_TYPE = IForgeContainerType.create(ContainerDryDock::new);
    public static final RegistryObject<ContainerType<ContainerDryDock>> DRYDOCK_CONTAINER = CONTAINER.register("drydock_container", () -> DRYDOCK_CONTAINER_TYPE);

    //entity
    public static final EntityType<EntityAyanami> ENTITY_AYANAMI = EntityType.Builder.create(EntityAyanami::new, EntityClassification.CREATURE).size(0.572F, 1.525F).build(ModResourceLocation("entityayanami").toString());
    public static final RegistryObject<EntityType<EntityAyanami>> AYANAMI = ENTITIES.register("entityayanami", () -> ENTITY_AYANAMI);

    public static final EntityType<EntityJavelin> ENTITY_JAVELIN = EntityType.Builder.create(EntityJavelin::new, EntityClassification.CREATURE).size(0.572F, 1.525F).build(ModResourceLocation("entityayanami").toString());
    public static final RegistryObject<EntityType<EntityJavelin>> JAVELIN = ENTITIES.register("entityjavelin", () -> ENTITY_JAVELIN);

    public static final EntityType<EntityGangwon> ENTITYGANGWON = EntityType.Builder.create(EntityGangwon::new, EntityClassification.CREATURE).size(0.572F, 1.35F).build(ModResourceLocation("entitygandwon").toString());
    public static final RegistryObject<EntityType<EntityGangwon>> GANGWON = ENTITIES.register("entitygangwon", () -> ENTITYGANGWON);

    public static final EntityType<EntityEnterprise> ENTERPRISE_ENTITY_TYPE = EntityType.Builder.create(EntityEnterprise::new, EntityClassification.CREATURE).size(1F, 1.825F).build(ModResourceLocation("entityenterprise").toString());
    public static final RegistryObject<EntityType<EntityEnterprise>> ENTERPRISE = ENTITIES.register("entityenterprise", () -> ENTERPRISE_ENTITY_TYPE);

    public static final EntityType<EntityShiroko> SHIROKO_ENTITY_TYPE = EntityType.Builder.create(EntityShiroko::new, EntityClassification.CREATURE).size(0.572F, 1.575F).build(ModResourceLocation("entityshiroko").toString());
    public static final RegistryObject<EntityType<EntityShiroko>> SHIROKO = ENTITIES.register("entityshiroko", () -> SHIROKO_ENTITY_TYPE);

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

    //Biomes
    public static final RegistryObject<Biome> MIRROR_SEA = registerManager.BIOMES.register("mirror_sea", registerBiomes::makeMirrorSea);


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
