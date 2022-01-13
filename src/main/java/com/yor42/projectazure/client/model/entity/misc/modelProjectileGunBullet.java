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

public class modelProjectileGunBullet extends EntityModel<Entity> {
	private final ModelRenderer bb_main;

	public modelProjectileGunBullet() {
		this.texWidth = 16;
		this.texHeight = 16;

		bb_main = new ModelRenderer(this);
		bb_main.setPos(0.0F, 0.5F, 0.0F);
		bb_main.texOffs(0, 0).addBox(-0.5F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		bb_main.yRot = MathHelper.lerp(1, entity.yRotO, entity.yRot) - 90.0F;
		bb_main.zRot = MathHelper.lerp(1, entity.xRotO, entity.xRot);
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		matrixStack.pushPose();
		matrixStack.scale(0.5f,0.5f,0.5f);
		bb_main.render(matrixStack, buffer, packedLight, packedOverlay);
		matrixStack.popPose();
	}
}