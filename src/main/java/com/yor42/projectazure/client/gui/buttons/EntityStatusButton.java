package com.yor42.projectazure.client.gui.buttons;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.yor42.projectazure.gameobject.entity.companion.AbstractEntityCompanion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

public class EntityStatusButton extends Button {

    private final AbstractEntityCompanion entity;
    private final ResourceLocation resourceLocation;
    private final ACTIONTYPES type;
    private final int xTexStart;
    private final int activeXDelta;
    private final int yTexStart;
    private final int yDiffTex;
    private final int textureWidth;
    private final int textureHeight;

    public EntityStatusButton(AbstractEntityCompanion entity, int imagex, int imagey, int width, int height, int textureX, int textureY, int activeXDelta, int hoverYDelta, ResourceLocation texture, ACTIONTYPES action) {
        super(imagex, imagey, width, height, TextComponent.EMPTY, getAction(entity, action));
        this.textureWidth = 256;
        this.textureHeight = 256;
        this.xTexStart = textureX;
        this.yTexStart = textureY;
        this.yDiffTex = hoverYDelta;
        this.activeXDelta = activeXDelta;
        this.type = action;
        this.resourceLocation = texture;
        this.entity = entity;
    }

    public EntityStatusButton(AbstractEntityCompanion entity, int imagex, int imagey, int width, int height, int textureX, int textureY, int activeXDelta, int hoverYDelta, ResourceLocation texture, ACTIONTYPES action, Button.OnTooltip pOnTooltip) {
        super(imagex, imagey, width, height, TextComponent.EMPTY, getAction(entity, action), pOnTooltip);
        this.textureWidth = 256;
        this.textureHeight = 256;
        this.xTexStart = textureX;
        this.yTexStart = textureY;
        this.yDiffTex = hoverYDelta;
        this.activeXDelta = activeXDelta;
        this.type = action;
        this.resourceLocation = texture;
        this.entity = entity;
    }

    public void renderButton(PoseStack p_230431_1_, int p_230431_2_, int p_230431_3_, float p_230431_4_) {
        Minecraft minecraft = Minecraft.getInstance();
        RenderSystem.setShaderTexture(0,this.resourceLocation);
        int i = this.yTexStart;
        if (this.isHovered) {
            i += this.yDiffTex;
        }

        int j = this.xTexStart;

        switch (this.type) {
            default:
                if(this.entity.isFreeRoaming()){
                    j+=this.activeXDelta;
                }
                break;
            case ITEM:
                if(this.entity.shouldPickupItem()){
                    j+=this.activeXDelta;
                }
                break;
            case DEFENCE:
                if(this.entity.shouldAttackFirst()){
                    j+=this.activeXDelta;
                }
                break;
        }

        RenderSystem.enableDepthTest();
        blit(p_230431_1_, this.x, this.y, (float)j, (float)i, this.width, this.height, this.textureWidth, this.textureHeight);
        if (this.isHovered) {
            this.renderToolTip(p_230431_1_, p_230431_2_, p_230431_3_);
        }

    }

    private static void switchBehavior(AbstractEntityCompanion entity) {
        if(Screen.hasShiftDown()){
            entity.clearHomePos();
        }
        else {
            entity.SwitchFreeRoamingStatus();
        }
    }

    private static void switchItemBehavior(AbstractEntityCompanion entity) {
        entity.SwitchItemBehavior();
    }
    private static void switchAttackBehavior(AbstractEntityCompanion entity) {
        entity.SwitchPassiveAttack();
    }

    private static OnPress getAction(AbstractEntityCompanion entity, ACTIONTYPES action){
        switch (action) {
            default:
                return (pressible)->switchBehavior(entity);
            case ITEM:
                return (pressible)->switchItemBehavior(entity);
            case DEFENCE:
                return (pressible)->switchAttackBehavior(entity);
        }
    }

    public enum ACTIONTYPES{
        FREEROAM,
        ITEM,
        DEFENCE
    }
}
