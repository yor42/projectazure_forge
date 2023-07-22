package com.yor42.projectazure.client.renderer.entity.biped;

import com.yor42.projectazure.client.model.entity.gunUser.ModelSaori;
import com.yor42.projectazure.client.renderer.entity.GeoCompanionRenderer;
import com.yor42.projectazure.gameobject.entity.companion.gunusers.EntitySaori;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class EntitySaoriRenderer extends GeoCompanionRenderer<EntitySaori> {
    public EntitySaoriRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelSaori());
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull EntitySaori entity) {
        return TextureEntityLocation("modelsaori");
    }
}
