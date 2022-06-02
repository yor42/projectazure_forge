package com.yor42.projectazure.events;

import com.yor42.projectazure.libs.Constants;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid= Constants.MODID, value = Dist.CLIENT)
public class ModBusEventHandlerClient {
    public static void setup() {
    }
}
