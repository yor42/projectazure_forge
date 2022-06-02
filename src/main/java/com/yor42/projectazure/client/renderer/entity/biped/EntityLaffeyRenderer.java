package com.yor42.projectazure.client.renderer.entity.biped;

import com.yor42.projectazure.client.model.entity.kansen.laffeyModel;
import com.yor42.projectazure.client.renderer.entity.GeoCompanionRenderer;
import com.yor42.projectazure.client.renderer.layer.CompanionRiggingLayer;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityLaffey;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class EntityLaffeyRenderer extends GeoCompanionRenderer<EntityLaffey> {

    public EntityLaffeyRenderer(EntityRendererManager renderManager) {
        super(renderManager, new laffeyModel());
        this.addLayer(new CompanionRiggingLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(EntityLaffey entity) {
        return new ResourceLocation(Constants.MODID, "textures/entity/modellaffey.png");
    }

}
