package com.yor42.projectazure.client.renderer.entity.biped;

import com.yor42.projectazure.client.model.entity.sworduser.HaruModel;
import com.yor42.projectazure.client.renderer.entity.GeoCompanionRenderer;
import com.yor42.projectazure.gameobject.entity.companion.meleeattacker.EntityHaru;
import net.minecraft.client.renderer.entity.EntityRendererManager;

public class EntityHaruRenderer extends GeoCompanionRenderer<EntityHaru> {
    public EntityHaruRenderer(EntityRendererManager renderManager) {
        super(renderManager, new HaruModel());
    }
}
