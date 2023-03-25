package com.yor42.projectazure.client.renderer.entity.biped;

import com.yor42.projectazure.client.model.entity.sworduser.ArtoriaModel;
import com.yor42.projectazure.client.renderer.entity.GeoCompanionRenderer;
import com.yor42.projectazure.gameobject.entity.companion.meleeattacker.EntityArtoria;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class EntityArtoriaRenderer extends GeoCompanionRenderer<EntityArtoria> {
    public EntityArtoriaRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ArtoriaModel());
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull EntityArtoria entity) {
        return TextureEntityLocation("modelartoria");
    }
}
