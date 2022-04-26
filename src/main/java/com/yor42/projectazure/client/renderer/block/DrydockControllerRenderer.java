package com.yor42.projectazure.client.renderer.block;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yor42.projectazure.client.model.block.ModelDryDockController;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.MultiblockDrydockTE;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

import javax.annotation.Nullable;
import java.util.Objects;

import static com.yor42.projectazure.gameobject.blocks.blockMultiblockDryDockController.ACTIVE;
import static com.yor42.projectazure.gameobject.blocks.blockMultiblockDryDockController.POWERED;

public class DrydockControllerRenderer extends GeoBlockRenderer<MultiblockDrydockTE> {
    private MultiblockDrydockTE entity;
    private MultiBufferSource rtb;
    private ResourceLocation texture;
    private int lightDelay=0;
    private long LastLightSwitchTime = 0;

    public DrydockControllerRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn, new ModelDryDockController());
    }

    @Override
    public void renderEarly(MultiblockDrydockTE animatable, MatrixStack stackIn, float ticks, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer  vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        this.rtb = renderTypeBuffer;
        this.entity = animatable;
        this.texture = this.getTextureLocation(animatable);
    }

    @Override
    public void renderRecursively(GeoBone bone, MatrixStack stack, VertexConsumer  bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if(this.entity.getLevel() != null) {
            boolean hasProperty = this.entity.getLevel().getBlockState(this.entity.getBlockPos()).hasProperty(ACTIVE) && this.entity.getLevel().getBlockState(this.entity.getBlockPos()).hasProperty(POWERED);
            if(hasProperty) {
                if (this.LastLightSwitchTime == 0) {
                    this.LastLightSwitchTime = System.currentTimeMillis();
                }
                boolean powered = this.entity.getLevel().getBlockState(this.entity.getBlockPos()).getValue(POWERED) || this.entity.getEnergyStorage().getEnergyStored() >= this.entity.getPowerConsumption();
                if (Objects.equals(bone.getName(), "poweredon")) {
                    bone.setHidden(!powered);
                } else if (Objects.equals(bone.getName(), "poweredoff")) {
                    bone.setHidden(powered);
                } else if (Objects.equals(bone.getName(), "blinker")) {
                    if (powered) {
                        if (System.currentTimeMillis() - this.LastLightSwitchTime >= this.lightDelay) {
                            bone.setHidden(!bone.isHidden());
                            this.lightDelay = 1000;
                            this.LastLightSwitchTime = System.currentTimeMillis();
                        }
                    } else {
                        bone.setHidden(true);
                    }
                }
                if (bone.getName().contains("activelight") || bone.getName().contains("activeeffect")) {
                    boolean flag = !(this.entity.getLevel().getBlockState(this.entity.getBlockPos()).getValue(ACTIVE) && this.entity.getLevel().getBlockState(this.entity.getBlockPos()).getValue(POWERED));
                    bone.setHidden(flag);
                }
            }
        }

        int light = packedLightIn;
        if (bone.getName().contains("activelight") || bone.getName().contains("powered") || Objects.equals(bone.getName(), "blinker") || Objects.equals(bone.getName(), "activeeffect")) {
            light = LightTexture.pack(15, 15);
        }

        bufferIn = rtb.getBuffer(RenderType.entitySmoothCutout(texture));
        super.renderRecursively(bone, stack, bufferIn, light, packedOverlayIn, red, green, blue, alpha);

    }
}
