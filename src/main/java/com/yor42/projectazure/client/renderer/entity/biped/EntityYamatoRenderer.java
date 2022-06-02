package com.yor42.projectazure.client.renderer.entity.biped;

import com.yor42.projectazure.client.model.entity.kansen.YamatoModel;
import com.yor42.projectazure.client.renderer.entity.GeoCompanionRenderer;
import com.yor42.projectazure.client.renderer.layer.CompanionRiggingLayer;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityYamato;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class EntityYamatoRenderer extends GeoCompanionRenderer<EntityYamato> {

    public EntityYamatoRenderer(EntityRendererManager renderManager) {
        super(renderManager, new YamatoModel());
        this.addLayer(new CompanionRiggingLayer<>(this));
    }
    @Override
    public ResourceLocation getTextureLocation(EntityYamato instance) {
        return TextureEntityLocation("modelyamato");
    }

}
