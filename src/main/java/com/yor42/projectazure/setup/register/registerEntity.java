package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.gameobject.entity.EntityAyanami;
import com.yor42.projectazure.libs.defined;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

//@Mod.EventBusSubscriber(modid = defined.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class registerEntity {

    //kansen


    public static void RegisterAttributes(){
        GlobalEntityTypeAttributes.put(registerManager.ENTITYAYANAMI, EntityAyanami.MutableAttribute().create());

    }

/*
    private static final EntityType<EntityAyanami> ENTITY_AYANAMI_ENTITY_TYPE = EntityType.Builder.create(EntityAyanami::new, EntityClassification.CREATURE).size(0.5F, 0.5F).build(new ResourceLocation(defined.MODID, "entityayanami").toString());
    public static final RegistryObject<EntityType<EntityAyanami>> ENTITYAYANAMI = registerManager.ENTITIES.register("entityayanami", () -> ENTITY_AYANAMI_ENTITY_TYPE);


    @SubscribeEvent
    public static void init(RegistryEvent.Register<EntityType<?>> evt) {
        IForgeRegistry<EntityType<?>> r = evt.getRegistry();
        r.register(EntityType.Builder.create(EntityAyanami::new, EntityClassification.CREATURE)
                .setTrackingRange(256)
                .setUpdateInterval(10)
                .build("").setRegistryName(defined.MODID, "entityayanami"));
    }

 */


}
