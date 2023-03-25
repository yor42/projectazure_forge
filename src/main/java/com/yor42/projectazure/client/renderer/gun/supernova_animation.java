package com.yor42.projectazure.client.renderer.gun;

import com.mojang.blaze3d.vertex.PoseStack;
import com.tac.guns.client.render.gun.IOverrideModel;
import com.yor42.projectazure.gameobject.items.GeoGunItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class supernova_animation implements IOverrideModel {
    private static final GeoItemRenderer<GeoGunItem> renderer = new SupernovaGeoRenderer();
    @Override
    public void render(float v, ItemTransforms.TransformType transformType, ItemStack itemStack, ItemStack itemStack1, LivingEntity livingEntity, PoseStack matrixStack, MultiBufferSource buffer, int i, int i1) {
        matrixStack.translate(-0.5F,-0.5F,-0.5F);
        renderer.renderByItem(itemStack, transformType, matrixStack, buffer, i, i1);
    }
}
