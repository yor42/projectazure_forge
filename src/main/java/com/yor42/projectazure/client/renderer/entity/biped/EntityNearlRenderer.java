package com.yor42.projectazure.client.renderer.entity.biped;

import com.yor42.projectazure.client.model.entity.sworduser.NearlModel;
import com.yor42.projectazure.client.renderer.entity.GeoCompanionRenderer;
import com.yor42.projectazure.gameobject.entity.companion.meleeattacker.EntityNearl;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class EntityNearlRenderer extends GeoCompanionRenderer<EntityNearl> {

    public EntityNearlRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new NearlModel());
    }


}
