package com.yor42.projectazure.client.renderer.entity;

import com.yor42.projectazure.client.model.entity.magicuser.SylviModel;
import com.yor42.projectazure.gameobject.entity.companion.magicuser.EntitySylvi;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class EntitySylviRenderer extends GeoCompanionRenderer<EntitySylvi> {
    public EntitySylviRenderer(EntityRendererManager renderManager) {
        super(renderManager, new SylviModel());
        this.shadowRadius = 0.3F;
    }

    @Override
    public ResourceLocation getTextureLocation(EntitySylvi entity) {
        return TextureEntityLocation("modelsylvi");
    }

    @Nonnull
    @Override
    protected Vector3d getHandItemCoordinate() {
        return new Vector3d(0.6F, 0.1F, 1.35F);
    }

}
