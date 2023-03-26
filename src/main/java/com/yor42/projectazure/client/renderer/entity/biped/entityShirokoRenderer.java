package com.yor42.projectazure.client.renderer.entity.biped;

import com.yor42.projectazure.client.model.entity.gunUser.ModelShiroko;
import com.yor42.projectazure.client.renderer.entity.GeoCompanionRenderer;
import com.yor42.projectazure.gameobject.entity.companion.gunusers.EntityShiroko;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class entityShirokoRenderer extends GeoCompanionRenderer<EntityShiroko> {

    public entityShirokoRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelShiroko());
        this.shadowRadius = 0.3F;
    }
}
