package com.yor42.projectazure.client.gui.multiblocked;

import com.google.common.collect.ImmutableMap;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.texture.TextTexture;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.utils.Position;
import com.lowdragmc.multiblocked.api.gui.controller.PageWidget;
import com.lowdragmc.multiblocked.api.recipe.Recipe;
import com.lowdragmc.multiblocked.api.recipe.RecipeLogic;
import com.lowdragmc.multiblocked.api.recipe.RecipeMap;
import com.lowdragmc.multiblocked.api.tile.ControllerTileEntity;
import com.yor42.projectazure.client.gui.buttons.multiblocked.RiftwayStartButtonWidget;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.RiftwayControllerTE;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.recipelogics.RiftwayRecipeLogic;
import com.yor42.projectazure.libs.utils.MathUtil;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RiftwayRecipePage extends PageWidget {

    public static ResourceTexture resourceTexture = new ResourceTexture(ResourceUtils.TextureLocation("gui/riftway"), 0,0,1,1);
    public static ResourceTexture ButtonTexture = new ResourceTexture(ResourceUtils.TextureLocation("gui/startbutton"), 0,0,1,1);

    public static ResourceTexture IdleIcon = new ResourceTexture(ResourceUtils.TextureLocation("gui/riftway"), 176/256F,0/256F,45/256F,62/256F);
    public static ResourceTexture ActiveIcon = new ResourceTexture(ResourceUtils.TextureLocation("gui/riftway"), 176/256F,62/256F,45/256F,62/256F);
    public static ResourceTexture SuspendIcon = new ResourceTexture(ResourceUtils.TextureLocation("gui/riftway"), 176/256F,124/256F,45/256F,62/256F);

    public final ControllerTileEntity controller;
    private Recipe recipe;
    public final DraggableScrollableWidgetGroup tips;
    @OnlyIn(Dist.CLIENT)
    private RiftwayRecipeWidget recipeWidget;
    private RecipeLogic.Status status;
    private int progress, fuelTime, fuelMaxTime = 1;

    public RiftwayRecipePage(ControllerTileEntity controller, TabContainer tabContainer) {
        super(resourceTexture, tabContainer);
        this.controller = controller;
        this.status = RecipeLogic.Status.IDLE;
        this.addWidget(tips = new DraggableScrollableWidgetGroup(9, 9, 158, 144));

        tips.addWidget(new LabelWidget(10, 50, () -> I18n.get("gui.riftway_status") + I18n.get(status.name)).setTextColor(0x00FF00));
        tips.addWidget(new LabelWidget(10, 60, () -> I18n.get("gui.riftway_remainingtime")).setTextColor(0x00FF00));
        tips.addWidget(new LabelWidget(10, 70, () -> I18n.get(MathUtil.Tick2StringClock(recipe==null?0:recipe.duration-progress))).setTextColor(0x00FF00));
        this.addWidget(new SwitchWidget(77, 206, 23, 32, (cd, r) -> {
            controller.asyncRecipeSearching = r;
            if (!cd.isRemote) {
                controller.markAsDirty();
            }
        })
                .setPressed(controller.asyncRecipeSearching)
                .setSupplier(() -> controller.asyncRecipeSearching)
                .setTexture(resourceTexture.getSubTexture(233 / 256.0, 0 / 256.0, 23 / 256.0, 32 / 256.0),
                        resourceTexture.getSubTexture(233 / 256.0, 32 / 256.0, 23 / 256.0, 32 / 256.0))
                .setHoverTooltips("Async/Sync recipes searching:",
                        "Async has better performance and only tries to match recipes when the internal contents changed",
                        "Sync always tries to match recipes, never miss matching recipes"));



        if (controller.getDefinition().getRecipeMap().isFuelRecipeMap()) {
            tips.addWidget(new LabelWidget(5, 35, () -> (status == RecipeLogic.Status.SUSPEND && fuelTime == 0) ? I18n.get("multiblocked.recipe.lack_fuel") : I18n.get("multiblocked.recipe.remaining_fuel", fuelTime / 20)).setTextColor(-1));
        }

        this.addWidget(new RiftwayStartButtonWidget(104,174, 64,64, ButtonTexture.getSubTexture(0F,0F,1F,0.5F), (ci)->{
            if(controller instanceof RiftwayControllerTE){
                if(controller.getRecipeLogic() instanceof RiftwayRecipeLogic){
                    ((RiftwayRecipeLogic) controller.getRecipeLogic()).startProcess(ci.clickedUserUUID);
                }
            }
        }).setHoverTexture(ButtonTexture.getSubTexture(0F,0.5F,1F,0.5F)));

        this.addWidget(new ImageWidget(3, 10, 162, 16,
                new TextTexture(controller.getUnlocalizedName(), 0x00FF00)
                        .setType(TextTexture.TextType.ROLL)
                        .setWidth(162)));

        this.addWidget(new WorkingStatusWidget(ImmutableMap.of(RecipeLogic.Status.IDLE, IdleIcon, RecipeLogic.Status.WORKING, ActiveIcon, RecipeLogic.Status.SUSPEND, SuspendIcon), this.controller.getRecipeLogic(), 115, 19, 45, 62));

    }

    private double getFuelProgress() {
        return Math.min(fuelTime, fuelMaxTime) * 1d / Math.max(1, fuelMaxTime);
    }

    @Override
    public void writeInitialData(PacketBuffer buffer) {
        super.writeInitialData(buffer);
        detectAndSendChanges();
        writeRecipe(buffer);
        writeStatus(buffer);
    }

    @Override
    public void readInitialData(PacketBuffer buffer) {
        super.readInitialData(buffer);
        readRecipe(buffer);
        readStatus(buffer);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if (controller.getRecipeLogic() != null) {
            RecipeLogic recipeLogic = controller.getRecipeLogic();
            if (recipe != recipeLogic.lastRecipe) {
                recipe = recipeLogic.lastRecipe;
                writeUpdateInfo(-1, this::writeRecipe);
            }
            if (status != recipeLogic.getStatus() || progress != recipeLogic.progress || fuelTime != recipeLogic.fuelTime || fuelMaxTime != recipeLogic.fuelMaxTime) {
                status = recipeLogic.getStatus();
                progress = recipeLogic.progress;
                fuelTime = recipeLogic.fuelTime;
                fuelMaxTime = recipeLogic.fuelMaxTime;
                writeUpdateInfo(-2, this::writeStatus);
            }
        } else if (recipe != null) {
            recipe = null;
            writeUpdateInfo(-1, this::writeRecipe);
        }
    }

    private void writeStatus(PacketBuffer buffer) {
        buffer.writeEnum(status);
        buffer.writeVarInt(progress);
        buffer.writeVarInt(fuelTime);
        buffer.writeVarInt(fuelMaxTime);
    }

    private void readStatus(PacketBuffer buffer) {
        status = buffer.readEnum(RecipeLogic.Status.class);
        progress = buffer.readVarInt();
        fuelTime = buffer.readVarInt();
        fuelMaxTime = buffer.readVarInt();
    }

    private void writeRecipe(PacketBuffer buffer) {
        if (recipe == null) {
            buffer.writeBoolean(false);
        }
        else {
            buffer.writeBoolean(true);
            buffer.writeUtf(recipe.uid);
        }
    }

    private void readRecipe(PacketBuffer buffer) {
        if (buffer.readBoolean()) {
            RecipeMap recipeMap = controller.getDefinition().getRecipeMap();
            recipe = recipeMap.recipes.get(buffer.readUtf());
            if (recipeWidget != null) {
                removeWidget(recipeWidget);
            }
            recipeWidget = new RiftwayRecipeWidget(
                    recipeMap,
                    recipe,
                    ProgressWidget.JEIProgress,
                    this::getFuelProgress);
            this.addWidget(recipeWidget);
            recipeWidget.setSelfPosition(new Position(19, 97));
        } else {
            if (recipeWidget != null) {
                removeWidget(recipeWidget);
            }
            RecipeMap recipeMap = controller.getDefinition().getRecipeMap();
            recipeWidget = new RiftwayRecipeWidget(
                    recipeMap,
                    null,
                    () -> 0,
                    this::getFuelProgress);
            addWidget(recipeWidget);
            recipeWidget.setSelfPosition(new Position(19, 97));
            status = RecipeLogic.Status.IDLE;
            progress = 0;
            fuelTime = 0;
            fuelMaxTime = 1;
        }
    }

    @Override
    public void readUpdateInfo(int id, PacketBuffer buffer) {
        if (id == -1) {
            readRecipe(buffer);
        } else if (id == -2) {
            readStatus(buffer);
        } else {
            super.readUpdateInfo(id, buffer);
        }
    }
}
