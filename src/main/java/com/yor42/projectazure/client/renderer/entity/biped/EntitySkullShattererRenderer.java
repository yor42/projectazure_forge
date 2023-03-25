package com.yor42.projectazure.client.renderer.entity.biped;

import com.yor42.projectazure.client.model.entity.bonus.ModelSkullShatterer;
import com.yor42.projectazure.client.renderer.entity.GeoCompanionRenderer;
import com.yor42.projectazure.gameobject.entity.companion.bonus.EntitySkullShatterer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class EntitySkullShattererRenderer extends GeoCompanionRenderer<EntitySkullShatterer> {
    public EntitySkullShattererRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelSkullShatterer());
    }

    @Override
    public ResourceLocation getTextureLocation(EntitySkullShatterer entity) {
        return TextureEntityLocation("modelskullshatterer");
    }
}
