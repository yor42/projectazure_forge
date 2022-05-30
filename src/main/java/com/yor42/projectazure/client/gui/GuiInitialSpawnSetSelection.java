package com.yor42.projectazure.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yor42.projectazure.Main;
import com.yor42.projectazure.libs.Constants;
import com.yor42.projectazure.libs.utils.ClientUtils;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.network.packets.SelectInitialSpawnSetPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.particle.Particle;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.UUID;

public class GuiInitialSpawnSetSelection extends Screen {

    public static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MODID, "textures/gui/initialspawnselection.png");
    public static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation(Constants.MODID, "textures/gui/button_initialspawnselection.png");
    private final int backgroundWidth = 256;
    private final int backgroundHeight = 173;
    private boolean ispopulated = false;
    private final Hand hand;
    private int x, y;

    public GuiInitialSpawnSetSelection(Hand hand) {
        super(new TranslationTextComponent("gui.initialspawnsetselect"));
        this.hand = hand;
    }

    @Override
    public void init(Minecraft p_231158_1_, int p_231158_2_, int p_231158_3_) {
        super.init(p_231158_1_, p_231158_2_, p_231158_3_);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    protected void init() {
        super.init();
        this.x = (this.width - backgroundWidth) / 2;
        this.y = (this.height - backgroundHeight) / 2;
        this.ispopulated = false;
    }

    private void selectSpawnSet(byte id){
        Main.NETWORK.sendToServer(new SelectInitialSpawnSetPacket(id, this.hand));
        if(this.minecraft != null && this.minecraft.player != null) {
            Vector3d vector3d = this.minecraft.player.position();
            for (int i = 0; i < 40; ++i) {
                double d0 = MathUtil.getRand().nextGaussian() * 0.2D;
                double d1 = MathUtil.getRand().nextGaussian() * 0.2D;
                double d2 = MathUtil.getRand().nextGaussian() * 0.2D;
                ClientUtils.getClientWorld().addParticle(ParticleTypes.END_ROD, vector3d.x, vector3d.y, vector3d.z, d0, d1, d2);
            }
            this.minecraft.player.sendMessage(new TranslationTextComponent("message.glitchedphone.selected_"+id), UUID.randomUUID());
        }
        this.onClose();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.drawBackgroundLayer(matrixStack, partialTicks, mouseX, mouseY);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    private void drawBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        this.minecraft.getTextureManager().bind(TEXTURE);
        ITextComponent text = new TranslationTextComponent("gui.initialspawnsetselect.selectpath");
        this.blit(matrixStack, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight);
        float width = (float) this.font.width(text)/2;
        this.font.draw(matrixStack, text, this.x+128-width, this.y+40, 0xffee90);
        if(!this.ispopulated) {
            int buttonwidth = 58;
            int buttonheight = 49;
            this.addButton(new ImageButton(this.x + 42, this.y + 62, buttonwidth, buttonheight, 0, 0, buttonheight, BUTTON_TEXTURE, (runnable) -> this.selectSpawnSet((byte) 0)));
            this.addButton(new ImageButton(this.x + 150, this.y + 62, buttonwidth, buttonheight, 58, 0, buttonheight, BUTTON_TEXTURE, (runnable) -> this.selectSpawnSet((byte) 1)));
            this.ispopulated = true;
        }

    }
}
