package com.yor42.projectazure.libs.utils;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.fluids.FluidStack;

/*
 *  BluSunrize
 *  Copyright (c) 2021
 *
 *  This code is licensed under "Blu's License of Common Sense"
 *  Details can be found in the license file in the root folder of this project
 */

public class RenderingUtils {

    protected static final RenderStateShard.TransparencyStateShard TRANSLUCENT_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("translucent_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });
    public static void drawRepeatedFluidSpriteGui(MultiBufferSource buffer, PoseStack stack, FluidStack fluid, float x, float y, float w, float h)
    {
        RenderType renderType = getGui(InventoryMenu.BLOCK_ATLAS);
        VertexConsumer builder = buffer.getBuffer(renderType);
        drawRepeatedFluidSprite(builder, stack, fluid, x, y, w, h);
    }

    public static RenderType getGui(ResourceLocation texture)
    {
        return RenderType.create(
                "gui_"+texture,
                DefaultVertexFormat.POSITION_COLOR_TEX,
                VertexFormat.Mode.QUADS,
                256, false, false,
                RenderType.CompositeState.builder()
                        .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .createCompositeState(false)
        );
    }

    public static void drawRepeatedFluidSprite(VertexConsumer builder, PoseStack transform, FluidStack fluid, float x, float y, float w, float h)
    {
        TextureAtlasSprite sprite = getSprite(fluid.getFluid().getAttributes().getStillTexture(fluid));
        int col = fluid.getFluid().getAttributes().getColor(fluid);
        int iW = sprite.getWidth();
        int iH = sprite.getHeight();
        if(iW > 0&&iH > 0)
            drawRepeatedSprite(builder, transform, x, y, w, h, iW, iH,
                    sprite.getU0(), sprite.getU1(), sprite.getV0(), sprite.getV1(),
                    (col >> 16&255)/255.0f, (col >> 8&255)/255.0f, (col&255)/255.0f, 1);
    }
    public static void drawRepeatedSprite(VertexConsumer builder, PoseStack transform, float x, float y, float w,
                                          float h, int iconWidth, int iconHeight, float uMin, float uMax, float vMin, float vMax,
                                          float r, float g, float b, float alpha)
    {
        int iterMaxW = (int)(w/iconWidth);
        int iterMaxH = (int)(h/iconHeight);
        float leftoverW = w%iconWidth;
        float leftoverH = h%iconHeight;
        float leftoverWf = leftoverW/(float)iconWidth;
        float leftoverHf = leftoverH/(float)iconHeight;
        float iconUDif = uMax-uMin;
        float iconVDif = vMax-vMin;
        for(int ww = 0; ww < iterMaxW; ww++)
        {
            for(int hh = 0; hh < iterMaxH; hh++)
                drawTexturedColoredRect(builder, transform, x+ww*iconWidth, y+hh*iconHeight, iconWidth, iconHeight,
                        r, g, b, alpha, uMin, uMax, vMin, vMax);
            drawTexturedColoredRect(builder, transform, x+ww*iconWidth, y+iterMaxH*iconHeight, iconWidth, leftoverH,
                    r, g, b, alpha, uMin, uMax, vMin, (vMin+iconVDif*leftoverHf));
        }
        if(leftoverW > 0)
        {
            for(int hh = 0; hh < iterMaxH; hh++)
                drawTexturedColoredRect(builder, transform, x+iterMaxW*iconWidth, y+hh*iconHeight, leftoverW, iconHeight,
                        r, g, b, alpha, uMin, (uMin+iconUDif*leftoverWf), vMin, vMax);
            drawTexturedColoredRect(builder, transform, x+iterMaxW*iconWidth, y+iterMaxH*iconHeight, leftoverW, leftoverH,
                    r, g, b, alpha, uMin, (uMin+iconUDif*leftoverWf), vMin, (vMin+iconVDif*leftoverHf));
        }
    }
    public static void drawTexturedColoredRect(
            VertexConsumer builder, PoseStack transform,
            float x, float y, float w, float h,
            float r, float g, float b, float alpha,
            float u0, float u1, float v0, float v1
    )
    {
        TransformingVertexBuilder innerBuilder = new TransformingVertexBuilder(builder, transform);
        innerBuilder.setColor(r, g, b, alpha);
        innerBuilder.setLight(LightTexture.pack(15, 15));
        innerBuilder.setOverlay(OverlayTexture.NO_OVERLAY);
        innerBuilder.setNormal(1, 1, 1);
        innerBuilder.vertex(x, y+h, 0).uv(u0, v1).endVertex();
        innerBuilder.vertex(x+w, y+h, 0).uv(u1, v1).endVertex();
        innerBuilder.vertex(x+w, y, 0).uv(u1, v0).endVertex();
        innerBuilder.vertex(x, y, 0).uv(u0, v0).endVertex();
    }
    public static TextureAtlasSprite getSprite(ResourceLocation rl)
    {
        return Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS).getSprite(rl);
    }

}
