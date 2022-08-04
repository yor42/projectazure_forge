package com.yor42.projectazure.client.renderer.entity.biped;

import com.yor42.projectazure.client.model.entity.gunUser.ModelHK416;
import com.yor42.projectazure.client.renderer.entity.GeoCompanionRenderer;
import com.yor42.projectazure.gameobject.entity.companion.gunusers.EntityHK416;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class EntityHK416Renderer extends GeoCompanionRenderer<EntityHK416> {
    public EntityHK416Renderer(EntityRendererManager renderManager) {
        super(renderManager, new ModelHK416());
    }
}
