package com.yor42.projectazure.libs.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ClientUtils {
    @OnlyIn(Dist.CLIENT)
    public static World getClientWorld()
    {
        return Minecraft.getInstance().level;
    }
}
