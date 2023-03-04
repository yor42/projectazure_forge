package com.yor42.projectazure.client.renderer.gun;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.tac.guns.client.render.gun.IOverrideModel;
import com.tac.guns.client.util.RenderUtil;
import com.yor42.projectazure.gameobject.items.GeoGunItem;
import com.yor42.projectazure.setup.register.registerGunAttachments;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class supernova_animation implements IOverrideModel {
    private static final GeoItemRenderer<GeoGunItem> renderer = new SupernovaGeoRenderer();
    @Override
    public void render(float v, ItemCameraTransforms.TransformType transformType, ItemStack itemStack, ItemStack itemStack1, LivingEntity livingEntity, MatrixStack matrixStack, IRenderTypeBuffer buffer, int i, int i1) {
        matrixStack.translate(-0.5F,-0.5F,-0.5F);
        renderer.renderByItem(itemStack, transformType, matrixStack, buffer, i, i1);
    }
}
