package com.yor42.projectazure.client.renderer.items;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yor42.projectazure.client.model.rigging.modelDDRiggingDefault;
import com.yor42.projectazure.gameobject.entity.EntityKansenBase;
import com.yor42.projectazure.gameobject.items.ItemRiggingBase;
import com.yor42.projectazure.gameobject.items.itemRiggingDDDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

import javax.annotation.Nullable;

public class DDDefaultRiggingRenderer extends GeoItemRenderer<itemRiggingDDDefault> {

    private itemRiggingDDDefault rigging;
    private IRenderTypeBuffer rtb;
    private ResourceLocation texture;

    @Override
    public void renderEarly(itemRiggingDDDefault animatable, MatrixStack stackIn, float ticks, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        this.rigging = animatable;
        this.rtb = renderTypeBuffer;
        this.texture = this.getTextureLocation(animatable);
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
    }

    public DDDefaultRiggingRenderer() {
        super(new modelDDRiggingDefault());
    }

    @Override
    public RenderType getRenderType(itemRiggingDDDefault animatable, float partialTicks, MatrixStack stack, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.getEntitySmoothCutout(textureLocation);
    }

    @Override
    public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        /*
        if(bone.getName().equals("torpedomountL")){
            stack.push();
            ItemStack torpedo1stack = this.rigging.getEquipments().getStackInSlot(3);
            if(!torpedo1stack.isEmpty()){
                stack.translate(11.5/16, 21.5/16,11.5/16);
                stack.rotate(Vector3f.YP.rotationDegrees(-90));
                stack.rotate(Vector3f.XP.rotationDegrees(-90));
                Minecraft.getInstance().getItemRenderer().renderItem(torpedo1stack, ItemCameraTransforms.TransformType.NONE, packedLightIn, packedOverlayIn, stack, this.rtb);
            }
            stack.pop();


        }
        bufferIn = rtb.getBuffer(RenderType.getEntitySmoothCutout(texture));
        */
        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
