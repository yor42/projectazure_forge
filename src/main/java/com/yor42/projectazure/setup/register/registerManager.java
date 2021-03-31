package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.gameobject.containers.ContainerBAInventory;
import com.yor42.projectazure.gameobject.containers.ContainerKansenInventory;
import com.yor42.projectazure.gameobject.containers.riggingcontainer.RiggingContainer;
import com.yor42.projectazure.gameobject.entity.companion.gunusers.EntityShiroko;
import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityAyanami;
import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityEnterprise;
import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityGangwon;
import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityNagato;
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
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;


public class registerManager {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, defined.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, defined.MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, defined.MODID);


    //container
    public static final DeferredRegister<ContainerType<?>> CONTAINER = DeferredRegister.create(ForgeRegistries.CONTAINERS, defined.MODID);

    private static final ContainerType<ContainerKansenInventory> SHIP_INVENTORY = new ContainerType<ContainerKansenInventory>(ContainerKansenInventory::new);
    public static final RegistryObject<ContainerType<ContainerKansenInventory>> SHIP_CONTAINER = CONTAINER.register("kansen_inventory", () -> SHIP_INVENTORY);

    private static final ContainerType<RiggingContainer> RIGGING_INVENTORY_TYPE = new ContainerType<RiggingContainer>(RiggingContainer::new);
    public static final RegistryObject<ContainerType<RiggingContainer>> RIGGING_INVENTORY = CONTAINER.register("rigging_inventory", () -> RIGGING_INVENTORY_TYPE);

    public static final ContainerType<ContainerBAInventory> BA_INVENTORY_TYPE = new ContainerType<ContainerBAInventory>(ContainerBAInventory::new);
    public static final RegistryObject<ContainerType<ContainerBAInventory>> BA_CONTAINER = CONTAINER.register("bluearchive_inventory", () -> BA_INVENTORY_TYPE);

    //entity
    public static final EntityType<EntityAyanami> ENTITYAYANAMI = EntityType.Builder.create(EntityAyanami::new, EntityClassification.CREATURE).size(1F, 1.5F).build(ModResourceLocation("entityayanami").toString());
    public static final RegistryObject<EntityType<EntityAyanami>> AYANAMI = ENTITIES.register("entityayanami", () -> ENTITYAYANAMI);

    public static final EntityType<EntityGangwon> ENTITYGANGWON = EntityType.Builder.create(EntityGangwon::new, EntityClassification.CREATURE).size(0.572F, 1.5F).build(ModResourceLocation("entitygandwon").toString());
    public static final RegistryObject<EntityType<EntityGangwon>> GANGWON = ENTITIES.register("entitygangwon", () -> ENTITYGANGWON);

    public static final EntityType<EntityEnterprise> ENTERPRISE_ENTITY_TYPE = EntityType.Builder.create(EntityEnterprise::new, EntityClassification.CREATURE).size(1F, 1.8F).build(ModResourceLocation("entityenterprise").toString());
    public static final RegistryObject<EntityType<EntityEnterprise>> ENTERPRISE = ENTITIES.register("entityenterprise", () -> ENTERPRISE_ENTITY_TYPE);

    public static final EntityType<EntityShiroko> SHIROKO_ENTITY_TYPE = EntityType.Builder.create(EntityShiroko::new, EntityClassification.CREATURE).size(1F, 1.8F).build(ModResourceLocation("entityshiroko").toString());
    public static final RegistryObject<EntityType<EntityShiroko>> SHIROKO = ENTITIES.register("entityshiroko", () -> SHIROKO_ENTITY_TYPE);

    public static final EntityType<EntityNagato> ENTITYTYPE_NAGATO = EntityType.Builder.create(EntityNagato::new, EntityClassification.CREATURE).size(0.572F, 1.5F).build(ModResourceLocation("entitynagato").toString());
    public static final RegistryObject<EntityType<EntityNagato>> NAGATO = ENTITIES.register("entitynagato", () -> ENTITYTYPE_NAGATO);

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
        BLOCKS.register(eventbus);
        ITEMS.register(eventbus);
        CONTAINER.register(eventbus);
        registerBlocks.register();
        registerItems.register();
    }


}
