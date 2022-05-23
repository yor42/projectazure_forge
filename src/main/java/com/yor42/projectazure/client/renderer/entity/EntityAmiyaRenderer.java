package com.yor42.projectazure.client.renderer.entity;

import com.yor42.projectazure.client.model.entity.magicuser.AmiyaModel;
import com.yor42.projectazure.gameobject.entity.companion.magicuser.EntityAmiya;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class EntityAmiyaRenderer extends GeoCompanionRenderer<EntityAmiya> {
    public EntityAmiyaRenderer(EntityRendererManager renderManager) {
        super(renderManager, new AmiyaModel());
    }

    @Override
    public ResourceLocation getTextureLocation(EntityAmiya entity) {
        return TextureEntityLocation("modelamiya");
    }

    @Nonnull
    @Override
    protected Vector3d getHandItemCoordinate() {
        return new Vector3d(0.5F, 0.1F, 1.3F);
    }

}
