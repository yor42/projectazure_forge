package com.yor42.projectazure.client.renderer.entity.biped;

import com.yor42.projectazure.client.model.entity.kansen.enterpriseModel;
import com.yor42.projectazure.client.renderer.entity.GeoCompanionRenderer;
import com.yor42.projectazure.client.renderer.layer.CompanionRiggingLayer;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityEnterprise;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class entityEnterpriseRenderer extends GeoCompanionRenderer<EntityEnterprise> {

    public entityEnterpriseRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new enterpriseModel());
        this.addLayer(new CompanionRiggingLayer<>(this));
    }
    @Override
    public ResourceLocation getTextureLocation(@Nonnull EntityEnterprise entity) {
        return TextureEntityLocation("modelenterprise");
    }

}
