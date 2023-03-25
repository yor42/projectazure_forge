package com.yor42.projectazure.client.gui.container;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.containers.machine.ContainerRecruitBeacon;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import com.yor42.projectazure.network.packets.StartRecruitPacket;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class guiRecruitBeacon extends AbstractContainerScreen<ContainerRecruitBeacon> implements MenuAccess<ContainerRecruitBeacon> {

    private static final ResourceLocation TEXTURE = ResourceUtils.ModResourceLocation("textures/gui/recruit_beacon.png");

    private final ContainerRecruitBeacon container;


    public guiRecruitBeacon(ContainerRecruitBeacon screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.container = screenContainer;
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
        if(isHovering(157, 6,10,72,mouseX,mouseY)){
            this.renderTooltip(matrixStack, new TextComponent(this.container.getField().get(2)+"/"+this.container.getField().get(3)), mouseX,mouseY);
        }
        this.renderButtons(matrixStack, mouseX, mouseY, partialTicks);
    }

    private void renderButtons(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Button button = new Button(this.leftPos+102,this.topPos+61,50,16,new TranslatableComponent("gui.machine.start"), (action)-> {
            Main.NETWORK.sendToServer(new StartRecruitPacket(this.container.getBlockPos()));
        });
        this.addButton(button);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bind(TEXTURE);
        this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        int p = this.container.getStoredPowerScaled(71);
        this.blit(matrixStack, this.leftPos+157, this.topPos+6, 176, 0, 10, 72-p);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int x, int y) {

        this.font.draw(matrixStack, this.title, 51, 7, 0x00FF00);

        float renderScale = 0.6F;
        matrixStack.pushPose();
        matrixStack.scale(renderScale, renderScale, renderScale);
        this.font.draw(matrixStack, new TranslatableComponent("gui.recruitbeacon_remainingtime").append(":"), 51/renderScale,38/renderScale, 0x00FF00);
        matrixStack.popPose();

        renderScale = 1.5F;
        matrixStack.pushPose();
        matrixStack.scale(renderScale, renderScale, renderScale);
        TextComponent RemainingTime = MathUtil.Tick2FormattedClock(container.getRemainingTick());
        this.font.draw(matrixStack, RemainingTime, 51/renderScale,45/renderScale, 0x00FF00);
        matrixStack.popPose();
    }
}
