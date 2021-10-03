package com.yor42.projectazure.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.containers.machine.ContainerDryDock;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import com.yor42.projectazure.network.packets.StartRecruitPacket;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class guiDryDock extends ContainerScreen<ContainerDryDock> {

    private final ContainerDryDock container;

    private static final ResourceLocation TEXTURE = ResourceUtils.ModResourceLocation("textures/gui/dry_dock.png");
    public guiDryDock(ContainerDryDock screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.container = screenContainer;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        int p = this.container.getStoredPowerScaled(71);
        this.blit(matrixStack, this.guiLeft+157, this.guiTop+6, 176, 0, 10, 72-p);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        this.font.func_243248_b(matrixStack, this.title, 51, 7, 0x00FF00);

        float renderScale = 0.6F;
        matrixStack.push();
        matrixStack.scale(renderScale, renderScale, renderScale);
        this.font.func_243248_b(matrixStack, new TranslationTextComponent("gui.construction_eta").appendString(":"), 51/renderScale,39/renderScale, 0x00FF00);
        matrixStack.pop();

        renderScale = 0.95F;
        matrixStack.push();
        matrixStack.scale(renderScale, renderScale, renderScale);
        StringTextComponent RemainingTime = MathUtil.Tick2FormattedClock(container.getRemainingTick());
        this.font.func_243248_b(matrixStack, RemainingTime, 51/renderScale,46/renderScale, 0x00FF00);
        matrixStack.pop();
        this.renderButtons();
        if(isPointInRegion(157, 6,10,72,x,y)){
            this.renderTooltip(matrixStack, new StringTextComponent(this.container.getField().get(2)+"/"+this.container.getField().get(3)), x,y);
        }
    }

    private void renderButtons() {
        Button button = new Button(this.guiLeft+49,this.guiTop+60,61,16,new TranslationTextComponent("gui.machine.start"), (action)-> {
            Main.NETWORK.sendToServer(new StartRecruitPacket(this.container.getBlockPos()));
        });
        this.addButton(button);
    }
}
