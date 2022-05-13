package com.yor42.projectazure.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.ProjectAzureWorldSavedData;
import com.yor42.projectazure.gameobject.capability.playercapability.CompanionTeam;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.network.packets.CreateTeamPacket;
import com.yor42.projectazure.network.packets.RemoveTeamPacket;
import com.yor42.projectazure.network.packets.TeamNameChangedPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.item.ItemGroup;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.*;

import java.util.ArrayList;
import java.util.List;

public class GuiTeamFormation extends Screen {

    private int Subscreen = 0;
    private int TeamListPage = 0;
    private int x, y;
    private List<CompanionTeam> player_teams = new ArrayList<>();
    private CompanionTeam editingTeam;
    private final int backgroundWidth = this.Subscreen == 0? 248:155;
    private final int backgroundHeight = this.Subscreen == 0?219:167;
    private boolean notYetPopulated = true;

    public static final ResourceLocation TEXTURE_MAINSCREEN = new ResourceLocation(Constants.MODID, "textures/gui/teammanagement.png");
    public static final ResourceLocation TEXTURE_SUBSCREEN = new ResourceLocation(Constants.MODID, "textures/gui/teamaddmember.png");
    private static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation(Constants.MODID, "textures/gui/button_teamformation.png");

    private TextFieldWidget name;


    public GuiTeamFormation(ITextComponent p_i51108_1_) {
        super(p_i51108_1_);

    }

    public void tick() {
        super.tick();
        this.name.tick();
        List<CompanionTeam> teams = this.player_teams;
        this.player_teams = ProjectAzureWorldSavedData.getPlayersTeamClient(this.minecraft.player);
        if(teams.size()!=ProjectAzureWorldSavedData.getPlayersTeamClient(this.minecraft.player).size()){
            this.notYetPopulated = true;
            this.init();
        }
    }

    @Override
    public void init(Minecraft p_231158_1_, int p_231158_2_, int p_231158_3_) {
        super.init(p_231158_1_, p_231158_2_, p_231158_3_);
        this.x = (this.width - backgroundWidth) / 2;
        this.y = (this.height - backgroundHeight) / 2;
        this.subInit();
    }

    @Override
    protected void init() {
        this.notYetPopulated = true;
        this.buttons.clear();
        this.children.clear();
        super.init();
        //this.subInit();
        
    }

    private void subInit() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        this.name = new TextFieldWidget(this.font, x + 9, y + 25, 109, 16, new TranslationTextComponent("team.defaultteamname"));
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
    public void resize(Minecraft p_231152_1_, int p_231152_2_, int p_231152_3_) {
        this.notYetPopulated = true;
        super.resize(p_231152_1_, p_231152_2_, p_231152_3_);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        this.drawBackgroundLayer(matrixStack, partialTicks, mouseX, mouseY);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.drawForegroundLayer(matrixStack, mouseX, mouseY, partialTicks);
        /*
        this.renderButtons(matrixStack,mouseX, mouseY, partialTicks);

         */
    }

