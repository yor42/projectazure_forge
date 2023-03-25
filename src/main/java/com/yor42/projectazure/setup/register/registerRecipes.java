package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.gameobject.crafting.recipes.*;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class registerRecipes {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Constants.MODID);

    public static void register() {
    }

    public static class Types{
        public static final RecipeType<PressingRecipe> PRESSING = RecipeType.register(Constants.MODID+":pressing");
        public static final RecipeType<CrushingRecipe> CRUSHING = RecipeType.register(Constants.MODID+":crushing");
        public static final RecipeType<AlloyingRecipe> ALLOYING = RecipeType.register(Constants.MODID+":alloying");
        public static final RecipeType<CrystalizingRecipe> CRYSTALIZING = RecipeType.register(Constants.MODID+":crystalizing");

        public static final RecipeType<BasicChemicalReactionRecipe> BASIC_CHEMICAL_REACTION = RecipeType.register(Constants.MODID+":basic_chemicalreaction");
        public static void register(){}
    }

    public static class Serializers{

        public static final RegistryObject<RecipeSerializer<PressingRecipe>> PRESSING = register("pressing", PressingRecipe.Serializer::new);
        public static final RegistryObject<RecipeSerializer<CrushingRecipe>> CRUSHING = register("crushing", CrushingRecipe.Serializer::new);
        public static final RegistryObject<RecipeSerializer<AlloyingRecipe>> ALLOYING = register("alloying", AlloyingRecipe.Serializer::new);
        public static final RegistryObject<RecipeSerializer<CrystalizingRecipe>> CRYSTALIZING = register("crystalizing", CrystalizingRecipe.Serializer::new);
        public static final RegistryObject<RecipeSerializer<BasicChemicalReactionRecipe>> BASICCHEMICALREACTION = register("basic_chemicalreaction", BasicChemicalReactionRecipe.Serializer::new);

        //Special Crafting Recipe
        public static final RegistryObject<RecipeSerializer<ReloadRecipes>> RELOADING = register("reloading", ()-> new SimpleRecipeSerializer<>(ReloadRecipes::new));
        public static final RegistryObject<RecipeSerializer<RepairRecipe>> REPAIRING = register("repairing", ()-> new SimpleRecipeSerializer<>(RepairRecipe::new));
        public static final RegistryObject<RecipeSerializer<TransferPotiontoSyringeRecipe>> TRANSFERPOTION = register("transerpotion", ()-> new SimpleRecipeSerializer<>(TransferPotiontoSyringeRecipe::new));

        public static final RegistryObject<RecipeSerializer<SawingSiliconeRecipe>> SAW_SILICON = register("sawsilicon", ()-> new SimpleRecipeSerializer<>(SawingSiliconeRecipe::new));


        private static <T extends Recipe<?>> RegistryObject<RecipeSerializer<T>> register(String name, Supplier<RecipeSerializer<T>> serializer){
            return RECIPE_SERIALIZERS.register(name, serializer);
        }

        public static void register(){}
    }



}
