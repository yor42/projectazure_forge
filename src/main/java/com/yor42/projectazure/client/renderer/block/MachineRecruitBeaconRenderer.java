package com.yor42.projectazure.client.renderer.block;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yor42.projectazure.client.model.block.ModelRecruitBeacon;
import com.yor42.projectazure.gameobject.blocks.RecruitBeaconBlock;
import com.yor42.projectazure.gameobject.blocks.tileentity.TileEntityRecruitBeacon;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

import javax.annotation.Nullable;
import java.util.Objects;

public class MachineRecruitBeaconRenderer extends GeoBlockRenderer<TileEntityRecruitBeacon> {

    private TileEntityRecruitBeacon entity;
    private IRenderTypeBuffer rtb;
    private ResourceLocation texture;

    public MachineRecruitBeaconRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn, new ModelRecruitBeacon());
    }

    @Override
    public RenderType getRenderType(TileEntityRecruitBeacon animatable, float partialTicks, MatrixStack stack, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.getEntitySmoothCutout(textureLocation);
    }

    @Override
    public void renderEarly(TileEntityRecruitBeacon animatable, MatrixStack stackIn, float ticks, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        this.rtb = renderTypeBuffer;
        this.entity = animatable;
        this.texture = this.getTextureLocation(animatable);
    }

    @Override
    public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if(this.entity.getWorld() != null && this.entity.getWorld().getBlockState(this.entity.getPos()).hasProperty(RecruitBeaconBlock.POWERED)) {
            if (Objects.equals(bone.getName(), "on")) {
                bone.setHidden(!this.entity.getWorld().getBlockState(this.entity.getPos()).get(RecruitBeaconBlock.POWERED));
            } else if (Objects.equals(bone.getName(), "off")) {
                bone.setHidden(this.entity.getWorld().getBlockState(this.entity.getPos()).get(RecruitBeaconBlock.POWERED));
            }
        }
        int light = packedLightIn;
        if (Objects.equals(bone.getName(), "light") || Objects.equals(bone.getName(), "emissive")) {
            light = LightTexture.packLight(15, 15);
        }
        bufferIn = rtb.getBuffer(RenderType.getEntitySmoothCutout(texture));
        super.renderRecursively(bone, stack, bufferIn, light, packedOverlayIn, red, green, blue, alpha);
    }
}
