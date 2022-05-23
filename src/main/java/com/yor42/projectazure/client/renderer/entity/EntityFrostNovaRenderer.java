package com.yor42.projectazure.client.renderer.entity;

import com.yor42.projectazure.client.model.entity.bonus.ModelFrostNova;
import com.yor42.projectazure.gameobject.entity.companion.bonus.EntityFrostnova;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class EntityFrostNovaRenderer extends GeoCompanionRenderer<EntityFrostnova> {
    public EntityFrostNovaRenderer(EntityRendererManager renderManager) {
        super(renderManager, new ModelFrostNova());
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull EntityFrostnova entity) {
        return TextureEntityLocation("modelfrostnova");
    }

    @Nonnull
    @Override
    protected Vector3d getHandItemCoordinate() {
        return new Vector3d(0.6F, 0.1, 1.5F);
    }

}
