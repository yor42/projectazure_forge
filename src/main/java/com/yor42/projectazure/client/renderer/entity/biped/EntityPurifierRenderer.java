package com.yor42.projectazure.client.renderer.entity.biped;

import com.yor42.projectazure.client.model.entity.kansen.PurifierModel;
import com.yor42.projectazure.client.renderer.entity.GeoCompanionRenderer;
import com.yor42.projectazure.client.renderer.layer.CompanionRiggingLayer;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityPurifier;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class EntityPurifierRenderer extends GeoCompanionRenderer<EntityPurifier> {
    public EntityPurifierRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PurifierModel());
        this.addLayer(new CompanionRiggingLayer<>(this));
    }
}
