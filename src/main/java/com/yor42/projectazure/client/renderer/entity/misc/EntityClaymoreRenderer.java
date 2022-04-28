package com.yor42.projectazure.client.renderer.entity.misc;

import com.yor42.projectazure.client.model.entity.misc.ModelClaymore;
import com.yor42.projectazure.gameobject.entity.misc.EntityClaymore;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureLocation;

public class EntityClaymoreRenderer extends GeoEntityRenderer<EntityClaymore> {
    public EntityClaymoreRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelClaymore());
    }

    @Override
    public ResourceLocation getTextureLocation(EntityClaymore entity) {
        return TextureLocation("entity/modelclaymore");
    }
}
