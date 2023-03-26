// Made with Blockbench 4.6.5
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yor42.projectazure.gameobject.entity.projectiles.EntityMissileDroneMissile;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class modelProjectileDroneMissile extends EntityModel<EntityMissileDroneMissile> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("modid", "missiledrone"), "main");
	private final ModelPart Body;

	public modelProjectileDroneMissile(ModelPart root) {
		this.Body = createBodyLayer().bakeRoot();
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(1, 24).addBox(-1.5F, -1.5F, -10.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(1, 1).addBox(-2.0F, -2.0F, -9.0F, 4.0F, 4.0F, 18.0F, new CubeDeformation(0.0F))
		.texOffs(8, 2).addBox(-2.0F, -2.0F, 9.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(8, 10).addBox(-2.0F, 1.0F, 9.0F, 4.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(8, 5).addBox(1.0F, -1.0F, 9.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(14, 5).addBox(-2.0F, -1.0F, 9.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(2, 29).addBox(-0.5F, -3.0F, 4.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(32, 29).addBox(-3.0F, -0.5F, 4.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(2, 37).addBox(-0.5F, -4.0F, 5.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(28, 37).addBox(-4.0F, -0.5F, 5.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(2, 44).addBox(-0.5F, -5.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(24, 44).addBox(-5.0F, -0.5F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(47, 29).mirror().addBox(2.0F, -0.5F, 4.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(41, 37).mirror().addBox(3.0F, -0.5F, 5.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(35, 44).mirror().addBox(4.0F, -0.5F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(17, 29).addBox(-0.5F, 2.0F, 4.0F, 1.0F, 1.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(15, 37).addBox(-0.5F, 3.0F, 5.0F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(13, 44).addBox(-0.5F, 4.0F, 6.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 16.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(EntityMissileDroneMissile entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}