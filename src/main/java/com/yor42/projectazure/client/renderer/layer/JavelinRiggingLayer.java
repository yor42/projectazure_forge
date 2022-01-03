package com.yor42.projectazure.client.renderer.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityJavelin;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class JavelinRiggingLayer extends GeoLayerRenderer<EntityJavelin> {

    public JavelinRiggingLayer(IGeoRenderer<EntityJavelin> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntityJavelin entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack rigging = entitylivingbaseIn.getRigging();
        Item item = rigging.getItem();

        boolean flag = item instanceof ItemRiggingBase;
        if (flag) {
            ((ItemRiggingBase)item).RenderRigging(this.getEntityModel(), rigging, entitylivingbaseIn, matrixStackIn, bufferIn, packedLightIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        }
    }
}
