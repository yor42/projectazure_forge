package com.yor42.projectazure.libs.utils;

import com.lowdragmc.multiblocked.Multiblocked;
import com.lowdragmc.multiblocked.api.item.ItemBlueprint;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.setup.register.RegisterItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.registries.RegistryObject;

@OnlyIn(Dist.CLIENT)
public class ClientUtils {

    public static final ResourceLocation GASMASKOVERLAY = ResourceUtils.ModResourceLocation("textures/misc/gasmaskblur.png");

    public static Level getClientWorld()
    {
        return Minecraft.getInstance().level;
    }

    public static void RegisterModelProperties(){
        ItemProperties.register(RegisterItems.RECHARGEABLE_BATTERY.get(), new ResourceLocation(Constants.MODID, "charge"), (stack, world, entity, seed)-> stack.getCapability(CapabilityEnergy.ENERGY, null).map((energy)-> ((float)energy.getEnergyStored())/((float)energy.getMaxEnergyStored())).orElse(0F));

    }

    public static void renderTextureOverlay(ResourceLocation p_168709_, float p_168710_) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, p_168710_);
        RenderSystem.setShaderTexture(0, p_168709_);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(0.0D, Minecraft.getInstance().getWindow().getGuiScaledHeight(), -90.0D).uv(0.0F, 1.0F).endVertex();
        bufferbuilder.vertex(Minecraft.getInstance().getWindow().getGuiScaledWidth(), Minecraft.getInstance().getWindow().getGuiScaledHeight(), -90.0D).uv(1.0F, 1.0F).endVertex();
        bufferbuilder.vertex(Minecraft.getInstance().getWindow().getGuiScaledWidth(), 0.0D, -90.0D).uv(1.0F, 0.0F).endVertex();
        bufferbuilder.vertex(0.0D, 0.0D, -90.0D).uv(0.0F, 0.0F).endVertex();
        tesselator.end();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
