package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.Main;
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
import com.yor42.projectazure.libs.Constants;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class registerEntity {

    @SubscribeEvent
    public static void RegisterAttributes(EntityAttributeCreationEvent event){
        event.put(Main.ENTITYTYPE_AYANAMI, EntityAyanami.MutableAttribute().build());
        event.put(Main.ENTITYTYPE_JAVELIN, EntityJavelin.MutableAttribute().build());
        event.put(Main.ENTITYTYPE_GANGWON, EntityGangwon.MutableAttribute().build());
        event.put(Main.ENTITYTYPE_ENTERPRISE, EntityEnterprise.MutableAttribute().build());
        event.put(Main.PLANEF4FWildCat, EntityF4fWildcat.MutableAttribute().build());
        event.put(Main.ENTITYTYPE_MISSILEDRONE, EntityMissileDrone.MutableAttribute().build());
        event.put(Main.ENTITYTYPE_SHIROKO, EntityShiroko.MutableAttribute().build());
        event.put(Main.ENTITYTYPE_NAGATO, EntityNagato.MutableAttribute().build());
        event.put(Main.ENTITYTYPE_CHEN, EntityChen.MutableAttribute().build());
        event.put(Main.ENTITYTYPE_ROSMONTIS, EntityRosmontis.MutableAttribute().build());
        event.put(Main.ENTITYTYPE_TALULAH, EntityTalulah.MutableAttribute().build());
        event.put(Main.ENTITYTYPE_Z23, EntityZ23.MutableAttribute().build());
        event.put(Main.ENTITYTYPE_LAFFEY, EntityLaffey.MutableAttribute().build());
        event.put(Main.ENTITYTYPE_M4A1, EntityM4A1.MutableAttribute().build());
        event.put(Main.ENTITYTYPE_AMIYA, EntityAmiya.MutableAttribute().build());
        event.put(Main.ENTITYTYPE_MUDROCK, EntityMudrock.MutableAttribute().build());
        event.put(Main.ENTITYTYPE_TEXAS, EntityTexas.MutableAttribute().build());
        event.put(Main.ENTITYTYPE_FROSTNOVA, EntityFrostnova.MutableAttribute().build());
        event.put(Main.ENTITYTYPE_LAPPLAND, EntityLappland.MutableAttribute().build());
        event.put(Main.ENTITYTYPE_SIEGE, EntitySiege.MutableAttribute().build());
        event.put(Main.ENTITYTYPE_SCHWARZ, EntitySchwarz.MutableAttribute().build());
        event.put(Main.ENTITYTYPE_SYLVI, EntitySylvi.MutableAttribute().build());
        event.put(Main.ENTITYTYPE_YAMATO, EntityYamato.MutableAttribute().build());
        event.put(Main.ENTITYTYPE_CLAYMORE, EntityClaymore.MutableAttribute().build());
    }


}
