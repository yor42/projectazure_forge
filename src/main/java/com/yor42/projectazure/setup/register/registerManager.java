package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.gameobject.entity.EntityAyanami;
import com.yor42.projectazure.libs.defined;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
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
    public static final EntityType<EntityAyanami> ENTITYAYANAMI = EntityType.Builder.<EntityAyanami>create(EntityAyanami::new, EntityClassification.CREATURE).build(new ResourceLocation(defined.MODID, "entityayanami").toString());
    public static final RegistryObject<EntityType<EntityAyanami>> AYANAMI = ENTITIES.register("entityayanami", () -> ENTITYAYANAMI);

    public static void register() {
        IEventBus eventbus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(eventbus);
        ITEMS.register(eventbus);
        ENTITIES.register(eventbus);
        registerBlocks.register();
        registerItems.register();
    }

}
