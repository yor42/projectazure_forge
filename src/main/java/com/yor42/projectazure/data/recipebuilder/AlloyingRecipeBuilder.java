package com.yor42.projectazure.data.recipebuilder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.yor42.projectazure.gameobject.crafting.recipes.AlloyingRecipe;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import com.yor42.projectazure.setup.register.registerRecipes;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
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

    public AlloyingRecipeBuilder(ItemLike result, byte count, int processingTime) {
        this.result = result.asItem();
        this.count = count;
        this.processingTime = processingTime;
        this.ingredients = new ArrayList<>();
    }

    public static AlloyingRecipeBuilder AlloyRecipe(ItemLike result, byte count, int processingtime){
        return new AlloyingRecipeBuilder(result, count, processingtime);
    }

    public static AlloyingRecipeBuilder AlloyRecipe(ItemLike result, byte count){
        return AlloyingRecipeBuilder.AlloyRecipe(result, count, 300);
    }

    public static AlloyingRecipeBuilder AlloyRecipe(ItemLike result){
        return AlloyingRecipeBuilder.AlloyRecipe(result, (byte) 1);
    }

    public AlloyingRecipeBuilder addIngredient(Ingredient ingredient, byte count){
        this.ingredients.add(new Pair<>(ingredient, count));
        return this;
    }

    public AlloyingRecipeBuilder addIngredient(Ingredient ingredient){
        return this.addIngredient(ingredient, (byte) 1);
    }

    public void build(Consumer<FinishedRecipe> consumerIn) {
        this.build(consumerIn, ForgeRegistries.ITEMS.getKey(this.result));
    }

    public void build(Consumer<FinishedRecipe> consumerIn, String save) {
        ResourceLocation resourcelocation = Registry.ITEM.getKey(this.result);
        ResourceLocation resourcelocation1 = ResourceUtils.ModResourceLocation(save);
        if (resourcelocation1.equals(resourcelocation)) {
            throw new IllegalStateException("Recipe " + resourcelocation1 + " should remove its 'save' argument");
        } else {
            this.build(consumerIn, resourcelocation1);
        }
    }

    public void build(Consumer<FinishedRecipe> consumerIn, ResourceLocation id) {
        consumerIn.accept(new AlloyingRecipeBuilder.Result(id, "", this.ingredients, this.result, this.count, this.processingTime, registerRecipes.Serializers.ALLOYING.get()));
    }

    public static class Result implements FinishedRecipe {

        private final ResourceLocation id;
        public final List<Pair<Ingredient, Byte>> ingredients;
        private final String group;
        private final Item result;
        private final int cookingTime, outputcount;
        private final RecipeSerializer<AlloyingRecipe> serializer;

        public Result(ResourceLocation id, String group, List<Pair<Ingredient, Byte>> ingredients ,Item resultIn, int outputcount, int cookingTimeIn, RecipeSerializer<AlloyingRecipe> serializerIn){
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
