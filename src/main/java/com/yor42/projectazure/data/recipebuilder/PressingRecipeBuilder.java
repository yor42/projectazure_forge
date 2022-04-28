package com.yor42.projectazure.data.recipebuilder;

import com.google.gson.JsonObject;
import com.yor42.projectazure.gameobject.crafting.PressingRecipe;
import com.yor42.projectazure.setup.register.registerRecipes;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class PressingRecipeBuilder {

    private final Item result;
    private final Ingredient ingredient, mold;
    private final int processingTime, count;
    private final Advancement.Builder advancementBuilder = Advancement.Builder.advancement();

    public PressingRecipeBuilder(ItemLike result, Ingredient ingredient, Ingredient mold, int count, int processingTime) {
        this.result = result.asItem();
        this.ingredient = ingredient;
        this.mold = mold;
        this.count = count;
        this.processingTime = processingTime;
    }

    public static PressingRecipeBuilder addRecipe(ItemLike result, Ingredient ingredient, Ingredient mold, int count, int processingTime){
        return new PressingRecipeBuilder(result, ingredient, mold, count, processingTime);
    }

    public void build(Consumer<FinishedRecipe> consumerIn) {
        this.build(consumerIn, Registry.ITEM.getKey(this.result));
    }

    /**
     * Adds a criterion needed to unlock the recipe.
     */
    public PressingRecipeBuilder addCriterion(String name, CriterionTriggerInstance criterionIn) {
        this.advancementBuilder.addCriterion(name, criterionIn);
        return this;
    }

    public void build(Consumer<FinishedRecipe> consumerIn, String save) {
        ResourceLocation resourcelocation = Registry.ITEM.getKey(this.result);
        ResourceLocation resourcelocation1 = new ResourceLocation(save);
        if (resourcelocation1.equals(resourcelocation)) {
            throw new IllegalStateException("Recipe " + resourcelocation1 + " should remove its 'save' argument");
        } else {
            this.build(consumerIn, resourcelocation1);
        }
    }

    public void build(Consumer<FinishedRecipe> consumerIn, ResourceLocation id) {
        this.validate(id);
        this.advancementBuilder.parent(new ResourceLocation("recipes/root")).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id)).rewards(AdvancementRewards.Builder.recipe(id)).requirements(RequirementsStrategy.OR);
        consumerIn.accept(new PressingRecipeBuilder.Result(id, "", this.ingredient, this.mold, this.result, this.count, this.processingTime, this.advancementBuilder, new ResourceLocation(id.getNamespace(), "recipes/" + this.result.getItemCategory().getRecipeFolderName() + "/" + id.getPath()), registerRecipes.Serializers.PRESSING.get()));
    }

    private void validate(ResourceLocation id) {
        if (this.advancementBuilder.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final Ingredient ingredient, mold;
        private final String group;
        private final Item result;
        private final int cookingTime, count;
        private final Advancement.Builder advancementBuilder;
        private final ResourceLocation advancementId;
        private final RecipeSerializer<PressingRecipe> serializer;

        public Result(ResourceLocation idIn, String groupIn, Ingredient ingredientIn, Ingredient moldIn, Item resultIn, int count, int cookingTimeIn, Advancement.Builder advancementBuilderIn, ResourceLocation advancementIdIn, RecipeSerializer<PressingRecipe> serializerIn) {
            this.id = idIn;
            this.group = groupIn;
            this.ingredient = ingredientIn;
            this.mold = moldIn;
            this.result = resultIn;
            this.count = count;
            this.cookingTime = cookingTimeIn;
            this.advancementBuilder = advancementBuilderIn;
            this.advancementId = advancementIdIn;
            this.serializer = serializerIn;
        }

        public void serializeRecipeData(JsonObject json) {

            if (!this.group.isEmpty()) {
                json.addProperty("group", this.group);
            }

            json.add("ingredient", this.ingredient.toJson());
            json.add("mold", this.mold.toJson());
            json.addProperty("result", ForgeRegistries.ITEMS.getKey(this.result).toString());
            json.addProperty("count", this.count);
            json.addProperty("cookingtime", this.cookingTime);
        }

        public RecipeSerializer<?> getType() {
            return this.serializer;
        }

        /**
         * Gets the ID for the recipe.
         */
        public ResourceLocation getId() {
            return this.id;
        }

        /**
         * Gets the JSON for the advancement that unlocks this recipe. Null if there is no advancement.
         */
        @Nullable
        public JsonObject serializeAdvancement() {
            return this.advancementBuilder.serializeToJson();
        }

        @Nullable
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}
