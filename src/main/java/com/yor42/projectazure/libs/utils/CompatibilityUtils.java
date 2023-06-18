package com.yor42.projectazure.libs.utils;

import net.minecraftforge.fml.ModList;

public class CompatibilityUtils {
    public static final String CURIOS_MODID = "curios";
    public static final String TCONSTRUCT_MODID = "tconstruct";
    public static final String PATCHOULI_MODID = "patchouli";
    public static final String MULTIBLOCKED_MODID = "multiblocked";

    public static boolean isCurioLoaded(){
        return ModList.get().isLoaded(CURIOS_MODID);
    }

    public static boolean isPatchouliLoaded(){
        return ModList.get().isLoaded(PATCHOULI_MODID);
    }

    public static boolean isTConLoaded(){
        return ModList.get().isLoaded(TCONSTRUCT_MODID);
    }

    public static boolean isMultiblockedLoaded(){
        return ModList.get().isLoaded(MULTIBLOCKED_MODID);
    }
}
