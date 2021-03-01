package com.yor42.projectazure.libs.utils;

import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityKansenBase;
import com.yor42.projectazure.libs.enums;
import com.yor42.projectazure.network.proxy.ClientProxy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.hasPlanes;

public class EntityUtils {

    public static boolean EntityHasPlanes(EntityKansenBase entity){
        return hasPlanes(entity.getRigging());
    }

}
