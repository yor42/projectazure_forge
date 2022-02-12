package com.yor42.projectazure.client.renderer.entity.projectile;

import com.yor42.projectazure.client.model.entity.misc.modelProjectileTorpedo;
import com.yor42.projectazure.client.renderer.GeoProjectileRenderer;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityProjectileTorpedo;
import net.minecraft.client.renderer.entity.EntityRendererManager;

public class EntityProjectileTorpedoRenderer extends GeoProjectileRenderer<EntityProjectileTorpedo> {
    public EntityProjectileTorpedoRenderer(EntityRendererManager renderManager) {
        super(renderManager, new modelProjectileTorpedo());
    }
}
