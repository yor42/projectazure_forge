package com.yor42.projectazure.client.model.entity.misc;// Made with Blockbench 3.9.3
// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityMissileDroneMissile;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class modelProjectileDroneMissile extends EntityModel<EntityMissileDroneMissile> {
	private final ModelRenderer Body;

	public modelProjectileDroneMissile() {
		textureWidth = 64;
		textureHeight = 64;

		Body = new ModelRenderer(this);
		Body.setRotationPoint(0.0F, 24.0F, 0.0F);
		Body.setTextureOffset(1, 24).addBox(-1.5F, -1.5F, -10.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
		Body.setTextureOffset(1, 1).addBox(-2.0F, -2.0F, -9.0F, 4.0F, 4.0F, 18.0F, 0.0F, false);
		Body.setTextureOffset(8, 2).addBox(-2.0F, -2.0F, 9.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);
		Body.setTextureOffset(8, 10).addBox(-2.0F, 1.0F, 9.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);
		Body.setTextureOffset(8, 5).addBox(1.0F, -1.0F, 9.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		Body.setTextureOffset(14, 5).addBox(-2.0F, -1.0F, 9.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		Body.setTextureOffset(2, 29).addBox(-0.5F, -3.0F, 4.0F, 1.0F, 1.0F, 6.0F, 0.0F, false);
		Body.setTextureOffset(32, 29).addBox(-3.0F, -0.5F, 4.0F, 1.0F, 1.0F, 6.0F, 0.0F, false);
		Body.setTextureOffset(2, 37).addBox(-0.5F, -4.0F, 5.0F, 1.0F, 1.0F, 5.0F, 0.0F, false);
		Body.setTextureOffset(28, 37).addBox(-4.0F, -0.5F, 5.0F, 1.0F, 1.0F, 5.0F, 0.0F, false);
		Body.setTextureOffset(2, 44).addBox(-0.5F, -5.0F, 6.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
		Body.setTextureOffset(24, 44).addBox(-5.0F, -0.5F, 6.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
		Body.setTextureOffset(47, 29).addBox(2.0F, -0.5F, 4.0F, 1.0F, 1.0F, 6.0F, 0.0F, true);
		Body.setTextureOffset(41, 37).addBox(3.0F, -0.5F, 5.0F, 1.0F, 1.0F, 5.0F, 0.0F, true);
		Body.setTextureOffset(35, 44).addBox(4.0F, -0.5F, 6.0F, 1.0F, 1.0F, 4.0F, 0.0F, true);
		Body.setTextureOffset(17, 29).addBox(-0.5F, 2.0F, 4.0F, 1.0F, 1.0F, 6.0F, 0.0F, false);
		Body.setTextureOffset(15, 37).addBox(-0.5F, 3.0F, 5.0F, 1.0F, 1.0F, 5.0F, 0.0F, false);
		Body.setTextureOffset(13, 44).addBox(-0.5F, 4.0F, 6.0F, 1.0F, 1.0F, 4.0F, 0.0F, false);
	}

	@Override
	public void setRotationAngles(EntityMissileDroneMissile entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		Body.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}