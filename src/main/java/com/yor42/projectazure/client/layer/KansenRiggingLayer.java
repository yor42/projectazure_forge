package com.yor42.projectazure.client.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yor42.projectazure.gameobject.entity.EntityKansenBase;
import com.yor42.projectazure.gameobject.items.ItemRiggingBase;
import com.yor42.projectazure.gameobject.items.ItemRiggingDD;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class KansenRiggingLayer extends LayerRenderer<EntityKansenBase, EntityModel<EntityKansenBase>> {

    private AnimatedGeoModel modelProvider;

    static {
        AnimationController.addModelFetcher((IAnimatable object) ->
        {
            if (object instanceof ItemRiggingDD) {
                return ((ItemRiggingBase) object).getModel();
            }
            return null;
        });
    }

    public KansenRiggingLayer(IEntityRenderer<EntityKansenBase, EntityModel<EntityKansenBase>> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntityKansenBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrixStackIn.push();
        this.modelProvider = (AnimatedGeoModel) ((ItemRiggingBase)entitylivingbaseIn.getRigging().getItem()).getModel();
    }
}
