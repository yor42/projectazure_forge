package com.yor42.projectazure.client.renderer.entity.biped;

import com.yor42.projectazure.client.model.entity.sworduser.ModelMash;
import com.yor42.projectazure.client.renderer.entity.GeoCompanionRenderer;
import com.yor42.projectazure.gameobject.entity.companion.meleeattacker.EntityMash;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class EntityMashRenderer extends GeoCompanionRenderer<EntityMash> {
    public EntityMashRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelMash());
    }
}
