package com.yor42.projectazure.client.model.entity.misc;// Made with Blockbench 4.2.4
// Exported for Minecraft version 1.15 - 1.16 with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityArtsProjectile;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

import javax.annotation.Nonnull;

public class ModelArtsProjectile extends EntityModel<EntityArtsProjectile> {
	private final ModelRenderer bone;

	public ModelArtsProjectile() {
		texWidth = 32;
		texHeight = 32;

		bone = new ModelRenderer(this);
		bone.setPos(0.0F, 4.0F, 0.0F);
		bone.texOffs(0, 0).addBox(-2.5F, -2.5F, -1.5F, 5.0F, 5.0F, 7.0F, 0.0F, false);
		bone.texOffs(12, 12).addBox(-1.0F, -1.0F, -5.5F, 2.0F, 2.0F, 2.0F, 0.0F, false);
		bone.texOffs(0, 12).addBox(-2.0F, -2.0F, -3.5F, 4.0F, 4.0F, 2.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(@Nonnull EntityArtsProjectile entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
		this.bone.xRot = headPitch*-1 * ((float)Math.PI / 180F);
		this.bone.yRot = (netHeadYaw+90) * ((float)Math.PI / 180F);
	}

	@Override
	public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		bone.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}