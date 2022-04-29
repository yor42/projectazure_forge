package com.yor42.projectazure.setup;

import com.yor42.projectazure.gameobject.crafting.CrushingRecipe;
import com.yor42.projectazure.libs.utils.ItemStackWithChance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*
 * This class is distributed as part of the Ex Nihilo Sequentia Mod.
 * Get the Source Code in github:
 * https://github.com/NovaMachina-Mods/ExNihiloSequentia
 *
 * Ex Nihilo Sequentia is Open Source and distributed under the
 * CC BY-NC-SA 4.0 License: https://creativecommons.org/licenses/by-nc-sa/4.0/deed.en
 */
public class CrushingRecipeCache {
    private static final List<CrushingRecipe> recipeList = new ArrayList<>();

    private final Map<Block, CrushingRecipe> recipeByBlockCache = new HashMap<>();

    public List<ItemStackWithChance> getResult(Block input) {
        List<ItemStackWithChance> returnList = findRecipe(input).getOutput();
        return returnList;
    }

    public boolean isHammerable(Block block) {
        return findRecipe(block) != CrushingRecipe.EMPTY;
    }

    public CrushingRecipe findRecipe(Block block) {
        return recipeByBlockCache.computeIfAbsent(block, k -> {
            final ItemStack itemStack = new ItemStack(block);
            return recipeList
                    .stream()
                    .filter(recipe -> recipe.getInput().test(itemStack))
                    .findFirst()
                    .orElse(CrushingRecipe.EMPTY);
        });
    }

    public void setRecipes(List<CrushingRecipe> recipes) {
        recipeList.addAll(recipes);

        recipeByBlockCache.clear();
    }

    public List<CrushingRecipe> getRecipeList() {
        return recipeList;
    }

    public void clearRecipes() {
        recipeList.clear();

        recipeByBlockCache.clear();
    }
    
}
