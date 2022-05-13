package com.yor42.projectazure.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.ProjectAzureWorldSavedData;
import com.yor42.projectazure.gameobject.capability.playercapability.CompanionTeam;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.network.packets.CreateTeamPacket;
import com.yor42.projectazure.network.packets.TeamNameChangedPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.item.ItemGroup;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.ResourceLocation;
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

    public static final ResourceLocation TEXTURE_MAINSCREEN = new ResourceLocation(Constants.MODID, "textures/gui/teammanagement.png");
    public static final ResourceLocation TEXTURE_SUBSCREEN = new ResourceLocation(Constants.MODID, "textures/gui/teamaddmember.png");

    private TextFieldWidget name;


    public GuiTeamFormation(ITextComponent p_i51108_1_) {
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
        this.player_teams = ProjectAzureWorldSavedData.getPlayersTeamClient(p_231158_1_.player);
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
        /*
        this.drawForegroundLayer(matrixStack, mouseX, mouseY, partialTicks);
        this.renderButtons(matrixStack,mouseX, mouseY, partialTicks);

         */
    }

    private void renderSubScreenButtons(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {

    }

    private void renderMainScreenButtons(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if(this.minecraft!= null) {
            //Draw Team Lists
            int i=0;
            while(i<5 && 5* this.TeamListPage+i<=this.player_teams.size()){
                if(5* this.TeamListPage+i==this.player_teams.size()){
                    //draw create team button here
                    Button button = new Button(this.x+126, 6+37*i,113, 37, new StringTextComponent("create team"), (runnable)->this.addTeam());
                    //this.addButton(button);
                }
                else {
                    CompanionTeam team = this.player_teams.get(i);
                    int x = 130;
                    int y = 10 + 37 * i;
                    this.font.draw(matrixStack, team.getDisplayName().copy().setStyle(Style.EMPTY.withColor(this.editingTeam == team ? TextFormatting.YELLOW : TextFormatting.WHITE)), x, y, 0xFFFF0000);
                }
                i++;
            }
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
            if(this.isHovering(126, 6+37*i,113, 37, mouseX, mouseY)){
                this.editingTeam = this.player_teams.get(5* this.TeamListPage+i);
                return true;
            }
            i++;
        }
        return false;
    }

    public void changeScreen(int value){
        this.Subscreen = value;
    }

    public void addTeam(){
        Main.NETWORK.sendToServer(new CreateTeamPacket(this.minecraft.player.getUUID()));
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
        if(this.Subscreen==0){
            this.renderMainScreenButtons(matrixStack,mouseX, mouseY, partialTicks);
        }
        else{
            this.renderSubScreenButtons(matrixStack,mouseX, mouseY, partialTicks);
        }
    }
}
