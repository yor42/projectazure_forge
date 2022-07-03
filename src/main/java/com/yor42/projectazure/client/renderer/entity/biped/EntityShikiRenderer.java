package com.yor42.projectazure.client.renderer.entity.biped;

import com.yor42.projectazure.client.model.entity.sworduser.ModelShiki;
import com.yor42.projectazure.client.renderer.entity.GeoCompanionRenderer;
import com.yor42.projectazure.gameobject.entity.companion.magicuser.EntityKyaru;
import com.yor42.projectazure.gameobject.entity.companion.meleeattacker.EntityShiki;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class EntityShikiRenderer extends GeoCompanionRenderer<EntityShiki> {
    public EntityShikiRenderer(EntityRendererManager renderManager) {
        super(renderManager, new ModelShiki());
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull EntityShiki entity) {
        return TextureEntityLocation("modelshiki");
    }
}
