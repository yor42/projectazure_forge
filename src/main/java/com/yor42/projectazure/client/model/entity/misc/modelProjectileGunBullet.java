package com.yor42.projectazure.client.model.entity.misc;
// Made with Blockbench 3.7.5
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

public class modelProjectileGunBullet extends EntityModel<Entity> {
	private final ModelRenderer bb_main;

	public modelProjectileGunBullet() {
		this.textureWidth = 16;
		this.textureHeight = 16;

		bb_main = new ModelRenderer(this);
		bb_main.setRotationPoint(0.0F, 0.5F, 0.0F);
		bb_main.setTextureOffset(0, 0).addBox(-0.5F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
	}

	@Override
	public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		bb_main.rotateAngleY = MathHelper.lerp(1, entity.prevRotationYaw, entity.rotationYaw) - 90.0F;
		bb_main.rotateAngleZ = MathHelper.lerp(1, entity.prevRotationPitch, entity.rotationPitch);
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		matrixStack.push();
		matrixStack.scale(0.5f,0.5f,0.5f);
		bb_main.render(matrixStack, buffer, packedLight, packedOverlay);
		matrixStack.pop();
	}
}