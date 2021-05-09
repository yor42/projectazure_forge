package com.yor42.projectazure.libs.utils;

import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityKansenBase;

import static com.yor42.projectazure.libs.utils.ItemStackUtils.hasPlanes;

public class EntityUtils {

    public static boolean EntityHasPlanes(EntityKansenBase entity){
        return hasPlanes(entity.getRigging());
    }

}
