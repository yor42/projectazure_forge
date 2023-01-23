package com.yor42.projectazure.client.renderer.gun;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.client.render.gun.IOverrideModel;
import com.tac.guns.client.util.RenderUtil;
import com.tac.guns.common.Gun;
import com.tac.guns.init.ModItems;
import com.tac.guns.item.attachment.IAttachment;
import com.yor42.projectazure.setup.register.registerGunAttachments;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class typhoon_animation implements IOverrideModel {

    public typhoon_animation(){}

    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack stack, ItemStack parent, LivingEntity livingEntity, MatrixStack matrixStack, IRenderTypeBuffer renderBuffer, int light, int overlay) {

        RenderUtil.renderModel(registerGunAttachments.TYPHOON_BODY.getModel(), stack, matrixStack, renderBuffer, light, overlay);


        if (Gun.getAttachment(IAttachment.Type.UNDER_BARREL, stack).getItem() == ModItems.SPECIALISED_GRIP.orElse(ItemStack.EMPTY.getItem())) {
            RenderUtil.renderModel(registerGunAttachments.TYPHOON_T_GRIP.getModel(), stack, matrixStack, renderBuffer, light, overlay);
        }
        else if (Gun.getAttachment(IAttachment.Type.UNDER_BARREL, stack).getItem() == ModItems.LIGHT_GRIP.orElse(ItemStack.EMPTY.getItem())) {
            RenderUtil.renderModel(registerGunAttachments.TYPHOON_L_GRIP.getModel(), stack, matrixStack, renderBuffer, light, overlay);
        }

    }
}
