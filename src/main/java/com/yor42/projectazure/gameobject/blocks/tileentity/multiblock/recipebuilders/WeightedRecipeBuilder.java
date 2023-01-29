package com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.recipebuilders;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.lowdragmc.multiblocked.api.capability.MultiblockCapability;
import com.lowdragmc.multiblocked.api.recipe.Content;
import com.lowdragmc.multiblocked.api.recipe.Recipe;
import com.lowdragmc.multiblocked.api.recipe.RecipeBuilder;
import com.lowdragmc.multiblocked.api.recipe.RecipeMap;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.recipes.RiftwayRecipes;
import com.yor42.projectazure.gameobject.blocks.tileentity.multiblock.recipes.WeightedRandomRecipe;

import java.util.Map;
import java.util.UUID;

public class WeightedRecipeBuilder extends RecipeBuilder {

    public WeightedRecipeBuilder(RecipeMap recipeMap) {
        super(recipeMap);
    }

    public WeightedRandomRecipe buildWeighted() {
        ImmutableMap.Builder<MultiblockCapability<?>, ImmutableList<Content>> inputBuilder = new ImmutableMap.Builder<>();
        for (Map.Entry<MultiblockCapability<?>, ImmutableList.Builder<Content>> entry : this.inputBuilder.entrySet()) {
            inputBuilder.put(entry.getKey(), entry.getValue().build());
        }
        ImmutableMap.Builder<MultiblockCapability<?>, ImmutableList<Content>> outputBuilder = new ImmutableMap.Builder<>();
        for (Map.Entry<MultiblockCapability<?>, ImmutableList.Builder<Content>> entry : this.outputBuilder.entrySet()) {
            outputBuilder.put(entry.getKey(), entry.getValue().build());
        }
        ImmutableMap.Builder<MultiblockCapability<?>, ImmutableList<Content>> tickInputBuilder = new ImmutableMap.Builder<>();
        for (Map.Entry<MultiblockCapability<?>, ImmutableList.Builder<Content>> entry : this.tickInputBuilder.entrySet()) {
            tickInputBuilder.put(entry.getKey(), entry.getValue().build());
        }
        ImmutableMap.Builder<MultiblockCapability<?>, ImmutableList<Content>> tickOutputBuilder = new ImmutableMap.Builder<>();
        for (Map.Entry<MultiblockCapability<?>, ImmutableList.Builder<Content>> entry : this.tickOutputBuilder.entrySet()) {
            tickOutputBuilder.put(entry.getKey(), entry.getValue().build());
        }
        return new WeightedRandomRecipe(fixedName == null ? UUID.randomUUID().toString() : fixedName, inputBuilder.build(), outputBuilder.build(), tickInputBuilder.build(), tickOutputBuilder.build(), ImmutableList.copyOf(conditions), data.isEmpty() ? Recipe.EMPTY : ImmutableMap.copyOf(data), text, duration);
    }

    public void buildAndRegisterWeighted() {
        buildAndRegisterWeighted(false);
    }

    public void buildAndRegisterWeighted(boolean isFuel) {
        if (isFuel) {
            recipeMap.addFuelRecipe(buildWeighted());
        } else {
            recipeMap.addRecipe(buildWeighted());
        }
    }

    public RiftwayRecipes buildRiftWay() {
        ImmutableMap.Builder<MultiblockCapability<?>, ImmutableList<Content>> inputBuilder = new ImmutableMap.Builder<>();
        for (Map.Entry<MultiblockCapability<?>, ImmutableList.Builder<Content>> entry : this.inputBuilder.entrySet()) {
            inputBuilder.put(entry.getKey(), entry.getValue().build());
        }
        ImmutableMap.Builder<MultiblockCapability<?>, ImmutableList<Content>> outputBuilder = new ImmutableMap.Builder<>();
        for (Map.Entry<MultiblockCapability<?>, ImmutableList.Builder<Content>> entry : this.outputBuilder.entrySet()) {
            outputBuilder.put(entry.getKey(), entry.getValue().build());
        }
        ImmutableMap.Builder<MultiblockCapability<?>, ImmutableList<Content>> tickInputBuilder = new ImmutableMap.Builder<>();
        for (Map.Entry<MultiblockCapability<?>, ImmutableList.Builder<Content>> entry : this.tickInputBuilder.entrySet()) {
            tickInputBuilder.put(entry.getKey(), entry.getValue().build());
        }
        ImmutableMap.Builder<MultiblockCapability<?>, ImmutableList<Content>> tickOutputBuilder = new ImmutableMap.Builder<>();
        for (Map.Entry<MultiblockCapability<?>, ImmutableList.Builder<Content>> entry : this.tickOutputBuilder.entrySet()) {
            tickOutputBuilder.put(entry.getKey(), entry.getValue().build());
        }
        return new RiftwayRecipes(fixedName == null ? UUID.randomUUID().toString() : fixedName, inputBuilder.build(), outputBuilder.build(), tickInputBuilder.build(), tickOutputBuilder.build(), ImmutableList.copyOf(conditions), data.isEmpty() ? Recipe.EMPTY : ImmutableMap.copyOf(data), text, duration);
    }

    public void buildAndRegisterRiftway() {
        buildAndRegisterRiftway(false);
    }

    public void buildAndRegisterRiftway(boolean isFuel) {
        if (isFuel) {
            recipeMap.addFuelRecipe(buildRiftWay());
        } else {
            recipeMap.addRecipe(buildRiftWay());
        }
    }
}
