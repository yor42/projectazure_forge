package com.yor42.projectazure.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;
import com.yor42.projectazure.interfaces.ISecondaryBar;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {


    @Inject(method = "renderGuiItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V", at = @At("TAIL"))
    public void onrenderGuiItemDecorations(Font pFr, ItemStack pStack, int pXPosition, int pYPosition, String pText, CallbackInfo ci) {

        if (!pStack.isEmpty() && pStack.getItem() instanceof ISecondaryBar bar && bar.isSecondaryBarVisible(pStack)) {
            RenderSystem.disableDepthTest();
            RenderSystem.disableTexture();
            RenderSystem.disableBlend();
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder bufferbuilder = tesselator.getBuilder();
            int i = bar.getSecondaryBarWidth(pStack);
            int j = bar.getSecondaryBarColor(pStack);
            this.fillRect(bufferbuilder, pXPosition + 2, pYPosition + 14, 13, 2, 0, 0, 0, 255);
            this.fillRect(bufferbuilder, pXPosition + 2, pYPosition + 14, i, 1, j >> 16 & 255, j >> 8 & 255, j & 255, 255);
            RenderSystem.enableBlend();
            RenderSystem.enableTexture();
            RenderSystem.enableDepthTest();
        }

    }

    @Shadow
    private void fillRect(BufferBuilder bufferbuilder, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
    }

}
