package com.yor42.projectazure.intermod;

import com.yor42.solarapocalypse.utils.MathUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.fml.ModList;

public class SolarApocalypse {

    public static boolean isSolarApocalypseLoaded(){
        return ModList.get().isLoaded("solarapocalypse");
    }


    //Compatibility layer for Solar Apocalypse
    public static boolean isSunlightDangerous(ServerLevel world){
        if(isSolarApocalypseLoaded()){
            return shouldEntityBurnInDaylight(world);
        }
        return false;
    }

    //Make another method to prevent NoSuchMethod Exception
    private static boolean shouldEntityBurnInDaylight(ServerLevel world){
        return MathUtils.shouldExcuteStage(world, MathUtils.STAGE.STAGE_3);
    }


}
