package com.yor42.projectazure.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.client.gui.buttons.buttonStarterSelect;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.network.packets.selectedStarterPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.yor42.projectazure.libs.Constants.StarterList;

@OnlyIn(Dist.CLIENT)
public class GuiALStarterSpawn extends Screen {

    public static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MODID, "textures/gui/rainbow_cube_gui.png");

    private boolean notYetPopulated = true;

    private final int backgroundWidth = 253;
    private final int backgroundHeight = 162;

    private int x, y;

    private int scrollBarFarLeft, lastScrollX;

    private final int buttonWidth = 59;

    public GuiALStarterSpawn(Component titleIn) {
        super(titleIn);
    }

    @Override
    public void init(Minecraft minecraft, int width, int height) {
        super.init(minecraft, width, height);
        this.x = (this.width - backgroundWidth) / 2;
        this.y = (this.height - backgroundHeight) / 2;
        this.minecraft = minecraft;
        this.scrollBarFarLeft = this.x+13;
        
        RenderSystem.setShaderTexture(0,TEXTURE);
        //this.blit(matrixStack, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
        for(int index = 0; index < StarterList.length; index++) {
            int buttonHeight = 127;
            if (this.notYetPopulated) {
                int finalIndex = index;
                Button button = createButton(this.x+9+(index * buttonWidth), this.y+26, buttonWidth, buttonHeight, index, StarterList[index], (action) -> {
                    Main.NETWORK.sendToServer(new selectedStarterPacket(finalIndex));
                    this.onClose();
                });
                this.addButton(button);
                        //createButton(entityList[index], this.scrollBarFarLeft + (++index * buttonHeight), this.y+26, buttonWidth, buttonHeight, new TranslationTextComponent("gui.selectstarter.select"+index), action -> Main.LOGGER.info("Player tried to spawn starter. but guess what. this doesnt do shit!"));
            } /*else {
                Button button = (Button) this.buttons.get(index);
                //button.setMessage(getSkillEntryFormattedText(skill, entry.getValue()));
                button.x = this.scrollBarFarLeft + (index * buttonHeight);
                button.y = this.y+26;
            }*/
        }
        this.notYetPopulated = false;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.drawBackgroundLayer(matrixStack, partialTicks, mouseX, mouseY);
        this.drawForegroundLayer(matrixStack, mouseX, mouseY, partialTicks);
        this.renderButtons(matrixStack,mouseX, mouseY, partialTicks);
    }

    private void renderButtons(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {

        if(this.buttons.size() > 0) {
            for (net.minecraft.client.gui.components.AbstractWidget widget : this.buttons) {
                buttonStarterSelect button = (buttonStarterSelect) widget;
                button.render(matrixStack, mouseX, mouseY, partialTicks);
            }
        }
        //will be implemented when screen actually needs to be scrolled
        /*
        int position = Math.floorDiv(this.lastScrollX - this.scrollBarFarLeft, 20); // CORRECT - GETS THE 'INDEX"
        for (int i = 0; i < this.buttons.size(); i++) {
            guiStarterButton button = (guiStarterButton) this.buttons.get(i);
            if (i < position || i > position + 3) {
                button.visible = false;
                button.active = false;
            } else {
                button.visible = true;
                button.active = true;
                button.setx(this.scrollBarFarLeft + ((i - position) * buttonWidth));
                button.render(matrixStack, mouseX, mouseY, partialTicks);
            }
        }

         */
    }

    private void drawBackgroundLayer(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        
        RenderSystem.setShaderTexture(0,TEXTURE);
        this.blit(matrixStack, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    private void drawForegroundLayer(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        drawString(matrixStack, this.font, new TranslatableComponent("gui.selectstarter.title"), this.x+18, this.y+10, 14085119);
    }

    public int getBackgroundLeft(){
        return this.x;
    }

    public int getBackgroundTop(){
        return this.y;
    }

    public String getNarrationMessage() {
        return "Hello World";
    }

    private buttonStarterSelect createButton(int x, int y, int width, int height, int idx, EntityType<?> type, Button.OnPress onPress) {
        ResourceLocation TEXTURE1 = new ResourceLocation(Constants.MODID, "textures/gui/rainbow_cube_overlay.png");
        return new buttonStarterSelect(x, y, width, height, 0, 0, this.buttonWidth , TEXTURE1, idx, type, onPress);
    }
}
