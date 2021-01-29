package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.gameobject.containers.ContainerKansenInventory;
import com.yor42.projectazure.gameobject.containers.riggingcontainer.RiggingContainerDDDefault;
import com.yor42.projectazure.gameobject.entity.EntityAyanami;
import com.yor42.projectazure.libs.defined;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class registerManager {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, defined.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, defined.MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, defined.MODID);


    //container
    public static final DeferredRegister<ContainerType<?>> CONTAINER = DeferredRegister.create(ForgeRegistries.CONTAINERS, defined.MODID);
    private static final ContainerType<ContainerKansenInventory> SHIP_INVENTORY = new ContainerType<ContainerKansenInventory>(ContainerKansenInventory::new);
    public static final RegistryObject<ContainerType<ContainerKansenInventory>> SHIP_CONTAINER = CONTAINER.register("kansen_inventory", () -> SHIP_INVENTORY);

    private static final ContainerType<RiggingContainerDDDefault> RIGGING_INVENTORY_DD_DEFAULT = new ContainerType<RiggingContainerDDDefault>(RiggingContainerDDDefault::new);
    public static final RegistryObject<ContainerType<RiggingContainerDDDefault>> DD_DEFAULT_RIGGING_INVENTORY = CONTAINER.register("defaultDD_Rigging_inventory", () -> RIGGING_INVENTORY_DD_DEFAULT);

    //entity
    public static final EntityType<EntityAyanami> ENTITYAYANAMI = EntityType.Builder.<EntityAyanami>create(EntityAyanami::new, EntityClassification.CREATURE).size(0.572F, 1.5F).build(new ResourceLocation(defined.MODID, "entityayanami").toString());
    public static final RegistryObject<EntityType<EntityAyanami>> AYANAMI = ENTITIES.register("entityayanami", () -> ENTITYAYANAMI);

    public static void register() {
        IEventBus eventbus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(eventbus);
        ITEMS.register(eventbus);
        ENTITIES.register(eventbus);
        CONTAINER.register(eventbus);
        registerBlocks.register();
        registerItems.register();
    }


}
