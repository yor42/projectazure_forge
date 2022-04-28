package com.yor42.projectazure.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yor42.projectazure.client.model.block.ModelRecruitBeacon;
import com.yor42.projectazure.gameobject.blocks.RecruitBeaconBlock;
import com.yor42.projectazure.gameobject.blocks.tileentity.TileEntityRecruitBeacon;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

import javax.annotation.Nullable;
import java.util.Objects;

public class MachineRecruitBeaconRenderer extends GeoBlockRenderer<TileEntityRecruitBeacon> {

    private TileEntityRecruitBeacon entity;
    private MultiBufferSource rtb;
    private ResourceLocation texture;

    public MachineRecruitBeaconRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
        super(rendererDispatcherIn, new ModelRecruitBeacon());
    }

    @Override
    public RenderType getRenderType(TileEntityRecruitBeacon animatable, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer  vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entitySmoothCutout(textureLocation);
    }

    @Override
    public void renderEarly(TileEntityRecruitBeacon animatable, PoseStack stackIn, float ticks, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer  vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        this.rtb = renderTypeBuffer;
        this.entity = animatable;
        this.texture = this.getTextureLocation(animatable);
    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack stack, VertexConsumer  bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if(this.entity.getLevel() != null && this.entity.getLevel().getBlockState(this.entity.getBlockPos()).hasProperty(RecruitBeaconBlock.POWERED)) {
            if (Objects.equals(bone.getName(), "on")) {
                bone.setHidden(!this.entity.getLevel().getBlockState(this.entity.getBlockPos()).getValue(RecruitBeaconBlock.POWERED));
            } else if (Objects.equals(bone.getName(), "off")) {
                bone.setHidden(this.entity.getLevel().getBlockState(this.entity.getBlockPos()).getValue(RecruitBeaconBlock.POWERED));
            }
        }
        int light = packedLightIn;
        if (Objects.equals(bone.getName(), "light") || Objects.equals(bone.getName(), "emissive")) {
            light = LightTexture.pack(15, 15);
        }
        bufferIn = rtb.getBuffer(RenderType.entitySmoothCutout(texture));
        super.renderRecursively(bone, stack, bufferIn, light, packedOverlayIn, red, green, blue, alpha);
    }
}
