package com.yor42.projectazure.client.gui.multiblocked;

import com.google.common.collect.ImmutableList;
import com.lowdragmc.lowdraglib.LDLMod;
import com.lowdragmc.lowdraglib.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib.gui.texture.ProgressTexture;
import com.lowdragmc.lowdraglib.gui.texture.ResourceTexture;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.jei.JEIPlugin;
import com.lowdragmc.multiblocked.api.capability.IO;
import com.lowdragmc.multiblocked.api.capability.MultiblockCapability;
import com.lowdragmc.multiblocked.api.recipe.Content;
import com.lowdragmc.multiblocked.api.recipe.Recipe;
import com.lowdragmc.multiblocked.api.recipe.RecipeCondition;
import com.lowdragmc.multiblocked.api.recipe.RecipeMap;
import com.lowdragmc.multiblocked.jei.recipepage.RecipeMapCategory;
import com.lowdragmc.multiblocked.jei.recipepage.RecipeMapFuelCategory;
import com.lowdragmc.multiblocked.rei.recipepage.RecipeMapDisplayCategory;
import com.lowdragmc.multiblocked.rei.recipepage.RecipeMapFuelDisplayCategory;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import me.shedaniel.rei.api.client.view.ViewSearchBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.DoubleSupplier;

public class RiftwayRecipeWidget extends WidgetGroup {

    public final RecipeMap recipeMap;
    public final Recipe recipe;
    public final DraggableScrollableWidgetGroup inputs;
    public final DraggableScrollableWidgetGroup outputs;

    public static ResourceTexture resourceTexture = new ResourceTexture(ResourceUtils.TextureLocation("gui/riftway"), 199F/256F,186F/256F,52F/256F,52F/256F);
    public static ResourceTexture Progress = new ResourceTexture(ResourceUtils.TextureLocation("gui/riftway"), 221F/256F,154F/256F,21F/256F,32F/256F);
    public static ColorRectTexture dark = new ColorRectTexture(0x1f000000);
    public RiftwayRecipeWidget(RecipeMap recipeMap, @Nullable Recipe recipe, DoubleSupplier progress, DoubleSupplier fuel) {
        super(0, 0, 133, 52);
        this.recipeMap = recipeMap;
        this.recipe = recipe;
        setClientSideWidget();
        IGuiTexture overlay = new ColorRectTexture(0x1f000000);
        inputs = new DraggableScrollableWidgetGroup(0, 0, 52, 52).setBackground(resourceTexture);
        outputs = new DraggableScrollableWidgetGroup(81, 0, 52, 52).setBackground(resourceTexture);
        this.addWidget(inputs);
        this.addWidget(outputs);

        this.addWidget(new ProgressWidget(progress, 56, 18, 21, 16, Progress).setFillDirection(ProgressTexture.FillDirection.LEFT_TO_RIGHT));

        this.addWidget(new ButtonWidget(78, 27, 20, 20, IGuiTexture.EMPTY, cd -> {
            if (LDLMod.isJeiLoaded()) {
                JEIPlugin.jeiRuntime.getRecipesGui().showTypes(Collections.singletonList(RecipeMapCategory.TYPES.apply(recipeMap)));
            } else if (LDLMod.isReiLoaded()) {
                ViewSearchBuilder.builder().addCategory(RecipeMapDisplayCategory.CATEGORIES.apply(recipeMap)).open();
            }
        }).setHoverTexture(overlay));
        if (recipeMap.isFuelRecipeMap()) {
            this.addWidget(new ProgressWidget(fuel, 78, 47, 20, 20, recipeMap.fuelTexture).setFillDirection(ProgressTexture.FillDirection.DOWN_TO_UP));
            this.addWidget(new ButtonWidget(78, 47, 20, 20, IGuiTexture.EMPTY, cd -> {
                if (LDLMod.isJeiLoaded()) {
                    JEIPlugin.jeiRuntime.getRecipesGui().showTypes(Collections.singletonList(RecipeMapFuelCategory.TYPES.apply(recipeMap)));
                } else if (LDLMod.isReiLoaded()) {
                    ViewSearchBuilder.builder().addCategory(RecipeMapFuelDisplayCategory.CATEGORIES.apply(recipeMap)).open();
                }
            }).setHoverTexture(overlay));
        }

        if (recipe == null) return;

        int index = 0;
        for (Map.Entry<MultiblockCapability<?>, ImmutableList<Content>> entry : recipe.inputs.entrySet()) {
            MultiblockCapability<?> capability = entry.getKey();
            for (Content in : entry.getValue()) {
                inputs.addWidget(capability.createContentWidget().setContent(IO.IN, in, false).setSelfPosition(1 + 17 * (index % 3), 1 + 17 * (index / 3)));
                index++;
            }
        }
        for (Map.Entry<MultiblockCapability<?>, ImmutableList<Content>> entry : recipe.tickInputs.entrySet()) {
            MultiblockCapability<?> capability = entry.getKey();
            for (Content in : entry.getValue()) {
                inputs.addWidget(capability.createContentWidget().setContent(IO.IN, in, true).setSelfPosition(1 + 17 * (index % 3), 1 + 17 * (index / 3)));
                index++;
            }
        }
        if (index > 9) {
            inputs.setYScrollBarWidth(4).setYBarStyle(null, new ColorRectTexture(-1));
        }

        index = 0;
        for (Map.Entry<MultiblockCapability<?>, ImmutableList<Content>> entry : recipe.outputs.entrySet()) {
            MultiblockCapability<?> capability = entry.getKey();
            for (Content out : entry.getValue()) {
                outputs.addWidget(capability.createContentWidget().setContent(IO.OUT, out, false).setSelfPosition( 17 * (index % 3), 17 * (index / 3)));
                index++;
            }
        }
        for (Map.Entry<MultiblockCapability<?>, ImmutableList<Content>> entry : recipe.tickOutputs.entrySet()) {
            MultiblockCapability<?> capability = entry.getKey();
            for (Content out : entry.getValue()) {
                outputs.addWidget(capability.createContentWidget().setContent(IO.OUT, out, true).setSelfPosition(1 + 17 * (index % 3), 1 + 17 * (index / 3)));
                index++;
            }
        }
        if (index > 9) {
            outputs.setYScrollBarWidth(4).setYBarStyle(null, new ColorRectTexture(-1));
        }
        Map<String, List<RecipeCondition>> conditionMap = new HashMap<>();
        for (RecipeCondition condition : recipe.conditions) {
            if (condition.isReverse()) {
                conditionMap.computeIfAbsent(condition.getType(), s->new ArrayList<>()).add(condition);
            } else {
                conditionMap.computeIfAbsent(condition.getType(), s->new ArrayList<>()).add(0, condition);
            }
        }

        index = 0;
        for (Map.Entry<String, List<RecipeCondition>> entry : conditionMap.entrySet()) {
            List<RecipeCondition> list = entry.getValue();
            if (!list.isEmpty()) {
                index++;
                boolean reversed = false;
                List<Component> components = new ArrayList<>();
                for (RecipeCondition condition : list) {
                    if (!reversed && condition.isReverse()) {
                        reversed = true;
                        components.add(new TranslatableComponent("multiblocked.gui.condition.reverse"));
                    }
                    components.add(condition.getTooltips());
                }
                this.addWidget(new ImageWidget(75 - index * 16, 98, 16, 16, list.get(0).getValidTexture()).setHoverTooltips(components));
            }

        }
    }
}
