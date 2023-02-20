package com.yor42.projectazure.intermod.curios.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface ICurioRenderer {
    void render(ItemStack stack, LivingEntity entity, MatrixStack posestack, IRenderTypeBuffer buffer, int light, float partialTicks);
}
