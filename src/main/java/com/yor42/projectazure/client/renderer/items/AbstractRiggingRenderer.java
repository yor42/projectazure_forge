package com.yor42.projectazure.client.renderer.items;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public abstract class AbstractRiggingRenderer<T extends ItemRiggingBase> extends GeoItemRenderer<T> {

    public AbstractRiggingRenderer(AnimatedGeoModel<T> modelProvider) {
        super(modelProvider);
    }

    @Override
    public void render(T animatable, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn, ItemStack itemStack) {
        super.render(animatable, stack, bufferIn, packedLightIn, itemStack);
        Item item = itemStack.getItem();

        if(!(item instanceof ItemRiggingBase rigging)){
            return;
        }

        stack.pushPose();
        GeoModel riggingmodel = modelProvider.getModel(modelProvider.getModelLocation(animatable));
        riggingmodel.getBone("rigging").ifPresent((bone)->{
            this.moveAndRotateMatrixToMatchBone(stack, bone);
        });

        stack.translate(0, 0.01f, 0);
        stack.translate(0.5, 0.5, 0.5);
        rigging.RenderEquipments(itemStack, riggingmodel, stack, bufferIn, packedLightIn, 0,0,0,0,0,0);
        stack.popPose();
    }

    protected void moveAndRotateMatrixToMatchBone(PoseStack stack, GeoBone bone) {
        double x = bone.getModelPosition().x;
        double y = bone.getModelPosition().y;
        double z = bone.getModelPosition().z;

        stack.translate(x / 16, y / 16, z / 16);

        stack.mulPose(Vector3f.XP.rotationDegrees(bone.getRotationX()));
        stack.mulPose(Vector3f.YP.rotationDegrees(bone.getRotationY()));
        stack.mulPose(Vector3f.ZP.rotationDegrees(bone.getRotationZ()));
    }
}
