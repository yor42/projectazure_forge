package com.yor42.projectazure.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.yor42.projectazure.client.model.entity.kansen.nagatoModel;
import com.yor42.projectazure.client.renderer.layer.NagatoRiggingLayer;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenBase;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityNagato;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ToolActions;
import software.bernie.geckolib3.geo.render.built.GeoBone;

import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class entityNagatoRenderer extends GeoCompanionRenderer<EntityNagato> {

    private EntityKansenBase entity;
    private MultiBufferSource rtb;
    private ResourceLocation texture;

    public entityNagatoRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new nagatoModel());
        this.addLayer(new NagatoRiggingLayer(this));
        this.shadowRadius = 0.4F;
    }

    @Override
    public void renderEarly(EntityNagato animatable, PoseStack stackIn, float ticks, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer  vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        this.rtb = renderTypeBuffer;
        this.entity = animatable;
        this.texture = this.getTextureLocation(animatable);
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
    }

    @Override
    public void render(EntityNagato entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {

        stack.pushPose();
        stack.scale(0.4F, 0.4F, 0.4F);
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
        stack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityNagato entity) {
        return TextureEntityLocation("modelnagato");
    }

    @Override
    public RenderType getRenderType(EntityNagato animatable, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer  vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entitySmoothCutout(textureLocation);
    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack stack, VertexConsumer  bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (bone.getName().equals("itemMainHand")){
            stack.pushPose();
            stack.mulPose(Vector3f.XP.rotationDegrees(-90));
            ItemStack mainHandStack = this.entity.getItemBySlot(EquipmentSlot.MAINHAND);
            stack.translate(0.5F, 0.1, 1.15F);
            stack.scale(1.5F, 1.5F, 1.5F);
            if(!mainHandStack.isEmpty()){
                Item gunItem = this.entity.getGunStack().getItem();
                if(!this.entity.isReloadingMainHand() && this.entity.isUsingGun() && gunItem instanceof ItemGunBase && ((ItemGunBase)gunItem).isTwoHanded()){
                    stack.mulPose(Vector3f.XN.rotationDegrees(27.5F));
                }
                Minecraft.getInstance().getItemRenderer().renderStatic(mainHandStack, ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, packedLightIn, packedOverlayIn, stack, this.rtb, this.entity.getId());
            }
            stack.popPose();
        }
        else if (bone.getName().equals("itemOffHand")){
            stack.pushPose();
            stack.mulPose(Vector3f.XP.rotationDegrees(-90));
            ItemStack mainHandStack = this.entity.getItemBySlot(EquipmentSlot.OFFHAND);
            float xvalue = -0.5F;
            if(mainHandStack.canPerformAction(ToolActions.SHIELD_BLOCK)){
                stack.mulPose(Vector3f.ZP.rotationDegrees(180));
                xvalue = 0.5F;
            }

            stack.translate(xvalue, 0.1, 1.15F);
            stack.scale(1.5F, 1.5F, 1.5F);
            if(!mainHandStack.isEmpty()){
                Minecraft.getInstance().getItemRenderer().renderStatic(mainHandStack, ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, packedLightIn, packedOverlayIn, stack, this.rtb, this.entity.getId());
            }
            stack.popPose();
        }
        /*
        else if (bone.getName().equals("Body")){
            stack.push();
            ItemStack rigging = this.entity.getRigging();
            stack.translate(bone.getPositionX()/16, bone.getPositionY()/16, bone.getPositionZ()/16);
            if(!rigging.isEmpty()){
                Minecraft.getInstance().getItemRenderer().renderItem(rigging, ItemTransforms.TransformType.NONE, packedLightIn, packedOverlayIn, stack, this.rtb, this.entity.getId());
            }
            stack.pop();
        }

         */

        bufferIn = rtb.getBuffer(RenderType.entitySmoothCutout(texture));
        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
