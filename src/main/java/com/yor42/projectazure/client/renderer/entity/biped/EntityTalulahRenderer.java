package com.yor42.projectazure.client.renderer.entity.biped;

import com.yor42.projectazure.client.model.entity.bonus.ModelTalulah;
import com.yor42.projectazure.client.renderer.entity.GeoCompanionRenderer;
import com.yor42.projectazure.gameobject.entity.companion.bonus.EntityTalulah;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class EntityTalulahRenderer extends GeoCompanionRenderer<EntityTalulah> {

    public EntityTalulahRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelTalulah());
    }

}
