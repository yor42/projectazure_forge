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
        event.put(Main.AYANAMI.get(), EntityAyanami.MutableAttribute().build());
        event.put(Main.JAVELIN.get(), EntityJavelin.MutableAttribute().build());
        event.put(Main.GANGWON.get(), EntityGangwon.MutableAttribute().build());
        event.put(Main.ENTERPRISE.get(), EntityEnterprise.MutableAttribute().build());
        event.put(Main.F4FWildCat.get(), EntityF4fWildcat.MutableAttribute().build());
        event.put(Main.MISSILEDRONE.get(), EntityMissileDrone.MutableAttribute().build());
        event.put(Main.SHIROKO.get(), EntityShiroko.MutableAttribute().build());
        event.put(Main.NAGATO.get(), EntityNagato.MutableAttribute().build());
        event.put(Main.CHEN.get(), EntityChen.MutableAttribute().build());
        event.put(Main.ROSMONTIS.get(), EntityRosmontis.MutableAttribute().build());
        event.put(Main.TALULAH.get(), EntityTalulah.MutableAttribute().build());
        event.put(Main.Z23.get(), EntityZ23.MutableAttribute().build());
        event.put(Main.LAFFEY.get(), EntityLaffey.MutableAttribute().build());
        event.put(Main.M4A1.get(), EntityM4A1.MutableAttribute().build());
        event.put(Main.AMIYA.get(), EntityAmiya.MutableAttribute().build());
        event.put(Main.MUDROCK.get(), EntityMudrock.MutableAttribute().build());
        event.put(Main.TEXAS.get(), EntityTexas.MutableAttribute().build());
        event.put(Main.FROSTNOVA.get(), EntityFrostnova.MutableAttribute().build());
        event.put(Main.LAPPLAND.get(), EntityLappland.MutableAttribute().build());
        event.put(Main.SIEGE.get(), EntitySiege.MutableAttribute().build());
        event.put(Main.SCHWARZ.get(), EntitySchwarz.MutableAttribute().build());
        event.put(Main.SYLVI.get(), EntitySylvi.MutableAttribute().build());
        event.put(Main.YAMATO.get(), EntityYamato.MutableAttribute().build());
        event.put(Main.CLAYMORE.get(), EntityClaymore.MutableAttribute().build());
    }


}
