package com.yor42.projectazure.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.gameobject.containers.entity.ContainerGFLInventory;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import com.yor42.projectazure.network.proxy.ClientProxy;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import static com.yor42.projectazure.libs.utils.ResourceUtils.ModResourceLocation;

public class GuiGFLInventory extends ContainerScreen<ContainerGFLInventory> implements IHasContainer<ContainerGFLInventory> {

    public static final ResourceLocation TEXTURE = ModResourceLocation("textures/gui/gfl_inventory.png");
    private final AbstractEntityCompanion host;
    private final double affection, morale;

    public GuiGFLInventory(ContainerGFLInventory screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.host = Main.PROXY.getSharedMob();
        this.affection = this.host.getAffection();
        this.morale = this.host.getMorale();
        this.xSize = 170;
        this.ySize = 188;
    }

    private void renderEntity(int mousex, int mousey){
        Entity entity = this.host.getType().create(ClientProxy.getClientWorld());
        if(entity instanceof AbstractEntityCompanion) {
            entity.copyDataFromOld(this.host);
            int entityWidth = (int) entity.getWidth();
            try {
                InventoryScreen.drawEntityOnScreen(this.guiLeft + (46 - (entityWidth / 2)), this.guiTop + 75, 30, mousex * -1 + guiLeft + (53 - entityWidth / 2), mousey * -1 + this.guiTop + 70, (LivingEntity) entity);
            } catch (Exception e) {
                Main.LOGGER.error("Failed to render Entity!");
            }
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderEntity(mouseX, mouseY);
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderButton();
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        matrixStack.push();
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);

        for(int l = 0; l<host.getSkillItemCount(); l++){
            this.blit(matrixStack, this.guiLeft + 96, this.guiTop + 4+l*18, 153, 189, 16,16);
        }

        this.blit(matrixStack, this.guiLeft + 93, this.guiTop + 75, 173, 88, (int) (76*(this.affection/this.host.getmaxAffection())), 9);
        this.blit(matrixStack, this.guiLeft+43, this.guiTop+89, 173, 85,  (int) (45*(this.host.getExp()/this.host.getMaxExp())), 2);
        this.blit(matrixStack, this.guiLeft+176, this.guiTop+7, 173, 109, 42, 78);
        matrixStack.pop();

        matrixStack.push();
        float renderscale = (float) 5/9;
        matrixStack.scale(renderscale, renderscale, renderscale);
        this.blit(matrixStack, (int) ((this.guiLeft+43)/renderscale), (int) ((this.guiTop+92)/renderscale), 246, 11, 9, 9);
        this.blit(matrixStack, (int) ((this.guiLeft+43)/renderscale), (int) ((this.guiTop+98)/renderscale), 236, 11, 9, 9);
        this.blit(matrixStack, (int) ((this.guiLeft+67)/renderscale), (int) ((this.guiTop+98)/renderscale), 246, 11, 9, 9);
        matrixStack.pop();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {

        matrixStack.push();
        float scalerate = 0.75F;
        matrixStack.scale(scalerate,scalerate,scalerate);

        String AffectionString = String.format("%,.2f", this.host.getAffection())+"/"+this.host.getmaxAffection();
        int affectionwidth = (int) ((this.font.getStringWidth(AffectionString)+8)*scalerate);
        this.font.func_243248_b(matrixStack, new StringTextComponent(AffectionString), (140-((float)affectionwidth/2))/scalerate, 77/scalerate, 0x242424);
        this.minecraft.getTextureManager().bindTexture(TEXTURE);
        this.blit(matrixStack, (int) (((133)-((float)affectionwidth/2))/scalerate), (int) (77/scalerate), 203, 72, 7, 7);

        int textwidth = (int) (this.font.getStringWidth(this.host.getDisplayName().getString())*scalerate);
        this.font.func_243248_b(matrixStack, this.host.getDisplayName(), (42-textwidth)/scalerate, 81/scalerate, 0x242424);
        this.font.func_243248_b(matrixStack, new StringTextComponent("Lv."+this.host.getLevel()), 43/scalerate, 82/scalerate, 0x242424);
        matrixStack.pop();

        matrixStack.push();
        scalerate = 0.5F;
        matrixStack.scale(scalerate,scalerate,scalerate);
        String MaxHP = "MAX "+(int)this.host.getMaxHealth();
        int maxHPLength = (int) (this.font.getStringWidth(MaxHP)*scalerate);
        ITextComponent GunClass = new TranslationTextComponent(this.host.getGunSpecialty().getName());
        int gunNameLength = (int) (this.font.getStringWidth(GunClass.getString())*scalerate);
        this.font.func_243248_b(matrixStack, new StringTextComponent(MaxHP), (89-maxHPLength)/scalerate, (float) (92.5/scalerate), 0x7e8552);
        this.font.func_243248_b(matrixStack, new StringTextComponent("HP "+ (int) this.host.getHealth()), 48/scalerate, (float) (92.5/scalerate), 0x242424);
        this.font.func_243248_b(matrixStack, new StringTextComponent("FD "+ this.host.getFoodStats().getFoodLevel()), 48/scalerate, (float) (98.5/scalerate), 0x242424);
        this.font.func_243248_b(matrixStack, new StringTextComponent("MR "+ (int) this.host.getMorale()), 72/scalerate, (float) (98.5/scalerate), 0x242424);
        this.font.func_243248_b(matrixStack, GunClass, (41-gunNameLength)/scalerate, 88.5F/scalerate, 0x242424);
        matrixStack.pop();
    }

    private void switchBehavior() {
        if(Screen.hasShiftDown()){
            this.host.clearHomePos();
        }
        else {
            this.host.SwitchFreeRoamingStatus();
        }
    }

    private void switchItemBehavior() {
        this.host.SwitchItemBehavior();
    }

    private void renderButton(){
        int homeModeX = this.host.isFreeRoaming()? 1:15;
        int itemModeX = this.host.shouldPickupItem()? 1:15;
        Button homebutton = new ImageButton(this.guiLeft+1, this.guiTop+64, 14,14, homeModeX, 188, 14,TEXTURE, action->switchBehavior());
        Button itembutton = new ImageButton(this.guiLeft+16, this.guiTop+64, 14,14, itemModeX, 216, 14,TEXTURE, action->switchItemBehavior());

        this.addButton(homebutton);
        this.addButton(itembutton);
    }
}
