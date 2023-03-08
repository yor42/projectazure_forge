package com.yor42.projectazure.libs.utils;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

/*
 *  BluSunrize
 *  Copyright (c) 2021
 *
 *  This code is licensed under "Blu's License of Common Sense"
 *  Details can be found in the license file in the root folder of this project
 */

public class RenderingUtils {
    public static void drawRepeatedFluidSpriteGui(IRenderTypeBuffer buffer, MatrixStack stack, FluidStack fluid, float x, float y, float w, float h)
    {
        RenderType renderType = getGui(PlayerContainer.BLOCK_ATLAS);
        IVertexBuilder builder = buffer.getBuffer(renderType);
        drawRepeatedFluidSprite(builder, stack, fluid, x, y, w, h);
    }

    public static RenderType getGui(ResourceLocation texture)
    {
        return RenderType.create(
                "gui_"+texture,
                DefaultVertexFormats.POSITION_COLOR_TEX,
                GL11.GL_QUADS,
                256,
                RenderType.State.builder()
                        .setTextureState(new RenderState.TextureState(texture, false, false))
                        .setAlphaState(new RenderState.AlphaState(0.5F))
                        .createCompositeState(false)
        );
    }

    public static void drawRepeatedFluidSprite(IVertexBuilder builder, MatrixStack transform, FluidStack fluid, float x, float y, float w, float h)
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
    public static void drawRepeatedSprite(IVertexBuilder builder, MatrixStack transform, float x, float y, float w,
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
            IVertexBuilder builder, MatrixStack transform,
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
        return Minecraft.getInstance().getModelManager().getAtlas(PlayerContainer.BLOCK_ATLAS).getSprite(rl);
    }

    public static void renderEntityInInventory(int x, int y, float scale, float mouseX, float mouseY, LivingEntity entity) {
        float f = (float)Math.atan((x-mouseX) / 40.0F);
        float f1 = (float)Math.atan((y-mouseY) / 40.0F);
        RenderSystem.pushMatrix();
        RenderSystem.translatef((float)x, (float)y, 1050.0F);
        RenderSystem.scalef(1.0F, 1.0F, -1.0F);
        MatrixStack matrixstack = new MatrixStack();
        matrixstack.translate(0.0D, 0.0D, 1000.0D);
        matrixstack.scale(scale, scale, scale);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
        Quaternion quaternion1 = Vector3f.XP.rotationDegrees(f1 * 20.0F);
        quaternion.mul(quaternion1);
        matrixstack.mulPose(quaternion);
        float f2 = entity.yBodyRot;
        float f3 = entity.yRot;
        float f4 = entity.xRot;
        float f5 = entity.yHeadRotO;
        float f6 = entity.yHeadRot;
        entity.yBodyRot = 180.0F + f * 20.0F;
        entity.yRot = 180.0F + f * 40.0F;
        entity.xRot = -f1 * 20.0F;
        entity.yHeadRot = entity.yRot;
        entity.yHeadRotO = entity.yRot;
        EntityRendererManager entityrenderermanager = Minecraft.getInstance().getEntityRenderDispatcher();
        quaternion1.conj();
        entityrenderermanager.overrideCameraOrientation(quaternion1);
        entityrenderermanager.setRenderShadow(false);
        IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> entityrenderermanager.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, matrixstack, irendertypebuffer$impl, 15728880));
        irendertypebuffer$impl.endBatch();
        entityrenderermanager.setRenderShadow(true);
        entity.yBodyRot = f2;
        entity.yRot = f3;
        entity.xRot = f4;
        entity.yHeadRotO = f5;
        entity.yHeadRot = f6;
        RenderSystem.popMatrix();
    }
}
