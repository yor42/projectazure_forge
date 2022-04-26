package com.yor42.projectazure.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yor42.projectazure.client.model.entity.bonus.ModelTalulah;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.gameobject.entity.companion.bonus.EntityTalulah;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.inventory.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib3.geo.render.built.GeoBone;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class EntityTalulahRenderer extends GeoCompanionRenderer<EntityTalulah> {

    private AbstractEntityCompanion entity;
    private MultiBufferSource rtb;
    private ResourceLocation texture;

    public EntityTalulahRenderer(EntityRendererManager renderManager) {
        super(renderManager, new ModelTalulah());
        this.shadowRadius = 0.4F;
    }

    @Nonnull
    @Override
    public ResourceLocation getTextureLocation(@Nonnull EntityTalulah entity) {
        return TextureEntityLocation("modeltalulah");
    }

    @Override
    public void renderEarly(EntityTalulah animatable, MatrixStack stackIn, float ticks, MultiBufferSource renderTypeBuffer, VertexConsumer  vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
        this.rtb = renderTypeBuffer;
        this.entity = animatable;
        this.texture = this.getTextureLocation(animatable);
    }

    @Override
    public void render(EntityTalulah entity, float entityYaw, float partialTicks, MatrixStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        stack.pushPose();
        stack.scale(0.4F, 0.4F, 0.4F);
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
        stack.popPose();
    }

    @Override
    public RenderType getRenderType(EntityTalulah animatable, float partialTicks, MatrixStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer  vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entitySmoothCutout(textureLocation);
    }

    @Override
    public void renderRecursively(GeoBone bone, MatrixStack stack, VertexConsumer  bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (bone.getName().equals("itemMainHand")){
            stack.pushPose();
            stack.mulPose(Vector3f.XP.rotationDegrees(-90));
            ItemStack mainHandStack = this.entity.getItemBySlot(EquipmentSlot.MAINHAND);
            stack.translate(0.6F, 0.1, 1.5F);
            stack.mulPose(new Quaternion(bone.getRotationX(), bone.getRotationY(), bone.getRotationZ(),false));
            stack.scale(1.5F, 1.5F, 1.5F);
            if(!mainHandStack.isEmpty()){
                Item gunItem = this.entity.getGunStack().getItem();
                if(!this.entity.isReloadingMainHand() && this.entity.isUsingGun() && gunItem instanceof ItemGunBase && ((ItemGunBase)gunItem).isTwoHanded()){
                    stack.mulPose(Vector3f.XN.rotationDegrees(27.5F));
                }
                Minecraft.getInstance().getItemRenderer().renderStatic(mainHandStack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, packedLightIn, packedOverlayIn, stack, this.rtb);
            }
            stack.popPose();
        }
        else if (bone.getName().equals("itemOffHand")){
            stack.pushPose();
            stack.mulPose(Vector3f.XP.rotationDegrees(-90));
            ItemStack mainHandStack = this.entity.getItemBySlot(EquipmentSlot.OFFHAND);
            float xvalue = -0.7F;
            if(mainHandStack.isShield(this.entity)){
                stack.mulPose(Vector3f.ZP.rotationDegrees(180));
                xvalue = 0.7F;
            }
            stack.translate(xvalue, 0.2F, 1.7F);
            stack.scale(1.5F, 1.5F, 1.5F);
            if(!mainHandStack.isEmpty()){
                Minecraft.getInstance().getItemRenderer().renderStatic(mainHandStack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, packedLightIn, packedOverlayIn, stack, this.rtb);
            }
            stack.popPose();
        }

        bufferIn = rtb.getBuffer(RenderType.entitySmoothCutout(texture));
        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
