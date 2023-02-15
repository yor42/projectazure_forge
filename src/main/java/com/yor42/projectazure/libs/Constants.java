package com.yor42.projectazure.libs;

import com.yor42.projectazure.setup.register.registerEntity;
import net.minecraft.entity.EntityType;

public class Constants {

    public static final String MODID = "projectazure";

    //Starter ID
    public static final EntityType<?>[] StarterList = {
            registerEntity.AYANAMI.get(),
            registerEntity.JAVELIN.get(),
            registerEntity.Z23.get(),
            registerEntity.LAFFEY.get()
    };

    public static final int CRYSTAL_CHAMBER_SOLUTION_TANK_CAPACITY = 4000;

}
