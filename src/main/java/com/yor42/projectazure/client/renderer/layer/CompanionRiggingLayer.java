package com.yor42.projectazure.client.renderer.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityEnterprise;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenBase;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class CompanionRiggingLayer<E extends AbstractEntityCompanion> extends GeoLayerRenderer<E> {
    public CompanionRiggingLayer(IGeoRenderer<E> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, E entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack rigging = entitylivingbaseIn.getRigging();
        Item item = rigging.getItem();

        if (item instanceof ItemRiggingBase) {
            ((ItemRiggingBase)item).RenderRigging(this.getEntityModel(), rigging, entitylivingbaseIn, matrixStackIn, bufferIn, packedLightIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        }
    }
}