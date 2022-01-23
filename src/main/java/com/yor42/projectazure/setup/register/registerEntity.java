package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.gameobject.entity.companion.bonus.EntityFrostnova;
import com.yor42.projectazure.gameobject.entity.companion.bonus.EntityTalulah;
import com.yor42.projectazure.gameobject.entity.companion.gunusers.EntityM4A1;
import com.yor42.projectazure.gameobject.entity.companion.gunusers.EntityShiroko;
import com.yor42.projectazure.gameobject.entity.companion.magicuser.EntityAmiya;
import com.yor42.projectazure.gameobject.entity.companion.magicuser.EntityRosmontis;
import com.yor42.projectazure.gameobject.entity.companion.ships.*;
import com.yor42.projectazure.gameobject.entity.companion.sworduser.EntityChen;
import com.yor42.projectazure.gameobject.entity.companion.sworduser.EntityMudrock;
import com.yor42.projectazure.gameobject.entity.companion.sworduser.EntityTexas;
import com.yor42.projectazure.gameobject.entity.misc.EntityClaymore;
import com.yor42.projectazure.gameobject.entity.misc.EntityF4fWildcat;
import com.yor42.projectazure.gameobject.entity.misc.EntityMissileDrone;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class registerEntity {

    public static void RegisterAttributes(){
        GlobalEntityTypeAttributes.put(registerManager.ENTITYTYPE_AYANAMI, EntityAyanami.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(registerManager.ENTITYTYPE_JAVELIN, EntityJavelin.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(registerManager.ENTITYTYPE_GANGWON, EntityGangwon.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(registerManager.ENTITYTYPE_ENTERPRISE, EntityEnterprise.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(registerManager.PLANEF4FWildCat, EntityF4fWildcat.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(registerManager.ENTITYTYPE_MISSILEDRONE, EntityMissileDrone.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(registerManager.ENTITYTYPE_SHIROKO, EntityShiroko.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(registerManager.ENTITYTYPE_NAGATO, EntityNagato.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(registerManager.ENTITYTYPE_CHEN, EntityChen.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(registerManager.ENTITYTYPE_ROSMONTIS, EntityRosmontis.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(registerManager.ENTITYTYPE_TALULAH, EntityTalulah.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(registerManager.ENTITYTYPE_Z23, EntityZ23.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(registerManager.ENTITYTYPE_LAFFEY, EntityLaffey.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(registerManager.ENTITYTYPE_M4A1, EntityM4A1.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(registerManager.ENTITYTYPE_AMIYA, EntityAmiya.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(registerManager.ENTITYTYPE_MUDROCK, EntityMudrock.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(registerManager.ENTITYTYPE_TEXAS, EntityTexas.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(registerManager.ENTITYTYPE_FROSTNOVA, EntityFrostnova.MutableAttribute().build());
        GlobalEntityTypeAttributes.put(registerManager.ENTITYTYPE_CLAYMORE, EntityClaymore.MutableAttribute().build());
    }


}
