package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.gameobject.crafting.recipes.*;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.tconstruct.TConstruct;

import java.util.function.Supplier;

public class registerRecipes {



    public static void register() {
    }

    public static class Types{
        public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registry.RECIPE_TYPE_REGISTRY, Constants.MODID);
        public static final RegistryObject<RecipeType<PressingRecipe>> PRESSING = (RegistryObject)register("pressing");
        public static final RegistryObject<RecipeType<CrushingRecipe>> CRUSHING = (RegistryObject)register("crushing");
        public static final RegistryObject<RecipeType<AlloyingRecipe>> ALLOYING = (RegistryObject)register("alloying");
        public static final RegistryObject<RecipeType<CrystalizingRecipe>> CRYSTALIZING = (RegistryObject)register("crystalizing");
        public static final RegistryObject<RecipeType<BasicChemicalReactionRecipe>> BASIC_CHEMICAL_REACTION = (RegistryObject)register("basic_chemicalreaction");

        static RegistryObject<RecipeType<?>> register(String name)
        {
            return RECIPE_TYPES.register(name, () -> new RecipeType<>()
            {
                @Override
                public String toString() {
                    return Constants.MODID + ":" + name;
                }
            });
        }
        public static void register(){}
    }

    public static class Serializers{
        public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Constants.MODID);
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
