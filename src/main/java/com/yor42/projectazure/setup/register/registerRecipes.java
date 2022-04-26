package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.gameobject.crafting.*;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class registerRecipes {

    public static class Types{
        public static final RecipeType<PressingRecipe> PRESSING = RecipeType.register(Constants.MODID+":pressing");
        public static final RecipeType<CrushingRecipe> CRUSHING = RecipeType.register(Constants.MODID+":crushing");
        public static final RecipeType<AlloyingRecipe> ALLOYING = RecipeType.register(Constants.MODID+":alloying");
        public static final RecipeType<CrystalizingRecipe> CRYSTALIZING = RecipeType.register(Constants.MODID+":crystalizing");
        public static void register(){}
    }

    public static class Serializers{

        public static final RegistryObject<RecipeSerializer<PressingRecipe>> PRESSING = register("pressing", PressingRecipe.Serializer::new);
        public static final RegistryObject<RecipeSerializer<CrushingRecipe>> CRUSHING = register("crushing", CrushingRecipe.Serializer::new);
        public static final RegistryObject<RecipeSerializer<AlloyingRecipe>> ALLOYING = register("alloying", AlloyingRecipe.Serializer::new);
        public static final RegistryObject<RecipeSerializer<CrystalizingRecipe>> CRYSTALIZING = register("crystalizing", CrystalizingRecipe.Serializer::new);

        //Special Crafting Recipe
        public static final RegistryObject<RecipeSerializer<ReloadRecipes>> RELOADING = register("reloading", ()-> new SpecialRecipeSerializer<>(ReloadRecipes::new));
        public static final RegistryObject<RecipeSerializer<RepairRecipe>> REPAIRING = register("repairing", ()-> new SpecialRecipeSerializer<>(RepairRecipe::new));


        private static <T extends Recipe<?>> RegistryObject<RecipeSerializer<T>> register(String name, Supplier<RecipeSerializer<T>> serializer){
            return registerManager.RECIPE_SERIALIZERS.register(name, serializer);
        }

        private static <T extends Recipe<?>> RegistryObject<RecipeSerializer<T>> register_special_recipe(String name, Supplier<RecipeSerializer<T>> serializer){
            return registerManager.RECIPE_SERIALIZERS.register(name, serializer);
        }

        public static void register(){}
    }



}
