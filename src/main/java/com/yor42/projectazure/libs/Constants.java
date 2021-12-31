package com.yor42.projectazure.libs;

import com.yor42.projectazure.setup.register.registerManager;
import net.minecraft.entity.EntityType;

public class Constants {

    public static final String MODID = "projectazure";

    //Starter ID
    public static final EntityType<?>[] StarterList = {
            registerManager.AYANAMI.get(),
            registerManager.JAVELIN.get(),
            registerManager.Z23.get(),
            registerManager.LAFFEY.get()
    };

    public static final int CRYSTAL_CHAMBER_SOLUTION_TANK_CAPACITY = 4000;

}
