package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.gameobject.crafting.recipes.*;
import com.yor42.projectazure.libs.Constants;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class registerRecipes {

    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Constants.MODID);

    public static void register() {
    }

    public static class Types{
        public static final IRecipeType<PressingRecipe> PRESSING = IRecipeType.register(Constants.MODID+":pressing");
        public static final IRecipeType<CrushingRecipe> CRUSHING = IRecipeType.register(Constants.MODID+":crushing");
        public static final IRecipeType<AlloyingRecipe> ALLOYING = IRecipeType.register(Constants.MODID+":alloying");
        public static final IRecipeType<CrystalizingRecipe> CRYSTALIZING = IRecipeType.register(Constants.MODID+":crystalizing");

        public static final IRecipeType<BasicChemicalReactionRecipe> BASIC_CHEMICAL_REACTION = IRecipeType.register(Constants.MODID+":basic_chemicalreaction");
        public static void register(){}
    }

    public static class Serializers{

        public static final RegistryObject<IRecipeSerializer<PressingRecipe>> PRESSING = register("pressing", PressingRecipe.Serializer::new);
        public static final RegistryObject<IRecipeSerializer<CrushingRecipe>> CRUSHING = register("crushing", CrushingRecipe.Serializer::new);
        public static final RegistryObject<IRecipeSerializer<AlloyingRecipe>> ALLOYING = register("alloying", AlloyingRecipe.Serializer::new);
        public static final RegistryObject<IRecipeSerializer<CrystalizingRecipe>> CRYSTALIZING = register("crystalizing", CrystalizingRecipe.Serializer::new);
        public static final RegistryObject<IRecipeSerializer<BasicChemicalReactionRecipe>> BASICCHEMICALREACTION = register("basic_chemicalreaction", BasicChemicalReactionRecipe.Serializer::new);

        //Special Crafting Recipe
        public static final RegistryObject<IRecipeSerializer<ReloadRecipes>> RELOADING = register("reloading", ()-> new SpecialRecipeSerializer<>(ReloadRecipes::new));
        public static final RegistryObject<IRecipeSerializer<RepairRecipe>> REPAIRING = register("repairing", ()-> new SpecialRecipeSerializer<>(RepairRecipe::new));


        private static <T extends IRecipe<?>> RegistryObject<IRecipeSerializer<T>> register(String name, Supplier<IRecipeSerializer<T>> serializer){
            return RECIPE_SERIALIZERS.register(name, serializer);
        }

        public static void register(){};
    }



}
