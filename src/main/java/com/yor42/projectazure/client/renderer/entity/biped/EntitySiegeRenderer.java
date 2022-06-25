package com.yor42.projectazure.client.renderer.entity.biped;

import com.yor42.projectazure.client.model.entity.sworduser.SiegeModel;
import com.yor42.projectazure.client.renderer.entity.GeoCompanionRenderer;
import com.yor42.projectazure.gameobject.entity.companion.meleeattacker.EntitySiege;
import net.minecraft.client.renderer.entity.EntityRendererManager;

public class EntitySiegeRenderer extends GeoCompanionRenderer<EntitySiege> {

    public EntitySiegeRenderer(EntityRendererManager renderManager) {
        super(renderManager, new SiegeModel());
    }

}
