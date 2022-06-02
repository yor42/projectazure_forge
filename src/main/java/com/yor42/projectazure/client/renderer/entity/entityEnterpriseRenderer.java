package com.yor42.projectazure.client.renderer.entity;

import com.yor42.projectazure.client.model.entity.kansen.enterpriseModel;
import com.yor42.projectazure.client.renderer.layer.CompanionRiggingLayer;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityEnterprise;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class entityEnterpriseRenderer extends GeoCompanionRenderer<EntityEnterprise> {

    public entityEnterpriseRenderer(EntityRendererManager renderManager) {
        super(renderManager, new enterpriseModel());
        this.addLayer(new CompanionRiggingLayer<>(this));
    }
    @Override
    public ResourceLocation getTextureLocation(@Nonnull EntityEnterprise entity) {
        return TextureEntityLocation("modelenterprise");
    }

}
