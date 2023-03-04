package com.yor42.projectazure.client.renderer.entity.projectile;

import com.yor42.projectazure.gameobject.entity.projectiles.EntityArtsProjectile;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class EntityArtsProjectileRenderer extends Abstract2DprojectileRenderer<EntityArtsProjectile> {

    private static final ResourceLocation TEXTURE = ModResourceLocation("textures/entity/projectile/artsprojectile.png");

    public EntityArtsProjectileRenderer(EntityRendererManager p_i46179_1_) {
        super(p_i46179_1_);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityArtsProjectile p_110775_1_) {
        return TEXTURE;
    }
}
