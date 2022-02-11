package com.yor42.projectazure.libs.utils;

import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
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
