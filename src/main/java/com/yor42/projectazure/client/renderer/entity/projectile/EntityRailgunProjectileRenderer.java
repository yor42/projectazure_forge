package com.yor42.projectazure.client.renderer.entity.projectile;

import com.yor42.projectazure.gameobject.entity.projectiles.EntityRailgunProjectile;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class EntityRailgunProjectileRenderer extends Abstract2DprojectileRenderer<EntityRailgunProjectile> {
    protected static final ResourceLocation TEXTURE = ModResourceLocation("textures/entity/projectile/railgun_shell.png");

    public EntityRailgunProjectileRenderer(EntityRendererManager p_i46179_1_) {
        super(p_i46179_1_);
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull EntityRailgunProjectile p_110775_1_) {
        return TEXTURE;
    }
}
