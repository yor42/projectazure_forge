package com.yor42.projectazure.libs;

import com.yor42.projectazure.Main;
import net.minecraft.world.entity.EntityType;

public class Constants {

    public static final String MODID = "projectazure";

    //Starter ID
    public static final EntityType<?>[] StarterList = {
            Main.AYANAMI.get(),
            Main.JAVELIN.get(),
            Main.Z23.get(),
            Main.LAFFEY.get()
    };

    public static final int CRYSTAL_CHAMBER_SOLUTION_TANK_CAPACITY = 4000;

}
