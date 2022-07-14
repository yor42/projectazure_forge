package com.yor42.projectazure.client.gui.buttons;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yor42.projectazure.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

import static com.yor42.projectazure.libs.utils.RenderingUtils.renderEntityInInventory;

public class buttonStarterSelect extends ImageButton {
    private final ResourceLocation resourceLocation;
    private final EntityType entityType;
    private int x;
    private final int y;
    private final int xTexStart;
    private final int yTexStart;
    private final int xDiffText;
    private final int index;


    public buttonStarterSelect(int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int xDiffTextIn, ResourceLocation resourceLocationIn, int idx , EntityType type , IPressable onPressIn) {
        super(xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, xDiffTextIn, resourceLocationIn, onPressIn);
        this.x = xIn;
        this.y = yIn;
        this.width = widthIn;
        this.height = heightIn;
        this.xTexStart = xTexStartIn;
        this.yTexStart = yTexStartIn;
        this.xDiffText = xDiffTextIn;
        this.resourceLocation = resourceLocationIn;
        this.entityType = type;
        this.index = idx;
    }

    public void setx(int x){
        this.x = x;
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        int logox, logoy;

        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bind(this.resourceLocation);
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
                logoy = 181;
                break;

            case 2:
                logox = 55;
                logoy = 127;
                break;
            case 3:
                logox = 163;
                logoy = 127;
                break;

            default:
                logox = 1;
                logoy = 127;
                break;
        }

        /** Cache of entities for each entity type */
        final Map<EntityType<?>,Entity> ENTITY_MAP = new HashMap<>();

        blit(matrixStack, this.x+2, this.y+2, logox, logoy, 53, 53, 256, 256);

        if (this.entityType != null) {
            World world = Minecraft.getInstance().level;
                if (world != null){
                    Entity entity;

                    entity = ENTITY_MAP.computeIfAbsent(this.entityType, t -> t.create(world));

                    if (entity instanceof LivingEntity) {
                        LivingEntity livingEntity = (LivingEntity) entity;

                        int entityWidth = (int) entity.getBbWidth();
                        try {
                            renderEntityInInventory(this.x+(this.width/2), this.y+this.height-5, 40, mouseX, mouseY, livingEntity);
                            return;
                        } catch (Exception e) {
                            Main.LOGGER.error("Failed to render Entity!");
                            ENTITY_MAP.remove(this.entityType);
                        }

                    }
            }
        }


        if (this.isHovered()) {
            this.renderToolTip(matrixStack, mouseX, mouseY);
        }

    }
}
