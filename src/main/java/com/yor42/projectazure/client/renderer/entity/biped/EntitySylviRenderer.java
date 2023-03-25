package com.yor42.projectazure.client.renderer.entity.biped;

import com.yor42.projectazure.client.model.entity.magicuser.SylviModel;
import com.yor42.projectazure.client.renderer.entity.GeoCompanionRenderer;
import com.yor42.projectazure.gameobject.entity.companion.magicuser.EntitySylvi;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class EntitySylviRenderer extends GeoCompanionRenderer<EntitySylvi> {
    public EntitySylviRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SylviModel());
        this.shadowRadius = 0.3F;
    }

    @Override
    public ResourceLocation getTextureLocation(EntitySylvi entity) {
        return TextureEntityLocation("modelsylvi");
    }

}
