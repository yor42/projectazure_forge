package com.yor42.projectazure.client.renderer.entity.projectile;

import com.yor42.projectazure.client.model.entity.misc.ModelKnife;
import com.yor42.projectazure.client.renderer.GeoProjectileRenderer;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityThrownKnifeProjectile;
import net.minecraft.client.renderer.entity.EntityRendererManager;

public class EntityThrownKnifeRenderer extends GeoProjectileRenderer<EntityThrownKnifeProjectile> {

    public EntityThrownKnifeRenderer(EntityRendererManager renderManager){
        super(renderManager, new ModelKnife());
    }

}
