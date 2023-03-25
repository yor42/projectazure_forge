package com.yor42.projectazure.client.renderer.entity.projectile;

import com.yor42.projectazure.gameobject.entity.projectiles.EntitySupernovaProjectile;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class EntitySupernovaProjectileRenderer extends Abstract2DprojectileRenderer<EntitySupernovaProjectile> {

    protected static final ResourceLocation TEXTURE = ModResourceLocation("textures/entity/projectile/supernova_shell.png");
    public EntitySupernovaProjectileRenderer(EntityRendererProvider.Context p_i46179_1_) {
        super(p_i46179_1_);
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull EntitySupernovaProjectile p_110775_1_) {
        return TEXTURE;
    }
}
