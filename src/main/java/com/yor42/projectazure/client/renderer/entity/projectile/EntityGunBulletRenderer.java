package com.yor42.projectazure.client.renderer.entity.projectile;

import com.yor42.projectazure.gameobject.entity.projectiles.EntityProjectileBullet;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class EntityGunBulletRenderer extends EntityRenderer<EntityProjectileBullet> {

    private static final ResourceLocation TEXTURE = ModResourceLocation("textures/entity/projectile/gun_bullet.png");
    public EntityGunBulletRenderer(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityProjectileBullet entity) {
        return TEXTURE;
    }
}
