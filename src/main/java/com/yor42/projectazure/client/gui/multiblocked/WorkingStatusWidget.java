package com.yor42.projectazure.client.gui.multiblocked;

import com.google.common.collect.ImmutableMap;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.widget.ImageWidget;
import com.lowdragmc.multiblocked.api.recipe.RecipeLogic;
import com.mojang.blaze3d.matrix.MatrixStack;

import javax.annotation.Nonnull;

public class WorkingStatusWidget extends ImageWidget {

    public RecipeLogic logic;
    public ImmutableMap<RecipeLogic.Status, ResourceTexture> textures;

    public WorkingStatusWidget(ImmutableMap<RecipeLogic.Status, ResourceTexture> texturemap, RecipeLogic logic, int xPosition, int yPosition, int width, int height) {
        super(xPosition, yPosition, width, height);
        this.logic = logic;
        this.textures = texturemap;
    }

    @Override
    public void drawInBackground(@Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {

        this.setBackground(textures.get(this.logic==null? RecipeLogic.Status.IDLE:logic.getStatus()));

        super.drawInBackground(matrixStack, mouseX, mouseY, partialTicks);
    }
}
