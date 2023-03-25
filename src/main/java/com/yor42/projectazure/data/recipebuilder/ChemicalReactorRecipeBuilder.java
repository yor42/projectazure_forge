package com.yor42.projectazure.data.recipebuilder;

import com.google.gson.JsonObject;
import com.yor42.projectazure.gameobject.crafting.recipes.BasicChemicalReactionRecipe;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import com.yor42.projectazure.setup.register.registerRecipes;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class ChemicalReactorRecipeBuilder {

    private final Fluid result;
    private final Ingredient input;
    private final int processtime, resultamount;

    private ChemicalReactorRecipeBuilder(Fluid result, int resultamount, Ingredient input, int processtime) {
        this.result = result;
        this.resultamount = resultamount;
        this.input = input;
        this.processtime = processtime;
    }

    public static void add(Fluid result, int resultamount, Ingredient input, int processtime, Consumer<FinishedRecipe> consumerIn){
        new ChemicalReactorRecipeBuilder(result, resultamount, input, processtime).build(consumerIn);
    }

    public static void add(Fluid result, int resultamount, Ingredient input, Consumer<FinishedRecipe> consumerIn){
        new ChemicalReactorRecipeBuilder(result, resultamount, input, 200).build(consumerIn);
    }

    public static void add(Fluid result, int resultamount, Ingredient input, int processtime, String id, Consumer<FinishedRecipe> consumerIn){
        new ChemicalReactorRecipeBuilder(result, resultamount, input, processtime).build(consumerIn, id);
    }

    public void build(Consumer<FinishedRecipe> consumerIn) {
        this.build(consumerIn, this.result.getRegistryName());
    }

    public void build(Consumer<FinishedRecipe> consumerIn, String save) {
        ResourceLocation resourcelocation = this.result.getRegistryName();
        ResourceLocation resourcelocation1 = ResourceUtils.ModResourceLocation(save);
        if (resourcelocation1.equals(resourcelocation)) {
            throw new IllegalStateException("Recipe " + resourcelocation1 + " should remove its 'save' argument");
        } else {
            this.build(consumerIn, resourcelocation1);
        }
    }

    public void build(Consumer<FinishedRecipe> consumerIn, ResourceLocation id) {
        consumerIn.accept(new Result(id, this.result,this.resultamount, this.input, this.processtime, registerRecipes.Serializers.BASICCHEMICALREACTION.get()));
    }


    public static class Result implements FinishedRecipe{

        private final Fluid result;
        private final Ingredient input;
        private final int processtime, resultamount;
        private final ResourceLocation id;
        private final RecipeSerializer<BasicChemicalReactionRecipe> serializer;
        public Result(ResourceLocation id, Fluid result, int resultamount, Ingredient input, int processtime, RecipeSerializer<BasicChemicalReactionRecipe> serializer) {
            this.id = id;
            this.result = result;
            this.resultamount = resultamount;
            this.input = input;
            this.processtime = processtime;
            this.serializer = serializer;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            JsonObject output = new JsonObject();
            output.addProperty("fluid", ForgeRegistries.FLUIDS.getKey(this.result).toString());
            output.addProperty("amount", this.resultamount);
            json.add("output", output);
            json.add("input", this.input.toJson());
            json.addProperty("processtime", this.processtime);
        }

        @Nonnull
        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Nonnull
        @Override
        public RecipeSerializer<?> getType() {
            return this.serializer;
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}
