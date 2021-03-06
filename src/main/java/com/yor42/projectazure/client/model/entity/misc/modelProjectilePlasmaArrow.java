package com.yor42.projectazure.client.model.entity.misc;// Made with Blockbench 3.7.5
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityPlasmaArrow;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class modelProjectilePlasmaArrow extends EntityModel<EntityPlasmaArrow> {
	private final ModelRenderer bb_main;

	public modelProjectilePlasmaArrow() {
		textureWidth = 16;
		textureHeight = 16;

		bb_main = new ModelRenderer(this);
		bb_main.setRotationPoint(0.0F, 24.0F, 0.0F);
		bb_main.setTextureOffset(10, 3).addBox(-0.5F, -0.5F, -5.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		bb_main.setTextureOffset(2, 2).addBox(-0.5F, -1.5F, -4.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);
		bb_main.setTextureOffset(0, 0).addBox(-0.5F, -0.5F, -3.5F, 1.0F, 1.0F, 7.0F, 0.0F, false);
		bb_main.setTextureOffset(1, 9).addBox(-0.5F, -1.5F, 3.5F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		bb_main.setTextureOffset(10, 9).addBox(-0.5F, 0.5F, 3.5F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		bb_main.setTextureOffset(6, 11).addBox(-0.5F, -0.5F, 3.5F, 1.0F, 1.0F, 2.0F, 0.0F, false);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(EntityPlasmaArrow entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void render(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		bb_main.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
	}
}