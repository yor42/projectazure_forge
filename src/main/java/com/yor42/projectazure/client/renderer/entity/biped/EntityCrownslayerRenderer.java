package com.yor42.projectazure.client.renderer.entity.biped;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yor42.projectazure.client.model.entity.bonus.ModelCrownslayer;
import com.yor42.projectazure.client.renderer.entity.GeoCompanionRenderer;
import com.yor42.projectazure.gameobject.entity.companion.bonus.EntityCrownSlayer;
import com.yor42.projectazure.setup.register.RegisterItems;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import com.mojang.math.Vector3f;

import javax.annotation.Nonnull;

import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class EntityCrownslayerRenderer extends GeoCompanionRenderer<EntityCrownSlayer> {
    public EntityCrownslayerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ModelCrownslayer());
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull EntityCrownSlayer entity) {
        return TextureEntityLocation("modelcrownslayer");
    }

    @Override
    protected void performCustomRotationtoStack(ItemStack stack, PoseStack matrix, InteractionHand hand) {
        if(hand == InteractionHand.MAIN_HAND && stack.getItem()== RegisterItems.TACTICAL_KNIFE.get()) {
            matrix.mulPose(Vector3f.XP.rotationDegrees(180));
            matrix.mulPose(Vector3f.YP.rotationDegrees(180));
            matrix.translate(0, 0.2, 0);
        }
    }
}
