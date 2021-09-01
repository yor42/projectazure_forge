package com.yor42.projectazure.events;

import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.libs.GunRecoilUtil;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid= Constants.MODID, value = Dist.CLIENT)
public class ModBusEventHandlerClient {
    public static void setup() {
        MinecraftForge.EVENT_BUS.register(GunRecoilUtil.getInstance());
    }
}
