package com.yor42.projectazure.client.renderer.entity;

import com.yor42.projectazure.client.model.entity.gunUser.ModelM4A1;
import com.yor42.projectazure.gameobject.entity.companion.gunusers.EntityM4A1;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class EntityM4A1Renderer extends GeoCompanionRenderer<EntityM4A1> {
    public EntityM4A1Renderer(EntityRendererManager renderManager) {
        super(renderManager, new ModelM4A1());
    }

    @Override
    public ResourceLocation getTextureLocation(EntityM4A1 Entity) {
        return TextureEntityLocation("entitym4a1");
    }

    @Nonnull
    @Override
    protected Vector3d getHandItemCoordinate() {
        return new Vector3d(0.7F, 0.1F, 1.7F);
    }

}
