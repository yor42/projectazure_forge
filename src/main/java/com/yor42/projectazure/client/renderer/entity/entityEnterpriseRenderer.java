package com.yor42.projectazure.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yor42.projectazure.client.model.entity.kansen.enterpriseModel;
import com.yor42.projectazure.client.renderer.layer.EnterpriseRiggingLayer;
import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityEnterprise;
import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityKansenBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class entityEnterpriseRenderer extends GeoEntityRenderer<EntityEnterprise> {
    private EntityKansenBase entity;
    private IRenderTypeBuffer rtb;
    private ResourceLocation texture;

    public entityEnterpriseRenderer(EntityRendererManager renderManager) {
        super(renderManager, new enterpriseModel());
        this.addLayer(new EnterpriseRiggingLayer(this));
        this.shadowSize = 0.4F;
    }

    @Override
    public void renderEarly(EntityEnterprise animatable, MatrixStack stackIn, float ticks, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        this.rtb = renderTypeBuffer;
        this.entity = animatable;
        this.texture = this.getTextureLocation(animatable);
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
    }

    @Override
    public void render(EntityEnterprise entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn) {
        stack.push();
        stack.scale(0.4F, 0.4F, 0.4F);
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
        stack.pop();
    }

    @Override
    public ResourceLocation getEntityTexture(EntityEnterprise entity) {
        return TextureEntityLocation("modelenterprise");
    }

    @Override
    public RenderType getRenderType(EntityEnterprise animatable, float partialTicks, MatrixStack stack, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.getEntitySmoothCutout(textureLocation);
    }

    @Override
    public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (bone.getName().equals("itemMainHand")){
            stack.push();
            stack.rotate(Vector3f.XP.rotationDegrees(-90));
            ItemStack mainHandStack = this.entity.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
            stack.translate(0.7F, 0.1, 1.7F);
            stack.scale(1.5F, 1.5F, 1.5F);
            if(!mainHandStack.isEmpty()){
                Minecraft.getInstance().getItemRenderer().renderItem(mainHandStack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, packedLightIn, packedOverlayIn, stack, this.rtb);
            }
            stack.pop();
        }
        else if (bone.getName().equals("itemOffHand")){
            stack.push();
            stack.rotate(Vector3f.XP.rotationDegrees(-90));
            ItemStack mainHandStack = this.entity.getItemStackFromSlot(EquipmentSlotType.OFFHAND);
            float xvalue = -0.7F;
            if(mainHandStack.isShield(this.entity)){
                stack.rotate(Vector3f.ZP.rotationDegrees(180));
                xvalue = 0.7F;
            }
            stack.translate(xvalue, 0.2F, 1.7F);
            stack.scale(1.5F, 1.5F, 1.5F);
            if(!mainHandStack.isEmpty()){
                Minecraft.getInstance().getItemRenderer().renderItem(mainHandStack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, packedLightIn, packedOverlayIn, stack, this.rtb);
            }
            stack.pop();
        }

        bufferIn = rtb.getBuffer(RenderType.getEntitySmoothCutout(texture));
        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
