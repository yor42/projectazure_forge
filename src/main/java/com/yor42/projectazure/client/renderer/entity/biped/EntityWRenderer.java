package com.yor42.projectazure.client.renderer.entity.biped;

import com.yor42.projectazure.client.model.entity.gunUser.ModelW;
import com.yor42.projectazure.client.renderer.entity.GeoCompanionRenderer;
import com.yor42.projectazure.gameobject.entity.companion.gunusers.EntityW;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class EntityWRenderer extends GeoCompanionRenderer<EntityW> {
    public EntityWRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelW());
    }
}
