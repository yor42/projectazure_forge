package com.yor42.projectazure.client.gui.buttons.multiblocked;

import com.lowdragmc.lowdraglib.gui.texture.ColorBorderTexture;
import com.lowdragmc.lowdraglib.gui.texture.GuiTextureGroup;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.yor42.projectazure.libs.utils.ModifiedClickData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Consumer;

public class RiftwayStartButtonWidget extends Widget {
    protected Consumer<ModifiedClickData> onPressCallback;

    public RiftwayStartButtonWidget(int xPosition, int yPosition, int width, int height, IGuiTexture buttonTexture, Consumer<ModifiedClickData> onPressed) {
        super(xPosition, yPosition, width, height);
        this.onPressCallback = onPressed;
        setBackground(buttonTexture);
    }

    public RiftwayStartButtonWidget(int xPosition, int yPosition, int width, int height, Consumer<ModifiedClickData> onPressed) {
        super(xPosition, yPosition, width, height);
        this.onPressCallback = onPressed;
    }

    public RiftwayStartButtonWidget setOnPressCallback(Consumer<ModifiedClickData> onPressCallback) {
        this.onPressCallback = onPressCallback;
        return this;
    }

    public RiftwayStartButtonWidget setButtonTexture(IGuiTexture... buttonTexture) {
        super.setBackground(new GuiTextureGroup(buttonTexture));
        return this;
    }

    public RiftwayStartButtonWidget setHoverTexture(IGuiTexture... hoverTexture) {
        super.setHoverTexture(new GuiTextureGroup(hoverTexture));
        return this;
    }

    public RiftwayStartButtonWidget setHoverBorderTexture(int border, int color) {
        super.setHoverTexture(new GuiTextureGroup(backgroundTexture, new ColorBorderTexture(border, color)));
        return this;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOverElement(mouseX, mouseY)) {
            ModifiedClickData clickData = new ModifiedClickData();
            writeClientAction(1, clickData::writeToBuf);
            if (onPressCallback != null) {
                onPressCallback.accept(clickData);
            }
            playButtonClickSound();
            return true;
        }
        return false;
    }

    @Override
    public void handleClientAction(int id, FriendlyByteBuf buffer) {
        super.handleClientAction(id, buffer);
        if (id == 1) {
            ModifiedClickData clickData = ModifiedClickData.readFromBuf(buffer);
            if (onPressCallback != null) {
                onPressCallback.accept(clickData);
            }
        }
    }


}
