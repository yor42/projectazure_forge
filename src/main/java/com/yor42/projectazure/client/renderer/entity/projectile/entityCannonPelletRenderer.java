package com.yor42.projectazure.client.renderer.entity.projectile;

import com.yor42.projectazure.gameobject.entity.projectiles.EntityCannonPelllet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class entityCannonPelletRenderer extends Abstract2DprojectileRenderer<EntityCannonPelllet> {

    private static final ResourceLocation TEXTURE = ModResourceLocation("textures/entity/projectile/shell_generic.png");

    public entityCannonPelletRenderer(EntityRendererProvider.Context p_i46179_1_) {
        super(p_i46179_1_);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityCannonPelllet entity) {
        return TEXTURE;
    }
}
