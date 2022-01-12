package com.yor42.projectazure.client.renderer.entity.misc;

import com.yor42.projectazure.client.model.entity.misc.ModelClaymore;
import com.yor42.projectazure.client.renderer.GeoProjectileRenderer;
import com.yor42.projectazure.gameobject.entity.misc.EntityClaymore;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureLocation;

public class EntityClaymoreRenderer extends GeoEntityRenderer<EntityClaymore> {
    public EntityClaymoreRenderer(EntityRendererManager renderManager) {
        super(renderManager, new ModelClaymore());
    }

    @Override
    public ResourceLocation getEntityTexture(EntityClaymore entity) {
        return TextureLocation("entity/modelclaymore");
    }
}
