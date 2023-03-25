package com.yor42.projectazure.client.model.entity.misc;// Made with Blockbench 3.7.5
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityPlasmaArrow;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;

public class modelProjectilePlasmaArrow extends EntityModel<EntityPlasmaArrow> {
	private final ModelPart bb_main;

	public modelProjectilePlasmaArrow() {
		texWidth = 16;
		texHeight = 16;

		bb_main = new ModelPart(this);
		bb_main.setPos(0.0F, 24.0F, 0.0F);
		bb_main.texOffs(10, 3).addBox(-0.5F, -0.5F, -5.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		bb_main.texOffs(2, 2).addBox(-0.5F, -1.5F, -4.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);
		bb_main.texOffs(0, 0).addBox(-0.5F, -0.5F, -3.5F, 1.0F, 1.0F, 7.0F, 0.0F, false);
		bb_main.texOffs(1, 9).addBox(-0.5F, -1.5F, 3.5F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		bb_main.texOffs(10, 9).addBox(-0.5F, 0.5F, 3.5F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		bb_main.texOffs(6, 11).addBox(-0.5F, -0.5F, 3.5F, 1.0F, 1.0F, 2.0F, 0.0F, false);
	}

	public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}

	@Override
	public void setupAnim(EntityPlasmaArrow entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		bb_main.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn);
	}
}