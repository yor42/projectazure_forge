package com.yor42.projectazure.client.renderer.entity.biped;

import com.yor42.projectazure.client.model.entity.kansen.ayanamiModel;
import com.yor42.projectazure.client.renderer.entity.GeoCompanionRenderer;
import com.yor42.projectazure.client.renderer.layer.CompanionRiggingLayer;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityAyanami;
import net.minecraft.client.renderer.entity.EntityRendererManager;

public class entityAyanamiRenderer extends GeoCompanionRenderer<EntityAyanami> {
    public entityAyanamiRenderer(EntityRendererManager renderManager) {
        super(renderManager, new ayanamiModel());
        this.addLayer(new CompanionRiggingLayer<>(this));
    }
}

