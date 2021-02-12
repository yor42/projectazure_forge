package com.yor42.projectazure.client.gui.buttons;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;

public class buttonKansenInventoryTab extends ImageButton {

    private int x;
    private final int y;
    private final int xTexStart;
    private final int yTexStart;
    private final int width, height;
    int InttoSkip;

    ResourceLocation TEXTURE;

    public buttonKansenInventoryTab(int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int yDiffTextIn, ResourceLocation resourceLocationIn, int indextoSkip, IPressable onPressIn) {
        super(xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, resourceLocationIn, onPressIn);
        this.TEXTURE = resourceLocationIn;
        this.x = xIn;
        this.y = yIn;
        this.width = widthIn;
        this.height = heightIn;
        this.xTexStart = xTexStartIn;
        this.yTexStart = yTexStartIn;
        this.InttoSkip = indextoSkip;
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.renderButton(matrixStack, mouseX, mouseY, partialTicks);
        for (int i = 0; i < 5; i++){
            if(this.InttoSkip == i)
                continue;
        }
    }
}
