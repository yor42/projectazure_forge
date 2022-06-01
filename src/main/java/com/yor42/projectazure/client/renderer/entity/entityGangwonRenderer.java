package com.yor42.projectazure.client.renderer.entity;

import com.yor42.projectazure.client.model.entity.kansen.gangwonModel;
import com.yor42.projectazure.client.renderer.layer.CompanionRiggingLayer;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityGangwon;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nonnull;

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

    @Nonnull
    @Override
    protected Vector3d getHandItemCoordinate() {
        return new Vector3d(0.5F, 0.1, 1.15F);
    }
}
