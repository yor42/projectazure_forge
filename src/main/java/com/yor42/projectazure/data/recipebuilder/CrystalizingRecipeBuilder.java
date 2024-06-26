package com.yor42.projectazure.data.recipebuilder;

import com.google.gson.JsonObject;
import com.yor42.projectazure.gameobject.crafting.recipes.CrystalizingRecipe;
import com.yor42.projectazure.libs.utils.ResourceUtils;
import com.yor42.projectazure.setup.register.registerRecipes;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class CrystalizingRecipeBuilder {

    private final Item result;
    private final Ingredient seed;
    private final Fluid solution;
    private final int growthTime;

    public CrystalizingRecipeBuilder(ItemLike result, Ingredient seed, Fluid solution, int growthTime){
        this.result = result.asItem();
        this.seed = seed;
        this.solution = solution;
        this.growthTime = growthTime;
    }

    public static CrystalizingRecipeBuilder addRecipe(ItemLike result, Ingredient seed, Fluid solution, int growthTime){
        return new CrystalizingRecipeBuilder(result, seed, solution, growthTime);
    }

    public void build(Consumer<FinishedRecipe> consumerIn) {
        this.build(consumerIn, ForgeRegistries.ITEMS.getKey(this.result));
    }

    public void build(Consumer<FinishedRecipe> consumerIn, String save) {
        ResourceLocation resourcelocation = ForgeRegistries.ITEMS.getKey(this.result);
        ResourceLocation resourcelocation1 = ResourceUtils.ModResourceLocation(save);
        if (resourcelocation1.equals(resourcelocation)) {
            throw new IllegalStateException("Recipe " + resourcelocation1 + " should remove its 'save' argument");
        } else {
            this.build(consumerIn, resourcelocation1);
        }
    }

    public void build(Consumer<FinishedRecipe> consumerIn, ResourceLocation id) {
        consumerIn.accept(new CrystalizingRecipeBuilder.Result(id, "", this.seed, this.result, this.growthTime, this.solution, registerRecipes.Serializers.CRYSTALIZING.get()));
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final Ingredient seed;
        private final Fluid solution;
        private final String group;
        private final Item result;
        private final int GrowthTime;
        private final RecipeSerializer<CrystalizingRecipe> serializer;

        public Result(ResourceLocation idIn, String groupIn, Ingredient seedIn, Item resultIn, int growthTimeIn, Fluid fluidIn, RecipeSerializer<CrystalizingRecipe> serializerIn){
            this.id = idIn;
            this.group = groupIn;
            this.seed = seedIn;
            this.result = resultIn;
            this.GrowthTime = growthTimeIn;
            this.solution = fluidIn;
            this.serializer = serializerIn;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            if (!this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }

            json.add("seed", this.seed.toJson());
            json.addProperty("solution", ForgeRegistries.FLUIDS.getKey(this.solution).toString());
            json.addProperty("growthtime", this.GrowthTime);
            json.addProperty("result", ForgeRegistries.ITEMS.getKey(this.result).toString());
        }

        @Override
        public ResourceLocation getId() {
            return id;
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
