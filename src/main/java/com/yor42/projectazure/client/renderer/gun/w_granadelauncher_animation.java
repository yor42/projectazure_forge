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

public class w_granadelauncher_animation implements IOverrideModel {

    public w_granadelauncher_animation(){}
    @Override
    public void render(float v, ItemTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity livingEntity, PoseStack matrixStack, MultiBufferSource renderBuffer, int light, int overlay) {

        RenderUtil.renderModel(registerGunAttachments.GRANADELAUNCHER_BODY.getModel(), stack, matrixStack, renderBuffer, light, overlay);


        if (Gun.getAttachment(IAttachment.Type.UNDER_BARREL, stack).getItem() == ModItems.SPECIALISED_GRIP.orElse(ItemStack.EMPTY.getItem())) {
            RenderUtil.renderModel(registerGunAttachments.GRANADELAUNCHER_T_GRIP.getModel(), stack, matrixStack, renderBuffer, light, overlay);
        }
        else if (Gun.getAttachment(IAttachment.Type.UNDER_BARREL, stack).getItem() == ModItems.LIGHT_GRIP.orElse(ItemStack.EMPTY.getItem())) {
            RenderUtil.renderModel(registerGunAttachments.GRANADELAUNCHER_L_GRIP.getModel(), stack, matrixStack, renderBuffer, light, overlay);
        }

    }
}
