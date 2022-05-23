package com.yor42.projectazure.client.renderer.entity;

import com.yor42.projectazure.client.model.entity.kansen.laffeyModel;
import com.yor42.projectazure.client.renderer.layer.LaffeyRiggingLayer;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityLaffey;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nonnull;

public class EntityLaffeyRenderer extends GeoCompanionRenderer<EntityLaffey> {

    public EntityLaffeyRenderer(EntityRendererManager renderManager) {
        super(renderManager, new laffeyModel());
        this.addLayer(new LaffeyRiggingLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(EntityLaffey entity) {
        return new ResourceLocation(Constants.MODID, "textures/entity/modellaffey.png");
    }

    @Nonnull
    @Override
    protected Vector3d getHandItemCoordinate() {
        return new Vector3d(0.6F, 0.1, 1.35F);
    }

}
