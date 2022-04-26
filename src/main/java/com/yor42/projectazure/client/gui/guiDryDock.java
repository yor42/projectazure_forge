package com.yor42.projectazure.client.gui;

import com.mojang.blaze3d.matrix.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.containers.machine.ContainerDryDock;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import com.yor42.projectazure.network.packets.StartRecruitPacket;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.Inventory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;

public class guiDryDock extends AbstractContainerScreen<ContainerDryDock> {

    private final ContainerDryDock container;

    private static final ResourceLocation TEXTURE = ResourceUtils.ModResourceLocation("textures/gui/dry_dock.png");
    public guiDryDock(ContainerDryDock screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.container = screenContainer;
    }

    @Override
    public void render(PoseStack PoseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(PoseStack);
        super.render(PoseStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(PoseStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack PoseStack, float partialTicks, int x, int y) {
        this.minecraft.getTextureManager().getTexture(TEXTURE);
        this.blit(PoseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        int p = this.container.getStoredPowerScaled(71);
        this.blit(PoseStack, this.leftPos+157, this.topPos+6, 176, 0, 10, 72-p);
    }

    @Override
    protected void renderLabels(PoseStack PoseStack, int x, int y) {
        this.font.draw(PoseStack, this.title, 51, 7, 0x00FF00);

        float renderScale = 0.6F;
        PoseStack.pushPose();
        PoseStack.scale(renderScale, renderScale, renderScale);
        this.font.draw(PoseStack, new TranslatableComponent("gui.construction_eta").append(":"), 51/renderScale,39/renderScale, 0x00FF00);
        PoseStack.popPose();

        renderScale = 0.95F;
        PoseStack.pushPose();
        PoseStack.scale(renderScale, renderScale, renderScale);
        TextComponent RemainingTime = MathUtil.Tick2FormattedClock(container.getRemainingTick());
        this.font.draw(PoseStack, RemainingTime, 51/renderScale,46/renderScale, 0x00FF00);
        PoseStack.popPose();
        this.renderButtons();
        if(isHovering(157, 6,10,72,x,y)){
            this.renderTooltip(PoseStack, new TextComponent(this.container.getField().get(2)+"/"+this.container.getField().get(3)), x,y);
        }
    }

    private void renderButtons() {
        Button button = new Button(this.leftPos+49,this.topPos+60,61,16,new TranslatableComponent("gui.machine.start"), (action)-> {
            Main.NETWORK.sendToServer(new StartRecruitPacket(this.container.getBlockPos()));
        });
        this.addWidget(button);
    }
}
