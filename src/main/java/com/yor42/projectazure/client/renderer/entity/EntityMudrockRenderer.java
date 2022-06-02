package com.yor42.projectazure.client.renderer.entity;

import com.yor42.projectazure.client.model.entity.sworduser.ModelMudrock;
import com.yor42.projectazure.gameobject.entity.companion.sworduser.EntityMudrock;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class EntityMudrockRenderer extends GeoCompanionRenderer<EntityMudrock> {
    public EntityMudrockRenderer(EntityRendererManager renderManager) {
        super(renderManager, new ModelMudrock());
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull EntityMudrock entity) {
        return TextureEntityLocation("modelmudrock");
    }

}
