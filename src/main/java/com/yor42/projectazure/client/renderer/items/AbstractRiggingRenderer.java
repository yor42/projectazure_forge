package com.yor42.projectazure.client.renderer.items;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yor42.projectazure.gameobject.capability.multiinv.IMultiInventory;
import com.yor42.projectazure.gameobject.capability.multiinv.MultiInvUtil;
import com.yor42.projectazure.gameobject.items.rigging.ItemRiggingBase;
import com.yor42.projectazure.gameobject.items.shipEquipment.ItemEquipmentBase;
import com.yor42.projectazure.libs.enums;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.items.IItemHandler;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;

public abstract class AbstractRiggingRenderer<T extends ItemRiggingBase> extends GeoItemRenderer<T> {

    public AbstractRiggingRenderer(AnimatedGeoModel<T> modelProvider) {
        super(modelProvider);
    }

    @Override
    public void render(T animatable, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn, ItemStack itemStack) {
        super.render(animatable, stack, bufferIn, packedLightIn, itemStack);
        Item item = itemStack.getItem();

        if(!(item instanceof ItemRiggingBase)){
            return;
        }

        ItemRiggingBase rigging = (ItemRiggingBase) item;
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

    protected void moveAndRotateMatrixToMatchBone(MatrixStack stack, GeoBone bone) {
        double x = bone.getModelPosition().x;
        double y = bone.getModelPosition().y;
        double z = bone.getModelPosition().z;

        stack.translate(x / 16, y / 16, z / 16);

        stack.mulPose(Vector3f.XP.rotationDegrees(bone.getRotationX()));
        stack.mulPose(Vector3f.YP.rotationDegrees(bone.getRotationY()));
        stack.mulPose(Vector3f.ZP.rotationDegrees(bone.getRotationZ()));
    }
}
