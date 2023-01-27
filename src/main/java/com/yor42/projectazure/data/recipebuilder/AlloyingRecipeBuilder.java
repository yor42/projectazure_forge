package com.yor42.projectazure.data.recipebuilder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.gameobject.crafting.recipes.AlloyingRecipe;
import com.yor42.projectazure.setup.register.registerRecipes;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AlloyingRecipeBuilder {

    private final Item result;
    private final byte count;
    private final int processingTime;
    public final List<Pair<Ingredient, Byte>> ingredients;

    public AlloyingRecipeBuilder(IItemProvider result, byte count, int processingTime) {
        this.result = result.asItem();
        this.count = count;
        this.processingTime = processingTime;
        this.ingredients = new ArrayList<>();
    }

    public static AlloyingRecipeBuilder AlloyRecipe(IItemProvider result, byte count, int processingtime){
        return new AlloyingRecipeBuilder(result, count, processingtime);
    }

    public static AlloyingRecipeBuilder AlloyRecipe(IItemProvider result, byte count){
        return AlloyingRecipeBuilder.AlloyRecipe(result, count, 300);
    }

    public static AlloyingRecipeBuilder AlloyRecipe(IItemProvider result){
        return AlloyingRecipeBuilder.AlloyRecipe(result, (byte) 1);
    }

    public AlloyingRecipeBuilder addIngredient(Ingredient ingredient, byte count){
        this.ingredients.add(new Pair<>(ingredient, count));
        return this;
    }

    public AlloyingRecipeBuilder addIngredient(Ingredient ingredient){
        return this.addIngredient(ingredient, (byte) 1);
    }

    public void build(Consumer<IFinishedRecipe> consumerIn) {
        this.build(consumerIn, ForgeRegistries.ITEMS.getKey(this.result));
    }

    public void build(Consumer<IFinishedRecipe> consumerIn, String save) {
        ResourceLocation resourcelocation = Registry.ITEM.getKey(this.result);
        ResourceLocation resourcelocation1 = new ResourceLocation(save);
        if (resourcelocation1.equals(resourcelocation)) {
            throw new IllegalStateException("Recipe " + resourcelocation1 + " should remove its 'save' argument");
        } else {
            this.build(consumerIn, resourcelocation1);
        }
    }

    public void build(Consumer<IFinishedRecipe> consumerIn, ResourceLocation id) {
        consumerIn.accept(new AlloyingRecipeBuilder.Result(id, "", this.ingredients, this.result, this.count, this.processingTime, registerRecipes.Serializers.ALLOYING.get()));
    }

    public static class Result implements IFinishedRecipe {

        private final ResourceLocation id;
        public final List<Pair<Ingredient, Byte>> ingredients;
        private final String group;
        private final Item result;
        private final int cookingTime, outputcount;
        private final IRecipeSerializer<AlloyingRecipe> serializer;

        public Result(ResourceLocation id, String group, List<Pair<Ingredient, Byte>> ingredients ,Item resultIn, int outputcount, int cookingTimeIn, IRecipeSerializer<AlloyingRecipe> serializerIn){
            this.id = id;
            this.group = group;
            this.ingredients = ingredients;
            this.result = resultIn;
            this.outputcount = outputcount;
            this.cookingTime = cookingTimeIn;
            this.serializer = serializerIn;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            if (!this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }

            JsonArray input = new JsonArray();
            for(Pair<Ingredient, Byte> material : this.ingredients)
            {
                JsonObject resultObject = new JsonObject();
                resultObject.add("item", material.getFirst().toJson());
                if(material.getSecond() > 1)
                    resultObject.addProperty("count", material.getSecond());
                input.add(resultObject);
            }
            json.add("materials", input);

            json.addProperty("result", ForgeRegistries.ITEMS.getKey(this.result).toString());
            json.addProperty("resultcount", this.outputcount);
            json.addProperty("processtime", this.cookingTime);

        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public IRecipeSerializer<?> getType() {
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
