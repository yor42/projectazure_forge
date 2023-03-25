package com.yor42.projectazure.client.renderer.entity.biped;

import com.yor42.projectazure.client.model.entity.gunUser.ModelM4A1;
import com.yor42.projectazure.client.renderer.entity.GeoCompanionRenderer;
import com.yor42.projectazure.gameobject.entity.companion.gunusers.EntityM4A1;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class EntityM4A1Renderer extends GeoCompanionRenderer<EntityM4A1> {
    public EntityM4A1Renderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelM4A1());
    }

    @Override
    public ResourceLocation getTextureLocation(EntityM4A1 Entity) {
        return TextureEntityLocation("entitym4a1");
    }

}
