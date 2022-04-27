package com.yor42.projectazure.intermod;

import net.minecraftforge.fml.ModList;

public class SolarApocalypse {

    public static boolean isSolarApocalypseLoaded(){
        return ModList.get().isLoaded("solarapocalypse");
    }

/*
    //Compatibility layer for Solar Apocalypse
    public static <ServerWorld> boolean isSunlightDangerous(ServerWorld world){
        if(isSolarApocalypseLoaded()){
            return shouldEntityBurnInDaylight(world);
        }
        return false;
    }

    //Make another method to prevent NoSuchMethod Exception
    private static boolean shouldEntityBurnInDaylight(ServerWorld world){
        return MathUtils.shouldExcuteStage(world, MathUtils.STAGE.STAGE_3);
    }

 */


}