    private void drawForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (this.Subscreen == 0) {
            this.renderMainScreenButtons(matrixStack, mouseX, mouseY, partialTicks);
        } else {
            this.renderSubScreenButtons(matrixStack, mouseX, mouseY, partialTicks);
        }
    }

    private void renderSubScreenButtons(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {

    }

    private void renderMainScreenButtons(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if(this.minecraft!= null) {
            //Draw Team Lists
            int i=0;
            while(i<5 && 5* this.TeamListPage+i<=this.player_teams.size()){
                if(5* this.TeamListPage+i==this.player_teams.size()){
                    if(this.notYetPopulated) {
                        //draw create team button here
                        Button button = new CreateTeamButton(this.x + 126, this.y + 6 + 37 * i, 110, 37, new StringTextComponent(""), (runnable) -> this.addTeam());
                        this.addButton(button);
                    }
                }
                else{
                    CompanionTeam team = this.player_teams.get(i);
                    ITextComponent text = team.getDisplayName();
                    int x = 130;
                    int y = 10 + 37 * i;
                    this.minecraft.font.draw(matrixStack, text, this.x+x, this.y+y, this.editingTeam == team?0xFFFFFF00:0xFFFFFFFF);
                    if(this.notYetPopulated) {
                        Button button = new ImageButton(this.x+x+96, this.y+y,11,11, 125,0,11, BUTTON_TEXTURE, (runnable)->this.deleteTeam(team));
                        this.addButton(button);
                    }
                }
                i++;
            }
            this.notYetPopulated = false;
        }
    }

    public boolean mouseReleased(double p_231048_1_, double p_231048_3_, int p_231048_5_) {
        if (p_231048_5_ == 0 && this.Subscreen == 0) {
            return this.clickteam(p_231048_1_, p_231048_3_);
        }
        return super.mouseReleased(p_231048_1_, p_231048_3_, p_231048_5_);
    }

    protected boolean clickteam(double mouseX, double mouseY) {
        int i=0;
        while(i<5 && 5* this.TeamListPage+i<this.player_teams.size()){
            if(this.isHovering(116, 6+37*i,113, 37, mouseX, mouseY)){
                this.editingTeam = this.player_teams.get(5* this.TeamListPage+i);
                this.playDownSound(Minecraft.getInstance().getSoundManager());
                return true;
            }
            i++;
        }
        return false;
    }

    public void playDownSound(SoundHandler p_230988_1_) {
        p_230988_1_.play(SimpleSound.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    public void changeScreen(int value){
        this.Subscreen = value;
        this.notYetPopulated = true;
    }

    public void addTeam(){
        Main.NETWORK.sendToServer(new CreateTeamPacket(this.minecraft.player.getUUID()));
        this.init();
    }

    private void deleteTeam(CompanionTeam team) {
        ProjectAzureWorldSavedData.getPlayersTeamClient(this.minecraft.player).remove(team);
        this.player_teams.remove(team);
        Main.NETWORK.sendToServer(new RemoveTeamPacket(team.getTeamUUID()));
        this.init();
    }

    protected boolean isHovering(int p_195359_1_, int p_195359_2_, int p_195359_3_, int p_195359_4_, double p_195359_5_, double p_195359_7_) {
        int i = this.x;
        int j = this.y;
        p_195359_5_ = p_195359_5_ - (double)i;
        p_195359_7_ = p_195359_7_ - (double)j;
        return p_195359_5_ >= (double)(p_195359_1_ - 1) && p_195359_5_ < (double)(p_195359_1_ + p_195359_3_ + 1) && p_195359_7_ >= (double)(p_195359_2_ - 1) && p_195359_7_ < (double)(p_195359_2_ + p_195359_4_ + 1);
    }

    private void drawBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        ResourceLocation textureLocation = this.Subscreen==0? TEXTURE_MAINSCREEN:TEXTURE_SUBSCREEN;
        this.minecraft.getTextureManager().bind(textureLocation);
        this.blit(matrixStack, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    private static class CreateTeamButton extends Button {

        private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MODID, "textures/gui/button_teamformation.png");

        public CreateTeamButton(int p_i232255_1_, int p_i232255_2_, int p_i232255_3_, int p_i232255_4_, ITextComponent p_i232255_5_, IPressable p_i232255_6_) {
            super(p_i232255_1_, p_i232255_2_, p_i232255_3_, p_i232255_4_, p_i232255_5_, p_i232255_6_);
        }

        @Override
        public void renderButton(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
            Minecraft minecraft = Minecraft.getInstance();
            FontRenderer font = minecraft.font;
            IFormattableTextComponent text = new TranslationTextComponent("gui.teamformation.createteam");
            IReorderingProcessor ireorderingprocessor = text.getVisualOrderText();
            int textWidth = font.width(ireorderingprocessor);
            int startX = (this.x+this.width/2) - (textWidth+20) / 2;
            font.draw(matrix, text, startX+20, this.y + (this.height - 8) / 2, this.isHovered()?0xffff00:0xffffff);
            minecraft.getTextureManager().bind(TEXTURE);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            this.blit(matrix, startX, this.y + (this.height - 16) / 2, 0,58, 16,16);
        }
    }
}
