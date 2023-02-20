package com.yor42.projectazure.client.renderer.entity.biped;

import com.yor42.projectazure.client.model.entity.bonus.ModelSkullShatterer;
import com.yor42.projectazure.client.renderer.entity.GeoCompanionRenderer;
import com.yor42.projectazure.gameobject.entity.companion.gunusers.EntitySkullShatterer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class EntitySkullShattererRenderer extends GeoCompanionRenderer<EntitySkullShatterer> {
    public EntitySkullShattererRenderer(EntityRendererManager renderManager) {
        super(renderManager, new ModelSkullShatterer());
    }

    @Override
    public ResourceLocation getTextureLocation(EntitySkullShatterer entity) {
        return TextureEntityLocation("modelskullshatterer");
    }
}
