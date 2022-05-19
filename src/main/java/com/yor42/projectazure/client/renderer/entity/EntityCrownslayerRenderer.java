package com.yor42.projectazure.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yor42.projectazure.client.model.entity.bonus.ModelCrownslayer;
import com.yor42.projectazure.gameobject.entity.companion.bonus.EntityCrownSlayer;
import com.yor42.projectazure.setup.register.registerItems;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class EntityCrownslayerRenderer extends GeoCompanionRenderer<EntityCrownSlayer> {
    public EntityCrownslayerRenderer(EntityRendererManager renderManager) {
        super(renderManager, new ModelCrownslayer());
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull EntityCrownSlayer entity) {
        return TextureEntityLocation("modelcrownslayer");
    }

    @Nonnull
    @Override
    protected Vector3d getHandItemCoordinate() {
        return new Vector3d(0.6F, 0.1F, 1.5F);
    }

    @Override
    protected void performCustomRotationtoStack(ItemStack stack, MatrixStack matrix, Hand hand) {
        if(hand == Hand.MAIN_HAND && stack.getItem()== registerItems.TACTICAL_KNIFE.get()) {
            matrix.mulPose(Vector3f.XP.rotationDegrees(180));
            matrix.mulPose(Vector3f.YP.rotationDegrees(180));
            matrix.translate(0, 0.2, 0);
        }
    }
}
