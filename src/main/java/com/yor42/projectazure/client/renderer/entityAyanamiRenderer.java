package com.yor42.projectazure.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yor42.projectazure.client.layer.KansenItemLayer;
import com.yor42.projectazure.client.model.ayanamiModel;
import com.yor42.projectazure.gameobject.entity.EntityAyanami;
import com.yor42.projectazure.gameobject.entity.EntityKansenBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.client.renderer.entity.model.ArmorStandArmorModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

import static com.yor42.projectazure.gameobject.entity.EntityKansenBase.STORAGE;

public class entityAyanamiRenderer extends GeoEntityRenderer<EntityAyanami> {

    private EntityKansenBase entity;
    private IRenderTypeBuffer rtb;
    private ResourceLocation texture;

    @Override
    public void renderEarly(EntityAyanami animatable, MatrixStack stackIn, float ticks, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        this.rtb = renderTypeBuffer;
        this.entity = animatable;
        this.texture = this.getTextureLocation(animatable);
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
    }

    public entityAyanamiRenderer(EntityRendererManager renderManager/*, AnimatedGeoModel<EntityAyanami> modelProvider*/) {
        super(renderManager, new ayanamiModel());
        this.shadowSize = 0.3F; //change 0.7 to the desired shadow size.
    }

    @Override
    public void render(EntityAyanami entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn) {
        stack.scale(0.4F, 0.4F, 0.4F);
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }

    @Override
    protected void applyRotations(EntityAyanami entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
    }

    @Override
    public RenderType getRenderType(EntityAyanami animatable, float partialTicks, MatrixStack stack, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.getEntitySmoothCutout(textureLocation);
    }

    @Override
    public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (bone.getName().equals("itemMainHand")){
            stack.push();
            stack.rotate(Vector3f.XP.rotationDegrees(-90));
            ItemStack mainHandStack = this.entity.getDataManager().get(EntityKansenBase.ITEM_MAINHAND);
            stack.translate(0.6F, 0.1, 1.35F);
            stack.scale(1.5F, 1.5F, 1.5F);
            if(!mainHandStack.isEmpty()){
                Minecraft.getInstance().getItemRenderer().renderItem(mainHandStack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, packedLightIn, packedOverlayIn, stack, this.rtb);
            }
            stack.pop();
        }
        else if (bone.getName().equals("itemOffHand")){
            stack.push();
            stack.rotate(Vector3f.XP.rotationDegrees(-90));
            ItemStack mainHandStack = this.entity.getDataManager().get(EntityKansenBase.ITEM_OFFHAND);
            stack.translate(0.6F, 0.1, 1.35F);
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

