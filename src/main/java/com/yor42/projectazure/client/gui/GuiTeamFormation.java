package com.yor42.projectazure.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.ProjectAzureWorldSavedData;
import com.yor42.projectazure.gameobject.capability.playercapability.CompanionTeam;
import com.yor42.projectazure.gameobject.capability.playercapability.ProjectAzurePlayerCapability;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.network.packets.CreateTeamPacket;
import com.yor42.projectazure.network.packets.EditTeamMemberPacket;
import com.yor42.projectazure.network.packets.RemoveTeamPacket;
import com.yor42.projectazure.network.packets.TeamNameChangedPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class GuiTeamFormation extends Screen {

    private int Subscreen = 0;
    private int TeamListPage = 0;
    private int x, y;
    private List<CompanionTeam> player_teams = new ArrayList<>();
    @Nullable
    private UUID editingTeam;
    private int backgroundWidth = 248;
    private int backgroundHeight = 219;
    private boolean notYetPopulated = true;
    private boolean ignoreMouseRelease = false;
    private boolean scrollbarClicked = false;
    private int lastScrollY;
    private int scrollBarTop;

    public static final ResourceLocation TEXTURE_MAINSCREEN = new ResourceLocation(Constants.MODID, "textures/gui/teammanagement.png");
    public static final ResourceLocation TEXTURE_SUBSCREEN = new ResourceLocation(Constants.MODID, "textures/gui/teamaddmember.png");
    private static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation(Constants.MODID, "textures/gui/button_teamformation.png");
    List<AbstractEntityCompanion> Entitycache = new ArrayList<>();

    private TextFieldWidget name;


    public GuiTeamFormation(ITextComponent p_i51108_1_) {
        super(p_i51108_1_);

    }

    public void tick() {
        super.tick();
        this.name.tick();
        List<CompanionTeam> teams = this.player_teams;

        ProjectAzurePlayerCapability capability = ProjectAzurePlayerCapability.getCapability(this.minecraft.player);
        List<AbstractEntityCompanion> entities = capability.getCompanionList().stream().filter((entity) -> this.getEditingTeam().map((team)->team.getMembers().contains(entity.getUUID())).orElse(false)).collect(Collectors.toList());

        this.player_teams = ProjectAzureWorldSavedData.getPlayersTeamClient(this.minecraft.player);
        if(teams.size()!=ProjectAzureWorldSavedData.getPlayersTeamClient(this.minecraft.player).size() || this.Entitycache.size()!=entities.size()){
            this.init();
        }
    }

    @Override
    public void init(Minecraft p_231158_1_, int p_231158_2_, int p_231158_3_) {
        super.init(p_231158_1_, p_231158_2_, p_231158_3_);
        this.subInit();
    }

    @Override
    protected void init() {
        this.notYetPopulated = true;
        this.buttons.clear();
        this.children.clear();
        super.init();
        this.backgroundWidth = this.Subscreen == 0? 248:155;
        this.backgroundHeight = this.Subscreen == 0?219:167;
        this.x = (this.width - backgroundWidth) / 2;
        this.y = (this.height - backgroundHeight) / 2;
        this.scrollBarTop = this.y + 24;
        this.lastScrollY = this.scrollBarTop;
        if(this.editingTeam != null){
            if(this.Subscreen == 0){
                this.name.setEditable(true);
                this.name.setVisible(true);
                this.name.setFocus(true);
                this.setFocused(name);
            }
            else{
                this.name.setEditable(false);
                this.name.setVisible(false);
                this.name.setFocus(false);
                this.setFocused(null);
            }
        }
    }

    private void subInit() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        this.name = new TextFieldWidget(this.font, x + 9, y + 25, 109, 16, new TranslationTextComponent("team.defaultteamname"));
        this.name.setTextColor(-1);
        this.name.setTextColorUneditable(-1);
        this.name.setBordered(true);
        this.name.setMaxLength(35);
        if(this.editingTeam == null||this.Subscreen != 0){
            this.name.setEditable(false);
            this.name.setVisible(false);
        }
        else {
            this.name.setEditable(true);
            this.name.setVisible(true);
            this.name.setFocus(true);
            this.setFocused(name);
        }
        this.name.setResponder(this::onNameChanged);
        this.children.add(this.name);
    }

    private void onNameChanged(String s) {
        if(this.editingTeam!= null) {
            Main.NETWORK.sendToServer(new TeamNameChangedPacket(this.editingTeam, s));
        }
    }

    public Optional<CompanionTeam> getEditingTeam(){

        for(CompanionTeam team:this.player_teams){
            if(team.getTeamUUID().equals(this.editingTeam)){
                return Optional.of(team);
            }
        }
        return Optional.empty();
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
        this.name.render(matrixStack, mouseX, mouseY, partialTicks);
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
        ProjectAzurePlayerCapability capability = ProjectAzurePlayerCapability.getCapability(this.minecraft.player);
        List<AbstractEntityCompanion> entities = capability.getCompanionList().stream().filter((entity)->{
            for(CompanionTeam team:this.player_teams){
                if(team.getMembers().contains(entity.getUUID())){
                    return false;
                }
            }
            return true;
        }).collect(Collectors.toList());
        if (this.scrollbarClicked) {
            this.renderScroller(matrixStack, mouseY, entities.size());
        } else {
            this.renderScroller(matrixStack, this.lastScrollY, entities.size());
        }
        if(this.notYetPopulated) {
            int i=-1;
            for (AbstractEntityCompanion entity : entities) {
                Button button = new EntityButton(this.x+8, this.y+24+(27*++i), 121, 27, entity, (runnable)->this.addMeber(entity));
                this.addButton(button);
            }
        }
        this.resolveAndRenderButtons(matrixStack, mouseX, mouseY, partialTicks);
        this.notYetPopulated = false;
    }

    protected void renderScroller(MatrixStack stack, int drop, int entityCount) {
        int maxy = this.y + 139;
        if(entityCount>5) {
            int scrollbarTop = this.scrollBarTop;
            this.lastScrollY = Math.min(maxy, Math.max(scrollbarTop, drop));
        }
        this.minecraft.getTextureManager().bind(GuiTeamFormation.TEXTURE_SUBSCREEN);
        this.blit(stack, this.x + 135, this.lastScrollY, this.scrollbarClicked || entityCount<=5 ? 167 : 155, 0, 12, 20);
    }

    private void renderMainScreenButtons(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if(this.minecraft!= null) {
            //Draw Team Lists
            int i = 0;
            while (i < 5 && 5 * this.TeamListPage + i <= this.player_teams.size()) {
                if (5 * this.TeamListPage + i == this.player_teams.size()) {
                    if (this.notYetPopulated) {
                        //draw create team button here
                        Button button = new CreateButton(this.x + 126, this.y + 6 + 37 * i, 110, 37, new TranslationTextComponent("gui.teamformation.createteam"), (runnable) -> this.addTeam());
                        this.addButton(button);
                    }
                } else {
                    CompanionTeam team = this.player_teams.get(i+(5*this.TeamListPage));
                    ITextComponent text = team.getDisplayName();
                    int x = 130;
                    int y = 10 + 37 * i;
                    boolean isSelected = this.editingTeam != null && this.editingTeam.equals(team.getTeamUUID());
                    this.minecraft.font.drawShadow(matrixStack, text, this.x + x, this.y + y, isSelected ? 0xFFFFFF00 : 0xFFFFFFFF);
                    if (this.notYetPopulated) {
                        Button button = new ImageButton(this.x + x + 96, this.y + y, 11, 11, 125, 0, 11, BUTTON_TEXTURE, (runnable) -> this.deleteTeam(team));
                        this.addButton(button);
                    }
                }
                i++;
            }

            if(this.editingTeam!= null && this.minecraft.player != null){
                ProjectAzurePlayerCapability capability = ProjectAzurePlayerCapability.getCapability(this.minecraft.player);
                ArrayList<UUID> members = this.getEditingTeam().map(CompanionTeam::getMembers).orElse(new ArrayList<>());
                List<AbstractEntityCompanion> entities = capability.getCompanionList().stream().filter((entity)-> members.contains(entity.getUUID())).collect(Collectors.toList());

                for(int j=0; j<entities.size()+1; j++){
                    int x = this.x + 8;
                    int y = this.y + 65 + (29 * j);
                    if(j<entities.size()){
                        AbstractEntityCompanion entity = entities.get(j);
                        if (this.notYetPopulated) {
                            Button button = new ImageButton(x, y, 14, 29, 0, 0, 29, BUTTON_TEXTURE, (runnable) -> this.removeMeber(entity));
                            this.addButton(button);
                        }
                        this.minecraft.getTextureManager().bind(BUTTON_TEXTURE);
                        this.blit(matrixStack, x+12, y, 14, 0,97,29);
                    }
                    else {
                        if (this.notYetPopulated) {
                            //draw add member button here
                            Button button = new CreateButton(x, y, 111, 29, new TranslationTextComponent("gui.teamformation.addmember"), true, (runnable) -> this.changeScreen(1));
                            this.addButton(button);
                        }
                    }
                }

                if(this.notYetPopulated){
                    this.Entitycache = entities;
                }
            }

            IFormattableTextComponent text = new TranslationTextComponent("gui.teamformation.pages", (this.TeamListPage + 1) + "/" + (this.player_teams.size() / 5 + 1));
            IReorderingProcessor ireorderingprocessor = text.getVisualOrderText();
            int textWidth = this.font.width(ireorderingprocessor);
            int x = this.x + 182 - (textWidth + 40) / 2;
            int y = this.y + 204;
            this.font.draw(matrixStack, text, x + 20, this.y + 200, 0xFFFFFF);
            if (this.notYetPopulated) {
                if ((1 + this.TeamListPage) * 5 <= this.player_teams.size()) {
                    Button button = new ImageButton(x+8+textWidth+16, y-8, 16, 16, 141, 22, 17, BUTTON_TEXTURE, (runnable) -> this.Scrolldown());
                    this.addButton(button);
                }

                if (this.TeamListPage >0) {
                    Button button = new ImageButton(x, y-8, 16, 16, 125, 22, 16, BUTTON_TEXTURE, (runnable) -> this.Scrollup());
                    this.addButton(button);
                }
            }

            this.notYetPopulated = false;
        }
    }

    public void addMeber(AbstractEntityCompanion member){
        if(this.editingTeam!=null) {
            Main.NETWORK.sendToServer(new EditTeamMemberPacket(this.editingTeam, member.getUUID(), EditTeamMemberPacket.ACTION.ADD));
        }
        this.init();
        this.changeScreen(0);
    }

    public void removeMeber(AbstractEntityCompanion member){
        if(this.editingTeam!=null) {
            Main.NETWORK.sendToServer(new EditTeamMemberPacket(this.editingTeam, member.getUUID(), EditTeamMemberPacket.ACTION.REMOVE));
        }
        this.init();
    }

    protected void resolveAndRenderButtons(MatrixStack stack, int mouseX, int mouseY, float delta) {
        int position = Math.floorDiv(this.lastScrollY - this.scrollBarTop, 5); // CORRECT - GETS THE 'INDEX"
        for (int i = 0; i < this.buttons.size(); i++) {
            Widget button = this.buttons.get(i);
            if(button instanceof EntityButton) {
                if (i < position || i > position + 6) {
                    button.visible = false;
                    button.active = false;
                } else {
                    button.visible = true;
                    button.active = true;
                    button.y = this.scrollBarTop + ((i - position) * 27);
                    button.render(stack, mouseX, mouseY, delta);
                }
            }
        }
    }

    public void Scrollup(){
        this.TeamListPage--;
        this.init();
    }

    public void Scrolldown(){
        this.TeamListPage++;
        this.init();
    }

    private boolean hasMouseOnScrollBar(double mouseX, double mouseY) {
        return mouseX >= this.x + 135 && mouseX <= this.x + 147 && mouseY >= this.y + 24 && mouseY <= this.y + 159;
    }

    public boolean mouseReleased(double p_231048_1_, double p_231048_3_, int p_231048_5_) {
        if (p_231048_5_ == 0 && this.Subscreen == 0 && !this.ignoreMouseRelease) {
            return this.clickteam(p_231048_1_, p_231048_3_);
        }
        else if(this.Subscreen == 1){
            this.scrollbarClicked = false;
        }
        this.ignoreMouseRelease = false;
        return super.mouseReleased(p_231048_1_, p_231048_3_, p_231048_5_);
    }

    protected boolean clickteam(double mouseX, double mouseY) {
        int i=0;
        while(i<5 && (5* this.TeamListPage)+i<this.player_teams.size()){
            if(this.isHovering(116, 6+37*i,113, 37, mouseX, mouseY)){
                CompanionTeam team = this.player_teams.get(5* this.TeamListPage+i);
                this.editingTeam = team.getTeamUUID();
                this.name.setValue(team.getDisplayName().getString());
                this.init();
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
        this.scrollBarTop = this.y + 24;
        this.lastScrollY = this.scrollBarTop;
        this.init();
    }

    @Override
    public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
        if(this.hasMouseOnScrollBar(p_231044_1_, p_231044_3_)){
            this.scrollbarClicked = true;
            this.playDownSound(this.minecraft.getSoundManager());
        }
        return super.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_);
    }

    public void addTeam(){
        Main.NETWORK.sendToServer(new CreateTeamPacket(this.minecraft.player.getUUID()));
        this.ignoreMouseRelease = true;
        this.init();
    }

    private void deleteTeam(CompanionTeam team) {

        if(team.getTeamUUID().equals(this.editingTeam)){
            this.editingTeam = null;
            this.name.setValue("");
            this.name.setEditable(false);
            this.name.setVisible(false);
            this.name.setFocus(false);
            this.setFocused(null);
        }

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

    private static class CreateButton extends Button {

        private final boolean hasBG;

        public CreateButton(int p_i232255_1_, int p_i232255_2_, int p_i232255_3_, int p_i232255_4_, ITextComponent p_i232255_5_, IPressable p_i232255_6_) {
            this(p_i232255_1_, p_i232255_2_, p_i232255_3_, p_i232255_4_, p_i232255_5_, false,  p_i232255_6_);
        }

        public CreateButton(int p_i232255_1_, int p_i232255_2_, int p_i232255_3_, int p_i232255_4_, ITextComponent p_i232255_5_, boolean hasBG, IPressable p_i232255_6_) {
            super(p_i232255_1_, p_i232255_2_, p_i232255_3_, p_i232255_4_, p_i232255_5_, p_i232255_6_);
            this.hasBG = hasBG;
        }

        @Override
        public void renderButton(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
            Minecraft minecraft = Minecraft.getInstance();
            FontRenderer font = minecraft.font;
            IReorderingProcessor ireorderingprocessor = this.getMessage().getVisualOrderText();
            int textWidth = font.width(ireorderingprocessor);
            int startX = (this.x+this.width/2) - (textWidth+20) / 2;
            if(this.hasBG){
                this.blit(matrix, this.x, this.y, 14,this.isHovered()?29:0, 111,29);
            }
            font.draw(matrix, this.getMessage(), startX+20, this.y + (this.height - 8) / 2, this.isHovered()?0xffff00:0xffffff);
            minecraft.getTextureManager().bind(GuiTeamFormation.BUTTON_TEXTURE);
            this.blit(matrix, startX, this.y + (this.height - 16) / 2, 0,58, 16,16);
        }
    }

    private static class EntityButton extends Button {
        private final AbstractEntityCompanion entity;
        public EntityButton(int p_i232255_1_, int p_i232255_2_, int p_i232255_3_, int p_i232255_4_, AbstractEntityCompanion p_i232255_5_, IPressable p_i232255_6_) {
            super(p_i232255_1_, p_i232255_2_, p_i232255_3_, p_i232255_4_, p_i232255_5_.getDisplayName(), p_i232255_6_);
            this.entity = p_i232255_5_;
        }

        @Override
        public void renderButton(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
            Minecraft minecraft = Minecraft.getInstance();
            FontRenderer font = minecraft.font;
            minecraft.getTextureManager().bind(GuiTeamFormation.TEXTURE_SUBSCREEN);
            this.blit(matrix, this.x, this.y, 0,this.isHovered()?194:167, 121,27);
            font.draw(matrix, this.getMessage(), this.x+4, this.y + 4, this.isHovered()?0xffff00:0xffffff);
        }
    }
}
