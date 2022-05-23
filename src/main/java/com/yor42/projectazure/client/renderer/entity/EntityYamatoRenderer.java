package com.yor42.projectazure.client.renderer.entity;

import com.yor42.projectazure.client.model.entity.kansen.YamatoModel;
import com.yor42.projectazure.client.renderer.layer.YamatoRiggingLayer;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityYamato;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class EntityYamatoRenderer extends GeoCompanionRenderer<EntityYamato> {

    public EntityYamatoRenderer(EntityRendererManager renderManager) {
        super(renderManager, new YamatoModel());
        this.addLayer(new YamatoRiggingLayer(this));
        this.shadowRadius = 0.4F;
    }
    @Override
    public ResourceLocation getTextureLocation(EntityYamato instance) {
        return TextureEntityLocation("modelyamato");
    }

    @Nonnull
    @Override
    protected Vector3d getHandItemCoordinate() {
        return new Vector3d(0.7F, 0.1F, 1.7F);
    }

}
