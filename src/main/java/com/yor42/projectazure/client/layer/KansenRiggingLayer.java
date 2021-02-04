package com.yor42.projectazure.client.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yor42.projectazure.client.model.rigging.modelDDRiggingDefault;
import com.yor42.projectazure.client.renderer.entity.entityAyanamiRenderer;
import com.yor42.projectazure.gameobject.entity.EntityAyanami;
import com.yor42.projectazure.gameobject.entity.EntityKansenBase;
import com.yor42.projectazure.gameobject.items.ItemRiggingBase;
import com.yor42.projectazure.gameobject.items.ItemRiggingDD;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import javax.annotation.Nullable;

public class KansenRiggingLayer extends GeoLayerRenderer<EntityAyanami> implements IGeoRenderer {

    private modelDDRiggingDefault model = new modelDDRiggingDefault();

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

    public KansenRiggingLayer(IGeoRenderer<EntityAyanami> entityRendererIn) {
        super(entityRendererIn);
    }


    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntityAyanami entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if(entitylivingbaseIn.getRigging() != ItemStack.EMPTY){
            matrixStackIn.push();
            this.modelProvider = (AnimatedGeoModel) ((ItemRiggingBase)entitylivingbaseIn.getRigging().getItem()).getModel();
            RenderType type = RenderType.getEntitySmoothCutout(this.getEntityTexture(entitylivingbaseIn));

            this.render((this.modelProvider.getModel(this.modelProvider.getModelLocation(modelProvider))), entitylivingbaseIn, partialTicks, type, matrixStackIn,bufferIn, bufferIn.getBuffer(type), packedLightIn, OverlayTexture.NO_OVERLAY, 1,1,1,1);
                    //this.modelProvider.getModel(modelProvider.getModelLocation(this.modelProvider)), entitylivingbaseIn, partialTicks, type, matrixStackIn, bufferIn.getBuffer(type), null, packedLightIn, 1,1,1,1)
        }
    }

    @Override
    public GeoModelProvider getGeoModelProvider() {
        return this.modelProvider;
    }

    @Override
    public ResourceLocation getTextureLocation(Object o) {
        return this.modelProvider.getTextureLocation(o);
    }
}
