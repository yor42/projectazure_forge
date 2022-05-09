package com.yor42.projectazure.client.renderer.entity;

import com.yor42.projectazure.client.model.entity.sworduser.NearlModel;
import com.yor42.projectazure.client.model.entity.sworduser.SiegeModel;
import com.yor42.projectazure.gameobject.entity.companion.sworduser.EntityNearl;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nonnull;

public class EntityNearlRenderer extends GeoCompanionRenderer<EntityNearl> {

    public EntityNearlRenderer(EntityRendererManager renderManager) {
        super(renderManager, new NearlModel());
    }

    @Nonnull
    @Override
    protected Vector3d getHandItemCoordinate() {
        return new Vector3d(0.6F, 0.2F, 1.65F);
    }


}
