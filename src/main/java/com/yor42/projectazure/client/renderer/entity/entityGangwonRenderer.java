package com.yor42.projectazure.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yor42.projectazure.client.model.entity.kansen.gangwonModel;
import com.yor42.projectazure.client.renderer.layer.GangwonRiggingLayer;
import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityGangwon;
import com.yor42.projectazure.gameobject.entity.companion.kansen.EntityKansenBase;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

import static com.yor42.projectazure.libs.utils.ResourceUtils.TextureEntityLocation;

public class entityGangwonRenderer extends GeoEntityRenderer<EntityGangwon> {

    private EntityKansenBase entity;
    private IRenderTypeBuffer rtb;
    private ResourceLocation texture;

    public entityGangwonRenderer(EntityRendererManager renderManager) {
        super(renderManager, new gangwonModel());
        this.addLayer(new GangwonRiggingLayer(this));
        this.shadowSize = 0.4F;
    }

    @Override
    public void renderEarly(EntityGangwon animatable, MatrixStack stackIn, float ticks, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        this.rtb = renderTypeBuffer;
        this.entity = animatable;
        this.texture = this.getTextureLocation(animatable);
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
    }

    @Override
    public void render(EntityGangwon entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn) {

        stack.push();
        stack.scale(0.4F, 0.4F, 0.4F);
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
        stack.pop();
    }

    @Override
    public ResourceLocation getEntityTexture(EntityGangwon entity) {
        return TextureEntityLocation("modelgangwon");
    }

    @Override
    public RenderType getRenderType(EntityGangwon animatable, float partialTicks, MatrixStack stack, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.getEntitySmoothCutout(textureLocation);
    }

    @Override
    public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (bone.getName().equals("itemMainHand")){
            stack.push();
            stack.rotate(Vector3f.XP.rotationDegrees(-90));
            ItemStack mainHandStack = this.entity.getItemStackFromSlot(EquipmentSlotType.MAINHAND);
            stack.translate(0.5F, 0.1, 1.15F);
            stack.scale(1.5F, 1.5F, 1.5F);
            if(!mainHandStack.isEmpty()){
                Item gunItem = this.entity.getGunStack().getItem();
                if(!this.entity.isReloadingMainHand() && this.entity.isUsingGun() && gunItem instanceof ItemGunBase && ((ItemGunBase)gunItem).isTwoHanded()){
                    stack.rotate(Vector3f.XN.rotationDegrees(27.5F));
                }
                Minecraft.getInstance().getItemRenderer().renderItem(mainHandStack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, packedLightIn, packedOverlayIn, stack, this.rtb);
            }
            stack.pop();
        }
        else if (bone.getName().equals("itemOffHand")){
            stack.push();
            stack.rotate(Vector3f.XP.rotationDegrees(-90));
            ItemStack mainHandStack = this.entity.getItemStackFromSlot(EquipmentSlotType.OFFHAND);
            float xvalue = -0.5F;
            if(mainHandStack.isShield(this.entity)){
                stack.rotate(Vector3f.ZP.rotationDegrees(180));
                xvalue = 0.5F;
            }

            stack.translate(xvalue, 0.1, 1.15F);
            stack.scale(1.5F, 1.5F, 1.5F);
            if(!mainHandStack.isEmpty()){
                Minecraft.getInstance().getItemRenderer().renderItem(mainHandStack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, packedLightIn, packedOverlayIn, stack, this.rtb);
            }
            stack.pop();
        }
        /*
        else if (bone.getName().equals("Body")){
            stack.push();
            ItemStack rigging = this.entity.getRigging();
            stack.translate(bone.getPositionX()/16, bone.getPositionY()/16, bone.getPositionZ()/16);
            if(!rigging.isEmpty()){
                Minecraft.getInstance().getItemRenderer().renderItem(rigging, ItemCameraTransforms.TransformType.NONE, packedLightIn, packedOverlayIn, stack, this.rtb);
            }
            stack.pop();
        }

         */

        bufferIn = rtb.getBuffer(RenderType.getEntitySmoothCutout(texture));
        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
