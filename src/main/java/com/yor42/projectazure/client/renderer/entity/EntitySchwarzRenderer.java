package com.yor42.projectazure.client.renderer.entity;

import com.yor42.projectazure.client.model.entity.ranged.ModelSchwarz;
import com.yor42.projectazure.gameobject.entity.companion.ranged.EntitySchwarz;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nonnull;

public class EntitySchwarzRenderer extends GeoCompanionRenderer<EntitySchwarz> {

    public EntitySchwarzRenderer(EntityRendererManager renderManager) {
        super(renderManager, new ModelSchwarz());
    }

    @Nonnull
    @Override
    protected Vector3d getHandItemCoordinate() {
        return new Vector3d(0.6F, 0.2F, 1.4F);
    }

}
