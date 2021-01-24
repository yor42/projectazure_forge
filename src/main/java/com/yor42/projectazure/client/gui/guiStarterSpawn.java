package com.yor42.projectazure.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.libs.defined;
import com.yor42.projectazure.network.packets.selectedStarterPacket;
import com.yor42.projectazure.setup.register.registerEntity;
import com.yor42.projectazure.setup.register.registerManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class guiStarterSpawn extends Screen {

    public static final ResourceLocation TEXTURE = new ResourceLocation(defined.MODID, "textures/gui/rainbow_cube_gui.png");

    private boolean notYetPopulated = true;

    private final int backgroundWidth = 195;
    private final int backgroundHeight = 162;

    private int x, y;

    private int scrollBarFarLeft, lastScrollX;

    private final int buttonWidth = 59;
    private final int buttonHeight = 127;

    private final EntityType[] entityList = {registerManager.AYANAMI.get()};

    public guiStarterSpawn(ITextComponent titleIn) {
        super(titleIn);
    }

    @Override
    public void init(Minecraft minecraft, int width, int height) {
        super.init(minecraft, width, height);
        this.x = (this.width - backgroundWidth) / 2;
        this.y = (this.height - backgroundHeight) / 2;
        this.minecraft = minecraft;
        this.scrollBarFarLeft = this.x+13;
        for(int index = 0; index < 1; index++) {
            if (this.notYetPopulated) {
                int finalIndex = index;
                Button button = createButton(this.x+9 + (index * buttonHeight), this.y+26, buttonWidth, buttonHeight, index, this.entityList[index], action -> Main.NETWORK.sendToServer(new selectedStarterPacket(finalIndex)));
                this.addButton(button);
                        //createButton(entityList[index], this.scrollBarFarLeft + (++index * buttonHeight), this.y+26, buttonWidth, buttonHeight, new TranslationTextComponent("gui.selectstarter.select"+index), action -> Main.LOGGER.info("Player tried to spawn starter. but guess what. this doesnt do shit!"));
            } else {
                Button button = (Button) this.buttons.get(index);
                //button.setMessage(getSkillEntryFormattedText(skill, entry.getValue()));
                button.x = this.scrollBarFarLeft + (index * buttonHeight);
                button.y = this.y+26;
            }
        }
        this.notYetPopulated = false;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.drawBackgroundLayer(matrixStack, partialTicks, mouseX, mouseY);
        this.drawForegroundLayer(matrixStack, mouseX, mouseY, partialTicks);
    }

    private void drawBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        this.blit(matrixStack, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    private void drawForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        drawString(matrixStack, this.font, new TranslationTextComponent("gui.selectstarter.title"), this.x+18, this.y+10, 14085119);

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

    private Button createButton(EntityType entity, int x, int y, int width, int height, ITextComponent message, Button.IPressable onPress) {
        ResourceLocation TEXTURE = new ResourceLocation(defined.MODID, "textures/gui/rainbow_cube_gui.png");
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        return new Button(x, y, width, height, message, onPress) {

            @Override
            public void renderButton(MatrixStack matrix, int mouseX, int mouseY, float delta) {
                super.renderButton(matrix, mouseX, mouseY, delta);
                int buttonHeight = 126;
                int buttonWidth = 59;
                this.blit(matrix, 195, 2, 0, 0, buttonWidth, buttonHeight);
                //InventoryScreen.drawEntityOnScreen(x+30, y+54, 64,mouseX,mouseY, entity);
            }
        };
    }

    private guiStarterButton createButton(int x, int y, int width, int height, int idx, EntityType type, Button.IPressable onPress) {
        ResourceLocation TEXTURE = new ResourceLocation(defined.MODID, "textures/gui/rainbow_cube_overlay.png");
        return new guiStarterButton(x, y, width, height, 0, 0, this.buttonWidth , TEXTURE, idx, type, onPress);
    }
}
