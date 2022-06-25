package com.yor42.projectazure.client.renderer.entity.biped;

import com.yor42.projectazure.client.model.entity.sworduser.ChenModel;
import com.yor42.projectazure.client.renderer.entity.GeoCompanionRenderer;
import com.yor42.projectazure.gameobject.entity.companion.meleeattacker.EntityChen;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class EntityChenRenderer extends GeoCompanionRenderer<EntityChen> {
    public EntityChenRenderer(EntityRendererManager renderManager) {
        super(renderManager, new ChenModel());
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull EntityChen entity) {
        return TextureEntityLocation("modelchen");
    }

}
