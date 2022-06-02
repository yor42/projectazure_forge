package com.yor42.projectazure.client.renderer.entity;

import com.yor42.projectazure.client.model.entity.sworduser.ModelYato;
import com.yor42.projectazure.gameobject.entity.companion.sworduser.EntityYato;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class EntityYatoRenderer extends GeoCompanionRenderer<EntityYato> {
    public EntityYatoRenderer(EntityRendererManager renderManager) {
        super(renderManager, new ModelYato());
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull EntityYato entity) {
        return TextureEntityLocation("modelyato");
    }

}
