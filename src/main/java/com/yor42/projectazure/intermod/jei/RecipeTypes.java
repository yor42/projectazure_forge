package com.yor42.projectazure.intermod.jei;

import com.yor42.projectazure.gameobject.crafting.AlloyingRecipe;
import com.yor42.projectazure.gameobject.crafting.CrushingRecipe;
import com.yor42.projectazure.gameobject.crafting.CrystalizingRecipe;
import com.yor42.projectazure.gameobject.crafting.PressingRecipe;
import com.yor42.projectazure.libs.Constants;
import mezz.jei.api.constants.ModIds;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.crafting.CraftingRecipe;

public class RecipeTypes {
    public static final RecipeType<AlloyingRecipe> ALLOYING =
            RecipeType.create(Constants.MODID, AlloyingRecipe.TYPE.ID, AlloyingRecipe.class);

    public static final RecipeType<CrystalizingRecipe> CRYSTALIZING =
            RecipeType.create(Constants.MODID, CrystalizingRecipe.TYPE.ID, CrystalizingRecipe.class);

    public static final RecipeType<PressingRecipe> PRESSING =
            RecipeType.create(Constants.MODID, PressingRecipe.TYPE.ID, PressingRecipe.class);

    public static final RecipeType<CrushingRecipe> CRUSHING =
            RecipeType.create(Constants.MODID, CrushingRecipe.TYPE.ID, CrushingRecipe.class);
}
