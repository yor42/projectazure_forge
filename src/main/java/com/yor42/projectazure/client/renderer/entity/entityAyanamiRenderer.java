package com.yor42.projectazure.client.renderer.entity;

import com.yor42.projectazure.client.model.entity.kansen.ayanamiModel;
import com.yor42.projectazure.client.renderer.layer.CompanionRiggingLayer;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityAyanami;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nonnull;

public class entityAyanamiRenderer extends GeoCompanionRenderer<EntityAyanami> {
    public entityAyanamiRenderer(EntityRendererManager renderManager) {
        super(renderManager, new ayanamiModel());
        this.addLayer(new CompanionRiggingLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(EntityAyanami entity) {
        return new ResourceLocation(Constants.MODID, "textures/entity/modelayanami.png");
    }

    @Nonnull
    @Override
    protected Vector3d getHandItemCoordinate() {
        return new Vector3d(0.6F, 0.1, 1.35F);
    }

}

