package com.yor42.projectazure.client.renderer.entity.biped;

import com.yor42.projectazure.client.model.entity.kansen.javelinModel;
import com.yor42.projectazure.client.renderer.entity.GeoCompanionRenderer;
import com.yor42.projectazure.client.renderer.layer.CompanionRiggingLayer;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityJavelin;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class entityJavelinRenderer extends GeoCompanionRenderer<EntityJavelin> {

    public entityJavelinRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new javelinModel());
        this.addLayer(new CompanionRiggingLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(EntityJavelin entity) {
        return new ResourceLocation(Constants.MODID, "textures/entity/entityjavelin.png");
    }

}
