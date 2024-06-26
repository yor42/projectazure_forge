package com.yor42.projectazure.client.renderer.entity.biped;

import com.yor42.projectazure.client.model.entity.sworduser.ModelMudrock;
import com.yor42.projectazure.client.renderer.entity.GeoCompanionRenderer;
import com.yor42.projectazure.gameobject.entity.companion.meleeattacker.EntityMudrock;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class EntityMudrockRenderer extends GeoCompanionRenderer<EntityMudrock> {
    public EntityMudrockRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelMudrock());
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull EntityMudrock entity) {
        return TextureEntityLocation("modelmudrock");
    }

}
