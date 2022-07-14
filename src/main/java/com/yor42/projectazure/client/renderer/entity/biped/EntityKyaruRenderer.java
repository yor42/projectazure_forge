package com.yor42.projectazure.client.renderer.entity.biped;

import com.yor42.projectazure.client.model.entity.magicuser.KyaruModel;
import com.yor42.projectazure.client.renderer.entity.GeoCompanionRenderer;
import com.yor42.projectazure.gameobject.entity.companion.magicuser.EntityKyaru;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class EntityKyaruRenderer extends GeoCompanionRenderer<EntityKyaru> {
    public EntityKyaruRenderer(EntityRendererManager renderManager) {
        super(renderManager, new KyaruModel());
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull EntityKyaru entity) {
        return TextureEntityLocation("modelkyaru");
    }
}
