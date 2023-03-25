package com.yor42.projectazure.client.renderer.entity.biped;

import com.yor42.projectazure.client.model.entity.kansen.Z23Model;
import com.yor42.projectazure.client.renderer.entity.GeoCompanionRenderer;
import com.yor42.projectazure.client.renderer.layer.CompanionRiggingLayer;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityZ23;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class EntityZ23Renderer extends GeoCompanionRenderer<EntityZ23> {

    public EntityZ23Renderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new Z23Model());
        this.addLayer(new CompanionRiggingLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(EntityZ23 entity) {
        return new ResourceLocation(Constants.MODID, "textures/entity/entitynimi.png");
    }

}
