package com.yor42.projectazure.client.renderer.entity;

import com.yor42.projectazure.client.model.entity.kansen.nagatoModel;
import com.yor42.projectazure.client.renderer.layer.CompanionRiggingLayer;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityNagato;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class entityNagatoRenderer extends GeoCompanionRenderer<EntityNagato> {

    public entityNagatoRenderer(EntityRendererManager renderManager) {
        super(renderManager, new nagatoModel());
        this.addLayer(new CompanionRiggingLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(EntityNagato entity) {
        return TextureEntityLocation("modelnagato");
    }

    @Nonnull
    @Override
    protected Vector3d getHandItemCoordinate() {
        return new Vector3d(0.5F, 0.1F, 1.15F);
    }

}
