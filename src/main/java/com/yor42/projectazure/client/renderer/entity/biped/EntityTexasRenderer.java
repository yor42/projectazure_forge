package com.yor42.projectazure.client.renderer.entity.biped;

import com.yor42.projectazure.client.model.entity.sworduser.TexasModel;
import com.yor42.projectazure.client.renderer.entity.GeoCompanionRenderer;
import com.yor42.projectazure.gameobject.entity.companion.sworduser.EntityTexas;
import net.minecraft.client.renderer.entity.EntityRendererManager;

public class EntityTexasRenderer extends GeoCompanionRenderer<EntityTexas> {

    public EntityTexasRenderer(EntityRendererManager renderManager) {
        super(renderManager, new TexasModel());
    }

}
