package com.yor42.projectazure.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityYamato;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class YamatoRiggingLayer extends GeoLayerRenderer<EntityYamato> {
    public YamatoRiggingLayer(IGeoRenderer<EntityYamato> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EntityYamato entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack rigging = entityLivingBaseIn.getRigging();
        Item item = rigging.getItem();

        if (item instanceof ItemRiggingBase) {
            ((ItemRiggingBase)item).RenderRigging(this.getEntityModel(), rigging, entityLivingBaseIn, matrixStackIn, bufferIn, packedLightIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
        }
    }
}
