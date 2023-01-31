package com.yor42.projectazure.client.renderer.entity.biped;

import com.yor42.projectazure.client.model.entity.bonus.ModelTalulah;
import com.yor42.projectazure.client.renderer.entity.GeoCompanionRenderer;
import com.yor42.projectazure.gameobject.entity.companion.bonus.EntityTalulah;
import net.minecraft.client.renderer.entity.EntityRendererManager;

public class EntityTalulahRenderer extends GeoCompanionRenderer<EntityTalulah> {

    public EntityTalulahRenderer(EntityRendererManager renderManager) {
        super(renderManager, new ModelTalulah());
    }

}
