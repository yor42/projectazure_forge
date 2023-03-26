package com.yor42.projectazure.client.model.entity.misc;// Made with Blockbench 4.6.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityFallingSword;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class ModelProjectileFallingSword extends EntityModel<EntityFallingSword> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "flexsword_converted"), "main");
	private final ModelPart bone;

	public ModelProjectileFallingSword() {
		this.bone = createBodyLayer().bakeRoot();
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(9, 6).addBox(-0.5F, 7.5F, -0.9678F, 1.0F, 7.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.125F, -12.5F, -0.8678F, 0.0F, 20.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.125F, -13.45F, 0.1322F, 0.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.125F, -14.2F, 0.8322F, 0.0F, 0.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.125F, -13.55F, 0.1822F, 0.0F, 0.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.125F, -13.65F, 0.2822F, 0.0F, 0.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.125F, -13.75F, 0.3822F, 0.0F, 0.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.125F, -13.85F, 0.4822F, 0.0F, 0.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.125F, -13.95F, 0.5822F, 0.0F, 0.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.125F, -14.05F, 0.6822F, 0.0F, 0.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-0.125F, -14.15F, 0.7822F, 0.0F, 0.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 9.5F, -0.0322F, 1.5708F, 0.0F, 0.0F));

		PartDefinition cube_r1 = bone.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-0.125F, -1.2F, -0.4F, 0.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -12.3586F, -0.4436F, -0.7854F, 0.0F, 0.0F));

		PartDefinition cube_r2 = bone.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-0.125F, -0.85F, -0.4F, 0.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -13.3485F, 0.5464F, -0.7854F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(EntityFallingSword entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}