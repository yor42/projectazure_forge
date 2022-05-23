package com.yor42.projectazure.client.renderer.entity;

import com.yor42.projectazure.client.model.entity.kansen.Z23Model;
import com.yor42.projectazure.client.renderer.layer.Z23RiggingLayer;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityZ23;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nonnull;

public class entityZ23Renderer extends GeoCompanionRenderer<EntityZ23> {

    public entityZ23Renderer(EntityRendererManager renderManager) {
        super(renderManager, new Z23Model());
        this.addLayer(new Z23RiggingLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(EntityZ23 entity) {
        return new ResourceLocation(Constants.MODID, "textures/entity/entitynimi.png");
    }

    @Nonnull
    @Override
    protected Vector3d getHandItemCoordinate() {
        return new Vector3d(0.6F, 0.1F, 1.35F);
    }

}
