package com.yor42.projectazure.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.capability.playercapability.CompanionTeam;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.network.packets.TeamNameChangedPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class GuiTeamFormation extends Screen {

    private int Subscreen = 0;
    private int x, y;
    private CompanionTeam editingTeam;
    private final int backgroundWidth = this.Subscreen == 0? 248:155;
    private final int backgroundHeight = this.Subscreen == 0?219:167;

    public static final ResourceLocation TEXTURE_MAINSCREEN = new ResourceLocation(Constants.MODID, "textures/gui/teamformation.png");
    public static final ResourceLocation TEXTURE_SUBSCREEN = new ResourceLocation(Constants.MODID, "textures/gui/teamaddmember.png");

    private TextFieldWidget name;


    protected GuiTeamFormation(ITextComponent p_i51108_1_) {
        super(p_i51108_1_);

    }

    public void tick() {
        super.tick();
        this.name.tick();
    }

    @Override
    public void init(Minecraft p_231158_1_, int p_231158_2_, int p_231158_3_) {
        super.init(p_231158_1_, p_231158_2_, p_231158_3_);
        this.x = (this.width - backgroundWidth) / 2;
        this.y = (this.height - backgroundHeight) / 2;
        this.subInit();
    }

    private void subInit() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        this.name = new TextFieldWidget(this.font, x + 9, y + 25, 109, 16, new TranslationTextComponent("screen.teamname"));
        this.name.setTextColor(-1);
        this.name.setTextColorUneditable(-1);
        this.name.setBordered(false);
        this.name.setMaxLength(35);
        if(this.editingTeam == null||this.Subscreen != 0){
            this.name.setEditable(false);
            this.name.setVisible(false);
        }
        this.name.setResponder(this::onNameChanged);
        this.children.add(this.name);
    }

    private void onNameChanged(String s) {
        if(this.editingTeam!= null){
            this.editingTeam.setCustomName(new StringTextComponent(s));
            Main.NETWORK.sendToServer(new TeamNameChangedPacket(this.editingTeam.getTeamUUID(), s));
        }
    }


    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.drawBackgroundLayer(matrixStack, partialTicks, mouseX, mouseY);
        if(this.Subscreen==0){
            this.renderMainScreenButtons(matrixStack,mouseX, mouseY, partialTicks);
        }
        else{
            this.renderSubScreenButtons(matrixStack,mouseX, mouseY, partialTicks);
        }
        /*
        this.drawForegroundLayer(matrixStack, mouseX, mouseY, partialTicks);
        this.renderButtons(matrixStack,mouseX, mouseY, partialTicks);

         */
    }

    private void renderSubScreenButtons(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {

    }

    private void renderMainScreenButtons(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        /*
        Draw Team Lists
         */
        int maxLoop = Math.min(5, )
    }

    public void changeScreen(int value){
        this.Subscreen = value;
    }

    private void drawBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        ResourceLocation textureLocation = this.Subscreen==0? TEXTURE_MAINSCREEN:TEXTURE_SUBSCREEN;
        this.minecraft.getTextureManager().bind(textureLocation);
        this.blit(matrixStack, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }
}
