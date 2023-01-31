package com.yor42.projectazure.client.gui.multiblocked;

import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.multiblocked.api.recipe.RecipeLogic;

public class MachineStatusIconWidget extends Widget {
    public MachineStatusIconWidget(int xPosition, int yPosition, int width, int height, RecipeLogic.Status status) {
        super(xPosition, yPosition, width, height);
    }

    public MachineStatusIconWidget(int xPosition, int yPosition, int width, int height, IGuiTexture area, RecipeLogic.Status status) {
        this(xPosition, yPosition, width, height, status);

    }
}
