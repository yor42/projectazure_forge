package com.yor42.projectazure.client.renderer.gun;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.client.render.gun.IOverrideModel;
import com.tac.guns.client.util.RenderUtil;
import com.yor42.projectazure.setup.register.registerGunAttachments;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class sangvis_railgun_animation  implements IOverrideModel {
    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack itemStack, ItemStack itemStack1, LivingEntity livingEntity, MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int i, int i1) {
        if(itemStack.getOrCreateTag().getInt("CurrentFireMode") == 0){
            RenderUtil.renderModel(registerGunAttachments.SANGVIS_RAILGUN_SAFE.getModel(), itemStack, matrixStack, iRenderTypeBuffer, i, i1);
        }
        else{
            RenderUtil.renderModel(registerGunAttachments.SANGVIS_RAILGUN_BODY.getModel(), itemStack, matrixStack, iRenderTypeBuffer, i, i1);
        }
    }
}
