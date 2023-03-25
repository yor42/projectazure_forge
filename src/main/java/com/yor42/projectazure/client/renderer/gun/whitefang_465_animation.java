package com.yor42.projectazure.client.renderer.gun;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tac.guns.client.render.gun.IOverrideModel;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.common.Gun;
import com.tac.guns.init.ModItems;
import com.tac.guns.item.attachment.IAttachment;
import com.yor42.projectazure.setup.register.registerGunAttachments;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class whitefang_465_animation implements IOverrideModel {

    public whitefang_465_animation(){}

    @Override
    public void render(float v, ItemTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity livingEntity, PoseStack matrixStack, MultiBufferSource renderBuffer, int light, int overlay) {

        RenderUtil.renderModel(registerGunAttachments.WHITEFANG465_BODY.getModel(), stack, matrixStack, renderBuffer, light, overlay);

        if (Gun.getAttachment(IAttachment.Type.BARREL, stack).getItem() == ModItems.SILENCER.orElse(ItemStack.EMPTY.getItem())) {
            RenderUtil.renderModel(registerGunAttachments.WHITEFANG465_SUPPRESSOR.getModel(), stack, matrixStack, renderBuffer, light, overlay);
        }

        if (Gun.getAttachment(IAttachment.Type.UNDER_BARREL, stack).getItem() == ModItems.SPECIALISED_GRIP.orElse(ItemStack.EMPTY.getItem())) {
            RenderUtil.renderModel(registerGunAttachments.WHITEFANG465_VERTICALGRIP.getModel(), stack, matrixStack, renderBuffer, light, overlay);
        }
    }
}
