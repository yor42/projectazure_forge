package com.yor42.projectazure.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yor42.projectazure.client.model.entity.kansen.Z23Model;
import com.yor42.projectazure.client.renderer.layer.Z23RiggingLayer;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityKansenBase;
import com.yor42.projectazure.gameobject.entity.companion.ships.EntityZ23;
import com.yor42.projectazure.gameobject.items.gun.ItemGunBase;
import com.yor42.projectazure.libs.Constants;
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

import javax.annotation.Nullable;

public class entityZ23Renderer extends GeoCompanionRenderer<EntityZ23> {

    public entityZ23Renderer(EntityRendererManager renderManager) {
        super(renderManager, new Z23Model());
        this.addLayer(new Z23RiggingLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(EntityZ23 entity) {
        return new ResourceLocation(Constants.MODID, "textures/entity/entitynimi.png");
    }
    @Override
    public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (bone.getName().equals("itemMainHand")){
            stack.pushPose();
            stack.mulPose(Vector3f.XP.rotationDegrees(-90));
            ItemStack mainHandStack = this.entity.getItemBySlot(EquipmentSlotType.MAINHAND);
            stack.translate(0.6F, 0.1, 1.35F);
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
            ItemStack mainHandStack = this.entity.getItemBySlot(EquipmentSlotType.OFFHAND);
            float xvalue = -0.6F;
            if(mainHandStack.isShield(this.entity)){
                stack.mulPose(Vector3f.ZP.rotationDegrees(180));
                xvalue = 0.6F;
            }
            stack.translate(xvalue, 0.1, 1.35F);
            stack.scale(1.5F, 1.5F, 1.5F);
            if(!mainHandStack.isEmpty()){
                Minecraft.getInstance().getItemRenderer().renderStatic(mainHandStack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, packedLightIn, packedOverlayIn, stack, this.rtb);
            }
            stack.popPose();
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

        bufferIn = rtb.getBuffer(RenderType.entitySmoothCutout(texture));
        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

}
