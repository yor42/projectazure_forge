package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.gameobject.entity.companion.gunusers.EntityM4A1;
import com.yor42.projectazure.gameobject.entity.companion.gunusers.bluearchive.EntityShiroko;
import com.yor42.projectazure.gameobject.entity.companion.kansen.*;
import com.yor42.projectazure.gameobject.entity.companion.sworduser.EntityChen;
import com.yor42.projectazure.gameobject.entity.misc.EntityF4fWildcat;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class registerEntity {

    //kansen
    public static void RegisterAttributes(){
        GlobalEntityTypeAttributes.put(registerManager.ENTITYTYPE_AYANAMI, EntityAyanami.MutableAttribute().create());
        GlobalEntityTypeAttributes.put(registerManager.ENTITYTYPE_JAVELIN, EntityJavelin.MutableAttribute().create());
        GlobalEntityTypeAttributes.put(registerManager.ENTITYTYPE_GANGWON, EntityGangwon.MutableAttribute().create());
        GlobalEntityTypeAttributes.put(registerManager.ENTITYTYPE_ENTERPRISE, EntityEnterprise.MutableAttribute().create());
        GlobalEntityTypeAttributes.put(registerManager.PLANEF4FWildCat, EntityF4fWildcat.MutableAttribute().create());
        GlobalEntityTypeAttributes.put(registerManager.ENTITYTYPE_SHIROKO, EntityShiroko.MutableAttribute().create());
        GlobalEntityTypeAttributes.put(registerManager.ENTITYTYPE_NAGATO, EntityNagato.MutableAttribute().create());
        GlobalEntityTypeAttributes.put(registerManager.ENTITYTYPE_CHEN, EntityChen.MutableAttribute().create());
        GlobalEntityTypeAttributes.put(registerManager.ENTITYTYPE_Z23, EntityZ23.MutableAttribute().create());
        GlobalEntityTypeAttributes.put(registerManager.ENTITYTYPE_M4A1, EntityM4A1.MutableAttribute().create());
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
