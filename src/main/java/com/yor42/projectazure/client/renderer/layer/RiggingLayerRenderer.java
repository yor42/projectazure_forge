package com.yor42.projectazure.client.renderer.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatableModel;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.model.provider.data.EntityModelData;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Objects;

public class RiggingLayerRenderer<T extends AbstractEntityCompanion> extends GeoLayerRenderer<T> implements IGeoRenderer<T> {

    @Nullable
    private AnimatedGeoModel modelProvider;

    public RiggingLayerRenderer(IGeoRenderer<T> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        matrixStackIn.pushPose();

        boolean shouldSit = entitylivingbaseIn.isPassenger()
                && (entitylivingbaseIn.getVehicle() != null && entitylivingbaseIn.getVehicle().shouldRiderSit());

        EntityModelData entityModelData = new EntityModelData();
        entityModelData.isSitting = shouldSit;
        entityModelData.isChild = entitylivingbaseIn.isBaby();

        AnimationEvent<T> predicate = new AnimationEvent<T>(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks,
                !(limbSwingAmount > -0.15F && limbSwingAmount < 0.15F), Collections.singletonList(entityModelData));

        if(entitylivingbaseIn.getRigging().getItem() instanceof ItemRiggingBase) {
            this.modelProvider = ((ItemRiggingBase) entitylivingbaseIn.getRigging().getItem()).getModel();

            if (this.modelProvider != null) {

                if(getEntityModel().getModel(getEntityModel().getModelLocation(null)).getBone("Body").isPresent()){
                    IBone hostbone = getEntityModel().getModel(getEntityModel().getModelLocation(null)).getBone("Body").get();
                    matrixStackIn.translate(hostbone.getPositionX()/16, (hostbone.getPositionY()+36.6)/16, hostbone.getPositionZ()/16);
                }

                GeoModel model = this.modelProvider.getModel(this.modelProvider.getModelLocation(entitylivingbaseIn));
                ((IAnimatableModel) this.modelProvider).setLivingAnimations(entitylivingbaseIn, this.getUniqueID(entitylivingbaseIn), predicate);
                render(model, entitylivingbaseIn, partialTicks, RenderType.entitySmoothCutout(this.getTextureLocation(entitylivingbaseIn)), matrixStackIn, bufferIn, null, packedLightIn, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
                matrixStackIn.popPose();

                //ItemStackHandler Equipments = new RiggingItemCapabilityProvider(entitylivingbaseIn.getRigging(), entitylivingbaseIn, 10, 10).getEquipments();


            }
        }
        matrixStackIn.popPose();
    }

    public Integer getUniqueID(T animatable) {
        return Objects.hash(animatable.getId(), animatable.hashCode(), animatable.getRigging());
    }

    @Override
    public GeoModelProvider getGeoModelProvider() {
        return this.modelProvider;
    }

    @Override
    public ResourceLocation getTextureLocation(T instance) {
        return this.modelProvider.getTextureLocation(instance);
    }
}
