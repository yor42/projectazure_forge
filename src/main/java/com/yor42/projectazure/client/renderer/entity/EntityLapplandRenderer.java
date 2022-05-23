package com.yor42.projectazure.client.renderer.entity;

import com.yor42.projectazure.client.model.entity.sworduser.LapplandModel;
import com.yor42.projectazure.gameobject.entity.companion.sworduser.EntityLappland;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nonnull;

public class EntityLapplandRenderer extends GeoCompanionRenderer<EntityLappland> {
    public EntityLapplandRenderer(EntityRendererManager renderManager) {
        super(renderManager, new LapplandModel());
    }

    @Override
    protected boolean isLeftHanded() {
        return true;
    }

    @Nonnull
    @Override
    protected Vector3d getHandItemCoordinate() {
        return new Vector3d(0.6F, 0.2F, 1.4F);
    }
}
