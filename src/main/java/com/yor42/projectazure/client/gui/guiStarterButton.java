package com.yor42.projectazure.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yor42.projectazure.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class guiStarterButton extends ImageButton {
    private final ResourceLocation resourceLocation;
    private final EntityType entityType;
    private final int xTexStart;
    private final int yTexStart;
    private final int xDiffText;
    private final int index;

    public guiStarterButton(int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int xDiffTextIn, ResourceLocation resourceLocationIn, int idx ,EntityType type , IPressable onPressIn) {
        super(xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, xDiffTextIn, resourceLocationIn, onPressIn);
        this.width = widthIn;
        this.height = heightIn;
        this.xTexStart = xTexStartIn;
        this.yTexStart = yTexStartIn;
        this.xDiffText = xDiffTextIn;
        this.resourceLocation = resourceLocationIn;
        this.entityType = type;
        this.index = idx;
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        int logox, logoy;

        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindTexture(this.resourceLocation);
        int i = this.xTexStart;
        if (this.isHovered()) {
            i += this.xDiffText;
        }

        RenderSystem.enableDepthTest();
        blit(matrixStack, this.x, this.y, (float)i, (float)yTexStart, this.width, this.height, 256, 256);



        switch (this.index){
            case 0:
                logox = 109;
                logoy = 127;
                break;

            case 1:
                logox = 163;
                logoy = 127;

            default:
                logox = 1;
                logoy = 127;
                break;
        }

        /** Cache of entities for each entity type */
        final Map<EntityType<?>,Entity> ENTITY_MAP = new HashMap<>();

        blit(matrixStack, this.x+2, this.y+2, logox, logoy, 53, 53, 256, 256);

        if (entityType != null) {
            World world = Minecraft.getInstance().world;
                if (world != null){
                    Entity entity;

                    entity = ENTITY_MAP.computeIfAbsent(entityType, t -> t.create(world));

                    if (entity instanceof LivingEntity) {
                        LivingEntity livingEntity = (LivingEntity) entity;

                        int height = (int) entity.getHeight();
                        int width = (int) entity.getWidth();
                        try {
                            InventoryScreen.drawEntityOnScreen(x + width / 2, y + height, 1, 0, 10, livingEntity);
                            return;
                        } catch (Exception e) {
                            Main.LOGGER.error("Failed to render Entity!");
                            ENTITY_MAP.remove(entityType);
                        }

                    }
            }
        }

        if (this.isHovered()) {
            this.renderToolTip(matrixStack, mouseX, mouseY);
        }

    }
}
