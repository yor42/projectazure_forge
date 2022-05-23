package com.yor42.projectazure.client.renderer.entity;

import com.yor42.projectazure.client.model.entity.bonus.ModelTalulah;
import com.yor42.projectazure.gameobject.entity.companion.bonus.EntityTalulah;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class EntityTalulahRenderer extends GeoCompanionRenderer<EntityTalulah> {

    public EntityTalulahRenderer(EntityRendererManager renderManager) {
        super(renderManager, new ModelTalulah());
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull EntityTalulah entity) {
        return TextureEntityLocation("modeltalulah");
    }

    @Nonnull
    @Override
    protected Vector3d getHandItemCoordinate() {
        return new Vector3d(0.6F, 0.1F, 1.5F);
    }

}
