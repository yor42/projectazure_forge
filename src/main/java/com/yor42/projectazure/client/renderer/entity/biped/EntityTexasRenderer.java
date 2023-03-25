package com.yor42.projectazure.client.renderer.entity.biped;

import com.yor42.projectazure.client.model.entity.sworduser.TexasModel;
import com.yor42.projectazure.client.renderer.entity.GeoCompanionRenderer;
import com.yor42.projectazure.gameobject.entity.companion.meleeattacker.EntityTexas;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class EntityTexasRenderer extends GeoCompanionRenderer<EntityTexas> {

    public EntityTexasRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new TexasModel());
    }

}
