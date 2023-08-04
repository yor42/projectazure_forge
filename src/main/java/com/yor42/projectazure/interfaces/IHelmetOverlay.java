package com.yor42.projectazure.interfaces;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IHelmetOverlay {
    @OnlyIn(Dist.CLIENT)
    ResourceLocation getOverlayTexture(ItemStack stack, LocalPlayer player);
}
