package com.yor42.projectazure.client.renderer.entity;

import com.yor42.projectazure.client.model.entity.kansen.ayanamiModel;
import com.yor42.projectazure.client.renderer.layer.CompanionRiggingLayer;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityAyanami;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class entityAyanamiRenderer extends GeoCompanionRenderer<EntityAyanami> {
    public entityAyanamiRenderer(EntityRendererManager renderManager) {
        super(renderManager, new ayanamiModel());
        this.addLayer(new CompanionRiggingLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(EntityAyanami entity) {
        return new ResourceLocation(Constants.MODID, "textures/entity/modelayanami.png");
    }

}

