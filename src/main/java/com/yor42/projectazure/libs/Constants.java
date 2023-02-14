package com.yor42.projectazure.libs;

import com.yor42.projectazure.setup.register.registerEntity;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.ModList;

public class Constants {

    public static final String MODID = "projectazure";
    public static final String CURIOS_MODID = "curios";
    public static final String TCONSTRUCT_MODID = "tconstruct";
    
    public static boolean isCurioLoaded(){
        return ModList.get().isLoaded(CURIOS_MODID);
    }

    public static boolean isTConLoaded(){
        return ModList.get().isLoaded(TCONSTRUCT_MODID);
    }

    //Starter ID
    public static final EntityType<?>[] StarterList = {
            registerEntity.AYANAMI.get(),
            registerEntity.JAVELIN.get(),
            registerEntity.Z23.get(),
            registerEntity.LAFFEY.get()
    };

    public static final int CRYSTAL_CHAMBER_SOLUTION_TANK_CAPACITY = 4000;

}
