package com.yor42.projectazure.client.renderer.entity.biped;

import com.yor42.projectazure.client.model.entity.ranged.ModelSchwarz;
import com.yor42.projectazure.client.renderer.entity.GeoCompanionRenderer;
import com.yor42.projectazure.gameobject.entity.companion.ranged.EntitySchwarz;
import net.minecraft.client.renderer.entity.EntityRendererManager;

public class EntitySchwarzRenderer extends GeoCompanionRenderer<EntitySchwarz> {

    public EntitySchwarzRenderer(EntityRendererManager renderManager) {
        super(renderManager, new ModelSchwarz());
    }

}
