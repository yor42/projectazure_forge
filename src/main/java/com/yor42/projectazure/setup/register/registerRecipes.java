package com.yor42.projectazure.setup.register;

import com.yor42.projectazure.gameobject.crafting.PressingRecipe;
import com.yor42.projectazure.libs.defined;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class registerRecipes {

    public static class Types{
        public static final IRecipeType<PressingRecipe> PRESSING = IRecipeType.register(defined.MODID+"pressing");
    }

    public static class Serializers{

        public static final RegistryObject<IRecipeSerializer<PressingRecipe>> PRESSING = register("pressing", PressingRecipe.Serializer::new);

        private static <T extends IRecipe<?>> RegistryObject<IRecipeSerializer<T>> register(String name, Supplier<IRecipeSerializer<T>> serializer){
            return registerManager.RECIPE_SERIALIZERS.register(name, serializer);
        }
    }

}
