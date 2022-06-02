package com.yor42.projectazure.client.renderer.entity;

import com.yor42.projectazure.client.model.entity.magicuser.AmiyaModel;
import com.yor42.projectazure.gameobject.entity.companion.magicuser.EntityAmiya;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class EntityAmiyaRenderer extends GeoCompanionRenderer<EntityAmiya> {
    public EntityAmiyaRenderer(EntityRendererManager renderManager) {
        super(renderManager, new AmiyaModel());
    }

    @Override
    public ResourceLocation getTextureLocation(EntityAmiya entity) {
        return TextureEntityLocation("modelamiya");
    }

}
