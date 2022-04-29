package com.yor42.projectazure.intermod.jei;

import com.yor42.projectazure.gameobject.crafting.AlloyingRecipe;
import com.yor42.projectazure.gameobject.crafting.CrystalizingRecipe;
import com.yor42.projectazure.gameobject.crafting.PressingRecipe;
import com.yor42.projectazure.libs.Constants;
import mezz.jei.api.constants.ModIds;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.crafting.CraftingRecipe;

public class RecipeTypes {
    public static final RecipeType<AlloyingRecipe> ALLOYING =
            RecipeType.create(Constants.MODID, "alloying", AlloyingRecipe.class);

    public static final RecipeType<CrystalizingRecipe> CRYSTALIZING =
            RecipeType.create(Constants.MODID, "crystalizing", CrystalizingRecipe.class);

    public static final RecipeType<PressingRecipe> PRESSING =
            RecipeType.create(Constants.MODID, "pressing", PressingRecipe.class);
}
