package com.yor42.projectazure.client.renderer.entity.biped;

import com.yor42.projectazure.client.model.entity.sworduser.ScathathModel;
import com.yor42.projectazure.client.renderer.entity.GeoCompanionRenderer;
import com.yor42.projectazure.gameobject.entity.companion.meleeattacker.EntityScathath;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class EntityScathathRenderer extends GeoCompanionRenderer<EntityScathath> {
    public EntityScathathRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ScathathModel());
    }
    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull EntityScathath entity) {
        return TextureEntityLocation("modelscathath");
    }

}
