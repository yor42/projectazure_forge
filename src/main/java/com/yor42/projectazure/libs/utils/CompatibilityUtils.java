package com.yor42.projectazure.libs.utils;

import net.minecraftforge.fml.ModList;

public class CompatibilityUtils {
    public static final String CURIOS_MODID = "curios";
    public static final String TCONSTRUCT_MODID = "tconstruct";

    public static boolean isCurioLoaded(){
        return ModList.get().isLoaded(CURIOS_MODID);
    }

    public static boolean isTConLoaded(){
        return ModList.get().isLoaded(TCONSTRUCT_MODID);
    }
}
