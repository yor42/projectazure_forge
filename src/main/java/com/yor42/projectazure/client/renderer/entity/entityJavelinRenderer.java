package com.yor42.projectazure.client.renderer.entity;

import com.yor42.projectazure.client.model.entity.kansen.javelinModel;
import com.yor42.projectazure.client.renderer.layer.CompanionRiggingLayer;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityJavelin;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class entityJavelinRenderer extends GeoCompanionRenderer<EntityJavelin> {

    public entityJavelinRenderer(EntityRendererManager renderManager) {
        super(renderManager, new javelinModel());
        this.addLayer(new CompanionRiggingLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(EntityJavelin entity) {
        return new ResourceLocation(Constants.MODID, "textures/entity/entityjavelin.png");
    }

}
