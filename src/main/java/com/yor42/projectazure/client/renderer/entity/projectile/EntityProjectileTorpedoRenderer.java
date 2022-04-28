package com.yor42.projectazure.client.renderer.entity.projectile;

import com.yor42.projectazure.client.model.entity.misc.modelProjectileTorpedo;
import com.yor42.projectazure.client.renderer.GeoProjectileRenderer;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityProjectileTorpedo;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class EntityProjectileTorpedoRenderer extends GeoProjectileRenderer<EntityProjectileTorpedo> {
    public EntityProjectileTorpedoRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new modelProjectileTorpedo());
    }
}
