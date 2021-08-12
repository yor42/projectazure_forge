package com.yor42.projectazure.client.renderer.block;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.yor42.projectazure.client.model.block.ModelDryDockController;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.MultiblockDrydockTE;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
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
    private IRenderTypeBuffer rtb;
    private ResourceLocation texture;
    private int lightDelay=0;
    private long LastLightSwitchTime = 0;

    public DrydockControllerRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn, new ModelDryDockController());
    }

    @Override
    public void renderEarly(MultiblockDrydockTE animatable, MatrixStack stackIn, float ticks, @Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        this.rtb = renderTypeBuffer;
        this.entity = animatable;
        this.texture = this.getTextureLocation(animatable);
    }

    @Override
    public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

        if(this.entity.getWorld() != null && this.entity.getWorld().getBlockState(this.entity.getPos()).hasProperty(ACTIVE) && this.entity.getWorld().getBlockState(this.entity.getPos()).hasProperty(POWERED)) {
            if(this.LastLightSwitchTime == 0){
                this.LastLightSwitchTime = System.currentTimeMillis();
            }
            boolean powered = this.entity.getWorld().getBlockState(this.entity.getPos()).get(POWERED) || this.entity.getEnergyStorage().getEnergyStored()>=this.entity.getPowerConsumption();
            if (Objects.equals(bone.getName(), "poweredon")) {
                bone.setHidden(!powered);
            } else if (Objects.equals(bone.getName(), "poweredoff")) {
                bone.setHidden(powered);
            }else if(Objects.equals(bone.getName(), "blinker")) {
                if(powered) {
                    if (System.currentTimeMillis() - this.LastLightSwitchTime >= this.lightDelay) {
                        bone.setHidden(!bone.isHidden());
                        this.lightDelay = 1000;
                        this.LastLightSwitchTime = System.currentTimeMillis();
                    }
                }
                else{
                    bone.setHidden(true);
                }
            }
            if (bone.getName().contains("activelight") || bone.getName().contains("activeeffect")) {
                boolean flag = !(this.entity.getWorld().getBlockState(this.entity.getPos()).get(ACTIVE) && this.entity.getWorld().getBlockState(this.entity.getPos()).get(POWERED));
                bone.setHidden(flag);
            }
        }

        int light = packedLightIn;
        if (bone.getName().contains("activelight") || bone.getName().contains("powered") || Objects.equals(bone.getName(), "blinker") || Objects.equals(bone.getName(), "activeeffect")) {
            light = LightTexture.packLight(15, 15);
        }

        bufferIn = rtb.getBuffer(RenderType.getEntitySmoothCutout(texture));
        super.renderRecursively(bone, stack, bufferIn, light, packedOverlayIn, red, green, blue, alpha);

    }
}
