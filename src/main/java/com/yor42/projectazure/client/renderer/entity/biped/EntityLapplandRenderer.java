package com.yor42.projectazure.client.renderer.entity.biped;

import com.yor42.projectazure.client.model.entity.sworduser.LapplandModel;
import com.yor42.projectazure.client.renderer.entity.GeoCompanionRenderer;
import com.yor42.projectazure.gameobject.entity.companion.meleeattacker.EntityLappland;
import net.minecraft.client.renderer.entity.EntityRendererManager;

public class EntityLapplandRenderer extends GeoCompanionRenderer<EntityLappland> {
    public EntityLapplandRenderer(EntityRendererManager renderManager) {
        super(renderManager, new LapplandModel());
    }

}
