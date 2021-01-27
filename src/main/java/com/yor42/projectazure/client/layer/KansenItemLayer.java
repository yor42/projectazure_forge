package com.yor42.projectazure.client.layer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yor42.projectazure.gameobject.entity.EntityAyanami;
import com.yor42.projectazure.gameobject.entity.EntityKansenBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class KansenItemLayer<T extends EntityKansenBase & IAnimatable> extends GeoLayerRenderer implements IGeoRenderer<T> {

    public void translateRotate(MatrixStack matrixStackIn, IBone bone) {
        matrixStackIn.translate((double)(bone.getRotationX() / 16.0F), (double)(bone.getRotationY() / 16.0F), (double)(bone.getRotationZ() / 16.0F));
        if (bone.getRotationZ() != 0.0F) {
            matrixStackIn.rotate(Vector3f.ZP.rotation(bone.getRotationZ()));
        }

        if (bone.getRotationY() != 0.0F) {
            matrixStackIn.rotate(Vector3f.YP.rotation(bone.getRotationY()));
        }

        if (bone.getRotationX() != 0.0F) {
            matrixStackIn.rotate(Vector3f.XP.rotation(bone.getRotationX()));
        }

    }

    static
    {
        AnimationController.addModelFetcher((IAnimatable object) ->
        {
            if (object instanceof Entity)
            {

            }
            return null;
        });
    }

    public KansenItemLayer(IGeoRenderer entityRendererIn) {
        super(entityRendererIn);
    }

    private void renderMainHand(EntityKansenBase entity, ItemStack stackInSlot, ItemCameraTransforms.TransformType camera, MatrixStack matrix, IRenderTypeBuffer buffer, int i) {
        if(!stackInSlot.isEmpty()){
            matrix.push();

            matrix.rotate(Vector3f.XP.rotationDegrees(stackInSlot.getItem() == Items.CROSSBOW ? -90f : -75f));
            matrix.rotate(Vector3f.YP.rotationDegrees(180f));
            Minecraft.getInstance().getFirstPersonRenderer().renderItemSide(entity, stackInSlot, camera, false, matrix, buffer, i);
            matrix.pop();
        }
    }


    @Override
    public GeoModelProvider getGeoModelProvider() {
        return null;
    }

    @Override
    public ResourceLocation getTextureLocation(T t) {
        return null;
    }

    @Override
    public void render(MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int i, Entity entity, float v, float v1, float v2, float v3, float v4, float v5) {
    }
}
