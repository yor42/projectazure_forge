package com.yor42.projectazure.client.renderer.entity;

import com.yor42.projectazure.client.model.entity.kansen.gangwonModel;
import com.yor42.projectazure.client.renderer.layer.CompanionRiggingLayer;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityGangwon;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class entityGangwonRenderer extends GeoCompanionRenderer<EntityGangwon> {

    public entityGangwonRenderer(EntityRendererManager renderManager) {
        super(renderManager, new gangwonModel());
        this.addLayer(new CompanionRiggingLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(EntityGangwon entity) {
        return TextureEntityLocation("modelgangwon");
    }

}
