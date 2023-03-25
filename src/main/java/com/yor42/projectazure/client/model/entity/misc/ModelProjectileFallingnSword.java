package com.yor42.projectazure.client.model.entity.misc;// Made with Blockbench 4.6.2
// Exported for Minecraft version 1.15 - 1.16 with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityFallingSword;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;

public class ModelProjectileFallingnSword extends EntityModel<EntityFallingSword> {
	private final ModelPart bone;
	private final ModelPart cube_r1;
	private final ModelPart cube_r2;

	public ModelProjectileFallingnSword() {
		texWidth = 32;
		texHeight = 32;

		bone = new ModelPart(this);
		bone.setPos(0.0F, 13.0F, 0.0F);
		setRotationAngle(bone, 1.5708F, 0.0F, 0.0F);
		bone.texOffs(9, 6).addBox(-0.5F, 7.0F, -1.0F, 1.0F, 7.0F, 2.0F, 0.0F, false);
		bone.texOffs(0, 0).addBox(-0.125F, -13.0F, -0.9F, 0.0F, 20.0F, 1.0F, 0.0F, false);
		bone.texOffs(1, 1).addBox(-0.125F, -13.95F, 0.1F, 0.0F, 2.0F, 0.0F, 0.0F, false);
		bone.texOffs(1, 1).addBox(-0.125F, -14.7F, 0.8F, 0.0F, 0.0F, 0.0F, 0.0F, false);
		bone.texOffs(1, 3).addBox(-0.125F, -14.05F, 0.15F, 0.0F, 0.0F, 0.0F, 0.0F, false);
		bone.texOffs(1, 3).addBox(-0.125F, -14.15F, 0.25F, 0.0F, 0.0F, 0.0F, 0.0F, false);
		bone.texOffs(1, 3).addBox(-0.125F, -14.25F, 0.35F, 0.0F, 0.0F, 0.0F, 0.0F, false);
		bone.texOffs(1, 3).addBox(-0.125F, -14.35F, 0.45F, 0.0F, 0.0F, 0.0F, 0.0F, false);
		bone.texOffs(1, 3).addBox(-0.125F, -14.45F, 0.55F, 0.0F, 0.0F, 0.0F, 0.0F, false);
		bone.texOffs(1, 3).addBox(-0.125F, -14.55F, 0.65F, 0.0F, 0.0F, 0.0F, 0.0F, false);
		bone.texOffs(1, 0).addBox(-0.125F, -14.65F, 0.75F, 0.0F, 0.0F, 0.0F, 0.0F, false);

		cube_r1 = new ModelPart(this);
		cube_r1.setPos(0.0F, -12.8586F, -0.4757F);
		bone.addChild(cube_r1);
		setRotationAngle(cube_r1, -0.7854F, 0.0F, 0.0F);
		cube_r1.texOffs(1, 0).addBox(-0.125F, -1.2F, -0.4F, 0.0F, 1.0F, 0.0F, 0.0F, false);

		cube_r2 = new ModelPart(this);
		cube_r2.setPos(0.0F, -13.8485F, 0.5142F);
		bone.addChild(cube_r2);
		setRotationAngle(cube_r2, -0.7854F, 0.0F, 0.0F);
		cube_r2.texOffs(1, 1).addBox(-0.125F, -0.85F, -0.4F, 0.0F, 1.0F, 0.0F, 0.0F, false);
	}

	@Override
	public void setupAnim(EntityFallingSword entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//this.bone.xRot = headPitch*-1 * ((float)Math.PI / 180F);
		//this.bone.yRot = (netHeadYaw+90) * ((float)Math.PI / 180F);
	}

	@Override
	public void renderToBuffer(PoseStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		bone.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}
}